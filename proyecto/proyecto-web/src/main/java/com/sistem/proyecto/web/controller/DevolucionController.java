/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Devolucion;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.DevolucionManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.userDetail.UserDetail;
import java.sql.Timestamp;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/devoluciones")
public class DevolucionController extends BaseController {
    String atributos = "id,nombre,documento,email,telefono,telefonoMovil,comentario,pais,sexo,fechaNacimiento,contacto.id,"
            + "contacto.nombre,contacto.documento,actividad,contacto.cargo,contacto.telefono,contacto.movil,contacto.email,"
            + "contacto.comentario,empresa.id,empresa.nombre,direccion,activo,"
            + "empleo.id,empleo.nombreEmpresa,empleo.cargo,empleo.antiguedad,empleo.salario,empleo.telefono,empleo.direccion,empleo.actividad,empleo.comentario";

    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaClientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("devolucionesListar");
        return retorno;

    }
    
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Devolucion") Devolucion devolucionRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Devolucion ejDevolucion = new Devolucion();
        
        try {
            inicializarDevolucionManager();
            inicializarVehiculoManager();

            ejDevolucion = new Devolucion();

            ejDevolucion.setActivo("S");
            ejDevolucion.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejDevolucion.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejDevolucion.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            
            if(devolucionRecibido.getVehiculo() != null && 
                    devolucionRecibido.getVehiculo().getId() == null || 
                    devolucionRecibido.getVehiculo().getId() != null && 
                    devolucionRecibido.getVehiculo().getId().toString().compareToIgnoreCase("") == 0){
                
                mensaje.setError(true);
                mensaje.setMensaje("El Id del vehiculo no puede estar vacio");
                return mensaje;
            }
            
            Vehiculo ejVehiculo = vehiculoManager.get(devolucionRecibido.getVehiculo().getId());
            
            ejDevolucion.setVehiculo(devolucionRecibido.getVehiculo());
//            ejDevolucion.setDocumento(devolucionRecibido.getDocumento());
//            ejDevolucion.setEmail(devolucionRecibido.getEmail());
//            ejDevolucion.setNombre(devolucionRecibido.getNombre());
//            ejDevolucion.setFechaNacimiento(devolucionRecibido.getFechaNacimiento());
//            ejDevolucion.setSexo(devolucionRecibido.getSexo());
//            ejDevolucion.setPais(devolucionRecibido.getPais());
//            ejDevolucion.setComentario(devolucionRecibido.getComentario());
//            ejDevolucion.setTelefono(devolucionRecibido.getTelefono());
//            ejDevolucion.setTelefonoMovil(devolucionRecibido.getTelefonoMovil());

            devolucionManager.save(ejDevolucion);
            
            mensaje.setId(ejDevolucion.getId());
            mensaje.setError(false);
            mensaje.setMensaje("La devolucion se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar la devolucion");
            logger.debug("Error al guardar la devolucion ", ex);
        }

        return mensaje;
    }
    
    
}
