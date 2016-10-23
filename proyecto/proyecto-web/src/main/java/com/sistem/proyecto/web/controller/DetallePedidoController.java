/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Marca;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sistem.proyecto.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/pedido/detalles")
public class DetallePedidoController extends BaseController {

    String atributos = "id,neto,vehiculo.caracteristica,vehiculo.transmision,vehiculo.color,vehiculo.anho,vehiculo.codigo,precio,total,vehiculo.tipo.id,vehiculo.tipo.nombre,vehiculo.modelo.id,vehiculo.modelo.nombre,vehiculo.marca.id,vehiculo.marca.nombre,activo,estadoPedido,moneda.id,moneda.nombre,moneda.valor";

    @RequestMapping(value = "/agregar/{id}", method = RequestMethod.GET)
    public ModelAndView detalleAgregar(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("pedidoForm");
        model.addAttribute("action", "AGREGAR");
        model.addAttribute("id", id);
        return retorno;
    }
    
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarDetalle(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("idPedido") String idPedido,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DetallePedido ejemplo = new DetallePedido();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        if (idPedido != null && idPedido.compareToIgnoreCase("") != 0) {
            ejemplo.setPedido(new Pedido(Long.parseLong(idPedido)));
        }
        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarDetallePedidoManager();

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
            if(ordenarPor == null || ordenarPor != null && ordenarPor.compareToIgnoreCase(" ") == 0){
                ordenarPor = "tipo.nombre";
            }
            
            pagina = pagina != null ? pagina : 1;
            Integer total = 0;
            if (idPedido != null && idPedido.compareToIgnoreCase("") != 0) {
                if (!todos) {
                    total = detallePedidoManager.list(ejemplo, true).size();
                }

                Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

                if (total < inicio) {
                    inicio = total - total % cantidad;
                    pagina = total / cantidad;
                }

                listMapGrupos = detallePedidoManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, null, null, null, null, null, true);

                if (todos) {
                    total = listMapGrupos.size();
                }

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
    MensajeDTO guardar(@ModelAttribute("DetallePedido") DetallePedido detalleRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        DetallePedido ejDetalle = new DetallePedido();
        Pedido ejPedido = new Pedido();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            inicializarDetallePedidoManager();
            inicializarPedidoManager();
            inicializarMonedaManager();
            inicializarVehiculoManager();

            if (detalleRecibido.getPedido().getId() == null || detalleRecibido.getPedido().getId() != null
                    && detalleRecibido.getPedido().getId().toString().compareToIgnoreCase("") == 0) {

                if (detalleRecibido.getPedido().getCodigo() == null || detalleRecibido.getPedido().getCodigo() != null
                        && detalleRecibido.getPedido().getCodigo().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El codigo del pedido no puede estar vacio.");
                    return mensaje;
                }
                if (detalleRecibido.getPedido().getFecha() == null || detalleRecibido.getPedido().getFecha() != null
                        && detalleRecibido.getPedido().getFecha().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("La fecha de entrega del pedido no puede estar vacio.");
                    return mensaje;
                }
                if (detalleRecibido.getPedido().getProveedor() == null || detalleRecibido.getPedido().getProveedor().getId() != null
                        && detalleRecibido.getPedido().getProveedor().getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("Se debe ingresar un proveedor para el pedido.");
                    return mensaje;
                }
                Date resultFecha = dateFormat.parse(detalleRecibido.getPedido().getFecha());
                
                Pedido pedidoEj = new Pedido();
                pedidoEj.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                
                Integer total = pedidoManager.list(pedidoEj, false).size();

                ejPedido.setNumeroPedido(total + 1 + "");
                ejPedido.setCodigo(detalleRecibido.getPedido().getCodigo());
                ejPedido.setFechaEntrega(resultFecha);
                ejPedido.setObservacion(detalleRecibido.getPedido().getObservacion());
                ejPedido.setProveedor(detalleRecibido.getPedido().getProveedor());
                ejPedido.setActivo("S");
                ejPedido.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejPedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejPedido.setIdUsuarioCreacion(userDetail.getId());
                ejPedido.setUsuario(new Usuario(userDetail.getId()));
                ejPedido.setTotal(Double.parseDouble("0"));
                ejPedido.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                
                pedidoManager.save(ejPedido);
                mensaje.setId(ejPedido.getId());

            } else {
                ejPedido.setId(detalleRecibido.getPedido().getId());
                mensaje.setId(detalleRecibido.getPedido().getId());
            }


            mensaje = detallePedidoManager.guardarDetalle(detalleRecibido.getVehiculo(), detalleRecibido.getMoneda(),
                    detalleRecibido, true, ejPedido, userDetail.getIdEmpresa(), userDetail.getId());
 
        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al guardar el detalle del pedido");
            logger.error("Error al guardar el detalle del pedido ", ex);
        }

        return mensaje;
    }
    
    
    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO aprobar(@PathVariable("id") Long id) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarDetallePedidoManager();
            inicializarPedidoManager();
            
            DetallePedido detallePedido = detallePedidoManager.get(id);

            if (detallePedido != null) {
                nombre = detallePedido.getVehiculo().getCodigo().toString();
            }

            if (detallePedido != null && detallePedido.getActivo().toString()
                    .compareToIgnoreCase(DetallePedido.APROBADO) == 0) {
                retorno.setError(true);
                retorno.setMensaje("El pedido del vehiculo " + nombre + " ya se encuentra aprobada.");
                return retorno;
            }
            
            retorno = detallePedidoManager.aprobar(id, detallePedido.getPedido().getId(),
                    userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de aprobar el detalle.");
        }

        return retorno;

    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO rechazar(@PathVariable("id") Long id, Model model) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        ModelAndView view = new ModelAndView();
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        try {
            inicializarDetallePedidoManager();
            inicializarPedidoManager();

            DetallePedido detallePedido = detallePedidoManager.get(id);

            if (detallePedido != null) {
                nombre = detallePedido.getVehiculo().getCodigo().toString();
            }

            if (detallePedido != null && detallePedido.getEstadoPedido().toString()
                    .compareToIgnoreCase(DetallePedido.RECHAZADO) == 0) {
                retorno.setError(true);
                retorno.setMensaje("El pedido del vehiculo " + nombre + " ya se encuentra rechazada.");
                return retorno;
            }

            retorno = detallePedidoManager.rechazar(id, detallePedido.getPedido().getId(),
                    userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception e) {
            System.out.println("Error " + e);
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de rechazado el detalle.");
        }

        return retorno;

    }

}
