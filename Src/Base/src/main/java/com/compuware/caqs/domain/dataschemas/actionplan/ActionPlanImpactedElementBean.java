package com.compuware.caqs.domain.dataschemas.actionplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActionPlanImpactedElementBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7289429947576221870L;
	private String idElt;
	private String libElt;
	private String descElt;
	private String idTelt;
	private List<ActionPlanCriterionBean> criterions;
	private int    mark;
	
	private ActionPlanImpactedElementBean() {
		this.criterions = new ArrayList<ActionPlanCriterionBean>();
	}
	
	public ActionPlanImpactedElementBean(String idElt) {
		this();
		this.idElt = idElt;
	}
	
	public String getIdElt() {
		return idElt;
	}
	public void setIdElt(String idElt) {
		this.idElt = idElt;
	}
	public String getLibElt() {
		return libElt;
	}
	public void setLibElt(String libElt) {
		this.libElt = libElt;
	}
	public String getDescElt() {
		return descElt;
	}
	public void setDescElt(String descElt) {
		this.descElt = descElt;
	}
	public String getIdTelt() {
		return idTelt;
	}
	public void setIdTelt(String idTelt) {
		this.idTelt = idTelt;
	}
	public List<ActionPlanCriterionBean> getCriterions() {
		return criterions;
	}
	public void setCriterions(List<ActionPlanCriterionBean> criterions) {
		this.criterions = criterions;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
	
	
}
