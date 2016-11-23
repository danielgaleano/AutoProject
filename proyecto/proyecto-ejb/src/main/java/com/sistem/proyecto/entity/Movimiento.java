/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class Movimiento extends Base {
    
    private static long serialVersionUID = -959605420407250259L;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "caja")
    private Caja caja;
    
    @Column(name = "fecha_ingreso")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngreso;
    
    @NotNull
    @NotEmpty
    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda;
    
    @Column(name = "cotizacion")
    private Double cotizacion;
    
    @Column(name = "importe")
    private Double importe;
    
    @Column(name = "saldo")
    private Double saldo;
    
    @Column(name = "vuelto")
    private Double vuelto;
    
    @Column(name = "fecha_vencimiento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    
    @Column(name = "cuota")
    private Long cuota;
    
    @Column(name = "interes")
    private Double interes;
    
    public Movimiento() {

    }

    public Movimiento(Long id) {
            this.setId(id);
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
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
     * @return the caja
     */
    public Caja getCaja() {
        return caja;
    }

    /**
     * @param caja the caja to set
     */
    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    /**
     * @return the fechaIngreso
     */
    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * @param fechaIngreso the fechaIngreso to set
     */
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * @return the tipoTransaccion
     */
    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    /**
     * @param tipoTransaccion the tipoTransaccion to set
     */
    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
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
     * @return the cotizacion
     */
    public Double getCotizacion() {
        return cotizacion;
    }

    /**
     * @param cotizacion the cotizacion to set
     */
    public void setCotizacion(Double cotizacion) {
        this.cotizacion = cotizacion;
    }

    /**
     * @return the importe
     */
    public Double getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(Double importe) {
        this.importe = importe;
    }

    /**
     * @return the saldo
     */
    public Double getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the vuelto
     */
    public Double getVuelto() {
        return vuelto;
    }

    /**
     * @param vuelto the vuelto to set
     */
    public void setVuelto(Double vuelto) {
        this.vuelto = vuelto;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the cuota
     */
    public Long getCuota() {
        return cuota;
    }

    /**
     * @param cuota the cuota to set
     */
    public void setCuota(Long cuota) {
        this.cuota = cuota;
    }

    /**
     * @return the interes
     */
    public Double getInteres() {
        return interes;
    }

    /**
     * @param interes the interes to set
     */
    public void setInteres(Double interes) {
        this.interes = interes;
    }
    
    
}
