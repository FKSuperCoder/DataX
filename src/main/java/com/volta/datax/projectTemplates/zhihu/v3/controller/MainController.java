package com.volta.datax.projectTemplates.zhihu.v3.controller;

import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.AnswerPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.ZhiHuUserPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1.QuestionPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer2.KeywordPData;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Answer;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Question;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.ZhiHuUser;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.AnswerRepository;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.QuestionRepository;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.ZhiHuUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//@Controller
public class MainController {

    @Autowired
    ZhiHuUserRepository zhiHuUserRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Transactional // 事务 javax的 就是jpa的
    @GetMapping("/save")
    @ResponseBody
    public String fn2() throws IOException {

        KeywordPData keywordPData = KeywordPData.readKeywordFromJsonDataFile("C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\1\\移民.json");
        // 问题
        for (QuestionPData questionPData : keywordPData.getQuestionPDataList()) {
            // [Question]
            Question question = questionRepository.findByTitle(questionPData.getQuestionHeader());
            if (question == null) {
                String questionHeader = questionPData.getQuestionHeader();
                String questionContent = questionPData.getQuestionContent();
                String questionUrl = questionPData.getQuestionUrl();
                Date questionDate = questionPData.getQuestionDate();
                question = Question.builder()
                        .title(questionHeader)
                        .content(questionContent)
                        .date(questionDate)
                        .url(questionUrl)
                        .weightScore((long) 0)
                        .build();
                questionRepository.save(question);
            }

            // 回答
            ArrayList<AnswerPData> answerPDataList = questionPData.getAnswerPData();
            for (AnswerPData answerPData : answerPDataList) {
                ZhiHuUserPData answerer = answerPData.getAnswerer();
                String answerContent = answerPData.getAnswerContent();
                Date answerDate = answerPData.getAnswerDate();

                // [ZhiHuUser]
                ZhiHuUser zhiHuUser = zhiHuUserRepository.findByName(answerer.getUserId());
                // 检查数据库是否已经收录
                if (zhiHuUser == null) {
                    zhiHuUser = ZhiHuUser.builder()
                            .name(answerer.getUserId())
                            .build();
                    zhiHuUserRepository.save(zhiHuUser);
                }

                // [Answer]
                // 检查回答是否已经被收录
                Answer answer = answerRepository.getByZhiHuUserIdAndQuestionId(zhiHuUser.getId(), question.getId());
                if (answer == null) {
                    answer = Answer.builder()
                            .answerDate(answerDate)
                            .weightScore((long) 0)
                            .zhiHuUser(zhiHuUser)// 维护关系
                            .question(question)// 维护关系
                            .answerContent(answerContent)
                            .build();
                    answerRepository.save(answer);
                }

            }
        }
        return "OK";
    }

    @GetMapping("/view")
    @ResponseBody
    public String fn3(){


        return "ok";
    }

}
