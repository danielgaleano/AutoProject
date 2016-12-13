/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Movimiento;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.manager.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.DetalleCanvas;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author daniel
 */
@Controller
@RequestMapping(value = "/reportes")
public class ReportesController extends BaseController {

    String atributosCompras = "id,estadoPago,estadoCompra,nroFactura,fechaCuota,fechaCompra,tipoCompra,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,proveedor.nombre,activo,pedido.usuario.nombre,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,pedido.numeroPedido,pedido.codigo,pedido.fechaEntrega,"
            + "pedido.cantidadAprobados,pedido.cantidadTotal,pedido.total,proveedor.id,proveedor.ruc,proveedor.nombre,proveedor.direccion,proveedor.telefono";

    String atributos = "id,proveedor.nombre,proveedor.ruc,proveedor.email,proveedor.telefono,proveedor.telefonoMovil,"
            + "cliente.nombre,cliente.documento,cliente.email,cliente.telefono,cliente.telefonoMovil,"
            + "fechaIngreso,tipoTransaccion,importe,neto,saldo,vuelto,fechaVencimiento,venta.nroFactura,"
            + "cuota,interes,compra.estadoCompra,compra.nroFactura,compra.formaPago,compra.cantidadCuotas,compra.fechaCuota,compra.fechaCompra";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaVentas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ventasListar");
        return retorno;

    }

    @RequestMapping(value = "/movimiento", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteMovimiento");
        return retorno;

    }

    @RequestMapping(value = "/compras", method = RequestMethod.GET)
    public ModelAndView compras(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteCompras");
        return retorno;

    }

    @RequestMapping(value = "/compras/pendientes", method = RequestMethod.GET)
    public ModelAndView comprasPendientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteComprasPendientes");
        return retorno;

    }

    @RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
    public ModelAndView formEdit(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ventaForm");
        model.addAttribute("action", "EDITAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/visualizar/{id}", method = RequestMethod.GET)
    public ModelAndView formView(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ventaForm");
        model.addAttribute("action", "VISUALIZAR");
        model.addAttribute("id", id);
        return retorno;
    }

    @RequestMapping(value = "/grafico/movimiento", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteMovimiento(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("estado") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaIngreso");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaIngreso");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Movimiento ejemplo = new Movimiento();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {
            inicializarMovimientoManager();

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

            listMapGrupos = movimientoManager.listAtributos(ejemplo, atributos.split(","), true, null, null,
                    "fechaIngreso".split(","), "asd".split(","), true, true, null, null,
                    null, null, null, atributoInicio, valorInicio, atributoFin,
                    valorFin, null, true);

            Long totalEgreso = Long.parseLong("0");
            Long totalIngreso = Long.parseLong("0");

            for (Map<String, Object> rpm : listMapGrupos) {
                if (rpm.get("tipoTransaccion").toString().compareToIgnoreCase("O") == 0) {
                    totalEgreso = totalEgreso + Math.round(Double.parseDouble(rpm.get("neto").toString()));
                } else if (rpm.get("tipoTransaccion").toString().compareToIgnoreCase("I") == 0) {
                    totalIngreso = totalIngreso + Math.round(Double.parseDouble(rpm.get("neto").toString()));
                }
            }

            DetalleCanvas ejEgreso = new DetalleCanvas();
            ejEgreso.setLabel("Egreso");
            ejEgreso.setY(totalEgreso);

            reporte.add(ejEgreso);

            DetalleCanvas ejIngreso = new DetalleCanvas();
            ejIngreso.setLabel("Ingreso");
            ejIngreso.setY(totalIngreso);

            reporte.add(ejIngreso);

            retorno.setData(reporte);

            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la venta");

        } catch (Exception ex) {
            logger.error("Error al obtener la venta", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }

    @RequestMapping(value = "/grafico/compras", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteCompras(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("estado") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaIngreso");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaIngreso");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Movimiento ejemplo = new Movimiento();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setTipoTransaccion("O");

        try {
            inicializarMovimientoManager();
            inicializarProveedorManager();
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

            Proveedor ejProveedor = new Proveedor();
            ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejProveedor.setId(Long.parseLong(estado));
            }

            List<Map<String, Object>> listMapProveedor = proveedorManager.listAtributos(ejProveedor, "id,nombre".split(","));

            Long total = Long.parseLong("0");

            for (Map<String, Object> pro : listMapProveedor) {

                ejemplo.setProveedor(new Proveedor(Long.parseLong(pro.get("id").toString())));

                listMapGrupos = movimientoManager.listAtributos(ejemplo, atributos.split(","), true, null, null,
                        "fechaIngreso".split(","), "asd".split(","), true, true, null, null,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true);

                Long totalEgreso = Long.parseLong("0");

                for (Map<String, Object> rpm : listMapGrupos) {
                    totalEgreso = totalEgreso + Math.round(Double.parseDouble(rpm.get("neto").toString()));
                }

                total = total + totalEgreso;

                if (totalEgreso > 0) {
                    DetalleCanvas ejEgreso = new DetalleCanvas();
                    ejEgreso.setLabel(pro.get("nombre").toString());
                    ejEgreso.setY(totalEgreso);

                    reporte.add(ejEgreso);
                }

            }

            retorno.setData(reporte);

            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la venta");

        } catch (Exception ex) {
            logger.error("Error al obtener la venta", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }

    @RequestMapping(value = "/grafico/compras/pendientes", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteComprasPendientes(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("estado") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaCompra");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaCompra");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Compra ejemplo = new Compra();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoPago("PENDIENTE");

        try {
            inicializarCompraManager();
            inicializarProveedorManager();
            inicializarDocumentoPagarManager();
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

            Proveedor ejProveedor = new Proveedor();
            ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejProveedor.setId(Long.parseLong(estado));
            }

            List<Map<String, Object>> listMapProveedor = proveedorManager.listAtributos(ejProveedor, "id,nombre".split(","));

            Long total = Long.parseLong("0");

            for (Map<String, Object> pro : listMapProveedor) {

                ejemplo.setProveedor(new Proveedor(Long.parseLong(pro.get("id").toString())));

                listMapGrupos = compraManager.listAtributos(ejemplo, atributosCompras.split(","), true, null, null,
                        "fechaCompra".split(","), "asd".split(","), true, true, null, null,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true);

                Long totalEgreso = Long.parseLong("0");

                for (Map<String, Object> rpm : listMapGrupos) {

                    if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {
                        DocumentoPagar docPagar = new DocumentoPagar();
                        docPagar.setCompra(new Compra(Long.parseLong(rpm.get("id").toString())));

                        List<DocumentoPagar> aPagar = documentoPagarManager.list(docPagar);

                        for (DocumentoPagar pagar : aPagar) {
                            if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {
                                totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                            } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {
                                totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                                totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            }
                        }
                    } else {
                        totalEgreso = totalEgreso + Math.round(Double.parseDouble(rpm.get("neto").toString()));
                    }

                }

                total = total + totalEgreso;

                if (totalEgreso > 0) {
                    DetalleCanvas ejEgreso = new DetalleCanvas();
                    ejEgreso.setLabel(pro.get("nombre").toString());
                    ejEgreso.setY(totalEgreso);

                    reporte.add(ejEgreso);
                }

            }

            retorno.setData(reporte);

            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente el reporte compra");

        } catch (Exception ex) {
            logger.error("Error al obtener el reporte compra", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }
    
    @RequestMapping(value = "/grafico/compras/realizadas", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteComprasRealizadas(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("estado") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaCompra");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaCompra");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Compra ejemplo = new Compra();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoPago("CANCELADO");

        try {
            inicializarCompraManager();
            inicializarProveedorManager();
            inicializarDocumentoPagarManager();
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

            Proveedor ejProveedor = new Proveedor();
            ejProveedor.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejProveedor.setId(Long.parseLong(estado));
            }

            List<Map<String, Object>> listMapProveedor = proveedorManager.listAtributos(ejProveedor, "id,nombre".split(","));

            Long total = Long.parseLong("0");

            for (Map<String, Object> pro : listMapProveedor) {

                ejemplo.setProveedor(new Proveedor(Long.parseLong(pro.get("id").toString())));

                listMapGrupos = compraManager.listAtributos(ejemplo, atributosCompras.split(","), true, null, null,
                        "fechaCompra".split(","), "asd".split(","), true, true, null, null,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true);

                Long totalEgreso = Long.parseLong("0");

                for (Map<String, Object> rpm : listMapGrupos) {

                    if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {
                        DocumentoPagar docPagar = new DocumentoPagar();
                        docPagar.setCompra(new Compra(Long.parseLong(rpm.get("id").toString())));

                        List<DocumentoPagar> aPagar = documentoPagarManager.list(docPagar);

                        for (DocumentoPagar pagar : aPagar) {
                            if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.CANCELADO) == 0) {
                                totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                            } 
                        }
                    } else {
                        totalEgreso = totalEgreso + Math.round(Double.parseDouble(rpm.get("neto").toString()));
                    }

                }

                total = total + totalEgreso;

                if (totalEgreso > 0) {
                    DetalleCanvas ejEgreso = new DetalleCanvas();
                    ejEgreso.setLabel(pro.get("nombre").toString());
                    ejEgreso.setY(totalEgreso);

                    reporte.add(ejEgreso);
                }

            }

            retorno.setData(reporte);

            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente el reporte compra");

        } catch (Exception ex) {
            logger.error("Error al obtener el reporte compra", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }

    @RequestMapping(value = "/movimientos/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Movimiento ejemplo = new Movimiento();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listVentasMap = null;
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
                total = movimientoManager.listAtributos(ejemplo, atributos.split(","), true, null, null,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = movimientoManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, atributoInicio, valorInicio, atributoFin,
                    valorFin, null, true);

            if (todos) {
                total = listMapGrupos.size();
            }

            Integer totalPaginas = total / cantidad;

            retorno.setTotal(totalPaginas + 1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al optener ventas");
            logger.error("Error al listar", e);
        }

        return retorno;
    }

    @RequestMapping(value = "detalles/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarDetalle(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("idDetalle") String idDetalle,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DetalleVenta ejemplo = new DetalleVenta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        if (idVenta != null && idVenta.compareToIgnoreCase("") != 0) {
            ejemplo.setVenta(new Venta(Long.parseLong(idVenta)));
        } else if (idDetalle != null && idDetalle.compareToIgnoreCase("") != 0) {
            ejemplo.setId(Long.parseLong(idDetalle));
        }
        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarDetalleVentaManager();

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
                ordenarPor = "tipo.nombre";
            }

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;
            if (idVenta != null && idVenta.compareToIgnoreCase("") != 0
                    || idDetalle != null && idDetalle.compareToIgnoreCase("") != 0) {
                if (!todos) {
                    total = detalleVentaManager.list(ejemplo, true).size();
                }

                Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

                if (total < inicio) {
                    inicio = total - total % cantidad;
                    pagina = total / cantidad;
                }

                listMapGrupos = detalleVentaManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad,
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
    MensajeDTO guardar(@ModelAttribute("Venta") Venta ventaRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Venta ejVenta = new Venta();
        try {
            inicializarVentaManager();

            if (ventaRecibido.getNroFactura() == null || ventaRecibido.getNroFactura() != null
                    && ventaRecibido.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de factura no puede estar vacia.");
                return mensaje;
            }

            if (ventaRecibido.getCliente() == null
                    || ventaRecibido.getCliente().getId() == null
                    || ventaRecibido.getCliente().getId() != null
                    && ventaRecibido.getCliente().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Se debe ingresar un cliente para realizar la venta.");
                return mensaje;
            }

            if (ventaRecibido.getFormaPago() == null || ventaRecibido.getFormaPago() != null
                    && ventaRecibido.getFormaPago().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe seleccionar una forma de pago.");
                return mensaje;
            }

            if (ventaRecibido.getItemsVenta() == null || ventaRecibido.getItemsVenta() != null
                    && ventaRecibido.getItemsVenta().isEmpty()) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe seleccionar un vehiculo para realizar la venta.");
                return mensaje;
            }

            mensaje = ventaManager.guardarVenta(ventaRecibido.getItemsVenta(), ventaRecibido, ventaRecibido.getNroFactura(), ventaRecibido.getFormaPago(), "GENERAL", userDetail.getIdEmpresa(), userDetail.getId());

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el usuario");
            System.out.println("Error" + e);
        }

        return mensaje;
    }

    @RequestMapping(value = "/compras/pendientes/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarComprasPendientes(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
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
        List<Map<String, Object>> listVentasMap = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaCompra");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaCompra");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

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
                    if (filtro.getData() != null && filtro.getData().compareToIgnoreCase("") != 0) {
                        ejemplo.setProveedor(new Proveedor(Long.parseLong(filtro.getData())));
                    }//ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejemplo.setEstadoPago(estado);
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
                total = compraManager.listAtributos(ejemplo, atributosCompras.split(","), true, null, null,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = compraManager.listAtributos(ejemplo, atributosCompras.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, atributoInicio, valorInicio, atributoFin,
                    valorFin, null, true);
            
            for (Map<String, Object> rpm : listMapGrupos) {
                Long totalEgreso = Long.parseLong("0");
                if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {
                    
                    DocumentoPagar docPagar = new DocumentoPagar();
                    docPagar.setCompra(new Compra(Long.parseLong(rpm.get("id").toString())));

                    List<DocumentoPagar> aPagar = documentoPagarManager.list(docPagar,"nroCuota","asc");
                    Double importeCuota = 0.0;
                    boolean primeraCuota = true;
                    
                    String cuotaPendiente = "";
                    
                    for (DocumentoPagar pagar : aPagar) {
                        if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            importeCuota = pagar.getMonto();
                            if(primeraCuota){
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            if(primeraCuota){
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        }
                    }
                    rpm.put("saldo", totalEgreso);
                    rpm.put("importe", importeCuota);
                    rpm.put("cuota", cuotaPendiente);
                } 
            }

            if (todos) {
                total = listMapGrupos.size();
            }

            Integer totalPaginas = total / cantidad;

            retorno.setTotal(totalPaginas + 1);
            retorno.setRetorno(listMapGrupos);
            retorno.setPage(pagina);

        } catch (Exception e) {
            retorno.setError(true);
            retorno.setMensaje("Error al optener ventas");
            logger.error("Error al listar", e);
        }

        return retorno;
    }

}
