package com.compuware.caqs.domain.dataschemas.actionplan.list;

import java.util.HashMap;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;

public class ActionPlanCriterionBeanMap extends HashMap<String, ActionPlanCriterionBean>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2829737770758151824L;
	/**
	 * ea id
	 */
	private String idEa = null;
	/**
	 * baseline id
	 */
	private String idBline = null;

	private ActionPlanCriterionBeanMap() {
	}

	public ActionPlanCriterionBeanMap(String idEa, String idBline) {
		this();
		this.idEa = idEa;
		this.idBline = idBline;
	}

	public String getIdEa() {
		return idEa;
	}

	public void setIdEa(String idEa) {
		this.idEa = idEa;
	}

	public String getIdBline() {
		return idBline;
	}

	public void setIdBline(String idBline) {
		this.idBline = idBline;
	}

}
