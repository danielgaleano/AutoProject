/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Rol;
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
@RequestMapping(value = "/clientes")
public class ClienteController extends BaseController {

    String atributos = "id,nombre,documento,email,telefono,telefonoMovil,comentario,"
            + "empresa.id,empresa.nombre,direccion,activo";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaClientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("clientes");

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        List<Map<String, Object>> listMapClientes;
        Cliente ejCliente = new Cliente();
        ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {
            inicializarClienteManager();

            listMapClientes = clienteManager.listAtributos(ejCliente, atributos.split(","), true);

            model.addAttribute("clientes", listMapClientes);

        } catch (Exception ex) {
            logger.debug("Error al listar clientes", ex);
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
            logger.debug("Error al crear cliente", ex);
        }
        return new ModelAndView("cliente");

    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Cliente") Cliente clienteRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        try {
            inicializarClienteManager();
            inicializarImagenManager();

            if (clienteRecibido.getDocumento() == null || clienteRecibido.getDocumento() != null
                    && clienteRecibido.getDocumento().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El documento del cliente no puede estar vacio.");
                return mensaje;
            }

            if (clienteRecibido.getNombre() == null || clienteRecibido.getNombre() != null
                    && clienteRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del cliente no puede estar vacio.");
                return mensaje;
            }

            if (clienteRecibido.getTelefono() == null || clienteRecibido.getTelefono() != null
                    && clienteRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del cliente no puede estar vacia.");
                return mensaje;
            }

            ejCliente.setDocumento(clienteRecibido.getDocumento());

            Map<String, Object> clienteDocumento = clienteManager.getLike(ejCliente, "id".split(","));

            if (clienteDocumento != null && !clienteDocumento.isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de documento ya se encuentra registrado.");
                return mensaje;

            }

            ejCliente = new Cliente();

            ejCliente.setActivo("S");
            ejCliente.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejCliente.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejCliente.setDireccion(clienteRecibido.getDireccion());
            ejCliente.setDocumento(clienteRecibido.getDocumento());
            ejCliente.setEmail(clienteRecibido.getEmail());
            ejCliente.setNombre(clienteRecibido.getNombre());
            ejCliente.setComentario(clienteRecibido.getComentario());
            ejCliente.setTelefono(clienteRecibido.getTelefono());
            ejCliente.setTelefonoMovil(clienteRecibido.getTelefonoMovil());

            clienteManager.save(ejCliente);
           

            mensaje.setError(false);
            mensaje.setMensaje("El cliente " + clienteRecibido.getNombre()+ " se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Cliente") Cliente clienteRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        try {
            inicializarClienteManager();
            inicializarImagenManager();
            if (clienteRecibido.getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar el cliente.");
                return mensaje;
            }

            if (clienteRecibido.getNombre() == null || clienteRecibido.getNombre() != null
                    && clienteRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del cliente no puede estar vacio.");
                return mensaje;
            }

            if (clienteRecibido.getTelefono() == null || clienteRecibido.getTelefono() != null
                    && clienteRecibido.getTelefono().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del cliente no puede estar vacia.");
                return mensaje;
            }

            Cliente ejClienteUp = new Cliente();
            ejClienteUp = clienteManager.get(clienteRecibido.getId());

            ejClienteUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejClienteUp.setComentario(clienteRecibido.getComentario());
            ejClienteUp.setDireccion(clienteRecibido.getDireccion());
            ejClienteUp.setEmail(clienteRecibido.getEmail());
            ejClienteUp.setNombre(clienteRecibido.getNombre());
            ejClienteUp.setTelefono(clienteRecibido.getTelefono());
            ejClienteUp.setTelefonoMovil(clienteRecibido.getTelefonoMovil());
            
            clienteManager.update(ejClienteUp);

            mensaje.setError(false);
            mensaje.setMensaje("El cliente " + clienteRecibido.getNombre() + " se modifico exitosamente.");
            return mensaje;

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al ediatar el cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        Cliente ejCliente = new Cliente();
        ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        try {

            inicializarClienteManager();
            
            ejCliente = clienteManager.get(ejCliente);

            if (ejCliente != null) {
                nombre = ejCliente.getNombre().toString();
            }

            if (ejCliente != null && ejCliente.getActivo()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El cliente " + nombre + " ya se encuentra desactivada.");
            }
            ejCliente.setActivo("N");
            ejCliente.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejCliente.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            clienteManager.update(ejCliente);

            retorno.setError(false);
            retorno.setMensaje("El Cliente " + nombre + " se desactivo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el cliente.");
            logger.debug("Error al tratar de desactivar el cliente ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        
        Cliente ejCliente = new Cliente();
        ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {

            inicializarClienteManager();

            ejCliente = clienteManager.get(ejCliente);

            if (ejCliente != null) {
                nombre = ejCliente.getNombre().toString();
            }

            if (ejCliente != null && ejCliente.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El cliente " + nombre + " ya se encuentra activo.");
            }
            ejCliente.setActivo("S");
            ejCliente.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejCliente.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            clienteManager.update(ejCliente);

            retorno.setError(false);
            retorno.setMensaje("El cliente " + nombre + " se activo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el cliente.");
            logger.debug("Error al tratar de activar el cliente ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ModelAndView retorno = new ModelAndView();
                    retorno.setViewName("cliente");
        String nombre = "";

        try {

            inicializarClienteManager();
            inicializarEmpresaManager();

            Map<String, Object> cliente = clienteManager.getAtributos(
                    new Cliente(id), atributos.split(","), false, true);
           

            model.addAttribute("cliente", cliente);

            model.addAttribute("editar", true);


        } catch (Exception ex) {

            logger.debug("Error al tratar de editar el cliente ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView visualizar(@PathVariable("id") Long id, Model model) {
        
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cliente");
        String nombre = "";

        try {

            inicializarClienteManager();
            inicializarEmpresaManager();

            Map<String, Object> cliente = clienteManager.getAtributos(
                    new Cliente(id), atributos.split(","), false, true);

            model.addAttribute("cliente", cliente);

            model.addAttribute("editar", false);

            
        } catch (Exception ex) {

            logger.debug("Error al tratar de visualizar el cliente ", ex);
        }

        return retorno;

    }
}
