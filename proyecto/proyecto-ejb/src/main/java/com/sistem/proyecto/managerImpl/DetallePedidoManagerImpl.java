/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.DetallePedidoManager;
import com.sistem.proyecto.manager.MonedaManager;
import com.sistem.proyecto.manager.PedidoManager;
import com.sistem.proyecto.manager.VehiculoManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class DetallePedidoManagerImpl extends GenericDaoImpl<DetallePedido, Long>
        implements DetallePedidoManager {

    @EJB(mappedName = "java:app/proyecto-ejb/VehiculoManagerImpl")
    private VehiculoManager vehiculoManager;

    @EJB(mappedName = "java:app/proyecto-ejb/MonedaManagerImpl")
    private MonedaManager monedaManager;

    @EJB(mappedName = "java:app/proyecto-ejb/PedidoManagerImpl")
    private PedidoManager pedidoManager;

    @Override
    protected Class<DetallePedido> getEntityBeanType() {
        return DetallePedido.class;
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Override
    public MensajeDTO guardarDetalle(Vehiculo vehiculo, Moneda moneda, DetallePedido detallePedido,
            boolean tienePedido, Pedido ejPedido, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        try {
            if (tienePedido) {
                if (ejPedido == null || ejPedido.getId() != null
                        && ejPedido.getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("Debe Ingresar un pedido para el detalle.");
                    return mensaje;
                }
                if (vehiculo.getTipo() == null || vehiculo.getTipo().getId() != null
                        && vehiculo.getTipo().getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El tipo de vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getMarca() == null || vehiculo.getMarca().getId() != null
                        && vehiculo.getMarca().getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("La marca de vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getModelo() == null || vehiculo.getModelo().getId() != null
                        && vehiculo.getModelo().getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El modelo del vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getAnho() == null || vehiculo.getAnho() != null
                        && vehiculo.getAnho().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El año del vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getColor() == null || vehiculo.getColor() != null
                        && vehiculo.getColor().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getColor() == null || vehiculo.getColor() != null
                        && vehiculo.getColor().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                    return mensaje;
                }

                if (vehiculo.getTransmision() == null || vehiculo.getTransmision() != null
                        && vehiculo.getTransmision().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("La transmision del vehiculo no puede estar vacia.");
                    return mensaje;
                }

                if (moneda == null || moneda.getId() != null
                        && moneda.getId().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El campo moneda no puede estar vacia.");
                    return mensaje;
                }

                if (detallePedido.getPrecio() == null || detallePedido.getPrecio() != null
                        && detallePedido.getPrecio().toString().compareToIgnoreCase("") == 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El campo precio no puede estar vacio.");
                    return mensaje;
                }

                String codDetalle = randomString(5, "DET");

                vehiculo.setCodigo(ejPedido.getId() + "-" + codDetalle);
                vehiculo.setActivo("S");
                vehiculo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                vehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));

                vehiculoManager.save(vehiculo);

                DetallePedido ejDetalle = new DetallePedido();
                ejDetalle.setPedido(ejPedido);
                ejDetalle.setVehiculo(vehiculo);
                ejDetalle.setMoneda(detallePedido.getMoneda());
                ejDetalle.setPrecio(detallePedido.getPrecio());
                ejDetalle.setActivo("S");
                ejDetalle.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejDetalle.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejDetalle.setEmpresa(new Empresa(idEmpresa));

                Moneda ejMoneda = monedaManager.get(detallePedido.getMoneda());

                Double cambio = Double.parseDouble(ejMoneda.getValor());
                ejDetalle.setCambioDia(cambio);
                Moneda defMoneda = new Moneda();
                defMoneda.setPorDefecto(true);

                defMoneda = monedaManager.get(defMoneda);

                Double total = detallePedido.getPrecio() * 1;

                if (ejMoneda.getId() != defMoneda.getId()) {
                    Double cotizacion = total * Double.parseDouble(defMoneda.getValor());
                    ejDetalle.setNeto(cotizacion);
                } else {
                    ejDetalle.setNeto(total);
                }

                ejDetalle.setTotal(total);
                ejDetalle.setEstadoPedido(DetallePedido.PENDIENTE);

                this.save(ejDetalle);

                Pedido pedidoEj = new Pedido();
                pedidoEj.setEmpresa(new Empresa(idEmpresa));
                pedidoEj.setId(ejPedido.getId());

                ejDetalle = new DetallePedido();
                ejDetalle.setPedido(pedidoEj);

                Integer totalDetalle = this.list(ejDetalle, true).size();

                pedidoEj = pedidoManager.get(ejPedido);
                pedidoEj.setCantidadTotal(Long.parseLong(totalDetalle + ""));

                pedidoManager.update(pedidoEj);

                mensaje.setId(pedidoEj.getId());
                mensaje.setError(false);
                mensaje.setMensaje("El detalle del pedido se guardo exitosamente.");

            }
        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al guardar el detalle.");

        }
        return mensaje;
    }

    @Override
    public MensajeDTO aprobar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        try {
            if (idDetalle == null || idDetalle != null
                    && idDetalle.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar el detalle a aprobar.");
                return mensaje;
            }
            if (idPedido == null || idPedido != null
                    && idPedido.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar el pedido a aprobar.");
                return mensaje;
            }
            DetallePedido detallePedido = new DetallePedido();
            
            detallePedido = this.get(idDetalle);
            
            String nombre = detallePedido.getVehiculo().getCodigo();
            
            detallePedido.setEstadoPedido(DetallePedido.APROBADO);
            detallePedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            detallePedido.setIdUsuarioModificacion(idUsuario);

            this.update(detallePedido);
 
            detallePedido = new DetallePedido();
            detallePedido.setPedido(new Pedido(idPedido));
            detallePedido.setEstadoPedido(DetallePedido.APROBADO);
            
            List<DetallePedido> listDetalle = this.list(detallePedido, true);
            Double totalPedido = 0.0 ;
            
            for(DetallePedido rpm : listDetalle){
                totalPedido = totalPedido + rpm.getNeto();
            }
            
            Pedido pedido = pedidoManager.get(idPedido);
            pedido.setCantidadAprobados(Long.parseLong(pedido.getDetallePedidoCollection().size()+""));
            pedido.setTotal(totalPedido);
            pedido.setConfirmado(true);
            pedidoManager.update(pedido);
            
            mensaje.setError(false);
            mensaje.setMensaje("El pedido del vehiculo " + nombre + " se aprobo exitosamente.");
  

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al aprobar el pedido.");
        }
        return mensaje;
    }

    @Override
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        try {
            if (idDetalle == null || idDetalle != null
                    && idDetalle.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar el detalle a rechazar.");
                return mensaje;
            }
            if (idPedido == null || idPedido != null
                    && idPedido.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar el pedido a rechazar.");
                return mensaje;
            }
            DetallePedido detallePedido = new DetallePedido();
            
            detallePedido = this.get(idDetalle);
            
            String nombre = detallePedido.getVehiculo().getCodigo();
            
            detallePedido.setEstadoPedido(DetallePedido.RECHAZADO);
            detallePedido.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            detallePedido.setIdUsuarioModificacion(idUsuario);

            this.update(detallePedido);
 
            DetallePedido detallePedidoApro = new DetallePedido();
            detallePedidoApro.setPedido(new Pedido(idPedido));           
            detallePedidoApro.setEstadoPedido(DetallePedido.APROBADO);
            
            List<DetallePedido> listDetalle = this.list(detallePedidoApro, true);
            Double totalPedido = 0.0 ;
            
            for(DetallePedido rpm : listDetalle){
                totalPedido = totalPedido + rpm.getNeto();
            }
            
            Pedido pedido = pedidoManager.get(idPedido);
            pedido.setTotal(pedido.getTotal() - detallePedido.getNeto() );
            
            pedido.setCantidadAprobados(Long.parseLong(listDetalle.size()+""));
            pedido.setTotal(totalPedido);
            
            if(listDetalle.size() > 0){
                pedido.setConfirmado(true);
            }else{
                pedido.setConfirmado(false);
            }
            
            pedidoManager.update(pedido);
            
            mensaje.setError(false);
            mensaje.setMensaje("El pedido del vehiculo " + nombre + " se rechazo exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error al rechazar el pedido.");
        }
        return mensaje;
    }

    String randomString(int len, String variable) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            String rand = AB + variable;
            sb.append(rand.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
