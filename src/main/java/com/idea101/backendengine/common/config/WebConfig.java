package com.idea101.backendengine.common.config;

import com.idea101.backendengine.common.interceptor.JwtAnnotationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAnnotationInterceptor jwtAnnotationInterceptor;

    public WebConfig(JwtAnnotationInterceptor jwtAnnotationInterceptor) {
        this.jwtAnnotationInterceptor = jwtAnnotationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAnnotationInterceptor).addPathPatterns("/**");
    }
}
