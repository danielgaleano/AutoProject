/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.manager.CompraManager;
import com.sistem.proyecto.manager.DetalleCompraManager;
import com.sistem.proyecto.manager.DocumentoPagarManager;
import com.sistem.proyecto.manager.PedidoManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class CompraManagerImpl extends GenericDaoImpl<Compra, Long>
        implements CompraManager {

    @EJB(mappedName = "java:app/proyecto-ejb/PedidoManagerImpl")
    private PedidoManager pedidoManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DocumentoPagarManagerImpl")
    private DocumentoPagarManager documentoPagarManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DetalleCompraManagerImpl")
    private DetalleCompraManager detalleCompraManager;

    protected SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSSSSS");
    protected SimpleDateFormat sdfSimple = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected Class<Compra> getEntityBeanType() {
        return Compra.class;
    }

    @Override
    public MensajeDTO guardarCompra(Long idPedido, Compra compra, String formaPgo, String tipoDescuento, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        try {

            if (idPedido == null || idPedido != null
                    && idPedido.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar un pedido para relizar la compra.");
                return mensaje;
            }

            if (compra.getNroFactura() == null || compra.getNroFactura() != null
                    && compra.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (formaPgo == null || formaPgo != null
                    && formaPgo.compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar una forma de pago para relizar la compra.");
                return mensaje;
            }

            Pedido pedido = pedidoManager.get(idPedido);

            ejCompra.setActivo("S");
            ejCompra.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            ejCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
            ejCompra.setEmpresa(new Empresa(idEmpresa));
            ejCompra.setMonto(pedido.getTotal() + "");
            ejCompra.setNroFactura(compra.getNroFactura());
            ejCompra.setProveedor(new Proveedor(pedido.getProveedor().getId()));
            ejCompra.setFormaPago(compra.getFormaPago());

            if (compra.getFormaPago() != null
                    && compra.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                ejCompra.setCantidadCuotas(compra.getCantidadCuotas());
                ejCompra.setPorcentajeInteresCredito(compra.getPorcentajeInteresCredito());
                ejCompra.setMoraInteres(compra.getMoraInteres());

                Double interes = Double.parseDouble(compra.getPorcentajeInteresCredito());

                if (compra.getEntrega() != null
                        && compra.getEntrega().compareToIgnoreCase("") != 0) {

                    Double entrega = Double.parseDouble(compra.getEntrega());

                    Double montoInteres = ((pedido.getTotal() - entrega) * interes) / 100;

                    Double saldo = (pedido.getTotal() - entrega) + montoInteres;

                    Double montoCuota = saldo / compra.getCantidadCuotas();

                    ejCompra.setEntrega(entrega + "");
                    ejCompra.setMontoInteres(montoInteres + "");
                    ejCompra.setSaldo(saldo + "");
                    ejCompra.setNeto(saldo);
                    ejCompra.setMontoCuotas(montoCuota + "");

                    this.save(ejCompra);
                }

            } else {
                if (compra.getTipoDescuento() != null
                        && compra.getTipoDescuento().compareToIgnoreCase("GENERAL") == 0) {

                    Double interes = Double.parseDouble(compra.getDescuento());

                    Double montoInteres = (pedido.getTotal() * interes) / 100;

                    Double saldo = pedido.getTotal() - montoInteres;

                    ejCompra.setSaldo(saldo + "");
                    ejCompra.setDescuento(interes + "");
                    ejCompra.setMontoDescuento(montoInteres + "");
                    ejCompra.setNeto(saldo);

                    this.save(ejCompra);
                }

            }

            for (DetallePedido rpm : pedido.getDetallePedidoCollection()) {
                if (rpm.getEstadoPedido().compareToIgnoreCase(DetallePedido.APROBADO) == 0) {
                    DetalleCompra ejDetCompra = new DetalleCompra();
                    ejDetCompra.setActivo("S");
                    ejDetCompra.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                    ejDetCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                    ejDetCompra.setEmpresa(new Empresa(idEmpresa));
                    ejDetCompra.setVehiculo(rpm.getVehiculo());
                    ejDetCompra.setMoneda(rpm.getMoneda());
                    ejDetCompra.setCambioDia(rpm.getCambioDia());
                    ejDetCompra.setPrecio(rpm.getNeto());
                    ejDetCompra.setCompra(ejCompra);

                    detalleCompraManager.save(ejDetCompra);

                }
            }
            mensaje.setError(false);
            mensaje.setId(ejCompra.getId());
            mensaje.setMensaje("La compra se registro exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error  al guardar la compra.");
            return mensaje;
        }
        return mensaje;

    }

    @Override
    public MensajeDTO editarCompra(Long idCompra, Compra compra, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        try {

            if (idCompra == null || idCompra != null
                    && idCompra.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar  la compra.");
                return mensaje;
            }

            if (compra.getNroFactura() == null || compra.getNroFactura() != null
                    && compra.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (formaPgo == null || formaPgo != null
                    && formaPgo.compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar una forma de pago para relizar la compra.");
                return mensaje;
            }

            Compra ejCompraUp = this.get(idCompra);
            ejCompraUp.setNroFactura(compra.getNroFactura());
            ejCompraUp.setFormaPago(compra.getFormaPago());

            if (compra.getFormaPago() != null
                    && compra.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                ejCompraUp.setCantidadCuotas(compra.getCantidadCuotas());
                ejCompraUp.setPorcentajeInteresCredito(compra.getPorcentajeInteresCredito());
                ejCompraUp.setMoraInteres(compra.getMoraInteres());

                Double interes = Double.parseDouble(compra.getPorcentajeInteresCredito());

                if (compra.getEntrega() != null
                        && compra.getEntrega().compareToIgnoreCase("") != 0) {

                    Double entrega = Double.parseDouble(compra.getEntrega());

                    Double montoInteres = ((ejCompraUp.getPedido().getTotal() - entrega) * interes) / 100;

                    Double saldo = (ejCompraUp.getPedido().getTotal() - entrega) + montoInteres;

                    Double montoCuota = saldo / compra.getCantidadCuotas();

                    ejCompraUp.setEntrega(entrega + "");
                    ejCompraUp.setMontoInteres(montoInteres + "");
                    ejCompraUp.setSaldo(saldo + "");
                    ejCompraUp.setNeto(saldo);
                    ejCompraUp.setMontoCuotas(montoCuota + "");

                    Date fecha = new Date();

                    if (compra.getCuotaFecha() != null) {
                        fecha = sdfSimple.parse(compra.getCuotaFecha());
                    }

                    ejCompraUp.setFechaCuota(fecha);

                    this.update(ejCompraUp);

                    DocumentoPagar ejAPagar = new DocumentoPagar();
                    ejAPagar.setMonto(montoCuota);

                    List<DocumentoPagar> aPagar = documentoPagarManager.list(ejAPagar);

                    for (DocumentoPagar rpm : aPagar) {
                        documentoPagarManager.delete(rpm.getId());
                    }
                    int contador = 1;
                    boolean tieneFecha = true;
                    for (int i = 1; i <= compra.getCantidadCuotas(); i++) {

                        ejAPagar = new DocumentoPagar();
                        ejAPagar.setCompra(ejCompraUp);
                        ejAPagar.setActivo("S");
                        ejAPagar.setNroCuota(i + "");
                        ejAPagar.setMonto(montoCuota);
                        ejAPagar.setEstado(DocumentoPagar.PENDIENTE);

                        if (compra.getCuotaFecha() != null
                                && compra.getCuotaFecha().compareToIgnoreCase("") != 0) {

                            Calendar date = Calendar.getInstance();
                            if (tieneFecha) {
                                date.set(Calendar.MONTH, fecha.getMonth());
                                tieneFecha = false;

                            } else {
                                date.set(Calendar.MONTH, fecha.getMonth() + contador);
                            }

                            date.set(Calendar.DATE, fecha.getDate());

                            ejAPagar.setFecha(date.getTime());

                        } else {

                            Calendar date = Calendar.getInstance();
                            date.set(Calendar.DATE, 5);
                            date.set(Calendar.MONTH, fecha.getMonth() + i);

                            ejAPagar.setFecha(date.getTime());
                        }

                        documentoPagarManager.save(ejAPagar);
                    }

                } else {

                    Double montoInteres = ((ejCompraUp.getPedido().getTotal()) * interes) / 100;

                    Double saldo = (ejCompraUp.getPedido().getTotal()) + montoInteres;

                    Double montoCuota = saldo / compra.getCantidadCuotas();

                    ejCompraUp.setMontoInteres(montoInteres + "");
                    ejCompraUp.setSaldo(saldo + "");
                    ejCompraUp.setNeto(saldo);
                    ejCompraUp.setMontoCuotas(montoCuota + "");

                    this.update(ejCompraUp);

                    for (int i = 1; i <= compra.getCantidadCuotas(); i++) {

                        DocumentoPagar ejAPagar = new DocumentoPagar();
                        ejAPagar.setCompra(ejCompraUp);
                        ejAPagar.setActivo("S");
                        ejAPagar.setNroCuota(i + "");
                        ejAPagar.setMonto(montoCuota);
                        ejAPagar.setEstado(DocumentoPagar.PENDIENTE);

                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.MONTH, i);

                        String fecha = sdf.format(date.getTime());

                        ejAPagar.setFecha(Timestamp.valueOf(fecha));

                        documentoPagarManager.save(ejAPagar);
                    }
                }

            } else {
                if (compra.getTipoDescuento() != null
                        && compra.getTipoDescuento().compareToIgnoreCase("GENERAL") == 0) {

                    Double interes = Double.parseDouble(compra.getDescuento());

                    Double montoInteres = (ejCompraUp.getPedido().getTotal() * interes) / 100;

                    Double saldo = ejCompraUp.getPedido().getTotal() - montoInteres;

                    ejCompraUp.setSaldo(saldo + "");
                    ejCompraUp.setDescuento(interes + "");
                    ejCompraUp.setMontoDescuento(montoInteres + "");
                    ejCompraUp.setNeto(saldo);

                    this.update(ejCompraUp);
                }

            }

            mensaje.setError(false);
            mensaje.setId(ejCompra.getId());
            mensaje.setMensaje("La compra se registro exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error  al guardar la compra.");
            return mensaje;
        }
        return mensaje;
    }

}
