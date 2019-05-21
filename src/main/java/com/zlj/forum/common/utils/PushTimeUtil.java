package com.zlj.forum.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

/**
 * 推送时间转换
 *
 * @author tori
 * 2018/8/11 上午9:17
 * V1.0.0
 */


@Slf4j
public class PushTimeUtil {

    public static final long DEFAULT_PUSH_TIME_INTERVAL = 3600 * 24 * 1000 * 15;

    /**
     * 刚刚
     */
    public static final long TIME_ONE = 60 * 1000;

    /**
     * xx分钟之前
     */
    public static final long TIME_TWO = 3600 * 1000;

    /**
     * 一整天的毫秒数
     */
    public static final long WHOLE_DAY = 3600 * 24 * 1000;

    /**
     * 获取用户可以看到的通知时间
     *
     * @param
     * @return
     */
    public static long getPushTimeInterval(Date time) {

        //获取当天凌晨0：00的时间
        long downloadTime = getSomeDay(time);

        long pushTimeInterval = DEFAULT_PUSH_TIME_INTERVAL;

        long currentInterval = System.currentTimeMillis() - downloadTime;
        if (currentInterval < DEFAULT_PUSH_TIME_INTERVAL) {
            pushTimeInterval = currentInterval;
        }

        return pushTimeInterval;
    }

    /**
     * 获取今日凌晨的时间戳(毫秒)
     * @return
     */
    public static long getToday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 获取某天凌晨的时间戳
     * @param time
     * @return
     */
    public static long getSomeDay(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static String getPushTime(Date date) {

        String pushTime = "";
        Date now = new Date();
        long currentTime = System.currentTimeMillis();
        long todayTime = getToday();

        long timeInterval = currentTime - date.getTime();
        int hour = date.getHours();
        String minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : "" + date.getMinutes();
        if (timeInterval < TIME_ONE) {
            pushTime = "刚刚";
        } else if (timeInterval <= TIME_TWO) {
            pushTime = timeInterval/(1000 * 60) + "分钟前";
        } else if (timeInterval <= (currentTime - todayTime)) {
            pushTime = "今天" + hour + ":" + minute;
        } else if (timeInterval <= (currentTime - todayTime + WHOLE_DAY)) {
            pushTime = "昨天" + hour + ":" + minute;
        } else if (timeInterval <= (currentTime - todayTime + WHOLE_DAY * 2)) {
            pushTime = "前天" + hour + ":" + minute;
        } else {
            if (date.getYear() == now.getYear()) {
                pushTime = (date.getMonth()+1) + "月" + date.getDate() + "日"
                        + hour + ":" + minute;
            } else {
                pushTime = date.getYear() + "年" + (date.getMonth()+1) + "月" + date.getDate() + "日"
                        + hour + ":" + minute;
            }
        }

        return pushTime;
    }
}
