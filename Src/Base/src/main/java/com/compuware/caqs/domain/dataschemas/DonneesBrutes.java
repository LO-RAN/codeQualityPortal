/*
 * DonneesBrutes.java
 *
 * Created on 9 janvier 2003, 12:12
 */

package com.compuware.caqs.domain.dataschemas;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  cwfr-fdubois
 */
public class DonneesBrutes {
    
    private String idElt;
    private String idTelt;
    private String descElt;
    
    private Map<String, Double> metriques = new HashMap<String, Double>();
    
    /** Creates a new instance of DonneesBrutes */
    public DonneesBrutes(String idElt, String idTelt, String descElt) {
        this.idElt = idElt;
        this.idTelt = idTelt;
        this.descElt = descElt;
    }
    
    public void addMetrique(String idMet, double valbrute) {
        metriques.put(idMet, new Double(valbrute));
    }

    public String getTypeElt() {
    	return idTelt;
    }
    
    public String getDesc() {
    	return descElt;
    }
    
    public Double getValue(String key) {
    	return (Double)metriques.get(key);
    }
    
}
