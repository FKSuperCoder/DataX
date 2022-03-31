package com.volta.datax.core.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Common {
    // ---集合

    /** 改变list为矩阵
     * @param list
     * @param rowCount 就是字面意思，往往用在多线程前，等于线程数
     * @param <T>
     * @return
     */
    public static <T> ArrayList<ArrayList<T>> reshapeArrayList(ArrayList<T> list, Integer rowCount) {
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

    // ArrayList去重
    public static <T> ArrayList<T> removeArrayListDuplication(ArrayList<T> arrayList) {
        HashSet<T> hashSet = new HashSet<T>(arrayList);
        return new ArrayList<T>(hashSet);
    }

    /**
     * @param inputString 参数示例: "1,2,3,4,5"
     * @return 返回字符串list
     */
    public static ArrayList<String> generateListFromString(String inputString) {
        String[] strings = inputString.split(",");
        ArrayList<String> arrayList = new ArrayList<>();
        for (String s : strings) {
            arrayList.add(s.trim());
        }
        return arrayList;
    }


    public static void requestURLByBrowser(String url) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                // 创建一个URI实例
                java.net.URI uri = java.net.URI.create(url);
                // 获取当前系统桌面扩展
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                // 判断系统桌面是否支持要执行的功能
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    // 获取系统默认浏览器打开链接
                    dp.browse(uri);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // ---JSON相关

    public static JSONObject stringToJSONObject(String jsonString) {
        return JSONObject.parseObject(jsonString);
    }


    public static String readJSONFileAsJSONString(String filePath) {
        return readJsonFile(filePath);
    }

    private static String readJsonFile(String fileName) {
        String jsonStr;
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // ---date

    /**
     * @param dateString
     * @param formatPattern 例如yyyy-MM-dd HH:mm
     * @return
     * @throws ParseException
     */
    public static Date dateStringToDateObj(String dateString, String formatPattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(formatPattern);
        return df.parse(dateString);
    }

    public static long dateObjToTimestamp(Date date) {
        return date.getTime();
    }

    public static Date timestampToDateObj(String timestamp, String formatPattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(formatPattern);
        String dateString = df.format(timestamp);
        return dateStringToDateObj(dateString, formatPattern);
    }

}
