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
import com.sistem.proyecto.manager.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.manager.utils.PagoDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/cobros")
public class CobroController extends BaseController {

    String atributos = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,total,proveedor.id,"
            + "proveedor.nombre,activo,usuario.nombre,cantidadAprobados,cantidadTotal";
    String atributosDetalle = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,descuento,total,neto,proveedor.id,"
            + "proveedor.nombre";

    String atributosCompras = "id,nroFactura,fechaCompra,tipoCompra,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,proveedor.nombre,activo,pedido.usuario.nombre,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,pedido.numeroPedido,pedido.codigo,pedido.fechaEntrega,"
            + "pedido.cantidadAprobados,pedido.cantidadTotal,pedido.total,proveedor.id,proveedor.ruc,proveedor.nombre,proveedor.direccion,proveedor.telefono";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarPermisos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cobrosForm");
        return retorno;
    }

    @RequestMapping(value = "/pendientes", method = RequestMethod.GET)
    public ModelAndView listarPendientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("comprasListar");
        model.addAttribute("action", "PENDIENTE");
        return retorno;
    }

    @RequestMapping(value = "/realizadas", method = RequestMethod.GET)
    public ModelAndView listarRealizadas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("comprasListar");
        model.addAttribute("action", "REALIZADA");
        return retorno;
    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("compraForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno calcularDeduda(@PathVariable("id") Long id) {
        DTORetorno<PagoDTO> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarMovimientoManager();
            PagoDTO ejPago = new PagoDTO();
        

            DTORetorno<PagoDTO> ejPagoMap = movimientoManager.obtenerDatosPago(id);

            retorno.setData(ejPagoMap.getData());
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la compra");

        } catch (Exception ex) {
            logger.error("Error al obtener la compra", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la compra");
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
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Compra ejemplo = new Compra();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoCompra(Compra.COMPRA_APROBADA);

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
    
    @RequestMapping(value = "/realizar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardar(@ModelAttribute("PagoDTO") PagoDTO pagoRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        
        try {
            inicializarMovimientoManager();

            if (pagoRecibido.getIdCompra() == null || pagoRecibido.getIdCompra() != null
                    && pagoRecibido.getIdCompra().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al realizar el pago.");
                return mensaje;
            }

            if (pagoRecibido.getImportePagar() == null || pagoRecibido.getImportePagar() != null
                    && pagoRecibido.getImportePagar()  == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el importe a Pagar.");
                return mensaje;
            }
            
            if (pagoRecibido.getInteres()== null || pagoRecibido.getInteres() != null
                    && pagoRecibido.getInteres().toString().compareToIgnoreCase("")  == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar el interes a Pagar.");
                return mensaje;
            }
            
            if (pagoRecibido.getInteres() > pagoRecibido.getImportePagar() ) {
                mensaje.setError(true);
                mensaje.setMensaje("El interes no puede ser mayor al importe a pagar.");
                return mensaje;
            }
            
           mensaje = movimientoManager.realizarCompra(pagoRecibido.getIdCompra(), pagoRecibido.getImportePagar(), pagoRecibido.getInteres(),
                   pagoRecibido.getIdDocPagar(), userDetail.getIdEmpresa(), userDetail.getId());



        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el vehiculo");
            logger.debug("Error al guardar vehiculo ", ex);
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
