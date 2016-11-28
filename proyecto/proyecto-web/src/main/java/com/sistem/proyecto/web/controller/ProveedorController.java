/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
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
import com.sistem.proyecto.manager.utils.DTORetorno;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/proveedores")
public class ProveedorController extends BaseController {

    String atributos = "id,nombre,ruc,email,telefono,telefonoMovil,comentario,fax,ciudad,pais,codigoPostal,contacto.id,"
            + "contacto.nombre,contacto.documento,contacto.movil,contacto.cargo,contacto.telefono,contacto.email,"
            + "contacto.comentario,empresa.id,empresa.nombre,direccion,activo";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaProveedores(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedoresListar");       
        return retorno;

    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedorForm");
        model.addAttribute("action", "CREAR");
        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedorForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView formEdit(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("proveedorForm");
        model.addAttribute("action", "EDITAR");
        model.addAttribute("id", id);
        return retorno;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno proveedorForm(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarProveedorManager();
            Proveedor ejProveedor = new Proveedor();
            ejProveedor.setId(id);

            Map<String, Object> mapProveedor = proveedorManager.getAtributos(ejProveedor, atributos.split(","));

            retorno.setData(mapProveedor);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente el proveedor");

        } catch (Exception ex) {
            logger.error("Error al obtener el cliente", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener el proveedor");
        }

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
        Proveedor ejProveedor = new Proveedor();
        ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarProveedorManager();

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
                total = proveedorManager.list(ejProveedor, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = proveedorManager.listAtributos(ejProveedor, atributos.split(","), todos, inicio, cantidad,
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
           
            mensaje.setId(ejProveedor.getId());
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
        ejProveedor.setId(id);
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
        ejProveedor.setId(id);
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
    
    @RequestMapping(value = "/{id}/contacto/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardarContacto(@PathVariable("id") Long id, @ModelAttribute("Contacto") Contacto contacto) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Proveedor ejProveedor = new Proveedor();
        Contacto ejContacto = new Contacto();
        try {
            inicializarProveedorManager();
            inicializarContactoManager();
            
            if (id == null) {
                mensaje.setError(true);
                mensaje.setMensaje("Se debe guardar los datos personales del cliente.");
                return mensaje;
            }
            
            ejProveedor = proveedorManager.get(id);
            
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

            ejProveedor.setContacto(ejContacto);

            proveedorManager.update(ejProveedor);
            
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
        Contacto ejContacto = new Contacto();
        try {
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
}

