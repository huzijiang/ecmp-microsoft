package com.hq.ecmp.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
    public static final String TIME_FORMAT_1 = "HH";

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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date dateTime = null;
            try {
                dateTime = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String timeStamp2Date(String seconds,String format) {
        if(StringUtils.isBlank(seconds)){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = DATE_TIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (seconds.length()==13){
            return sdf.format(new Date(Long.valueOf(seconds)));
        }else{
            return sdf.format(new Date(Long.valueOf(seconds+"000")));
        }
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
     * 比较两个时间大小
     * @param start  hh:mm
     * @param end   hh:mm
     * @return -1:start>end,0:end=start,1:start<end
     */
    public static int compareTime(String start, String end) {
        if (StringUtils.isBlank(start)||StringUtils.isBlank(end)){
            return 0;
        }
        Long startDay=parseDate(TIME_FORMAT,start).getTime();
        Long endDay=parseDate(TIME_FORMAT,end).getTime();
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
     * 比较两个日期+时间大小
     * @param startDate  yyyy-MM-dd HH:mm
     * @param endDate  yyyy-MM-dd HH:mm
     * @return -1:start>end,0:end=start,1:start<end
     */
    public static int compareDayAndTime(Date startDate, Date endDate) {
        if (startDate==null||endDate==null){
            return 0;
        }
        String start= new SimpleDateFormat(DATE_TIME_FORMAT_1). format(startDate);
        String end= new SimpleDateFormat(DATE_TIME_FORMAT_1). format(endDate);
        Long startDay=parseDate(DATE_TIME_FORMAT_1,start).getTime();
        Long endDay=parseDate(DATE_TIME_FORMAT_1,end).getTime();
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
     * 比较两个日期+时间大小
     * @param start  yyyy-MM-dd
     * @param end  yyyy-MM-dd
     * @return -1:start>end,0:end=start,1:start<end
     */
    public static int compareDate(String start, String end) {
        if (StringUtils.isBlank(start)||StringUtils.isBlank(end)){
            return 0;
        }
        Long startDay=parseDate(DATE_FORMAT,start).getTime();
        Long endDay=parseDate(DATE_FORMAT,end).getTime();
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
    
	public static boolean compareDateInterval(Date date, int minute) {
		Date currentDate = new Date();
		long diff = currentDate.getTime() - date.getTime();
		long interval = diff / 60 / 1000;
		return interval < minute;
	}
	
	public static Long getDateToWaitInterval(Date date){
		Date currentDate = new Date();
		long diff = currentDate.getTime() - date.getTime();
		long interval = diff / 60 / 1000;
		return Long.valueOf(interval);
	}

	public static String getWeek( Date today){
        String week = "";
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            int weekday = c.get(7);
            if (weekday == 1) {
                week = "0";
            } else if (weekday == 2) {
                week = "1";
            } else if (weekday == 3) {
                week = "2";
            } else if (weekday == 4) {
                week = "3";
            } else if (weekday == 5) {
                week ="4";
            } else if (weekday == 6) {
                week ="5";
            } else if (weekday == 7) {
                week = "6";
            }
            return week;
	}

    public static String addDay(String s, int n) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);//增加一天
            //cd.add(Calendar.MONTH, n);//增加一个月

            return sdf.format(cd.getTime());

        } catch (Exception e) {
            return null;
        }
    }

    public static String YEAR_REGEX = "^\\d{4}$";
    public static String MONTH_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}$";
    public static String DATE_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
    //根据开始结束时间的格式分割时间
    public static List<String> sliceUpDateRange(String startDate, String endDate) {
        List<String> rs = new ArrayList<>();
        try {
            int dt = Calendar.DATE;
            String pattern = "yyyy-MM-dd";
            String[] temp = getPattern(startDate);
            pattern = temp[0];
            dt = Integer.parseInt(temp[1]);
            Calendar sc = Calendar.getInstance();
            Calendar ec = Calendar.getInstance();
            sc.setTime(parseDate(pattern,startDate));
            ec.setTime(parseDate(pattern,endDate));
            while(sc.compareTo(ec) < 1){
                rs.add(formatDate(pattern,sc.getTime()));
                sc.add(dt, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 计算两个日期的相差天数，年 月 日 格式都可
     * @param startDate 参数示例 2020、2020-01、2020-01-01
     * @param endDate
     * @return
     */
    public static int getDayDuration(String startDate, String endDate){
        int dayDuration=0;
        List<String> list = DateFormatUtils.sliceUpDateRange(startDate,endDate);
        for (String date: list) {
            Calendar calendar = Calendar.getInstance();
            if(date.matches(DateFormatUtils.MONTH_REGEX)) {
                date=date+"-01-01";
                calendar.setTime(DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT,date));
                dayDuration +=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
            if(date.matches(DateFormatUtils.YEAR_REGEX)) {
                date=date+"-01";
                calendar.setTime(DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT,date));
                dayDuration +=calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            if(date.matches(DateFormatUtils.DATE_REGEX)) {
                dayDuration ++;
            }
        }
        return dayDuration;
    }

    public static String[] getPattern(String date){
        String[] x = new String[2];
        if(date.matches(YEAR_REGEX)) {
            x[0] = "yyyy";
            x[1] = String.valueOf(Calendar.YEAR);
        } else if(date.matches(MONTH_REGEX)) {
            x[0] = "yyyy-MM";
            x[1] = String.valueOf(Calendar.MONTH);
        } else if(date.matches(DATE_REGEX)) {
            x[0] = "yyyy-MM-dd";
            x[1] = String.valueOf(Calendar.DATE);
        }
        return x;
    }


     public  static void main(String[] args){
//         Date date = parseDate(DATE_TIME_FORMAT, "2020-04-10 22:22:22");
//         System.out.println(date);
//         System.out.println(date.getTime());
         String date1="2020-04-30";
         System.out.println("当前时间"+date1);
         System.out.println("加的日期"+addDay(date1, 1));
         System.out.println("减的日期"+addDay(date1, -1));

//         int i=compareTime("2020-05-06 19:30:50","2020-05-07 19:30:50");
//         if (i==-1){
//             System.out.println("大于当前时间");
//         }else if (i==0){
//             System.out.println("相等");
//         }else{
//             System.out.println("小于当前时候");
//         }

     }
}
