package com.volta.datax.playground.test6_javaMuiltiThreading;

import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    /** 多个线程读写同一list
     *  list中的每一个元素，由任意一个线程取到
     */
    public static void fn() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        System.out.println(arrayList);

        return;
    }

    /**
     *  多个线程读写同一文件
     *  有一个线程在读写文件时，其他线程就别动
     *
     */
    public static void main(String[] args) {
        fn();
    }

}
