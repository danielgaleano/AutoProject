/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.userDetail.UserDetail;
import static com.sistem.proyecto.web.controller.BaseController.logger;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Miguel
 */
@Controller
@RequestMapping(value = "/pedidos")
public class PedidoController extends BaseController {
    
    String atributos = "id,numeroPedido,codigo,fechaEntrega,observacion,confirmado,descuento,total,neto,proveedor.id,"
            + "proveedor.nombre";
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listarPermisos(Model model) {
            ModelAndView retorno = new ModelAndView();

            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try{
            inicializarPedidoManager();
            Pedido ejemplo = new Pedido();
            ejemplo.setEmpresa(new Empresa(userDetail.getIdEmpresa()));
            
            List<Map<String, Object>> listMapPedidos = pedidoManager.listAtributos(ejemplo, atributos.split(","), true);
 
            model.addAttribute("pedidos", listMapPedidos);
            
            retorno.setViewName("pedidosListar");
            
        }catch (Exception ex){
            logger.error("Error en listar pedidos", ex);
        }
        
        return retorno;
    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    public ModelAndView crear(Model model) {
        
        try {
            inicializarPedidoManager();
            model.addAttribute("tipo", "Crear");
            model.addAttribute("editar", false);

        } catch (Exception ex) {
            logger.debug("Error al crear pedidos", ex);
        }
        return new ModelAndView("pedido");

    }
    
}
