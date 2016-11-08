/**
 * 
 */
package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Empleo;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;



/**
 * @author Miguel Ojeda
 * 
 */
@Local
public interface EmpleoManager extends GenericDao<Empleo, Long> {

}
