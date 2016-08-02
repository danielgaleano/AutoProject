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
import com.sistem.proyecto.entity.Marca;
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
@RequestMapping(value = "/marcas")
public class MarcaController extends BaseController {

    String atributos = "id,nombre,activo";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarMarcas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("marca");
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            inicializarMarcaManager();
            inicializarEmpresaManager();
            System.out.println(userDetail.getNombre());

            Marca ejemplo = new Marca();
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            List<Map<String, Object>> listMapMarcas = marcaManager.listAtributos(ejemplo, atributos.split(","), true);

            model.addAttribute("marcas", listMapMarcas);


        } catch (Exception ex) {
            System.out.println("Error " + ex);
        }

        return retorno;
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Marca") Marca marcaRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Marca ejMarca = new Marca();
        try {
            inicializarMarcaManager();
            if (marcaRecibido.getNombre() == null || marcaRecibido.getNombre() != null
                    && marcaRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }

            ejMarca.setNombre(marcaRecibido.getNombre());

            Map<String, Object> marcaMap = marcaManager.getLike(ejMarca, "id".split(","));

            if (marcaMap != null && !marcaMap.isEmpty()) {
                retorno.setError(true);
                retorno.setMensaje("La marca de vehiculo ya se encuentra registrada.");
                return retorno;

            }

            ejMarca = new Marca();
            ejMarca.setNombre(marcaRecibido.getNombre());
            ejMarca.setActivo("S");
            ejMarca.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejMarca.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejMarca.setIdUsuarioCreacion(userDetail.getId());
            ejMarca.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            
            marcaManager.save(ejMarca);
           
            retorno.setMensaje("La marca se creo exitosamente.");
            return retorno;

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar/crear la marca.");

        }
        return retorno;
    }

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Marca") Marca marcaRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Marca ejMarca = new Marca();
        try {
            inicializarMarcaManager();

            if (marcaRecibido.getNombre() == null || marcaRecibido.getNombre() != null
                    && marcaRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }
            
            Map<String, Object> marcaMap = marcaManager.getLike(ejMarca, "id".split(","));

            if (marcaMap != null && !marcaMap.isEmpty() && marcaMap.get("id").toString()
                    .compareToIgnoreCase(marcaRecibido.getId().toString()) != 0) {
                retorno.setError(true);
                retorno.setMensaje("La marca de vehiculo ya se encuentra registrada.");
                return retorno;

            }

            if (marcaRecibido.getId() != null) {

                Marca marca = marcaManager.get(marcaRecibido.getId());
                marca.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                marca.setIdUsuarioModificacion(userDetail.getId());
                marca.setNombre(marcaRecibido.getNombre());

                marcaManager.update(marca);

                retorno.setError(false);
                retorno.setMensaje("La marca se modifico exitosamente.");
                return retorno;
            }

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar la marca.");

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

            inicializarMarcaManager();

            Marca marca = marcaManager.get(id);

            if (marca != null) {
                nombre = marca.getNombre().toString();
            }

            if (marca != null && marca.getActivo().toString()
                    .compareToIgnoreCase("S") == 0) {
                retorno.setError(true);
                retorno.setMensaje("La marca " + nombre + " ya se encuentra activada.");
                return retorno;
            }

            marca.setActivo("S");
            marca.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            marca.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            marca.setIdUsuarioModificacion(userDetail.getId());

            marcaManager.update(marca);

            retorno.setError(false);
            retorno.setMensaje("La marca " + nombre + " se activo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar la marca de vehiculo.");
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

            inicializarMarcaManager();

            Marca marca = marcaManager.get(id);

            if (marca != null) {
                nombre = marca.getNombre().toString();
            }

            if (marca != null && marca.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("La marca " + nombre + " ya se encuentra desactivada.");
                return retorno;
            }

            marca.setActivo("N");
            marca.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            marca.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));
            marca.setIdUsuarioEliminacion(userDetail.getId());

            marcaManager.update(marca);

            retorno.setError(false);
            retorno.setMensaje("La marca " + nombre + " se desactivo exitosamente.");

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar la marca de vehiculo.");
        }

        return retorno;

    }

}
