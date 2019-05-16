package com.pps.springsecuritydemo.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @Classname MyWebConfig
 * @Description
 * @@Author Pupansheng
 * @Date 2019/5/12 21:58
 * @Vestion 1.0
 **/
@Configuration
public class MyWebConfig  {


    @Autowired
//进行操作的数据源
    private DataSource dataSource;
    @Autowired
    private CodeProperties codeProperties;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //tokenRepository.setCreateTableOnStartup(true); //这个操作是帮我们自定创建一张表，只需操作一次，当数据库这张表已经存在的时候，会报错
        return tokenRepository;

    }
    //验证码
    @Bean(name="captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        properties.setProperty("kaptcha.textproducer.char.string", "123456789");//验证码字符范围
        properties.setProperty("kaptcha.border.color", "245,248,249");//图片边框颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");//字体颜色
        properties.setProperty("kaptcha.textproducer.char.space", "2");//文字间隔
        properties.setProperty("kaptcha.image.width", "125");//图片宽度
        properties.setProperty("kaptcha.image.height", "45");//图片高度
        properties.setProperty("kaptcha.session.key", "code");//session的key
        properties.setProperty("kaptcha.textproducer.char.length", "4");//长度
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");//字体
        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
