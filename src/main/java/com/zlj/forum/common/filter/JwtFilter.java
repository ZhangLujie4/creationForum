package com.zlj.forum.common.filter;

import com.zlj.forum.common.token.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author tori
 * 2018/7/30 下午2:06
 */
public class JwtFilter extends GenericFilterBean {

    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String jwt = getAuthHeader(httpServletRequest);
        if (StringUtils.hasText(jwt)) {
            if (tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getAuthHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AUTH_PREFIX)) {
            bearerToken = bearerToken.substring(7);
        }
        return bearerToken;
    }
}
