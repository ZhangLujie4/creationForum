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

    private String trade;

    private Integer identity;

    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    public SecurityUser() {
    }

    public SecurityUser(Long userId, String username, String trade, Integer identity, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.trade = trade;
        this.identity = identity;
        this.authorities = authorities;
    }
}
