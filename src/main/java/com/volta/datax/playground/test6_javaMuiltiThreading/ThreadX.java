package com.volta.datax.playground.test6_javaMuiltiThreading;

import lombok.Builder;
import lombok.Data;

public class ThreadX extends Thread {
    private TheadXData theadXData;

    public ThreadX(TheadXData theadXData) {
        this.theadXData = theadXData;
    }

    @Override
    public void run() {
        super.run();
    }

    public void printData() {
        System.out.println(this.theadXData);
    }


    public static void main(String[] args) {
        TheadXData theadXData = TheadXData.builder()
                .i(10)
                .build();
        ThreadX threadX = new ThreadX(theadXData);
        threadX.start();

        theadXData.setI(11);
        threadX.printData();
    }
}

@Data
@Builder
class TheadXData {
    private Integer i;
}

