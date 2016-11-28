/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.entity.RolPermiso;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.DatosDTO;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.sistem.proyecto.manager.utils.DTORetorno;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/monedas")
public class MonedaController extends BaseController {

    String atributos = "id,nombre,activo,empresa.id,empresa.nombre,simbolo,valor,porDefecto";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarMonedas(Model model) {
        ModelAndView retorno = new ModelAndView();

        retorno.setViewName("monedasListar");

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

        Moneda ejemplo = new Moneda();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarMonedaManager();

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
                total = monedaManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = monedaManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad,
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
    MensajeDTO guardar(@ModelAttribute("Moneda") Moneda monedaRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        try {
            inicializarMonedaManager();
            if (monedaRecibido.getNombre() == null || monedaRecibido.getNombre() != null
                    && monedaRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }

            if (monedaRecibido.getSimbolo() == null || monedaRecibido.getSimbolo() != null
                    && monedaRecibido.getSimbolo().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo simbolo no puede estar vacio.");
                return retorno;
            }

            if (monedaRecibido.getValor() == null || monedaRecibido.getValor() != null
                    && monedaRecibido.getValor().toString().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo cotizacion no puede estar vacio.");
                return retorno;
            }

            Moneda moneda = new Moneda();
            moneda.setNombre(Moneda.MONEDA_NACIONAL);
            moneda = monedaManager.get(moneda);

            if (moneda == null) {
                moneda = new Moneda();
                moneda.setNombre(Moneda.MONEDA_NACIONAL);
                moneda.setSimbolo("M-NACIONAL");
                moneda.setValor(Double.parseDouble("1"));
                moneda.setPorDefecto(true);
                moneda.setActivo("S");
                moneda.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                moneda.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                moneda.setIdUsuarioCreacion(userDetail.getId());
                moneda.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                monedaManager.save(moneda);
            }
            moneda = new Moneda();
            moneda.setNombre(monedaRecibido.getNombre());
            moneda.setSimbolo(monedaRecibido.getSimbolo());
            moneda.setValor(monedaRecibido.getValor());
            moneda.setPorDefecto(monedaRecibido.getPorDefecto());
            moneda.setActivo("S");
            moneda.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            moneda.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            moneda.setIdUsuarioCreacion(userDetail.getId());
            moneda.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            monedaManager.save(moneda);

            retorno.setError(false);
            retorno.setMensaje("La moneda " + moneda.getNombre() + " se creo exitosamente.");

            return retorno;

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al crear la moneda.");

        }
        return retorno;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Moneda") Moneda monedaRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        try {

            inicializarMonedaManager();
            if (monedaRecibido.getNombre() == null || monedaRecibido.getNombre() != null
                    && monedaRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }

            if (monedaRecibido.getSimbolo() == null || monedaRecibido.getSimbolo() != null
                    && monedaRecibido.getSimbolo().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo simbolo no puede estar vacio.");
                return retorno;
            }

            if (monedaRecibido.getValor() == null || monedaRecibido.getValor() != null
                    && monedaRecibido.getValor().toString().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo cotizacion no puede estar vacio.");
                return retorno;
            }

            Moneda moneda = monedaManager.get(monedaRecibido.getId());
            moneda.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            moneda.setIdUsuarioModificacion(userDetail.getId());
            moneda.setNombre(monedaRecibido.getNombre());
            moneda.setValor(monedaRecibido.getValor());
            moneda.setSimbolo(monedaRecibido.getSimbolo());

            monedaManager.update(moneda);

            retorno.setError(false);
            retorno.setMensaje("La moneda se modifico exitosamente.");
            return retorno;

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar el rol.");

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

            inicializarMonedaManager();

            Moneda moneda = monedaManager.get(id);

            if (moneda != null) {
                nombre = moneda.getNombre().toString();
            }

            if (moneda != null && moneda.getActivo().toString()
                    .compareToIgnoreCase("S") == 0) {
                retorno.setError(true);
                retorno.setMensaje("La moneda " + nombre + " ya se encuentra activada.");
                return retorno;
            }

            moneda.setActivo("S");
            moneda.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            moneda.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            moneda.setIdUsuarioModificacion(userDetail.getId());

            monedaManager.update(moneda);

            retorno.setError(false);
            retorno.setMensaje("La moneda " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el rol.");
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

            inicializarMonedaManager();

            Moneda moneda = monedaManager.get(id);

            if (moneda != null) {
                nombre = moneda.getNombre().toString();
            }

            if (moneda != null && moneda.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El moneda " + nombre + " ya se encuentra desactivada.");
                return retorno;
            }

            if (moneda != null && moneda.getPorDefecto()) {
                retorno.setError(true);
                retorno.setMensaje("No puede desactivarse una moneda definida como principal");
                return retorno;
            }

            moneda.setActivo("N");
            moneda.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            moneda.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            moneda.setIdUsuarioEliminacion(userDetail.getId());

            monedaManager.update(moneda);

            retorno.setError(false);
            retorno.setMensaje("La moneda " + nombre + " se desactivo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el rol.");
        }
        return retorno;

    }

    @RequestMapping(value = "/moneda/definir/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO definirMoneda(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        ModelAndView view = new ModelAndView();
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {

            inicializarMonedaManager();
            Moneda ejMoneda = new Moneda();
            ejMoneda.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejMoneda.setActivo("S");

            List<Map<String, Object>> monedasMap = monedaManager.listAtributos(ejMoneda, "id".split(","));

            for (Map<String, Object> rmp : monedasMap) {
                ejMoneda = monedaManager.get(Long.parseLong(rmp.get("id").toString()));
                ejMoneda.setPorDefecto(Boolean.FALSE);
                monedaManager.update(ejMoneda);
            }
            ejMoneda = monedaManager.get(id);

            ejMoneda.setPorDefecto(Boolean.TRUE);

            monedaManager.update(ejMoneda);

            retorno.setError(false);
            retorno.setMensaje("La moneda " + nombre + " se definio exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el rol.");
        }
        return retorno;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno obtenerMoneda(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DTORetorno<Moneda> retorno = new DTORetorno<Moneda>();
        String nombre = "";

        try {

            inicializarMonedaManager();

            Moneda ejMoneda = monedaManager.get(id);

            retorno.setData(ejMoneda);
            retorno.setError(false);
            retorno.setMensaje("La moneda se obtuvo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la moneda.");
        }
        return retorno;

    }

}
