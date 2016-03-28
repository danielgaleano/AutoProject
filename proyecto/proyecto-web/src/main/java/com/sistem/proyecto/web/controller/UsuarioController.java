/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Usuario;
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
@RequestMapping(value = "/usuarios")
public class UsuarioController extends BaseController{
    
    String atributos = "id,nombre,apellido,documento,email,claveAcceso,telefono,rol.id,"
            + "rol.nombre,empresa.id,empresa.nombre,direccion,activo";
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaUsuarios(Model model) {
            ModelAndView retorno = new ModelAndView();
            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Empresa ejemplo = new Empresa();
            Usuario ejUsuario = new Usuario();
            ejUsuario.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        try{
            inicializarUsuarioManager();
            System.out.println(userDetail.getNombre());
            

            List<Map<String, Object>> listMapUsuarios = usuarioManager.listAtributos(ejUsuario, atributos.split(","), true);
            for(Map<String, Object> rpm : listMapUsuarios){
                rpm.put("rolNombre", rpm.get("rol.nombre"));
            }

            model.addAttribute("usuarios", listMapUsuarios);

            retorno.setViewName("usuarios");
            
        }catch (Exception ex){
            
        }
            
            return retorno;

    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        
        try{
            inicializarEmpresaManager();
            model.addAttribute("tipo", "Crear");           
            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(new Empresa(), "id,nombre".split(","), true);
            model.addAttribute("empresas", listMapEmpresas);
        }catch (Exception ex){
            
        }
            
            return new ModelAndView("usuario");
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
               }
               
           }else{
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar numero de ruc.");
                return mensaje;
           }
             
           
           if(empresaRecibido.getId() != null){
               Empresa ejEmpresaUp = new Empresa();
               ejEmpresaUp = empresaManager.get(empresaRecibido.getId());
               ejEmpresaUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
               ejEmpresaUp.setDescripcion(empresaRecibido.getDescripcion());
               ejEmpresaUp.setDireccion(empresaRecibido.getDireccion());
               ejEmpresaUp.setRuc(empresaRecibido.getRuc());
               ejEmpresaUp.setEmail(empresaRecibido.getEmail());
               ejEmpresaUp.setNombre(empresaRecibido.getNombre());
               ejEmpresaUp.setTelefono(empresaRecibido.getTelefono()); 
               empresaManager.update(ejEmpresaUp);  
           }else{
              empresaManager.save(ejEmpresa); 
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
   
    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarUsuarioManager();

                    Usuario usuario = usuarioManager.get(id);
                    

                    if (usuario != null) {
                            nombre = usuario.getNombre().toString();
                    }

                    if (usuario != null && usuario.getActivo().toString()
                                                    .compareToIgnoreCase("N") == 0) {
                        retorno.setError(true);
                        retorno.setMensaje("El usuario "+ nombre+" ya se encuentra desactivada.");
                    }
                    usuario.setActivo("N");
                    usuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    usuario.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    usuarioManager.update(usuario);

                    retorno.setError(false);
                    retorno.setMensaje("El Usuario "+ nombre+" se desactivo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de desactivar el usuario.");
            }

            return retorno;

    }
    
    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
            MensajeDTO retorno = new MensajeDTO();
            String nombre = "";

            try {

                    inicializarUsuarioManager();

                    Usuario usuario = usuarioManager.get(id);

                    if (usuario != null) {
                            nombre = usuario.getNombre().toString();
                    }

                    if (usuario != null && usuario.getActivo().toString()
                                                    .compareToIgnoreCase("N") == 0) {
                        retorno.setError(true);
                        retorno.setMensaje("El usuario "+ nombre+" ya se encuentra activo.");
                    }
                    usuario.setActivo("S");
                    usuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    usuario.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
                    
                    usuarioManager.update(usuario);

                    retorno.setError(false);
                    retorno.setMensaje("El usuario "+ nombre+" se activo exitosamente.");

            } catch (Exception e) {
                    retorno.setError(true);
                    retorno.setMensaje("Error al tratar de activar el usuario.");
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
