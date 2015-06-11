package com.compuware.caqs.domain.dataschemas.actionplan.list;

import java.util.ArrayList;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;

public class ActionPlanImpactedElementBeanCollection extends ArrayList<ActionPlanImpactedElementBean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ea id
	 */
	private String idEa = null;
	/**
	 * baseline id
	 */
	private String idBline = null;

	private ActionPlanImpactedElementBeanCollection() {
	}

	public ActionPlanImpactedElementBeanCollection(String idEa, String idBline) {
		this();
		this.idEa = idEa;
		this.idBline = idBline;
	}

	public ActionPlanImpactedElementBeanCollection(ActionPlanImpactedElementBeanCollection other) {
		super(other);
		this.idBline = other.getIdBline();
		this.idEa = other.getIdEa();
	}

	public boolean contains(Object o) {
		boolean retour = false;
		if(o!=null) {
			if(o instanceof ActionPlanImpactedElementBean) {
				ActionPlanImpactedElementBean fb = (ActionPlanImpactedElementBean) o;
				for(ActionPlanImpactedElementBean thisFb : this) {
					if(thisFb.getIdElt().equals(fb.getIdElt())) {
						retour = true;
						break;
					}
				}
			}
		}
		return retour;
	}

	public boolean add(ActionPlanImpactedElementBean fb) {
		boolean retour = false;
		if(!this.contains(fb)) {
			retour = super.add(fb);
		}
		return retour;
	}

	public ActionPlanImpactedElementBean get(ActionPlanImpactedElementBean apeb) {
		ActionPlanImpactedElementBean retour = null;
		if(apeb!=null) {
			for(ActionPlanImpactedElementBean element : this) {
				if(element.getIdElt().equals(apeb.getIdElt())) {
					retour = element;
					break;
				}
			}
		}
		return retour;
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
