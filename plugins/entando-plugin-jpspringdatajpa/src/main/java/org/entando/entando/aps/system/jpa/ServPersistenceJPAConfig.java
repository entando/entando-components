/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = {"org.entando.entando.aps.system.jpa.servdb"},
        transactionManagerRef = "servTransactionManager",
        entityManagerFactoryRef = "servEntityManager"
)
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ServPersistenceJPAConfig {

    private final DataSource dataSource;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAutoMode;

    @Autowired
    public ServPersistenceJPAConfig(@Qualifier("servDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = "servEntityManager")
    // this force the Entando db generation to run before the JPA one
    @DependsOn("InitializerManager")
    public LocalContainerEntityManagerFactoryBean getServEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("org.entando.entando.aps.system.jpa.servdb");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean(name = "servTransactionManager")
    @Primary
    public PlatformTransactionManager getServTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(this.getServEntityManagerFactory().getObject());
        return transactionManager;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateDdlAutoMode);
        return properties;
    }

}
