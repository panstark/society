package com.pan.society.Common.util;

import javax.xml.bind.SchemaOutputResolver;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * create by panstark
 * create date 2019/6/29
 */
public class DateUtil {

    public static String yyMMdd = "yyyyMMdd";

    public static String yy_MM_dd = "yyyy-MM-dd";

    public static String yyMMddHHmmss = "yyyyMMddHHmmss";

    public static String yyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    private static String firstDay;
    private static String lastDay;
    private static String saturday;
    private static String sunday;

    //Date 转 String
    public static String from_Date_to_String(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    //String 转 Date
    public static Date from_String_to_Date(String format, String str) {
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {

        }
        return date;
    }

    //Long 转 String
    public static String from_Long_to_String(String format, long l) {
        Date date = new Date(l);
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    //Long 转 Date
    public static Date from_Long_to_Date(long l) {
        return new Date(l);
    }

    //Date 转 Long
    public static long from_Date_to_Long(Date date) {
        return date.getTime();
    }

    public static Date getFirstDayofMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        firstDay = format.format(cal_1.getTime());
        Date firstDay1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, firstDay);
        return firstDay1;
    }

    public static Date getLastDayofMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("-----1------firstDay:" + firstDay);
        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        lastDay = format.format(cale.getTime());
        Date lastDay1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, lastDay);
        return lastDay1;
    }

    public static Date getFirstDayofWeek() {
        //获取前周第一天
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        sunday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        Date sunday1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, sunday);
        return sunday1;
    }

    public static Date getLastDayofWeek() {
        //获取前周最后一天
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        saturday= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        Date saturday1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, saturday);
        return saturday1;
    }

    public static Date getYesterdayStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        yesterday = yesterday + " 00:00:00";
        Date yesterday1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, yesterday);
        return yesterday1;
    }

    public static Date getYesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        yesterday = yesterday + " 23:59:59";
        Date yesterday1 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, yesterday);
        return yesterday1;
    }

    public static Date getDayOfStart(Date date1, Date date2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long test = Math.abs(time2 - time1);
        long time3 = Math.abs(time1 - test);
        Date result = new Date(time3);
        Date time4 = DateUtil.from_String_to_Date(yyMMdd_HHmmss, timeformat.format(result));
        return time4;
    }

    /**
     * 输入一个日期，如：20180709
     * 返回日期为：20180101
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        String yearDay = year+"-01-01";
        return from_String_to_Date(yy_MM_dd,yearDay);
    }

    /**
     * 输入一个日期，如：20180709
     * 返回日期为：20180101
     * @param date
     * @return
     */
    public static Date getLastYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,-1);
        return calendar.getTime();
    }

    public static String toDBDate(Date date){

        String dbDate = date.toString().replace(".0","");
        return dbDate;
    }

    public static void main(String[] args) {
        System.out.println(getLastYear(new Date()));
        System.out.println(getFirstDayOfYear(new Date()));
        System.out.println(toDBDate(new Date()));
    }
}
