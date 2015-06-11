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
public class BottomUpSummaryBean implements Comparable, Serializable {
    
    private static final long serialVersionUID = -1402100323628620641L;

	private double[] notes = new double[4];
	
	private Map<String, MetriqueBean> additionnalMetricMap = null;
    
    private ElementBean element = null;
    
    /** Creates a new instance of CriterionBean */
    public BottomUpSummaryBean() {
    }
    
    public double getNote(int i) {
        return this.notes[i];
    }
    
    public ElementBean getElement() {
        return this.element;
    }
    
    public void setNote(double note, int i) {
        this.notes[i] = note;
    }
    
    public void addNote(double note) {
        if (note < 2) {
            this.notes[0] += 1;
        }
        else if (note < 3) {
            this.notes[1] += 1;
        }
        else if (note < 4) {
            this.notes[2] += 1;
        }
        else if (note == 4) {
            this.notes[3] += 1;
        }
    }
    
    public void setElement(ElementBean element) {
        this.element = element;
    }
    
    public void setAdditionnalMetricMap(Map<String, MetriqueBean> additionnalMetricMap) {
    	this.additionnalMetricMap = additionnalMetricMap;
    }
    
    public double getAdditionnalMetric(String idMet) {
    	double result = 0;
    	if (this.additionnalMetricMap != null && idMet != null && idMet.length() > 0) {
    		MetriqueBean metBean = this.additionnalMetricMap.get(idMet);
    		if (metBean != null) {
    			result = metBean.getValbrute();
    		}
    	}
    	return result;
    }
    
    public int compareTo(Object o) {
        int result = 0;
        int i = 0;
        boolean evaluated = false;
        BottomUpSummaryBean busb = (BottomUpSummaryBean)o;
        while (i < this.notes.length && !evaluated) {
            if (this.notes[i] < busb.getNote(i)) {
                result = 1;
                evaluated = true;
            }
            else {
                if (this.notes[i] > busb.getNote(i)) {
                    result = -1;
                    evaluated = true;
                }
            }
            i++;
        }
        return result;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ID_ELT="+this.element.getId());
        buffer.append(", LIB_ELT="+this.element.getLib());
        buffer.append(", NOTES=["+this.notes[0]+", "+this.notes[1]+", "+this.notes[2]+", "+this.notes[3]+"]\n");
        return buffer.toString();
    }
}
