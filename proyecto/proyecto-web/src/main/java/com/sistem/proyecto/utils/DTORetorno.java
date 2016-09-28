/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.utils;

import java.util.List;
import java.util.Map;

/**
 *
 * @author mojeda
 */
public class DTORetorno<T> {
    
    private boolean error;
    
    private String mensaje;
    
    private Integer draw;
    
    private Integer total;
    
    private Integer page;
    
    private T data;
     
    private List<Map<String, Object>> retorno;

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return the draw
     */
    public Integer getDraw() {
        return draw;
    }

    /**
     * @param draw the draw to set
     */
    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    

    /**
     * @return the retorno
     */
    public List<Map<String, Object>> getRetorno() {
        return retorno;
    }

    /**
     * @param retorno the rows to set
     */
    public void setRetorno(List<Map<String, Object>> retorno) {
        this.retorno = retorno;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    
}
