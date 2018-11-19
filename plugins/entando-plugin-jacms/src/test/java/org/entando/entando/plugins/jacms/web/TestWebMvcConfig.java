package org.entando.entando.plugins.jacms.web;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "org.entando.entando.plugins.jacms.web" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = ".*ContentModelController"))
// Excluded ContentModelController because it doesn't use the same package structure with auto wiring
public class TestWebMvcConfig implements WebMvcConfigurer { }