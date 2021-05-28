package io.gitlab.hsedjame.buzz.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class BuzzWebConfig implements WebFluxConfigurer {
    private static final String[] CLASSPATH_RESOURCES_LOCATIONS = { "classpath:/META-INF/resources/",
            "classpath:/templates/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")

                .addResourceLocations(CLASSPATH_RESOURCES_LOCATIONS);
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

}
