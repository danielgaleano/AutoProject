/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Modelo;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Marca;
import com.sistem.proyecto.userDetail.UserDetail;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value="/modelos")
public class ModeloController extends BaseController{
    
    String atributos = "id,nombre,activo,marca.id,marca.nombre,empresa.id,empresa.nombre";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaModelos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("modelosListar");       
        return retorno;

    }
    
    @RequestMapping(value = "/agregar/{id}", method = RequestMethod.GET)
    public ModelAndView agregarView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("modelosListar");
        model.addAttribute("action", "AGREGAR");
        model.addAttribute("id", id);
        
        try {
            inicializarMarcaManager();
            Marca ejMarca = marcaManager.get(id);
             model.addAttribute("nombre", ejMarca.getNombre());
        
        } catch (Exception ex) {
            logger.error("Error ", ex);
        }
        
        return retorno;
    }
    
    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("modelosListar");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        try {
            inicializarMarcaManager();
            Marca ejMarca = marcaManager.get(id);
            model.addAttribute("nombre", ejMarca.getNombre());
        
        } catch (Exception ex) {
            logger.error("Error ", ex);
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
            @ModelAttribute("idMarca") String idMarca,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ordenarPor = "nombre";
        Modelo ejModelo = new Modelo();
        ejModelo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        
        if(idMarca != null && idMarca.compareToIgnoreCase("") != 0){
            ejModelo.setMarca(new Marca(Long.parseLong(idMarca)));
        }
  
        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarModeloManager();

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
                total = modeloManager.list(ejModelo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = modeloManager.listAtributos(ejModelo, atributos.split(","), todos, inicio, cantidad,
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
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {

        try {
            inicializarEmpresaManager();
            model.addAttribute("tipo", "Crear");
            model.addAttribute("editar", false);

        } catch (Exception ex) {
            logger.debug("Error al crear modelo", ex);
        }
        return new ModelAndView("modelo");

    }
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Modelo") Modelo modeloRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Modelo ejModelo = new Modelo();
        ejModelo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        try {
            inicializarModeloManager();

            if (modeloRecibido.getNombre() == null || modeloRecibido.getNombre() != null
                    && modeloRecibido.getNombre().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El nombre del modelo no puede estar vacio.");
                return mensaje;
            }

            if (modeloRecibido.getMarca() == null || modeloRecibido.getMarca() != null
                    && modeloRecibido.getMarca().getId() == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La marca del modelo no puede estar vacia.");
                return mensaje;
            }

            ejModelo.setNombre(modeloRecibido.getNombre());
            ejModelo.setMarca(new Marca(modeloRecibido.getMarca().getId()));
            
            Map<String, Object> modeloMap = modeloManager.getLike(ejModelo, "id".split(","));

            if (modeloMap != null && !modeloMap.isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("El modelo de vehiculo ya se encuentra registrado.");
                return mensaje;

            }

            ejModelo = new Modelo();
            ejModelo.setActivo("S");
            ejModelo.setNombre(modeloRecibido.getNombre());
            ejModelo.setMarca(modeloRecibido.getMarca());
            ejModelo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            modeloManager.save(ejModelo);

            mensaje.setError(false);
            mensaje.setMensaje("El modelo " + modeloRecibido.getNombre() + " se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el modelo");
            logger.debug("Error al guardar modelo ", ex);
        }

        return mensaje;
    }
   
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Modelo") Modelo modeloRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Modelo ejModelo = new Modelo();
        try {
            inicializarModeloManager();

            if (modeloRecibido.getNombre() == null || modeloRecibido.getNombre() != null
                    && modeloRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }
            
            if (modeloRecibido.getMarca() == null || modeloRecibido.getMarca() != null
                    && modeloRecibido.getMarca().getId() == 0) {
                retorno.setError(true);
                retorno.setMensaje("La marca del modelo no puede estar vacia.");
                return retorno;
            }
            
//            Map<String, Object> modeloMap = modeloManager.getLike(ejModelo, "id".split(","));
//
//            if (modeloMap != null && !modeloMap.isEmpty() && modeloMap.get("id").toString()
//                    .compareToIgnoreCase(modeloRecibido.getId().toString()) != 0) {
//                retorno.setError(true);
//                retorno.setMensaje("El modelo de vehiculo ya se encuentra registrado.");
//                return retorno;
//
//            }

            if (modeloRecibido.getId() != null) {

                Modelo modelo = modeloManager.get(modeloRecibido.getId());
                modelo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                modelo.setIdUsuarioModificacion(userDetail.getId());
                modelo.setNombre(modeloRecibido.getNombre());

                modeloManager.update(modelo);

                retorno.setError(false);
                retorno.setMensaje("El modelo se modifico exitosamente.");
                return retorno;
            }

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar el modelo.");

        }
        return retorno;
    }
    
    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

            inicializarModeloManager();

            Modelo modelo = modeloManager.get(id);

            if (modelo != null) {
                nombre = modelo.getNombre().toString();
            }

            if (modelo != null && modelo.getActivo().toString()
                    .compareToIgnoreCase("S") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El modelo " + nombre + " ya se encuentra activado.");
                return retorno;
            }

            modelo.setActivo("S");
            modelo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            modelo.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            modelo.setIdUsuarioModificacion(userDetail.getId());

            modeloManager.update(modelo);

            retorno.setError(false);
            retorno.setMensaje("El modelo " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el modelo de vehiculo.");
        }

        return retorno;

    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        ModelAndView view = new ModelAndView();
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

            inicializarModeloManager();

            Modelo modelo = modeloManager.get(id);

            if (modelo != null) {
                nombre = modelo.getNombre().toString();
            }

            if (modelo != null && modelo.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El modelo " + nombre + " ya se encuentra desactivado.");
                return retorno;
            }

            modelo.setActivo("N");
            modelo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            modelo.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            modelo.setIdUsuarioEliminacion(userDetail.getId());

            modeloManager.update(modelo);

            retorno.setError(false);
            retorno.setMensaje("El modelo " + nombre + " se desactivo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el modelo de vehiculo.");
        }

        return retorno;

    }
}
