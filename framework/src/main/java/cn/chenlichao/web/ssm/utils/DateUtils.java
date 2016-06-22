package cn.chenlichao.web.ssm.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间处理工具类
 *
 * <br>author: 陈立朝
 * <br>date: 16/6/16 下午1:07
 * <br>version: V1.0.0
 * <br>Copyright：Copyright © 2016 Jlzx. All rights reserved.
 */
public class DateUtils {

    private DateUtils() {
        // 禁止外部实例化
    }

    private static final ThreadLocal<DateFormat> COMMON_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 通用日期时间格式化方法, 格式为: 2016-01-01 00:00:00
     *
     * @param date 日期时间
     * @return 格式化后的字符串
     */
    public static String format(Date date) {
        return COMMON_FORMAT.get().format(date);
    }

    /**
     * 解析通用日期/日期时间格式的字符串, 格式为: 2016-01-01 00:00:00 或 2016-01-01
     *
     * @param dateString 日期时间字符串
     * @return 解析获得的 {@link java.util.Date}对象
     * @throws ParseException 传入的字符串不符合格式要求时抛出
     */
    public static Date parse(String dateString) throws ParseException {
        try {
            return COMMON_FORMAT.get().parse(dateString);
        } catch (ParseException e) {
            return DATE_FORMAT.get().parse(dateString);
        }
    }

    /**
     * 通用日期格式化方法, 格式为: 2016-01-01
     *
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.get().format(date);
    }

    /**
     * 解析通用日期格式的字符串, 格式为: 2016-01-01
     *
     * @param dateString 日期字符串
     * @return 解析获得的 {@link java.util.Date}对象
     * @throws ParseException 传入的字符串不符合格式要求时抛出
     */
    public static Date parseDate(String dateString) throws ParseException {
        return DATE_FORMAT.get().parse(dateString);
    }
}
