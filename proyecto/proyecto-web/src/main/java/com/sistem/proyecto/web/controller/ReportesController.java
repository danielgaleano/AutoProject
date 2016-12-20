/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.DocumentoCobrar;
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
import com.sistem.proyecto.utils.JasperDatasource;
import com.sistem.proyecto.utils.ReglaDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRExporterParameter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ServletContext servletContext;

    String atributosCompras = "id,estadoPago,estadoCompra,nroFactura,fechaCuota,fechaCompra,tipoCompra,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,proveedor.nombre,activo,pedido.usuario.nombre,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,pedido.numeroPedido,pedido.codigo,pedido.fechaEntrega,"
            + "pedido.cantidadAprobados,pedido.cantidadTotal,pedido.total,proveedor.id,proveedor.ruc,proveedor.nombre,proveedor.direccion,proveedor.telefono";

    String atributosVentas = "id,estadoCobro,estadoVenta,nroFactura,fechaCuota,fechaVenta,tipoVenta,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,cliente.nombre,activo,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,"
            + "cliente.id,cliente.documento,cliente.nombre,cliente.direccion,cliente.telefono,diasGracia";

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

    @RequestMapping(value = "/compras/realizadas", method = RequestMethod.GET)
    public ModelAndView comprasRealizadas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteComprasRealizadas");
        return retorno;

    }

    @RequestMapping(value = "/ventas/pendientes", method = RequestMethod.GET)
    public ModelAndView ventasPendientes(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteVentasPendientes");
        return retorno;

    }

    @RequestMapping(value = "/ventas/realizadas", method = RequestMethod.GET)
    public ModelAndView ventasRealizadas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("reporteVentasRealizadas");
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
            @ModelAttribute("idProveedor") String estado) {

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
            @ModelAttribute("idProveedor") String estado) {

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
            @ModelAttribute("idProveedor") String estado) {

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

    @RequestMapping(value = "/movimientos/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarMovimientos(@ModelAttribute("_search") boolean filtrar,
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
            
            for(Map<String, Object> rpm: listMapGrupos){
                rpm.put("nroFactura", rpm.get("compra.nroFactura"));
                rpm.put("formaPago", rpm.get("compra.formaPago"));
                if(rpm.get("compra.cantidadCuotas") != null){
                    rpm.put("cantidadCuotas", Long.parseLong(rpm.get("compra.cantidadCuotas").toString()));
                }                
                rpm.put("proveedor", rpm.get("proveedor.nombre"));
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
                Long totalGeneral = Long.parseLong("0");
                if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {

                    totalGeneral = Math.round(Double.parseDouble(rpm.get("neto").toString())) + Math.round(Double.parseDouble(rpm.get("entrega").toString()));
                    DocumentoPagar docPagar = new DocumentoPagar();
                    docPagar.setCompra(new Compra(Long.parseLong(rpm.get("id").toString())));

                    List<DocumentoPagar> aPagar = documentoPagarManager.list(docPagar, "nroCuota", "asc");
                    Double importeCuota = 0.0;
                    boolean primeraCuota = true;

                    String cuotaPendiente = "";

                    for (DocumentoPagar pagar : aPagar) {
                        if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            importeCuota = pagar.getMonto();
                            if (primeraCuota) {
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            if (primeraCuota) {
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        }
                    }
                    rpm.put("saldo", totalEgreso);
                    rpm.put("importe", Math.round(importeCuota));
                    rpm.put("cuota", cuotaPendiente);
                    rpm.put("totalGeneral", totalGeneral);
                } else {
                    totalGeneral = Math.round(Double.parseDouble(rpm.get("neto").toString()));
                    
                    if(rpm.get("saldo") != null){
                        rpm.put("saldo", Long.parseLong(rpm.get("saldo").toString()));
                    }else{
                        rpm.put("saldo", Long.parseLong("0"));
                    }
                    
                    rpm.put("totalGeneral", totalGeneral);
                }
                rpm.put("neto", Math.round(Double.parseDouble(rpm.get("neto").toString())));
                rpm.put("proveedor", rpm.get("proveedor.nombre").toString());
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

    @RequestMapping(value = "/grafico/ventas/pendientes", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteVentasPendientes(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("idCliente") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaVenta");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaVenta");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Venta ejemplo = new Venta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoCobro("PENDIENTE");

        try {
            inicializarVentaManager();
            inicializarClienteManager();
            inicializarDocumentoCobrarManager();

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

            Cliente ejCliente = new Cliente();
            ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejCliente.setId(Long.parseLong(estado));
            }

            List<Map<String, Object>> listMapCliente = clienteManager.listAtributos(ejCliente, "id,nombre".split(","));

            Long total = Long.parseLong("0");

            for (Map<String, Object> pro : listMapCliente) {

                ejemplo.setCliente(new Cliente(Long.parseLong(pro.get("id").toString())));

                listMapGrupos = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), true, null, null,
                        "fechaVenta".split(","), "asd".split(","), true, true, null, null,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true);

                Long totalEgreso = Long.parseLong("0");

                for (Map<String, Object> rpm : listMapGrupos) {

                    if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {
                        DocumentoCobrar docPagar = new DocumentoCobrar();
                        docPagar.setVenta(new Venta(Long.parseLong(rpm.get("id").toString())));

                        List<DocumentoCobrar> aPagar = documentoCobrarManager.list(docPagar);

                        for (DocumentoCobrar pagar : aPagar) {
                            if (pagar.getEstado().compareToIgnoreCase(DocumentoCobrar.PARCIAL) == 0) {
                                totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                            } else if (pagar.getEstado().compareToIgnoreCase(DocumentoCobrar.PENDIENTE) == 0) {
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
            retorno.setMensaje("Se obtuvo exitosamente el reporte venta");

        } catch (Exception ex) {
            logger.error("Error al obtener el reporte venta", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }

    @RequestMapping(value = "/grafico/ventas/realizadas", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno reporteVentasRealizadas(@ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("idCliente") String estado) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DTORetorno<List<DetalleCanvas>> retorno = new DTORetorno<>();

        List<DetalleCanvas> reporte = new ArrayList<DetalleCanvas>();
        List<Map<String, Object>> listMapGrupos = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaVenta");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaVenta");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        Venta ejemplo = new Venta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setEstadoCobro("CANCELADO");

        try {
            inicializarVentaManager();
            inicializarClienteManager();
            inicializarDocumentoCobrarManager();

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

            Cliente ejCliente = new Cliente();
            ejCliente.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejCliente.setId(Long.parseLong(estado));
            }

            List<Map<String, Object>> listMapCliente = clienteManager.listAtributos(ejCliente, "id,nombre".split(","));

            Long total = Long.parseLong("0");

            for (Map<String, Object> pro : listMapCliente) {

                ejemplo.setCliente(new Cliente(Long.parseLong(pro.get("id").toString())));

                listMapGrupos = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), true, null, null,
                        "fechaVenta".split(","), "asd".split(","), true, true, null, null,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true);

                Long totalEgreso = Long.parseLong("0");

                for (Map<String, Object> rpm : listMapGrupos) {

                    if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {
                        DocumentoCobrar docPagar = new DocumentoCobrar();
                        docPagar.setVenta(new Venta(Long.parseLong(rpm.get("id").toString())));

                        List<DocumentoCobrar> aPagar = documentoCobrarManager.list(docPagar);

                        for (DocumentoCobrar pagar : aPagar) {
                            if (pagar.getEstado().compareToIgnoreCase(DocumentoCobrar.CANCELADO) == 0) {
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

    @RequestMapping(value = "/ventas/pendientes/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarVentasPendientes(@ModelAttribute("_search") boolean filtrar,
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

        Venta ejemplo = new Venta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listVentasMap = null;
        List<Object> valorInicio = new ArrayList<>();
        List<Object> valorFin = new ArrayList<>();

        List<String> atributoInicio = new ArrayList<>();
        atributoInicio.add("fechaVenta");
        List<String> atributoFin = new ArrayList<>();
        atributoFin.add("fechaVenta");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date resultFechaInicio = null;
        Date resultFechaFin = null;

        try {

            inicializarVentaManager();

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
                        ejemplo.setCliente(new Cliente(Long.parseLong(filtro.getData())));
                    }//ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            if (estado != null && estado.compareToIgnoreCase("") != 0) {
                ejemplo.setEstadoCobro(estado);
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
                total = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), true, null, null,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, atributoInicio, valorInicio, atributoFin,
                        valorFin, null, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, atributoInicio, valorInicio, atributoFin,
                    valorFin, null, true);

            for (Map<String, Object> rpm : listMapGrupos) {
                Long totalEgreso = Long.parseLong("0");
                Long totalGeneral = Long.parseLong("0");
                if (rpm.get("formaPago").toString().compareToIgnoreCase("CREDITO") == 0) {

                    totalGeneral = Math.round(Double.parseDouble(rpm.get("neto").toString())) + Math.round(Double.parseDouble(rpm.get("entrega").toString()));
                    DocumentoCobrar docPagar = new DocumentoCobrar();
                    docPagar.setVenta(new Venta(Long.parseLong(rpm.get("id").toString())));

                    List<DocumentoCobrar> aPagar = documentoCobrarManager.list(docPagar, "nroCuota", "asc");
                    Double importeCuota = 0.0;
                    boolean primeraCuota = true;

                    String cuotaPendiente = "";

                    for (DocumentoCobrar pagar : aPagar) {
                        if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getSaldo());
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            importeCuota = pagar.getMonto();
                            if (primeraCuota) {
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        } else if (pagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                            totalEgreso = totalEgreso + Math.round(pagar.getMonto());
                            if (primeraCuota) {
                                primeraCuota = false;
                                cuotaPendiente = pagar.getNroCuota();
                            }
                        }
                    }
                    rpm.put("saldo", totalEgreso);
                    rpm.put("importe", Math.round(importeCuota));
                    rpm.put("cuota", cuotaPendiente);
                    rpm.put("totalGeneral", totalGeneral);
                } else {
                    totalGeneral = Math.round(Double.parseDouble(rpm.get("neto").toString()));

                    rpm.put("totalGeneral", totalGeneral);
                }
                rpm.put("neto", Math.round(Double.parseDouble(rpm.get("neto").toString())));
                rpm.put("proveedor", rpm.get("cliente.nombre").toString());
                rpm.put("fechaCuota", rpm.get("fechaVenta"));
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

    @ResponseBody
    @RequestMapping(value = "/exportar/factura/{tipo}/{id}", method = RequestMethod.GET)
    public void exportarFactura(@PathVariable("tipo") String tipo, @PathVariable("id") Long id,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        try {
            inicializarVentaManager();
            inicializarDetalleVentaManager();
            inicializarEmpresaManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/proyecto-factura.jasper"));

            Map<String, Object> parametros = new HashMap<String, Object>();

            Map<String, Object> cliente = new HashMap<String, Object>();

            String dirLogo = servletContext
                    .getRealPath("/WEB-INF/resources/img/empresa.jpg")
                    + "/";
            parametros.put("DIR_LOGO", dirLogo);
            parametros.put("titulo", "Factura Venta");

            Empresa ejEmpresa = empresaManager.get(userDetail.getIdEmpresa());

            parametros.put("ruc", ejEmpresa.getRuc());
            parametros.put("empresa", ejEmpresa.getNombre());

            Venta ejemplo = new Venta();
            ejemplo.setId(id);
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            ejemplo = ventaManager.get(ejemplo);

            parametros.put("nombre", ejemplo.getCliente().getNombre());
            parametros.put("documento", ejemplo.getCliente().getDocumento());
            parametros.put("direccion", ejemplo.getCliente().getDireccion());
            parametros.put("telefono", ejemplo.getCliente().getTelefono());
            parametros.put("formaPago", ejemplo.getFormaPago());

            parametros.put("factura", ejemplo.getNroFactura());
            parametros.put("timbrado", ejemplo.getTimbrado());
            parametros.put("valido", "2017/08/15");

            List<Object> columnas = new ArrayList<Object>();
            columnas.add("Razon Social");
            columnas.add("Documento/Ruc");
            columnas.add("Dirección");
            columnas.add("Telefono");
            columnas.add("Forma Pago");
            columnas.add("Cantidad");
            columnas.add("Cod. Vehiculo");
            columnas.add("Descripcion");
            columnas.add("Precio");
            columnas.add("IVA 10 %");
            parametros.put("columnas", columnas);

            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }
            DetalleVenta ejDetalle = new DetalleVenta();
            ejDetalle.setVenta(ejemplo);

            List<Map<String, Object>> listMapCategorias = detalleVentaManager
                    .listAtributos(ejDetalle, "vehiculo.tipo.nombre,vehiculo.codigo,vehiculo.marca.nombre,vehiculo.modelo.nombre,vehiculo.anho,neto".split(","),
                            false);
            Long total = Long.parseLong("0");

            for (Map<String, Object> rpm : listMapCategorias) {
                String descipcion = rpm.get("vehiculo.tipo.nombre").toString()
                        + " " + rpm.get("vehiculo.marca.nombre").toString()
                        + " " + rpm.get("vehiculo.modelo.nombre").toString()
                        + " " + rpm.get("vehiculo.anho").toString();

                rpm.put("CANTIDAD", 1);
                rpm.put("CODPRODUCTO", rpm.get("vehiculo.codigo").toString());
                rpm.put("DESCRIPCION", descipcion);
                rpm.put("PRECIO", Math.round(Double.parseDouble(rpm.get("neto").toString())));

                total = total + Math.round(Double.parseDouble(rpm.get("neto").toString()));

                rpm.put("IVA", Math.round(Double.parseDouble(rpm.get("neto").toString())));
            }

            parametros.put("subTotal", total + "");
            parametros.put("totalPagar", total + "");

            Double iva = Double.parseDouble(total + "") / 11;

            parametros.put("totalIva", Math.round(iva) + "");
            parametros.put("iva10", Math.round(iva) + "");

            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(listMapCategorias);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-depositos." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exportar/pagare/{tipo}/{id}", method = RequestMethod.GET)
    public void exportarPagare(@PathVariable("tipo") String tipo, @PathVariable("id") Long id,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        try {
            inicializarVentaManager();
            inicializarDetalleVentaManager();
            inicializarEmpresaManager();
            inicializarDocumentoCobrarManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/proyecto-pagare.jasper"));

            Map<String, Object> parametros = new HashMap<String, Object>();

            Map<String, Object> cliente = new HashMap<String, Object>();

            parametros.put("titulo", "Pagare");

            Empresa ejEmpresa = empresaManager.get(userDetail.getIdEmpresa());

            parametros.put("ruc", ejEmpresa.getRuc());
            parametros.put("nombreEmpresa", ejEmpresa.getNombre());

            Venta ejemplo = new Venta();
            ejemplo.setId(id);
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            ejemplo = ventaManager.get(ejemplo);

            parametros.put("numero", ejemplo.getNroPagare());
            parametros.put("cliente", ejemplo.getCliente().getNombre());
            parametros.put("direccion", ejemplo.getCliente().getDireccion());

            String emision = fecha_español.format(ejemplo.getFechaVenta());

            parametros.put("emision", emision);

            if (ejemplo.getEntrega() != null && ejemplo.getEntrega().compareToIgnoreCase("") != 0) {
                Long total = Math.round(ejemplo.getNeto()) + Math.round(Double.parseDouble(ejemplo.getEntrega()));
                parametros.put("montoTotal", total + "");
            } else {
                parametros.put("montoTotal", Math.round(ejemplo.getNeto()) + "");
            }

            if (ejemplo.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {
                DocumentoCobrar ejCobrar = new DocumentoCobrar();
                ejCobrar.setVenta(ejemplo);

                List<DocumentoCobrar> ejCobrarList = documentoCobrarManager.list(ejCobrar, "nroCuota", "desc");

                for (DocumentoCobrar cobrar : ejCobrarList) {
                    parametros.put("vencimiento", fecha.format(cobrar.getFecha()));
                    parametros.put("fechaVencimiento", fecha_español.format(cobrar.getFecha()));
                    break;
                }
            } else {
                parametros.put("vencimiento", fecha.format(ejemplo.getFechaVenta()));
                parametros.put("fechaVencimiento", fecha_español.format(ejemplo.getFechaVenta()));
            }

            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }
            DetalleVenta ejDetalle = new DetalleVenta();
            ejDetalle.setVenta(ejemplo);

            List<Map<String, Object>> listMapCategorias = detalleVentaManager
                    .listAtributos(ejDetalle, "vehiculo.tipo.nombre,vehiculo.codigo,vehiculo.marca.nombre,vehiculo.modelo.nombre,vehiculo.anho,neto".split(","),
                            false);
            Long total = Long.parseLong("0");
            String descipcion = "Venta Vehiculos ,";

            for (Map<String, Object> rpm : listMapCategorias) {

                descipcion = descipcion + "- " + rpm.get("vehiculo.marca.nombre").toString()
                        + " " + rpm.get("vehiculo.modelo.nombre").toString()
                        + " " + rpm.get("vehiculo.anho").toString();

            }

            parametros.put("concepto", descipcion);

            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(listMapCategorias);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-pagare." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exportar/compras/{tipo}", method = RequestMethod.GET)
    public void exportarReporteCompra(@PathVariable("tipo") String tipo,
            @ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filters,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos,
            @ModelAttribute("idProveedor") String idProveedor,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DTORetorno<List<DetalleCanvas>> nivelActual = new DTORetorno<List<DetalleCanvas>>();
        try {
            inicializarProveedorManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/reporte-comprasPendientes.jasper"));

            List<Map<String, String>> filtros = new ArrayList<Map<String, String>>();

            cargarFiltros(filtros, "Desde", fechaInicio);
            cargarFiltros(filtros, "Hasta", fechaFin);

            if (idProveedor == null || idProveedor.isEmpty()) {
                idProveedor = null;
            } else {
                Map<String, Object> clienteMap = proveedorManager.getAtributos(new Proveedor(Long.parseLong(idProveedor)), "nombre".split(","));
                cargarFiltros(filtros, "Proveedor", (String) clienteMap.get("nombre"));
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            parametros.put("titulo", "Reporte Compras");
            parametros.put("usuario", userDetail.getNombre());
            parametros.put("filtros1", filtros.subList(0, (filtros.size() / 2) + 1));
            parametros.put("filtros2", filtros.subList((filtros.size() / 2) + 1, filtros.size()));

            if (estado != null && estado.compareToIgnoreCase("PENDIENTE") == 0) {
                nivelActual = reporteComprasPendientes(fechaInicio, fechaFin, idProveedor);
                parametros.put("graficos_titulos", "Compras " + estado);
            } else {
                nivelActual = reporteComprasRealizadas(fechaInicio, fechaFin, idProveedor);
                parametros.put("graficos_titulos", "Compras REALIZADAS");
            }
            List<Map<String, Object>> graf = new ArrayList<Map<String, Object>>();

            for (DetalleCanvas rpm : nivelActual.getData()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("label", rpm.getLabel());
                map.put("value", rpm.getY());
                graf.add(map);
            }

            parametros.put("grafico2", graf);

            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }

            List<Object> columnas = new ArrayList<Object>();
            columnas.add("Nro. Factura");
            columnas.add("Forma Pago");
            columnas.add("Cant. Cuotas");
            columnas.add("Proveedor");
            columnas.add("Fecha Compra");
            columnas.add("Cuota Pendiente");
            columnas.add("Importe Cuota");
            columnas.add("Saldo");
            columnas.add("Neto");
            columnas.add("Total");
            parametros.put("columnas", columnas);

            DTORetorno<List<Map<String, Object>>> grilla = listarComprasPendientes(filtrar, filters,
                    fechaInicio, fechaFin, pagina, cantidad, ordenarPor, estado, sentidoOrdenamiento, true);

            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(grilla.getRetorno());

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-pagare." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exportar/ventas/{tipo}", method = RequestMethod.GET)
    public void exportarReporteVenta(@PathVariable("tipo") String tipo,
            @ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filters,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos,
            @ModelAttribute("idCliente") String idCliente,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DTORetorno<List<DetalleCanvas>> nivelActual = new DTORetorno<List<DetalleCanvas>>();
        try {
            inicializarClienteManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/reporte-comprasPendientes.jasper"));

            List<Map<String, String>> filtros = new ArrayList<Map<String, String>>();

            cargarFiltros(filtros, "Desde", fechaInicio);
            cargarFiltros(filtros, "Hasta", fechaFin);

            if (idCliente == null || idCliente.isEmpty()) {
                idCliente = null;
            } else {
                Map<String, Object> clienteMap = clienteManager.getAtributos(new Cliente(Long.parseLong(idCliente)), "nombre".split(","));
                cargarFiltros(filtros, "Cliente", (String) clienteMap.get("nombre"));
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            parametros.put("titulo", "Reporte Ventas");
            parametros.put("usuario", userDetail.getNombre());
            parametros.put("filtros1", filtros.subList(0, (filtros.size() / 2) + 1));
            parametros.put("filtros2", filtros.subList((filtros.size() / 2) + 1, filtros.size()));

            if (estado != null && estado.compareToIgnoreCase("PENDIENTE") == 0) {
                nivelActual = reporteVentasPendientes(fechaInicio, fechaFin, idCliente);
                parametros.put("graficos_titulos", "Ventas " + estado);
            } else {
                nivelActual = reporteVentasRealizadas(fechaInicio, fechaFin, idCliente);
                parametros.put("graficos_titulos", "Ventas REALIZADAS");
            }
            List<Map<String, Object>> graf = new ArrayList<Map<String, Object>>();

            for (DetalleCanvas rpm : nivelActual.getData()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("label", rpm.getLabel());
                map.put("value", rpm.getY());
                graf.add(map);
            }

            parametros.put("grafico2", graf);

            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }

            List<Object> columnas = new ArrayList<Object>();
            columnas.add("Nro. Factura");
            columnas.add("Forma Pago");
            columnas.add("Cant. Cuotas");
            columnas.add("Cliente");
            columnas.add("Fecha Venta");
            columnas.add("Cuota Pendiente");
            columnas.add("Importe Cuota");
            columnas.add("Saldo");
            columnas.add("Neto");
            columnas.add("Total");
            parametros.put("columnas", columnas);

            DTORetorno<List<Map<String, Object>>> grilla = listarVentasPendientes(filtrar, filters,
                    fechaInicio, fechaFin, pagina, cantidad, ordenarPor, estado, sentidoOrdenamiento, true);

            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(grilla.getRetorno());

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-pagare." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exportar/movimiento/compras/{tipo}", method = RequestMethod.GET)
    public void exportarMovimientoCompra(@PathVariable("tipo") String tipo,
            @ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filters,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos,
            @ModelAttribute("idProveedor") String idProveedor,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DTORetorno<List<DetalleCanvas>> nivelActual = new DTORetorno<List<DetalleCanvas>>();
        try {
            inicializarProveedorManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/reporte-movimiento.jasper"));

            List<Map<String, String>> filtros = new ArrayList<Map<String, String>>();

            cargarFiltros(filtros, "Desde", fechaInicio);
            cargarFiltros(filtros, "Hasta", fechaFin);

            if (idProveedor == null || idProveedor.isEmpty()) {
                idProveedor = null;
            } else {
                Map<String, Object> clienteMap = proveedorManager.getAtributos(new Proveedor(Long.parseLong(idProveedor)), "nombre".split(","));
                cargarFiltros(filtros, "Cliente", (String) clienteMap.get("nombre"));
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            parametros.put("titulo", "Reporte Transacciones de Compra");
            
            parametros.put("usuario", userDetail.getNombre());
            parametros.put("filtros1", filtros.subList(0, (filtros.size() / 2) + 1));
            parametros.put("filtros2", filtros.subList((filtros.size() / 2) + 1, filtros.size()));

            nivelActual = reporteCompras(fechaInicio, fechaFin, idProveedor);
            parametros.put("graficos_titulos", "Reporte Transacciones");

            List<Map<String, Object>> graf = new ArrayList<Map<String, Object>>();

            for (DetalleCanvas rpm : nivelActual.getData()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("label", rpm.getLabel());
                map.put("value", rpm.getY());
                graf.add(map);
            }

            parametros.put("grafico2", graf);

            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }

            List<Object> columnas = new ArrayList<Object>();
            columnas.add("Nro. Factura");
            columnas.add("Forma Pago");
            columnas.add("Cant. Cuotas");
            columnas.add("Proveedor");
            columnas.add("Fecha Venta");
            columnas.add("Cuota");
            columnas.add("Importe");
            columnas.add("Saldo");
            columnas.add("Vuelto");
            columnas.add("Interes");
            columnas.add("neto");
            parametros.put("columnas", columnas);

            DTORetorno<List<Map<String, Object>>> grilla = listarMovimientos(filtrar, filters,
                    fechaInicio, fechaFin, pagina, cantidad, ordenarPor, estado, sentidoOrdenamiento, true);

            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(grilla.getRetorno());

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-pagare." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/exportar/recibo/{tipo}/{id}", method = RequestMethod.GET)
    public void exportarRecibo(@PathVariable("tipo") String tipo, @PathVariable("id") Long id,
            HttpServletRequest request, HttpServletResponse response) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        try {
            inicializarVentaManager();
            inicializarMovimientoManager();
            inicializarDetalleVentaManager();
            inicializarEmpresaManager();
            inicializarDocumentoCobrarManager();

            JasperReport reporte = (JasperReport) JRLoader
                    .loadObjectFromFile(request
                            .getSession()
                            .getServletContext()
                            .getRealPath(
                                    "WEB-INF/resources/reports/proyecto-recibo.jasper"));

            Map<String, Object> parametros = new HashMap<String, Object>();

            Map<String, Object> cliente = new HashMap<String, Object>();

            parametros.put("titulo", "Recibo");

            Empresa ejEmpresa = empresaManager.get(userDetail.getIdEmpresa());

            parametros.put("nombreEmpresa", ejEmpresa.getNombre());

            Movimiento ejemplo = new Movimiento();
            ejemplo.setId(id);
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            ejemplo = movimientoManager.get(ejemplo);

            parametros.put("numero", ejemplo.getId() + "");
            parametros.put("cliente", ejemplo.getCliente().getNombre());

            String emision = fecha_español.format(ejemplo.getFechaIngreso());

            parametros.put("vencimiento", emision);
            
            parametros.put("montoTotal", Math.round(ejemplo.getNeto()) + "");
            parametros.put("importe", Math.round(ejemplo.getImporte()) + "");
            parametros.put("interes", Math.round(ejemplo.getInteres()) + "");
            parametros.put("vuelto", Math.round(ejemplo.getVuelto()) + "");
            
            String concepto = "";
            if(ejemplo.getVenta().getFormaPago().compareToIgnoreCase("CREDITO") == 0){
                concepto = "Cuo/" + ejemplo.getCuota() +", Importe Cuota " + Math.round(ejemplo.getNeto()) +", Venta Credito " + ejemplo.getVenta().getNroFactura();
            }else{
                concepto = "Importe " + Math.round(ejemplo.getNeto()) +", Venta Contado" + ejemplo.getVenta().getNroFactura();
            }
           
            parametros.put("concepto", concepto);
            
            JRExporter exporter;
            if (tipo.equals("pdf")) {
                response.setContentType("application/pdf");
                exporter = new JRPdfExporter();
            } else if (tipo.equals("xls")) {
                parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
                response.setContentType("application/vnd.ms-excel");
                exporter = new JRXlsExporter();
            } else {
                return;
            }
            
            DetalleVenta ejDetalle = new DetalleVenta();
            ejDetalle.setVenta(ejemplo.getVenta());

            List<Map<String, Object>> listMapCategorias = detalleVentaManager
                    .listAtributos(ejDetalle, "vehiculo.tipo.nombre,vehiculo.codigo,vehiculo.marca.nombre,vehiculo.modelo.nombre,vehiculo.anho,neto".split(","),
                            false);
            
            JasperDatasource datasource = new JasperDatasource();
            datasource.addAll(listMapCategorias);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte,
                    parametros, datasource);

            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + "export-recibo." + tipo + "\"");
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,
                    "Cp1252");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            ServletOutputStream output = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarFiltros(List<Map<String, String>> filtros, String label, String value) {
        Map<String, String> filtro = new HashMap<String, String>();
        filtro.put("label", label);
        filtro.put("value", value);
        filtros.add(filtro);
    }

}
