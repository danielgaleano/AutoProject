/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.DetalleVentaManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class DetalleVentaManagerImpl extends GenericDaoImpl<DetalleVenta, Long>
		implements DetalleVentaManager{
    @Override
    protected Class<DetalleVenta> getEntityBeanType() {
	return DetalleVenta.class;
    }
    
    @Override
    public MensajeDTO guardarDetalle(Vehiculo vehiculo, Moneda moneda, DetalleVenta detalleVenta, boolean tienePedido, Venta ejVenta, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO aprobar(Long idDetalle, Long idVenta, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO rechazar(Long idDetalle, Long idVenta, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
