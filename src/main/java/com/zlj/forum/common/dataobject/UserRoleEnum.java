package com.zlj.forum.common.dataobject;

/**
 * @author tori
 * 2018/7/30 上午11:23
 */

public enum UserRoleEnum {

    //获取最新持仓
    ROLE_USER_1,

    //获取最新持仓+持仓变动
    ROLE_USER_2,

    //获取最新持仓+持仓变动+变动均价
    ROLE_USER_3,

    //获取最新持仓+持仓变动+变动均价+每天变动记录
    ROLE_USER_4,

    //获取最新持仓+持仓变动+变动均价+每天变动记录+每小时变动记录(48条)
    ROLE_USER_5,

    ROLE_ADMIN
}
