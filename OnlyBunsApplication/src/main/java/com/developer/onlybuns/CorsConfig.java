/*package com.developer.onlybuns;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    public CorsConfig() {
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
//                .allowedHeaders(
//                        HttpHeaders.AUTHORIZATION,
//                        HttpHeaders.CONTENT_TYPE,
//                        HttpHeaders.ACCEPT
//                )
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}*/