package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import net.sf.json.JSONObject;

/**
 *
 * @author cwfr-dzysman
 */
public class AgregationAvg extends Agregation implements Serializable {

    @Override
    public String getId() {
        return "AVG";
    }

    @Override
    public void setParamsFromJSON(JSONObject obj) {
        
    }

}
