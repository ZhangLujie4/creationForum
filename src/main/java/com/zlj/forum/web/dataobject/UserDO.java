package com.zlj.forum.web.dataobject;

import com.zlj.forum.common.dataobject.UserRoleEnum;
import lombok.Data;

import javax.persistence.*;

/**
 * @author tori
 * 2018/8/12 下午10:22
 */
@Entity
@Data
@Table(name = "user")
public class UserDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    /**
     * ROLE_ADMIN 管理员
     * ROLE_USER_1 一级用户
     * ROLE_USER_2 二级用户
     * ROLE_USER_3 三级用户
     * ROLE_USER_4 四级用户
     * ROLE_USER_5 五级用户
     */

    @Enumerated(EnumType.STRING)
    private UserRoleEnum type;

    private String trade;

    private Integer identity;
}