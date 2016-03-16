/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/roles")
public class RolController extends BaseController{
    
    String atributos = "id,nombre,activo,empresa.id,empresa.nombre";
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarRoles(Model model) {
            ModelAndView retorno = new ModelAndView();

            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarRolManager();
            inicializarEmpresaManager();
            System.out.println(userDetail.getNombre());
            
            Rol ejemplo = new Rol();
            //ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            
            List<Map<String, Object>> listMapRoles = rolManager.listAtributos(ejemplo, atributos.split(","), true);
            
            for(Map<String, Object> rpm : listMapRoles){
                rpm.put("rolEmpresa", rpm.get("empresa.nombre"));
            }
            
            Empresa ejEmpresa = new Empresa();
            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(ejEmpresa, "id,nombre".split(","), true);

            model.addAttribute("roles", listMapRoles);
            model.addAttribute("empresas", listMapEmpresas);
            
            retorno.setViewName("roles");
            
        }catch (Exception ex){
            
        }
        
        return retorno;
    }
}
