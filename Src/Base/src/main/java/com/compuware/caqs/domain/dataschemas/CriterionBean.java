/*
 * CriterionBean.java
 *
 * Created on 27 janvier 2004, 15:29
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author  cwfr-fdubois
 */
public class CriterionBean implements Serializable {

    private static final long serialVersionUID = -3301083270348798055L;
    private double note = 0.0;
    private double justNote = 0.0;
    private double weight = 1.0;
    private Double tendance = null;
    private ElementBean element = null;
    private Map metriques = null;
    private CriterionDefinition criterionDefinition = null;
    private JustificationBean justificatif = null;
    private double cost = 0.0;
    private boolean isIncludedInActionPlan = false;
    private String comment = null;

    /** Creates a new instance of CriterionBean */
    public CriterionBean() {
    }

    public double getNote() {
        return this.note;
    }

    public double getNoteOrJustNote() {
        double retour = this.getNote();
        if (this.getJustNote() != 0.0) {
            retour = this.getJustNote();
        }
        return retour;
    }

    public double getJustNote() {
        return this.justNote;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getTendance() {
        double result = 0.0;
        if (this.tendance != null) {
            result = this.tendance.doubleValue();
        }
        return result;
    }

    public ElementBean getElement() {
        return this.element;
    }

    public Map getMetriques() {
        return this.metriques;
    }

    public CriterionDefinition getCriterionDefinition() {
        return this.criterionDefinition;
    }

    public JustificationBean getJustificatif() {
        return this.justificatif;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public void setJustNote(double justNote) {
        this.justNote = justNote;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setElement(ElementBean element) {
        this.element = element;
    }

    public void setMetriques(Map metriques) {
        this.metriques = metriques;
    }

    public void setTendance(Double tendance) {
        this.tendance = tendance;
    }

    public void setCriterionDefinition(CriterionDefinition criterionDef) {
        this.criterionDefinition = criterionDef;
    }

    public void setJustificatif(JustificationBean just) {
        this.justificatif = just;
    }

    public Double getValbrute(String idMet) {
        Double result = null;
        if (metriques != null) {
            MetriqueBean met = (MetriqueBean) metriques.get(idMet);
            if (met != null) {
                result = new Double(met.getValbrute());
            }
        }
        return result;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ID_ELT=").append(this.element.getId());
        buffer.append(", LIB_ELT=").append(this.element.getLib());
        buffer.append(", NOTE=").append(this.note);
        buffer.append(", WEIGHT=").append(this.weight);
        if (this.getMetriques() != null) {
            buffer.append(", METRIQUES=").append(this.getMetriques().keySet());
        }
        buffer.append("]\n");
        return buffer.toString();
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isIncludedInActionPlan() {
        return isIncludedInActionPlan;
    }

    public void setIncludedInActionPlan(boolean pIsIncludedInActionPlan) {
        this.isIncludedInActionPlan = pIsIncludedInActionPlan;
    }

    public void setComment(String c) {
        this.comment = c;
    }

    public String getComment() {
        return this.comment;
    }
}
