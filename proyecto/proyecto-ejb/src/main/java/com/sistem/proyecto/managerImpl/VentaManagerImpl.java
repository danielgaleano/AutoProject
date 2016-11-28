/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.DocumentoCobrar;
import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Marca;
import com.sistem.proyecto.entity.Modelo;
import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.entity.Tipo;
import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.DetalleVentaManager;
import com.sistem.proyecto.manager.DocumentoCobrarManager;
import com.sistem.proyecto.manager.MonedaManager;
import com.sistem.proyecto.manager.VehiculoManager;
import com.sistem.proyecto.manager.VentaManager;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class VentaManagerImpl extends GenericDaoImpl<Venta, Long>
		implements VentaManager{

    @EJB(mappedName = "java:app/proyecto-ejb/VehiculoManagerImpl")
    private VehiculoManager vehiculoManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DocumentoCobrarManagerImpl")
    private DocumentoCobrarManager documentoCobrarManager;

    @EJB(mappedName = "java:app/proyecto-ejb/DetalleVentaManagerImpl")
    private DetalleVentaManager detalleVentaManager;

    @EJB(mappedName = "java:app/proyecto-ejb/MonedaManagerImpl")
    private MonedaManager monedaManager;

    protected SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSSSSS");
    protected SimpleDateFormat sdfSimple = new SimpleDateFormat("yyyy-MM-dd");

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Override
    protected Class<Venta> getEntityBeanType() {
        return Venta.class;
    }

    @Override
    public MensajeDTO guardarVenta(Long idVenta, DetalleVenta detalleVenta, String nroFactura, String formaPgo, String tipoDescuento, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Venta ejVenta = new Venta();
        try {

            if (nroFactura == null || nroFactura != null
                    && nroFactura.compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getTipo() == null || detalleVenta.getVehiculo().getTipo().getId() != null
                    && detalleVenta.getVehiculo().getTipo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El tipo de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getMarca() == null || detalleVenta.getVehiculo().getMarca().getId() != null
                    && detalleVenta.getVehiculo().getMarca().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La marca de vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getModelo() == null || detalleVenta.getVehiculo().getModelo().getId() != null
                    && detalleVenta.getVehiculo().getModelo().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El modelo del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getAnho() == null || detalleVenta.getVehiculo().getAnho() != null
                    && detalleVenta.getVehiculo().getAnho().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El año del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getColor() == null || detalleVenta.getVehiculo().getColor() != null
                    && detalleVenta.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getColor() == null || detalleVenta.getVehiculo().getColor() != null
                    && detalleVenta.getVehiculo().getColor().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El color del vehiculo no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVehiculo().getTransmision() == null || detalleVenta.getVehiculo().getTransmision() != null
                    && detalleVenta.getVehiculo().getTransmision().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("La transmision del vehiculo no puede estar vacia.");
                return mensaje;
            }

            if (detalleVenta.getMoneda() == null || detalleVenta.getMoneda().getId() != null
                    && detalleVenta.getMoneda().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo moneda no puede estar vacia.");
                return mensaje;
            }

            if (detalleVenta.getPrecio() == null || detalleVenta.getPrecio() != null
                    && detalleVenta.getPrecio().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El campo precio no puede estar vacio.");
                return mensaje;
            }

            if (detalleVenta.getVenta().getCliente() == null || detalleVenta.getVenta().getCliente().getId() == null
                    || detalleVenta.getVenta().getCliente().getId() != null
                    && detalleVenta.getVenta().getCliente().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un proovedor para realizar la venta.");
                return mensaje;
            }

            if (idVenta == null || idVenta != null
                    && idVenta.toString().compareToIgnoreCase("") == 0) {

                Venta ejFactura = new Venta();
                ejFactura.setNroFactura(nroFactura);

                Map<String, Object> nroFacturaMap = this.getAtributos(ejFactura, "id".split(","), true, true);

                if (nroFacturaMap != null && !nroFacturaMap.isEmpty()) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El numero de factura ya se encuentra registrada.");
                    return mensaje;

                }

                ejVenta.setActivo("S");
                ejVenta.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejVenta.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejVenta.setEmpresa(new Empresa(idEmpresa));
                ejVenta.setNroFactura(nroFactura);
                ejVenta.setTipoVenta("DIRECTA");
                ejVenta.setEstadoVenta(Venta.VENTA_PENDIENTE);
                ejVenta.setCliente(new Cliente(detalleVenta.getVenta().getCliente().getId()));

                this.save(ejVenta);

                String codDetalle = randomString(5, "DET");

                Vehiculo ejVehiculo = new Vehiculo();

                ejVehiculo.setCodigo(ejVenta.getId() + "-" + codDetalle);
                ejVehiculo.setActivo("S");
                ejVehiculo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setAnho(detalleVenta.getVehiculo().getAnho());
                ejVehiculo.setCaracteristica(detalleVenta.getVehiculo().getCaracteristica());
                ejVehiculo.setColor(detalleVenta.getVehiculo().getColor());
                ejVehiculo.setMarca(new Marca(detalleVenta.getVehiculo().getMarca().getId()));
                ejVehiculo.setModelo(new Modelo(detalleVenta.getVehiculo().getModelo().getId()));
                ejVehiculo.setTipo(new Tipo(detalleVenta.getVehiculo().getTipo().getId()));
                ejVehiculo.setTransmision(detalleVenta.getVehiculo().getTransmision());

                vehiculoManager.save(ejVehiculo);

                Moneda ejMoneda = monedaManager.get(detalleVenta.getMoneda());

                Double cambio = ejMoneda.getValor();

                Long total = Math.round(detalleVenta.getPrecio() * cambio);

                DetalleVenta ejDetVenta = new DetalleVenta();
                ejDetVenta.setCambioDia(cambio);
                ejDetVenta.setActivo("S");
                ejDetVenta.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejDetVenta.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejDetVenta.setEmpresa(new Empresa(idEmpresa));
                ejDetVenta.setVehiculo(ejVehiculo);
                ejDetVenta.setMoneda(detalleVenta.getMoneda());
                ejDetVenta.setCambioDia(detalleVenta.getCambioDia());
                ejDetVenta.setTotal(Double.parseDouble(total.toString()));
                ejDetVenta.setNeto(Double.parseDouble(total.toString()));
                ejDetVenta.setPrecio(detalleVenta.getPrecio());
                ejDetVenta.setVenta(ejVenta);

                detalleVentaManager.save(ejDetVenta);

                Long totalPedido = Long.parseLong("0");
                DetalleVenta ejDetalle = new DetalleVenta();
                ejDetalle.setVenta(ejVenta);

                List<DetalleVenta> ejDetalleVenta = detalleVentaManager.list(ejDetalle);

                for (DetalleVenta rpm : ejDetalleVenta) {
                    totalPedido = totalPedido + Math.round(rpm.getTotal());
                }

                ejVenta.setMonto(totalPedido.toString());

                this.update(ejVenta);

            } else {
                
                ejVenta = this.get(idVenta);
                
                Venta ejFactura = new Venta();
                ejFactura.setNroFactura(nroFactura);
                Map<String, Object> nroFacturaMap = this.getAtributos(ejFactura, "id".split(","), true, true);

                if (nroFacturaMap != null && !nroFacturaMap.isEmpty()
                        && nroFacturaMap.get("id").toString().compareToIgnoreCase(ejVenta.getId().toString()) != 0) {
                    mensaje.setError(true);
                    mensaje.setMensaje("El numero de factura ya se encuentra registrada.");
                    return mensaje;

                }          

                Moneda ejMoneda = monedaManager.get(new Moneda(detalleVenta.getMoneda().getId()));

                Double cambio = ejMoneda.getValor();

                Long total = Math.round(detalleVenta.getPrecio() * cambio);

                String codDetalle = randomString(5, "DET");

                Vehiculo ejVehiculo = new Vehiculo();

                ejVehiculo.setCodigo(ejVenta.getId() + "-" + codDetalle);
                ejVehiculo.setActivo("S");
                ejVehiculo.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejVehiculo.setAnho(detalleVenta.getVehiculo().getAnho());
                ejVehiculo.setCaracteristica(detalleVenta.getVehiculo().getCaracteristica());
                ejVehiculo.setColor(detalleVenta.getVehiculo().getColor());
                ejVehiculo.setMarca(new Marca(detalleVenta.getVehiculo().getMarca().getId()));
                ejVehiculo.setModelo(new Modelo(detalleVenta.getVehiculo().getModelo().getId()));
                ejVehiculo.setTipo(new Tipo(detalleVenta.getVehiculo().getTipo().getId()));
                ejVehiculo.setTransmision(detalleVenta.getVehiculo().getTransmision());

                vehiculoManager.save(ejVehiculo);

                DetalleVenta ejDetVenta = new DetalleVenta();
                ejDetVenta.setCambioDia(cambio);
                ejDetVenta.setActivo("S");
                ejDetVenta.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
                ejDetVenta.setFechaModificacion(new Timestamp(System.currentTimeMillis()));
                ejDetVenta.setEmpresa(new Empresa(idEmpresa));
                ejDetVenta.setVehiculo(ejVehiculo);
                ejDetVenta.setMoneda(ejMoneda);
                 ejDetVenta.setCambioDia(ejMoneda.getValor());
                ejDetVenta.setTotal(Double.parseDouble(total.toString()));
                ejDetVenta.setNeto(Double.parseDouble(total.toString()));
                ejDetVenta.setPrecio(detalleVenta.getPrecio());
                ejDetVenta.setVenta(ejVenta);

                detalleVentaManager.save(ejDetVenta);
                Long totalPedido = Long.parseLong("0");

                DetalleVenta ejDetalle = new DetalleVenta();
                ejDetalle.setVenta(ejVenta);

                List<DetalleVenta> ejDetalleVenta = detalleVentaManager.list(ejDetalle);

                for (DetalleVenta rpm : ejDetalleVenta) {
                    totalPedido = totalPedido + Math.round(rpm.getTotal());
                }

                ejVenta.setMonto(totalPedido.toString());
                ejVenta.setNeto(Double.parseDouble(totalPedido.toString()));

                this.update(ejVenta);
            }

            mensaje.setError(false);
            mensaje.setId(ejVenta.getId());
            mensaje.setMensaje("La venta se registro exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error  al guardar la venta.");
            return mensaje;
        }
        return mensaje;

    }

    @Override
    public MensajeDTO editarVenta(Long idVenta, Venta venta, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception {
        MensajeDTO mensaje = new MensajeDTO();
        Venta ejVenta = new Venta();
        try {

            if (idVenta == null || idVenta != null
                    && idVenta.toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar  la venta.");
                return mensaje;
            }

            if (venta.getNroFactura() == null || venta.getNroFactura() != null
                    && venta.getNroFactura().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("El Nro. Factura no puede estar vacio.");
                return mensaje;
            }

            if (venta.getCliente() == null || venta.getCliente().getId() == null
                    || venta.getCliente().getId() != null
                    && venta.getCliente().getId().toString().compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe ingresar un cliente para realizar la venta.");
                return mensaje;
            }

            if (formaPgo == null || formaPgo != null
                    && formaPgo.compareToIgnoreCase("") == 0) {
                mensaje.setError(true);
                mensaje.setMensaje("Debe Ingresar una forma de pago para relizar la venta.");
                return mensaje;
            }

            Venta ejVentaUp = this.get(idVenta);
            ejVentaUp.setNroFactura(venta.getNroFactura());
            ejVentaUp.setFormaPago(venta.getFormaPago());
            ejVentaUp.setCliente(new Cliente(venta.getCliente().getId()));

            if (venta.getFormaPago() != null
                    && venta.getFormaPago().compareToIgnoreCase("CREDITO") == 0) {

                ejVentaUp.setCantidadCuotas(venta.getCantidadCuotas());
                ejVentaUp.setPorcentajeInteresCredito(venta.getPorcentajeInteresCredito());
                ejVentaUp.setMoraInteres(venta.getMoraInteres());

                Double interes = Double.parseDouble(venta.getPorcentajeInteresCredito());

                if (venta.getEntrega() != null
                        && venta.getEntrega().compareToIgnoreCase("") != 0) {

                    Double entrega = Double.parseDouble(venta.getEntrega());

                    Double montoInteres = ((Double.parseDouble(ejVentaUp.getMonto()) - entrega) * interes) / 100;

                    Double saldo = (Double.parseDouble(ejVentaUp.getMonto()) - entrega) + montoInteres;

                    Double montoCuota = saldo / venta.getCantidadCuotas();

                    ejVentaUp.setEntrega(entrega + "");
                    ejVentaUp.setMontoInteres(montoInteres + "");
                    ejVentaUp.setSaldo(saldo + "");
                    ejVentaUp.setNeto(saldo);
                    ejVentaUp.setMontoCuotas(montoCuota + "");
                    ejVentaUp.setMoraInteres(venta.getMoraInteres());

                    Date fecha = new Date();

                    if (venta.getCuotaFecha() != null) {
                        fecha = sdfSimple.parse(venta.getCuotaFecha());
                    }

                    ejVentaUp.setFechaCuota(fecha);

                    this.update(ejVentaUp);

                    DocumentoCobrar ejACobrar = new DocumentoCobrar();
                    ejACobrar.setVenta(new Venta(idVenta));

                    List<DocumentoCobrar> aCobrar = documentoCobrarManager.list(ejACobrar);

                    for (DocumentoCobrar rpm : aCobrar) {
                        documentoCobrarManager.delete(rpm.getId());
                    }

                    ejACobrar = new DocumentoCobrar();

                    int contador = 1;
                    boolean tieneFecha = true;
                    for (int i = 1; i <= venta.getCantidadCuotas(); i++) {

                        ejACobrar = new DocumentoCobrar();
                        ejACobrar.setVenta(ejVentaUp);
                        ejACobrar.setActivo("S");
                        ejACobrar.setNroCuota(i + "");
                        ejACobrar.setMonto(montoCuota);
                        ejACobrar.setEstado(DocumentoCobrar.PENDIENTE);

                        if (venta.getCuotaFecha() != null
                                && venta.getCuotaFecha().compareToIgnoreCase("") != 0) {

                            Calendar date = Calendar.getInstance();
                            if (tieneFecha) {
                                date.set(Calendar.MONTH, fecha.getMonth());
                                tieneFecha = false;

                            } else {
                                date.set(Calendar.MONTH, fecha.getMonth() + contador);
                                contador++;
                            }

                            date.set(Calendar.DATE, fecha.getDate());

                            ejACobrar.setFecha(date.getTime());

                        } else {

                            Calendar date = Calendar.getInstance();
                            date.set(Calendar.DATE, 5);
                            date.set(Calendar.MONTH, fecha.getMonth() + contador);

                            ejACobrar.setFecha(date.getTime());
                            contador++;
                        }

                        documentoCobrarManager.save(ejACobrar);

                    }

                } else {

                    Double montoInteres = ((Double.parseDouble(ejVentaUp.getMonto())) * interes) / 100;

                    Double saldo = (Double.parseDouble(ejVentaUp.getMonto())) + montoInteres;

                    Double montoCuota = saldo / venta.getCantidadCuotas();

                    ejVentaUp.setMontoInteres(montoInteres + "");
                    ejVentaUp.setSaldo(saldo + "");
                    ejVentaUp.setNeto(saldo);
                    ejVentaUp.setMontoCuotas(montoCuota + "");
                    ejVentaUp.setMoraInteres(venta.getMoraInteres());

                    Date fecha = new Date();

                    if (venta.getCuotaFecha() != null) {
                        fecha = sdfSimple.parse(venta.getCuotaFecha());
                    }

                    ejVentaUp.setFechaCuota(fecha);

                    this.update(ejVentaUp);

                    DocumentoCobrar ejACobrar = new DocumentoCobrar();
                    ejACobrar.setVenta(new Venta(idVenta));

                    List<DocumentoCobrar> aCobrar = documentoCobrarManager.list(ejACobrar);

                    for (DocumentoCobrar rpm : aCobrar) {
                        documentoCobrarManager.delete(rpm.getId());
                    }

                    ejACobrar = new DocumentoCobrar();

                    int contador = 1;
                    boolean tieneFecha = true;

                    for (int i = 1; i <= venta.getCantidadCuotas(); i++) {

                        ejACobrar = new DocumentoCobrar();
                        ejACobrar.setVenta(ejVentaUp);
                        ejACobrar.setActivo("S");
                        ejACobrar.setNroCuota(i + "");
                        ejACobrar.setMonto(montoCuota);
                        ejACobrar.setEstado(DocumentoCobrar.PENDIENTE);

                        if (venta.getCuotaFecha() != null
                                && venta.getCuotaFecha().compareToIgnoreCase("") != 0) {

                            Calendar date = Calendar.getInstance();
                            if (tieneFecha) {
                                date.set(Calendar.MONTH, fecha.getMonth());
                                tieneFecha = false;

                            } else {
                                date.set(Calendar.MONTH, fecha.getMonth() + contador);
                                contador++;
                            }

                            date.set(Calendar.DATE, fecha.getDate());

                            ejACobrar.setFecha(date.getTime());

                        } else {

                            Calendar date = Calendar.getInstance();
                            date.set(Calendar.DATE, 5);
                            date.set(Calendar.MONTH, fecha.getMonth() + contador);

                            ejACobrar.setFecha(date.getTime());
                            contador++;
                        }

                        documentoCobrarManager.save(ejACobrar);

                    }
                }

            } else {

                ejVentaUp.setEntrega("");
                ejVentaUp.setMontoInteres("");
                ejVentaUp.setSaldo("");
                ejVentaUp.setMontoCuotas("");
                ejVentaUp.setFechaCuota(null);
                ejVentaUp.setCantidadCuotas(null);
                ejVentaUp.setMoraInteres("");

                if (venta.getTipoDescuento() != null
                        && venta.getTipoDescuento().compareToIgnoreCase("GENERAL") == 0) {

                    Double interes = Double.parseDouble(venta.getDescuento());

                    Double montoInteres = (Double.parseDouble(ejVentaUp.getMonto()) * interes) / 100;

                    Double saldo = Double.parseDouble(ejVentaUp.getMonto()) - montoInteres;

                    ejVentaUp.setSaldo("");
                    ejVentaUp.setDescuento(interes + "");
                    ejVentaUp.setMontoDescuento(montoInteres + "");
                    ejVentaUp.setNeto(saldo);

                } else {
                    ejVentaUp.setNeto(Double.parseDouble(ejVentaUp.getMonto()));
                }

                this.update(ejVentaUp);
            }

            mensaje.setError(false);
            mensaje.setId(ejVenta.getId());
            mensaje.setMensaje("La venta se registro exitosamente.");

        } catch (Exception e) {
            mensaje.setError(true);
            mensaje.setMensaje("Error  al guardar la venta.");
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
