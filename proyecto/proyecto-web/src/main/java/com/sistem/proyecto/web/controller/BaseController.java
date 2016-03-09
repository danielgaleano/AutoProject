/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.manager.EmpresaManager;
import com.sistem.proyecto.manager.UsuarioManager;
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
}
