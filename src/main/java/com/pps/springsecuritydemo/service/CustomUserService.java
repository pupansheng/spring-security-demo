package com.pps.springsecuritydemo.service;

import com.pps.springsecuritydemo.entity.SysUser;
import com.pps.springsecuritydemo.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @Classname CustomUserService
 * @Description
 * @@Author Pupansheng
 * @Date 2019/5/12 21:53
 * @Vestion 1.0
 **/
@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    SysUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = userRepository.findByUsername(s);
        if (user == null) {
            System.out.println("密码错误");
            throw new UsernameNotFoundException("用户名不存在");
        }
        System.out.println("s:"+s);
        System.out.println("username:"+user.getUsername()+";password:"+user.getPassword());
        System.out.println(user);
        return user;
    }


}