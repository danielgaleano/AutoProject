/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface for a Data Access Object that can be used for a single specified
 * type domain object. A single instance implementing this interface can be used
 * only for the type of domain object specified in the type parameters.
 * 
 * @author dwolverton
 * 
 * @param <T>
 *            The type of the domain object for which this instance is to be
 *            used.
 * @param <ID>
 *            The type of the id of the domain object for which this instance is
 *            to be used.
 */
public interface GenericDao<T, ID extends Serializable> {
    
    /**
	 * <p>
	 * Get the entity with the specified type and id from the datastore.
	 * 
	 * <p>
	 * If none is found, return null.
	 */
	public T get(ID id);
	
	public Map<String, Object> getLike(T ejemplo, String[] atributos);

	public T get(T ejemplo);
	
	public Map<String, Object> getAtributos(T ejemplo, String[] atributos);

	public Map<String, Object> getAtributos(T ejemplo, String[] atributos, boolean like, boolean caseSensitive);

	public void save(T entity) throws Exception;

	public void update(T entity) throws Exception;

	public void delete(ID id) throws Exception;
        
	
	public List<T> list();

	public List<T> list(Integer primerResultado, Integer cantResultado);

	public List<T> list(T ejemplo);

	public List<T> list(T ejemplo, boolean like);

	public List<T> list(T ejemplo, String orderBy, String dir);

	public List<T> list(T ejemplo, String orderBy, String dir, boolean like);

        public List<T> list(T ejemplo, boolean all, Integer primerResultado,
			Integer cantResultados, String[] orderByAttrList,
			String[] orderByDirList, boolean like, boolean caseSensitive, String campoComparacion, List<Long> valoresComparacion, String tipoFiltro,
			List<String> atrMayIgual, List<Object> objMayIgual, List<String> atrMenIgual, List<Object> objMenIgual,
			String camposDistintos, boolean distintos);
        
	public List<Map<String, Object>> listAtributos(T ejemplo, String[] atributos);

	public List<Map<String, Object>> listAtributos(T ejemplo,
			String[] atributos, boolean like);
        public List<Map<String, Object>> listAtributos(T ejemplo,
		String[] atributos, boolean all, Integer primerResultado,
		Integer cantResultados, String[] orderByAttrList,
		String[] orderByDirList, boolean like, boolean caseSensitive,
		String propiedadFiltroComunes, String comun, String campoComparacion,List<Long> valoresComparacion, String tipoFiltro,
		List<String> atrMayIgual, List<Object> objMayIgual,List<String> atrMenIgual, List<Object> objMenIgual,
		String camposDinamicos, boolean distintos);

    
}
