/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/permisos")
public class PermisoController extends BaseController{
    
    String atributos = "id,nombre";
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarPermisos(Model model) {
            ModelAndView retorno = new ModelAndView();

            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarPermisoManager();
            Permiso ejemplo = new Permiso();
            //ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            
            List<Map<String, Object>> listMapPermisos = permisoManager.listAtributos(ejemplo, atributos.split(","), true);
 
            model.addAttribute("permisos", listMapPermisos);
            
            retorno.setViewName("permisosListar");
            
        }catch (Exception ex){
            logger.error("Error en listar permisos", ex);
        }
        
        return retorno;
    }
}
