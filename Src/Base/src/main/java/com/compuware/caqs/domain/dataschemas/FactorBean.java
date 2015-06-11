/*
 * ElementBean.java
 *
 * Created on 1 d�cembre 2003, 10:40
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author  cwfr-fdubois
 */
public class FactorBean extends FactorDefinitionBean implements Serializable {

    private static final long serialVersionUID = -257449817053759286L;
    private BaselineBean baseline = null;
    private double noteFacbl = 0.0;
    private Double tendance = null;
    private LabelBean label = null;
    private Collection criterions = null;
    /**
     * commentaire
     */
    private String comment = null;

    /** Creates a new instance of ElementBean */
    public FactorBean() {
        super();
    }

    @Override
    public FactorBean clone() {
        FactorBean retour = new FactorBean();
        retour.setId(this.getId());
        retour.setTendance(this.getTendance());
        retour.setBaseline(this.getBaseline());
        retour.setLabel(this.getLabel());
        retour.setCriterions(this.getCriterions());
        retour.setNote(this.getNote());
        return retour;
    }

    public BaselineBean getBaseline() {
        return this.baseline;
    }

    public void setBaseline(BaselineBean baseline) {
        this.baseline = baseline;
    }

    public double getNote() {
        return this.noteFacbl;
    }

    public void setNote(double note) {
        this.noteFacbl = note;
    }

    public LabelBean getLabel() {
        return this.label;
    }

    public void setLabel(LabelBean label) {
        this.label = label;
    }

    public Collection getCriterions() {
        return this.criterions;
    }

    public void setCriterions(Collection criterions) {
        this.criterions = criterions;
    }

    public double getTendance() {
        double result = 0.0;
        if (this.tendance != null) {
            result = this.tendance.doubleValue();
        }
        return result;
    }

    public void setTendance(Double tendance) {
        this.tendance = tendance;
    }

    public void setComment(String c) {
        this.comment = c;
    }

    public String getComment() {
        return this.comment;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retour = false;
        if (obj != null) {
            if (obj instanceof String) {
                retour = ((String) obj).equals(this.getId());
            } else if(obj instanceof FactorBean) {
                retour = ((FactorBean)obj).getId().equals(this.getId());
            }
        }
        return retour;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
