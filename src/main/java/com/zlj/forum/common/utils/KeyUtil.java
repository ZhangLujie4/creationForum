package com.zlj.forum.common.utils;

import java.util.Random;

/**
 * @author tori
 * 2018/8/6 下午10:08
 */
public class KeyUtil {

    /**
     * 生成唯一key
     * 格式 时间戳 + 随机数
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
