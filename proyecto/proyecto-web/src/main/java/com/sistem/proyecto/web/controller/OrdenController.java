/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.sistem.proyecto.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/orden/compras")
public class OrdenController extends BaseController {

    String atributos = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,total,proveedor.id,"
            + "proveedor.nombre,activo,usuario.nombre,cantidadAprobados,cantidadTotal";
    String atributosDetalle = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,descuento,total,neto,proveedor.id,"
            + "proveedor.nombre";
    
    String atributosCompras = "id,nroFactura,fechaCompra,tipoCompra,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,proveedor.nombre,activo,pedido.usuario.nombre,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,pedido.numeroPedido,pedido.codigo,pedido.fechaEntrega,"
            + "pedido.cantidadAprobados,pedido.cantidadTotal,pedido.total";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarPermisos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ordenesListar");
        return retorno;
    }
    
    @RequestMapping(value = "/{id}/realizada", method = RequestMethod.GET)
    public ModelAndView compraHecha(@PathVariable("id") Long id,Model model) {
        model.addAttribute("action", "CREAR");
        model.addAttribute("editar", false);
        model.addAttribute("id", id);
        return new ModelAndView("compraForm");
    }

    @RequestMapping(value = "/realizar/{id}", method = RequestMethod.GET)
    public ModelAndView crear(@PathVariable("id") Long id,Model model) {
        model.addAttribute("action", "CREAR");
        model.addAttribute("editar", false);
        model.addAttribute("id", id);
        return new ModelAndView("compraForm");
    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("compraForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }
    
    @RequestMapping(value = "/{id}/aprobar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno compraAprobar(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarCompraManager();

            Compra ejCompra = compraManager.get(id);
            
            ejCompra.setEstadoCompra(Compra.COMPRA_PENDIENTE);
            compraManager.update(ejCompra);
            
            retorno.setError(false);
            retorno.setMensaje("La compra fue aprobada exitosamente.");

        } catch (Exception ex) {
            logger.error("Error al aprobar la compra", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al aprobar la compra");
        }

        return retorno;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno compraForm(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarCompraManager();
            Compra ejCompra = new Compra();
            ejCompra.setId(id);

            Map<String, Object> ejCompraMap = compraManager.getAtributos(ejCompra, atributosCompras.split(","));

            retorno.setData(ejCompraMap);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la compra");

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
        
        Compra ejemplo = new Compra();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoCompra(Compra.ORDEN_COMPRA);
        
        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarCompraManager();

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
            if (ordenarPor == null || ordenarPor != null && ordenarPor.compareToIgnoreCase(" ") == 0) {
                ordenarPor = "numeroPedido";
            }

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            if (!todos) {
                total = compraManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = compraManager.listAtributos(ejemplo, atributosCompras.split(","), todos, inicio, cantidad,
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
            retorno.setError(true);
            retorno.setMensaje("Error al optener pedidos");
            logger.error("Error al listar", e);
        }

        return retorno;
    }    

    
        @RequestMapping(value = "/{id}/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@PathVariable("id") Long id, @ModelAttribute("Compra") Compra compraRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        try {
            inicializarCompraManager();
            inicializarPedidoManager();
            inicializarImagenManager();
            inicializarContactoManager();
            
            
            if (id == null || id != null
                    && id.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La compra no posee ningun pedido.");
                return mensaje;
            }
            
            if (compraRecibido.getNroFactura()== null || compraRecibido.getNroFactura() != null
                    && compraRecibido.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }
            
            mensaje = compraManager.guardarCompra(id, compraRecibido, compraRecibido.getFormaPago(),
                    compraRecibido.getTipoDescuento(), userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        Pedido ejPedido = new Pedido();
        ejPedido.setId(id);
        ejPedido.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        try {

            inicializarPedidoManager();

            ejPedido = pedidoManager.get(ejPedido);

            if (ejPedido != null) {
                nombre = ejPedido.getCodigo().toString();
            }

            if (ejPedido != null && ejPedido.getActivo()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El pedido " + nombre + " ya se encuentra desactivado.");
            }
            ejPedido.setActivo("N");
            ejPedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejPedido.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            pedidoManager.update(ejPedido);

            retorno.setError(false);
            retorno.setMensaje("El pedido " + nombre + " se desactivo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de desactivar el pedido.");
            logger.error("Error al tratar de desactivar el pedido ", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        Pedido ejPedido = new Pedido();
        ejPedido.setId(id);
        ejPedido.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {

            inicializarPedidoManager();

            ejPedido = pedidoManager.get(ejPedido);

            if (ejPedido != null) {
                nombre = ejPedido.getCodigo().toString();
            }

            if (ejPedido != null && ejPedido.getActivo().toString()
                    .compareToIgnoreCase("N") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El pedido " + nombre + " ya se encuentra activo.");
            }
            ejPedido.setActivo("S");
            ejPedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejPedido.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            pedidoManager.update(ejPedido);

            retorno.setError(false);
            retorno.setMensaje("El pedido " + nombre + " se activo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de activar el pedido.");
            logger.error("Error al tratar de activar el pedido ", ex);
        }

        return retorno;

    }
}