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

/**
 * @author Miguel Ojeda
 *
 */
@Entity
public class Pedido extends Base {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "numero_pedido")
    private String numeroPedido;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @Column(name = "fecha_entrega", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;

    @Column(name = "observacion", nullable = true)
    private String observacion;

    @Column(name = "confirmado")
    private Boolean confirmado;


    @Column(name = "descuento")
    private Double descuento;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "descuento_porcentual", nullable = false)
    private Double descuentoPorcentual;

    @Column(name = "neto", nullable = false)
    private Double neto;

    @Column(name = "total_guaranies", nullable = false)
    private Double totalGuaranies;

    @Column(name = "descuento_guaranies", nullable = false)
    private Double descuentoGuaranies;

    @Column(name = "neto_guaranies", nullable = false)
    private Double netoGuaranies;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    public Pedido() {
    }

    public Pedido(Long id) {
        setId(id);
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the numeroPedido
     */
    public String getNumeroPedido() {
        return numeroPedido;
    }

    /**
     * @param numeroPedido the numeroPedido to set
     */
    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the fechaEntrega
     */
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the observacion
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * @param observacion the observacion to set
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /**
     * @return the confirmado
     */
    public Boolean getConfirmado() {
        return confirmado;
    }

    /**
     * @param confirmado the confirmado to set
     */
    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }

    /**
     * @return the descuento
     */
    public Double getDescuento() {
        return descuento;
    }

    /**
     * @param d the descuento to set
     */
    public void setDescuento(Double d) {
        this.descuento = d;
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
     * @return the descuentoPorcentual
     */
    public Double getDescuentoPorcentual() {
        return descuentoPorcentual;
    }

    /**
     * @param descuentoPorcentual the descuentoPorcentual to set
     */
    public void setDescuentoPorcentual(Double descuentoPorcentual) {
        this.descuentoPorcentual = descuentoPorcentual;
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
     * @return the totalGuaranies
     */
    public Double getTotalGuaranies() {
        return totalGuaranies;
    }

    /**
     * @param totalGuaranies the totalGuaranies to set
     */
    public void setTotalGuaranies(Double totalGuaranies) {
        this.totalGuaranies = totalGuaranies;
    }

    /**
     * @return the descuentosGuaranies
     */
    public Double getDescuentoGuaranies() {
        return descuentoGuaranies;
    }

    /**
     * @param descuentosGuaranies the descuentosGuaranies to set
     */
    public void setDescuentosGuaranies(Double descuentoGuaranies) {
        this.descuentoGuaranies = descuentoGuaranies;
    }

    /**
     * @return the netoGuaranies
     */
    public Double getNetoGuaranies() {
        return netoGuaranies;
    }

    /**
     * @param netoGuaranies the netoGuaranies to set
     */
    public void setNetoGuaranies(Double netoGuaranies) {
        this.netoGuaranies = netoGuaranies;
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


    public void setDescuentoGuaranies(Double descuentoGuaranies) {
        this.descuentoGuaranies = descuentoGuaranies;
    }
}
