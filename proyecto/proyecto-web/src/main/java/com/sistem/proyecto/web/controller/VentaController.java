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
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.manager.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
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
@RequestMapping(value = "/ventas")
public class VentaController extends BaseController {
    
    String atributos = "id,vehiculo.chasis,vehiculo.precioVenta,vehiculo.precioCosto,neto,vehiculo.codigo,vehiculo.activo,vehiculo.marca.id,vehiculo.precioCosto,vehiculo.precioVenta,vehiculo.marca.nombre,vehiculo.modelo.id,vehiculo.modelo.nombre,empresa.id,empresa.nombre,"
            + "vehiculo.tipo.id,vehiculo.tipo.nombre,vehiculo.transmision,vehiculo.color,vehiculo.estado,vehiculo.anho,vehiculo.caracteristica,vehiculo.proveedor.id,vehiculo.proveedor.nombre";
    
    String atributosAcobrar = "id,nroCuota,monto,saldo,montoInteres,fecha,estado";
    
    String atributosVehiculo = "id,codigo,estado,activo,marca.id,precioCosto,precioVenta,marca.nombre,modelo.id,modelo.nombre,empresa.id,empresa.nombre,"
            + "tipo.id,tipo.nombre,transmision,color,anho,caracteristica,proveedor.id,proveedor.nombre,chasis,kilometraje";

    String atributosVentas = "id,estadoVenta,nroFactura,fechaCuota,fechaVenta,tipoVenta,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,cliente.nombre,activo,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,"
            + "cliente.id,cliente.documento,cliente.nombre,cliente.direccion,cliente.telefono,diasGracia";
    
    String atributosVehiculoVenta = "venta.id,venta.nroFactura,venta.fechaVenta,venta.tipoVenta,venta.formaPago,venta.porcentajeInteresCredito,"
            + "venta.montoInteres,venta.tipoMoraInteres,venta.moraInteres,venta.cantidadCuotas,venta.montoCuotas,"
            + "venta.montoTotalCuotas,venta.fechaCuota,venta.entrega,venta.saldo,venta.tipoDescuento,"
            + "venta.descuento,venta.monto,venta.entrega,venta.montoDescuento,venta.neto,"
            + "venta.cliente.id,venta.cliente.nombre,venta.cliente.documento,venta.moraInteres,venta.diasGracia,venta.estadoCobro";
    
     String atributosPagosRecibidos = "id,proveedor.nombre,proveedor.ruc,proveedor.email,proveedor.telefono,proveedor.telefonoMovil,"
            + "cliente.nombre,cliente.documento,cliente.email,cliente.telefono,cliente.telefonoMovil,"
            + "fechaIngreso,tipoTransaccion,importe,neto,saldo,vuelto,fechaVencimiento,venta.nroFactura,"
            + "cuota,interes,compra.estadoCompra,compra.nroFactura,compra.formaPago,compra.cantidadCuotas,compra.fechaCuota,compra.fechaCompra";


    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listaVentas(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ventasListar");
        return retorno;

    }

    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("ventaForm");
        model.addAttribute("action", "CREAR");
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
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno ventaForm(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarVentaManager();
            Venta ejVenta = new Venta();
            ejVenta.setId(id);

            Map<String, Object> ejVentaMap = ventaManager.getAtributos(ejVenta, atributosVentas.split(","));

            retorno.setData(ejVentaMap);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la venta");

        } catch (Exception ex) {
            logger.error("Error al obtener la venta", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }

        return retorno;
    }
    
    
        @RequestMapping(value = "/obtener/{id}", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno obtenerVentaVehiculo(@PathVariable("id") Long id) {
        DTORetorno<Map<String, Object>> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarDetalleVentaManager();
            inicializarDocumentoCobrarManager();
            
            DetalleVenta ejVenta = new DetalleVenta();
            ejVenta.setVehiculo(new Vehiculo (id));
            ejVenta.setDevuelto(false);
            
            Map<String, Object> ejVentaMap = detalleVentaManager.getAtributos(ejVenta, atributosVehiculoVenta.split(","));
        
            if(ejVentaMap.get("venta.estadoCobro").toString().compareToIgnoreCase("CANCELADO") != 0){
                if(ejVentaMap.get("venta.formaPago").toString().compareToIgnoreCase("CREDITO") == 0){
                    
                    DocumentoCobrar aPagar = new DocumentoCobrar();
                    aPagar.setVenta(new Venta(Long.parseLong(ejVentaMap.get("venta.id").toString())));
                    
                    List<DocumentoCobrar> aPagarList = documentoCobrarManager.list(aPagar);
                    
                    Long deuda = Long.parseLong("0");
                    for(DocumentoCobrar rpm : aPagarList){
                        if(rpm.getEstado().compareToIgnoreCase(DocumentoCobrar.PENDIENTE) == 0){
                            deuda = deuda + Math.round(rpm.getMonto());
                        }else if(rpm.getEstado().compareToIgnoreCase(DocumentoCobrar.PARCIAL) == 0){
                            deuda = deuda + Math.round(rpm.getSaldo());
                        }
                    }
                    ejVentaMap.put("deudaPendiente", deuda);
                }else{
                    if(ejVentaMap.get("venta.estadoCobro").toString().compareToIgnoreCase(DocumentoCobrar.PARCIAL) == 0){
                       ejVentaMap.put("deudaPendiente", ejVentaMap.get("venta.saldo")); 
                    }else{
                        ejVentaMap.put("deudaPendiente", ejVentaMap.get("venta.neto"));
                    }
                }
            }
            retorno.setData(ejVentaMap);
            retorno.setError(false);
            retorno.setMensaje("Se obtuvo exitosamente la venta");

        } catch (Exception ex) {
            logger.error("Error al obtener la venta", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
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
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Venta ejemplo = new Venta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listMaPagados = null;
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
                    //ejemplo = generarEjemplo(filtro, ejemplo);
                }

            }
            // ejemplo.setActivo("S");
            if (ordenarPor == null || ordenarPor != null && ordenarPor.compareToIgnoreCase(" ") == 0) {
                ordenarPor = "nroFactura";
            }
            
            pagina = pagina != null ? pagina : 1;
            Integer total = 0;
            
            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;
                       

            if (!todos) {
                total = ventaManager.list(ejemplo, true).size();
            }           

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), todos, inicio, cantidad,
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
        }else  if (idDetalle != null && idDetalle.compareToIgnoreCase("") != 0) {
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
            if(ordenarPor == null || ordenarPor != null && ordenarPor.compareToIgnoreCase(" ") == 0){
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
    
    @RequestMapping(value = "/vehiculos/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarVehiculoVenta(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("action") String action,
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ordenarPor = "marca.nombre";
        
        Vehiculo ejVehiculo = new Vehiculo();
        ejVehiculo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejVehiculo.setEstado(Vehiculo.STOCK);
        
        List<Map<String, Object>> listMapGrupos = null;
        
        List<Map<String, Object>> listMapVenta = null;
        try {

            inicializarVehiculoManager();
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

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            Integer totalValor = 0;
            
            if(idVenta != null && idVenta.compareToIgnoreCase("") != 0){
                
                DetalleVenta ejDetalle = new DetalleVenta();
                ejDetalle.setVenta(new Venta(Long.parseLong(idVenta)));
                List<Map<String, Object>> listDetalle = detalleVentaManager.listAtributos(ejDetalle, "vehiculo.id".split(","));
                List<Long> idVehiculos =new ArrayList<>();
                
                for(Map<String, Object> rpm : listDetalle){
                    idVehiculos.add(Long.parseLong(rpm.get("vehiculo.id").toString()));
                }
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                
                listMapVenta = vehiculoManager.listAtributos(vehiculo, atributosVehiculo.split(","), true , null, null,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    "id", idVehiculos, "OP_IN", null, null, null, null, null, true);
                
                totalValor = listMapVenta.size();
            }
            if (!todos) {
                total = vehiculoManager.list(ejVehiculo, true).size() + totalValor;
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = vehiculoManager.listAtributos(ejVehiculo, atributosVehiculo.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    null, null, null, null, null, null, null, null, true);
           
            if(listMapVenta != null){
               listMapGrupos.addAll(listMapVenta);
           }
 
            if (todos) {
                total = listMapGrupos.size();
            }
            Integer totalPaginas = total / cantidad;

            retorno.setTotal(totalPaginas + 1);
            
            if(action != null && action.compareToIgnoreCase("VISUALIZAR") == 0){
                retorno.setRetorno(listMapVenta);
            }else{
                retorno.setRetorno(listMapGrupos);
            }
            
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

            if (ventaRecibido.getCliente() == null ||
                        ventaRecibido.getCliente().getId() == null
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

            mensaje = ventaManager.guardarVenta(ventaRecibido.getItemsVenta(), ventaRecibido
                    , ventaRecibido.getNroFactura(), ventaRecibido.getFormaPago(), "GENERAL", userDetail.getIdEmpresa(), userDetail.getId());


        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el usuario");
            System.out.println("Error" + e);
        }

        return mensaje;
    }
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("Venta") Venta ventaRecibido) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO mensaje = new MensajeDTO();
        Venta ejVenta = new Venta();
        try {
            inicializarVentaManager();
            inicializarDetalleVentaManager();
            inicializarVehiculoManager();
            
            if (ventaRecibido.getNroFactura() == null || ventaRecibido.getNroFactura() != null
                    && ventaRecibido.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El numero de factura no puede estar vacia.");
                return mensaje;
            }

            if (ventaRecibido.getCliente() == null ||
                        ventaRecibido.getCliente().getId() == null
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
            
            DetalleVenta ejDetVentaElimi = new DetalleVenta();
            ejDetVentaElimi.setVenta(new Venta(ventaRecibido.getId()));

            List<DetalleVenta> ventaDetalle = detalleVentaManager.list(ejDetVentaElimi);

            for (DetalleVenta rpm : ventaDetalle) {
                
                rpm.getVehiculo().setEstado(Vehiculo.STOCK);
                vehiculoManager.update(rpm.getVehiculo());
                
                detalleVentaManager.delete(rpm.getId());
            }

            mensaje = ventaManager.editarVenta(ventaRecibido.getItemsVenta(), ventaRecibido
                    , ventaRecibido.getId(), ventaRecibido.getFormaPago(), "GENERAL", userDetail.getIdEmpresa(), userDetail.getId());


        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error a guardar el usuario");
            System.out.println("Error" + e);
        }

        return mensaje;
    }
    
    @RequestMapping(value = "/desactivar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO desactivar(@PathVariable("id") Long id) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";
        Venta ejVenta = new Venta();
        ejVenta.setId(id);
        ejVenta.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        
        try {

            inicializarVentaManager();
            inicializarDetalleVentaManager();
            inicializarVehiculoManager();
            
            ejVenta = ventaManager.get(ejVenta);

            if (ejVenta != null) {
                nombre = ejVenta.getNroFactura().toString();
            }

            if (ejVenta != null && ejVenta.getEstadoVenta()
                    .compareToIgnoreCase(Venta.VENTA_RECHAZADA) == 0) {
                retorno.setError(true);
                retorno.setMensaje("La venta " + nombre + " ya se encuentra rechazada.");
            }
            ejVenta.setEstadoVenta(Venta.VENTA_RECHAZADA);
            ejVenta.setEstadoCobro("RECHAZADA");
            ejVenta.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejVenta.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            ventaManager.update(ejVenta);
           
            DetalleVenta ejDetalle = new DetalleVenta();
            ejDetalle.setVenta(ejVenta);
            
            List<DetalleVenta> ventaDetalle = detalleVentaManager.list(ejDetalle);
            
            for(DetalleVenta rpm : ventaDetalle){
                
                rpm.getVehiculo().setEstado(Vehiculo.STOCK);
                
                vehiculoManager.update(rpm.getVehiculo());
            }
            retorno.setError(false);
            retorno.setMensaje("La venta " + nombre + " ya se  rechazo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de rechazar la venta.");
            logger.error("Error al tratar de rechazar la venta.", ex);
        }

        return retorno;

    }

    @RequestMapping(value = "/activar/{id}", method = RequestMethod.GET)
    public @ResponseBody
    MensajeDTO activar(@PathVariable("id") Long id) {

        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        String nombre = "";

        Venta ejVenta = new Venta();
        ejVenta.setId(id);
        ejVenta.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        try {

            inicializarVentaManager();

            ejVenta = ventaManager.get(ejVenta);

            if (ejVenta != null) {
                nombre = ejVenta.getNroFactura().toString();
            }

            if (ejVenta != null && ejVenta.getEstadoVenta()
                    .compareToIgnoreCase(Venta.VENTA_APROBADA) == 0) {
                retorno.setError(true);
                retorno.setMensaje("La venta " + nombre + " ya se encuentra aprobada.");
            }
            ejVenta.setEstadoVenta(Venta.VENTA_APROBADA);
            ejVenta.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejVenta.setFechaEliminacion(new Timestamp(System.currentTimeMillis()));

            ventaManager.update(ejVenta);

            retorno.setError(false);
            retorno.setMensaje("La venta " + nombre + " ya se  aprobo exitosamente.");

        } catch (Exception ex) {
            retorno.setError(true);
            retorno.setMensaje("Error al tratar de aprobar la venta.");
            logger.debug("Error al tratar de aprobar la venta", ex);
        }

        return retorno;

    }
    
    
    @RequestMapping(value = "/docs/{id}", method = RequestMethod.GET)
    public ModelAndView listaDocsCobrar(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("documentosCobrarListar");
        model.addAttribute("id", id);
        return retorno;

    }
    
    @RequestMapping(value = "/docsCobrar/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarDocumentosCobrar(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DocumentoCobrar ejemplo = new DocumentoCobrar();
        ejemplo.setVenta(new Venta(Long.parseLong(idVenta)));

        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarDocumentoCobrarManager();

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
                total = documentoCobrarManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = documentoCobrarManager.listAtributos(ejemplo, atributosAcobrar.split(","), todos, inicio, cantidad,
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
            retorno.setMensaje("Error al obtener lista de documentos a cobrar");
            logger.error("Error al listar", e);
        }

        return retorno;
    }
    
    @RequestMapping(value = "/cobros/{id}", method = RequestMethod.GET)
    public ModelAndView listaCobros(@PathVariable("id") Long id, Model model) {
        ModelAndView retorno = new ModelAndView();
        retorno.setViewName("cobrosContadoListar");
        model.addAttribute("id", id);
        return retorno;

    }
    
    
    @RequestMapping(value = "/cobrosrecibidos/listar", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno listarCobrosRecibidos(@ModelAttribute("_search") boolean filtrar,
            @ModelAttribute("filters") String filtros,
            @ModelAttribute("fechaInicio") String fechaInicio,
            @ModelAttribute("fechaFin") String fechaFin,
            @ModelAttribute("page") Integer pagina,
            @ModelAttribute("rows") Integer cantidad,
            @ModelAttribute("sidx") String ordenarPor,
            @ModelAttribute("estado") String estado,
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Movimiento ejemplo = new Movimiento();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
        ejemplo.setVenta(new Venta(Long.parseLong(idVenta)));

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
                total = movimientoManager.listAtributos(ejemplo, atributosPagosRecibidos.split(","), true, null, null,
                        ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                        null, null, null, null, null, null,
                        null, null, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = movimientoManager.listAtributos(ejemplo, atributosPagosRecibidos.split(","), todos, inicio, cantidad,
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
            retorno.setMensaje("Error al optener ventas");
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
            @ModelAttribute("idVenta") String idVenta,
            @ModelAttribute("sord") String sentidoOrdenamiento,
            @ModelAttribute("todos") boolean todos) {

        DTORetorno retorno = new DTORetorno();
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        DocumentoCobrar ejemplo = new DocumentoCobrar();
        ejemplo.setVenta(new Venta(Long.parseLong(idVenta)));

        List<Map<String, Object>> listMapGrupos = null;

        try {

            inicializarDocumentoCobrarManager();

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
                total = documentoCobrarManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = documentoCobrarManager.listAtributos(ejemplo, atributosAcobrar.split(","), todos, inicio, cantidad,
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
    
}
