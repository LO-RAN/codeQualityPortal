package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import net.sf.json.JSONObject;

/**
 *
 * @author cwfr-dzysman
 */
public class AgregationExclus extends Agregation implements Serializable {

    @Override
    public String getId() {
        return "EXCLUS";
    }

    public int getDefaultValue() {
        int retour = 1;
        String param = this.getParam("DEFAULT");
        if (param != null) {
            try {
                retour = Integer.parseInt(param);
            } catch (NumberFormatException exc) {
            }
        }
        return retour;
    }

    public int getValueExclus() {
        int retour = 1;
        String param = this.getParam("VALUE_EXCLUS");
        if (param != null) {
            try {
                retour = Integer.parseInt(param);
            } catch (NumberFormatException exc) {
            }
        }
        return retour;
    }

    @Override
    public void setParamsFromJSON(JSONObject obj) {
        this.setParam("VALUE_EXCLUS", obj.getString("VALUE_EXCLUS"));
        this.setParam("DEFAULT", obj.getString("DEFAULT"));
    }
}
