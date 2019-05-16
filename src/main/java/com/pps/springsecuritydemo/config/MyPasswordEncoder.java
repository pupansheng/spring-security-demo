package com.pps.springsecuritydemo.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Classname MyPasswordEncoder
 * @Description
 * @@Author Pupansheng
 * @Date 2019/5/15 17:15
 * @Vestion 1.0
 **/
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.equals(s);
    }
}
