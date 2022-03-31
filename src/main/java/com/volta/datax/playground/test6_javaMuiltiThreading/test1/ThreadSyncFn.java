package com.volta.datax.playground.test6_javaMuiltiThreading.test1;

public class ThreadSyncFn {

    synchronized public static <T> void fn(ThreadSharedArrayList<T> data) {
        data.getSharedArrayList().remove(0);
    }

    synchronized public static <T> void fn2(ThreadSharedArrayList<T> data) {
        data.getSharedArrayList().add((T) Thread.currentThread().getName());
    }
}
