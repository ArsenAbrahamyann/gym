package org.example.gym.config;

import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for setting up Hibernate and Spring integration.
 * It handles database configuration, session factory creation, and transaction management.
 */
@Data
@Configuration
@ComponentScan(basePackages = "org.example.gym")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    @Value("${hibernate.db.driver-class-name}")
    private String driverClassName;

    @Value("${hibernate.db.url}")
    private String url;

    @Value("${hibernate.db.username}")
    private String username;

    @Value("${hibernate.db.password}")
    private String password;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${spring.jpa.show-sql}")
    private String showSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;



    /**
     * Configures the Hibernate session factory bean.
     * The session factory is responsible for managing Hibernate sessions,
     * and it integrates Hibernate with Spring's transaction management.
     *
     * @return a configured {@link LocalSessionFactoryBean} object
     */

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("org.example.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        log.debug("Hibernate properties: {}", hibernateProperties());

        return sessionFactory;
    }

    /**
     * Configures the data source to be used by the application.
     * The data source includes the database connection details such as URL, username, and password.
     *
     * @return a configured {@link DataSource} object
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.debug("Driver class name: {}", driverClassName);
        log.debug("DataSource URL: {}", url);
        log.debug("DataSource username: {}", username);

        dataSource.setDriverClassName(Objects.requireNonNull(driverClassName));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Configures the Hibernate transaction manager to handle transactions for Hibernate sessions.
     * The transaction manager integrates Hibernate transactions with Spring's transaction management.
     *
     * @param sessionFactory the Hibernate {@link SessionFactory} used by the transaction manager
     * @return a configured {@link HibernateTransactionManager} object
     */
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    /**
     * Creates and configures Hibernate properties, such as the SQL dialect,
     * SQL formatting, and other database-related settings.
     * These properties are loaded from the application.properties file.
     *
     * @return a {@link Properties} object containing Hibernate configuration settings
     */
    Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", showSql);
        properties.put("spring.jpa.hibernate.ddl-auto", ddlAuto);
        return properties;
    }

    /**
     * Configures and initializes a Flyway bean for database migrations.
     *
     * @param dataSource the data source to be used for connecting to the database.
     * @return a configured and initialized Flyway instance.
     * @see Flyway#configure()
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }

    /**
     * Defines an {@link ModelMapper} bean that will be managed by the Spring container.
     *
     * @return a new instance of {@link ModelMapper}.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
