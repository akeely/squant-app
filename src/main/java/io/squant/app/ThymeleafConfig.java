package io.squant.app;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Value("${squant.template.directory}")
    private String templatePrefix;

    @Bean
    public FileTemplateResolver defaultTemplateResolver() {

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(templatePrefix + File.separator);
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);

        return resolver;
    }
}
