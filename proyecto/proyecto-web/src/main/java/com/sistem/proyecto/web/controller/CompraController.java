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
import com.sistem.proyecto.entity.DocumentoCobrar;
import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Movimiento;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
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
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/compras")
public class CompraController extends BaseController {

    String atributos = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,total,proveedor.id,"
            + "proveedor.nombre,activo,usuario.nombre,cantidadAprobados,cantidadTotal";
    String atributosApagar = "id,nroCuota,monto,saldo,montoInteres,fecha,estado";

    String atributosCompras = "id,estadoCompra,nroFactura,fechaCuota,fechaCompra,tipoCompra,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,proveedor.nombre,activo,pedido.usuario.nombre,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,pedido.numeroPedido,pedido.codigo,pedido.fechaEntrega,"
            + "pedido.cantidadAprobados,pedido.cantidadTotal,pedido.total,proveedor.id,proveedor.ruc,proveedor.nombre,proveedor.direccion,proveedor.telefono";

    String atributosVehiculoCompra = "compra.id,compra.nroFactura,compra.fechaCompra,compra.tipoCompra,compra.formaPago,compra.porcentajeInteresCredito,"
            + "compra.montoInteres,compra.tipoMoraInteres,compra.moraInteres,compra.cantidadCuotas,compra.montoCuotas,"
            + "compra.montoTotalCuotas,compra.fechaCuota,compra.entrega,compra.saldo,compra.tipoDescuento,"
            + "compra.descuento,compra.monto,compra.entrega,compra.montoDescuento,compra.neto,"
            + "compra.proveedor.id,compra.proveedor.nombre,compra.proveedor.ruc";
    
    String atributosPagosRealizados = "id,proveedor.nombre,proveedor.ruc,proveedor.email,proveedor.telefono,proveedor.telefonoMovil,"
            + "cliente.nombre,cliente.documento,cliente.email,cliente.telefono,cliente.telefonoMovil,"
            + "fechaIngreso,tipoTransaccion,importe,neto,saldo,vuelto,fechaVencimiento,venta.nroFactura,"
            + "cuota,interes,compra.estadoCompra,compra.nroFactura,compra.formaPago,compra.cantidadCuotas,compra.fechaCuota,compra.fechaCompra";

    
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarPermisos(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("comprasListar");
        return retorno;
    }

    @RequestMapping(value = "/registros", method = RequestMethod.GET)
    public ModelAndView listarRegistros(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("comprasListar");
        //model.addAttribute("action", "PENDIENTE");
        return retorno;
    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView formEdit(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("compraForm");
        model.addAttribute("action", "EDITAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/directa/crear", method = RequestMethod.GET)
    public ModelAndView formCreate(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("compraForm");
        model.addAttribute("action", "CREAR");
        //model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/registros/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("compraForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
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
    
    @RequestMapping(value = "/obtener/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno obtenerCompraVehiculo(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarDetalleCompraManager();
            DetalleCompra ejCompra = new DetalleCompra();
            ejCompra.setVehiculo(new Vehiculo (id));

            Map<String, Object> ejCompraMap = detalleCompraManager.getAtributos(ejCompra, atributosVehiculoCompra.split(","));
//            ejCompraMap.put("montoCuotas", Long.parseLong(Double.parseDouble(ejCompraMap.get("compra.montoCuotas").toString())+""));
//            ejCompraMap.put("montoInteres", Long.parseLong(Double.parseDouble(ejCompraMap.get("compra.montoInteres").toString())+""));
//            ejCompraMap.put("monto", Long.parseLong(Double.parseDouble(ejCompraMap.get("compra.monto").toString())+""));
//            ejCompraMap.put("saldo", Long.parseLong(Double.parseDouble(ejCompraMap.get("compra.saldo").toString())+""));
            
            
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
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Compra ejemplo = new Compra();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listOrdenesMap = null;
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
            List<Long> ordenesCompras = new ArrayList<Long>();
            Compra ejOrden = new Compra();
            ejOrden.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejOrden.setEstadoCompra(Compra.ORDEN_COMPRA);

            listOrdenesMap = compraManager.listAtributos(ejOrden, "id".split(","), true, null, null,
                    "id".split(","), "ASD".split(","), true, true, "id", null,
                    null, null, null, null, null, null, null, null, true);

            for (Map<String, Object> rpm : listOrdenesMap) {
                ordenesCompras.add(Long.parseLong(rpm.get("id").toString()));
            }

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            if (!todos) {
                total = compraManager.list(ejemplo, true).size() - ordenesCompras.size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = compraManager.listAtributos(ejemplo, atributosCompras.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    "id", ordenesCompras, "OP_NOT_IN", null, null, null, null, null, true);

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

    @RequestMapping(value = "/docApagar/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarApagar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("idCompra") String idCompra,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DocumentoPagar ejemplo = new DocumentoPagar();
        ejemplo.setCompra(new Compra(Long.parseLong(idCompra)));

        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarDocumentoPagarManager();

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
                total = documentoPagarManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = documentoPagarManager.listAtributos(ejemplo, atributosApagar.split(","), todos, inicio, cantidad,
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

    @RequestMapping(value = "/directa/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("DetalleCompra") DetalleCompra detalleCompra) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        try {
            inicializarCompraManager();
            inicializarMonedaManager();
            inicializarVehiculoManager();
            inicializarDetalleCompraManager();

            if (detalleCompra.getId() == null || detalleCompra.getId() != null
                    && detalleCompra.getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al editar la compra.");
                return mensaje;
            }

            if (detalleCompra.getCompra().getNroFactura() == null || detalleCompra.getCompra().getNroFactura() != null
                    && detalleCompra.getCompra().getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getTipo() == null || detalleCompra.getVehiculo().getTipo().getId() != null
                    && detalleCompra.getVehiculo().getTipo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El tipo de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getMarca() == null || detalleCompra.getVehiculo().getMarca().getId() != null
                    && detalleCompra.getVehiculo().getMarca().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La marca de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getModelo() == null || detalleCompra.getVehiculo().getModelo().getId() != null
                    && detalleCompra.getVehiculo().getModelo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El modelo del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getAnho() == null || detalleCompra.getVehiculo().getAnho() != null
                    && detalleCompra.getVehiculo().getAnho().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El año del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getColor() == null || detalleCompra.getVehiculo().getColor() != null
                    && detalleCompra.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getColor() == null || detalleCompra.getVehiculo().getColor() != null
                    && detalleCompra.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getTransmision() == null || detalleCompra.getVehiculo().getTransmision() != null
                    && detalleCompra.getVehiculo().getTransmision().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La transmision del vehiculo no puede estar vacia.");
                return mensaje;
            }

            if (detalleCompra.getMoneda() == null || detalleCompra.getMoneda().getId() != null
                    && detalleCompra.getMoneda().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo moneda no puede estar vacia.");
                return mensaje;
            }

            if (detalleCompra.getPrecio() == null || detalleCompra.getPrecio() != null
                    && detalleCompra.getPrecio().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo precio no puede estar vacio.");
                return mensaje;
            }

            ejDetalleCompra = detalleCompraManager.get(detalleCompra.getId());

            Vehiculo ejVehiculo = vehiculoManager.get(ejDetalleCompra.getVehiculo().getId());

            ejVehiculo.setAnho(detalleCompra.getVehiculo().getAnho());
            ejVehiculo.setCaracteristica(detalleCompra.getVehiculo().getCaracteristica());
            ejVehiculo.setMarca(detalleCompra.getVehiculo().getMarca());
            ejVehiculo.setTipo(detalleCompra.getVehiculo().getTipo());
            ejVehiculo.setModelo(detalleCompra.getVehiculo().getModelo());
            ejVehiculo.setColor(detalleCompra.getVehiculo().getColor());
            ejVehiculo.setTransmision(detalleCompra.getVehiculo().getTransmision());
            ejVehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));

            vehiculoManager.update(ejVehiculo);

            Moneda ejMoneda = monedaManager.get(detalleCompra.getMoneda());

            Double cambio = ejMoneda.getValor();

            Long total = Math.round(detalleCompra.getPrecio() * cambio);

            ejDetalleCompra.setMoneda(detalleCompra.getMoneda());
            ejDetalleCompra.setCambioDia(detalleCompra.getCambioDia());
            ejDetalleCompra.setTotal(Double.parseDouble(total.toString()));
            ejDetalleCompra.setNeto(Double.parseDouble(total.toString()));
            ejDetalleCompra.setPrecio(detalleCompra.getPrecio());

            detalleCompraManager.update(ejDetalleCompra);

            Long totalPedido = Long.parseLong("0");
            DetalleCompra ejDetalle = new DetalleCompra();
            ejDetalle.setCompra(ejDetalleCompra.getCompra());

            List<DetalleCompra> ejDetalleCom = detalleCompraManager.list(ejDetalle);

            for (DetalleCompra rpm : ejDetalleCom) {
                totalPedido = totalPedido + Math.round(rpm.getTotal());
            }
            ejCompra = compraManager.get(ejDetalleCompra.getCompra().getId());
            
            ejCompra.setMonto(totalPedido.toString());
            
            compraManager.update(ejCompra);
            
            mensaje.setError(false);
            mensaje.setId(ejCompra.getId());
            mensaje.setMensaje("El detalle de la compra se modifico exitosamente");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }
    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO compraGuardar(@ModelAttribute("Compra") Compra compraRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        
        try {
            inicializarCompraManager();
            inicializarPedidoManager();
            inicializarImagenManager();
            inicializarContactoManager();

            if (compraRecibido.getNroFactura() == null || compraRecibido.getNroFactura() != null
                    && compraRecibido.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }
            if (compraRecibido.getProveedor() == null || compraRecibido.getProveedor().getId() == null
                    || compraRecibido.getProveedor().getId() != null
                    && compraRecibido.getProveedor().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un proveedor para realizar la compra.");
                return mensaje;
            }
            
            ejCompra.setNroFactura(compraRecibido.getNroFactura());
            ejCompra.setProveedor(new Proveedor(compraRecibido.getProveedor().getId()));
            compraManager.save(ejCompra);
            
            mensaje.setId(ejCompra.getId());
            mensaje.setError(false);
            mensaje.setMensaje("La compra se registro exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/directa/guardar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO compraDirectaGuardar(@ModelAttribute("DetalleCompra") DetalleCompra compraRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        try {
            inicializarCompraManager();
            inicializarPedidoManager();
            inicializarImagenManager();
            inicializarContactoManager();

            if (compraRecibido.getCompra().getNroFactura() == null || compraRecibido.getCompra().getNroFactura() != null
                    && compraRecibido.getCompra().getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            mensaje = compraManager.guardarCompra(compraRecibido.getCompra().getId(), compraRecibido, compraRecibido.getCompra().getNroFactura(), compraRecibido.getCompra().getFormaPago(),
                    compraRecibido.getCompra().getTipoDescuento(), userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el cliente");
            logger.debug("Error al guardar cliente ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/detalles/descuento", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO guardarDescuento(@ModelAttribute("Compra") DetalleCompra compraRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarCompraManager();
            inicializarDetalleCompraManager();

            if (compraRecibido.getPorcentajeDescuento() == null || compraRecibido.getPorcentajeDescuento() != null
                    && compraRecibido.getPorcentajeDescuento().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Interes de Descuento no puede estar vacio.");
                return mensaje;
            }

            ejDetalleCompra = detalleCompraManager.get(compraRecibido.getId());

            Double descuento = (ejDetalleCompra.getTotal() * Double.valueOf(compraRecibido.getPorcentajeDescuento())) / 100;
            Double neto = ejDetalleCompra.getTotal() - descuento;

            ejDetalleCompra.setNeto(neto);
            ejDetalleCompra.setPorcentajeDescuento(compraRecibido.getPorcentajeDescuento());
            ejDetalleCompra.setMontoDescuento(descuento);

            detalleCompraManager.update(ejDetalleCompra);

            ejCompra = compraManager.get(ejDetalleCompra.getCompra());

            ejDetalleCompra = new DetalleCompra();
            ejDetalleCompra.setCompra(ejCompra);

            List<DetalleCompra> ejDetalles = detalleCompraManager.list(ejDetalleCompra);

            Double netoPagar = 0.0;

            for (DetalleCompra rpm : ejDetalles) {
                if (rpm.getNeto() != null) {
                    netoPagar = netoPagar + Double.valueOf(rpm.getNeto());
                } else {
                    netoPagar = netoPagar + Double.valueOf(rpm.getTotal());

                }
            }
            ejCompra.setNeto(netoPagar);
            ejCompra.setFormaPago("CONTADO");
            ejCompra.setTipoDescuento("DETALLADO");
            // ejCompra.setNroFactura(compraRecibido.getNroFactura());

            compraManager.update(ejCompra);
            
            mensaje.setId(ejCompra.getId());
            mensaje.setError(false);
            mensaje.setMensaje("El descuento se agrego exitosamente.");

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al agregar el descuento");
            logger.error("Error al agregar el descuento ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/{id}/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editarCompra(@PathVariable("id") Long id, @ModelAttribute("Compra") Compra compraRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        DetalleCompra ejDetalleCompra = new DetalleCompra();
        try {
            inicializarCompraManager();

            if (compraRecibido.getNroFactura() == null || compraRecibido.getNroFactura() != null
                    && compraRecibido.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }
            
            

            mensaje = compraManager.editarCompra(compraRecibido.getId(), compraRecibido, compraRecibido.getFormaPago(),
                    compraRecibido.getTipoDescuento(), userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception ex) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar la compra");
            logger.debug("Error al guardar la compra ", ex);
        }

        return mensaje;
    }

    @RequestMapping(value = "/registros/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        Compra ejCompra = new Compra();
        ejCompra.setId(id);
        ejCompra.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        
        try {

            inicializarCompraManager();
            inicializarDetalleCompraManager();
            inicializarVehiculoManager();

            ejCompra = compraManager.get(ejCompra);

            if (ejCompra != null) {
                nombre = ejCompra.getNroFactura().toString();
            }

            if (ejCompra != null && ejCompra.getEstadoCompra()
                    .compareToIgnoreCase(Compra.COMPRA_RECHAZADA) == 0) {
                retorno.setError(true);
                retorno.setMensaje("La compra " + nombre + " ya se encuentra rechazada.");
            }
            ejCompra.setEstadoCompra(Compra.COMPRA_RECHAZADA);
            ejCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejCompra.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            compraManager.update(ejCompra);

            DetalleCompra ejDetalle = new DetalleCompra();
            ejDetalle.setCompra(ejCompra);
            
            List<DetalleCompra> compraDetalle = detalleCompraManager.list(ejDetalle);
            
            for(DetalleCompra rpm : compraDetalle){
                
                rpm.getVehiculo().setEstado(Vehiculo.PENDIENTE);
                
                vehiculoManager.update(rpm.getVehiculo());
            }
            retorno.setError(false);
            retorno.setMensaje("La compra " + nombre + " ya se  rechazo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de rechazar la compra.");
            logger.error("Error al tratar de rechazar la compra.", ex);
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
    
    
    @RequestMapping(value = "/docs/{id}", method = RequestMethod.GET)
    public ModelAndView listaDocsPagar(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("documentosPagarListar");
        model.addAttribute("id", id);
        return retorno;

    }
    
    @RequestMapping(value = "/docsPagar/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarDocumentosPagar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("idCompra") String idCompra,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DocumentoPagar ejemplo = new DocumentoPagar();
        ejemplo.setCompra(new Compra(Long.parseLong(idCompra)));

        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarDocumentoPagarManager();

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
                total = documentoPagarManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = documentoPagarManager.listAtributos(ejemplo, atributosApagar.split(","), todos, inicio, cantidad,
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
            retorno.setMensaje("Error al obtener lista de documentos a pagar");
            logger.error("Error al listar", e);
        }

        return retorno;
    }
    
    @RequestMapping(value = "/pagos/{id}", method = RequestMethod.GET)
    public ModelAndView listaPagos(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("pagosContadoListar");
        model.addAttribute("id", id);
        return retorno;

    }
    
    @RequestMapping(value = "/pagosrealizados/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarPagosRealizados(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("idCompra") String idCompra,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Movimiento ejemplo = new Movimiento();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setCompra(new Compra(Long.parseLong(idCompra)));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listComprasMap = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaIngreso");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaIngreso");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        try {

            inicializarMovimientoManager();

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
                    if (filtro.getData() != null && filtro.getData().compareToIgnoreCase("") != 0) {
                        ejemplo.setProveedor(new Proveedor(Long.parseLong(filtro.getData())));
                    }//ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejemplo.setTipoTransaccion(estado);
            }

            if (fechaInicio != null && fechaInicio.compareToIgnoreCase("") != 0
                    && fechaFin != null && fechaFin.compareToIgnoreCase("") != 0) {

                resultFechaInicio = dateFormat.parse(fechaInicio);
                resultFechaFin = dateFormat.parse(fechaFin);

            } else {

                Date date = new Date();
                date.setHours(00);
                date.setMinutes(00);

                resultFechaInicio = date;

                date.setHours(23);
                date.setMinutes(59);

                resultFechaFin = date;
            }

            valorInicio.add(resultFechaInicio);
            valorFin.add(resultFechaFin);

            // ejemplo.setActivo("S");
            if (ordenarPor == null || ordenarPor != null && ordenarPor.compareToIgnoreCase(" ") == 0) {
                ordenarPor = "nroFactura";
            }

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            
            
            if (!todos) {
                total = movimientoManager.listAtributos(ejemplo, atributosPagosRealizados.split(","), true, null, null,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, null, null, null,
                        null, null, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = movimientoManager.listAtributos(ejemplo, atributosPagosRealizados.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, null, null, null,
                    null, null, true);

            if (todos) {
                total = listMapGrupos.size();
            }

            Integer totalPaginas = total / cantidad;

            retorno.setTotal(totalPaginas + 1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al obtener compras");
            logger.error("Error al listar", e);
        }

        return retorno;
    }
    
    
}
