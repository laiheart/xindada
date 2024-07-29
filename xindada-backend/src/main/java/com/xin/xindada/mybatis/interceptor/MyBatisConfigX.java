//package com.xin.xindada.mybatis.interceptor;
////package com.ection.platform.photo.sticker.mybatis.interceptor;
//
//import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
//import com.baomidou.mybatisplus.core.MybatisConfiguration;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
//@Configuration
//@Slf4j
//public class MyBatisConfigX {
//
//    @Resource
//    private SelectCamelCaseInterceptor selectCamelCaseInterceptor;
//
//    @Bean
//    ConfigurationCustomizer mybatisConfigurationCustomizer() {
//        return configuration -> configuration.addInterceptor(selectCamelCaseInterceptor);
//    }
//}