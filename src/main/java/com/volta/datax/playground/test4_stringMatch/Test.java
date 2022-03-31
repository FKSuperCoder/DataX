package com.volta.datax.playground.test4_stringMatch;

import java.util.regex.Pattern;

public class Test {

    static String S1 = "我出生于1988.1.1（1988年1月1日，1988/01/01，1988-01-01），" +
            "我将基于swing实现一个图形化表格。" +
            "今天是2022年4月1日。";

    static String S2 = "我出生于1988.1.1（1988年1月1日，1988/01/01，1988-01-01），" +
            "我将基于swing实现一个图形化表格。" +
            "今天是2022年4月1日。";

    static String S3 = "我出生于1988.1.1（1988年1月1日，1988/01/01，1988-01-01），" +
            "我将基于swing实现一个图形化表格。" +
            "今天是2022年4月1日。";

    // 匹配作者生日
    public static void fn1() {
        String regex = "\\D*(\\d{4})";
        boolean isMatch = Pattern.matches(regex, S1);
        System.out.println(isMatch);
    }

    public static void fn2() {
        boolean isMatch = Pattern.matches(".*", S1);
        System.out.println(isMatch);
    }

    public static void main(String[] args) {
        fn1();
    }

}
