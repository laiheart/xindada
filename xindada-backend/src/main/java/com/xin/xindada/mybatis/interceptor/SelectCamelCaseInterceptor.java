//package com.xin.xindada.mybatis.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.Properties;
//
///**
// * @author lsx
// * @date 2024-07-15
// */
//@Slf4j
//@Component
//@Intercepts({
//        @Signature(
//        type = ParameterHandler.class,
//        method = "setParameters",
//        args = {PreparedStatement.class}),
//        @Signature(type = StatementHandler.class, method = "prepare",
//                args = {Connection.class, Integer.class}),
//        @Signature(
//                type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
//})
//public class SelectCamelCaseInterceptor implements Interceptor {
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        log.info("intercept {}", invocation);
//        return invocation;
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        log.info("plugin {}", target);
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        log.info("setProperties {}", properties);
//    }
//}
