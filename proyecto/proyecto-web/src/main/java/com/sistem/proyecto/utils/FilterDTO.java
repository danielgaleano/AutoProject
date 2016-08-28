/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.utils;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class FilterDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String data;

    private List<ReglaDTO> rules;

    private String groupOp;

    /**
     *
     */
    public FilterDTO() {
        super();
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the rules
     */
    public List<ReglaDTO> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<ReglaDTO> rules) {
        this.rules = rules;
    }

    /**
     * @return the groupOp
     */
    public String getGroupOp() {
        return groupOp;
    }

    /**
     * @param groupOp the groupOp to set
     */
    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }
    
    
}
