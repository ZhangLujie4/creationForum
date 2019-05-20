package com.zlj.forum.common.dataobject;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author tori
 * 2018/7/30 上午11:00
 */
@Data
public class SecurityUser {


    private Long userId;

    private String username;

    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    public SecurityUser() {
    }

    public SecurityUser(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
    }
}
