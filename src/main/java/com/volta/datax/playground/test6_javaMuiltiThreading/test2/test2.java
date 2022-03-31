package com.volta.datax.playground.test6_javaMuiltiThreading.test2;

import lombok.SneakyThrows;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class test2 {

    public static void fn() throws InterruptedException {
        int threadCount = 5;
        CountDownLatch cdL = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread() {
                @SneakyThrows
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        String ok = download();
                        alterFile();
                    }

                    cdL.countDown();
                }
            }.start();
        }
        cdL.await();
    }

    public static void fn2() throws InterruptedException {
        for (int j = 0; j < 50; j++) {
            String ok = download();
            alterFile();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = new Date().getTime();

        fn();
//        fn2();

        long endTime = new Date().getTime();
        System.out.println("本程序运行 " + (endTime - startTime)
                + " 毫秒完成。");
    }

    public static String download() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": 下载资源中");
        if (Thread.currentThread().getName().equals("Thread-0")) {
            Thread.sleep(1000);
        } else {
            Thread.sleep(3000);

        }
        return "ok";
    }

    public synchronized static void alterFile() {
//        try {
//            String fileFullPath = "C:\\Users\\Alice\\Desktop\\1.json";
//            // 读写文件，看看这样的并发操作会不会报错
//            String jsonStr = JsonUtils.readJsonFileAsString(fileFullPath);
//            KeywordPData oldKeywordPData = new Gson().fromJson(jsonStr, new TypeToken<KeywordPData>() {
//            }.getType());
//            oldKeywordPData.setQuestionKeyword(oldKeywordPData.getQuestionKeyword() + "1");
//            // 可抽取：将keyword写入文件
//            String jsonDataStr = new Gson().toJson(oldKeywordPData);
//            JsonUtils.saveJSONString(fileFullPath, jsonDataStr);
//            System.out.println(Thread.currentThread().getName() + ": 保存成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
