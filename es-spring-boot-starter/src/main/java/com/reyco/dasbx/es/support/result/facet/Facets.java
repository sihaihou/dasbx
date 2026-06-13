package com.reyco.dasbx.es.support.result.facet;

import java.util.ArrayList;
import java.util.List;

public class Facets {
	private Facets() {
		// TODO Auto-generated constructor stub
	}
	
	public static FacetDefinition facetDefinition(String name,String field,Class<? extends FacetLabelProvider> providerClass) {
		FacetDefinition facet = new FacetDefinition()
				.setName(name)
				.setField(field)
				.setProviderClass(providerClass);
		return facet;
	}
	
	public static List<FacetDefinition> facetDefinition(List<String> names,List<String> fields,List<Class<? extends FacetLabelProvider>> providerClasses) {
		if(names==null || fields==null || providerClasses==null) {
			throw new FacetException("Facet configuration error!");
		}
		if(names.size()!=fields.size() || fields.size()!=providerClasses.size()) {
			throw new FacetException("Facet configuration error!");
		}
		List<FacetDefinition> facets = new ArrayList<>(names.size());
		FacetDefinition facet = null;
		for (int i = 0; i < providerClasses.size(); i++) {
			 facet = new FacetDefinition()
					.setName(names.get(i))
					.setField(fields.get(i))
					.setProviderClass(providerClasses.get(i));
			 facets.add(facet);
		}
		return facets;
	}
	
	public static List<FacetDefinition> facetDefinition(String[] names,String[] fields,Class<? extends FacetLabelProvider>[] providerClasses) {
		if(names==null || fields==null || providerClasses==null) {
			throw new FacetException("Facet configuration error!");
		}
		if(names.length!=fields.length || fields.length!=providerClasses.length) {
			throw new FacetException("Facet configuration error!");
		}
		List<FacetDefinition> facets = new ArrayList<>(names.length);
		FacetDefinition facet = null;
		for (int i = 0; i < providerClasses.length; i++) {
			 facet = new FacetDefinition()
					.setName(names[i])
					.setField(fields[i])
					.setProviderClass(providerClasses[i]);
			 facets.add(facet);
		}
		return facets;
	}
}
