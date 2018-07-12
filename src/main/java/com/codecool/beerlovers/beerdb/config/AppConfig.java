package com.codecool.beerlovers.beerdb.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.codecool.beerlovers.beerdb")
@PropertySource("classpath:persistence-mysql.properties")
@EnableTransactionManagement
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public EntityManager entityManager() throws PropertyVetoException {
        return sessionFactory().getObject().createEntityManager();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.codecool.beerlovers.beerdb");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
        securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));
        securityDataSource.setUser(env.getProperty("jdbc.user"));
        securityDataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.pool.initialPoolSize")));
        securityDataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.pool.minPoolSize")));
        securityDataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.pool.maxIdleTime")));
        securityDataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.pool.maxPoolSize")));


        return securityDataSource;
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager() throws PropertyVetoException {
        return new HibernateTransactionManager(sessionFactory().getObject());

    }

    Properties hibernateProperties() {
        return new Properties() {
            {
/*                setProperty("hibernate.hbm2ddl.auto",
                        env.getProperty("hibernate.hbm2ddl.auto"));*/
                setProperty("hibernate.dialect",
                        env.getProperty("hibernate.dialect"));
            }
        };
    }

}