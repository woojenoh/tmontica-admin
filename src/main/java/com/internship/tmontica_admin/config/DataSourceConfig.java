package com.internship.tmontica_admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }
}
