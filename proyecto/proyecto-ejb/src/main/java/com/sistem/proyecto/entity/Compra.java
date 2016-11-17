/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Miguel
 */
@Entity
public class Compra extends Base{
    
    public static final String ORDEN_COMPRA = "ORDEN_COMPRA";
    public static final String COMPRA_PENDIENTE = "COMPRA_PENDIENTE";
    public static final String COMPRA_REALIZADA = "COMPRA_REALIZADA";
    
    @Column(name = "nro_factura", nullable = true)
    private String nroFactura;
    
    @Column(name = "fechaCompra")
    private Timestamp fechaCompra;
    
    @Column(name = "tipo_compra")
    private String tipoCompra;
    
    @Column(name = "forma_pago")
    private String formaPago;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "porcentaje_interes_credito")
    private String porcentajeInteresCredito;
    
    @Column(name = "monto_interes")
    private String montoInteres;
    
    @Column(name = "tipo_mora_interes")
    private String tipoMoraInteres;
    
    @Column(name = "mora_interes")
    private String moraInteres;
    
    @Column(name = "cantidad_cuotas")
    private Long cantidadCuotas;
    
    @Column(name = "monto_cuotas")
    private String montoCuotas;
    
    @Column(name = "monto_total_cuotas")
    private String montoTotalCuotas;
    
    @Column(name = "fechaCuota")
    private Timestamp fechaCuota;
    
    @Column(name = "entrega")
    private String entrega;
    
    @Column(name = "saldo")
    private String saldo;
    
    @Column(name = "tipo_descuento")
    private String tipoDescuento;
    
    @Column(name = "descuento")
    private String descuento;
    
    @Column(name = "monto")
    private String monto;
    
    @Column(name = "monto_descuento")
    private String montoDescuento;
    
    @Column(name = "neto")
    private Double neto;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "pedido")
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Column(name = "estado")
    private String estadoCompra;
    
    @Transient
    private String cuotaFecha;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compra")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<DetalleCompra> detalleCompraCollection;

    
    public Compra() {
    }

    public Compra(Long id) {
        setId(id);
    }

    
    /**
     * @return the nroFactura
     */
    public String getNroFactura() {
        return nroFactura;
    }

    /**
     * @param nroFactura the nroFactura to set
     */
    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    /**
     * @return the fechaCompra
     */
    public Timestamp getFechaCompra() {
        return fechaCompra;
    }

    /**
     * @param fechaCompra the fechaCompra to set
     */
    public void setFechaCompra(Timestamp fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    /**
     * @return the tipoCompra
     */
    public String getTipoCompra() {
        return tipoCompra;
    }

    /**
     * @param tipoCompra the tipoCompra to set
     */
    public void setTipoCompra(String tipoCompra) {
        this.tipoCompra = tipoCompra;
    }

    /**
     * @return the formaPago
     */
    public String getFormaPago() {
        return formaPago;
    }

    /**
     * @param formaPago the formaPago to set
     */
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPorcentajeInteresCredito() {
        return porcentajeInteresCredito;
    }

    public void setPorcentajeInteresCredito(String porcentajeInteresCredito) {
        this.porcentajeInteresCredito = porcentajeInteresCredito;
    }

    

    /**
     * @return the montoInteres
     */
    public String getMontoInteres() {
        return montoInteres;
    }

    /**
     * @param montoInteres the montoInteres to set
     */
    public void setMontoInteres(String montoInteres) {
        this.montoInteres = montoInteres;
    }

    /**
     * @return the tipoMoraInteres
     */
    public String getTipoMoraInteres() {
        return tipoMoraInteres;
    }

    /**
     * @param tipoMoraInteres the tipoMoraInteres to set
     */
    public void setTipoMoraInteres(String tipoMoraInteres) {
        this.tipoMoraInteres = tipoMoraInteres;
    }

    /**
     * @return the moraInteres
     */
    public String getMoraInteres() {
        return moraInteres;
    }

    /**
     * @param moraInteres the moraInteres to set
     */
    public void setMoraInteres(String moraInteres) {
        this.moraInteres = moraInteres;
    }

    /**
     * @return the cantidadCuotas
     */
    public Long getCantidadCuotas() {
        return cantidadCuotas;
    }

    /**
     * @param cantidadCuotas the cantidadCuotas to set
     */
    public void setCantidadCuotas(Long cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    /**
     * @return the montoCuotas
     */
    public String getMontoCuotas() {
        return montoCuotas;
    }

    /**
     * @param montoCuotas the montoCuotas to set
     */
    public void setMontoCuotas(String montoCuotas) {
        this.montoCuotas = montoCuotas;
    }

    /**
     * @return the montoTotalCuotas
     */
    public String getMontoTotalCuotas() {
        return montoTotalCuotas;
    }

    /**
     * @param montoTotalCuotas the montoTotalCuotas to set
     */
    public void setMontoTotalCuotas(String montoTotalCuotas) {
        this.montoTotalCuotas = montoTotalCuotas;
    }

    /**
     * @return the fechaCuota
     */
    public Timestamp getFechaCuota() {
        return fechaCuota;
    }

    /**
     * @param fechaCuota the fechaCuota to set
     */
    public void setFechaCuota(Timestamp fechaCuota) {
        this.fechaCuota = fechaCuota;
    }

    /**
     * @return the entrega
     */
    public String getEntrega() {
        return entrega;
    }

    /**
     * @param entrega the entrega to set
     */
    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    /**
     * @return the saldo
     */
    public String getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the descuento
     */
    public String getDescuento() {
        return descuento;
    }

    /**
     * @param descuento the descuento to set
     */
    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    /**
     * @return the monto
     */
    public String getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }

    /**
     * @return the montoDescuento
     */
    public String getMontoDescuento() {
        return montoDescuento;
    }

    /**
     * @param montoDescuento the montoDescuento to set
     */
    public void setMontoDescuento(String montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    /**
     * @return the neto
     */
    public Double getNeto() {
        return neto;
    }

    /**
     * @param neto the neto to set
     */
    public void setNeto(Double neto) {
        this.neto = neto;
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the tipoDescuento
     */
    public String getTipoDescuento() {
        return tipoDescuento;
    }

    /**
     * @param tipoDescuento the tipoDescuento to set
     */
    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    /**
     * @return the detalleCompraCollection
     */
    public Collection<DetalleCompra> getDetalleCompraCollection() {
        return detalleCompraCollection;
    }

    /**
     * @param detalleCompraCollection the detalleCompraCollection to set
     */
    public void setDetalleCompraCollection(Collection<DetalleCompra> detalleCompraCollection) {
        this.detalleCompraCollection = detalleCompraCollection;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * @return the cuotaFecha
     */
    public String getCuotaFecha() {
        return cuotaFecha;
    }

    /**
     * @param cuotaFecha the cuotaFecha to set
     */
    public void setCuotaFecha(String cuotaFecha) {
        this.cuotaFecha = cuotaFecha;
    }
    
    
     
}
