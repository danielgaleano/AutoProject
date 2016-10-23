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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author Miguel
 */
@Entity
public class Compra extends Base{
    
    @Column(name = "nro_factura", nullable = false)
    private Long nroFactura;
    
    @Column(name = "fechaCompra")
    private Timestamp fechaCompra;
    
    @Column(name = "tipo_compra")
    private String tipoCompra;
    
    @Column(name = "forma_pago")
    private String formaPago;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "porcentaje_interes")
    private String porcentajeInteres;
    
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
    private String neto;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compra")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<DetalleCompra> detalleCompraCollection;

    /**
     * @return the nroFactura
     */
    public Long getNroFactura() {
        return nroFactura;
    }

    /**
     * @param nroFactura the nroFactura to set
     */
    public void setNroFactura(Long nroFactura) {
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

    /**
     * @return the porcentajeInteres
     */
    public String getPorcentajeInteres() {
        return porcentajeInteres;
    }

    /**
     * @param porcentajeInteres the porcentajeInteres to set
     */
    public void setPorcentajeInteres(String porcentajeInteres) {
        this.porcentajeInteres = porcentajeInteres;
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
    public String getNeto() {
        return neto;
    }

    /**
     * @param neto the neto to set
     */
    public void setNeto(String neto) {
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
     
}
