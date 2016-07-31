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
                                .antMatchers("/home/**").access("isAuthenticated()")
                .antMatchers("/home**").access("isAuthenticated()")
                //------usuarios----//
                .antMatchers("/usuarios**").access("isAuthenticated()")
                .antMatchers("/usuarios/crear**").access("isAuthenticated()")
                .antMatchers("/usuarios/guardar**").access("isAuthenticated()")
                .antMatchers("/usuarios/asignar").hasAnyAuthority("SuperUsuario")
                .antMatchers("/usuarios/asignar/rol/*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/usuarios/activar/**").access("isAuthenticated()")
                .antMatchers("/usuarios/desactivar/**").access("isAuthenticated()")
                .antMatchers("/usuarios/editar/**").access("isAuthenticated()")
                .antMatchers("/usuarios/editar").access("isAuthenticated()")
                .antMatchers("/usuarios/visualizar/**").access("isAuthenticated()")
                .antMatchers("/usuarios/reset/**").hasAnyAuthority("SuperUsuario")
                //------roles----//
                .antMatchers("/roles**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/asignar/*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/asignar/*/permisos*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/*/permisos*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/guardar**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/editar**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/activar/**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/roles/desactivar/**").hasAnyAuthority("SuperUsuario")
                //------Permisos----//
                .antMatchers("/permisos**").hasAnyAuthority("SuperUsuario")
                //------Empresa----//
                .antMatchers("/empresas**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/crear**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/guardar**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/activar/*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/desactivar/*").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/editar/**").hasAnyAuthority("SuperUsuario")
                .antMatchers("/empresas/visualizar/**").hasAnyAuthority("SuperUsuario")
                //------Empresa----//
                .antMatchers("/clientes**").hasAnyAuthority("Cliente.Listar")
                .antMatchers("/clientes/crear**").hasAnyAuthority("Cliente.Crear")
                .antMatchers("/clientes/guardar**").hasAnyAuthority("Cliente.Crear")
                .antMatchers("/clientes/activar/*").hasAnyAuthority("Cliente.Activar")
                .antMatchers("/clientes/desactivar/*").hasAnyAuthority("Cliente.Desactivar")
                .antMatchers("/clientes/editar/**").hasAnyAuthority("Cliente.Editar")
                .antMatchers("/clientes/visualizar/**").hasAnyAuthority("Cliente.Visualizar")
                //------Tipos----//
                .antMatchers("/tipos**").hasAnyAuthority("Tipo.Listar")
                .antMatchers("/tipos/crear**").hasAnyAuthority("Tipo.Crear")
                .antMatchers("/tipos/guardar**").hasAnyAuthority("Tipo.Crear")
                .antMatchers("/tipos/editar**").hasAnyAuthority("Tipo.Editar")
                //------Proveedor----//
                .antMatchers("/proveedores**").hasAnyAuthority("Proveedor.Listar")
                .antMatchers("/proveedores/crear**").hasAnyAuthority("Proveedor.Crear")
                .antMatchers("/proveedores/guardar**").hasAnyAuthority("Proveedor.Crear")
                .antMatchers("/proveedores/activar/*").hasAnyAuthority("Proveedor.Activar")
                .antMatchers("/proveedores/desactivar/*").hasAnyAuthority("Proveedor.Desactivar")
                .antMatchers("/proveedores/editar/**").hasAnyAuthority("Proveedor.Editar")
                .antMatchers("/proveedores/visualizar/**").hasAnyAuthority("Proveedor.Visualizar")
               //------Movil----//
                .antMatchers("/movil/guardarImagen**").permitAll()
                .antMatchers("/obtenerImagen/*/*").permitAll()
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

