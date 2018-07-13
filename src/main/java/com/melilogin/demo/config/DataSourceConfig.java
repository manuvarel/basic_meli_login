package com.melilogin.demo.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
public class DataSourceConfig {
    
    private static final String JDBC_H2_URL = "jdbc:h2:mem:gshopping;Mode=MySql;DB_CLOSE_DELAY=-1";

    private static final Logger log = Logger.getLogger(DataSourceConfig.class);

    @Value("${test.datasource.host}")
    private String testHost;
    
    @Value("${test.datasource.user}")
    private String testUser;
    
    @Value("${test.datasource.pass}")
    private String testPwd;
    
    @Value("${test.datasource.schema}")
    private String testSchema;
    

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    public DataSource sqlConnection() throws SQLException {
        String scope = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
//        if (scope == null || "default".equalsIgnoreCase(scope)) {
//            log.info("Initializing H2 DataSource");
//            return DataSourceBuilder.create().url(JDBC_H2_URL).build();
//        }
        
        String host = testHost;
        String schema = testSchema;
        String user = testUser;
        String password = testPwd;
        
        String url = "jdbc:mysql://" + host + "/" + schema;
        
        log.info("Initializing DataSource for scope: " + scope);
        
        DataSource ds = DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").url(url).username(user).password(password).build();
        log.info("DataSource = " + ds);
        return ds;
    }
}
