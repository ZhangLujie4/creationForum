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

        SecurityUser user = new SecurityUser(userDO.getId(), userDO.getUsername(), userDO.getTrade(), userDO.getIdentity(), authorities);

        return user;
    }

    public static SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (SecurityUser) authentication.getPrincipal();
        }

        return null;
    }

    public static UserRoleEnum integer2Enum(Integer identity) {

        UserRoleEnum result = null;

        switch (identity) {
            case 6:
                result = UserRoleEnum.ROLE_ADMIN;
                break;
            case 1:
                result = UserRoleEnum.ROLE_USER_1;
                break;
            case 2:
                result =  UserRoleEnum.ROLE_USER_2;
                break;
            case 3:
                result =  UserRoleEnum.ROLE_USER_3;
                break;
            case 4:
                result = UserRoleEnum.ROLE_USER_4;
                break;
            case 5:
                result = UserRoleEnum.ROLE_USER_5;
                break;
        }

        return result;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getCurrentUserId() {

        return getCurrentUser() == null ? null : getCurrentUser().getUserId();
    }

    public static String getCurrentTrade() {
        return getCurrentUser() == null ? null : getCurrentUser().getTrade();
    }

}
