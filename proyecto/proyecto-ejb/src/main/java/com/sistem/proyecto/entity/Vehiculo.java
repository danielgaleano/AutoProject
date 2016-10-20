package com.sistem.proyecto.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Miguel Ojeda
 *
 */
@Entity
public class Vehiculo extends Base {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo")
    private Modelo modelo;

    @NotNull
    @NotEmpty
    @Column(name = "transmision")
    private String transmision;

    @NotNull
    @NotEmpty
    @Column(name = "color")
    private String color;

    @NotNull
    @NotEmpty
    @Column(name = "anho")
    private String anho;
    
    @Column(name = "caracteristica")
    private String caracteristica;

    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    public Vehiculo() {
    }

    public Vehiculo(Long id) {
        setId(id);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
     public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }
    
     public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }
    
    /**
     * @return la empresa del pedido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa la empresa del pedido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    

}
