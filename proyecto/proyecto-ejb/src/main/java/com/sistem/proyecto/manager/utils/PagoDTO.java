/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager.utils;

import java.util.Date;

/**
 *
 * @author Miguel
 */
public class PagoDTO {
    
    private Boolean cancelado;
    
    private Long idCompra;
    
    private Long idProveedor;
    
    private Long docPagar;
    
    private String nroFactura;
    
    private String formaPago;
    
    private Long cantidadCuotas;
    
    private String monto;
    
    private String montoTotalCuotas;
    
    private String cuota;
    
    private Double neto;
    
    private Double importePagar;
    
    private Double interes;
    
    private Double saldo;
    
    private Date fechaCuota;
    
    private String nombre;
    
    private String documento;
    
    private String direccion;
    
    private String telefono;
    

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getDocPagar() {
        return docPagar;
    }

    public void setDocPagar(Long docPagar) {
        this.docPagar = docPagar;
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

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
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
     * @return the cuota
     */
    public String getCuota() {
        return cuota;
    }

    /**
     * @param cuota the cuota to set
     */
    public void setCuota(String cuota) {
        this.cuota = cuota;
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
     * @return the fechaCuota
     */
    public Date getFechaCuota() {
        return fechaCuota;
    }

    /**
     * @param fechaCuota the fechaCuota to set
     */
    public void setFechaCuota(Date fechaCuota) {
        this.fechaCuota = fechaCuota;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getImportePagar() {
        return importePagar;
    }

    public void setImportePagar(Double importePagar) {
        this.importePagar = importePagar;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Double getInteres() {
        return interes;
    }

    public void setInteres(Double interes) {
        this.interes = interes;
    }

    public Boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }
    
    
}
