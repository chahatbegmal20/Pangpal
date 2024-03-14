package dev.n7meless.annotation;

import dev.n7meless.config.SecurityConfig;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        SecurityConfig.class})
@ContextConfiguration
public @interface UnsecuredWebMvcTest {
    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class<?>[] controllers();
}
