/*
 * BaselineBean.java
 *
 * Created on 23 janvier 2004, 11:27
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.sql.Timestamp;

import com.compuware.caqs.presentation.util.StringFormatUtil;

/**
 * Contains definition information.
 * @author  cwfr-fdubois
 */
public class DefinitionBean implements Serializable {
    
    private static final long serialVersionUID = -2233823108005674763L;
	
	/** The element ID. */
    protected String id = null;
    /** The element label. */
    protected String lib = null;
    /** The element description. */
    protected String desc = null;
    /** The element instantiation date and time. */
    protected Timestamp dinst = null;
    /** The element modification date and time. */
    protected Timestamp dmaj = null;
    /** peremeption date */
    protected Timestamp dperemption = null;
    /** application date */
    protected Timestamp dapplication = null;

    /** Creates a new instance of DefinitionBean */
    public DefinitionBean() {
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setId(java.lang.String)
	 */
    public void setId(String id) {
        this.id = id;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setLib(java.lang.String)
	 */
    public void setLib(String lib) {
        this.lib = lib;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setDesc(java.lang.String)
	 */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setDinst(java.sql.Timestamp)
	 */
    public void setDinst(Timestamp dinst) {
        this.dinst = dinst;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setDmaj(java.sql.Timestamp)
	 */
    public void setDmaj(Timestamp dmaj) {
        this.dmaj = dmaj;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getId()
	 */
    public String getId() {
        return this.id;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getLib()
	 */
    public String getLib() {
        return this.lib;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getDesc()
	 */
    public String getDesc() {
        return this.desc;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getDinst()
	 */
    public Timestamp getDinst() {
        return this.dinst;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getDmaj()
	 */
    public Timestamp getDmaj() {
        return this.dmaj;
    }

    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#getDinst()
	 */
    public Timestamp getDperemption() {
        return this.dperemption;
    }

    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#setDmaj(java.sql.Timestamp)
	 */
    public void setDperemption(Timestamp dperemption) {
        this.dperemption = dperemption;
    }

    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#compareLibTo(com.compuware.caqs.domain.dataschemas.DefinitionBean)
	 */
    public int compareLibTo(DefinitionBean o2) {
        int result = 0;
        if (this.lib != null && o2 != null && o2.getLib() != null) {
            result = this.lib.compareTo(o2.getLib());
        }
        return result;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#compareDescTo(com.compuware.caqs.domain.dataschemas.DefinitionBean)
	 */
    public int compareDescTo(DefinitionBean o2) {
        int result = 0;
        if (this.desc != null && o2 != null && o2.getDesc() != null) {
            result = this.desc.compareTo(o2.getDesc());
        }
        return result;
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.domain.dataschemas.IDefinitionBean#toString()
	 */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DEF[ID="+this.id);
        buffer.append(", LIB="+this.lib);
        buffer.append(", DESC="+this.desc);
        buffer.append(", DMAJ="+this.dmaj);
        buffer.append("]");
        return buffer.toString();
    }
    
    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getLib(), quote, bracket, doubleQuote);
	}
	
	public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote) {
		return StringFormatUtil.escapeString(this.getDesc(), quote, bracket, doubleQuote);
	}

    public Timestamp getDapplication() {
        return dapplication;
    }

    public void setDapplication(Timestamp dapplication) {
        this.dapplication = dapplication;
    }
    
}
