package com.volta.datax.projectTemplates.zhihu.v5.downloader.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.core.utils.Common;
import com.volta.datax.core.utils.DateUtils;
import com.volta.datax.projectTemplates.zhihu.v5.downloader.seleniumUtils.ZhiHuSeleniumUtils;
import com.volta.datax.projectTemplates.zhihu.v5.dao.models.Question;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class QuestionService extends KeywordService {

    public static void main(String[] args) {

    }


    public static void saveQuestionsToDataFile(ArrayList<String> questionUrls, int threadCount) throws InterruptedException {
        downloadQuestionMultiThread(questionUrls, threadCount);
    }

    // ---供dao层调用
    public static void fn(){

    }
    // ---

    private static void downloadQuestionMultiThread(ArrayList<String> urls, int threadCount) throws InterruptedException {
        System.out.println("---downloadQuestionMultiThread: urlsLength: " + urls.size());
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
                            // 防止这个url的question重复下载; todo 后期修改为，是否更新;但好像不必要
                            if (isQuestionDownloaded(url)) {
                                System.out.println(url + ": 已下载，忽略。");
                                continue;
                            }
                            question = getQuestion(url);
                            saveToJsonDataFile(question);
                            System.out.println(Thread.currentThread().getName() + ": saveOne: " + question.getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    cdL.countDown();
                    System.out.println(Thread.currentThread().getName() + ": finished");
                }
            }.start();
        }
        cdL.await();
        System.out.println("---downloadQuestionMultiThread: finished");
    }

    private static boolean isQuestionDownloaded(String questionUrl) throws IOException {
        // ---读取文件
        String jsonStr = null;
        try {
            jsonStr = JsonUtils.readJsonFileAsString(QUESTIONS_DATA_FILE_PATH);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                ArrayList<Question> questions = new ArrayList<>();
                JsonUtils.saveJSONString(QUESTIONS_DATA_FILE_PATH, new Gson().toJson(questions));
            }
        }
        ArrayList<Question> oldQuestionList = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());
        // ---检查
        if (oldQuestionList == null || oldQuestionList.size() == 0) {
            return false;
        }
        for (Question question : oldQuestionList) {
            if (question.getUrl().equals(questionUrl)) {
                return true;
            }
        }
        return false;
    }

    private synchronized static void saveToJsonDataFile(Question question) throws IOException {
        // 读取文件，当文件不存在时自动创建
        String jsonStr = null;
        try {
            jsonStr = JsonUtils.readJsonFileAsString(QUESTIONS_DATA_FILE_PATH);
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                ArrayList<Question> questions = new ArrayList<>();
                JsonUtils.saveJSONString(QUESTIONS_DATA_FILE_PATH, new Gson().toJson(questions));
            }
        }
        ArrayList<Question> oldQuestionList = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());
        assert oldQuestionList != null;
        oldQuestionList.add(question);
        JsonUtils.saveJSONString(QUESTIONS_DATA_FILE_PATH, new Gson().toJson(oldQuestionList));
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
