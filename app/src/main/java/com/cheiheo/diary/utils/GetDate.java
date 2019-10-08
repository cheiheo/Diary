package com.cheiheo.diary.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 李 on 2017/1/26.
 */
public class GetDate {

    public static StringBuilder getDate(){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.YEAR) + "年");
        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "月");
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");
        return stringBuilder;
    }

    public static StringBuilder getDate(Date date){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        stringBuilder.append(calendar.get(Calendar.YEAR)).append("年");
        stringBuilder.append(calendar.get(Calendar.MONTH) + 1).append("月");
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH)).append("日").append("  ");
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
        stringBuilder.append(calendar.get(Calendar.MINUTE));
        return stringBuilder;
    }

    public static StringBuilder getDate(Date date,int hour, int min){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        stringBuilder.append(calendar.get(Calendar.YEAR)).append("年");
        stringBuilder.append(calendar.get(Calendar.MONTH) + 1).append("月");
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH)).append("日").append("  ");
        stringBuilder.append(hour).append(":");
        stringBuilder.append(min);
        return stringBuilder;
    }
}
