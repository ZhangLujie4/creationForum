package com.zlj.forum.common.config;

import com.zlj.forum.common.filter.JwtFilter;
import com.zlj.forum.common.token.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author tori
 * 2018/7/30 上午10:29
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtTokenSecurityConfigure extends WebSecurityConfigurerAdapter {


    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/common").permitAll()
                .antMatchers("/api/user1/**")
                .hasAnyAuthority("ROLE_USER_1","ROLE_USER_2","ROLE_USER_3","ROLE_USER_4","ROLE_USER_5")
                .antMatchers("/api/user2/**")
                .hasAnyAuthority("ROLE_USER_2","ROLE_USER_3","ROLE_USER_4","ROLE_USER_5")
                .antMatchers("/api/user3/**")
                .hasAnyAuthority("ROLE_USER_3","ROLE_USER_4","ROLE_USER_5")
                .antMatchers("/api/user4/**")
                .hasAnyAuthority("ROLE_USER_4","ROLE_USER_5")
                .antMatchers("/api/user5/**").hasAuthority("ROLE_USER_5")
                .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
