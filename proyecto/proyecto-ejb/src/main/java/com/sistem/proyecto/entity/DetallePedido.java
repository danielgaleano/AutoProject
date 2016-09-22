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
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class DetallePedido extends Base {
    
    private static final long serialVersionUID = 798618560888L;
    
    @NotNull
    @NotEmpty
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
    
    @NotNull
    @Column(name = "total")
    private Double total;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    private Tipo tipo;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
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
     * @return the tipoVehiculo
     */
    public Tipo getTipoVehiculo() {
        return tipo;
    }

    /**
     * @param tipo the tipoVehiculo to set
     */
    public void setTipoVehiculo(Tipo tipo) {
        this.tipo = tipo;
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
    
    
    
}
