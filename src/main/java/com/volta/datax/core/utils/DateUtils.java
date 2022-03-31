package com.volta.datax.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
    public static String NORMAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param dateString
     * @param formatPattern 例如yyyy-MM-dd HH:mm
     * @return
     * @throws ParseException
     */
    public static Date dateStringToDateObj(String dateString, String formatPattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(formatPattern);
        return df.parse(dateString);
    }

    public static long dateObjToTimestamp(Date date) {
        return date.getTime();
    }

    public static Date timestampToDateObj(String timestamp, String formatPattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(formatPattern);
        String dateString = df.format(timestamp);
        return dateStringToDateObj(dateString, formatPattern);
    }

}
