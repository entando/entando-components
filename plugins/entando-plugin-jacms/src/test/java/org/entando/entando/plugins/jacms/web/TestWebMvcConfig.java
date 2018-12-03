package org.entando.entando.plugins.jacms.web;

import com.google.common.collect.ImmutableList;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.spring.*;
import com.j256.ormlite.support.ConnectionSource;
import org.entando.entando.aps.system.init.util.ApsDerbyEmbeddedDatabaseType;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.*;
import org.entando.entando.web.common.handlers.RestExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.web.servlet.config.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "org.entando.entando.plugins.jacms" },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*ContentModel.*"
        ))
// Excluded ContentModel classes because it doesn't use the same package structure with auto wiring
public class TestWebMvcConfig implements WebMvcConfigurer {

    @Bean
    DataSource portDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .setName("test")
                .addScript("classpath:sql/plugins/jacms/create_schema.sql")
//                .addScript("classpath:jdbc/test-data.sql")
                .build();
    }
    @Bean
    DataSourceConnectionSource connectionSource(DataSource portDataSource) throws SQLException {
        return new DataSourceConnectionSource(portDataSource, new ApsDerbyEmbeddedDatabaseType());
    }

    @Bean
    TransactionManager transactionManager(ConnectionSource connectionSource) {
        return new TransactionManager(connectionSource);
    }

    @Bean
    Dao<ContentType, Long> componentTypeDao(ConnectionSource connectionSource) throws SQLException {
        return DaoFactory.createDao(connectionSource, ContentType.class);
    }

    @Bean
    Dao<Attribute, Long> attributeDao(ConnectionSource connectionSource) throws SQLException {
        return DaoFactory.createDao(connectionSource, Attribute.class);
    }

    @Bean
    Dao<AttributeRole, Long> attributeRoleDao(ConnectionSource connectionSource) throws SQLException {
        return DaoFactory.createDao(connectionSource, AttributeRole.class);
    }

    @Bean
    TableCreator tableCreator(
            ConnectionSource connectionSource,
            Dao<ContentType, Long> componentTypeDao,
            Dao<AttributeRole, Long> attributeRoleDao,
            Dao<Attribute, Long> attributeDao
        ) throws SQLException {

        TableCreator tableCreator = new TableCreator(connectionSource,
                ImmutableList.of(componentTypeDao, attributeRoleDao, attributeDao));
        System.setProperty(TableCreator.AUTO_CREATE_TABLES, "true");

        tableCreator.initialize();

        return tableCreator;
    }

    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(true);
        messageSource.setBasename("rest/messages");

        return messageSource;
    }

    @Bean
    RestExceptionHandler restExceptionHandler(MessageSource messageSource) {
        RestExceptionHandler restExceptionHandler = new RestExceptionHandler();
        restExceptionHandler.setMessageSource(messageSource);
        return restExceptionHandler;
    }
}