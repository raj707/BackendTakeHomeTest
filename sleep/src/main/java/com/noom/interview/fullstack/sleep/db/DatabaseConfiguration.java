package com.noom.interview.fullstack.sleep.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;

@Configuration
@Profile("!" + UNIT_TEST_PROFILE)
class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

    @Bean
    public Connection dbConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
            DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
