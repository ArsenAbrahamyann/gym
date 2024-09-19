package org.example.config;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

@ExtendWith(MockitoExtension.class)
public class AppConfigTest {

    @Mock
    private Environment env;

    @InjectMocks
    private AppConfig appConfig;

    @Test
    public void testDataSource() {
        when(env.getProperty("spring.flyway.driver-class-name")).thenReturn("org.h2.Driver");
        when(env.getProperty("spring.flyway.url")).thenReturn("jdbc:h2:mem:testdb");
        when(env.getProperty("spring.flyway.username")).thenReturn("sa");
        when(env.getProperty("spring.flyway.password")).thenReturn("");

        DataSource dataSource = appConfig.dataSource();
        assertThat(dataSource).isInstanceOf(DriverManagerDataSource.class);

        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
        assertThat(driverManagerDataSource.getUrl()).isEqualTo("jdbc:h2:mem:testdb");
        assertThat(driverManagerDataSource.getUsername()).isEqualTo("sa");
    }

    @Test
    public void testTransactionManager() {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        HibernateTransactionManager txManager = appConfig.transactionManager(sessionFactory);
        assertThat(txManager).isNotNull();
        assertThat(txManager.getSessionFactory()).isEqualTo(sessionFactory);
    }

    @Test
    public void testHibernateProperties() {
        when(env.getProperty("spring.jpa.properties.hibernate.dialect")).thenReturn("org.hibernate.dialect.H2Dialect");
        when(env.getProperty("spring.jpa.show-sql")).thenReturn("true");
        when(env.getProperty("spring.jpa.properties.hibernate.format_sql")).thenReturn("true");
        when(env.getProperty("spring.jpa.hibernate.ddl-auto")).thenReturn("update");

        Properties properties = appConfig.hibernateProperties();
        assertThat(properties).containsEntry("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        assertThat(properties).containsEntry("hibernate.show_sql", "true");
        assertThat(properties).containsEntry("hibernate.format_sql", "true");
        assertThat(properties).containsEntry("spring.jpa.hibernate.ddl-auto", "update");
    }

    @Test
    public void testModelMapper() {
        ModelMapper modelMapper = appConfig.modelMapper();
        assertThat(modelMapper).isNotNull();
        assertThat(modelMapper).isInstanceOf(ModelMapper.class);
    }
}
