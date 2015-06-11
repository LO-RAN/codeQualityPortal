/*
 * BaselineBean.java
 *
 * Created on 23 janvier 2004, 11:27
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 *
 * @author  cwfr-fdubois
 */
public class DialecteBean extends DefinitionBean implements Serializable {
    
    private static final long serialVersionUID = 230044304669042545L;

    private LanguageBean language = null;
    
    /** Creates a new instance of BaselineBean */
    public DialecteBean() {
    }
    
    public DialecteBean(String id) {
        this.id = id;
    }
    
    public void setLangage(LanguageBean langage) {
        this.language = langage;
    }
    
    public LanguageBean getLangage() {
        return this.language;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append(", LANG_ID="+this.language.getId());
        buffer.append("]");
        return buffer.toString();
    }
    
}
