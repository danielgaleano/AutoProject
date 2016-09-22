package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Miguel Ojeda
 *
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"empresa", "nombre"})})
public class Moneda extends Base {

    /**
     *
     */
    private static final long serialVersionUID = 15465L;

    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    @Column(name = "simbolo", nullable = false, length = 16)
    private String simbolo;

    @Column(name = "valor", nullable = true)
    private String valor;

    @Column(name = "por_defecto")
    private Boolean porDefecto;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    public Moneda() {
    }

    public Moneda(Long id) {
        setId(id);
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

    public Boolean getPorDefecto() {
        return porDefecto;
    }

    public void setPorDefecto(Boolean porDefecto) {
        this.porDefecto = porDefecto;
    }

    /**
     * @return the simbolo
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * @param simbolo the simbolo to set
     */
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return la empresa de Moneda
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa la empresa de MOneda a setear
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
