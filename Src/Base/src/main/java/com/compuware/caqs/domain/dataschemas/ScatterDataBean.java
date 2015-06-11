/*
 * ScatterDataBean.java
 *
 * Created on 1 décembre 2003, 10:40
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;


/**
 *
 * @author  cwfr-fdubois
 */
public class ScatterDataBean extends ElementBean implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String metH = null;
    
    private String metV = null;
    
    /** Creates a new instance of ElementBean */
    public ScatterDataBean(BaselineBean baseline, String id, 
            String idTelt, String lib) {
        super(baseline, id, idTelt, lib);
    }
    
    public String getMetH() {
        return this.metH;
    }
    
    public void setMetH(String metH) {
        this.metH = metH;
    }
    
    public String getMetV() {
        return this.metV;
    }
    
    public void setMetV(String metV) {
        this.metV = metV;
    }
    
    public boolean isValid() {
        return this.metH != null && this.metV != null;
    }
    
}
