package org.example.config;

import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class HibernateConfig {

    private final Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperty(Environment.DRIVER, env.getProperty("spring.datasource.driver-class-name"));
        configuration.setProperty(Environment.URL, env.getProperty("spring.datasource.url"));
        configuration.setProperty(Environment.USER, env.getProperty("spring.datasource.username"));
        configuration.setProperty(Environment.PASS, env.getProperty("spring.datasource.password"));
        configuration.setProperty(Environment.DIALECT, env.getProperty("spring.jpa.properties.hibernate.dialect"));
        configuration.setProperty(Environment.SHOW_SQL, env.getProperty("spring.jpa.show-sql"));
        configuration.setProperty(Environment.HBM2DDL_AUTO, env.getProperty("spring.jpa.hibernate.ddl-auto"));

        // Add your entity classes here
        configuration.addAnnotatedClass(YourEntityClass.class); // Replace with actual entity class

        return configuration.buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }
}
