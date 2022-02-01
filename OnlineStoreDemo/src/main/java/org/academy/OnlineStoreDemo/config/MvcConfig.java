package org.academy.OnlineStoreDemo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@AllArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("./user-photos");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/user-photos/**").addResourceLocations("file:/"+ uploadPath + "/");

        Path uploadDir1 = Paths.get("./product-photos");
        String uploadPath1 = uploadDir1.toFile().getAbsolutePath();
        registry.addResourceHandler("/product-photos/**").addResourceLocations("file:/"+ uploadPath1 + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(localeChangeInterceptor);
    }


}