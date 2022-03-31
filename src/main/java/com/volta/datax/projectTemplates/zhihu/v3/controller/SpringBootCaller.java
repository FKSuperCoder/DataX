package com.volta.datax.projectTemplates.zhihu.v3.controller;

import com.volta.datax.core.springBoot.DataXSpringBootApplication;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.AnswerPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1.QuestionPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer2.KeywordPData;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Answer;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.Question;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.ZhiHuUser;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.AnswerRepository;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.QuestionRepository;
import com.volta.datax.projectTemplates.zhihu.v3.filter.models.repository.ZhiHuUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {DataXSpringBootApplication.class})
public class SpringBootCaller {
    @Autowired
    ZhiHuUserRepository zhiHuUserRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    private Long errorCount = 0l;

    // --- 将jsonData转换并存入数据库
    @Test
    public void fn0() throws IOException {
        // 关键字
        KeywordPData keywordPData = KeywordPData.readKeywordFromJsonDataFile("C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\1\\移民.json");
        // 问题
        for (QuestionPData questionPData : keywordPData.getQuestionPDataList()) {
            // [Question]
            Question question = saveQuestion(questionPData);
            // 回答
            ArrayList<AnswerPData> answerPDataList = questionPData.getAnswerPData();
            for (AnswerPData answerPData : answerPDataList) {
                // [ZhiHuUser]
                ZhiHuUser zhiHuUser = saveZhiHuUser(answerPData.getAnswerer().getUserId());
                // [Answer]
                Answer answer = saveAnswer(zhiHuUser.getId(), question.getId(),
                        answerPData,
                        zhiHuUser, question);
            }
        }
        System.out.println("错误数量：" + this.errorCount);

    }

    private Question saveQuestion(QuestionPData questionPData) {
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
        return question;
    }

    private ZhiHuUser saveZhiHuUser(String zhiHuUserName) {
        ZhiHuUser zhiHuUser = zhiHuUserRepository.findByName(zhiHuUserName);
        // 检查数据库是否已经收录
        if (zhiHuUser == null) {
            zhiHuUser = ZhiHuUser.builder()
                    .name(zhiHuUserName)
                    .build();
            zhiHuUserRepository.save(zhiHuUser);
        }
        return zhiHuUser;
    }

    private Answer saveAnswer(Long zhiHuUserId, Long questionId, AnswerPData answerPData, ZhiHuUser zhiHuUser, Question question) {
        Answer answer = null;
        try {
            answer = answerRepository.getByZhiHuUserIdAndQuestionId(zhiHuUserId, questionId);
            if (answer == null) {
                answer = Answer.builder()
                        .answerDate(answerPData.getAnswerDate())
                        .weightScore((long) 0)
                        .zhiHuUser(zhiHuUser)// 维护关系
                        .question(question)// 维护关系
                        .answerContent(answerPData.getAnswerContent())
                        .build();
                answerRepository.save(answer);
            }
        } catch (Exception e) {
            this.errorCount++;
        }
        return answer;
    }


    // ---
    @Test
    public void fn() {
        ArrayList<ZhiHuUser> zhiHuUserList = new ArrayList<>(zhiHuUserRepository.findAll());
        for (ZhiHuUser zhiHuUser : zhiHuUserList) {
            ArrayList<Answer> answerList = new ArrayList<>(zhiHuUser.getAnswerList());
            for (Answer answer : answerList) {
                int score = scoreCalculator(answer.getAnswerContent());
                System.out.println(answer.getAnswerContent() + "得分 >>>：" + score);
            }
        }
    }

    public static int scoreCalculator(String content) {
        if (interrogativeSentimentScore(content, 3)) {
            return keywordScore(content);
        } else {
            return 0;
        }
    }

    public static boolean interrogativeSentimentScore(String content, Integer threshold) {
        String[] interrogativeSentimentString = new String[]{
                "？", "?",
                "啊", "吗", "可以吗",
                "请问", "请教", "求教",
                "不知道", "想知道",
                "我想", "想了解", "我想要",
                "咋样", "怎么样",
                "好不好",
                "能否"
        };
        int score = 0;
        for (String s : interrogativeSentimentString) {
            if (content.contains(s)) score++;
        }
        return score >= threshold;
    }

    public static int keywordScore(String content) {
        String[] keywordList = new String[]{
                "子女教育以及医疗",
                "自由", "出行",
                "海外", "房产", "投资",
                "离岸", "架构",
                "资产", "分配",
                "双重", "国籍",
                "临时", "居留",
                "华侨生", "国际生", "留学",
                "快速入籍",
                "移民监",
                "CRS",
                "语言",
                "华侨", "华人",
                "自然", "环境，食品安全",
                "联邦", "技术移民",
                "税务", "规划",
                "低分", "清华", "北大",
                "欧盟", "护照"
        };
        int score = 0;
        for (String keyword : keywordList) {
            if (content.contains(keyword)) {
                score++;
            }
        }
        return score;
    }

}
