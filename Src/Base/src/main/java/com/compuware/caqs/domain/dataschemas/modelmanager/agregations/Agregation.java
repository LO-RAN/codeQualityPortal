package com.compuware.caqs.domain.dataschemas.modelmanager.agregations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class Agregation implements Comparable<Agregation>, Serializable {

    public static Agregation getAgregationFromId(String id) {
        Agregation retour = null;
        if ("EXCLUS".equals(id)) {
            retour = new AgregationExclus();
        } else if ("EXCLUS_AVG".equals(id)) {
            retour = new AgregationAvgExclus();
        } else if ("EXCLUS_AVG_SEUIL".equals(id)) {
            retour = new AgregationAvgExclusSeuil();
        } else if ("AVG_ALL".equals(id)) {
            retour = new AgregationAvgAll();
        } else if ("AVG_WEIGHT".equals(id)) {
            retour = new AgregationAvgWeightByScore();
        } else if ("AVG".equals(id)) {
            retour = new AgregationAvg();
        } else if ("MULTI_SEUIL".equals(id)) {
            retour = new AgregationMultiSeuil();
        }
        return retour;
    }

    private String idTelt;

    public String getIdTelt() {
        return idTelt;
    }

    public void setIdTelt(String idTelt) {
        this.idTelt = idTelt;
    }
    private Map<String, String> params;

    protected Agregation() {
        this.params = new HashMap<String, String>();
    }

    public abstract String getId();

    public String getLib(MessageResources resources, Locale loc) {
        return resources.getMessage(loc, "caqs.modeleditor.modelEdition.impression.agg."+this.getId());
    }

    public String getParam(String idParam) {
        return this.params.get(idParam);
    }

    public void setParam(String idParam, String valueParam) {
        this.params.put(idParam, valueParam);
    }

    public Set<Map.Entry<String, String>> getParamsSet() {
        return this.params.entrySet();
    }

    public int compareTo(Agregation o) {
        int retour = 0;
        if (o != null) {
            if (!this.getClass().getName().equals(o.getClass().getName())) {
                //agrégations possibles :
                //AVG, AVG_ALL, EXCLUS, EXCLUS_AVG, EXCLUS_AVG_SEUIL, MULTI_SEUIL, AVG_WEIGHT_BY_NOTE
                //sévérités:EXCLUS, EXCLUS_AVG, EXCLUS_AVG_SEUIL, MULTI_SEUIL, AVG_WEIGHT_BY_NOTE, AVG, AVG_ALL
                double thisSev = this.getAggregationSeverity();
                double otherSev = o.getAggregationSeverity();
                retour = (thisSev < otherSev) ? -1 : 1;
            }
        }
        return retour;
    }

    private double getAggregationSeverity() {
        double retour = 0;

        if (this instanceof AgregationExclus) {
            retour = 1;
            int lValueExclus = ((AgregationExclus) this).getValueExclus();
            retour += 0.1 * lValueExclus;
        } else if (this instanceof AgregationAvgExclus) {
            retour = 2;
            int lValueExclus = ((AgregationAvgExclus) this).getValueExclus();
            retour += 0.1 * lValueExclus;
        } else if (this instanceof AgregationAvgExclusSeuil) {
            retour = 3;
            int lValueExclus = ((AgregationAvgExclusSeuil) this).getValueExclus();
            retour += 0.1 * lValueExclus;
        } else if (this instanceof AgregationMultiSeuil) {
            retour = 4;
        } else if (this instanceof AgregationAvgWeightByScore) {
            retour = 5;
        } else if (this instanceof AgregationAvg) {
            retour = 6;
        } else if (this instanceof AgregationAvgAll) {
            retour = 7;
        }

        return retour;
    }

    public abstract void setParamsFromJSON(JSONObject obj);
}
