package com.compuware.caqs.domain.dataschemas.actionplan.list;

import java.util.HashMap;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;

public class ActionPlanElementBeanMap extends HashMap<String, ActionPlanImpactedElementBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6976821101588675694L;

	/**
	 * ea id
	 */
	private String idEa = null;
	/**
	 * baseline id
	 */
	private String idBline = null;

	private ActionPlanElementBeanMap() {
	}

	public ActionPlanElementBeanMap(String idEa, String idBline) {
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
