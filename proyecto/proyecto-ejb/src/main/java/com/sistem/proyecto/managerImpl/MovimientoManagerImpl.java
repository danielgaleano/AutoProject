/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Movimiento;
import com.sistem.proyecto.manager.MovimientoManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class MovimientoManagerImpl extends GenericDaoImpl<Movimiento, Long>
		implements MovimientoManager{
        @Override
	protected Class<Movimiento> getEntityBeanType() {
		return Movimiento.class;
	}

    @Override
    public MensajeDTO obtenerDatosPago(Long idCompra) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO realizarCompra(Long idCompra, String Monto, Long idDocPago, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
        
}
