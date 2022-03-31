package com.volta.datax.core.data.downloader.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.volta.datax.core.utils.FileUtils;

import java.io.*;

public class JsonUtils {

    public static boolean createFile(String jsonFilePath) throws IOException {
        File file = new File(jsonFilePath);
        if (!file.exists()) {
            file.createNewFile();
            return true;
        }
        return false;
    }


    public static String readJsonFileAsString(String filePath) throws IOException {
        String jsonStr = null;
        File jsonFile = new File(filePath);
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
    }

    // 用这个Gson对象去将对象序列化成json字符串, 排除不需要的字段
    public static Gson gsonBuilderObj() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    // ---写入
    public static void saveJSONString(String fileFullPath, String jsonObject) throws IOException {
        FileUtils.writeFile(fileFullPath, jsonObject.toString());
    }

}
