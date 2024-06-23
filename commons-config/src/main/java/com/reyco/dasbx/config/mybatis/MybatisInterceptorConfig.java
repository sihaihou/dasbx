package com.reyco.dasbx.config.mybatis;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisInterceptorConfig {

	@Autowired
	private List<SqlSessionFactory> sqlSessionFactoryList;

	@PostConstruct
    public void addSqlInterceptor() {
		GlobalParametersInterceptor interceptor = new GlobalParametersInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }
}
