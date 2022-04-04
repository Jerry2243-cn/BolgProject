package com.jerry.project.config;

import com.jerry.project.config.MapPath;
import com.jerry.project.util.FilepathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private MapPath mapPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(mapPath.getUrl()+"**").addResourceLocations("file://"+mapPath.getPath());
    }
}
