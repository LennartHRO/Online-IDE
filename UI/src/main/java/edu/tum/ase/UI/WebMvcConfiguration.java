package edu.tum.ase.UI;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // First, handle static resources like JS and CSS
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/browser/");

        // Then, handle other requests (e.g., Angular routes)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/browser/index.html")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        //logger.info("Requested resource: " + resourcePath);
                        Resource requestedResource = location.createRelative(resourcePath);
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        } else {
                           
                            return new ClassPathResource("/static/browser/index.html");
                        }
                    }
                });
    }
}
