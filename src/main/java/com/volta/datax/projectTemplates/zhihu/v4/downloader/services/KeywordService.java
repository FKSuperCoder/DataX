package com.volta.datax.projectTemplates.zhihu.v4.downloader.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.projectTemplates.zhihu.v4.models.Question;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.Key;
import java.text.ParseException;
import java.util.ArrayList;

import static com.volta.datax.projectTemplates.zhihu.v4.downloader.seleniumUtils.ZhiHuSeleniumUtils.downloadScrollPage;

public class KeywordService {
    static final String[] KEYWORD_LIST = new String[]{
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
            "自然环境",
            "食品安全",
            "联邦技术移民",
            "税务规划",
            "华侨生",
            "国际生",
            "低分读清华北大",
            "欧盟护照"
    };

    public static void main(String[] args) throws Exception {
        KeywordService keywordService = new KeywordService();
        // 下载所有Question数据到jsonDataFile中
        keywordService.downloadAllQuestions();

        // 补足jsonDataFile中Question的用户信息
        keywordService.addZhiHuUserInfo();
    }

    public void addZhiHuUserInfo() throws IOException {
        String jsonDataFileFullPath = "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\2\\QuestionsData.json";
        QuestionService questionService = new QuestionService();
        questionService.addZhiHuUserInfo(jsonDataFileFullPath);
    }

    /**
     * 将所有关键字下的所有问题页面数据下载到jsonDataFile中
     * @throws Exception
     */
    public void downloadAllQuestions() throws Exception {
        QuestionService questionService = new QuestionService();
        String jsonFileRootPath = "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\2\\";
        for (String keyword : KEYWORD_LIST) {
            String jsonQuestionUrlsFile = jsonFileRootPath + "QuestionUrls-" + keyword + ".json";
            String jsonDataFileFullPath = "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\2\\QuestionsData.json";// 数据文件只需要一个
            questionService.downloadAllQuestion(jsonQuestionUrlsFile, jsonDataFileFullPath);
        }
        return;
    }

    /**
     * 从Keyword字符串数组中，下载每个Keyword，转换为jsonQuestionUrlsFile
     * @param keywordList
     * @throws IOException
     */
    public static void downloadKeywordsQuestionUrl(String[] keywordList) throws IOException {
        for (String keywordStr : keywordList) {
            ArrayList<String> urls = getQuestionPageUrlsByKeyword(keywordStr);
            JsonUtils.saveJSONString("C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\2\\QuestionUrls-" + keywordStr + ".json",
                    new Gson().toJson(urls));
        }
    }

    private static ArrayList<String> getQuestionPageUrlsByKeyword(String keywordStr) {
        Document document = downloadScrollPage("https://www.zhihu.com/search?type=content&q=" + keywordStr);
        Element element = document.getElementsByClass("ListShortcut").get(0);
        element = element.children().get(0);
        element = element.children().get(0);// 根容器
        Elements elements = element.children();
        System.out.println("总数：" + elements.size());
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
        System.out.println("处理后：" + urls.size());
        return urls;
    }

}
