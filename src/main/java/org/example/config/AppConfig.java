package org.example.config;

import java.util.Properties;
import javax.sql.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@Data
@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    private final Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("org.example.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        // Debug log to check properties
        log.debug("Hibernate properties: {}", hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.debug("Driver class name: {}", env.getProperty("spring.datasource.driver-class-name"));
        log.debug("DataSource URL: {}", env.getProperty("spring.datasource.url"));
        log.debug("DataSource username: {}", env.getProperty("spring.datasource.username"));

        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    Properties hibernateProperties() {
        Properties properties = new Properties();
        String dialect = env.getProperty("spring.jpa.properties.hibernate.dialect");
        String showSql = env.getProperty("spring.jpa.show-sql");
        String formatSql = env.getProperty("spring.jpa.properties.hibernate.format_sql");

        if (dialect != null) {
            properties.put("hibernate.dialect", dialect);
        }
        if (showSql != null) {
            properties.put("hibernate.show_sql", showSql);
        }
        if (formatSql != null) {
            properties.put("hibernate.format_sql", formatSql);
        }
        return properties;
    }
}
