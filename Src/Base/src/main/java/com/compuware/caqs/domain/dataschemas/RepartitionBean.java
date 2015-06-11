/*
 * BaselineBean.java
 *
 * Created on 23 janvier 2004, 11:27
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class RepartitionBean implements Serializable, Internationalizable {
    
    private static final long serialVersionUID = 6259823479011100586L;

	String id = null;
    
    int mNb = 0;
    
    /** Creates a new instance of RepartitionBean */
    public RepartitionBean() {
    }
    
    /** Creates a new instance of RepartitionBean */
    public RepartitionBean(String id, int nb) {
    	this.id = id; 
        mNb = nb;
    }
    
	public String getId() {
		return this.id;
	}

    public void setId(String id) {
		this.id = id;
	}

	public String getLib() {
        return getLib(Locale.getDefault());
    }
    
    public int getNb() {
        return this.mNb;
    }
    
    public void setNb(int nb) {
        this.mNb = nb;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(this.getLib());
        buffer.append(", ");
        buffer.append(this.mNb);
        buffer.append("]");
        return buffer.toString();
    }

	public String getDesc(Locale loc) {
		return "";
	}
    
    /** Get the data complement.
     * @param loc the locale 
     * @return the data complement.
     */
    public String getComplement(Locale loc) {
    	return "";
    }
	
}
