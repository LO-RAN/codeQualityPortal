package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import net.sf.json.JSONObject;

/**
 *
 * @author cwfr-dzysman
 */
public class AgregationAvgAll extends Agregation implements Serializable  {

    @Override
    public String getId() {
        return "AVG_ALL";
    }

    @Override
    public void setParamsFromJSON(JSONObject obj) {
        
    }

}
