package com.tscredit.origin.uaa.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date stringToDate(String date, String model) {
        //字符串转时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(model);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String dateToString(Date date, String model) {
        //时间转字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(model);
        return simpleDateFormat.format(date);
    }

}
