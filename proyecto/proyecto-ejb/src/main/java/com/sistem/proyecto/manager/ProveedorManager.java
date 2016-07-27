/**
 * 
 */
package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Proveedor;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;



/**
 * @author daniel
 * 
 */
@Local
public interface ProveedorManager extends GenericDao<Proveedor, Long> {

}
