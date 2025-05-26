package com.example.travelappv2.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class PostgresOnlyConfig {

    @Autowired
    private Environment env;
    
    // Pas besoin d'injection de valeurs car on utilise directement l'Environment

    @Primary
    @Bean
    public DataSource dataSource() {
        // Utilisation explicite de DriverManagerDataSource pour éviter HikariCP
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        
        // Log pour le debugging
        System.out.println("DATABASE URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("DATABASE USERNAME: " + env.getProperty("spring.datasource.username"));
        System.out.println("DATABASE DRIVER: org.postgresql.Driver");
        
        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.example.travelappv2.model");
        factory.setDataSource(dataSource());
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", false);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", true);
        
        // Désactiver explicitement la détection automatique des drivers
        properties.put("hibernate.connection.provider_class", "org.hibernate.connection.DriverManagerConnectionProvider");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.connection.url", env.getProperty("spring.datasource.url"));
        properties.put("hibernate.connection.username", env.getProperty("spring.datasource.username"));
        properties.put("hibernate.connection.password", env.getProperty("spring.datasource.password"));
        
        factory.setJpaPropertyMap(properties);
        
        return factory;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
