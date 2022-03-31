package com.volta.datax.core.data.filter;

public class BaseFilter {

    public static boolean isMatchString(String inputString, String keyword) {
        return inputString.indexOf(keyword) == -1 ? false : true;
    }

    public static void main(String[] args) {
        System.out.println(isMatchString("哇啊所多","阿w索"));
    }
}
