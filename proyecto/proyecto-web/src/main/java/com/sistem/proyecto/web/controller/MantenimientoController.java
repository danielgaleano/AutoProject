/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Empresa;
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
import com.sistem.proyecto.manager.utils.PagoDTO;

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value="/mantenimiento/vehiculos")
public class MantenimientoController extends BaseController{
    
    String atributos = "id,codigo,activo,marca.id,precioCosto,precioVenta,precioMantenimiento,marca.nombre,modelo.id,modelo.nombre,empresa.id,empresa.nombre,"
            + "tipo.id,tipo.nombre,transmision,color,anho,caracteristica,proveedor.id,proveedor.nombre,"
            + "cedulaVerde,titulo,kilometraje,motor,chasis,chapa,fechaMantenimiento";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaVehiculos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("vehiculosListar");       
        return retorno;

    }
    
    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("vehiculoForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }
    
    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView formEdit(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("vehiculoForm");
        model.addAttribute("action", "EDITAR");
        model.addAttribute("id", id);
        return retorno;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno Form(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarVehiculoManager();
            Vehiculo ejVehiculo = new Vehiculo();
            ejVehiculo.setId(id);

            Map<String, Object> ejVehiculoMap = vehiculoManager.getAtributos(ejVehiculo, atributos.split(","));

            retorno.setData(ejVehiculoMap);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente el vehiculo");

        } catch (Exception ex) {
            logger.error("Error al obtener la compra", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener el cliente");
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
        ordenarPor = "marca.nombre";
        
        Vehiculo ejVehiculo = new Vehiculo();
        ejVehiculo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejVehiculo.setEstado(Vehiculo.MANTENIMIENTO);
        
        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarVehiculoManager();

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
                total = vehiculoManager.list(ejVehiculo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = vehiculoManager.listAtributos(ejVehiculo, atributos.split(","), todos, inicio, cantidad,
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
            logger.debug("Error al crear vehiculo", ex);
        }
        return new ModelAndView("modelo");

    }
    
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("Vehiculo") Vehiculo vehiculoRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        
        try {
            inicializarVehiculoManager();

            if (vehiculoRecibido.getAnho() == null || vehiculoRecibido.getAnho() != null
                    && vehiculoRecibido.getAnho().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el año del vehiculo.");
                return mensaje;
            }
            
            if (vehiculoRecibido.getChasis() == null || vehiculoRecibido.getChasis() != null
                    && vehiculoRecibido.getChasis().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el chasis del vehiculo.");
                return mensaje;
            }
            
            if (vehiculoRecibido.getMotor() == null || vehiculoRecibido.getMotor() != null
                    && vehiculoRecibido.getMotor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el motor del vehiculo.");
                return mensaje;
            }
            
            if (vehiculoRecibido.getKilometraje() == null || vehiculoRecibido.getKilometraje() != null
                    && vehiculoRecibido.getKilometraje().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el kilometraje del vehiculo.");
                return mensaje;
            }
            
            if (vehiculoRecibido.getPrecioVenta() == null || vehiculoRecibido.getPrecioVenta() != null
                    && vehiculoRecibido.getPrecioVenta().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el precio de venta del vehiculo.");
                return mensaje;
            }

            
            
           



        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el vehiculo");
            logger.debug("Error al guardar vehiculo ", ex);
        }

        return mensaje;
    }
   
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Vehiculo") Vehiculo vehiculoRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        Vehiculo ejVehiculo = new Vehiculo();
        try {
            inicializarVehiculoManager();

            if (vehiculoRecibido.getCodigo() == null || vehiculoRecibido.getCodigo() != null
                    && vehiculoRecibido.getCodigo().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El codigo del vehiculo no puede estar vacio.");
                return retorno;
            }

            if (vehiculoRecibido.getMarca() == null || vehiculoRecibido.getMarca() != null
                    && vehiculoRecibido.getMarca().getId() == 0) {
                retorno.setError(true);
                retorno.setMensaje("La marca del vehiculo no puede estar vacio.");
                return retorno;
            }
            
            if (vehiculoRecibido.getModelo() == null || vehiculoRecibido.getModelo() != null
                    && vehiculoRecibido.getModelo().getId() == 0) {
                retorno.setError(true);
                retorno.setMensaje("El modelo del vehiculo no puede estar vacio.");
                return retorno;
            }
            
//            Map<String, Object> modeloMap = vehiculoManager.getLike(ejVehiculo, "id".split(","));
//
//            if (modeloMap != null && !modeloMap.isEmpty() && modeloMap.get("id").toString()
//                    .compareToIgnoreCase(vehiculoRecibido.getId().toString()) != 0) {
//                retorno.setError(true);
//                retorno.setMensaje("El modelo de vehiculo ya se encuentra registrado.");
//                return retorno;
//
//            }

            if (vehiculoRecibido.getId() != null) {

                Vehiculo modelo = vehiculoManager.get(vehiculoRecibido.getId());
                modelo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                modelo.setIdUsuarioModificacion(userDetail.getId());
                modelo.setCodigo(vehiculoRecibido.getCodigo());

                vehiculoManager.update(modelo);

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

            inicializarVehiculoManager();

            Vehiculo modelo = vehiculoManager.get(id);

            if (modelo != null) {
                nombre = modelo.getCodigo().toString();
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

            vehiculoManager.update(modelo);

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

            inicializarVehiculoManager();

            Vehiculo modelo = vehiculoManager.get(id);

            if (modelo != null) {
                nombre = modelo.getCodigo().toString();
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

            vehiculoManager.update(modelo);

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
