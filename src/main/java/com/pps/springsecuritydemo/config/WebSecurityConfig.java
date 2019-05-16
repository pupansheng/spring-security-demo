package com.pps.springsecuritydemo.config;

import com.pps.springsecuritydemo.config.fiter.KaptchaAuthenticationFilter;
import com.pps.springsecuritydemo.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname WebSecurityConfig
 * @Description
 * @@Author Pupansheng
 * @Date 2019/5/12 22:09
 * @Vestion 1.0
 **/
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private  CodeProperties codeProperties;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserService);
        authenticationProvider.setPasswordEncoder(new MyPasswordEncoder()); // 设置密码加密方式
        return authenticationProvider;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //登录验证  密码解析等
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService).passwordEncoder(new MyPasswordEncoder());//把自定义的类加入

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

  //自定义拦截


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();

        //关闭csrf防护
        http.csrf().disable();

        //验证码验证

        http.addFilterBefore(new KaptchaAuthenticationFilter("/spring/login", "/login.html"), UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests()
                .antMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN')");

        //非ajax登录
    /*    http.formLogin().loginPage("/login.html").defaultSuccessUrl("/index.html").
               loginProcessingUrl("/spring/login") ;*/


        //ajax登录

        http.formLogin()   //基于 Form 表单登录验证
                .loginPage("/login.html").loginProcessingUrl("/spring/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    //用户名和密码正确执行
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal != null && principal instanceof UserDetails) {
                            httpServletResponse.setContentType("application/json;charset=utf-8");
                            PrintWriter out = httpServletResponse.getWriter();
                            out.write("{\"status\":false,\"message\":\"登录成功\"}");
                            out.flush();
                            out.close();
                        }
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    //用户名密码错误执行
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException, IOException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write("{\"status\":true,\"message\":\"用户名或密码错误\"}");
                        out.flush();
                        out.close();
                    }
                });




        //权限不足跳转得页面
        http .exceptionHandling().accessDeniedPage("/error.html");

        /*记住密码选项 注意首先得先创建一个表 建库语句：//源码中创建表的语句
        create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)*/
        http.rememberMe()
                .tokenRepository(persistentTokenRepository)
                .tokenValiditySeconds(codeProperties.getRememberMeSeconds())
                .userDetailsService(customUserService);



        }

    /**
     * 认证信息管理
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService);
        auth.authenticationProvider(authenticationProvider());
    }

}