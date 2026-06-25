package br.edu.ifpb.pweb2.psp.trivia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private UploadProperties uploadProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String urlPattern = uploadProperties.getNormalizedUrlPrefix() + "**";
        String location = uploadProperties.getFileLocationUri();

        registry.addResourceHandler(urlPattern)
                .addResourceLocations(location);

        log.info("Arquivos de upload servidos em {} a partir de {}", urlPattern, location);
    }
}
