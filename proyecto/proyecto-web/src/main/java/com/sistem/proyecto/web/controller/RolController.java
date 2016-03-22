/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.DatosDTO;
import com.sistem.proyecto.utils.MensajeDTO;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody MensajeDTO guardar(@ModelAttribute("Rol") DatosDTO rolRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        try{
            inicializarRolManager();

            
            Rol rol = rolManager.get(rolRecibido.getId());
            rol.setNombre(rolRecibido.getNombre());
            rol.setEmpresa(new Empresa(Long.valueOf(rolRecibido.getEmpresa())));
            rolManager.update(rol);
            retorno.setError(false);
            retorno.setMensaje("El rol se modifico exitosamente.");
                
            
            
        }catch (Exception ex){
            
        }
        return retorno;
    }
    
    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public  @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarRolManager();

                    Rol rol = rolManager.get(id);

                    if (rol != null) {
                            nombre = rol.getNombre().toString();
                    }

                    if (rol != null && rol.getActivo().toString()
                                                    .compareToIgnoreCase("S") == 0) {
                        retorno.setError(true);
                        retorno.setMensaje("La empresa "+ nombre+" ya se encuentra activada.");
                    }
                    rol.setActivo("S");
                    rol.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    rol.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    rolManager.update(rol);

                    retorno.setError(false);
                    retorno.setMensaje("El rol "+ nombre+" se activo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de activar el rol.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id,Model model) {
            ModelAndView view = new ModelAndView();
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarRolManager();

                    Rol rol = rolManager.get(id);

                    if (rol != null) {
                            nombre = rol.getNombre().toString();
                    }

                    if (rol != null && rol.getActivo().toString()
                                                    .compareToIgnoreCase("N") == 0) {
                        retorno.setError(true);
                        retorno.setMensaje("El rol "+ nombre+" ya se encuentra desactivada.");
                    }
                    rol.setActivo("N");
                    rol.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    rol.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    rolManager.update(rol);

                    retorno.setError(false);
                    retorno.setMensaje("El rol "+ nombre+" se desactivo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de desactivar el rol.");
            }
            view = listarRoles(model);
            return retorno;

    }
}
