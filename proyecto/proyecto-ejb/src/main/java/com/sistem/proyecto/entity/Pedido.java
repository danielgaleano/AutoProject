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
    
    @Column(name = "cantidas_aprobados")
    private Long cantidadAprobados;
            
    @Column(name = "cantidas_total")
    private Long cantidadTotal;        

    @Column(name = "total", nullable = true)
    private Double total;

    @Column(name = "neto", nullable = true)
    private Double neto;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor ;
    
//    @ManyToOne
//    @JoinColumn(name = "detalle_pedido")
//    private DetallePedido detallePedido ;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String fecha;

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
  
//    /**
//     * @return the detallePedido
//     */
//    public DetallePedido getDetallePedido() {
//        return detallePedido;
//    }
//
//    /**
//     * @param detallePedido the detallePedido to set
//     */
//    public void setDetallePedido(DetallePedido detallePedido) {
//        this.detallePedido = detallePedido;
//    }

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
     * @return the cantidadAprobados
     */
    public Long getCantidadAprobados() {
        return cantidadAprobados;
    }

    /**
     * @param cantidadAprobados the cantidadAprobados to set
     */
    public void setCantidadAprobados(Long cantidadAprobados) {
        this.cantidadAprobados = cantidadAprobados;
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
    
    
}
