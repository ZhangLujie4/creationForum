package com.zlj.forum.common.token;

import com.alibaba.fastjson.JSON;
import com.zlj.forum.common.dataobject.SecurityUser;
import com.zlj.forum.common.dataobject.UserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author tori
 * 2018/7/30 下午2:05
 */

@Slf4j
@Component
public class TokenProvider {

    private static final long EXPIRES_IN = 3600 * 1000;

    private static final String AUTHORITY_KEY = "auth";

    private static final String SECRET_KEY = "pxt_secret";

    public String createToken(UserAuthentication userAuthentication) {
        String authorities = userAuthentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority()).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        return Jwts.builder().setId(String.valueOf(userAuthentication.getId()))
                .setSubject(JSON.toJSONString(userAuthentication.getPrincipal()))
                //在这里可以存入序列化后的对象
                .claim(AUTHORITY_KEY, authorities)
                //这里可以存入key和value，value代表权限role的list
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                //秘钥和加密算法
                .setExpiration(new Date(now + EXPIRES_IN))
                //设置过期时间
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
            return true;
        } catch (Exception e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(claims.get(AUTHORITY_KEY).toString().split(","))
                .stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
        SecurityUser user = JSON.parseObject(claims.get(Claims.SUBJECT).toString(), SecurityUser.class);
        user.setAuthorities(authorities);
        UserAuthentication authentication = new UserAuthentication(user);

        return authentication;
    }
}
