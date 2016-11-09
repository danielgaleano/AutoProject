/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.entity.Empleo;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
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
import com.sistem.proyecto.utils.DTORetorno;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/clientes")
public class ClienteController extends BaseController {

    String atributos = "id,nombre,documento,email,telefono,telefonoMovil,comentario,pais,sexo,fechaNacimiento,contacto.id,"
            + "contacto.nombre,contacto.documento,actividad,contacto.cargo,contacto.telefono,contacto.movil,contacto.email,"
            + "contacto.comentario,empresa.id,empresa.nombre,direccion,activo,"
            + "empleo.id,empleo.nombreEmpresa,empleo.cargo,empleo.antiguedad,empleo.salario,empleo.telefono,empleo.direccion,empleo.actividad,empleo.comentario";


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaClientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("clientesListar");
        return retorno;

    }

    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cliente");
        model.addAttribute("action", "CREAR");
        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cliente");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView formEdit(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cliente");
        model.addAttribute("action", "EDITAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ordenarPor = "nombre";
        Cliente ejCliente = new Cliente();
        ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarClienteManager();

            Gson gson = new Gson();
            String camposFiltros = null;
            String valorFiltro = null;

            if (filtrar) {
                FilterDTO filtro = gson.fromJson(filtros, FilterDTO.class);
                if (filtro.getGroupOp().compareToIgnoreCase("OR") == 0) {
                    for (ReglaDTO regla : filtro.getRules()) {
                        if (camposFiltros == null) {
                            camposFiltros = regla.getField();
                            valorFiltro = regla.getData();
                        } else {
                            camposFiltros += "," + regla.getField();
                        }
                    }
                } else {
                    //ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            // ejemplo.setActivo("S");

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            if (!todos) {
                total = clienteManager.list(ejCliente, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = clienteManager.listAtributos(ejCliente, atributos.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, null, null, null, null, null, true);

            if (todos) {
                total = listMapGrupos.size();
            }
            Integer totalPaginas = total / cantidad;

            retorno.setTotal(totalPaginas + 1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {

            logger.error("Error al listar", e);
        }

        return retorno;
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Cliente") Cliente clienteRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Contacto ejContacto = new Contacto();
        try {
            inicializarClienteManager();
            inicializarImagenManager();
            inicializarContactoManager();
            if (clienteRecibido.getDocumento() == null || clienteRecibido.getDocumento() != null
                    && clienteRecibido.getDocumento().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El RUC/CI del cliente no puede estar vacio.");
                return mensaje;
            }

            if (clienteRecibido.getNombre() == null || clienteRecibido.getNombre() != null
                    && clienteRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del cliente no puede estar vacio.");
                return mensaje;
            }

//            if (clienteRecibido.getTelefono() == null || clienteRecibido.getTelefono() != null
//                    && clienteRecibido.getTelefono().compareToIgnoreCase("") == 0) {
//                mensaje.setError(true);
//                mensaje.setMensaje("El telefono del cliente no puede estar vacia.");
//                return mensaje;
//            }

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
            ejCliente.setPais(clienteRecibido.getPais());
            ejCliente.setComentario(clienteRecibido.getComentario());
            ejCliente.setTelefono(clienteRecibido.getTelefono());
            ejCliente.setTelefonoMovil(clienteRecibido.getTelefonoMovil());

            clienteManager.save(ejCliente);
            
            mensaje.setId(ejCliente.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El cliente " + clienteRecibido.getNombre() + " se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno pedidoForm(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarClienteManager();
            Cliente ejCliente = new Cliente();
            ejCliente.setId(id);

            Map<String, Object> ejPedido = clienteManager.getAtributos(ejCliente, atributos.split(","));

            retorno.setData(ejPedido);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente el cliente");

        } catch (Exception ex) {
            logger.error("Error al obtener el cliente", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener el cliente");
        }

        return retorno;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Cliente") Cliente clienteRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Contacto ejContacto = new Contacto();
        try {
            inicializarClienteManager();
            inicializarImagenManager();
            inicializarContactoManager();
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

//            if (clienteRecibido.getTelefono() == null || clienteRecibido.getTelefono() != null
//                    && clienteRecibido.getTelefono().compareToIgnoreCase("") == 0) {
//                mensaje.setError(true);
//                mensaje.setMensaje("El telefono del cliente no puede estar vacia.");
//                return mensaje;
//            }

            Cliente ejClienteUp = new Cliente();
            ejClienteUp = clienteManager.get(clienteRecibido.getId());
            
            ejClienteUp.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejClienteUp.setComentario(clienteRecibido.getComentario());
            ejClienteUp.setDireccion(clienteRecibido.getDireccion());
            ejClienteUp.setEmail(clienteRecibido.getEmail());
            ejClienteUp.setNombre(clienteRecibido.getNombre());
            ejClienteUp.setPais(clienteRecibido.getPais());
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

    @RequestMapping(value = "/{id}/contacto/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardarContacto(@PathVariable("id") Long id, @ModelAttribute("Contacto") Contacto contacto) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Contacto ejContacto = new Contacto();
        try {
            inicializarClienteManager();
            inicializarContactoManager();
            
            if (id == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Se debe guardar los datos personales del cliente.");
                return mensaje;
            }
            
            ejCliente = clienteManager.get(id);
            
            if (contacto != null && (contacto.getNombre() == null
                    || contacto.getNombre().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del contacto es obligario.");
                return mensaje;
            }

            if (contacto != null && (contacto.getTelefono() == null
                    || contacto.getTelefono().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del contacto es obligario.");
                return mensaje;
            }

            ejContacto = new Contacto();

            ejContacto.setActivo("S");
            ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejContacto.setCargo(contacto.getCargo());
            ejContacto.setComentario(contacto.getComentario());
            ejContacto.setEmail(contacto.getEmail());
            ejContacto.setDocumento(contacto.getDocumento());
            ejContacto.setMovil(contacto.getMovil());
            ejContacto.setNombre(contacto.getNombre());
            ejContacto.setTelefono(contacto.getTelefono());
            ejContacto.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            contactoManager.save(ejContacto);

            ejCliente.setContacto(ejContacto);

            clienteManager.update(ejCliente);
            
            mensaje.setId(ejContacto.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El contacto " + ejContacto.getNombre() + " se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el contacto");
            logger.error("Error al guardar contacto ", ex);
        }

        return mensaje;
    }
    
    @RequestMapping(value = "/contacto/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editarContacto(@ModelAttribute("Contacto") Contacto contacto) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Contacto ejContacto = new Contacto();
        try {
            inicializarClienteManager();
            inicializarContactoManager();
            
            if (contacto.getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar los datos del contacto.");
                return mensaje;
            }
            
            if (contacto != null && (contacto.getNombre() == null
                    || contacto.getNombre().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del contacto es obligario.");
                return mensaje;
            }

            if (contacto != null && (contacto.getTelefono() == null
                    || contacto.getTelefono().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del contacto es obligario.");
                return mensaje;
            }

            ejContacto = contactoManager.get(contacto.getId());

            ejContacto.setActivo("S");
            ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejContacto.setCargo(contacto.getCargo());
            ejContacto.setComentario(contacto.getComentario());
            ejContacto.setEmail(contacto.getEmail());
            ejContacto.setNombre(contacto.getNombre());
            ejContacto.setDocumento(contacto.getDocumento());
            ejContacto.setMovil(contacto.getMovil());
            ejContacto.setTelefono(contacto.getTelefono());
            ejContacto.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            contactoManager.update(ejContacto);

            
            mensaje.setId(ejContacto.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El contacto " + ejContacto.getNombre() + " se modifico exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al editar el contacto");
            logger.error("Error al editar contacto ", ex);
        }

        return mensaje;
    }
    
    @RequestMapping(value = "/{id}/empleo/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardarEmpleo(@PathVariable("id") Long id, @ModelAttribute("Empleo") Empleo empleo) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Empleo ejEmpleo = new Empleo();
        try {
            inicializarClienteManager();
            inicializarEmpleoManager();
            
            if (id == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Se debe guardar los datos personales del cliente.");
                return mensaje;
            }
            
            ejCliente = clienteManager.get(id);
            
            if (empleo != null && (empleo.getNombreEmpresa()== null
                    || empleo.getNombreEmpresa().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la Empresa es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getActividad()== null
                    || empleo.getActividad().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo actividad es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getCargo()== null
                    || empleo.getCargo().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo cargo es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getAntiguedad()== null
                    || empleo.getAntiguedad().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo antiguedad es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getSalario()== null
                    || empleo.getSalario().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo salario es obligario.");
                return mensaje;
            }

            if (empleo != null && (empleo.getTelefono() == null
                    || empleo.getTelefono().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del contacto es obligario.");
                return mensaje;
            }

            ejEmpleo = new Empleo();

            ejEmpleo.setActivo("S");
            ejEmpleo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejEmpleo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejEmpleo.setCargo(empleo.getCargo());
            ejEmpleo.setComentario(empleo.getComentario());
            ejEmpleo.setAntiguedad(empleo.getAntiguedad());
            ejEmpleo.setNombreEmpresa(empleo.getNombreEmpresa());
            ejEmpleo.setTelefono(empleo.getTelefono());
            ejEmpleo.setSalario(empleo.getSalario());
            ejEmpleo.setActividad(empleo.getActividad());
            ejEmpleo.setDireccion(empleo.getDireccion());

            empleoManager.save(ejEmpleo);

            ejCliente.setEmpleo(ejEmpleo);

            clienteManager.update(ejCliente);

            mensaje.setId(ejEmpleo.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El dato laboral se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el dato laboral");
            logger.error("Error al guardar dato laboral ", ex);
        }

        return mensaje;
    }
    
    @RequestMapping(value = "/empleo/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editarEmpleo( @ModelAttribute("Empleo") Empleo empleo) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Cliente ejCliente = new Cliente();
        Empleo ejEmpleo = new Empleo();
        try {
            inicializarClienteManager();
            inicializarEmpleoManager();
            
            if (empleo.getId() == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar los datos laborales.");
                return mensaje;
            }                       
            
            if (empleo != null && (empleo.getNombreEmpresa()== null
                    || empleo.getNombreEmpresa().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la Empresa es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getActividad()== null
                    || empleo.getActividad().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo actividad es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getCargo()== null
                    || empleo.getCargo().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo cargo es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getAntiguedad()== null
                    || empleo.getAntiguedad().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo antiguedad es obligario.");
                return mensaje;
            }
            
            if (empleo != null && (empleo.getSalario()== null
                    || empleo.getSalario().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo salario es obligario.");
                return mensaje;
            }

            if (empleo != null && (empleo.getTelefono() == null
                    || empleo.getTelefono().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El telefono del contacto es obligario.");
                return mensaje;
            }

            ejEmpleo = empleoManager.get(empleo.getId());

            ejEmpleo.setActivo("S");
            ejEmpleo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejEmpleo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejEmpleo.setCargo(empleo.getCargo());
            ejEmpleo.setComentario(empleo.getComentario());
            ejEmpleo.setAntiguedad(empleo.getAntiguedad());
            ejEmpleo.setNombreEmpresa(empleo.getNombreEmpresa());
            ejEmpleo.setTelefono(empleo.getTelefono());
            ejEmpleo.setSalario(empleo.getSalario());
            ejEmpleo.setActividad(empleo.getActividad());
            ejEmpleo.setDireccion(empleo.getDireccion());

            empleoManager.update(ejEmpleo);

            mensaje.setId(ejEmpleo.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El dato laboral se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al editar el dato laboral");
            logger.error("Error al editar dato laboral ", ex);
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
        ejCliente.setId(id);
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
        ejCliente.setId(id);
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

//    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
//    public @ResponseBody
//    ModelAndView editar(@PathVariable("id") Long id, Model model) {
//        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        ModelAndView retorno = new ModelAndView();
//        retorno.setViewName("cliente");
//        String nombre = "";
//        Map<String, Object> retornoMap = new HashMap<String, Object>();
//        try {
//
//            inicializarClienteManager();
//            inicializarEmpresaManager();
//
//            Map<String, Object> cliente = clienteManager.getAtributos(
//                    new Cliente(id), atributos.split(","), false, true);
//
//            retornoMap.putAll(cliente);
//
//            for (Map.Entry<String, Object> entry : cliente.entrySet()) {
//
//                if (entry.getKey().compareToIgnoreCase("contacto.id") == 0) {
//                    retornoMap.put("idContacto", entry.getValue());
//                } else if (entry.getKey().compareToIgnoreCase("contacto.nombre") == 0) {
//                    retornoMap.put("nombreContacto", entry.getValue());
//                } else if (entry.getKey().compareToIgnoreCase("contacto.cargo") == 0) {
//                    retornoMap.put("contactoCargo", entry.getValue());
//                } else if (entry.getKey().compareToIgnoreCase("contacto.telefono") == 0) {
//                    retornoMap.put("telefonoContacto", entry.getValue());
//                } else if (entry.getKey().compareToIgnoreCase("contacto.email") == 0) {
//                    retornoMap.put("contactoEmail", entry.getValue());
//                } else if (entry.getKey().compareToIgnoreCase("contacto.comentario") == 0) {
//                    retornoMap.put("contactoComentario", entry.getValue());
//                }
//
//            }
//
//            model.addAttribute("cliente", retornoMap);
//
//            model.addAttribute("editar", true);
//
//        } catch (Exception ex) {
//
//            logger.debug("Error al tratar de editar el cliente ", ex);
//        }
//
//        return retorno;
//
//    }
//    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
//    public @ResponseBody
//    ModelAndView visualizar(@PathVariable("id") Long id, Model model) {
//
//        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//
//        ModelAndView retorno = new ModelAndView();
//        retorno.setViewName("cliente");
//        String nombre = "";
//        Map<String, Object> retornoMap = new HashMap<String, Object>();
//        try {
//
//            inicializarClienteManager();
////            inicializarEmpresaManager();
////
////            Map<String, Object> cliente = clienteManager.getAtributos(
////                    new Cliente(id), atributos.split(","), false, true);
////            retornoMap.putAll(cliente);
////
////            for (Map.Entry<String, Object> entry : cliente.entrySet()) {
////
////                if (entry.getKey().compareToIgnoreCase("contacto.id") == 0) {
////                    retornoMap.put("idContacto", entry.getValue());
////                } else if (entry.getKey().compareToIgnoreCase("contacto.nombre") == 0) {
////                    retornoMap.put("nombreContacto", entry.getValue());
////                } else if (entry.getKey().compareToIgnoreCase("contacto.cargo") == 0) {
////                    retornoMap.put("contactoCargo", entry.getValue());
////                } else if (entry.getKey().compareToIgnoreCase("contacto.telefono") == 0) {
////                    retornoMap.put("telefonoContacto", entry.getValue());
////                } else if (entry.getKey().compareToIgnoreCase("contacto.email") == 0) {
////                    retornoMap.put("contactoEmail", entry.getValue());
////                } else if (entry.getKey().compareToIgnoreCase("contacto.comentario") == 0) {
////                    retornoMap.put("contactoComentario", entry.getValue());
////                }
////
////            }
////
////            model.addAttribute("cliente", retornoMap);
//
//            model.addAttribute("editar", false);
//
//        } catch (Exception ex) {
//
//            logger.debug("Error al tratar de visualizar el cliente ", ex);
//        }
//
//        return retorno;
//
//    }

}
