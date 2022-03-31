package com.volta.datax.projectTemplates.zhihu.v5.downloader.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.core.utils.Common;
import com.volta.datax.projectTemplates.zhihu.v5.dao.models.Question;
import com.volta.datax.projectTemplates.zhihu.v5.downloader.seleniumUtils.ZhiHuSeleniumUtils;
import com.volta.datax.projectTemplates.zhihu.v5.downloader.services.DO.QuestionUrlsFileDO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.json.Json;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class KeywordService {

    public final static String[] KEYWORD_LIST = new String[]{
            "子女教育以及医疗",
            "自由出行",
            "海外房产投资",
            "离岸架构",
            "资产分配",
            "双重国籍",
            "临时居留",
            "华侨生",
            "国际生",
            "快速入籍",
            "移民监",
            "CRS",
            "移民语言",
            "华侨和华人",
            "国外自然环境",
            "食品安全",
            "联邦技术移民",
            "税务规划",
            "华侨生",
            "国际生",
            "低分读清华北大",
            "欧盟护照"
    };

    public final static String DATA_FILE_ROOT_PATH = "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\3\\";

    public final static String QUESTION_URLS_FILE_PATH = DATA_FILE_ROOT_PATH + "questionUrlsFile.json";

    public final static String QUESTIONS_DATA_FILE_PATH = DATA_FILE_ROOT_PATH + "dataFile.json";

    public final static int THREAD_COUNT = 2;


    public static void main(String[] args) throws IOException, InterruptedException {
        String jsonStr = JsonUtils.readJsonFileAsString(DATA_FILE_ROOT_PATH+"sample.json");
        ArrayList<Question> questions = new Gson().fromJson(jsonStr,new TypeToken<ArrayList<Question>>(){}.getType());
        System.out.println(questions.size());

        // step1
//        saveQuestionUrlsFile();
        // step2
//        saveDataFile();
        // step3 saveToDB();
        // step4 attachZhiHuUserInfo();
    }


    /**
     * 存储DataFile.json
     * url已经去重
     * @throws IOException
     */
    public static void saveDataFile() throws IOException, InterruptedException {
        // 读取questionUrlsFile
        String jsonStr = JsonUtils.readJsonFileAsString(QUESTION_URLS_FILE_PATH);
        ArrayList<QuestionUrlsFileDO> questionUrlsFileDOList = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<QuestionUrlsFileDO>>() {
        }.getType());

        ArrayList<String> questionUrlsMerge = new ArrayList<>();
        for (QuestionUrlsFileDO questionUrlsFileDO : questionUrlsFileDOList) {
            // 根据对象下的urls，下载所有Question
            ArrayList<String> questionUrls = questionUrlsFileDO.getUrls();
            questionUrlsMerge.addAll(questionUrls);
        }
        // 去重
        questionUrlsMerge = Common.removeArrayListDuplication(questionUrlsMerge);
        QuestionService.saveQuestionsToDataFile(questionUrlsMerge, THREAD_COUNT);
    }

    /**
     * 存储QuestionUrlsFile.json
     * @throws IOException
     */
    public static void saveQuestionUrlsFile() throws IOException {
        // ---获取所有Url数组
        for (String keywordStr : KEYWORD_LIST) {
            ArrayList<String> urls = getQuestionUrlsFromKeywordPage(keywordStr);
            QuestionUrlsFileDO questionUrlsFileDO = QuestionUrlsFileDO.builder()
                    .keyword(keywordStr)
                    .urls(urls)
                    .build();
            // 写入一次文件
            saveOneToQuestionUrlsFile(questionUrlsFileDO);
        }
    }


    // ---

    private static void saveOneToQuestionUrlsFile(QuestionUrlsFileDO questionUrlsFileDO) throws IOException {

        // 更新文件
        String jsonStr = null;
        try {
            jsonStr = JsonUtils.readJsonFileAsString(QUESTION_URLS_FILE_PATH);
        } catch (Exception e) {// 如果文件为空，重新创建
            if (e instanceof FileNotFoundException) {
                ArrayList<QuestionUrlsFileDO> urlList = new ArrayList<>();
                JsonUtils.saveJSONString(QUESTION_URLS_FILE_PATH, new Gson().toJson(urlList));
            }
        }
        ArrayList<QuestionUrlsFileDO> oldQuestionUrlsFileDOList = new Gson().fromJson(jsonStr, new TypeToken<ArrayList<QuestionUrlsFileDO>>() {
        }.getType());

        if (oldQuestionUrlsFileDOList == null) {
            oldQuestionUrlsFileDOList = new ArrayList<>();
        }
        oldQuestionUrlsFileDOList.add(questionUrlsFileDO);

        // 去重
        HashSet<QuestionUrlsFileDO> questionUrlsFileDOHashSet = new HashSet<>(oldQuestionUrlsFileDOList);
        ArrayList<QuestionUrlsFileDO> newQuestionUrlsFileDOList = new ArrayList<>(questionUrlsFileDOHashSet);

        // 写回文件
        JsonUtils.saveJSONString(QUESTION_URLS_FILE_PATH, new Gson().toJson(newQuestionUrlsFileDOList));
    }

    private static ArrayList<String> getQuestionUrlsFromKeywordPage(String keywordStr) {
        String keywordPageUrl = "https://www.zhihu.com/search?type=content&q=" + keywordStr;
        Document document = ZhiHuSeleniumUtils.downloadScrollPage(keywordPageUrl);
        Element element = document.getElementsByClass("ListShortcut").get(0);
        element = element.children().get(0);
        element = element.children().get(0);// 根容器
        Elements elements = element.children();
        ArrayList<String> urls = new ArrayList<>();
        for (Element singleElement : elements) {
            // 第一类Question：带有回答的Question单项
            Elements question1Elements = singleElement.getElementsByClass("QuestionItem-title");
            // 第二类Question：不带有回答的Question单项
            Elements question2Elements = singleElement.getElementsByClass("ContentItem AnswerItem");
            if (question1Elements.size() == 0 && question2Elements.size() == 0) {
                // 不是Question
            } else if (question1Elements.size() > 0) {
                // 是第一类Question
                String urlRaw = question1Elements.get(0).getElementsByTag("a").attr("href");
                String url = "https://www.zhihu.com" + urlRaw;
                urls.add(url);
            } else {
                // 是第二类Question
                Element question2Element = question2Elements.get(0).getElementsByClass("ContentItem-title").get(0);
                String url = question2Element.getElementsByTag("meta").get(0).attr("content");
                urls.add(url);
            }
        }
        System.out.println("---getQuestionUrlsFromKeywordPage: " + keywordStr + " finished");
        return urls;
    }


}
