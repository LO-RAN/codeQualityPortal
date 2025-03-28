/*
 * CritererionNoteRepartition.java
 *
 * Created on 19 ao�t 2004, 10:40
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 * Contains criterion notes repartition.
 * Represent for each range ([1, 2[ ; [2, 3[ ; [3, 4[ ; [4]), the number of element associated.
 * @author cwfr-fdubois
 */
public class CriterionNoteRepartition extends CriterionDefinition implements Serializable {
    
    private static final long serialVersionUID = -4690991764690542414L;

	/** The global number of element. */
    private int total = 0;
    
    /** The note repartition. */
    private int[] repartition = new int[4];
    
    /** Creates a new instance of CritererionNoteRepartition */
    public CriterionNoteRepartition() {
    }
    
    /**
     * Add a number of element to the associated range.
     * @param part the part associated to the element set.
     * @param sum the number of element associated to the given set.
     */
    public void add(int part, int sum) {
        if (part >= 0) {
            this.repartition[part] += sum;
            this.total += sum;
        }
    }
    
    public void addWithoutSum(int part, int sum) {
    	if (part >= 0) {
    		//on n'ajoute qu'une fois la somme au total
    		if(this.repartition[part]==0) {
                this.total += sum;
            }
            this.repartition[part] = sum;
        }
    }
    
    /** Get the percentage representation for a range.
     * @param index the index-th range.
     * @return the percentage associated to a range.
     */
    public int getPct(int index) {
        int result = 0;
        if (this.total > 0) {
            // There is at least one element.
            // Percentage calculation.
            float pct = ((float)(this.repartition[index]*100))/((float)this.total);
            // Rounding the result to get an int value.
            result = Math.round(pct);
        }
        return result;
    }
    
    /** Get the number of element associated to a range.
     * @param index the index-th range.
     * @return the number of element associated to a range.
     */
    public int getValue(int index) {
        // Getter for the index-th range value.
        return this.repartition[index];
    }
    
}
