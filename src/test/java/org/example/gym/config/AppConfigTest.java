//package org.example.config;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//
//import java.util.Properties;
//import javax.sql.DataSource;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//
//@ExtendWith(MockitoExtension.class)
//public class AppConfigTest {
//
//    @InjectMocks
//    private AppConfig appConfig;
//
//    /**
//     * Sets up the properties for the {@link AppConfig} instance before each test.
//     * This method initializes the database connection properties,
//     * Hibernate dialect, SQL display settings, and DDL auto-configuration
//     * to ensure that the tests run with a consistent in-memory database setup.
//     * The following properties are set:
//     * <ul>
//     *     <li>Driver class name: {@code org.h2.Driver}</li>
//     *     <li>Database URL: {@code jdbc:h2:mem:testdb}</li>
//     *     <li>Username: {@code sa}</li>
//     *     <li>Password: {@code ""}</li>
//     *     <li>Hibernate dialect: {@code org.hibernate.dialect.H2Dialect}</li>
//     *     <li>Show SQL: {@code true}</li>
//     *     <li>DDL auto: {@code update}</li>
//     * </ul>
//     */
//    @BeforeEach
//    public void setupProperties() {
//        appConfig.setDriverClassName("org.h2.Driver");
//        appConfig.setUrl("jdbc:h2:mem:testdb");
//        appConfig.setUsername("sa");
//        appConfig.setPassword("");
//        appConfig.setHibernateDialect("org.hibernate.dialect.H2Dialect");
//        appConfig.setShowSql("true");
//        appConfig.setDdlAuto("update");
//    }
//
//    @Test
//    public void testDataSource() {
//        DataSource dataSource = appConfig.dataSource();
//        assertThat(dataSource).isInstanceOf(DriverManagerDataSource.class);
//
//        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
//        assertDataSourceConfig(driverManagerDataSource);
//    }
//
//    private void assertDataSourceConfig(DriverManagerDataSource dataSource) {
//        assertThat(dataSource.getUrl()).isEqualTo("jdbc:h2:mem:testdb");
//        assertThat(dataSource.getUsername()).isEqualTo("sa");
//        assertThat(dataSource.getPassword()).isEqualTo("");
//    }
//
//    @Test
//    public void testTransactionManager() {
//        SessionFactory sessionFactory = mock(SessionFactory.class);
//        HibernateTransactionManager txManager = appConfig.transactionManager(sessionFactory);
//        assertThat(txManager).isNotNull();
//        assertThat(txManager.getSessionFactory()).isEqualTo(sessionFactory);
//    }
//
//    @Test
//    public void testHibernateProperties() {
//        Properties properties = appConfig.hibernateProperties();
//        assertThat(properties).containsEntry("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        assertThat(properties).containsEntry("hibernate.show_sql", "true");
//        assertThat(properties).containsEntry("spring.jpa.hibernate.ddl-auto", "update");
//    }
//
//    @Test
//    public void testModelMapper() {
//        ModelMapper modelMapper = appConfig.modelMapper();
//        assertThat(modelMapper).isNotNull();
//        assertThat(modelMapper).isInstanceOf(ModelMapper.class);
//    }
//
//}
