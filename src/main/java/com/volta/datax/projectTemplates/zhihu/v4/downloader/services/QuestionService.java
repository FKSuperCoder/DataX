package com.volta.datax.projectTemplates.zhihu.v4.downloader.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.seleniumUtils.SeleniumUtils;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.core.springBoot.DataXSpringBootApplication;
import com.volta.datax.core.utils.Common;
import com.volta.datax.core.utils.DateUtils;
import com.volta.datax.projectTemplates.zhihu.v4.downloader.seleniumUtils.ZhiHuSeleniumUtils;
import com.volta.datax.projectTemplates.zhihu.v4.models.Question;
import com.volta.datax.projectTemplates.zhihu.v4.models.ZhiHuUser;
import com.volta.datax.projectTemplates.zhihu.v4.models.jpaRepository.QuestionRepository;
import org.hibernate.validator.internal.engine.valueextraction.ArrayElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DataXSpringBootApplication.class})
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    private static final String JSON_DATA_FILE_PATH = "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\2\\QuestionsData.json";


    /**
     * 将下载好的Question的JsonDataFile数据读出并存入数据库
     * @throws IOException
     */
    @Test
    public void saveAllQuestion(String jsonDataFileFullPath) throws IOException {
        String jsonStr = JsonUtils.readJsonFileAsString(jsonDataFileFullPath);
        ArrayList<Question> questions = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());
        for (Question question : questions) {
            try {
                questionRepository.save(question);
            } catch (Exception ignored) {
                System.out.println("重复");
            }
        }
    }

    /**
     * 将JsonDataFile下的所有Question补足用户信息,写回文件
     * @throws IOException
     */
    @Test
    public void addZhiHuUserInfo(String jsonDataFileFullPath) throws IOException {
        String jsonStr = JsonUtils.readJsonFileAsString(jsonDataFileFullPath);
        ArrayList<Question> questions = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());

        for (Question question : questions) {
            // 通过question的url页面，获取知乎用户信息
            String url = question.getUrl();
            WebDriver driver = SeleniumUtils.driverInit(SeleniumUtils.CHROME_DRIVER_URL, true, true, true);
            driver.get(url + "/log");
            String pageSrc = driver.getPageSource();
            driver.quit();

            Document document = Jsoup.parse(pageSrc);
            String zhiHuUserName = document.getElementById("zh-question-log-list-wrap").children().get(0).children().get(0).getElementsByTag("a").text();

            // 能够更新，不再需要关注引用的问题
            question.setZhiHuUser(ZhiHuUser.builder()
                    .name(zhiHuUserName)
                    .build());
        }
        JsonUtils.saveJSONString(jsonDataFileFullPath, new Gson().toJson(questions));
    }

    /**
     * 下载urlsJson文件的所有Question页面为JsonDataFile
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void downloadAllQuestion(String jsonQuestionUrlsFile,String jsonDataFileFullPath) throws Exception {

        String jsonStr = null;
        try {
            jsonStr = JsonUtils.readJsonFileAsString(jsonQuestionUrlsFile);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                ArrayList<String> urlList = new ArrayList<>();
                JsonUtils.saveJSONString(jsonQuestionUrlsFile, new Gson().toJson(urlList));
            }
        }
        ArrayList<String> oldUrls = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<String>>() {
        }.getType());
        assert oldUrls != null;
        downloadQuestionMultiThread(oldUrls, 8, jsonDataFileFullPath);
    }


    private static void downloadQuestionMultiThread(ArrayList<String> urls, int threadCount, String jsonDataFileFullPath) throws InterruptedException {
        ArrayList<ArrayList<String>> matrix = Common.reshapeArrayList(urls, threadCount);
        CountDownLatch cdL = new CountDownLatch(threadCount);
        for (ArrayList<String> urlList : matrix) {
            new Thread() {
                @Override
                public void run() {
                    for (String url : urlList) {
                        if (url == null) continue;
                        Question question = null;
                        try {
                            question = getQuestion(url);
                            saveToJsonDataFile(jsonDataFileFullPath, question);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    cdL.countDown();
                }
            }.start();
        }
        cdL.await();
    }

    private synchronized static void saveToJsonDataFile(String jsonDataFullPath, Question question) throws IOException {
        // 读取文件，当文件不存在时自动创建
        String jsonStr = null;
        try {
            jsonStr = JsonUtils.readJsonFileAsString(jsonDataFullPath);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                ArrayList<Question> questions = new ArrayList<>();
                JsonUtils.saveJSONString(jsonDataFullPath, new Gson().toJson(questions));
            }
        }
        ArrayList<Question> oldQuestionList = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());
        assert oldQuestionList != null;
        oldQuestionList.add(question);
        JsonUtils.saveJSONString(jsonDataFullPath, new Gson().toJson(oldQuestionList));
    }

    private static Question getQuestion(String pageUrl) throws ParseException {
        // 获取一个Question页面的源码
        WebDriver driver = ZhiHuSeleniumUtils.driverInit(ZhiHuSeleniumUtils.CHROME_DRIVER_URL, false, false, false);
        driver.get(pageUrl);
        ZhiHuSeleniumUtils.clickCore(driver, "Button Button--plain");
        String pageSrc = driver.getPageSource();
        driver.quit();
        Document document = Jsoup.parse(pageSrc);

        String questionTitle = getQuestionTitle(document);
        String questionContent = getQuestionContent(document);
        Date questionDate = getQuestionDate(document);

        return Question.builder()
                .date(questionDate)
                .content(questionContent)
                .title(questionTitle)
                .url(pageUrl)
                .weightScore(0L)
                .build();
    }

    private static String getQuestionTitle(Document document) {// todo 争议项
        return document.getElementsByClass("QuestionHeader-title").get(0).text();
    }

    private static String getQuestionContent(Document document) {//
        String questionContent = null;
        Elements elements = document.getElementsByClass("QuestionRichText QuestionRichText--expandable");
        if (elements.size() == 0) {//没有描述的问题
            questionContent = "";
        } else {
            questionContent = elements.get(0).getElementsByTag("span").text();
        }
        return questionContent;
    }

    private static Date getQuestionDate(Document document) throws ParseException {
        String questionDateStringRaw = document.getElementsByClass("QuestionPage").get(0).children().tagName("meta").get(6)
                .attr("content");
        String[] strings = questionDateStringRaw.split("T");
        String questionDateString = strings[0] + " " + strings[1].substring(0, 8);
        return DateUtils.dateStringToDateObj(questionDateString, DateUtils.NORMAL_PATTERN);
    }

}
