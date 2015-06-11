package com.compuware.caqs.service;

import java.util.Collection;
import java.util.Locale;

import com.compuware.caqs.business.consult.ModelDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;

public class DefinitionSvc {
	private static final DefinitionSvc instance = new DefinitionSvc();
	
	private DefinitionSvc() {
	}
	
	public static DefinitionSvc getInstance() {
		return instance;
	}
	
    private ModelDefinition model = new ModelDefinition();

    public Collection retrieveFactorDefinition(ElementBean eltBean) {
		return model.retrieveFactorDefinition(eltBean);
	}

    public Collection retrieveCriterionDefinition(ElementBean eltBean) {
		return model.retrieveCriterionDefinition(eltBean);
}

    public String retrieveCriterionRules(ElementBean eltBean, String idCrit) {
		return model.retrieveCriterionRules(eltBean, idCrit);
	}

    public Collection retrieveMetriquesDefinition(ElementBean eltBean, Locale loc) {
		return model.retrieveMetriquesDefinition(eltBean, loc);
	}

    public Collection retrieveMetriquesDefinition(ElementBean eltBean) {
    	return retrieveMetriquesDefinition(eltBean, Locale.getDefault());
    }
}
