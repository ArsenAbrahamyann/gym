package org.example.gym.config;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AppConfigTest {

    @InjectMocks
    private AppConfig appConfig;

    /**
     * Sets up the necessary environment for testing the AppConfig class.
     * Initializes an instance of AppConfig and sets the required properties
     * for the database connection and Hibernate configuration.
     * This method is called before each test to ensure a fresh state.
     */
    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        appConfig.setDriverClassName("org.h2.Driver");
        appConfig.setUrl("jdbc:h2:mem:testdb");
        appConfig.setUsername("sa");
        appConfig.setPassword("");
        appConfig.setHibernateDialect("org.hibernate.dialect.H2Dialect");
        appConfig.setShowSql("true");
        appConfig.setDdlAuto("update");
    }

    @Test
    void testDataSourceBean() {
        DataSource dataSource = appConfig.dataSource();
        assertNotNull(dataSource, "DataSource should not be null");
    }

    @Test
    void testEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = appConfig.entityManagerFactory();
        assertNotNull(entityManagerFactory, "EntityManagerFactory should not be null");
        assertNotNull(entityManagerFactory.getDataSource(), "EntityManagerFactory's DataSource should not be null");
        assertNotNull(entityManagerFactory.getJpaPropertyMap(), "EntityManagerFactory's JPA Properties should not be null");
    }

    @Test
    void testTransactionManagerBean() {
        PlatformTransactionManager transactionManager = appConfig.transactionManager();
        assertNotNull(transactionManager, "TransactionManager should not be null");
    }
}
