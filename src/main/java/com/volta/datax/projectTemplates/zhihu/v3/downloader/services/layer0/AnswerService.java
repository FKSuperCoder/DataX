

package com.volta.datax.projectTemplates.zhihu.v3.downloader.services.layer0;

import com.volta.datax.core.utils.DateUtils;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.AnswerPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer0.ZhiHuUserPData;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.Date;

public class AnswerService {

    public static AnswerPData resolveSingleAnswer(Element contentItemAnswerItemDIV) throws ParseException {
        return AnswerPData.builder()
                .answerContent(getAnswerContent(contentItemAnswerItemDIV))
                .answerDate(getAnswerDate(contentItemAnswerItemDIV))
                .answerer(getAnswerer(contentItemAnswerItemDIV))
                .build();
    }

    public static ZhiHuUserPData getAnswerer(Element contentItemAnswerItemDIV) {
        String zhiHuUserId = contentItemAnswerItemDIV.getElementsByClass("AuthorInfo").get(0)
                .getElementsByTag("a").text();
        ZhiHuUserPData answerer = ZhiHuUserPData.builder()
                .userId(zhiHuUserId)
                .intro("")
                .build();
        return answerer;
    }

    public static String getAnswerContent(Element contentItemAnswerItemDIV) {
        StringBuilder answerStringBuffer = new StringBuilder();
        for (Element p : contentItemAnswerItemDIV.getElementsByClass("RichContent-inner").get(0).getElementsByTag("p")) {
            answerStringBuffer.append(p.text())
                    .append("\r\n");
        }
        return answerStringBuffer.toString();
    }

    public static Date getAnswerDate(Element contentItemAnswerItemDIV) throws ParseException {
        // 2020-07-21T15:32:55.000Z 第二次处理这样格式的时期
        Elements elements = contentItemAnswerItemDIV.getElementsByClass("ContentItem AnswerItem").get(0).getElementsByTag("meta");
        String dateStringRaw = null;
        for (Element element : elements) {
            if (element.attr("itemprop").equals("dateModified")) {
                dateStringRaw = element.attr("content");
            }
        }
        String[] strings = dateStringRaw.split("T");
        String questionDateString = strings[0] + " " + strings[1].substring(0, 8);
        return DateUtils.dateStringToDateObj(questionDateString, DateUtils.NORMAL_PATTERN);
    }

    public static void main(String[] args) {

    }
}
