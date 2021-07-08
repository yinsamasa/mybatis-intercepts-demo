package com.lyf.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;


import java.sql.Connection;
import java.util.Properties;



@Intercepts({
        @Signature(type = StatementHandler.class
                    ,method = "prepare"
                    ,args = {Connection.class,Integer.class})
})
@Slf4j
public class FenbiaoPlugin implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object obj = boundSql.getParameterObject();
        String sql = boundSql.getSql();

        StringBuilder stringBuilder = new StringBuilder(sql.length() + 100);
        stringBuilder.append(sql);

        Page page = PageUtil.getPaingParam();
        if(page != null){
            stringBuilder.append(" limit ");
            if(page.getOffset() >= 0){
                stringBuilder.append(""+page.getOffset());
            }
            if(page.getLimit() > 0){
                stringBuilder.append(","+page.getLimit());
            }
        }

        String resSql = stringBuilder.toString();

        //Mybatis 反射工具类
        MetaObject metaObject = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        metaObject.setValue("delegate.boundSql.sql",resSql);


        Object result =invocation.proceed();
        PageUtil.removePagingParam();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        //生成代理对象
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
