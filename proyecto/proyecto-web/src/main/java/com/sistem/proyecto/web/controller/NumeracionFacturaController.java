/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.google.gson.Gson;
import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.NumeracionFactura;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.userDetail.UserDetail;
import com.sistem.proyecto.utils.FilterDTO;
import com.sistem.proyecto.utils.ReglaDTO;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
@RequestMapping(value = "/facturas")
public class NumeracionFacturaController extends BaseController {

    String atributos = "id,nombre,empresa.id,empresa.nombre,valor";

    SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarNumeracionFacturas(Model model) {
        ModelAndView retorno = new ModelAndView();

        retorno.setViewName("numeracionFacturasListar");

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
        ordenarPor = "nombre";

        NumeracionFactura ejemplo = new NumeracionFactura();
        ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

        List<Map<String, Object>> listMapGrupos = null;
        try {

            inicializarNumeracionFacturaManager();

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
                total = numeracionFacturaManager.list(ejemplo, true).size();
            }

            Integer inicio = ((pagina - 1) < 0 ? 0 : pagina - 1) * cantidad;

            if (total < inicio) {
                inicio = total - total % cantidad;
                pagina = total / cantidad;
            }

            listMapGrupos = numeracionFacturaManager.listAtributos(ejemplo, atributos.split(","), todos, inicio, cantidad,
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

    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public @ResponseBody
    MensajeDTO editar(@ModelAttribute("NumeracionFactura") NumeracionFactura numeracionFacturaRecibido) {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        MensajeDTO retorno = new MensajeDTO();
        try {

            inicializarNumeracionFacturaManager();
            if (numeracionFacturaRecibido.getNombre() == null || numeracionFacturaRecibido.getNombre() != null
                    && numeracionFacturaRecibido.getNombre().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo nombre no puede estar vacio.");
                return retorno;
            }

            if (numeracionFacturaRecibido.getValor() == null || numeracionFacturaRecibido.getValor() != null
                    && numeracionFacturaRecibido.getValor().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("El campo valor no puede estar vacio.");
                return retorno;
            }

            NumeracionFactura numeracion = numeracionFacturaManager.get(numeracionFacturaRecibido.getId());
            numeracion.setValor(numeracionFacturaRecibido.getValor());

            numeracionFacturaManager.update(numeracion);

            retorno.setError(false);
            retorno.setMensaje("La numeracion de la factura se modifico exitosamente.");
            return retorno;

        } catch (Exception ex) {
            System.out.println("Error " + ex);
            retorno.setError(true);
            retorno.setMensaje("Error al modificar la numeracion de factura.");

        }
        return retorno;
    }

    @RequestMapping(value = "/optener", method = RequestMethod.GET)
    public @ResponseBody
    DTORetorno optenerFactura() {
        UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        DTORetorno<String> retorno = new DTORetorno<>();
        List<Map<String, Object>> listMapGrupos = null;
        try {
            inicializarNumeracionFacturaManager();
            inicializarVentaManager();

            NumeracionFactura ejNumeracion = new NumeracionFactura();
            ejNumeracion.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            List<NumeracionFactura> ejFactura = numeracionFacturaManager.list(ejNumeracion, false);

            Venta ejVenta = new Venta();
            ejVenta.setEmpresa(new Empresa(userDetail.getIdEmpresa()));

            List<Venta> nroFactura = ventaManager.list(ejVenta, "id", "desc");

            String timbrado = "";
            String facturaVenta = "";
            String valor = "";

            for (Venta fac : nroFactura) {

                timbrado = fac.getTimbrado();

                String factura = fac.getNroFactura();

                String[] arrayFac = factura.split("-");

                Long numeracion = Long.parseLong(arrayFac[2]) + 1;

                if (numeracion.toString().length() > 1) {
                    valor = "0000" + numeracion.toString();
                } else if (numeracion.toString().length() > 2) {
                    valor = "000" + numeracion.toString();
                } else if (numeracion.toString().length() > 3) {
                    valor = "00" + numeracion.toString();
                } else if (numeracion.toString().length() > 4) {
                    valor = "0" + numeracion.toString();
                } else if (numeracion.toString().length() > 5) {
                    valor = numeracion.toString();
                } else if (numeracion.toString().length() < 2) {
                    valor = "00000" + numeracion.toString();
                }

                facturaVenta = arrayFac[0] + "-" + arrayFac[1] + "-" + valor;

                break;
            }

            Long contador = Long.parseLong("0");
            String inicio = "";
            String medio = "";

            NumeracionFactura ejTimbrado = new NumeracionFactura();
            ejTimbrado.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            ejTimbrado.setNombre("TIMBRADO");

            ejTimbrado = numeracionFacturaManager.get(ejTimbrado);

            if (ejTimbrado != null && ejTimbrado.getValor().compareToIgnoreCase(timbrado) == 0) {
                
                ejTimbrado = new NumeracionFactura();
                ejTimbrado.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
                ejTimbrado.setNombre("FINAL");

                ejTimbrado = numeracionFacturaManager.get(ejTimbrado);
                
                ejTimbrado.setValor(valor);
                
                numeracionFacturaManager.update(ejTimbrado);
                
            } else {
                for (NumeracionFactura rpm : ejFactura) {

                    if (rpm.getNombre().compareToIgnoreCase("FINAL") == 0) {
                        contador = Long.parseLong(rpm.getValor()) + 1;

                    } else if (rpm.getNombre().compareToIgnoreCase("INICIO") == 0) {
                        inicio = rpm.getValor();
                    } else if (rpm.getNombre().compareToIgnoreCase("MEDIO") == 0) {
                        medio = rpm.getValor();
                    }

                    if (contador.toString().length() > 1) {
                        valor = "0000" + contador.toString();
                    } else if (contador.toString().length() > 2) {
                        valor = "000" + contador.toString();
                    } else if (contador.toString().length() > 3) {
                        valor = "00" + contador.toString();
                    } else if (contador.toString().length() > 4) {
                        valor = "0" + contador.toString();
                    } else if (contador.toString().length() > 5) {
                        valor = contador.toString();
                    } else if (contador.toString().length() < 2) {
                        valor = "00000" + contador.toString();
                    }

                    rpm.setValor(contador.toString());
                    numeracionFacturaManager.update(rpm);

                    break;
                }

                facturaVenta = inicio + "-" + medio + "-" + valor;
            }

            retorno.setData(facturaVenta);
            retorno.setError(false);
            retorno.setMensaje("El numero de factura se genero exitosamente.");

        } catch (Exception ex) {
            logger.error("Error al generar el numero de factura", ex);
            retorno.setError(true);
            retorno.setMensaje("Error al generar el numero de factura");
        }

        return retorno;
    }

}
