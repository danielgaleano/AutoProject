/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.entity.Rol;
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
        }catch(Exception ex){
        
        }
        return new ModelAndView("usuario");
        
    }
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
   public @ResponseBody MensajeDTO guardar(@ModelAttribute("Usuario") Usuario usuarioRecibido) {
       MensajeDTO mensaje = new MensajeDTO();
       Usuario ejUsuario = new Usuario();
       try{
           inicializarUsuarioManager();
           
           if(usuarioRecibido != null && usuarioRecibido.getDocumento() != null){
               ejUsuario.setDocumento(usuarioRecibido.getDocumento());
               
               ejUsuario = usuarioManager.get(ejUsuario);
               if(ejUsuario != null){
                   mensaje.setError(true);
                   mensaje.setMensaje("El numero de documento ya se encuentra registrado.");
                   return mensaje;
               }else{
                    ejUsuario = new Usuario();
                    ejUsuario.setActivo("S");
                    ejUsuario.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    ejUsuario.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    ejUsuario.setAlias(usuarioRecibido.getAlias());
                    ejUsuario.setClaveAcceso(usuarioRecibido.getClaveAcceso());
                    ejUsuario.setDireccion(usuarioRecibido.getDireccion());
                    ejUsuario.setDocumento(usuarioRecibido.getDocumento());
                    ejUsuario.setEmail(usuarioRecibido.getEmail());
                    ejUsuario.setNombre(usuarioRecibido.getNombre());
                    ejUsuario.setApellido(usuarioRecibido.getApellido());
                    ejUsuario.setTelefono(usuarioRecibido.getTelefono());
                    ejUsuario.setTelefonoMovil(usuarioRecibido.getTelefonoMovil());
                    ejUsuario.setEmpresa(new Empresa(Long.valueOf(usuarioRecibido.getEmpresa().getId())));
                    ejUsuario.setRol(new Rol(Long.valueOf(usuarioRecibido.getRol().getId())));
               }
               
           }else{
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar numero de documento.");
                return mensaje;
           }
             
           
           if(usuarioRecibido.getId() != null){
               Usuario ejUsuarioUp = new Usuario();
               ejUsuarioUp = usuarioManager.get(usuarioRecibido.getId());
               ejUsuarioUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
               ejUsuarioUp.setAlias(usuarioRecibido.getAlias());
               ejUsuarioUp.setClaveAcceso(usuarioRecibido.getClaveAcceso());
               ejUsuarioUp.setDireccion(usuarioRecibido.getDireccion());
               ejUsuarioUp.setDocumento(usuarioRecibido.getDocumento());
               ejUsuarioUp.setEmail(usuarioRecibido.getEmail());
               ejUsuarioUp.setNombre(usuarioRecibido.getNombre());
               ejUsuarioUp.setApellido(usuarioRecibido.getApellido());
               ejUsuarioUp.setTelefono(usuarioRecibido.getTelefono());
               ejUsuarioUp.setTelefonoMovil(usuarioRecibido.getTelefonoMovil());
               ejUsuarioUp.setEmpresa(usuarioRecibido.getEmpresa());
               ejUsuarioUp.setEmpresa(new Empresa(Long.valueOf(usuarioRecibido.getEmpresa().getId())));
               ejUsuarioUp.setRol(new Rol(Long.valueOf(usuarioRecibido.getRol().getId())));
               usuarioManager.update(ejUsuarioUp);  
           }else{
              usuarioManager.save(ejUsuario); 
           }       
           
           
           mensaje.setError(false);
           mensaje.setMensaje("El usuario "+usuarioRecibido.getAlias()+" se guardo exitosamente.");
           
       }catch(Exception e){
           mensaje.setError(true);
           mensaje.setMensaje("Error a guardar el usuario");
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

                    inicializarUsuarioManager();

                    Map<String, Object> empresa = usuarioManager.getAtributos(
					new Usuario(id), atributos.split(","), false, true);

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
