/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.spring.config;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 *
 * @author Miguel
 */

@EnableWebMvc
@Configuration
@ComponentScan({ "com.sistem.proyecto.web.*"})
 @Import({Segurity.class})
public class SpringMvcContext extends WebMvcConfigurerAdapter{
//     
// 

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
// 
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setPrefix("/WEB-INF/resources/jsp/");
//        viewResolver.setSuffix(".jsp");
//        registry.viewResolver(viewResolver);
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
                
    } 

////    @Override
////    public void configurePathMatch(PathMatchConfigurer matcher) {
////        matcher.setUseRegisteredSuffixPatternMatch(true);
////    }
//    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
        
    }
////    
//    @Override
//    public void addViewControllers(ViewControllerRegistry configurer){
//        configurer.addViewController("/").setViewName("home");
//        
//    }
    
    @Bean(name ="templateResolver")	
    public ServletContextTemplateResolver getTemplateResolver() {
    	ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
    	templateResolver.setPrefix("/WEB-INF/resources/jsp/");
    	templateResolver.setSuffix(".html");
    	templateResolver.setTemplateMode("XHTML");
        templateResolver.setCharacterEncoding("utf-8");
	return templateResolver;
    }
//    @Bean(name ="templateEngine")	    
//    public SpringTemplateEngine getTemplateEngine() {
//    	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setAdditionalDialects(additionalDialects);
//    	templateEngine.setTemplateResolver(getTemplateResolver());
//	return templateEngine;
//    }
    @Bean(name="viewResolver")
    public ThymeleafViewResolver getViewResolver(){
    	ThymeleafViewResolver viewResolver = new ThymeleafViewResolver(); 
    	viewResolver.setTemplateEngine(getTemplateEngine());
	return viewResolver;
    }
    
    @Bean(name ="templateEngine")
    public SpringTemplateEngine getTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        Set<IDialect> dialects = new HashSet<IDialect>();
        dialects.add(new SpringSecurityDialect());
        dialects.add(new LayoutDialect());
        engine.setAdditionalDialects(dialects);
        LinkedHashSet<ITemplateResolver> templateResolvers = new LinkedHashSet<ITemplateResolver>(2);
        templateResolvers.add(getTemplateResolver());
        templateResolvers.add(layoutTemplateResolverServlet());
        engine.setTemplateResolvers(templateResolvers);
        return engine;
    }
    
    @Bean
    public ServletContextTemplateResolver layoutTemplateResolverServlet() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/resources/jsp/include/");
        templateResolver.setSuffix("");
        templateResolver.setTemplateMode("LEGACYHTML5");
        templateResolver.setOrder(1);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
