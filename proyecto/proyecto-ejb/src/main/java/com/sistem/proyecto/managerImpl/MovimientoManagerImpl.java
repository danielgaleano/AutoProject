/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.DocumentoCobrar;
import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Movimiento;
import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.CompraManager;
import com.sistem.proyecto.manager.DetalleCompraManager;
import com.sistem.proyecto.manager.DetalleVentaManager;
import com.sistem.proyecto.manager.DocumentoCobrarManager;
import com.sistem.proyecto.manager.DocumentoPagarManager;
import com.sistem.proyecto.manager.MovimientoManager;
import com.sistem.proyecto.manager.VehiculoManager;
import com.sistem.proyecto.manager.VentaManager;
import com.sistem.proyecto.manager.utils.DTORetorno;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import com.sistem.proyecto.manager.utils.PagoDTO;
import java.sql.Timestamp;
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
public class MovimientoManagerImpl extends GenericDaoImpl<Movimiento, Long>
        implements MovimientoManager {

    @EJB(mappedName = "java:app/proyecto-ejb/CompraManagerImpl")
    private CompraManager compraManager;

    @EJB(mappedName = "java:app/proyecto-ejb/VentaManagerImpl")
    private VentaManager ventaManager;

    @EJB(mappedName = "java:app/proyecto-ejb/VehiculoManagerImpl")
    private VehiculoManager vehiculoManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DetalleCompraManagerImpl")
    private DetalleCompraManager detalleCompraManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DetalleVentaManagerImpl")
    private DetalleVentaManager detalleVentaManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DocumentoPagarManagerImpl")
    private DocumentoPagarManager documentoPagarManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DocumentoCobrarManagerImpl")
    private DocumentoCobrarManager documentoCobrarManager;

    @Override
    protected Class<Movimiento> getEntityBeanType() {
        return Movimiento.class;
    }

    @Override
    public DTORetorno<PagoDTO> obtenerDatosPago(Long idCompra) throws Exception {
        DTORetorno<PagoDTO> retorno = new DTORetorno<PagoDTO>();
        Compra ejCompra = new Compra();

        PagoDTO pago = new PagoDTO();
        try {

            if (idCompra == null || idCompra != null
                    && idCompra.toString().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("Debe Ingresar un pedido para el detalle.");
                return retorno;
            }

            ejCompra = compraManager.get(idCompra);

            pago.setIdCompra(ejCompra.getId());
            pago.setIdProveedor(ejCompra.getProveedor().getId());

            pago.setNroFactura(ejCompra.getNroFactura());
            pago.setNeto(ejCompra.getNeto());
            pago.setFormaPago(ejCompra.getFormaPago());
            //pago.setSaldo(Double.parseDouble(ejCompra.getSaldo()));

            if (ejCompra.getEstadoCompra().compareToIgnoreCase(Compra.COMPRA_REALIZADA) == 0) {
                pago.setCancelado(true);
                retorno.setError(false);
                retorno.setMensaje("La cuenta ya se encuentra cancelada.");
                return retorno;
            }

            if (ejCompra.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                pago.setCantidadCuotas(ejCompra.getCantidadCuotas());

                DocumentoPagar docPagar = new DocumentoPagar();
                docPagar.setEstado(DocumentoPagar.ENTREGA);
                docPagar.setCompra(ejCompra);

                List<DocumentoPagar> entrega = documentoPagarManager.list(docPagar, "fecha", "asc");
                if (entrega != null && !entrega.isEmpty()) {
                    for (DocumentoPagar rpm : entrega) {

                        pago.setMonto(Math.round(rpm.getMonto()) + "");
                        pago.setCuota(rpm.getNroCuota());
                        pago.setFechaCuota(rpm.getFecha());
                        pago.setIdDocPagar(rpm.getId());

                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());
                    }

                    pago.setImportePagar(Double.parseDouble(pago.getMonto()));
                } else {

                    docPagar = new DocumentoPagar();
                    docPagar.setEstado(DocumentoPagar.PENDIENTE);
                    docPagar.setCompra(ejCompra);

                    List<DocumentoPagar> documentos = documentoPagarManager.list(docPagar, "fecha", "asc");

                    boolean tieneDeuda = true;
                    Long deudaTotal = Long.parseLong("0");

                    for (DocumentoPagar rpm : documentos) {
                        if (tieneDeuda) {
                            deudaTotal = Math.round(deudaTotal + rpm.getMonto());

                            pago.setMonto(Math.round(rpm.getMonto()) + "");
                            pago.setCuota(rpm.getNroCuota());
                            pago.setFechaCuota(rpm.getFecha());
                            pago.setIdDocPagar(rpm.getId());

                            tieneDeuda = false;
                            System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());
                        }
                    }
                    docPagar = new DocumentoPagar();
                    docPagar.setEstado(DocumentoPagar.PARCIAL);
                    docPagar.setCompra(ejCompra);

                    List<DocumentoPagar> documentosParcial = documentoPagarManager.list(docPagar, "fecha", "asc");

                    for (DocumentoPagar rpm : documentosParcial) {
                        Long saldo = Math.round(rpm.getSaldo());

                        deudaTotal = deudaTotal + saldo;

                        pago.setSaldo(Double.parseDouble(rpm.getSaldo().toString()));
                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());

                    }
                    pago.setImportePagar(Double.parseDouble(deudaTotal.toString()));
                }

                retorno.setData(pago);

                retorno.setError(false);
                retorno.setMensaje("La compra se obtuvo exitosamente.");

            } else {
                pago.setMonto(ejCompra.getMonto().toString());

                if (ejCompra.getSaldo() != null && ejCompra.getSaldo().compareToIgnoreCase("") != 0) {
                    pago.setSaldo(Double.parseDouble(ejCompra.getSaldo()));
                    pago.setImportePagar(Double.parseDouble(ejCompra.getNeto().toString()));
                    pago.setMonto(Math.round(ejCompra.getNeto()) + "");
                } else {
                    pago.setImportePagar(Double.parseDouble(ejCompra.getNeto().toString()));
                    pago.setMonto(Math.round(ejCompra.getNeto()) + "");
                }

                retorno.setData(pago);

                retorno.setError(false);
                retorno.setMensaje("La compra se obtuvo exitosamente.");

            }
            return retorno;

        } catch (Exception e) {
            System.err.println(e);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la compra");
        }
        //To change body of generated methods, choose Tools | Templates.
        return retorno;
    }

    @Override
    public MensajeDTO realizarCompra(Long idCompra, Double Monto, Double interes, Long idDocPago, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();

        Movimiento ejMovimiento = new Movimiento();

        PagoDTO pago = new PagoDTO();

        Long monto = Long.parseLong("0");
        Long interesPago = Long.parseLong("0");
        Long netoPago = Long.parseLong("0");
        Long saldo = Long.parseLong("0");

        DocumentoPagar docParcial = new DocumentoPagar();
        try {

            if (idCompra == null || idCompra != null
                    && idCompra.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al realizar el pago.");
                return mensaje;
            }

            ejCompra = compraManager.get(idCompra);

            if (ejCompra.getEstadoCompra().compareToIgnoreCase(Compra.COMPRA_REALIZADA) == 0) {
                pago.setCancelado(true);
                mensaje.setError(false);
                mensaje.setMensaje("La cuenta ya se encuentra cancelada.");
                return mensaje;
            }

            if (ejCompra.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                DocumentoPagar docPagar = new DocumentoPagar();
                docPagar.setId(idDocPago);

                docPagar = documentoPagarManager.get(idDocPago);

                if (docPagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                    if (Monto < docPagar.getMonto()) {

                        mensaje.setError(true);
                        mensaje.setMensaje("No se aceptan pagos parciales en la entrega.");
                        return mensaje;

                    } else {
                        docPagar = new DocumentoPagar();
                        docPagar = documentoPagarManager.get(idDocPago);

                        docPagar.setSaldo(0.0);
                        docPagar.setEstado(DocumentoPagar.CANCELADO);
                        documentoPagarManager.update(docPagar);

                        ejMovimiento.setCompra(ejCompra);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(0.0);
                        ejMovimiento.setSaldo(0.0);
                        ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setProveedor(ejCompra.getProveedor());
                        ejMovimiento.setTipoTransaccion("O");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));
                    }
                } else {

                    Long deudaTotal = Math.round(docPagar.getMonto());

                    docParcial.setEstado(DocumentoPagar.PARCIAL);
                    docParcial.setCompra(ejCompra);

                    List<DocumentoPagar> documentosParcial = documentoPagarManager.list(docParcial, "fecha", "asc");
                    Long idDocumento = null;
                    boolean tieneParcia = false;

                    for (DocumentoPagar rpm : documentosParcial) {
                        tieneParcia = true;
                        saldo = Math.round(rpm.getSaldo());
                        idDocumento = rpm.getId();
                        deudaTotal = deudaTotal + saldo;
                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());

                    }
                    if (tieneParcia) {

                        if (Math.round(Monto) > saldo) {
                            docPagar = new DocumentoPagar();
                            docPagar = documentoPagarManager.get(idDocumento);

                            docPagar.setSaldo(0.0);
                            docPagar.setEstado(DocumentoPagar.CANCELADO);
                            documentoPagarManager.update(docPagar);

                            netoPago = (deudaTotal - saldo) + Math.round(interes);

                            saldo = Math.round(Monto) - netoPago;

                            if (saldo < 0) {
                                Long netoSaldo = netoPago - Math.round(Monto);

                                docPagar = new DocumentoPagar();

                                docPagar = documentoPagarManager.get(idDocPago);

                                docPagar.setSaldo(Double.parseDouble(netoSaldo.toString()));
                                docPagar.setEstado(DocumentoPagar.PARCIAL);

                                documentoPagarManager.update(docPagar);

                                ejMovimiento.setCompra(ejCompra);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setNeto(Monto);
                                ejMovimiento.setInteres(interes);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setProveedor(ejCompra.getProveedor());
                                ejMovimiento.setTipoTransaccion("O");
                                ejMovimiento.setVuelto(0.0);
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));

                            } else {
                                docPagar = new DocumentoPagar();

                                docPagar = documentoPagarManager.get(idDocPago);

                                docPagar.setSaldo(0.0);
                                docPagar.setEstado(DocumentoPagar.CANCELADO);

                                documentoPagarManager.update(docPagar);

                                ejMovimiento.setCompra(ejCompra);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setInteres(interes);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                                ejMovimiento.setSaldo(0.0);
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setProveedor(ejCompra.getProveedor());
                                ejMovimiento.setTipoTransaccion("O");
                                ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));
                            }
                        } else {
                            docPagar = new DocumentoPagar();
                            docPagar = documentoPagarManager.get(idDocumento);

                            if (Math.round(interes) > Math.round(Monto)) {
                                mensaje.setError(true);
                                mensaje.setMensaje("No se aceptan pagos menores al interes.");
                                return mensaje;
                            }

                            netoPago = saldo + Math.round(interes);

                            saldo = netoPago - Math.round(Monto);

                            if (saldo > 0) {

                                docPagar.setSaldo(Double.parseDouble(saldo + ""));
                                docPagar.setEstado(DocumentoPagar.PARCIAL);

                                documentoPagarManager.update(docPagar);

                                ejMovimiento.setCompra(ejCompra);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setNeto(Monto);
                                ejMovimiento.setInteres(interes);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setSaldo(Double.parseDouble(saldo.toString()));
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setProveedor(ejCompra.getProveedor());
                                ejMovimiento.setTipoTransaccion("O");
                                ejMovimiento.setVuelto(0.0);
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));

                            }

                        }
                    } else {
                        netoPago = deudaTotal + Math.round(interes);

                        saldo = Math.round(Monto) - netoPago;

                        docPagar = new DocumentoPagar();
                        docPagar = documentoPagarManager.get(idDocPago);

                        if (saldo < 0) {

                            saldo = netoPago - Math.round(Monto);

                            docPagar.setEstado(DocumentoPagar.PARCIAL);
                            docPagar.setSaldo(Double.parseDouble(saldo.toString()));
                            documentoPagarManager.update(docPagar);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                            ejMovimiento.setSaldo(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                        } else {

                            docPagar.setSaldo(0.0);
                            docPagar.setEstado(DocumentoPagar.CANCELADO);
                            documentoPagarManager.update(docPagar);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));
                        }
                    }
                }

                this.save(ejMovimiento);

                Vehiculo ejVehiculo = new Vehiculo();
                ejVehiculo.setEstado("PENDIENTE");

                DetalleCompra ejDetalle = new DetalleCompra();
                ejDetalle.setCompra(ejCompra);
                ejDetalle.setVehiculo(ejVehiculo);

                List<DetalleCompra> listDetalle = detalleCompraManager.list(ejDetalle);

                Long montoTotal = Long.parseLong(Math.round(Double.parseDouble(ejCompra.getMonto())) + "");

                Long montoTotalImteres = Long.parseLong("0");

                if (ejCompra.getEntrega() != null && ejCompra.getEntrega().compareToIgnoreCase("") != 0) {
                    montoTotalImteres = Long.parseLong(Math.round(Double.parseDouble(ejCompra.getEntrega())) + "") + Math.round(ejCompra.getNeto());
                } else {
                    montoTotalImteres = Math.round(ejCompra.getNeto());
                }

                for (DetalleCompra rpm : listDetalle) {

                    Double porcentajeVehiculo = rpm.getNeto() / montoTotal;

                    Double costoInteresVeh = porcentajeVehiculo * montoTotalImteres;

                    rpm.getVehiculo().setEstado(Vehiculo.MANTENIMIENTO);
                    rpm.getVehiculo().setPrecioCosto(Double.parseDouble(costoInteresVeh.toString()));

                    vehiculoManager.update(rpm.getVehiculo());

                }

                docParcial = new DocumentoPagar();
                docParcial.setEstado(DocumentoPagar.PENDIENTE);
                docParcial.setCompra(ejCompra);

                int documentosPendientes = documentoPagarManager.list(docParcial, "fecha", "asc").size();

                if (documentosPendientes <= 0) {
                    ejCompra.setEstadoPago(DocumentoPagar.CANCELADO);
                    ejCompra.setEstadoCompra(Compra.COMPRA_REALIZADA);

                    compraManager.update(ejCompra);
                }

            } else {

                if (ejCompra.getEstadoPago().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {
                    netoPago = Math.round(ejCompra.getNeto()) + Math.round(interes);

                    saldo = Math.round(Monto) - netoPago;
                    if (saldo < 0) {
                        Long netoSaldo = netoPago - Math.round(Monto);

                        ejCompra.setSaldo(netoSaldo.toString());
                        ejCompra.setEstadoPago(DocumentoPagar.PARCIAL);

                        compraManager.update(ejCompra);

                        ejMovimiento.setCompra(ejCompra);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(interes);
                        ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setProveedor(ejCompra.getProveedor());
                        ejMovimiento.setTipoTransaccion("O");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));

                        this.save(ejMovimiento);
                    } else {
                        if (saldo > 0) {

                            ejCompra.setEstadoPago(DocumentoPagar.CANCELADO);
                            ejCompra.setEstadoCompra(Compra.COMPRA_REALIZADA);

                            compraManager.update(ejCompra);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        } else if (saldo == 0) {

                            ejCompra.setEstadoPago(DocumentoPagar.CANCELADO);
                            ejCompra.setEstadoCompra(Compra.COMPRA_REALIZADA);

                            compraManager.update(ejCompra);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        }
                    }
                } else if (ejCompra.getEstadoPago().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {

                    netoPago = Long.parseLong(ejCompra.getSaldo()) + Math.round(interes);

                    saldo = Math.round(Monto) - netoPago;

                    if (saldo < 0) {
                        Long netoSaldo = netoPago - Math.round(Monto);

                        ejCompra.setSaldo(netoSaldo.toString());
                        ejCompra.setEstadoPago(DocumentoPagar.PARCIAL);

                        compraManager.update(ejCompra);

                        ejMovimiento.setCompra(ejCompra);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(interes);
                        ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setProveedor(ejCompra.getProveedor());
                        ejMovimiento.setTipoTransaccion("O");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));

                        this.save(ejMovimiento);
                    } else {
                        if (saldo > 0) {
                            ejCompra.setSaldo("0");
                            ejCompra.setEstadoCompra(Compra.COMPRA_REALIZADA);
                            ejCompra.setEstadoPago(DocumentoPagar.CANCELADO);
                            compraManager.update(ejCompra);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        } else if (saldo == 0) {
                            ejCompra.setSaldo("0");
                            ejCompra.setEstadoCompra(Compra.COMPRA_REALIZADA);
                            ejCompra.setEstadoPago(DocumentoPagar.CANCELADO);
                            compraManager.update(ejCompra);

                            ejMovimiento.setCompra(ejCompra);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setInteres(interes);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setProveedor(ejCompra.getProveedor());
                            ejMovimiento.setTipoTransaccion("O");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        }
                    }
                }
                if (ejCompra.getTipoDescuento().compareToIgnoreCase("GENERAL") == 0) {

                    Vehiculo ejVehiculo = new Vehiculo();
                    ejVehiculo.setEstado("PENDIENTE");

                    DetalleCompra ejDetalle = new DetalleCompra();
                    ejDetalle.setCompra(ejCompra);
                    ejDetalle.setVehiculo(ejVehiculo);

                    List<DetalleCompra> listDetalle = detalleCompraManager.list(ejDetalle);

                    Long montoTotal = Long.parseLong(Math.round(Double.parseDouble(ejCompra.getMonto())) + "");

                    Long montoTotalImteres = Long.parseLong("0");

                    if (ejCompra.getMontoDescuento() != null && ejCompra.getMontoDescuento().compareToIgnoreCase("") != 0) {
                        montoTotalImteres = Long.parseLong(Math.round(Double.parseDouble(ejCompra.getMontoDescuento())) + "");
                    }

                    for (DetalleCompra rpm : listDetalle) {

                        Double porcentajeVehiculo = rpm.getNeto() / montoTotal;

                        Double costoInteresVeh = porcentajeVehiculo * montoTotalImteres;

                        Double costoVeh = Math.round(rpm.getNeto()) - costoInteresVeh;

                        rpm.getVehiculo().setEstado(Vehiculo.MANTENIMIENTO);
                        rpm.getVehiculo().setPrecioCosto(costoVeh);

                        vehiculoManager.update(rpm.getVehiculo());

                    }
                } else {
                    DetalleCompra ejDetalle = new DetalleCompra();
                    ejDetalle.setCompra(ejCompra);
                    List<DetalleCompra> listDetalle = detalleCompraManager.list(ejDetalle);

                    for (DetalleCompra rpm : listDetalle) {

                        rpm.getVehiculo().setEstado(Vehiculo.MANTENIMIENTO);
                        rpm.getVehiculo().setPrecioCosto(rpm.getNeto());

                        vehiculoManager.update(rpm.getVehiculo());

                    }
                }

            }

            mensaje.setId(idCompra);
            mensaje.setError(false);
            mensaje.setMensaje("El pago se realizo exitosamente.");
            return mensaje;
        } catch (Exception e) {
            System.err.println(e);
            mensaje.setError(true);
            mensaje.setMensaje("Error al realizar el pago.");
        }
        //To change body of generated methods, choose Tools | Templates.
        return mensaje;
    }

    @Override
    public MensajeDTO rechazar(Long idDetalle, Long idPedido, Long idEmpresa, Long idUsuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DTORetorno<PagoDTO> obtenerDatosCobro(Long idVenta) throws Exception {
        DTORetorno<PagoDTO> retorno = new DTORetorno<PagoDTO>();
        Venta ejVenta = new Venta();

        PagoDTO pago = new PagoDTO();
        try {

            if (idVenta == null || idVenta != null
                    && idVenta.toString().compareToIgnoreCase("") == 0) {
                retorno.setError(true);
                retorno.setMensaje("Debe Ingresar una venta para el detalle.");
                return retorno;
            }

            ejVenta = ventaManager.get(idVenta);

            pago.setIdCompra(ejVenta.getId());
            pago.setIdProveedor(ejVenta.getCliente().getId());

            pago.setNroFactura(ejVenta.getNroFactura());
            pago.setNeto(ejVenta.getNeto());
            pago.setFormaPago(ejVenta.getFormaPago());
            //pago.setSaldo(Double.parseDouble(ejCompra.getSaldo()));

            if (ejVenta.getEstadoVenta().compareToIgnoreCase(Venta.VENTA_REALIZADA) == 0) {
                pago.setCancelado(true);
                retorno.setError(false);
                retorno.setMensaje("La cuenta ya se encuentra cancelada.");
                return retorno;
            }

            if (ejVenta.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                pago.setCantidadCuotas(ejVenta.getCantidadCuotas());

                DocumentoCobrar docPagar = new DocumentoCobrar();
                docPagar.setEstado(DocumentoPagar.ENTREGA);
                docPagar.setVenta(ejVenta);

                List<DocumentoCobrar> entrega = documentoCobrarManager.list(docPagar, "fecha", "asc");

                if (entrega != null && !entrega.isEmpty()) {
                    for (DocumentoCobrar rpm : entrega) {

                        pago.setMonto(Math.round(rpm.getMonto()) + "");
                        pago.setCuota(rpm.getNroCuota());
                        pago.setFechaCuota(rpm.getFecha());
                        pago.setIdDocPagar(rpm.getId());

                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());
                    }

                    pago.setImportePagar(Double.parseDouble(pago.getMonto()));
                } else {

                    docPagar = new DocumentoCobrar();
                    docPagar.setEstado(DocumentoPagar.PENDIENTE);
                    docPagar.setVenta(ejVenta);

                    List<DocumentoCobrar> documentos = documentoCobrarManager.list(docPagar, "fecha", "asc");

                    boolean tieneDeuda = true;
                    Long deudaTotal = Long.parseLong("0");

                    Date fechaActual = new Timestamp(System.currentTimeMillis());

                    Calendar date = Calendar.getInstance();

                    for (DocumentoCobrar rpm : documentos) {
                        if (tieneDeuda) {
                            deudaTotal = Math.round(deudaTotal + rpm.getMonto());

                            date.set(Calendar.YEAR, rpm.getFecha().getYear() + 1900);
                            date.set(Calendar.MONTH, rpm.getFecha().getMonth());
                            date.set(Calendar.DATE, rpm.getFecha().getDate() + Integer.parseInt(ejVenta.getDiasGracia() + ""));

                            pago.setMonto(Math.round(rpm.getMonto()) + "");
                            pago.setCuota(rpm.getNroCuota());
                            pago.setFechaCuota(rpm.getFecha());
                            pago.setIdDocPagar(rpm.getId());

                            tieneDeuda = false;
                            System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());
                           
                        }
                         break;
                    }

                    int dias = diferenciaEnDias(fechaActual, date.getTime());
                    
                    pago.setDiasMoraCuota(Long.parseLong(dias+""));
                    
                    Double montoInteresPendiente = 0.0;

                    Double interes = Double.parseDouble(ejVenta.getMoraInteres()) / 100;

                    Double interesDias = Double.parseDouble(dias + "") / 360;

                    if (dias > 0) {
                        montoInteresPendiente = interesDias * interes * deudaTotal;

                    }

                    docPagar = new DocumentoCobrar();
                    docPagar.setEstado(DocumentoPagar.PARCIAL);
                    docPagar.setVenta(ejVenta);

                    List<DocumentoCobrar> documentosParcial = documentoCobrarManager.list(docPagar, "fecha", "asc");

                    date = Calendar.getInstance();
                    Long saldo = Long.parseLong("0");
                    for (DocumentoCobrar rpm : documentosParcial) {

                        saldo = Math.round(rpm.getSaldo());

                        date.set(Calendar.YEAR, rpm.getFecha().getYear() + 1900);
                        date.set(Calendar.MONTH, rpm.getFecha().getMonth());
                        date.set(Calendar.DATE, rpm.getFecha().getDate() + Integer.parseInt(ejVenta.getDiasGracia() + ""));

                        deudaTotal = deudaTotal + saldo;

                        pago.setSaldo(Double.parseDouble(rpm.getSaldo().toString()));
                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());

                    }
                    int diasParcial = diferenciaEnDias(fechaActual, date.getTime());
                    
                    pago.setDiasMoraSaldo(Long.parseLong(diasParcial+""));
                    
                    Double montoInteresParcial = 0.0;

                    Double interesDiasParcial = Double.parseDouble(diasParcial + "") / 360;

                    if (diasParcial > 0) {
                        montoInteresParcial = interesDiasParcial * interes * saldo;

                    }
                    deudaTotal = deudaTotal + Math.round(montoInteresParcial) + Math.round(montoInteresPendiente);

                    pago.setMontoInteres(montoInteresParcial + montoInteresPendiente);

                    pago.setImportePagar(Double.parseDouble(deudaTotal.toString()));
                }

                retorno.setData(pago);

                retorno.setError(false);
                retorno.setMensaje("La venta se obtuvo exitosamente.");

            } else {

                if (ejVenta.getSaldo() != null && ejVenta.getSaldo().compareToIgnoreCase("") != 0) {
                    pago.setSaldo(Double.parseDouble(ejVenta.getSaldo()));
                    pago.setImportePagar(Double.parseDouble(ejVenta.getNeto().toString()));
                    pago.setMonto(Math.round(ejVenta.getNeto()) + "");
                } else {
                    pago.setImportePagar(Double.parseDouble(ejVenta.getNeto().toString()));
                    pago.setMonto(Math.round(ejVenta.getNeto()) + "");
                }

                retorno.setData(pago);

                retorno.setError(false);
                retorno.setMensaje("La venta se obtuvo exitosamente.");

            }
            return retorno;

        } catch (Exception e) {
            System.err.println(e);
            retorno.setError(true);
            retorno.setMensaje("Error al obtener la venta");
        }
        //To change body of generated methods, choose Tools | Templates.
        return retorno; //To change body of generated methods, choose Tools | Templates.
    }

    public static int diferenciaEnDias(Date fechaMayor, Date fechaMenor) {

        long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();

        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);

        return (int) dias;
    }

    @Override
    public MensajeDTO realizarPago(Long idVenta, Double Monto, Double interesCobro, Long idDocPago, Long idEmpresa, Long idUsuario) throws Exception {

        MensajeDTO mensaje = new MensajeDTO();
        Venta ejVenta = new Venta();

        Movimiento ejMovimiento = new Movimiento();

        PagoDTO pago = new PagoDTO();

        Long monto = Long.parseLong("0");
        Long interesPago = Long.parseLong("0");
        Long netoPago = Long.parseLong("0");
        Long saldo = Long.parseLong("0");

        DocumentoCobrar docParcial = new DocumentoCobrar();
        Date fechaActual = new Timestamp(System.currentTimeMillis());
        Calendar date = Calendar.getInstance();

        try {

            if (idVenta == null || idVenta != null
                    && idVenta.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Error al realizar el pago.");
                return mensaje;
            }

            ejVenta = ventaManager.get(idVenta);
            ejVenta.setEstadoVenta(Venta.VENTA_PAGADA);
            
            if (ejVenta.getEstadoVenta().compareToIgnoreCase(Venta.VENTA_REALIZADA) == 0) {
                pago.setCancelado(true);
                mensaje.setError(false);
                mensaje.setMensaje("La cuenta ya se encuentra cancelada.");
                return mensaje;
            }

            if (ejVenta.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                DocumentoCobrar docPagar = new DocumentoCobrar();
                docPagar.setId(idDocPago);

                docPagar = documentoCobrarManager.get(idDocPago);

                if (docPagar.getEstado().compareToIgnoreCase(DocumentoPagar.ENTREGA) == 0) {
                    if (Monto < docPagar.getMonto()) {

                        mensaje.setError(true);
                        mensaje.setMensaje("No se aceptan pagos parciales en la entrega.");
                        return mensaje;

                    } else {
                        docPagar = new DocumentoCobrar();
                        docPagar = documentoCobrarManager.get(idDocPago);

                        docPagar.setSaldo(0.0);
                        docPagar.setEstado(DocumentoCobrar.CANCELADO);
                        documentoCobrarManager.update(docPagar);

                        ejMovimiento.setVenta(ejVenta);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(0.0);
                        ejMovimiento.setSaldo(0.0);
                        ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setCliente(ejVenta.getCliente());
                        ejMovimiento.setTipoTransaccion("I");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));
                    }
                } else {

                    Long deudaTotal = Math.round(docPagar.getMonto());

                    Double interes = Double.parseDouble(ejVenta.getMoraInteres()) / 100;

                    date.set(Calendar.YEAR, docPagar.getFecha().getYear() + 1900);
                    date.set(Calendar.MONTH, docPagar.getFecha().getMonth());
                    date.set(Calendar.DATE, docPagar.getFecha().getDate() + Integer.parseInt(ejVenta.getDiasGracia() + ""));

                    int diasPendiente = diferenciaEnDias(fechaActual, date.getTime());

                    Double montoInteresPendiente = 0.0;

                    Double interesDiasPendiente = Double.parseDouble(diasPendiente + "") / 360;

                    if (diasPendiente > 0) {
                        montoInteresPendiente = interesDiasPendiente * interes * deudaTotal;

                    }

                    docParcial.setEstado(DocumentoCobrar.PARCIAL);
                    docParcial.setVenta(ejVenta);

                    List<DocumentoCobrar> documentosParcial = documentoCobrarManager.list(docParcial, "fecha", "asc");

                    Long idDocumento = null;
                    boolean tieneParcia = false;
                    date = Calendar.getInstance();

                    for (DocumentoCobrar rpm : documentosParcial) {
                        tieneParcia = true;

                        saldo = Math.round(rpm.getSaldo());
                        idDocumento = rpm.getId();

                        date.set(Calendar.YEAR, rpm.getFecha().getYear() + 1900);
                        date.set(Calendar.MONTH, rpm.getFecha().getMonth());
                        date.set(Calendar.DATE, rpm.getFecha().getDate() + Integer.parseInt(ejVenta.getDiasGracia() + ""));

                        System.out.println(rpm.getNroCuota() + " " + rpm.getFecha());
                        break;
                    }
                    if (tieneParcia) {

                        int diasParcial = diferenciaEnDias(fechaActual, date.getTime());

                        Double montoInteresParcial = 0.0;

                        Double interesDiasParcial = Double.parseDouble(diasParcial + "") / 360;

                        if (diasParcial > 0) {
                            montoInteresParcial = interesDiasParcial * interes * saldo;

                        }

                        Long saldoInteres = saldo + Math.round(montoInteresParcial);

                        deudaTotal = deudaTotal + saldoInteres + Math.round(montoInteresPendiente);

                        if (Math.round(Monto) > saldoInteres) {

                            docPagar = new DocumentoCobrar();
                            docPagar = documentoCobrarManager.get(idDocumento);

                            docPagar.setSaldo(0.0);
                            docPagar.setEstado(DocumentoCobrar.CANCELADO);
                            documentoCobrarManager.update(docPagar);

                            netoPago = deudaTotal - saldoInteres;

                            saldo = Math.round(Monto) - netoPago;

                            if (saldo < 0) {
                                Long netoSaldo = netoPago - Math.round(Monto);

                                docPagar = new DocumentoCobrar();

                                docPagar = documentoCobrarManager.get(idDocPago);

                                docPagar.setSaldo(Double.parseDouble(netoSaldo.toString()));
                                docPagar.setEstado(DocumentoPagar.PARCIAL);

                                documentoCobrarManager.update(docPagar);

                                ejMovimiento.setVenta(ejVenta);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setNeto(Monto);
                                ejMovimiento.setInteres(montoInteresParcial);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setCliente(ejVenta.getCliente());
                                ejMovimiento.setTipoTransaccion("I");
                                ejMovimiento.setVuelto(0.0);
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));

                            } else {
                                docPagar = new DocumentoCobrar();

                                docPagar = documentoCobrarManager.get(idDocPago);

                                docPagar.setSaldo(0.0);
                                docPagar.setEstado(DocumentoCobrar.CANCELADO);

                                documentoCobrarManager.update(docPagar);

                                ejMovimiento.setVenta(ejVenta);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setInteres(montoInteresParcial);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                                ejMovimiento.setSaldo(0.0);
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setCliente(ejVenta.getCliente());
                                ejMovimiento.setTipoTransaccion("I");
                                ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));
                            }
                        } else {
                            docPagar = new DocumentoCobrar();
                            docPagar = documentoCobrarManager.get(idDocumento);

                            if (Math.round(montoInteresParcial) > Math.round(Monto)) {
                                mensaje.setError(true);
                                mensaje.setMensaje("No se aceptan pagos menores al interes.");
                                return mensaje;
                            }

                            netoPago = saldo + Math.round(montoInteresParcial);

                            saldo = netoPago - Math.round(Monto);

                            if (saldo > 0) {

                                docPagar.setSaldo(Double.parseDouble(saldo + ""));
                                docPagar.setEstado(DocumentoPagar.PARCIAL);

                                documentoCobrarManager.update(docPagar);

                                ejMovimiento.setVenta(ejVenta);
                                ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                                ejMovimiento.setImporte(Monto);
                                ejMovimiento.setNeto(Monto);
                                ejMovimiento.setInteres(montoInteresParcial);
                                ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                                ejMovimiento.setSaldo(Double.parseDouble(saldo.toString()));
                                ejMovimiento.setActivo("S");
                                ejMovimiento.setCliente(ejVenta.getCliente());
                                ejMovimiento.setTipoTransaccion("I");
                                ejMovimiento.setVuelto(0.0);
                                ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                                ejMovimiento.setUsuario(new Usuario(idUsuario));

                            }

                        }
                    } else {

                        netoPago = deudaTotal + Math.round(montoInteresPendiente);

                        saldo = Math.round(Monto) - netoPago;

                        docPagar = new DocumentoCobrar();
                        docPagar = documentoCobrarManager.get(idDocPago);

                        if (saldo < 0) {

                            saldo = netoPago - Math.round(Monto);

                            docPagar.setEstado(DocumentoCobrar.PARCIAL);
                            docPagar.setSaldo(Double.parseDouble(saldo.toString()));
                            documentoCobrarManager.update(docPagar);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setInteres(montoInteresPendiente);
                            ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                            ejMovimiento.setSaldo(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                        } else {

                            docPagar.setSaldo(0.0);
                            docPagar.setEstado(DocumentoCobrar.CANCELADO);
                            documentoCobrarManager.update(docPagar);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setCuota(Long.parseLong(docPagar.getNroCuota()));
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setInteres(montoInteresPendiente);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));
                        }
                    }
                }

                this.save(ejMovimiento);

                Vehiculo ejVehiculo = new Vehiculo();
                ejVehiculo.setEstado(Vehiculo.PROVESO_VENTA);

                DetalleVenta ejDetalle = new DetalleVenta();
                ejDetalle.setVenta(ejVenta);
                ejDetalle.setVehiculo(ejVehiculo);

                List<DetalleVenta> listDetalle = detalleVentaManager.list(ejDetalle);

                for (DetalleVenta rpm : listDetalle) {

                    rpm.getVehiculo().setEstado(Vehiculo.VENDIDA);
                    vehiculoManager.update(rpm.getVehiculo());

                }

                docParcial = new DocumentoCobrar();
                docParcial.setEstado(DocumentoCobrar.PENDIENTE);
                docParcial.setVenta(ejVenta);

                int documentosPendientes = documentoCobrarManager.list(docParcial, "fecha", "asc").size();

                if (documentosPendientes <= 0) {
                    ejVenta.setEstadoCobro(DocumentoPagar.CANCELADO);
                    ejVenta.setEstadoVenta(Venta.VENTA_REALIZADA);

                    ventaManager.update(ejVenta);
                }

            } else {

                if (ejVenta.getEstadoCobro().compareToIgnoreCase(DocumentoPagar.PENDIENTE) == 0) {

//                    Date fechaVenta = ejVenta.getFechaVenta();
//
//                    int diasParcial = diferenciaEnDias(fechaActual, fechaVenta);
//
//                    Double montoInteresDirecta = 0.0;
//
//                    Double interesDiasDirecta = Double.parseDouble(diasParcial + "") / 360;
                    netoPago = Math.round(ejVenta.getNeto());

                    saldo = Math.round(Monto) - netoPago;

                    if (saldo < 0) {
                        Long netoSaldo = netoPago - Math.round(Monto);

                        ejVenta.setSaldo(netoSaldo.toString());
                        ejVenta.setEstadoCobro(DocumentoPagar.PARCIAL);

                        ventaManager.update(ejVenta);

                        ejMovimiento.setVenta(ejVenta);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(0.0);
                        ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setCliente(ejVenta.getCliente());
                        ejMovimiento.setTipoTransaccion("I");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));

                        this.save(ejMovimiento);
                    } else {
                        if (saldo > 0) {

                            ejVenta.setEstadoCobro(DocumentoPagar.CANCELADO);
                            ejVenta.setEstadoVenta(Venta.VENTA_REALIZADA);

                            ventaManager.update(ejVenta);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setInteres(0.0);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        } else if (saldo == 0) {

                            ejVenta.setEstadoCobro(DocumentoPagar.CANCELADO);
                            ejVenta.setEstadoVenta(Venta.VENTA_REALIZADA);

                            ventaManager.update(ejVenta);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setInteres(0.0);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        }
                    }
                } else if (ejVenta.getEstadoCobro().compareToIgnoreCase(DocumentoPagar.PARCIAL) == 0) {

                    netoPago = Long.parseLong(ejVenta.getSaldo());

                    saldo = Math.round(Monto) - netoPago;

                    if (saldo < 0) {
                        Long netoSaldo = netoPago - Math.round(Monto);

                        ejVenta.setSaldo(netoSaldo.toString());
                        ejVenta.setEstadoCobro(DocumentoPagar.PARCIAL);

                        ventaManager.update(ejVenta);

                        ejMovimiento.setVenta(ejVenta);
                        ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                        ejMovimiento.setImporte(Monto);
                        ejMovimiento.setNeto(Monto);
                        ejMovimiento.setInteres(0.0);
                        ejMovimiento.setSaldo(Double.parseDouble(netoSaldo.toString()));
                        ejMovimiento.setActivo("S");
                        ejMovimiento.setCliente(ejVenta.getCliente());
                        ejMovimiento.setTipoTransaccion("I");
                        ejMovimiento.setVuelto(0.0);
                        ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                        ejMovimiento.setUsuario(new Usuario(idUsuario));

                        this.save(ejMovimiento);
                    } else {
                        if (saldo > 0) {
                            ejVenta.setSaldo("0");
                            ejVenta.setEstadoVenta(Venta.VENTA_REALIZADA);
                            ejVenta.setEstadoCobro(DocumentoPagar.CANCELADO);
                            ventaManager.update(ejVenta);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setInteres(0.0);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setNeto(Monto - Double.parseDouble(saldo.toString()));
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(Double.parseDouble(saldo.toString()));
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        } else if (saldo == 0) {
                            ejVenta.setSaldo("0");
                            ejVenta.setEstadoVenta(Venta.VENTA_REALIZADA);
                            ejVenta.setEstadoCobro(DocumentoPagar.CANCELADO);
                            ventaManager.update(ejVenta);

                            ejMovimiento.setVenta(ejVenta);
                            ejMovimiento.setEmpresa(new Empresa(idEmpresa));
                            ejMovimiento.setImporte(Monto);
                            ejMovimiento.setInteres(0.0);
                            ejMovimiento.setSaldo(0.0);
                            ejMovimiento.setNeto(Monto);
                            ejMovimiento.setActivo("S");
                            ejMovimiento.setCliente(ejVenta.getCliente());
                            ejMovimiento.setTipoTransaccion("I");
                            ejMovimiento.setVuelto(0.0);
                            ejMovimiento.setFechaIngreso(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                            ejMovimiento.setUsuario(new Usuario(idUsuario));

                            this.save(ejMovimiento);

                        }
                    }
                }
                Vehiculo ejVehiculo = new Vehiculo();
                ejVehiculo.setEstado("PENDIENTE");

                DetalleVenta ejDetalle = new DetalleVenta();
                ejDetalle.setVenta(ejVenta);
                ejDetalle.setVehiculo(ejVehiculo);
                
                List<DetalleVenta> listDetalle = detalleVentaManager.list(ejDetalle);
                
                for (DetalleVenta rpm : listDetalle) {

                    rpm.getVehiculo().setEstado(Vehiculo.VENDIDA);

                    vehiculoManager.update(rpm.getVehiculo());

                }
                

            }

            mensaje.setId(idVenta);
            mensaje.setError(false);
            mensaje.setMensaje("La venta se realizo exitosamente.");
            return mensaje;
        } catch (Exception e) {
            System.err.println(e);
            mensaje.setError(true);
            mensaje.setMensaje("Error al realizar el pago.");
        }
        //To change body of generated methods, choose Tools | Templates.
        return mensaje;//To change body of generated methods, choose Tools | Templates.
    }

}
