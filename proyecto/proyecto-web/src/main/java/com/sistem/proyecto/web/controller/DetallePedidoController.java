/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sistem.proyecto.utils.DTORetorno;
import com.sistem.proyecto.utils.MensajeDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/pedido/detalles")
public class DetallePedidoController extends BaseController {

    String atributos = "id,caracteristica,trasmision,color,anho,cantidad,precio,total,tipo.id,tipo.nombre,marca.id,marca.nombre,activo,estadoPedido,moneda.id,moneda.nombre";

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
        ordenarPor = "tipo.nombre";
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
                Integer total = pedidoManager.list(pedidoEj, true).size();
                
                ejPedido.setNumeroPedido(total+1 +"");
                ejPedido.setCodigo(detalleRecibido.getPedido().getCodigo());
                ejPedido.setFechaEntrega(resultFecha);
                ejPedido.setObservacion(detalleRecibido.getPedido().getObservacion());
                ejPedido.setProveedor(detalleRecibido.getPedido().getProveedor());
                ejPedido.setActivo("S");
                ejPedido.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejPedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejPedido.setIdUsuarioCreacion(userDetail.getId());
                ejPedido.setUsuario(new Usuario(userDetail.getId()));
                ejPedido.setNeto(Double.parseDouble("0"));
                ejPedido.setTotal(Double.parseDouble("0"));
                ejPedido.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                pedidoManager.save(ejPedido);
                
                mensaje.setId(ejPedido.getId());

            }else{
                ejPedido.setId(detalleRecibido.getPedido().getId());
                mensaje.setId(detalleRecibido.getPedido().getId());
            }

            if (detalleRecibido.getTipo() == null || detalleRecibido.getTipo().getId() != null
                    && detalleRecibido.getTipo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El tipo de vehiculo no puede estar vacio.");
                return mensaje;
            }
            
            if (detalleRecibido.getMarca()== null || detalleRecibido.getMarca().getId() != null
                    && detalleRecibido.getMarca().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La marca de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleRecibido.getAnho() == null || detalleRecibido.getAnho() != null
                    && detalleRecibido.getAnho().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El año del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleRecibido.getColor() == null || detalleRecibido.getColor() != null
                    && detalleRecibido.getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleRecibido.getColor() == null || detalleRecibido.getColor() != null
                    && detalleRecibido.getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleRecibido.getTrasmision() == null || detalleRecibido.getTrasmision() != null
                    && detalleRecibido.getTrasmision().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La transmision del vehiculo no puede estar vacia.");
                return mensaje;
            }

            if (detalleRecibido.getMoneda() == null || detalleRecibido.getMoneda().getId() != null
                    && detalleRecibido.getMoneda().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo moneda no puede estar vacia.");
                return mensaje;
            }

            if (detalleRecibido.getPrecio() == null || detalleRecibido.getPrecio() != null
                    && detalleRecibido.getPrecio().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo precio no puede estar vacio.");
                return mensaje;
            }

//            if (detalleRecibido.getCantidad() == null || detalleRecibido.getCantidad() != null
//                    && detalleRecibido.getCantidad().toString().compareToIgnoreCase("") == 0) {
//                mensaje.setError(true);
//                mensaje.setMensaje("El campo cantida no puede estar vacia.");
//                return mensaje;
//            }

            ejDetalle.setPedido(ejPedido);
            ejDetalle.setAnho(detalleRecibido.getAnho());
            ejDetalle.setCantidad(Long.parseLong("1"));
            ejDetalle.setCaracteristica(detalleRecibido.getCaracteristica());
            ejDetalle.setColor(detalleRecibido.getColor());
            ejDetalle.setMoneda(detalleRecibido.getMoneda());
            ejDetalle.setTipo(detalleRecibido.getTipo());
            ejDetalle.setMarca(detalleRecibido.getMarca());
            ejDetalle.setTrasmision(detalleRecibido.getTrasmision());
            ejDetalle.setPrecio(detalleRecibido.getPrecio());
            ejDetalle.setActivo("S");
            ejDetalle.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejDetalle.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejDetalle.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            Moneda ejMoneda = monedaManager.get(detalleRecibido.getMoneda());

            Double cambio = Double.parseDouble(ejMoneda.getValor());

            ejDetalle.setCambioDia(cambio);

            Double total = detalleRecibido.getPrecio() * 1;
            ejDetalle.setTotal(total);
            ejDetalle.setEstadoPedido(DetallePedido.PENDIENTE);
            
            detallePedidoManager.save(ejDetalle);

            mensaje.setError(false);
            mensaje.setMensaje("El detalle del pedido se guardo exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al guardar el detalle del pedido");
            logger.error("Error al guardar el detalle del pedido ", ex);
        }

        return mensaje;
    }
    
    

}
