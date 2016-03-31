/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.entity.RolPermiso;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.DatosDTO;
import com.sistem.proyecto.utils.MensajeDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
            if(rolRecibido.getNombre() == null || rolRecibido.getNombre() != null
                    && rolRecibido.getNombre().compareToIgnoreCase("") == 0){
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }
            
            if(rolRecibido.getEmpresa() == null || rolRecibido.getEmpresa() != null
                    && rolRecibido.getEmpresa().compareToIgnoreCase("") == 0){
                retorno.setError(true);
                retorno.setMensaje("Debe seleccionar una empresa para el rol.");
                return retorno;
            }

            if(rolRecibido.getId() == null || rolRecibido.getId() != null && rolRecibido.getId().toString().compareToIgnoreCase("") == 0){
                Rol rol = new Rol();
                rol.setNombre(rolRecibido.getNombre());
                rol.setActivo("S");
                rol.setEmpresa(new Empresa(Long.valueOf(rolRecibido.getEmpresa())));
                rol.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                rol.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                rolManager.save(rol);
                retorno.setMensaje("El rol se creo exitosamente.");
                return retorno;
            }else{
                Rol rol = rolManager.get(rolRecibido.getId());
                rol.setNombre(rolRecibido.getNombre());
                rol.setEmpresa(new Empresa(Long.valueOf(rolRecibido.getEmpresa())));
                rolManager.update(rol);
                retorno.setError(false);
                retorno.setMensaje("El rol se modifico exitosamente.");
                return retorno;
            }      
                
            
            
        }catch (Exception ex){
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar/crear el rol.");
            
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
    
    @RequestMapping(value = "/asignar/{id}/permisos", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    MensajeDTO asignarPermisos(@RequestBody String rolPermisos, @PathVariable("id") Long id) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ModelAndView view = new ModelAndView();
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

                inicializarRolPermisoManager();
                
                Gson gson = new GsonBuilder().create();

		RolPermiso permisos = gson.fromJson(rolPermisos,
					RolPermiso.class);

                if (permisos != null 
                        && permisos.getPermisos() != null) {
                    Rol ejRol = new Rol();
                    ejRol.setId(id);
                    Empresa ejEmpresa = new Empresa();
                    ejEmpresa.setId(userDetail.getIdEmpresa());
                    RolPermiso ejRolPer = new RolPermiso();
                    ejRolPer.setRol(ejRol);
                    ejRolPer.setEmpresa(ejEmpresa);
                    
                    List<Map<String, Object>> listMapRolPermisos = rolPermisoManager.listAtributos(ejRolPer, "id,rol.id,empresa.id".split(","), true);
    
                    if(!listMapRolPermisos.isEmpty())  {
                        for(Map<String, Object> rpm : listMapRolPermisos){
                            rolPermisoManager.delete(Long.parseLong(rpm.get("id").toString()));
                        }
                        
                    }
                    
                    for (Integer rp : permisos.getPermisos()) {
                        ejRolPer = new RolPermiso();
                        ejRolPer.setRol(ejRol);
                        ejRolPer.setEmpresa(ejEmpresa);
                        ejRolPer.setPermiso(new Permiso(Long.parseLong(rp.toString())));

                        rolPermisoManager.save(ejRolPer);
                    }
                     
                }else{
                    retorno.setError(true);
                    retorno.setMensaje("Debe seleccionar algun permiso para el rol.");
                    return retorno;
                }
                retorno.setError(false);
                retorno.setMensaje("Los permisos se asignaron exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al asignar los permisos.");
            return retorno;
        }
        return retorno;

    }
    
    
    @RequestMapping(value = "/asignar/{id}",method = RequestMethod.GET)
    public @ResponseBody
        ModelAndView listarPermisos(@PathVariable("id") Long id,Model model) {
            ModelAndView retorno = new ModelAndView();

            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarPermisoManager();
            
            Permiso ejemplo = new Permiso();
            //ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));            
            List<Map<String, Object>> listMapPermisos = permisoManager.listAtributos(ejemplo, "id,nombre".split(","), true);            
            
            model.addAttribute("permisos", listMapPermisos);
            model.addAttribute("id", id);
            retorno.setViewName("permisos"); 
            
        }catch (Exception ex){
            System.out.println("Error " + ex);
        }
        
        return retorno;
    }
        
    @RequestMapping(value = "/{id}/permisos",method = RequestMethod.GET)
    public @ResponseBody
        HashMap<String, Object> listarPermisos(@PathVariable("id") Long id) {
            List<Long> permisos = new ArrayList<Long>();
            HashMap<String, Object> retorno = new HashMap<String, Object>();
            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarRolPermisoManager();
            
            RolPermiso ejemplo = new RolPermiso();
            ejemplo.setRol(new Rol(id));
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));   
            
            List<Map<String, Object>> listMapPermisos = rolPermisoManager.listAtributos(ejemplo, "id,rol.id,permiso.id".split(","), true);            
            for(Map<String, Object> rpm : listMapPermisos){
                permisos.add(Long.parseLong(rpm.get("permiso.id").toString()));
            }
            retorno.put("permisos", permisos);
            retorno.put("error", false);
            retorno.put("mensaje", "Los permisos asignados se listaron exitosamente");
        }catch (Exception ex){
            System.out.println("Error " + ex);
            retorno.put("error", true);
            retorno.put("mensaje", "Error al listar los permisos asignados.");
        }
        
        return retorno;
    }
}
