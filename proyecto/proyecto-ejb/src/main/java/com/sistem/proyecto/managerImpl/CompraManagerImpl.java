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
import com.sistem.proyecto.entity.Marca;
import com.sistem.proyecto.entity.Modelo;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.entity.Tipo;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.CompraManager;
import com.sistem.proyecto.manager.DetalleCompraManager;
import com.sistem.proyecto.manager.DocumentoPagarManager;
import com.sistem.proyecto.manager.MonedaManager;
import com.sistem.proyecto.manager.PedidoManager;
import com.sistem.proyecto.manager.VehiculoManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import java.security.SecureRandom;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @EJB(mappedName = "java:app/proyecto-ejb/VehiculoManagerImpl")
    private VehiculoManager vehiculoManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DocumentoPagarManagerImpl")
    private DocumentoPagarManager documentoPagarManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DetalleCompraManagerImpl")
    private DetalleCompraManager detalleCompraManager;

    @EJB(mappedName = "java:app/proyecto-ejb/MonedaManagerImpl")
    private MonedaManager monedaManager;

    protected SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSSSSS");
    protected SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyy-MM-dd");

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Override
    protected Class<Compra> getEntityBeanType() {
        return Compra.class;
    }

    @Override
    public MensajeDTO guardarCompra(Long idCompra, DetalleCompra detalleCompra, String nroFactura, String formaPgo, String tipoDescuento, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Compra ejCompra = new Compra();
        try {

            if (nroFactura == null || nroFactura != null
                    && nroFactura.compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getTipo() == null || detalleCompra.getVehiculo().getTipo().getId() != null
                    && detalleCompra.getVehiculo().getTipo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El tipo de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getMarca() == null || detalleCompra.getVehiculo().getMarca().getId() != null
                    && detalleCompra.getVehiculo().getMarca().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La marca de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getModelo() == null || detalleCompra.getVehiculo().getModelo().getId() != null
                    && detalleCompra.getVehiculo().getModelo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El modelo del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getAnho() == null || detalleCompra.getVehiculo().getAnho() != null
                    && detalleCompra.getVehiculo().getAnho().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El año del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getColor() == null || detalleCompra.getVehiculo().getColor() != null
                    && detalleCompra.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getColor() == null || detalleCompra.getVehiculo().getColor() != null
                    && detalleCompra.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getVehiculo().getTransmision() == null || detalleCompra.getVehiculo().getTransmision() != null
                    && detalleCompra.getVehiculo().getTransmision().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La transmision del vehiculo no puede estar vacia.");
                return mensaje;
            }

            if (detalleCompra.getMoneda() == null || detalleCompra.getMoneda().getId() != null
                    && detalleCompra.getMoneda().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo moneda no puede estar vacia.");
                return mensaje;
            }

            if (detalleCompra.getPrecio() == null || detalleCompra.getPrecio() != null
                    && detalleCompra.getPrecio().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo precio no puede estar vacio.");
                return mensaje;
            }

            if (detalleCompra.getCompra().getProveedor() == null || detalleCompra.getCompra().getProveedor().getId() == null
                    || detalleCompra.getCompra().getProveedor().getId() != null
                    && detalleCompra.getCompra().getProveedor().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un proovedor para realizar la compra.");
                return mensaje;
            }

            if (idCompra == null || idCompra != null
                    && idCompra.toString().compareToIgnoreCase("") == 0) {

                Compra ejFactura = new Compra();
                ejFactura.setNroFactura(nroFactura);

                Map<String, Object> nroFacturaMap = this.getAtributos(ejFactura, "id".split(","), true, true);

                if (nroFacturaMap != null && !nroFacturaMap.isEmpty()) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El numero de factura ya se encuentra registrada.");
                    return mensaje;

                }

                ejCompra.setActivo("S");
                ejCompra.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejCompra.setEmpresa(new Empresa(idEmpresa));
                ejCompra.setNroFactura(nroFactura);
                ejCompra.setTipoCompra("DIRECTA");
                ejCompra.setEstadoCompra(Compra.COMPRA_PENDIENTE);
                ejCompra.setEstadoPago(DocumentoPagar.PENDIENTE);
                ejCompra.setProveedor(new Proveedor(detalleCompra.getCompra().getProveedor().getId()));

                this.save(ejCompra);

                String codDetalle = randomString(5, "DET");

                Vehiculo ejVehiculo = new Vehiculo();

                ejVehiculo.setCodigo(ejCompra.getId() + "-" + codDetalle);
                ejVehiculo.setActivo("S");
                ejVehiculo.setEmpresa(new Empresa(idEmpresa));
                ejVehiculo.setEstado(Vehiculo.PENDIENTE);
                ejVehiculo.setProveedor(new Proveedor(detalleCompra.getCompra().getProveedor().getId()));
                ejVehiculo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setAnho(detalleCompra.getVehiculo().getAnho());
                ejVehiculo.setCaracteristica(detalleCompra.getVehiculo().getCaracteristica());
                ejVehiculo.setColor(detalleCompra.getVehiculo().getColor());
                ejVehiculo.setMarca(new Marca(detalleCompra.getVehiculo().getMarca().getId()));
                ejVehiculo.setModelo(new Modelo(detalleCompra.getVehiculo().getModelo().getId()));
                ejVehiculo.setTipo(new Tipo(detalleCompra.getVehiculo().getTipo().getId()));
                ejVehiculo.setTransmision(detalleCompra.getVehiculo().getTransmision());

                vehiculoManager.save(ejVehiculo);

                Moneda ejMoneda = monedaManager.get(detalleCompra.getMoneda());

                Double cambio = ejMoneda.getValor();

                Long total = Math.round(detalleCompra.getPrecio() * cambio);

                DetalleCompra ejDetCompra = new DetalleCompra();
                ejDetCompra.setCambioDia(cambio);
                ejDetCompra.setActivo("S");
                ejDetCompra.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejDetCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejDetCompra.setEmpresa(new Empresa(idEmpresa));
                ejDetCompra.setVehiculo(ejVehiculo);
                ejDetCompra.setMoneda(detalleCompra.getMoneda());
                ejDetCompra.setCambioDia(detalleCompra.getCambioDia());
                ejDetCompra.setTotal(Double.parseDouble(total.toString()));
                ejDetCompra.setNeto(Double.parseDouble(total.toString()));
                ejDetCompra.setPrecio(detalleCompra.getPrecio());
                ejDetCompra.setCompra(ejCompra);

                detalleCompraManager.save(ejDetCompra);

                Long totalPedido = Long.parseLong("0");
                DetalleCompra ejDetalle = new DetalleCompra();
                ejDetalle.setCompra(ejCompra);

                List<DetalleCompra> ejDetalleCompra = detalleCompraManager.list(ejDetalle);

                for (DetalleCompra rpm : ejDetalleCompra) {
                    totalPedido = totalPedido + Math.round(rpm.getTotal());
                }

                ejCompra.setMonto(totalPedido.toString());

                this.update(ejCompra);

            } else {
                
                ejCompra = this.get(idCompra);
                
                Compra ejFactura = new Compra();
                ejFactura.setNroFactura(nroFactura);
                Map<String, Object> nroFacturaMap = this.getAtributos(ejFactura, "id".split(","), true, true);

                if (nroFacturaMap != null && !nroFacturaMap.isEmpty()
                        && nroFacturaMap.get("id").toString().compareToIgnoreCase(ejCompra.getId().toString()) != 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El numero de factura ya se encuentra registrada.");
                    return mensaje;

                }          

                Moneda ejMoneda = monedaManager.get(new Moneda(detalleCompra.getMoneda().getId()));

                Double cambio = ejMoneda.getValor();

                Long total = Math.round(detalleCompra.getPrecio() * cambio);

                String codDetalle = randomString(5, "DET");

                Vehiculo ejVehiculo = new Vehiculo();

                ejVehiculo.setCodigo(ejCompra.getId() + "-" + codDetalle);
                ejVehiculo.setActivo("S");
                ejVehiculo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setAnho(detalleCompra.getVehiculo().getAnho());
                ejVehiculo.setCaracteristica(detalleCompra.getVehiculo().getCaracteristica());
                ejVehiculo.setColor(detalleCompra.getVehiculo().getColor());
                ejVehiculo.setMarca(new Marca(detalleCompra.getVehiculo().getMarca().getId()));
                ejVehiculo.setModelo(new Modelo(detalleCompra.getVehiculo().getModelo().getId()));
                ejVehiculo.setTipo(new Tipo(detalleCompra.getVehiculo().getTipo().getId()));
                ejVehiculo.setTransmision(detalleCompra.getVehiculo().getTransmision());

                vehiculoManager.save(ejVehiculo);

                DetalleCompra ejDetCompra = new DetalleCompra();
                ejDetCompra.setCambioDia(cambio);
                ejDetCompra.setActivo("S");
                ejDetCompra.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejDetCompra.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejDetCompra.setEmpresa(new Empresa(idEmpresa));
                ejDetCompra.setVehiculo(ejVehiculo);
                ejDetCompra.setMoneda(ejMoneda);
                 ejDetCompra.setCambioDia(ejMoneda.getValor());
                ejDetCompra.setTotal(Double.parseDouble(total.toString()));
                ejDetCompra.setNeto(Double.parseDouble(total.toString()));
                ejDetCompra.setPrecio(detalleCompra.getPrecio());
                ejDetCompra.setCompra(ejCompra);

                detalleCompraManager.save(ejDetCompra);
                Long totalPedido = Long.parseLong("0");

                DetalleCompra ejDetalle = new DetalleCompra();
                ejDetalle.setCompra(ejCompra);

                List<DetalleCompra> ejDetalleCompra = detalleCompraManager.list(ejDetalle);

                for (DetalleCompra rpm : ejDetalleCompra) {
                    totalPedido = totalPedido + Math.round(rpm.getTotal());
                }

                ejCompra.setMonto(totalPedido.toString());
                ejCompra.setNeto(Double.parseDouble(totalPedido.toString()));

                this.update(ejCompra);
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

            if (compra.getProveedor() == null || compra.getProveedor().getId() == null
                    || compra.getProveedor().getId() != null
                    && compra.getProveedor().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un proovedor para realizar la compra.");
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
            ejCompraUp.setProveedor(new Proveedor(compra.getProveedor().getId()));

            if (compra.getFormaPago() != null
                    && compra.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                ejCompraUp.setCantidadCuotas(compra.getCantidadCuotas());
                ejCompraUp.setPorcentajeInteresCredito(compra.getPorcentajeInteresCredito());
                ejCompraUp.setMoraInteres(compra.getMoraInteres());

                Double interes = Double.parseDouble(compra.getPorcentajeInteresCredito());

                if (compra.getEntrega() != null
                        && compra.getEntrega().compareToIgnoreCase("") != 0) {

                    Double entrega = Double.parseDouble(compra.getEntrega());

                    Double montoInteres = ((Double.parseDouble(ejCompraUp.getMonto()) - entrega) * interes) / 100;

                    Double saldo = (Double.parseDouble(ejCompraUp.getMonto()) - entrega) + montoInteres;

                    Double montoCuota = saldo / compra.getCantidadCuotas();

                    ejCompraUp.setEntrega(entrega + "");
                    ejCompraUp.setMontoInteres(montoInteres + "");
                    ejCompraUp.setSaldo(saldo + "");
                    ejCompraUp.setNeto(saldo);
                    ejCompraUp.setMontoCuotas(montoCuota + "");
                    ejCompraUp.setMoraInteres(compra.getMoraInteres());

                    Date fecha = new Date();

                    if (compra.getCuotaFecha() != null) {
                        fecha = sdfSimple.parse(compra.getCuotaFecha());
                    }

                    ejCompraUp.setFechaCuota(fecha);

                    this.update(ejCompraUp);

                    DocumentoPagar ejAPagar = new DocumentoPagar();
                    ejAPagar.setCompra(new Compra(idCompra));

                    List<DocumentoPagar> aPagar = documentoPagarManager.list(ejAPagar);

                    for (DocumentoPagar rpm : aPagar) {
                        documentoPagarManager.delete(rpm.getId());
                    }

                    ejAPagar = new DocumentoPagar();

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
                                contador++;
                            }

                            date.set(Calendar.DATE, fecha.getDate());

                            ejAPagar.setFecha(date.getTime());

                        } else {

                            Calendar date = Calendar.getInstance();
                            date.set(Calendar.DATE, 5);
                            date.set(Calendar.MONTH, fecha.getMonth() + contador);

                            ejAPagar.setFecha(date.getTime());
                            contador++;
                        }

                        documentoPagarManager.save(ejAPagar);

                    }

                } else {

                    Double montoInteres = ((Double.parseDouble(ejCompraUp.getMonto())) * interes) / 100;

                    Double saldo = (Double.parseDouble(ejCompraUp.getMonto())) + montoInteres;

                    Double montoCuota = saldo / compra.getCantidadCuotas();

                    ejCompraUp.setMontoInteres(montoInteres + "");
                    ejCompraUp.setSaldo(saldo + "");
                    ejCompraUp.setNeto(saldo);
                    ejCompraUp.setMontoCuotas(montoCuota + "");
                    ejCompraUp.setMoraInteres(compra.getMoraInteres());

                    Date fecha = new Date();

                    if (compra.getCuotaFecha() != null) {
                        fecha = sdfSimple.parse(compra.getCuotaFecha());
                    }

                    ejCompraUp.setFechaCuota(fecha);

                    this.update(ejCompraUp);

                    DocumentoPagar ejAPagar = new DocumentoPagar();
                    ejAPagar.setCompra(new Compra(idCompra));

                    List<DocumentoPagar> aPagar = documentoPagarManager.list(ejAPagar);

                    for (DocumentoPagar rpm : aPagar) {
                        documentoPagarManager.delete(rpm.getId());
                    }

                    ejAPagar = new DocumentoPagar();

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
                                contador++;
                            }

                            date.set(Calendar.DATE, fecha.getDate());

                            ejAPagar.setFecha(date.getTime());

                        } else {

                            Calendar date = Calendar.getInstance();
                            date.set(Calendar.DATE, 5);
                            date.set(Calendar.MONTH, fecha.getMonth() + contador);

                            ejAPagar.setFecha(date.getTime());
                            contador++;
                        }

                        documentoPagarManager.save(ejAPagar);

                    }
                }

            } else {

                ejCompraUp.setEntrega("");
                ejCompraUp.setMontoInteres("");
                ejCompraUp.setSaldo("");
                ejCompraUp.setMontoCuotas("");
                ejCompraUp.setFechaCuota(null);
                ejCompraUp.setCantidadCuotas(null);
                ejCompraUp.setMoraInteres("");

                if (compra.getTipoDescuento() != null
                        && compra.getTipoDescuento().compareToIgnoreCase("GENERAL") == 0) {

                    Double interes = Double.parseDouble(compra.getDescuento());

                    Double montoInteres = (Double.parseDouble(ejCompraUp.getMonto()) * interes) / 100;

                    Double saldo = Double.parseDouble(ejCompraUp.getMonto()) - montoInteres;

                    ejCompraUp.setSaldo("");
                    ejCompraUp.setTipoDescuento("GENERAL");
                    ejCompraUp.setDescuento(interes + "");
                    ejCompraUp.setMontoDescuento(montoInteres + "");
                    ejCompraUp.setNeto(saldo);

                } else {
                    ejCompraUp.setNeto(Double.parseDouble(ejCompraUp.getMonto()));
                }

                this.update(ejCompraUp);
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

    String randomString(int len, String variable) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            String rand = AB + variable;
            sb.append(rand.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
