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

/**
 *
 * @author Miguel
 */
@Entity
public class DetallePedido extends Base {
    
    public static final String APROBADO = "APROBADO";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String RECHAZADO = "RECHAZADO";
    
    private static final long serialVersionUID = 798618560888L;
    
    
    @Column(name = "estado_pedido")
    private String estadoPedido;

    
    @NotNull
    @Column(name = "precio")
    private Double precio;
    
    
    @Column(name = "cambio_dia")
    private Double cambioDia;
    
    @NotNull
    @Column(name = "neto")
    private Double neto;
    
    @NotNull
    @Column(name = "total")
    private Double total;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda; 
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String fecha;
    
    @ManyToOne
    @JoinColumn(name = "vehiculo")
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "pedido")
    private Pedido pedido;
    
    public DetallePedido() {

    }

    /**
     * @param id
     *            el id de Rol
     */
    public DetallePedido(Long id) {
            super(id);
    }

    /**
     * @return the estadoPedido
     */
    public String getEstadoPedido() {
        return estadoPedido;
    }

    /**
     * @param estadoPedido the estadoPedido to set
     */
    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
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
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
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
     * @return the cotizacion
     */
    public Double getNeto() {
        return neto;
    }

    /**
     * @param cotizacion the cotizacion to set
     */
    public void setNeto(Double neto) {
        this.neto = neto;
    }

    
}
