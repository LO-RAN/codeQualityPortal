package com.compuware.caqs.service;

import java.util.List;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.LabelResume;
import com.compuware.caqs.exception.CaqsException;

public class LabelSvc {
	private static final LabelSvc instance = new LabelSvc();
	
	private LabelSvc() {
	}
	
	public static LabelSvc getInstance() {
		return instance;
	}
	
    public void linkLabels(String idLabelOld, String idLabelNew) throws CaqsException {
    	com.compuware.caqs.business.consult.Label label = new com.compuware.caqs.business.consult.Label();
    	label.linkLabels(idLabelOld, idLabelNew);
    }
    
    public LabelBean retrieveLabel(String idLabel) throws CaqsException {
    	com.compuware.caqs.business.consult.Label label = new com.compuware.caqs.business.consult.Label();
    	return label.retrieveLabel(idLabel);
    }
    
    public void store(LabelBean labelBean, ElementBean eltBean) throws CaqsException {
    	com.compuware.caqs.business.consult.Label label = new com.compuware.caqs.business.consult.Label();
    	label.store(labelBean, eltBean);
    }
    
    public List<LabelResume> retrieveAllLabels(String req, String userId) throws CaqsException {
    	com.compuware.caqs.business.consult.Label label = new com.compuware.caqs.business.consult.Label();
    	return label.retrieveAllLabels(req, userId);
    }    
    
}
