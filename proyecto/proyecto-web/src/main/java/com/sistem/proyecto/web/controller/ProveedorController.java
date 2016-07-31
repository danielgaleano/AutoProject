/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.MensajeDTO;
//import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.util.HashMap;
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
 * @author daniel
 */
@Controller
@RequestMapping(value = "/proveedores")
public class ProveedorController extends BaseController {

    String atributos = "id,nombre,ruc,email,telefono,telefonoMovil,comentario,fax,ciudad,pais,codigoPostal,contacto.id,"
            + "contacto.nombre,contacto.cargo,contacto.telefono,contacto.email,"
            + "contacto.comentario,empresa.id,empresa.nombre,direccion,activo";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaProveedores(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedores");

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        List<Map<String, Object>> listMapProveedores;
        Proveedor ejProveedor = new Proveedor();
        ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {
            inicializarProveedorManager();

            listMapProveedores = proveedorManager.listAtributos(ejProveedor, atributos.split(","), true);

            model.addAttribute("proveedores", listMapProveedores);

        } catch (Exception ex) {
            logger.debug("Error al listar proveedores", ex);
        }

        return retorno;

    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        
        try {
            inicializarEmpresaManager();
            model.addAttribute("tipo", "Crear");
            model.addAttribute("editar", false);

        } catch (Exception ex) {
            logger.debug("Error al crear proveedor", ex);
        }
        return new ModelAndView("proveedor");

    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Proveedor") Proveedor proveedorRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Proveedor ejProveedor = new Proveedor();
        Contacto ejContacto = new Contacto();
        try {
            inicializarProveedorManager();
            inicializarImagenManager();
            inicializarContactoManager();

            if (proveedorRecibido.getRuc() == null || proveedorRecibido.getRuc() != null
                    && proveedorRecibido.getRuc().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El RUc del proveedor no puede estar vacio.");
                return mensaje;
            }

            if (proveedorRecibido.getNombre() == null || proveedorRecibido.getNombre() != null
                    && proveedorRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del proveedor no puede estar vacio.");
                return mensaje;
            }

            if (proveedorRecibido.getTelefono() == null || proveedorRecibido.getTelefono() != null
                    && proveedorRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del proveedor no puede estar vacio.");
                return mensaje;
            }

            ejProveedor.setRuc(proveedorRecibido.getRuc());

            Map<String, Object> proveedorRuc = proveedorManager.getLike(ejProveedor, "id".split(","));

            if (proveedorRuc != null && !proveedorRuc.isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de ruc ya se encuentra registrado.");
                return mensaje;

            }

            ejProveedor = new Proveedor();
            
            if (proveedorRecibido.isTieneContacto()) {

                if (proveedorRecibido != null && (proveedorRecibido.getNombreContacto() == null
                        || proveedorRecibido.getNombreContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El nombre del contacto es obligario.");
                    return mensaje;
                }

                if (proveedorRecibido != null && (proveedorRecibido.getTelefonoContacto() == null
                        || proveedorRecibido.getTelefonoContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El telefono del contacto es obligario.");
                    return mensaje;
                }


                ejContacto = new Contacto();

                ejContacto.setActivo("S");
                ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejContacto.setCargo(proveedorRecibido.getContactoCargo());
                ejContacto.setComentario(proveedorRecibido.getContactoComentario());
                ejContacto.setEmail(proveedorRecibido.getContactoEmail());
                ejContacto.setNombre(proveedorRecibido.getNombreContacto());
                ejContacto.setTelefono(proveedorRecibido.getTelefonoContacto());
                ejContacto.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

                contactoManager.save(ejContacto);

                ejProveedor.setContacto(ejContacto);

            }

            ejProveedor.setActivo("S");
            ejProveedor.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejProveedor.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejProveedor.setDireccion(proveedorRecibido.getDireccion());
            ejProveedor.setRuc(proveedorRecibido.getRuc());
            ejProveedor.setEmail(proveedorRecibido.getEmail());
            ejProveedor.setNombre(proveedorRecibido.getNombre());
            ejProveedor.setComentario(proveedorRecibido.getComentario());
            ejProveedor.setTelefono(proveedorRecibido.getTelefono());
            ejProveedor.setTelefonoMovil(proveedorRecibido.getTelefonoMovil());
            ejProveedor.setCiudad(proveedorRecibido.getCiudad());
            ejProveedor.setCodigoPostal(proveedorRecibido.getCodigoPostal());
            ejProveedor.setPais(proveedorRecibido.getPais());
            ejProveedor.setFax(proveedorRecibido.getFax());

            proveedorManager.save(ejProveedor);
           

            mensaje.setError(false);
            mensaje.setMensaje("El proveedor " + proveedorRecibido.getNombre()+ " se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el proveedor");
            logger.debug("Error al guardar proveedor ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Proveedor") Proveedor proveedorRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Proveedor ejProveedor = new Proveedor();
        Contacto ejContacto = new Contacto();
        try {
            inicializarProveedorManager();
            inicializarImagenManager();
            inicializarContactoManager();
            
            if (proveedorRecibido.getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar el proveedor.");
                return mensaje;
            }

            if (proveedorRecibido.getNombre() == null || proveedorRecibido.getNombre() != null
                    && proveedorRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del proveedor no puede estar vacio.");
                return mensaje;
            }

            if (proveedorRecibido.getTelefono() == null || proveedorRecibido.getTelefono() != null
                    && proveedorRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del proveedor no puede estar vacio.");
                return mensaje;
            }

            Proveedor ejProveedorUp = new Proveedor();
            ejProveedorUp = proveedorManager.get(proveedorRecibido.getId());
            if (proveedorRecibido.isTieneContacto()) {

                if (proveedorRecibido != null && (proveedorRecibido.getNombreContacto() == null
                        || proveedorRecibido.getNombreContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El nombre del contacto es obligario.");
                    return mensaje;
                }

                if (proveedorRecibido != null && (proveedorRecibido.getTelefonoContacto() == null
                        || proveedorRecibido.getTelefonoContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El telefono del contacto es obligario.");
                    return mensaje;
                }

                if (proveedorRecibido.getIdContacto() != null && proveedorRecibido.getIdContacto()
                        .toString().compareToIgnoreCase("") != 0) {

                    ejContacto.setNombre(proveedorRecibido.getNombre());
                    ejContacto.setEmpresa(ejProveedorUp.getEmpresa());
                    
                    Map<String, Object> contactoNombre = contactoManager.getLike(ejContacto, "id".split(","));

                    if (contactoNombre != null && !contactoNombre.isEmpty() 
                            && contactoNombre.get("id").toString()
                                    .compareToIgnoreCase(proveedorRecibido.getIdContacto().toString())  != 0) {
                        mensaje.setError(true);
                        mensaje.setMensaje("El nombre del contacto ya se encuentra registrado.");
                        return mensaje;

                    }
                    ejContacto = new Contacto();

                    ejContacto = contactoManager.get(proveedorRecibido.getIdContacto());

                    ejContacto.setCargo(proveedorRecibido.getContactoCargo());
                    ejContacto.setComentario(proveedorRecibido.getContactoComentario());
                    ejContacto.setEmail(proveedorRecibido.getContactoEmail());
                    ejContacto.setNombre(proveedorRecibido.getNombreContacto());
                    ejContacto.setTelefono(proveedorRecibido.getTelefonoContacto());

                    contactoManager.update(ejContacto);
                    
                    ejProveedorUp.setContacto(ejContacto);
                } else {
                    
                    ejContacto.setNombre(proveedorRecibido.getNombre());
                    ejContacto.setEmpresa(ejProveedorUp.getEmpresa());
                    
                    Map<String, Object> contactoNombre = contactoManager.getLike(ejContacto, "id".split(","));

                    if (contactoNombre != null && !contactoNombre.isEmpty() ) {
                        mensaje.setError(true);
                        mensaje.setMensaje("El nombre del contacto ya se encuentra registrado.");
                        return mensaje;

                    }
                    ejContacto = new Contacto();
                    ejContacto.setActivo("S");
                    ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    ejContacto.setCargo(proveedorRecibido.getContactoCargo());
                    ejContacto.setComentario(proveedorRecibido.getContactoComentario());
                    ejContacto.setEmail(proveedorRecibido.getContactoEmail());
                    ejContacto.setNombre(proveedorRecibido.getNombreContacto());
                    ejContacto.setTelefono(proveedorRecibido.getTelefonoContacto());
                    ejContacto.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

                    contactoManager.save(ejContacto);
                    
                    ejProveedorUp.setContacto(ejContacto);
                }
                

            }

            ejProveedorUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejProveedorUp.setComentario(proveedorRecibido.getComentario());
            ejProveedorUp.setDireccion(proveedorRecibido.getDireccion());
            ejProveedorUp.setEmail(proveedorRecibido.getEmail());
            ejProveedorUp.setNombre(proveedorRecibido.getNombre());
            ejProveedorUp.setTelefono(proveedorRecibido.getTelefono());
            ejProveedorUp.setTelefonoMovil(proveedorRecibido.getTelefonoMovil());
            ejProveedorUp.setCiudad(proveedorRecibido.getCiudad());
            ejProveedorUp.setCodigoPostal(proveedorRecibido.getCodigoPostal());
            ejProveedorUp.setPais(proveedorRecibido.getPais());
            ejProveedorUp.setFax(proveedorRecibido.getFax());
            
            proveedorManager.update(ejProveedorUp);

            mensaje.setError(false);
            mensaje.setMensaje("El proveedor " + proveedorRecibido.getNombre() + " se modifico exitosamente.");
            return mensaje;

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el proveedor");
            logger.debug("Error al editar el proveedor ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        Proveedor ejProveedor = new Proveedor();
        ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        try {

            inicializarProveedorManager();
            
            ejProveedor = proveedorManager.get(ejProveedor);

            if (ejProveedor != null) {
                nombre = ejProveedor.getNombre().toString();
            }

            if (ejProveedor != null && ejProveedor.getActivo()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El proveedor " + nombre + " ya se encuentra desactivado.");
            }
            ejProveedor.setActivo("N");
            ejProveedor.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejProveedor.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            proveedorManager.update(ejProveedor);

            retorno.setError(false);
            retorno.setMensaje("El proveedor " + nombre + " se desactivo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el proveedor.");
            logger.debug("Error al tratar de desactivar el proveedor ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        
        Proveedor ejProveedor = new Proveedor();
        ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {

            inicializarProveedorManager();

            ejProveedor = proveedorManager.get(ejProveedor);

            if (ejProveedor != null) {
                nombre = ejProveedor.getNombre().toString();
            }

            if (ejProveedor != null && ejProveedor.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El proveedor " + nombre + " ya se encuentra activo.");
            }
            ejProveedor.setActivo("S");
            ejProveedor.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejProveedor.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            proveedorManager.update(ejProveedor);

            retorno.setError(false);
            retorno.setMensaje("El proveedor " + nombre + " se activo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el proveedor.");
            logger.debug("Error al tratar de activar el proveedor ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedor");
        String nombre = "";
        Map<String, Object> retornoMap = new HashMap<String, Object>();
        try {

            inicializarProveedorManager();
            inicializarEmpresaManager();

            Map<String, Object> proveedor = proveedorManager.getAtributos(
                    new Proveedor(id), atributos.split(","), false, true);
            
            retornoMap.putAll(proveedor);
            
            for (Map.Entry<String, Object> entry : proveedor.entrySet()) {
                
                
                    if (entry.getKey().compareToIgnoreCase("contacto.id") == 0) {
                        retornoMap.put("idContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.nombre") == 0) {
                        retornoMap.put("nombreContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.cargo") == 0) {
                        retornoMap.put("contactoCargo", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.telefono") == 0) {
                        retornoMap.put("telefonoContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.email") == 0) {
                        retornoMap.put("contactoEmail", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.comentario") == 0) {
                        retornoMap.put("contactoComentario", entry.getValue());
                    }
               
            }
           

            model.addAttribute("proveedor", retornoMap);

            model.addAttribute("editar", true);


        } catch (Exception ex) {

            logger.debug("Error al tratar de editar el proveedor ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView visualizar(@PathVariable("id") Long id, Model model) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedor");
        String nombre = "";
        Map<String, Object> retornoMap = new HashMap<String, Object>();
        try {

            inicializarProveedorManager();
            inicializarEmpresaManager();

            Map<String, Object> proveedor = proveedorManager.getAtributos(
                    new Proveedor(id), atributos.split(","), false, true);
            
            retornoMap.putAll(proveedor);
            
            for (Map.Entry<String, Object> entry : proveedor.entrySet()) {
                
                
                    if (entry.getKey().compareToIgnoreCase("contacto.id") == 0) {
                        retornoMap.put("idContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.nombre") == 0) {
                        retornoMap.put("nombreContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.cargo") == 0) {
                        retornoMap.put("contactoCargo", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.telefono") == 0) {
                        retornoMap.put("telefonoContacto", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.email") == 0) {
                        retornoMap.put("contactoEmail", entry.getValue());
                    } else if (entry.getKey().compareToIgnoreCase("contacto.comentario") == 0) {
                        retornoMap.put("contactoComentario", entry.getValue());
                    }
               
            }

            model.addAttribute("proveedor", retornoMap);

            model.addAttribute("editar", false);

            
        } catch (Exception ex) {

            logger.debug("Error al tratar de visualizar el proveedor ", ex);
        }

        return retorno;

    }
}

