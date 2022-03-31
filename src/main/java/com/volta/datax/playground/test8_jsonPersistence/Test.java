package com.volta.datax.playground.test8_jsonPersistence;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class Test {
    public static void fn1() throws IOException {
        String localUri = "C:\\Users\\Alice\\Desktop\\";
        String fileName = "JsonStorage.json";

        JSONObject obj = new JSONObject();
        obj.put("hello", "world");


        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(localUri + fileName), "UTF-8");
        osw.write(obj.toString());
        osw.flush();
        osw.close();
    }
    public static void fn2(){
        String localUri = "C:\\Users\\Alice\\Desktop\\";
        String fileName = "JsonStorage.json";
        String jsonStr = readJsonFile(localUri+fileName);

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        System.out.println(jsonObject.get("hello"));
    }


    // 读取json文件为字符串
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
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


    public static void main(String[] args) throws IOException {
//        fn1();
        fn2();
    }
}
