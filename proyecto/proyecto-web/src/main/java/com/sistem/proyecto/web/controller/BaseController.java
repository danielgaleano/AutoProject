/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.manager.EmpresaManager;
import com.sistem.proyecto.manager.ImagenManager;
import com.sistem.proyecto.manager.PermisoManager;
import com.sistem.proyecto.manager.RolManager;
import com.sistem.proyecto.manager.RolPermisoManager;
import com.sistem.proyecto.manager.UsuarioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Miguel
 */
@Controller
public class BaseController {
    
    private Context context;

    protected UsuarioManager usuarioManager;
    
    protected EmpresaManager empresaManager;
    
    protected RolManager rolManager;
    
    protected PermisoManager permisoManager;
    
    protected RolPermisoManager rolPermisoManager;
    
    protected ImagenManager imagenManager;
    
    public static final Logger logger = LoggerFactory
			.getLogger("proyecto");
    
    protected void inicializarImagenManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (imagenManager == null) {
                try {

                        imagenManager = (ImagenManager) context.lookup("java:app/proyecto-ejb/ImagenManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
    
    
    protected void inicializarRolPermisoManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (rolPermisoManager == null) {
                try {

                        rolPermisoManager = (RolPermisoManager) context.lookup("java:app/proyecto-ejb/RolPermisoManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
    
    protected void inicializarPermisoManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (permisoManager == null) {
                try {

                        permisoManager = (PermisoManager) context.lookup("java:app/proyecto-ejb/PermisoManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
    
    protected void inicializarUsuarioManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (usuarioManager == null) {
                try {

                        usuarioManager = (UsuarioManager) context.lookup("java:app/proyecto-ejb/UsuarioManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
    
    protected void inicializarEmpresaManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (empresaManager == null) {
                try {

                        empresaManager = (EmpresaManager) context.lookup("java:app/proyecto-ejb/EmpresaManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
    
    protected void inicializarRolManager() throws Exception{
        if (context == null)
                try {
                        context = new InitialContext();
                } catch (NamingException e1) {
                        throw new RuntimeException("No se puede inicializar el contexto", e1);
                }
        if (rolManager == null) {
                try {

                        rolManager = (RolManager) context.lookup("java:app/proyecto-ejb/RolManagerImpl");
                } catch (NamingException ne) {
                        throw new RuntimeException("No se encuentra EJB valor Manager: ", ne);
                }
        }
    }
}
