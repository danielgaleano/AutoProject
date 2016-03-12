/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.spring.config;

import com.sistem.proyecto.userDetail.ServiceLogin;
import com.sistem.proyecto.userDetail.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 *
 * @author Miguel
 */
@Configuration
@EnableWebSecurity
public class Segurity extends WebSecurityConfigurerAdapter{
       
    
    @Bean
    public ServiceLogin serviceLogin(){
        ServiceLogin serviceLogin = new ServiceLogin();
        return serviceLogin;
    }
    
    @Bean
    UserSession customUserAuthenticationProvider(){
        UserSession userSession = new UserSession();
        userSession.setServiceLogin(serviceLogin());
        return userSession;
    }
    
    @Autowired(required = true)
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(customUserAuthenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/").access("isAuthenticated()")
                .antMatchers("/login**").permitAll()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/logout*").access("isAuthenticated()")
                .antMatchers("/usuarios/**").access("permitAll")
                .antMatchers("/usuarios**").access("permitAll")
                .antMatchers("/home/**").access("isAuthenticated()")
                .antMatchers("/home**").access("isAuthenticated()")
                //------Empresa----//
                .antMatchers("/empresas**").access("isAuthenticated()")
                .antMatchers("/empresas/crear**").access("isAuthenticated()")
                .antMatchers("/empresas/guardar**").access("isAuthenticated()")
                .antMatchers("/empresas/*/activar**").access("isAuthenticated()")
                .antMatchers("/empresas/*/desactivar**").access("isAuthenticated()")
                .antMatchers("/empresas/*/editar**").access("isAuthenticated()")
                .antMatchers("/empresas/*/visualizar**").access("isAuthenticated()")
               //------Movil----//
                .antMatchers("/movil/guardarImagen**").permitAll()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=credenciales")
                .permitAll()
                .usernameParameter("usuario")
                .passwordParameter("password")
                .and()
                .csrf().disable()
                .logout().invalidateHttpSession(true).logoutUrl("/logout").logoutSuccessUrl("/login");
                        
    }
    
    @Override   
    public void configure(WebSecurity  web) throws Exception{
        web.ignoring().antMatchers("/resources/**");
    }
    
}

