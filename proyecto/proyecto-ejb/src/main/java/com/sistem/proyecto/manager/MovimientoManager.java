/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Movimiento;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Local;

/**
 *
 * @author Miguel
 */
@Local
public interface MovimientoManager extends GenericDao<Movimiento, Long> {
    
    public MensajeDTO obtenerDatosPago(Long idCompra) throws Exception;
    
    public MensajeDTO realizarCompra(Long idCompra, String Monto, Long idDocPago, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception;
    
}

