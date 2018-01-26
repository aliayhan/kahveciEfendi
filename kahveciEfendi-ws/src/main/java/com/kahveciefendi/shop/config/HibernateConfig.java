package com.kahveciefendi.shop.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hibernate configuration.
 * 
 * @author Ayhan Dardagan
 * 
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfig {

  @Autowired
  private Environment environment;

  /**
   * Session factory settings setting Hibernate settings.
   * 
   * @return Session factory
   */
  @Bean
  public LocalSessionFactoryBean getSessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(getDataSource());
    sessionFactory.setPackagesToScan(new String[] { "com.kahveciefendi.shop.models" });
    sessionFactory.setHibernateProperties(getHibernateProperties());
    return sessionFactory;
  }

  /**
   * Transaction manager settings.
   * 
   * @param sessionFactory
   *          Session factory
   * @return Transaction manager
   */
  @Bean
  public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
    HibernateTransactionManager txManager = new HibernateTransactionManager();
    txManager.setSessionFactory(sessionFactory);
    return txManager;
  }

  /**
   * MySQL data source settings read out of application.properties.
   * 
   * @return Data source
   */
  @Bean
  public DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
    dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
    dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
    dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
    return dataSource;
  }

  private Properties getHibernateProperties() {
    Properties properties = new Properties();
    properties.put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS,
        environment.getRequiredProperty("hibernate.id.new_generator_mappings"));
    properties.put(AvailableSettings.HBM2DDL_AUTO,
        environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
    properties.put(AvailableSettings.SHOW_SQL,
        environment.getRequiredProperty("hibernate.show_sql"));
    properties.put(AvailableSettings.FORMAT_SQL,
        environment.getRequiredProperty("hibernate.format_sql"));
    properties.put(AvailableSettings.STATEMENT_BATCH_SIZE,
        environment.getRequiredProperty("hibernate.batch.size"));
    properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS,
        environment.getRequiredProperty("hibernate.current.session.context.class"));
    return properties;
  }
}
