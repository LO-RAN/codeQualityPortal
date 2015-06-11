/*
 * CriterionBean.java
 *
 * Created on 27 janvier 2004, 15:29
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  cwfr-fdubois
 */
public class BottomUpDetailBean implements Serializable {
    
    private static final long serialVersionUID = 2504763344260970571L;
	
    private ElementBean element = null;
    private java.util.List critList = null;
    
    /** Creates a new instance of CriterionBean */
    public BottomUpDetailBean() {
    }
    
    public ElementBean getElement() {
        return this.element;
    }
    
    public void setElement(ElementBean element) {
        this.element = element;
    }
    
    public java.util.List getCriterions() {
        return this.critList;
    }
    
    public void setCriterions(java.util.List pCritList) {
        this.critList = pCritList;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[BOTTOMUP_DETAIL ");
        buffer.append("\nELEMENT=").append(this.element);
        buffer.append("\nCRITERES:");
        if (this.critList != null) {
            Iterator i = this.critList.iterator();
            while (i.hasNext()) {
                Object fact = (Object)i.next();
                buffer.append("\n").append(fact);
            }
        }
        return buffer.toString();
    }

    public CriterionPerFactorBean lookUp(String idCrit) {
        CriterionPerFactorBean result = null;
        Iterator i = this.critList.iterator();
        while (i.hasNext() && (result == null)) {
            CriterionPerFactorBean crit = (CriterionPerFactorBean)i.next();
            if (crit.getId().equals(idCrit)) {
                result = crit;
            }
        }
        return result;
    }
}
