/*
 * FactorEvolutionBean.java
 *
 * Created on 1 d�cembre 2003, 14:00
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.compuware.toolbox.util.resources.Internationalizable;
import java.util.Locale;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class EvolutionBean extends DefinitionBean implements Serializable, Internationalizable {
    
    private static final long serialVersionUID = 6706692744998828580L;
	
    private Map<String, Double> data = new HashMap<String, Double>();
    
    /** Creates a new instance of FactorEvolutionBean */
    public EvolutionBean(String id) {
        super();
        setId(id);
    }

    public abstract String getLibWithoutTruncate(Locale loc);
    
    public void addEntry(String idBline, Double note) {
    	data.put(idBline, note);
    }
    
    public Collection getBaselines() {
        return data.keySet();
    }
    
    public String getEntry(String idBline) {
        String retour = null;
        Double v = (Double)data.get(idBline);
        if (v != null) {
            retour =  v.toString();
        }
        return retour;
    }

    public double getDoubleEntry(String idBline) {
        double retour = 0.0;
        Double v = (Double)data.get(idBline);
        if (v != null) {
            retour =  v.doubleValue();
        }
        return retour;
    }
    
    public String getStringList(Collection blineList, String separator) {
        StringBuffer buffer = new StringBuffer();
        Iterator i = blineList.iterator();
        int count = 0;
        while (i.hasNext()) {
            String bline = (String)i.next();
            String note = getEntry(bline);
            if (note == null) {
                note = "0.0";
            }
            if (count != 0) {
                buffer.append("|");
            }
            buffer.append(note);
            count++;
        }
        return buffer.toString();
    }
    
    private static final int MAX_LENGHT = 32;
    
    protected String truncate(String s) {
    	String result;
    	if (s != null && s.length() > MAX_LENGHT) {
    		result = s.substring(0, MAX_LENGHT) + "...";
    	}
    	else {
    		result = s;
    	}
    	return result;
    }
    
}
