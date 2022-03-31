package com.volta.datax.core.data.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactStringFilter {

    public static String genResults(Matcher matcher) {
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    // 手机号
    public static boolean isMatchPhone(String inputString) {
        return extractPhoneSubString(inputString).length() != 0 ? true : false;
    }

    public static String extractPhoneSubString(String inputString) {
        // 查找满足的手机号，多个的话，结果中用，隔开
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[358]\\d{9})|(?:861[358]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(inputString);

        return genResults(matcher);
    }

    // 邮箱
    public static boolean isMatchEmail(String inputString) {
        return extractEmailSubString(inputString).length() != 0 ? true : false;

    }

    public static String extractEmailSubString(String inputString) {
        Pattern pattern = Pattern.compile("\\w+?@\\w+?.com");
        Matcher matcher = pattern.matcher(inputString);
        return genResults(matcher);
    }


    public static boolean isMatchQQ(String inputString) {
        String[] conditions = new String[]{"扣扣", "qq", "QQ", "企鹅号","q","Q"}; // 字符串数组的创建方式
        for (String condition : conditions) {
            if(BaseFilter.isMatchString(inputString, condition)){
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
//        String s1 = "我很牛，我的联系方式是18565728748";
//        System.out.println(isMatchPhoneNumber(s1));

//        String txt = "我很牛，我的联系方式是19@qq.com";
//        String txt = "我很牛，我的联系方式是1912192982@qq.com和19@qq.com";
//        System.out.println(extractEmailSubString(txt));

        String s1 = "我的扣扣";
        System.out.println(isMatchQQ(s1));

    }


}
