package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 7 févr. 2006
 * Time: 16:46:26
 * To change this template use File | Settings | File Templates.
 */
public class ElementRetrieveFilterBean implements Serializable {
    
	private static final long serialVersionUID = -318255477165507301L;
	
	private String typeElt;
    private String descElt;
    private String idStereotype;
    private boolean emptyOnly;

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

    public boolean getEmptyOnly() {
        return emptyOnly;
    }

    public void setEmptyOnly(boolean emptyOnly) {
        this.emptyOnly = emptyOnly;
    }

    public String getIdStereotype() {
        return idStereotype;
    }

    public void setIdStereotype(String idStereotype) {
        this.idStereotype = idStereotype;
    }
}
