/**
 * 
 */
package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Cliente;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;



/**
 * @author Miguel Ojeda
 * 
 */
@Local
public interface ClienteManager extends GenericDao<Cliente, Long> {

}
