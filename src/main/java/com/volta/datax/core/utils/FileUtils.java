package com.volta.datax.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

public class FileUtils {

    // 往指定文件中写字符
    public static void writeFile(String filePath, String content) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
        osw.write(content);
        osw.flush();
        osw.close();
    }

    // 读取html文件为jSoup的doc
    public static Document readHtmlAsJSoupDoc(String filePath) throws IOException {
        File input = new File(filePath);
        return Jsoup.parse(input, "UTF-8");
    }

    // 读取文本文件
    public static String readFileAsString(String filePath) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        String s = null;
        while ((s = bf.readLine()) != null) {//使用readLine方法，一次读一行
            buffer.append(s.trim());
        }
        return buffer.toString();
    }
}
