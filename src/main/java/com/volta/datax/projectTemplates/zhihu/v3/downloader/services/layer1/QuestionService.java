package com.volta.datax.projectTemplates.zhihu.v3.downloader.services.layer1;

import com.volta.datax.core.utils.DateUtils;
import com.volta.datax.core.utils.FileUtils;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.AnswerPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1.QuestionPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.seleniumUtils.ZhiHuSeleniumUtils;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.services.layer0.AnswerService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/**QuestionService
 *  职能
 *      解析问题页的url为Question对象
 *          其下属的所有成员都将被解析
 *
 *          对一个问题页的回答能够进行完整性检测
 *      并返回
 */
public class QuestionService {
    static double TOLERANCE_RATIO = 0.7;

    /**
     * 将知乎一个问题的html页面爬取下来，抽取关键信息，不包括图片。转换为自定义的Question对象
     * @param pageUrl 知乎问题页面URL
     * @return 自定义Question对象
     */
    public static QuestionPData resolveSingleQuestionPage(String pageUrl) throws Exception {

        Document document = getQuestionDoc(pageUrl);

        // 问题标题
        String questionHeader = getQuestionHeader(document);

        // 问题描述内容 注意点：存在没有问题描述的情况
        String questionContent = getQuestionContent(document);

        // 问题最后更新日期
        Date date = getQuestionDateUpdate(document);

        // 问题回答List 注意点：存在没有回答的问题
        ArrayList<AnswerPData> answerPData = getQuestionAnswerList(document);

        // 获取问题的总回答数，进行对照
        int answerSupposedLength = getAnswerSupposedCount(document);
        boolean isComplete = answerPData.size() > (answerSupposedLength * TOLERANCE_RATIO);

        return QuestionPData.builder()
                .questionUrl(pageUrl)
                .questionHeader(questionHeader)
                .questionContent(questionContent)
                .questionDate(date)
                .answerPData(answerPData)
                .isComplete(isComplete)
                .build();
    }

    public static Document getQuestionDoc(String pageUrl) throws Exception {
//        return FileUtils.readHtmlAsJSoupDoc("C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\input\\question\\288167788.html");
        return ZhiHuSeleniumUtils.downloadScrollAndClickPage(pageUrl, "Button QuestionRichText-more Button--plain");
    }

    public static String getQuestionHeader(Document document) {// todo 争议项
        return document.getElementsByClass("QuestionHeader-title").get(0).text();
    }

    public static String getQuestionContent1(Document document) {
        String questionContent = null;
        Element e1 = document.getElementsByClass("QuestionRichText QuestionRichText--expandable").get(1).children().get(3);
        if (e1.children().size() != 0) {
            questionContent = e1.children().get(0)
                    .children().get(0)
                    .children().get(0)
                    .children().get(0).text();
        } else {
            questionContent = "";
        }
        return questionContent;
    }

    public static String getQuestionContent(Document document) {//
        String questionContent = null;
        Elements elements = document.getElementsByClass("QuestionRichText QuestionRichText--expandable");
        if (elements.size() == 0) {//没有描述的问题
            questionContent = "";
        } else {
            questionContent = elements.get(0).getElementsByTag("span").text();
        }
        return questionContent;
    }


    public static Date getQuestionDateUpdate(Document document) throws ParseException {
        String questionDateStringRaw = document.getElementsByClass("QuestionPage").get(0).children().tagName("meta").get(6)
                .attr("content");
        String[] strings = questionDateStringRaw.split("T");
        String questionDateString = strings[0] + " " + strings[1].substring(0, 8);
        return DateUtils.dateStringToDateObj(questionDateString, DateUtils.NORMAL_PATTERN);
    }

    public static ArrayList<AnswerPData> getQuestionAnswerList(Document document) throws ParseException {
        ArrayList<AnswerPData> answerPDataList = new ArrayList<>();
        Elements elements = document.getElementsByClass("List-item");// todo 注意先id=QuestionAnswers-answers
        if (elements.size() != 0) {
            for (Element e : elements) {
                Elements contentItemAnswerItemDIVs = e.getElementsByClass("ContentItem AnswerItem");
                if (contentItemAnswerItemDIVs.size() == 0) {
                    continue;
                }
                AnswerPData answerPData = AnswerService.resolveSingleAnswer(contentItemAnswerItemDIVs.get(0));
                answerPDataList.add(answerPData);
            }
        }
        return answerPDataList;
    }

    public static int getAnswerSupposedCount(Document document) {
        String answerLengthStrRaw = document.getElementsByClass("List-headerText").get(0).getElementsByTag("span").text();
        return new Integer(answerLengthStrRaw.substring(0, answerLengthStrRaw.length() - 4));
    }

    public static void main(String[] args) throws Exception {
        Document document = FileUtils.readHtmlAsJSoupDoc("C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\input\\question\\288167788.html");
//        System.out.println(getQuestionContent(document));

        // 问题标题
        String questionHeader = getQuestionHeader(document);

        // 问题描述内容 注意点：存在没有问题描述的情况
        String questionContent = getQuestionContent(document);

        // 问题最后更新日期
        Date date = getQuestionDateUpdate(document);

        // 问题回答List 注意点：存在没有回答的问题
        ArrayList<AnswerPData> answerPData = getQuestionAnswerList(document);

        // 获取问题的总回答数，进行对照
        int answerSupposedLength = getAnswerSupposedCount(document);
        boolean isComplete = answerPData.size() > (answerSupposedLength * TOLERANCE_RATIO);

        System.out.println(questionHeader);
    }

}
