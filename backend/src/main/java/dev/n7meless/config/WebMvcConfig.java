package dev.n7meless.config;

import dev.n7meless.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;
    private final String[] allowedOrigins;

    @Autowired
    public WebMvcConfig(AuthorizationInterceptor authorizationInterceptor,
                        @Value("${application.security.allowed-origins}")String[] allowedOrigins) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(false);
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(byteArrayHttpMessageConverter());
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
        return arrayHttpMessageConverter;
    }

    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.MULTIPART_FORM_DATA);
        return list;
    }
}
