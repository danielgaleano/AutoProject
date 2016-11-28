/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Local;

/**
 *
 * @author daniel
 */
@Local
public interface DetalleVentaManager extends GenericDao<DetalleVenta, Long>{
    
    public MensajeDTO guardarDetalle(Vehiculo vehiculo, Moneda moneda,DetalleVenta detalleVenta,
            boolean tienePedido, Venta ejVenta, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO aprobar(Long idDetalle, Long idVenta, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO rechazar(Long idDetalle, Long idVenta, Long idEmpresa, Long idUsuario) throws Exception;
}
