/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.MensajeDTO;
//import static com.sistem.proyecto.web.controller.BaseController.logger;
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
 * @author daniel
 */
@Controller
@RequestMapping(value = "/proveedores")
public class ProveedorController extends BaseController {

    String atributos = "id,nombre,ruc,email,telefono,telefonoMovil,comentario,"
            + "empresa.id,empresa.nombre,direccion,activo";

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
        try {
            inicializarProveedorManager();
            inicializarImagenManager();

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
        try {
            inicializarProveedorManager();
            inicializarImagenManager();
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

            ejProveedorUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejProveedorUp.setComentario(proveedorRecibido.getComentario());
            ejProveedorUp.setDireccion(proveedorRecibido.getDireccion());
            ejProveedorUp.setEmail(proveedorRecibido.getEmail());
            ejProveedorUp.setNombre(proveedorRecibido.getNombre());
            ejProveedorUp.setTelefono(proveedorRecibido.getTelefono());
            ejProveedorUp.setTelefonoMovil(proveedorRecibido.getTelefonoMovil());
            
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

        try {

            inicializarProveedorManager();
            inicializarEmpresaManager();

            Map<String, Object> proveedor = proveedorManager.getAtributos(
                    new Proveedor(id), atributos.split(","), false, true);
           

            model.addAttribute("proveedor", proveedor);

            model.addAttribute("proveedor", true);


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

        try {

            inicializarProveedorManager();
            inicializarEmpresaManager();

            Map<String, Object> proveedor = proveedorManager.getAtributos(
                    new Proveedor(id), atributos.split(","), false, true);

            model.addAttribute("proveedor", proveedor);

            model.addAttribute("editar", false);

            
        } catch (Exception ex) {

            logger.debug("Error al tratar de visualizar el proveedor ", ex);
        }

        return retorno;

    }
}

