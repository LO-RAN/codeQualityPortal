/*
 * ElementBean.java
 *
 * Created on 1 décembre 2003, 10:40
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 *
 * @author  cwfr-fdubois
 */
public class FactorElementBean extends FactorBean implements Serializable {
    
    private static final long serialVersionUID = -5755087053700207131L;
	
	private ElementBean eltBean = null;
    
    /** Creates a new instance of ElementBean */
    public FactorElementBean() {
        super();
    }
    
    public void setElement(ElementBean pEltBean) {
        this.eltBean = pEltBean;
    }
    
    public ElementBean getElement() {
        return this.eltBean;
    }
    
}
