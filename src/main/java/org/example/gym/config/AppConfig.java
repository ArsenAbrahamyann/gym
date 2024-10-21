package org.example.gym.config;

import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.aspects.AuthAspect;
import org.example.gym.logger.TransactionInterceptor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Hibernate and Spring integration.
 * It handles database configuration, session factory creation, and transaction management.
 */
@Data
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example.gym")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "org.example.gym.repository")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@RequiredArgsConstructor
@Slf4j
@Import({org.springdoc.webmvc.ui.SwaggerConfig.class})
public class AppConfig implements WebMvcConfigurer {

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
     * Configures the application's interceptors.
     * <p>
     * This method adds a {@link TransactionInterceptor} to the interceptor registry,
     * which will be applied to incoming requests for transaction management.
     * </p>
     *
     * @param registry the interceptor registry to which the interceptor will be added
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TransactionInterceptor());
    }

    /**
     * Creates a bean of type {@link AuthAspect}.
     * <p>
     * This aspect is responsible for handling authorization concerns across
     * the application. It can be used to enforce security rules before
     * executing specific methods or controllers.
     * </p>
     *
     * @return an instance of {@link AuthAspect}
     */
    @Bean
    public AuthAspect authAspect() {
        return new AuthAspect();
    }

    /**
     * Configures the DataSource for database connections.
     *
     * @return the configured DataSource
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.debug("Driver class name: {}", driverClassName);
        log.debug("DataSource URL: {}", url);
        log.debug("DataSource username: {}", username);

        dataSource.setDriverClassName(Objects.requireNonNull(driverClassName, "Driver class name must not be null"));
        dataSource.setUrl(Objects.requireNonNull(url, "Database URL must not be null"));
        dataSource.setUsername(Objects.requireNonNull(username, "Username must not be null"));
        dataSource.setPassword(Objects.requireNonNull(password, "Password must not be null"));
        log.info("DataSource configured successfully");
        return dataSource;
    }


    /**
     * Creates the EntityManagerFactory for JPA.
     *
     * @return the configured LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.example.gym.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());

        log.info("EntityManagerFactory created successfully");
        return em;
    }

    /**
     * Configures the PlatformTransactionManager.
     *
     * @return the configured JpaTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * Sets Hibernate properties.
     *
     * @return the configured Properties for Hibernate
     */
    Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", showSql);
        properties.put("spring.jpa.hibernate.ddl-auto", ddlAuto);
        log.debug("Hibernate properties set: {}", properties);
        return properties;
    }

    /**
     * Configures Flyway for database migrations.
     *
     * @param dataSource the DataSource to be used by Flyway
     * @return the configured Flyway instance
     */
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
        log.info("Flyway migration completed successfully");
        return flyway;
    }
}


