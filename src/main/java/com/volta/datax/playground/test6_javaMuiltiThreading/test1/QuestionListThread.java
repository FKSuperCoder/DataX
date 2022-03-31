package com.volta.datax.playground.test6_javaMuiltiThreading.test1;

import java.util.ArrayList;

public class QuestionListThread extends Thread {
    private ThreadSharedArrayList threadSharedArrayList;

    public QuestionListThread(ThreadSharedArrayList threadSharedArrayList) {
        super();
        this.threadSharedArrayList = threadSharedArrayList;
    }

    @Override
    public void run() {
        super.run();


        /**
         * 遍历urls列表
         */
        ThreadSyncFn.fn2(this.threadSharedArrayList);
    }

    public static void main(String[] args) throws InterruptedException {

        ArrayList<String> urls = new ArrayList<>();
        urls.add("一");
        urls.add("二");
        urls.add("三");
        urls.add("四");
        ThreadSharedArrayList threadSharedArrayList = new ThreadSharedArrayList(urls);// 公用的数据

        QuestionListThread th1 = new QuestionListThread(threadSharedArrayList);
        QuestionListThread th2 = new QuestionListThread(threadSharedArrayList);
        QuestionListThread th3 = new QuestionListThread(threadSharedArrayList);
        QuestionListThread th4 = new QuestionListThread(threadSharedArrayList);
        
        new Thread(th1, "线程1").start();
        new Thread(th2, "线程2").start();
        new Thread(th3, "线程3").start();
        new Thread(th4, "线程4").start();

        sleep(500);
        System.out.println(threadSharedArrayList.getSharedArrayList());

    }
}
