/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Tipo;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.DatosDTO;
import com.sistem.proyecto.utils.MensajeDTO;
import com.sistem.proyecto.web.controller.BaseController;
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

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/tipos")
public class TipoController extends BaseController {

    String atributos = "id,nombre,activo";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarTipos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("tipo");
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            inicializarTipoManager();
            inicializarEmpresaManager();
            System.out.println(userDetail.getNombre());

            Tipo ejemplo = new Tipo();
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            List<Map<String, Object>> listMapTipoes = tipoManager.listAtributos(ejemplo, atributos.split(","), true);

            model.addAttribute("tipos", listMapTipoes);


        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }

        return retorno;
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Tipo") Tipo tipoRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Tipo ejTipo = new Tipo();
        try {
            inicializarTipoManager();
            if (tipoRecibido.getNombre() == null || tipoRecibido.getNombre() != null
                    && tipoRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }

            ejTipo.setNombre(tipoRecibido.getNombre());

            Map<String, Object> tipoMap = tipoManager.getLike(ejTipo, "id".split(","));

            if (tipoMap != null && !tipoMap.isEmpty()) {
                retorno.setError(true);
                retorno.setMensaje("El tipo de vehiculo ya se encuentra registrado.");
                return retorno;

            }

            ejTipo = new Tipo();
            ejTipo.setNombre(tipoRecibido.getNombre());
            ejTipo.setActivo("S");
            ejTipo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejTipo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejTipo.setIdUsuarioCreacion(userDetail.getId());
            ejTipo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            
            tipoManager.save(ejTipo);
           
            retorno.setMensaje("El tipo se creo exitosamente.");
            return retorno;

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar/crear el tipo.");

        }
        return retorno;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Tipo") Tipo tipoRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Tipo ejTipo = new Tipo();
        try {
            inicializarTipoManager();

            if (tipoRecibido.getNombre() == null || tipoRecibido.getNombre() != null
                    && tipoRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }
            
            Map<String, Object> tipoMap = tipoManager.getLike(ejTipo, "id".split(","));

            if (tipoMap != null && !tipoMap.isEmpty() && tipoMap.get("id").toString()
                    .compareToIgnoreCase(tipoRecibido.getId().toString()) != 0) {
                retorno.setError(true);
                retorno.setMensaje("El tipo de vehiculo ya se encuentra registrado.");
                return retorno;

            }

            if (tipoRecibido.getId() != null) {

                Tipo tipo = tipoManager.get(tipoRecibido.getId());
                tipo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                tipo.setIdUsuarioModificacion(userDetail.getId());
                tipo.setNombre(tipoRecibido.getNombre());

                tipoManager.update(tipo);

                retorno.setError(false);
                retorno.setMensaje("El tipo se modifico exitosamente.");
                return retorno;
            }

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar el tipo.");

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

            inicializarTipoManager();

            Tipo tipo = tipoManager.get(id);

            if (tipo != null) {
                nombre = tipo.getNombre().toString();
            }

            if (tipo != null && tipo.getActivo().toString()
                    .compareToIgnoreCase("S") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El tipo " + nombre + " ya se encuentra activada.");
                return retorno;
            }

            tipo.setActivo("S");
            tipo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            tipo.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            tipo.setIdUsuarioModificacion(userDetail.getId());

            tipoManager.update(tipo);

            retorno.setError(false);
            retorno.setMensaje("El tipo " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el tipo de vehiculo.");
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

            inicializarTipoManager();

            Tipo tipo = tipoManager.get(id);

            if (tipo != null) {
                nombre = tipo.getNombre().toString();
            }

            if (tipo != null && tipo.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El tipo " + nombre + " ya se encuentra desactivada.");
                return retorno;
            }

            tipo.setActivo("N");
            tipo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            tipo.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            tipo.setIdUsuarioEliminacion(userDetail.getId());

            tipoManager.update(tipo);

            retorno.setError(false);
            retorno.setMensaje("El tipo " + nombre + " se desactivo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el tipo de vehiculo.");
        }

        return retorno;

    }

}
