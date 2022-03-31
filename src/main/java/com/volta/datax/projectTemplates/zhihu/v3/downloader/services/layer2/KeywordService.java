package com.volta.datax.projectTemplates.zhihu.v3.downloader.services.layer2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.volta.datax.core.data.downloader.utils.JsonUtils;
import com.volta.datax.core.utils.FileUtils;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer1.QuestionPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.models.layer2.KeywordPData;
import com.volta.datax.projectTemplates.zhihu.v3.downloader.services.layer1.QuestionService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**KeywordService
 * 职能
 *  获取一个搜索页的：
 *      搜索关键字
 *      搜索关键字下的所有问题url
 *  最终返回就行
 */
public class KeywordService {

    public static KeywordPData resolveQuestionList(String keywordStr, String jsonDataFileFullPath) throws Exception {

        // [urls]
        ArrayList<String> urlsRaw = getQuestionListUrlsRaw(keywordStr);
        ArrayList<String> urlsValid = getQuestionUrlsValid(urlsRaw, keywordStr, jsonDataFileFullPath);// 还未下载的Question的urls

        System.out.println("共需要下载：" + urlsRaw.size() + " 还需要下载：" + urlsValid.size());

        // [QuestionList]
        ArrayList<QuestionPData> questionPDataList = getQuestionList(urlsValid, 2, keywordStr, jsonDataFileFullPath);

        // [isComplete]
//        Boolean isComplete = questionList.size() == urlsRaw.size();
        Boolean isComplete = urlsValid.size() == 0;

        KeywordPData currentKeywordPData = new Gson().fromJson(JsonUtils.readJsonFileAsString(jsonDataFileFullPath),
                new TypeToken<KeywordPData>() {
                }.getType());
        return KeywordPData.builder()
                .questionKeyword(keywordStr)
                .questionPDataList(currentKeywordPData.getQuestionPDataList())
                .isComplete(isComplete)
                .build();
    }

    public static ArrayList<QuestionPData> getQuestionList1(ArrayList<String> urls, int threadCount) throws InterruptedException {
        // 多线程组装copyOnWriteArrayList
        CopyOnWriteArrayList<QuestionPData> questionPDataListRaw = new CopyOnWriteArrayList<>();
        CountDownLatch cdL = new CountDownLatch(threadCount);
        ArrayList<ArrayList<String>> matrix = reformArrayList(urls, threadCount);
        for (ArrayList<String> list : matrix) {
            new Thread(() -> {
                for (String url : list) {
                    if (url == null) continue;
                    try {
                        QuestionPData questionPData = QuestionService.resolveSingleQuestionPage(url);
                        if (questionPData.getIsComplete()) questionPDataListRaw.add(questionPData);
                    } catch (Exception e) {
                        System.out.println(url + ": 失败");
                        e.printStackTrace();
                    }
                }
                cdL.countDown();
            }).start();
        }
        cdL.await();// 前面所有子线程执行完后，才会到这一步
        return new ArrayList<>(questionPDataListRaw);
    }

    public static ArrayList<QuestionPData> getQuestionList(ArrayList<String> urls, int threadCount, String keywordStr, String jsonDataFileFullPath) throws InterruptedException {

        // 多线程组装copyOnWriteArrayList
        CopyOnWriteArrayList<QuestionPData> questionPDataListRaw = new CopyOnWriteArrayList<>();
        CountDownLatch cdL = new CountDownLatch(threadCount);
        ArrayList<ArrayList<String>> matrix = reformArrayList(urls, threadCount);
        for (ArrayList<String> list : matrix) {
            new Thread() {
                @Override
                public void run() {
                    for (String url : list) {
                        if (url == null) continue;
                        try {
                            QuestionPData questionPData = QuestionService.resolveSingleQuestionPage(url);
                            questionPDataListRaw.add(questionPData);
                            System.out.println(questionPData.getQuestionUrl() + ": isComplete: " + questionPData.getIsComplete());
                            // 只对Question已完成的，做一次存储操作
                            if (questionPData.getIsComplete()) {
                                updateKeywordAndSave(questionPData, keywordStr, jsonDataFileFullPath);
                            }
                        } catch (Exception e) {
                            System.out.println(url + ": 失败");
                            e.printStackTrace();
                        }
                    }
                    cdL.countDown();
                }
            }.start();
        }
        cdL.await();// 前面所有子线程执行完后，才会放开这一步
        return new ArrayList<>(questionPDataListRaw);
    }

    /**
     * 将urlRaw中已经下载完备的url去掉
     * 同时创建了jsonDataFile todo 后面把这一步逻辑单独提出来防到主方法最前面去
     *
     */
    public static ArrayList<String> getQuestionUrlsValid(ArrayList<String> urlsRaw, String keywordStr, String jsonDataFileFullPath) throws IOException {
        // urls 后续逻辑筛掉完整了的url
        ArrayList<String> urls = new ArrayList<>();
        // 读取jsonDataFile
        String jsonDataStr = null;
        try {
            jsonDataStr = JsonUtils.readJsonFileAsString(jsonDataFileFullPath);
        } catch (Exception e) {
            // jsonData文件不存在时
            if (e instanceof FileNotFoundException) {
                KeywordPData keywordPData = KeywordPData.builder()
                        .isComplete(false)
                        .questionKeyword(keywordStr)
                        .questionPDataList(new ArrayList<>())
                        .build();
                // 可抽取：将keyword写入文件
                jsonDataStr = new Gson().toJson(keywordPData);
                JsonUtils.saveJSONString(jsonDataFileFullPath, jsonDataStr);
            }
        }
        // 可抽取，将文件读成Keyword对象
        KeywordPData oldKeywordPData = new Gson().fromJson(jsonDataStr, new TypeToken<KeywordPData>() {
        }.getType());
        assert oldKeywordPData != null;// todo 现象
        // ---将urlsRaw中，已经完备的url去除
        for (String url : urlsRaw) {
            if (!checkQuestionListUrlIsOk(oldKeywordPData.getQuestionPDataList(), url)) {
                // 如果说，在oldKeyword中已经是完备的url，被发现在urlsRaw中存在，则不加入最后的结果集，筛选掉
                urls.add(url);
            }
        }

        return urls;
    }

    public static boolean checkQuestionListUrlIsOk(ArrayList<QuestionPData> questionPDataList, String url) {
        for (QuestionPData q : questionPDataList) {
            if (q.getIsComplete()) {
                if (q.getQuestionUrl().equals(url)) return true;
            }
        }
        return false;
    }

    public synchronized static void updateKeywordAndSave(QuestionPData questionPData, String keywordStr, String jsonDataFileFullPath) throws IOException {
        // ---更新Keyword
        String jsonStr = JsonUtils.readJsonFileAsString(jsonDataFileFullPath);// todo 预估后期再提升一个级别就要改动
        KeywordPData oldKeywordPData = new Gson().fromJson(jsonStr, new TypeToken<KeywordPData>() {
        }.getType());
        ArrayList<QuestionPData> oldQuestionPDataList = oldKeywordPData.getQuestionPDataList();
        QuestionPData oldQuestionPData = getFromArrayList(oldQuestionPDataList, questionPData.getQuestionUrl());
        // [questionList]
        ArrayList<QuestionPData> questionPDataList = new ArrayList<>(oldQuestionPDataList);
        if (oldQuestionPData != null) {
            // 原list中存在这个Question，直接进行完备性覆盖
            for (int i = 0; i < questionPDataList.size(); i++) {
                QuestionPData currentQ = questionPDataList.get(i);
                if (currentQ.getQuestionUrl().equals(questionPData.getQuestionUrl())) {
                    questionPDataList.set(i, questionPData);
                    break;
                }
            }
        } else {
            // 原list中这个Question还没有存在，是第一次加入，可以直接add
            questionPDataList.add(questionPData);
        }
        // [Keyword]
        KeywordPData keywordPData = KeywordPData.builder()
                .questionPDataList(questionPDataList)
                .questionKeyword(oldKeywordPData.getQuestionKeyword())
                .isComplete(oldKeywordPData.getIsComplete())
                .build();
        // ---写入文件
        jsonStr = new Gson().toJson(keywordPData);
        JsonUtils.saveJSONString(jsonDataFileFullPath, jsonStr);
        System.out.println(questionPData.getQuestionUrl() + ": 已保存");
    }

    public static QuestionPData getFromArrayList(ArrayList<QuestionPData> list, String url) {
        for (QuestionPData q : list) {
            if (q.getQuestionUrl().equals(url)) {
                return q;
            }
        }
        return null;
    }

    public static String getUrlFromArrayList(ArrayList<String> list, String url) {
        for (String urli : list) {
            if (urli.equals(url)) {
                return urli;
            }
        }
        return null;
    }

    public static <T> ArrayList<ArrayList<T>> reformArrayList(ArrayList<T> list, Integer rowCount) {
        int colCount = (int) Math.ceil((float) list.size() / (float) rowCount);
        ArrayList<ArrayList<T>> matrix = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            ArrayList<T> row = new ArrayList<>();
            for (int j = i * colCount; j < (i + 1) * colCount; j++) {
                if (j <= list.size() - 1) {
                    row.add(list.get(j));
                } else {
                    row.add(null);
                }
            }
            matrix.add(row);
        }
        return matrix;
    }

    public static ArrayList<String> getQuestionListUrlsRaw(String keywordStr) throws IOException {
        //        Document doc = SeleniumUtils.resolveSingleQuestionPageToJSoupDoc("https://www.zhihu.com/search?type=content&q="+keywordStr); // 需要先登录
        Document doc = FileUtils.readHtmlAsJSoupDoc("C:\\Users\\Alice\\Desktop\\2.html");

        ArrayList<String> urlsRaw = new ArrayList<>();
        Elements items = doc.getElementsByClass("ListShortcut").get(0).children().get(0).children().get(0).children();
        for (Element item : items) {
            // 注意点：不需要其他类型存在，只提取问题
            if (item.getElementsByClass("ContentItem AnswerItem").size() == 0) {
                continue;
            }
            Elements metas = item.getElementsByClass("ContentItem AnswerItem").get(0).getElementsByClass("ContentItem-title").get(0).children().get(0).getElementsByTag("meta");

            String url = metas.get(0).attr("content");
            String questionName = metas.get(1).attr("content");

            urlsRaw.add(url);
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(urlsRaw); // urlList内去重
        // [urls]
        ArrayList<String> urls = new ArrayList<>(hashSet);

        return urls;
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            KeywordPData keywordPData = resolveQuestionList("移民", "C:\\Users\\Alice\\Desktop\\DataX\\2000DISK\\output\\1\\移民.json");
            if (keywordPData.getIsComplete()) {
                System.out.println("下载完备");
                break;
            } else {
                System.out.println("未下载完备");
            }
        }
//        JsonUtils.saveJSONString(jsonFilePath, new Gson().toJson(keywordObj));
    }

}



