package com.sistem.proyecto.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class JasperDatasource implements JRDataSource {

	private List<Map<String, Object>> listaItems = new ArrayList<Map<String, Object>>();
	private int indiceItemActual = -1;

	@Override
	public boolean next() throws JRException {
		return ++indiceItemActual < listaItems.size();
	}

	@Override
	public Object getFieldValue(JRField jrf) throws JRException {
		return listaItems.get(indiceItemActual).get(jrf.getName());
	}

	public int size() {
		return listaItems.size();
	}

	public void add(HashMap<String, Object> indicador) {
		listaItems.add(indicador);
	}

	public void addAll(Collection<? extends Map<String, Object>> lista) {
		listaItems.addAll(lista);
	}

	public void clear() {
		listaItems.clear();
	}

	public void remove(int i) {
		listaItems.remove(i);
	}

	public void remove(HashMap<String, Object> item) {
		listaItems.remove(item);
	}

	public void removeAll(Collection<? extends HashMap<String, Object>> lista) {
		listaItems.removeAll(lista);
	}
}