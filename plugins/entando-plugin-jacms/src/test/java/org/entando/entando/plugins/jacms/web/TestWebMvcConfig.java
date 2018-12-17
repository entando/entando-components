package org.entando.entando.plugins.jacms.web;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.parse.ContentTypeDOM;
import com.google.common.collect.ImmutableList;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.spring.*;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.poi.openxml4j.opc.internal.ContentTypeManager;
import org.entando.entando.aps.system.init.util.ApsDerbyEmbeddedDatabaseType;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.*;
import org.entando.entando.web.common.handlers.RestExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "org.entando.entando.plugins.jacms" },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*ContentModel.*"
        ))
// Excluded ContentModel classes because it doesn't use the same package structure with auto wiring
public class TestWebMvcConfig implements WebMvcConfigurer {

//    @Bean
//    ContentTypeDOM jacmsEntityTypeDom() {
//        return new
//    }


    @Bean
    IEntityManager jacmsContentManager(EntityTypeDOM jacmsEntityTypeDom) {
        ContentManager contentManager = new ContentManager();
        contentManager.setEntityClassName(Content.class.getName());
        contentManager.setEntityTypeDom(jacmsEntityTypeDom);
        return contentManager;
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