package org.example.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@ExtendWith(MockitoExtension.class)
public class AppConfigTest {

    @Mock
    private Environment env;


    @InjectMocks
    private AppConfig appConfig;


    @Test
    public void testSessionFactoryBeanConfiguration() throws Exception {
        // Mock environment properties for dataSource
        when(env.getProperty("spring.datasource.driver-class-name")).thenReturn("org.h2.Driver");
        when(env.getProperty("spring.datasource.url")).thenReturn("jdbc:h2:mem:testdb");
        when(env.getProperty("spring.datasource.username")).thenReturn("sa");
        when(env.getProperty("spring.datasource.password")).thenReturn("");

        // Mock environment properties for hibernate
        when(env.getProperty("spring.jpa.properties.hibernate.dialect")).thenReturn("org.hibernate.dialect.H2Dialect");
        when(env.getProperty("spring.jpa.show-sql")).thenReturn("true");
        when(env.getProperty("spring.jpa.properties.hibernate.format_sql")).thenReturn("true");

        // Call the sessionFactory() method
        LocalSessionFactoryBean sessionFactory = appConfig.sessionFactory();

        // Verify the sessionFactory was created and properties are set
        assertNotNull(sessionFactory);

        Properties hibernateProps = sessionFactory.getHibernateProperties();
        assertEquals("org.hibernate.dialect.H2Dialect", hibernateProps.getProperty("hibernate.dialect"));
        assertEquals("true", hibernateProps.getProperty("hibernate.show_sql"));
        assertEquals("true", hibernateProps.getProperty("hibernate.format_sql"));
    }

    @Test
    void testDataSource() {
        // Mock environment properties
        when(env.getProperty("spring.datasource.driver-class-name")).thenReturn("org.h2.Driver");
        when(env.getProperty("spring.datasource.url")).thenReturn("jdbc:h2:mem:testdb");
        when(env.getProperty("spring.datasource.username")).thenReturn("sa");
        when(env.getProperty("spring.datasource.password")).thenReturn("");

        // Call the dataSource() method
        DataSource dataSource = appConfig.dataSource();

        // Verify the DataSource configuration
        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
        assertEquals("jdbc:h2:mem:testdb", driverManagerDataSource.getUrl());
        assertEquals("sa", driverManagerDataSource.getUsername());
        assertEquals("", driverManagerDataSource.getPassword());
    }

    @Test
    void testTransactionManager() {
        // Mock the SessionFactory
        SessionFactory sessionFactory = mock(SessionFactory.class);

        // Call the method under test
        HibernateTransactionManager transactionManager = appConfig.transactionManager(sessionFactory);

        // Verify that the transaction manager was properly configured
        assertEquals(sessionFactory, transactionManager.getSessionFactory());
    }

    @Test
    void testHibernateProperties() {
        // Mock environment properties
        when(env.getProperty("spring.jpa.properties.hibernate.dialect")).thenReturn("org.hibernate.dialect.H2Dialect");
        when(env.getProperty("spring.jpa.show-sql")).thenReturn("true");
        when(env.getProperty("spring.jpa.properties.hibernate.format_sql")).thenReturn("true");

        // Call the method under test
        Properties hibernateProperties = appConfig.hibernateProperties();

        // Verify the properties
        assertEquals("true", hibernateProperties.getProperty("hibernate.show_sql"));
        assertEquals("true", hibernateProperties.getProperty("hibernate.format_sql"));
    }
}
