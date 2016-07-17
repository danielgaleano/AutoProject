/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
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
           inicializarImagenManager();
           if(empresaRecibido != null && (empresaRecibido.getRuc()== null 
                   || empresaRecibido.getRuc().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("El RUC de la empresa no puede estar vacio.");
                return mensaje;
           }
           
           if(empresaRecibido != null && (empresaRecibido.getNombre() == null 
                   || empresaRecibido.getNombre().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la empresa no puede estar vacio.");
                return mensaje;
           }
           
           if(empresaRecibido != null && (empresaRecibido.getDireccion()== null 
                   || empresaRecibido.getDireccion().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar una direccion para la empresa.");
                return mensaje;
           }
           
           if(empresaRecibido != null && (empresaRecibido.getEmail()== null 
                   || empresaRecibido.getEmail().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un email para la empresa.");
                return mensaje;
           }
           
           if(empresaRecibido != null && (empresaRecibido.getNombreContacto()== null 
                   || empresaRecibido.getNombreContacto().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un contacto para la empresa.");
                return mensaje;
           }
           
           Imagen imagenP = null;
           
           String imagenPortada = empresaRecibido.getImagenPort();
           
           ejEmpresa.setRuc(empresaRecibido.getRuc());
               
           ejEmpresa = empresaManager.get(ejEmpresa);
           if(ejEmpresa != null){
                mensaje.setError(true);
                mensaje.setMensaje("El numero de ruc ya se encuentra registrado.");
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
                
                empresaManager.save(ejEmpresa);
                
                if (imagenPortada != null && !imagenPortada.equals("")
                            && imagenPortada.length() > 0) {
                    imagenP = new Imagen();
                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/")+1);
                    imagenP.setNombreTabla("empresa");
                    imagenP.setNombreImagen(empresaRecibido.getNombre()+ "." + extension);
                    imagenP.setActivo("S");
                    imagenP.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setEmpresa(ejEmpresa);
                    imagenP.setEntidadId(ejEmpresa.getId());
                    
                     imagenManager.save(imagenP);

                }
               
           }

           mensaje.setError(false);
           mensaje.setMensaje("La empresa "+empresaRecibido.getNombre()+" se guardo exitosamente.");
           
       }catch(Exception e){
           mensaje.setError(true);
           mensaje.setMensaje("Error a guardar la empresa");
           System.out.println("Error");
       }
           
           return mensaje;
   }
   
   @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody MensajeDTO editar(@ModelAttribute("Empresa") Empresa empresaRecibido) {
        MensajeDTO mensaje = new MensajeDTO();
        Empresa ejEmpresa = new Empresa();
        try{
            inicializarEmpresaManager();
            inicializarImagenManager();
            
            if(empresaRecibido != null && (empresaRecibido.getNombre() == null 
                   || empresaRecibido.getNombre().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la empresa no puede estar vacio.");
                return mensaje;
            }
           
            if(empresaRecibido != null && (empresaRecibido.getDireccion()== null 
                   || empresaRecibido.getDireccion().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar una direccion para la empresa.");
                return mensaje;
            }
           
            if(empresaRecibido != null && (empresaRecibido.getEmail()== null 
                   || empresaRecibido.getEmail().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un email para la empresa.");
                return mensaje;
            }
           
            if(empresaRecibido != null && (empresaRecibido.getNombreContacto()== null 
                   || empresaRecibido.getNombreContacto().compareToIgnoreCase("") == 0) ){
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un contacto para la empresa.");
                return mensaje;
            }
               
            ejEmpresa = empresaManager.get(empresaRecibido.getId());
           
            Imagen imagenP = null;
           
            String imagenPortada = empresaRecibido.getImagenPort();
           
            if (imagenPortada != null && !imagenPortada.equals("")
                            && imagenPortada.length() > 0) {
                imagenP = new Imagen();
                imagenP.setEntidadId(empresaRecibido.getId());
                imagenP.setNombreTabla(Empresa.class.getName());
                imagenP = imagenManager.get(imagenP);

                if(imagenP != null){
                    
                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/")+1);
                    imagenP.setNombreImagen(empresaRecibido.getNombre()+ "." + extension);

                    imagenManager.update(imagenP);

                }else{
                    imagenP = new Imagen();
                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/")+1);
                    imagenP.setNombreTabla("empresa");
                    imagenP.setNombreImagen(empresaRecibido.getNombre()+ "." + extension);
                    imagenP.setActivo("S");
                    imagenP.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    imagenP.setEmpresa(ejEmpresa);
                    imagenP.setEntidadId(empresaRecibido.getId());
                    
                    imagenManager.save(imagenP);
                }
           }
                       
            ejEmpresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejEmpresa.setDescripcion(empresaRecibido.getDescripcion());
            ejEmpresa.setDireccion(empresaRecibido.getDireccion());
            ejEmpresa.setEmail(empresaRecibido.getEmail());
            ejEmpresa.setNombre(empresaRecibido.getNombre());
            ejEmpresa.setTelefono(empresaRecibido.getTelefono());
            ejEmpresa.setTelefonoMovil(empresaRecibido.getTelefonoMovil());
            ejEmpresa.setNombreContacto(empresaRecibido.getNombreContacto());
            ejEmpresa.setTelefonoContacto(empresaRecibido.getTelefonoContacto());
            ejEmpresa.setTelefonoMovilContacto(empresaRecibido.getTelefonoMovilContacto());
                
            empresaManager.update(ejEmpresa);                               

            mensaje.setError(false);
            mensaje.setMensaje("La empresa "+empresaRecibido.getNombre()+" se modifico exitosamente.");
            return mensaje;
           

           
       }catch(Exception e){
           mensaje.setError(true);
           mensaje.setMensaje("Error a guardar la empresa");
           System.out.println("Error" + e);
       }
           
           return mensaje;
   }
   
    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.DELETE)
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
                        retorno.setMensaje("La empresa "+ nombre+" ya se encuentra desactivada.");
                    }
                    empresa.setActivo("N");
                    empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    empresaManager.update(empresa);

                    retorno.setError(false);
                    retorno.setMensaje("La empresa "+ nombre+" se desactivo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de desactivar la empresa.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
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
                        retorno.setMensaje("La empresa "+ nombre+" ya se encuentra activada.");
                    }
                    empresa.setActivo("S");
                    empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    empresaManager.update(empresa);

                    retorno.setError(false);
                    retorno.setMensaje("La empresa "+ nombre+" se activo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de activar la empresa.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id,Model model) {
            ModelAndView retorno = new ModelAndView();
            String nombre = "";

            try {

                    inicializarEmpresaManager();

                    Map<String, Object> empresa = empresaManager.getAtributos(
					new Empresa(id), atributos.split(","), false, true);

                    model.addAttribute("empresa", empresa);
                    model.addAttribute("editar", true);
                    

                    retorno.setViewName("empresa");
            } catch (Exception e) {
                    System.out.println("Error" + e);
            }

            return retorno;

    }
    
    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView visualizar(@PathVariable("id") Long id,Model model) {
            ModelAndView retorno = new ModelAndView();
            String nombre = "";

            try {
                    inicializarEmpresaManager();

                    Map<String, Object> empresa = empresaManager.getAtributos(
					new Empresa(id), atributos.split(","), false, true);
                   
                    model.addAttribute("editar", false);
                    model.addAttribute("empresa", empresa);
                    retorno.setViewName("empresa");
            } catch (Exception e) {
                 
                System.out.println("Error" + e);   
            }

            return retorno;

    }
}
