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
public class DetallePedido extends Base {
    
    public static final String APROBADO = "APROBADO";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String RECHAZADO = "RECHAZADO";
    
    private static final long serialVersionUID = 798618560888L;
    
    @Column(name = "codigo_detalle")
    private String codigoDetalle;
    
    @Column(name = "estado_pedido")
    private String estadoPedido;
    
    @Column(name = "caracteristica")
    private String caracteristica;
    
    @NotNull
    @NotEmpty
    @Column(name = "trasmision")
    private String trasmision;
    
    @NotNull
    @NotEmpty
    @Column(name = "color")
    private String color;
    
    @NotNull
    @NotEmpty
    @Column(name = "anho")
    private String anho;
    
    @NotNull
    @Column(name = "cantidad")
    private Long cantidad;
    
    @NotNull
    @Column(name = "precio")
    private Double precio;
    
    
    @Column(name = "cambio_dia")
    private Double cambioDia;
    
    @NotNull
    @Column(name = "total")
    private Double total;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    private Tipo tipo;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "marca", referencedColumnName = "id")
    private Marca marca;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    private Modelo modelo;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda; 
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String fecha;
    
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
     * @return the caracteristica
     */
    public String getCaracteristica() {
        return caracteristica;
    }

    /**
     * @param caracteristica the caracteristica to set
     */
    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    /**
     * @return the trasmision
     */
    public String getTrasmision() {
        return trasmision;
    }

    /**
     * @param trasmision the trasmision to set
     */
    public void setTrasmision(String trasmision) {
        this.trasmision = trasmision;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the cantidad
     */
    public Long getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

   
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
     * @return the anho
     */
    public String getAnho() {
        return anho;
    }

    /**
     * @param anho the anho to set
     */
    public void setAnho(String anho) {
        this.anho = anho;
    }

    /**
     * @return the tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
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
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
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
     * @return the marca
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getCodigoDetalle() {
        return codigoDetalle;
    }

    public void setCodigoDetalle(String codigoDetalle) {
        this.codigoDetalle = codigoDetalle;
    }

    /**
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    
}
