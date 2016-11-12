/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class DetalleCompra extends Base {    
    
    private static final long serialVersionUID = 798618560888L;   
    
    
    @Column(name = "precio")
    private Double precio;
    
    @Column(name = "porcentaje_descuento")
    private String porcentajeDescuento;
    
    @Column(name = "monto_descuento")
    private Double montoDescuento;
    
    @Column(name = "neto")
    private Double neto;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda;
    
    @Column(name = "cambio_dia")
    private Double cambioDia;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;   
    
    @ManyToOne
    @JoinColumn(name = "pedido")
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "vehiculo")
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    public DetalleCompra() {

    }

    /**
     * @param id
     *            el id de Rol
     */
    public DetalleCompra(Long id) {
            super(id);
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @return the porcentajeDescuento
     */
    public String getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    /**
     * @param porcentajeDescuento the porcentajeDescuento to set
     */
    public void setPorcentajeDescuento(String porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * @return the MontoDescuento
     */
    public Double getMontoDescuento() {
        return montoDescuento;
    }

    /**
     * @param montoDescuento the MontoDescuento to set
     */
    public void setMontoDescuento(Double montoDescuento) {
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
     * @return the moneda
     */
    public Moneda getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the cambioDia
     */
    public Double getCambioDia() {
        return cambioDia;
    }

    /**
     * @param cambioDia the cambioDia to set
     */
    public void setCambioDia(Double cambioDia) {
        this.cambioDia = cambioDia;
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
     * @return the pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * @return the vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * @param vehiculo the vehiculo to set
     */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * @return the compra
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    
}
