package com.pps.springsecuritydemo.repository;

import com.pps.springsecuritydemo.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Classname SysUserRepository
 * @Description
 * @@Author Pupansheng
 * @Date 2019/5/12 21:52
 * @Vestion 1.0
 **/
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}