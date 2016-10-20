package com.sistem.proyecto.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author daniel
 *
 */
@Entity
public class Venta extends Base {

    private static final long serialVersionUID = 1L;

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
            
    @Column(name = "cantidas_total")
    private Long cantidadTotal;        

    @Column(name = "total", nullable = true)
    private Double total;

    @Column(name = "neto", nullable = true)
    private Double neto;
    
    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente ;
    
    @OneToMany(mappedBy="venta")
    private List<DetalleVenta> detalleVenta;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String fecha;

    public Venta() {
    }

    public Venta(Long id) {
        setId(id);
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
     * @return the cantidadTotal
     */
    public Long getCantidadTotal() {
        return cantidadTotal;
    }

    /**
     * @param cantidadTotal the cantidadTotal to set
     */
    public void setCantidadTotal(Long cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    
    public List<DetalleVenta> getDetalle() {
        return detalleVenta;
    }

    public void setDetalle(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
}

