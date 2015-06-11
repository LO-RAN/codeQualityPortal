/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:28:58
*/

package com.compuware.caqs.presentation.admin.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class ElementRetrieveFilterForm extends org.apache.struts.action.ActionForm {

    private static final long serialVersionUID = -6073735440349007919L;
	
	private String typeElt;
    private String descElt;
    private String idStereotype;
    private String emptyOnly;

    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
		super.reset(actionMapping, request);
	}

    public String getTypeElt() {
        return typeElt;
    }

    public void setTypeElt(String typeElt) {
        this.typeElt = typeElt;
    }

    public String getDescElt() {
        return descElt;
    }

    public void setDescElt(String descElt) {
        this.descElt = descElt;
    }

    public String getEmptyOnly() {
        return emptyOnly;
    }

    public void setEmptyOnly(String emptyOnly) {
        this.emptyOnly = emptyOnly;
    }

    public String getIdStereotype() {
        return idStereotype;
    }

    public void setIdStereotype(String idStereotype) {
        this.idStereotype = idStereotype;
    }
}
