/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Local;

/**
 *
 * @author Miguel
 */
@Local
public interface DetalleCompraManager extends GenericDao<DetalleCompra, Long>{
    
    public MensajeDTO guardarDetalle(Vehiculo vehiculo, Moneda moneda,DetalleCompra detallePedido,
            boolean tienePedido, Compra ejPedido, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO aprobar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception;
    
}
