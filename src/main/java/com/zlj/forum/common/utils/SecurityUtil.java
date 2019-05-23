package com.zlj.forum.common.utils;

import com.zlj.forum.common.dataobject.SecurityUser;
import com.zlj.forum.common.dataobject.UserRoleEnum;
import com.zlj.forum.web.dataobject.UserDO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author tori
 * 2018/7/30 上午11:43
 */
public class SecurityUtil {

    public static SecurityUser convertUser(UserDO userDO) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(userDO.getType().toString()));

        SecurityUser user = new SecurityUser(userDO.getUid(), userDO.getUsername(), authorities);

        return user;
    }

    public static SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication != null) {
                return (SecurityUser) authentication.getPrincipal();
            }
        } catch (Exception e) {
            // 不作处理
        }
        return null;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getCurrentUserId() {

        return getCurrentUser() == null ? null : getCurrentUser().getUserId();
    }

}
