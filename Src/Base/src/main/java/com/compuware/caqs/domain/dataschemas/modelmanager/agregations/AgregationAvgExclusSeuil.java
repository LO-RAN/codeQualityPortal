package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import net.sf.json.JSONObject;

/**
 *
 * @author cwfr-dzysman
 */
public class AgregationAvgExclusSeuil extends Agregation implements Serializable {

    @Override
    public String getId() {
        return "EXCLUS_AVG_SEUIL";
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

    public int getSeuil() {
        int retour = 1;
        String param = this.getParam("SEUIL");
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
        this.setParam("SEUIL", obj.getString("SEUIL"));
    }
}
