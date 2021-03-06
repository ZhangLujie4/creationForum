package com.zlj.forum.web.dataobject;

import com.zlj.forum.common.dataobject.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tori
 * 2018/8/12 下午10:22
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uid;

    private String username;

    private String password;

    /**
     * ROLE_ADMIN 管理员
     * ROLE_USER 用户
     */
    @Enumerated(EnumType.STRING)
    private UserRoleEnum type;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtUpdate;
}