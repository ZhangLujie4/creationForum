package com.zlj.forum.common.utils;

import com.zlj.forum.common.exception.ResultException;
import com.zlj.forum.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;

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
}
