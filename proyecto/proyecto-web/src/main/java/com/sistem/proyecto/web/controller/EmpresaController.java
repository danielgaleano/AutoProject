/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.Base64Bytes;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.ReglaDTO;
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
 * Controller para la entidad Empresa. Implementación de mappings, servicios
 * REST y métodos de mapeo privados y públicos.
 *
 */
@Controller
@RequestMapping(value = "/empresas")
public class EmpresaController extends BaseController {

    String atributos = "id,nombre,descripcion,email,ruc,telefono,telefonoMovil,contacto.id,"
            + "contacto.nombre,contacto.cargo,contacto.telefono,contacto.email,"
            + "contacto.comentario,direccion,activo";

    /**
     * Mapping para el metodo GET de la vista listaEmpresa.
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaEmpresa(Model model) {
        ModelAndView retorno = new ModelAndView();
        Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
//            inicializarEmpresaManager();
//            System.out.println(autentication.getName());
//            System.out.println(userDetail.getNombre());
//            Empresa ejemplo = new Empresa();
//
//            List<Map<String, Object>> listMapEmpresas = empresaManager.listAtributos(ejemplo, atributos.split(","), true);
//
//            model.addAttribute("empresas", listMapEmpresas);

            retorno.setViewName("empresasListar");

        } catch (Exception ex) {
            logger.error("Error al obtener la vista de empresas", ex);
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
        Empresa ejemplo = new Empresa();

        try {

            inicializarEmpresaManager();

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
                total = empresaManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            List<Map<String, Object>> listMapGrupos = empresaManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad, 
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro, 
                    null, null, null, null, null, null, null, null, true);
                    
                    
                    
            if (todos) {
                total = listMapGrupos.size();
            }
            Integer totalPaginas =  total / cantidad;
            
            retorno.setTotal(totalPaginas+1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {

            logger.error("Error al listar", e);
        }

        return retorno;
    }

    /**
     * Mapping para el metodo GET de la vista crear Empresa.
     *
     */
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        model.addAttribute("tipo", "Crear");
        return new ModelAndView("empresa");
    }

    /**
     * Mapping para el metodo POST de la vista guardar. (agregar Empresa)
     *
     * @param empresaRecibido la nueva entidad Empresa recibida de la vista
     * @return
     */
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Empresa") Empresa empresaRecibido) {
        MensajeDTO mensaje = new MensajeDTO();
        Empresa ejEmpresa = new Empresa();
        Contacto ejContacto = new Contacto();
        try {
            inicializarEmpresaManager();
            inicializarImagenManager();
            inicializarContactoManager();

            if (empresaRecibido != null && (empresaRecibido.getRuc() == null
                    || empresaRecibido.getRuc().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El RUC de la empresa no puede estar vacio.");
                return mensaje;
            }

            if (empresaRecibido != null && (empresaRecibido.getNombre() == null
                    || empresaRecibido.getNombre().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la empresa no puede estar vacio.");
                return mensaje;
            }

            if (empresaRecibido != null && (empresaRecibido.getDireccion() == null
                    || empresaRecibido.getDireccion().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar una direccion para la empresa.");
                return mensaje;
            }

            if (empresaRecibido != null && (empresaRecibido.getEmail() == null
                    || empresaRecibido.getEmail().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un email para la empresa.");
                return mensaje;
            }

            ejEmpresa.setRuc(empresaRecibido.getRuc());

            ejEmpresa = empresaManager.get(ejEmpresa);
            if (ejEmpresa != null) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de ruc ya se encuentra registrado.");
                return mensaje;
            }

            ejEmpresa = new Empresa();

            if (empresaRecibido.isTieneContacto()) {

                if (empresaRecibido != null && (empresaRecibido.getNombreContacto() == null
                        || empresaRecibido.getNombreContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El nombre del contacto es obligario.");
                    return mensaje;
                }

                if (empresaRecibido != null && (empresaRecibido.getTelefonoContacto() == null
                        || empresaRecibido.getTelefonoContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El telefono del contacto es obligario.");
                    return mensaje;
                }

                ejContacto = new Contacto();

                ejContacto.setActivo("S");
                ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejContacto.setCargo(empresaRecibido.getContactoCargo());
                ejContacto.setComentario(empresaRecibido.getContactoComentario());
                ejContacto.setEmail(empresaRecibido.getContactoEmail());
                ejContacto.setNombre(empresaRecibido.getNombreContacto());
                ejContacto.setTelefono(empresaRecibido.getTelefonoContacto());

                contactoManager.save(ejContacto);

                ejEmpresa.setContacto(ejContacto);

            }

            Imagen imagenP = null;

            String imagenPortada = empresaRecibido.getImagenPort();

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

            empresaManager.save(ejEmpresa);

            if (imagenPortada != null && !imagenPortada.equals("")
                    && imagenPortada.length() > 0) {
                imagenP = new Imagen();
                imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                String extension = imagenPortada.split(";")[0];
                extension = extension.substring(extension.indexOf("/") + 1);
                imagenP.setNombreTabla("empresa");
                imagenP.setNombreImagen(empresaRecibido.getNombre() + "." + extension);
                imagenP.setActivo("S");
                imagenP.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                imagenP.setEmpresa(ejEmpresa);
                imagenP.setEntidadId(ejEmpresa.getId());

                imagenManager.save(imagenP);

            }

            mensaje.setError(false);
            mensaje.setMensaje("La empresa " + empresaRecibido.getNombre() + " se guardo exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar la empresa");
            System.out.println("Error");
        }

        return mensaje;
    }

    /**
     * Mapping para el metodo POST de la vista edita. (edita Empresa)
     *
     * @param empresaRecibido entidad Empresa recibida de la vista
     * @return
     */
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Empresa") Empresa empresaRecibido) {
        MensajeDTO mensaje = new MensajeDTO();
        Empresa ejEmpresa = new Empresa();
        Contacto ejContacto = new Contacto();
        try {
            inicializarEmpresaManager();
            inicializarImagenManager();
            inicializarContactoManager();

            if (empresaRecibido != null && (empresaRecibido.getNombre() == null
                    || empresaRecibido.getNombre().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre de la empresa no puede estar vacio.");
                return mensaje;
            }

            if (empresaRecibido != null && (empresaRecibido.getDireccion() == null
                    || empresaRecibido.getDireccion().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar una direccion para la empresa.");
                return mensaje;
            }

            if (empresaRecibido != null && (empresaRecibido.getEmail() == null
                    || empresaRecibido.getEmail().compareToIgnoreCase("") == 0)) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un email para la empresa.");
                return mensaje;
            }

            ejEmpresa = empresaManager.get(empresaRecibido.getId());

            if (empresaRecibido.isTieneContacto()) {

                if (empresaRecibido != null && (empresaRecibido.getNombreContacto() == null
                        || empresaRecibido.getNombreContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El nombre del contacto es obligario.");
                    return mensaje;
                }

                if (empresaRecibido != null && (empresaRecibido.getTelefonoContacto() == null
                        || empresaRecibido.getTelefonoContacto().compareToIgnoreCase("") == 0)) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El telefono del contacto es obligario.");
                    return mensaje;
                }

                if (empresaRecibido.getIdContacto() != null && empresaRecibido.getIdContacto()
                        .toString().compareToIgnoreCase("") != 0) {

                    ejContacto.setNombre(empresaRecibido.getNombre());
                    ejContacto.setEmpresa(ejEmpresa);

                    Map<String, Object> contactoNombre = contactoManager.getLike(ejContacto, "id".split(","));

                    if (contactoNombre != null && !contactoNombre.isEmpty()
                            && contactoNombre.get("id").toString()
                            .compareToIgnoreCase(empresaRecibido.getIdContacto().toString()) != 0) {
                        mensaje.setError(true);
                        mensaje.setMensaje("El nombre del contacto ya se encuentra registrado.");
                        return mensaje;

                    }
                    ejContacto = new Contacto();

                    ejContacto = contactoManager.get(empresaRecibido.getIdContacto());

                    ejContacto.setCargo(empresaRecibido.getContactoCargo());
                    ejContacto.setComentario(empresaRecibido.getContactoComentario());
                    ejContacto.setEmail(empresaRecibido.getContactoEmail());
                    ejContacto.setNombre(empresaRecibido.getNombreContacto());
                    ejContacto.setTelefono(empresaRecibido.getTelefonoContacto());

                    contactoManager.update(ejContacto);

                    ejEmpresa.setContacto(ejContacto);
                } else {

                    ejContacto.setNombre(empresaRecibido.getNombre());
                    ejContacto.setEmpresa(ejEmpresa);

                    Map<String, Object> contactoNombre = contactoManager.getLike(ejContacto, "id".split(","));

                    if (contactoNombre != null && !contactoNombre.isEmpty()) {
                        mensaje.setError(true);
                        mensaje.setMensaje("El nombre del contacto ya se encuentra registrado.");
                        return mensaje;

                    }
                    ejContacto = new Contacto();
                    ejContacto.setActivo("S");
                    ejContacto.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    ejContacto.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    ejContacto.setCargo(empresaRecibido.getContactoCargo());
                    ejContacto.setComentario(empresaRecibido.getContactoComentario());
                    ejContacto.setEmail(empresaRecibido.getContactoEmail());
                    ejContacto.setNombre(empresaRecibido.getNombreContacto());
                    ejContacto.setTelefono(empresaRecibido.getTelefonoContacto());
                    ejContacto.setEmpresa(ejEmpresa);

                    contactoManager.save(ejContacto);

                    ejEmpresa.setContacto(ejContacto);
                }

            }

            Imagen imagenP = null;

            String imagenPortada = empresaRecibido.getImagenPort();

            if (imagenPortada != null && !imagenPortada.equals("")
                    && imagenPortada.length() > 0) {
                imagenP = new Imagen();
                imagenP.setEntidadId(empresaRecibido.getId());
                imagenP.setNombreTabla(Empresa.class.getName());
                imagenP = imagenManager.get(imagenP);

                if (imagenP != null) {

                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    imagenP.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/") + 1);
                    imagenP.setNombreImagen(empresaRecibido.getNombre() + "." + extension);

                    imagenManager.update(imagenP);

                } else {
                    imagenP = new Imagen();
                    imagenP.setImagen(Base64Bytes.decode(imagenPortada.split(",")[1]));
                    String extension = imagenPortada.split(";")[0];
                    extension = extension.substring(extension.indexOf("/") + 1);
                    imagenP.setNombreTabla("empresa");
                    imagenP.setNombreImagen(empresaRecibido.getNombre() + "." + extension);
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

            empresaManager.update(ejEmpresa);

            mensaje.setError(false);
            mensaje.setMensaje("La empresa " + empresaRecibido.getNombre() + " se modifico exitosamente.");
            return mensaje;

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar la empresa");
            System.out.println("Error" + e);
        }

        return mensaje;
    }

    /**
     * Mapping para desactivar una empresa
     *
     * @param id el id de la Empresa a desactivar
     */
    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
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
                retorno.setMensaje("La empresa " + nombre + " ya se encuentra desactivada.");
            }
            empresa.setActivo("N");
            empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            empresaManager.update(empresa);

            retorno.setError(false);
            retorno.setMensaje("La empresa " + nombre + " se desactivo exitosamente.");

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
                retorno.setMensaje("La empresa " + nombre + " ya se encuentra activada.");
            }
            empresa.setActivo("S");
            empresa.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            empresa.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            empresaManager.update(empresa);

            retorno.setError(false);
            retorno.setMensaje("La empresa " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar la empresa.");
        }

        return retorno;

    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView editar(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        String nombre = "";
        Map<String, Object> retornoMap = new HashMap<String, Object>();
        try {

            inicializarEmpresaManager();

            Map<String, Object> empresa = empresaManager.getAtributos(
                    new Empresa(id), atributos.split(","), false, true);

            retornoMap.putAll(empresa);

            for (Map.Entry<String, Object> entry : empresa.entrySet()) {

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

            model.addAttribute("empresa", retornoMap);
            model.addAttribute("editar", true);

            retorno.setViewName("empresa");
        } catch (Exception e) {
            System.out.println("Error" + e);
        }

        return retorno;

    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView visualizar(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        String nombre = "";
        Map<String, Object> retornoMap = new HashMap<String, Object>();
        try {
            inicializarEmpresaManager();

            Map<String, Object> empresa = empresaManager.getAtributos(
                    new Empresa(id), atributos.split(","), false, true);
            retornoMap.putAll(empresa);

            for (Map.Entry<String, Object> entry : empresa.entrySet()) {

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

            model.addAttribute("editar", false);
            model.addAttribute("empresa", retornoMap);
            retorno.setViewName("empresa");
        } catch (Exception e) {

            System.out.println("Error" + e);
        }

        return retorno;

    }
}
