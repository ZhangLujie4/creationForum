package com.zlj.forum.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author tori
 * 2018/7/30 下午1:02
 */
public class Base64Util {

    private final static Base64 base64 = new Base64();

    public static Base64 getBase64() {
        return base64;
    }
}
