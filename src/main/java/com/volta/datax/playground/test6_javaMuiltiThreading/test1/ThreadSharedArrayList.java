package com.volta.datax.playground.test6_javaMuiltiThreading.test1;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ThreadSharedArrayList<T> {
    private ArrayList<T> sharedArrayList;

    public ThreadSharedArrayList(ArrayList<T> sharedArrayList) {
        this.sharedArrayList = sharedArrayList;
    }


}
