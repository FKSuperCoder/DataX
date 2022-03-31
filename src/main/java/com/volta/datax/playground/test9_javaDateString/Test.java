package com.volta.datax.playground.test9_javaDateString;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void fn1() throws ParseException {
        String s1 = "编辑于 2019-12-03 13:47";
        String[] strArray = s1.split(" ");
        String dateString = strArray[1]+" "+strArray[2];

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = df.parse(dateString);
        System.out.println(date.getTime()); // getTime()将获得时间戳
    }

    public static void fn2(){
        long timestamp = 1575352020000l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = format.format(timestamp);
        System.out.println(str);
    }

    public static void main(String[] args) throws ParseException {
//        fn1();
        fn2();
    }
}
