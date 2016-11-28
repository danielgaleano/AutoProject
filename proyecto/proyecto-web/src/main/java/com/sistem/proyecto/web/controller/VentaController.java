/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.DTORetorno;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import java.util.ArrayList;
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
    
    String atributos = "id,codigo,fechaEntrega,observacion,confirmado,total,cliente.id,"
            + "cliente.nombre,activo,usuario.nombre,cantidadAprobados,cantidadTotal";
    String atributosAcobrar = "id,nroCuota,monto,saldo,montoInteres,fecha,estado";

    String atributosVentas = "id,estadoVenta,nroFactura,fechaCuota,fechaVenta,tipoVenta,formaPago,descripcion,porcentajeInteresCredito,montoInteres,"
            + "tipoMoraInteres,moraInteres,cantidadCuotas,montoCuotas,cliente.nombre,activo,"
            + "entrega,saldo,tipoDescuento,descuento,monto,montoDescuento,neto,"
            + "cliente.id,cliente.ruc,cliente.nombre,cliente.direccion,cliente.telefono";

    
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

        Venta ejemplo = new Venta();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        List<Map<String, Object>> listVentasMap = null;
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
                ordenarPor = "codigo";
            }
            List<Long> ventas = new ArrayList<Long>();
            Venta ejOrden = new Venta();
            ejOrden.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            //ejOrden.setEstadoVenta(Venta.ORDEN_COMPRA);

            listVentasMap = ventaManager.listAtributos(ejOrden, "id".split(","), true, null, null,
                    "id".split(","), "ASD".split(","), true, true, "id", null,
                    null, null, null, null, null, null, null, null, true);

            for (Map<String, Object> rpm : listVentasMap) {
                ventas.add(Long.parseLong(rpm.get("id").toString()));
            }

            pagina = pagina != null ? pagina : 1;
            Integer total = 0;

            if (!todos) {
                total = ventaManager.list(ejemplo, true).size() - ventas.size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = ventaManager.listAtributos(ejemplo, atributosVentas.split(","), todos, inicio, cantidad,
                    ordenarPor.split(","), sentidoOrdenamiento.split(","), true, true, camposFiltros, valorFiltro,
                    "id", ventas, "OP_NOT_IN", null, null, null, null, null, true);

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
