package com.gov.systemservice.config;

import com.gov.systemservice.config.JwtAuthenticationFilter;
import com.gov.systemservice.config.JwtAuthorizationFilter;
import com.gov.systemservice.service.UserService;
import com.gov.systemservice.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserService userService;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义的UserDetailsService和BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 创建JWT认证过滤器
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean(), userService);

        http
            .cors()
            .and()
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/user/login", "/api/user/register", "/api/user/resetpd", "/system/api/user/login", "/system/api/user/register", "/system/api/user/resetpd", "/api/system/user/login", "/api/system/user/register", "/api/system/user/resetpd")
            .permitAll()
            .anyRequest()
            .authenticated();

        // 添加JWT认证过滤器
        http.addFilter(jwtAuthenticationFilter);
        // 添加JWT授权过滤器
        http.addFilterAfter(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
    }
}
