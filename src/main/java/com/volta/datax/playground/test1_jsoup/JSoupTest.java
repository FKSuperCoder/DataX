package com.volta.datax.playground.test1_jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JSoupTest {

    public static void fn() throws IOException {
        // 页面获取操作
        Connection conn = Jsoup.connect("https://www.zhihu.com/question/68679336");
        Document doc = conn.get();
        // 页面解析操作
        doc.getElementsByClass("List-item");




    }

    public static void main(String[] args) throws IOException {
        fn();


    }

}
