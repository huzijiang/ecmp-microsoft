package com.hq.ecmp.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Date的parse()与format(), 采用Apache Common Lang中线程安全, 性能更佳的FastDateFormat
 * <p>
 * 注意Common Lang版本，3.5版才使用StringBuilder，3.4及以前使用StringBuffer.
 * <p>
 * 1. 常用格式的FastDateFormat定义, 常用格式直接使用这些FastDateFormat
 * <p>
 * 2. 日期格式不固定时的String<->Date 转换函数.
 * <p>
 * 3. 打印时间间隔，如"01:10:10"，以及用户友好的版本，比如"刚刚"，"10分钟前"
 *
 * @see FastDateFormat#parse(String)
 * @see FastDateFormat#format(Date)
 * @see FastDateFormat#format(long)
 */
public class DateFormatUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_1 = "yyyy/MM/dd";

    public static final String DATE_FORMAT_2 = "yyyyMMdd";

    public static final String DATE_FORMAT_3 = "yyyy.MM.dd";

    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_EN = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm";
    public static final String DATE_TIME_FORMAT_CN_3 = "MM月dd日 HH:mm";

    public static final String DATE_TIME_FORMAT_CN_1 = "yyyy年MM月dd日 HH时mm分";

    public static final String DATE_TIME_FORMAT_CN_2 = "yyyy年MM月dd日 HH时mm分ss秒";

    public static final String DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm";

    public static final String TIME_FORMAT = "HH:mm";

    public static final String MONTH_DAY_FORMAT = "MM-dd";

    public static final String MONTH_DAY_FORMAT_CN = "MM月dd日";

    public static final String MONTH_DAY_FORMAT_EN = "MMdd";

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    public static final String YEAR_MONTH_FORMAT_CN = "yyyy年MM月";

    public static final String YEAR_MONTH_FORMAT_EN = "yyyyMM";

    /**
     * 分析日期字符串, 仅用于pattern不固定的情况.
     * <p>
     * 否则直接使用DateFormats中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     */
    public static Date parseDate(String pattern, String dateString) {
        try {
            return FastDateFormat.getInstance(pattern).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 比较两个日期大小
     * @param start
     * @param end
     * @return -1:start>end,0:end=start,1:start<end
     */
    public static int compareDay(Date start, Date end) {
        if (start==null||end==null){
            return 0;
        }
        Long startDay=Long.parseLong(formatDate(DATE_FORMAT_2,start));
        Long endDay=Long.parseLong(formatDate(DATE_FORMAT_2,end));
        try {
            if (startDay.longValue()>endDay.longValue()){
                return -1;
            }else if(startDay.longValue()<endDay.longValue()){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }
    }

    /**
     * 格式化日期, 仅用于pattern不固定的情况.
     * <p>
     * 否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     */
    public static String formatDate(String pattern, Date date) {
        if (date==null){
            return null;
        }
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化日期, 仅用于不固定pattern不固定的情况.
     * <p>
     * 否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     */
    public static String formatDate(String pattern, long date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化间隔时间
     * 按HH:mm:ss.SSS格式.
     * <p>
     * endDate必须大于startDate，间隔可大于1天，
     *
     * @see DurationFormatUtils
     */
    public static String formatDuration(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDurationHMS(endDate.getTime() - startDate.getTime());
    }

    /**
     * 格式化时间间隔
     * 按HH:mm:ss.SSS格式
     * <p>
     * 单位为毫秒，必须大于0，可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDuration(long durationMillis) {
        return DurationFormatUtils.formatDurationHMS(durationMillis);
    }

    /**
     * 格式化时间间隔
     * 按HH:mm:ss格式
     * <p>
     * endDate必须大于startDate，间隔可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDurationOnSecond(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "HH:mm:ss");
    }

    /**
     * 格式化时间间隔
     * 按HH:mm:ss格式
     * <p>
     * 单位为毫秒，必须大于0，可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDurationOnSecond(long durationMillis) {
        return DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss");
    }

    public static String secToTime(int time) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer hour = time / 3600;
        Integer minute = time / 60 % 60;
//        Integer second = time % 60;
        if(hour<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(hour);
        stringBuilder.append(":");
        if(minute < 10){
            stringBuilder.append("0");
        }
        stringBuilder.append(minute);
        return stringBuilder.toString();
    }

    public static String formatSecond(int totalTime){
        String totalTimeStr="";
        int hour=totalTime/3600;
        int minute = (totalTime % 3600) / 60;
        if (totalTime>=3600){
            totalTimeStr=hour+"小时"+( minute>0?minute + "分":"整");
        }else if (totalTime>=60&&totalTime<3600){
            totalTimeStr=minute+"分";
        }else {
            totalTimeStr="";
        }
        return totalTimeStr;
    }

    public static String formatMinute(int totalTime){
        int hour = totalTime/60;
        int minute = totalTime%60;
        String totalTimeStr="";
        if (totalTime>=60){
            totalTimeStr=hour+"小时"+( minute>0?minute + "分":"整");
        }else {
            totalTimeStr=minute+"分";
        }
        return totalTimeStr;
    }


    public static String getLastDayOfMonth(Date date){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        Date time = cal.getTime();
        return formatDate(DateFormatUtils.DATE_FORMAT, time);

    }

    public static String formaTimestamp(Date date){
        String dateStr = formatDate(DATE_TIME_FORMAT, date);
        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateStr, new ParsePosition(0)).getTime() / 1000;
        return String.valueOf(time);

    }
    public static boolean beforeCurrentDate(Date date){
    	Date currentDate=new Date();
    	if(date.getTime()>currentDate.getTime()){
    		return false;
    	}
    	return true;
    }

     public  static void main(String[] args){
         Date date = parseDate(DATE_TIME_FORMAT, "2020-04-10 22:22:22");
         int i=compareDay(date,new Date());
         if (i==-1){
             System.out.println("大于当前时间");
         }else if (i==0){
             System.out.println("相等");
         }else{
             System.out.println("小于当前时候");
         }

         System.out.println(date.toString());
         System.out.println(new Date().toString());
     }
}
