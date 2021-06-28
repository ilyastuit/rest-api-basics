package com.epam.esm;

import com.epam.esm.config.AppConfig;
import com.epam.esm.config.DataSourceConfig;
import com.epam.esm.config.HttpConverterConfig;
import com.epam.esm.config.property.DevProperty;
import com.epam.esm.config.property.MainPropertyConfig;
import com.epam.esm.config.property.ProdProperty;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class RestApiBasicsDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.scan("com.epam.esm.*");
        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                AppConfig.class,
                HttpConverterConfig.class,
                MainPropertyConfig.class,
                DevProperty.class,
                ProdProperty.class,
                DataSourceConfig.class,
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {AppConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
