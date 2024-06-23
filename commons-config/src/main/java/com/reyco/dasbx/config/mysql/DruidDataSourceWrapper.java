package com.reyco.dasbx.config.mysql;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@RefreshScope
@Configuration
public class DruidDataSourceWrapper extends DruidDataSource implements InitializingBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8708099714753254571L;
	@Value("${spring.datasource.druid.url}")
    private String url;
    @Value("${spring.datasource.druid.username}")
    private String username;
    @Value("${spring.datasource.druid.password}")
    private String password;
    @Value("${spring.datasource.druid.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.druid.maxIdle}")
    private int maxIdle;
    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;
 
    private String passwordCallbackClassName;
 
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
 
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
 
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
 
    @Override
    public void setUrl(String url) {
        this.url = url;
    }
 
    @Override
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
 
    @Override
    public void setPasswordCallbackClassName(String passwordCallbackClassName) {
        this.passwordCallbackClassName = passwordCallbackClassName;
    }
 
    @Override
    public void afterPropertiesSet() throws Exception {
        super.setUrl(url);
        super.setUsername(username);
        super.setPassword(password);
        super.setDriverClassName(driverClassName);
        super.setInitialSize(initialSize);
        super.setMinIdle(minIdle);
        super.setMaxActive(maxActive);
        super.setMaxWait(maxWait);
        super.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        super.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        super.setValidationQuery(validationQuery);
        super.setTestWhileIdle(testWhileIdle);
        super.setTestOnBorrow(testOnBorrow);
        super.setTestOnReturn(testOnReturn);
        super.setPoolPreparedStatements(poolPreparedStatements);
        super.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        super.setDbType(dbType);
        super.setPasswordCallbackClassName(passwordCallbackClassName);
    }
}