package com.zlj.forum.common.utils;

import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tori
 * 2018/8/13 上午9:20
 */

@Slf4j
public class TimeFormatUtil {

    public static final Integer DEFAULT_TIME = 9;

    public static String formatTime(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static String formatDay(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * time yyyy-MM-dd
     * @param time
     * @return
     */
    public static Date getTodayDefaultHour(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, DEFAULT_TIME);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Date(c.getTime().getTime());
        } catch (ParseException e) {
            log.info("时间转换错误,time = {}", time);
            throw new ResultException(ResultEnum.DATE_CONVERT_ERROR);
        }
    }

    /**
     * time yyyy-MM-dd +1天
     * @param time
     * @return
     */
    public static Date getAfterDefaultHour(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, DEFAULT_TIME);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return new Date(c.getTime().getTime() + 3600 * 1000 * 24);
        } catch (ParseException e) {
            log.info("时间转换错误,time = {}", time);
            throw new ResultException(ResultEnum.DATE_CONVERT_ERROR);
        }
    }

    /**
     * 获取某小时的时间戳
     * @param time
     * @return
     */
    public static Date getSomeHour(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        //c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getSomeHour(long time) {
        Date date = new Date(time);
        return getSomeHour(date);
    }

    /**
     * 获取某天凌晨的时间戳
     * @param time
     * @return
     */
    public static Date getSomeDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getSomeDay(long time) {
        Date date = new Date(time);
        return getSomeDay(date);
    }

    /**
     * 获取前一个小时的time
     * @param time
     * @return
     */
    public static Date getBeforeHour(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static boolean isDefaultTime(Date time) {

        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, DEFAULT_TIME);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        if (time.getTime() == c.getTime().getTime()) {
            return true;
        }

        return false;
    }

    public static Date earlyDayDefaultTime(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, DEFAULT_TIME);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long earlyDay = c.getTime().getTime() - 3600 * 1000 * 24;
        return new Date(earlyDay);
    }

    /**
     * 获取时间节点时间戳
     * @param expireTime
     * @return
     */
    public static Date getInRecTimestamp(int expireTime) {
        // 得到日历
        Calendar calendar = Calendar.getInstance();
        // 设置为前expireTime天
        calendar.add(Calendar.DAY_OF_MONTH, expireTime);
        return calendar.getTime();
    }

    /**
     * 获取时间节点的格式化字符串
     * @param day
     * @return
     */
    public static String getSpecificDayFormat(int day) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, day);
        Date d = calendar.getTime();
        return "'" + date_format.format(d) + "'";
    }

    /**
     * 获取时间节点时间戳
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Timestamp getCertainTimestamp(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return new Timestamp(calendar.getTime().getTime());
    }
}
