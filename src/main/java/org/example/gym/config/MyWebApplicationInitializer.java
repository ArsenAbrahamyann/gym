package org.example.gym.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocSortConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Web application initializer for configuring the Spring application context and setting up
 * the DispatcherServlet, character encoding filter, and SpringDoc configurations for
 * API documentation.
 */
@Component
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    /**
     * Initializes the web application by setting up the Spring application context,
     * configuring the DispatcherServlet, and registering the character encoding filter.
     *
     * @param servletContext the ServletContext to configure the web application
     * @throws ServletException if an error occurs during the initialization
     */
    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.setConfigLocation("org.example.gym.config");

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");

        FilterRegistration.Dynamic registration =
                servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
        registration.setInitParameter("encoding", "UTF-8");
        registration.setInitParameter("forceEncoding", "true");
        registration.addMappingForUrlPatterns(null, true, "/*");

        context.register(this.getClass(),
                SpringDocConfiguration.class,
                SpringDocConfigProperties.class,
                SpringDocSortConfiguration.class,
                SpringDocWebMvcConfiguration.class,
                MultipleOpenApiSupportConfiguration.class,
                OpenApiConfig.class,
                SwaggerUiConfigProperties.class,
                SwaggerUiOAuthProperties.class,
                SpringDocUIConfiguration.class);

    }
}


