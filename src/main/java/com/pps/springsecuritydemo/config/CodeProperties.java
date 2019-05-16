package com.pps.springsecuritydemo.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Classname CodeProperties
 * @Description  记住密码的时限
 * @@Author Pupansheng
 * @Date 2019/5/15 22:49
 * @Vestion 1.0
 **/
@Configuration
public class CodeProperties {


    private int rememberMeSeconds = 60;

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }


}
