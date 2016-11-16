/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.CompraManager;
import com.sistem.proyecto.manager.DetalleCompraManager;
import com.sistem.proyecto.manager.PedidoManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class DetalleCompraManagerImpl extends GenericDaoImpl<DetalleCompra, Long>
        implements DetalleCompraManager {

 
    @Override
    protected Class<DetalleCompra> getEntityBeanType() {
        return DetalleCompra.class;
    }

    @Override
    public MensajeDTO guardarDetalle(Vehiculo vehiculo, Moneda moneda, DetalleCompra detallePedido, boolean tienePedido, Compra ejPedido, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO aprobar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
