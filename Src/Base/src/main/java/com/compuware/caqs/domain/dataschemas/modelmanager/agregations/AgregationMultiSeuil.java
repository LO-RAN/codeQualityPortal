package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import net.sf.json.JSONObject;

/**
 *
 * @author cwfr-dzysman
 */
public class AgregationMultiSeuil extends Agregation implements Serializable {

    @Override
    public String getId() {
        return "MULTI_SEUIL";
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

    public int getSeuil1() {
        int retour = 1;
        String param = this.getParam("SEUIL1");
        if (param != null) {
            try {
                retour = Integer.parseInt(param);
            } catch (NumberFormatException exc) {
            }
        }
        return retour;
    }

    public int getSeuil2() {
        int retour = 1;
        String param = this.getParam("SEUIL2");
        if (param != null) {
            try {
                retour = Integer.parseInt(param);
            } catch (NumberFormatException exc) {
            }
        }
        return retour;
    }

    public int getSeuil3() {
        int retour = 1;
        String param = this.getParam("SEUIL3");
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
        this.setParam("SEUIL1", obj.getString("SEUIL1"));
        this.setParam("SEUIL2", obj.getString("SEUIL2"));
        this.setParam("SEUIL3", obj.getString("SEUIL3"));
    }
}
