package com.zlj.forum.enums;

import lombok.Getter;

/**
 * @author tori
 * 2018/8/6 下午10:35
 */
@Getter
public enum ResultEnum {

    PARAM_ERROR(1, "参数错误"),

    LOGIN_FAIL(2, "账号或密码错误，登录失败"),

    NO_LOGIN(3, "账户未登录"),

    ROLE_ERROR(4, "您的用户权限不够"),

    ENCODE_ERROR(5, "密码加密失败"),

    JWT_EXPIRED(6, "jwt过期,invalid jwt"),

    DATE_CONVERT_ERROR(7, "时间转换异常")
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
