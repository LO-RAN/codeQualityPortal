/*
 * CriterionBean.java
 *
 * Created on 27 janvier 2004, 15:29
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author  cwfr-fdubois
 */
public class CriterionPerFactorBean extends CriterionDefinition implements Serializable {
    
	private static final long serialVersionUID = -2044732288049600554L;
	
	private List fact = new ArrayList();
    private JustificationBean just = null;
    private Double note = null;
    private double justNote = 0.0;
    private Double tendance = null;
    
    /** Creates a new instance of CriterionBean */
    public CriterionPerFactorBean() {
    }
    
    public List getFactors() {
        return this.fact;
    }
    
    public void setFactor(List pFact) {
        this.fact = pFact;
    }

    public void addFactor(CriterionFactorWeightBean pFact) {
        this.fact.add(pFact);
    }

    public Double getNote() {
        return this.note;
    }
    
    public double getJustNote() {
        return this.justNote;
    }
    
    public JustificationBean getJustificatif() {
        return this.just;
    }
    
    public void setNote(Double note) {
        this.note = note;
    }
    
    public void setJustNote(double justNote) {
        this.justNote = justNote;
    }
    
    public void setJustificatif(JustificationBean pJust) {
        this.just = pJust;
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
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ID_CRIT="+this.getId());
        buffer.append(", LIB_CRIT="+this.getLib());
        buffer.append(", NOTE="+this.getNote());
        buffer.append(", WEIGHT="+this.getWeight());
        List factorList = this.getFactors();
        if (factorList != null) {
            Iterator i = factorList.iterator();
            while (i.hasNext()) {
                Object tfact = i.next();
                buffer.append("\n").append(tfact);
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
}
