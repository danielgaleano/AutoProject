/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.userDetail.UserDetail;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/empresas")
public class EmpresaController extends BaseController{
    
    String atributos = "id,nombre,descripcion,email,ruc,telefono,telefonoMovil,"
            + "nombreContacto,telefonoContacto,telefonoMovilContacto,direccion,activo";
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView welcomePage(Model model) {
            ModelAndView retorno = new ModelAndView();
            Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarEmpresaManager();
            System.out.println(autentication.getName());
            System.out.println(userDetail.getNombre());
            Empresa ejemplo = new Empresa();

            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(ejemplo, atributos.split(","), true);


            model.addAttribute("empresas", listMapEmpresas);

            retorno.setViewName("empresas");
            
        }catch (Exception ex){
            
        }
            
            return retorno;

    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
            model.addAttribute("tipo", "Crear");
            return new ModelAndView("empresa");
    }
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
   public @ResponseBody MensajeDTO guardar(@ModelAttribute("Empresa") Empresa empresaRecibido) {
       MensajeDTO mensaje = new MensajeDTO();
       Empresa ejEmpresa = new Empresa();
       try{
           inicializarEmpresaManager();
           
           if(empresaRecibido != null && empresaRecibido.getRuc() != null){
               ejEmpresa.setRuc(empresaRecibido.getRuc());
               
               ejEmpresa = empresaManager.get(ejEmpresa);
               if(ejEmpresa != null){
                   mensaje.setError(true);
                   mensaje.setMenasje("El numero de ruc ya se encuentra registrado.");
                   return mensaje;
               }else{
                    ejEmpresa = new Empresa();
                    ejEmpresa.setActivo("S");
                    ejEmpresa.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    ejEmpresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    ejEmpresa.setDescripcion(empresaRecibido.getDescripcion());
                    ejEmpresa.setDireccion(empresaRecibido.getDireccion());
                    ejEmpresa.setRuc(empresaRecibido.getRuc());
                    ejEmpresa.setEmail(empresaRecibido.getEmail());
                    ejEmpresa.setNombre(empresaRecibido.getNombre());
                    ejEmpresa.setTelefono(empresaRecibido.getTelefono());
                    ejEmpresa.setTelefonoMovil(empresaRecibido.getTelefonoMovil());
                    ejEmpresa.setNombreContacto(empresaRecibido.getNombreContacto());
                    ejEmpresa.setTelefonoContacto(empresaRecibido.getTelefonoContacto());
                    ejEmpresa.setTelefonoMovilContacto(empresaRecibido.getTelefonoMovilContacto());
               }
               
           }else{
                mensaje.setError(true);
                mensaje.setMenasje("Debe ingresar numero de ruc.");
                return mensaje;
           }
             
           
           if(empresaRecibido.getId() != null){
               Empresa ejEmpresaUp = new Empresa();
               ejEmpresaUp = empresaManager.get(empresaRecibido.getId());
               ejEmpresaUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
               ejEmpresaUp.setDescripcion(empresaRecibido.getDescripcion());
               ejEmpresaUp.setDireccion(empresaRecibido.getDireccion());
               //ejEmpresaUp.setRuc(empresaRecibido.getRuc());
               ejEmpresaUp.setEmail(empresaRecibido.getEmail());
               ejEmpresaUp.setNombre(empresaRecibido.getNombre());
               ejEmpresaUp.setTelefono(empresaRecibido.getTelefono());
               ejEmpresa.setTelefonoMovil(empresaRecibido.getTelefonoMovil());
               ejEmpresa.setNombreContacto(empresaRecibido.getNombreContacto());
               ejEmpresa.setTelefonoContacto(empresaRecibido.getTelefonoContacto());
               ejEmpresa.setTelefonoMovilContacto(empresaRecibido.getTelefonoMovilContacto());
               empresaManager.update(ejEmpresaUp);  
           }else{
              empresaManager.save(ejEmpresa); 
           }       
           
           
           mensaje.setError(false);
           mensaje.setMenasje("La empresa "+empresaRecibido.getNombre()+" se guardo exitosamente.");
           
       }catch(Exception e){
           mensaje.setError(true);
           mensaje.setMenasje("Error a guardar la empresa");
           System.out.println("Error");
       }
           
           return mensaje;
   }
   
    @RequestMapping(value = "/{id}/desactivar", method = RequestMethod.DELETE)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarEmpresaManager();

                    Empresa empresa = empresaManager.get(id);
                    

                    if (empresa != null) {
                            nombre = empresa.getNombre().toString();
                    }

                    if (empresa != null && empresa.getActivo().toString()
                                                    .compareToIgnoreCase("N") == 0) {
                        retorno.setError(true);
                        retorno.setMenasje("La empresa "+ nombre+" ya se encuentra desactivada.");
                    }
                    empresa.setActivo("N");
                    empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    empresaManager.update(empresa);

                    retorno.setError(false);
                    retorno.setMenasje("La empresa "+ nombre+" se desactivo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMenasje("Error al tratar de desactivar la empresa.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/{id}/activar", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarEmpresaManager();

                    Empresa empresa = empresaManager.get(id);

                    if (empresa != null) {
                            nombre = empresa.getNombre().toString();
                    }

                    if (empresa != null && empresa.getActivo().toString()
                                                    .compareToIgnoreCase("N") == 0) {
                        retorno.setError(true);
                        retorno.setMenasje("La empresa "+ nombre+" ya se encuentra activada.");
                    }
                    empresa.setActivo("S");
                    empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    empresaManager.update(empresa);

                    retorno.setError(false);
                    retorno.setMenasje("La empresa "+ nombre+" se activo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMenasje("Error al tratar de activar la empresa.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/{id}/{tipo}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id,@PathVariable("tipo") String tipo,Model model) {
            ModelAndView retorno = new ModelAndView();
            String nombre = "";

            try {

                    inicializarEmpresaManager();

                    Map<String, Object> empresa = empresaManager.getAtributos(
					new Empresa(id), atributos.split(","), false, true);

                    model.addAttribute("empresa", empresa);
                    
                    if(tipo.compareToIgnoreCase("editar") == 0){
                        model.addAttribute("editar", true);
                        model.addAttribute("tipo", "Editar");
                    }else{
                        model.addAttribute("visualizar", true);
                        model.addAttribute("tipo", "Visualizar");
                    }

                    retorno.setViewName("empresa");
            } catch (Exception e) {
                    
            }

            return retorno;

    }
}
