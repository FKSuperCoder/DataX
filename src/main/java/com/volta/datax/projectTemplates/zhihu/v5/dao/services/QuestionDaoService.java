package com.volta.datax.projectTemplates.zhihu.v5.dao.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.seleniumUtils.SeleniumUtils;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.core.springBoot.DataXSpringBootApplication;
import com.volta.datax.core.utils.Common;
import com.volta.datax.core.utils.DateUtils;
import com.volta.datax.projectTemplates.zhihu.v5.dao.jpaRepository.QuestionRepository;
import com.volta.datax.projectTemplates.zhihu.v5.dao.jpaRepository.ZhiHuUserRepository;
import com.volta.datax.projectTemplates.zhihu.v5.dao.models.Question;
import com.volta.datax.projectTemplates.zhihu.v5.dao.models.ZhiHuUser;
import com.volta.datax.projectTemplates.zhihu.v5.downloader.services.KeywordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DataXSpringBootApplication.class})
public class QuestionDaoService {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ZhiHuUserRepository zhiHuUserRepository;

    @Test
    public void saveQuestion() throws IOException {
        String jsonStr = JsonUtils.readJsonFileAsString(KeywordService.QUESTIONS_DATA_FILE_PATH);
        ArrayList<Question> questions = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());
        for (Question question : questions) {
            saveSingleQuestion(question);
        }
    }

    @Test
    public void attachZhiHuUserInfo() throws IOException {
        String jsonStr = JsonUtils.readJsonFileAsString(KeywordService.DATA_FILE_ROOT_PATH + "test.json");
        ArrayList<Question> questions = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<Question>>() {
        }.getType());

        int index = 1;
        for (Question question : questions) {
            // 通过question的url页面，获取知乎用户信息
            String url = question.getUrl();
            WebDriver driver = SeleniumUtils.driverInit(SeleniumUtils.CHROME_DRIVER_URL, true, true, true);
            driver.get(url + "/log");
            String pageSrc = driver.getPageSource();
            driver.quit();

            Document document = Jsoup.parse(pageSrc);
            String zhiHuUserName = document.getElementById("zh-question-log-list-wrap").children().get(0).children().get(0).getElementsByTag("a").text();


            // 根据question的url查出数据库中的Question
            Question questionDB = questionRepository.findByUrl(question.getUrl());
            questionDB.setZhiHuUser(ZhiHuUser.builder()
                    .name(zhiHuUserName)
                    .build());
            // Question存回数据库， 可以更新ZhiHuUser
            saveSingleQuestion(questionDB);
            System.out.println("--attachZhiHuUserInfo: progress: " + index++ + "/" + questions.size());
        }
    }

    @Test
    public void getValidQuestions() throws IOException, ParseException {
        Date date = DateUtils.dateStringToDateObj("2021-03-25 11:11:11", DateUtils.NORMAL_PATTERN);// 不能多写0
        ArrayList<Question> questions = questionRepository.findAllByDateAfter(date);
        // 使@Expose生效
        JsonUtils.saveJSONString(KeywordService.DATA_FILE_ROOT_PATH + "sample.json", new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(questions));
    }

    // ---
    private void saveSingleQuestion(Question question) {
        try {
            questionRepository.save(question);
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                System.out.println("数据重复插入，已忽略");
            } else {
                System.out.println("save异常");
            }
        }
    }


}