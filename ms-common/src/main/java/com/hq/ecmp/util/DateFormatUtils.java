package com.hq.ecmp.util;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;

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
     * 格式化日期, 仅用于pattern不固定的情况.
     * <p>
     * 否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     */
    public static String formatDate(String pattern, Date date) {
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


}
