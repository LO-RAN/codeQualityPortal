package com.compuware.caqs.domain.dataschemas.modelmanager;

import java.util.Locale;
import org.apache.struts.util.MessageResources;


/**
 * Form bean FormuleForm.
 */

/* include your imports here */
public class FormuleForm {

    /**
     * Formbean attribute for note.
     */
    private int score;

    /**
     * Formbean attribute for alwaysTrue.
     */
    private boolean alwaysTrue;

    /**
     * formula
     */
    private FormulaPart formula;

    public FormulaPart getFormula() {
        return formula;
    }

    public void setFormula(FormulaPart formula) {
        this.formula = formula;
    }

    public FormuleForm() {
        super();
        this.score = 1;
        this.alwaysTrue = false;
    }
    
    /**
     * Get the human readable formula.
     * @param includeErrors include errors as span into formula
     * @return value represented as a string. Possibly null.
     */
    public String getReadableFormula(boolean includeErrors, MessageResources resources, Locale loc) {
        return (this.formula==null)?"":this.formula.toFormatedString(includeErrors, resources, loc);
    }

    /**
     * Get the value of note.
     * @return value represented as an int.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Set the value of note. 
     * @param value, an int.
     */
    public void setScore(int value) {
        this.score = value;
    }                 
    
    /**
     * Get the value of alwaysTrue.
     * @return value represented as a string. Possibly null.
     */
    public boolean isAlwaysTrue() {
        return this.alwaysTrue;
    }

    /**
     * Set the value of alwaysTrue. 
     * @param value, an instanceof String.
     */
    public void setAlwaysTrue(boolean value) {
        this.alwaysTrue = value;
    }

} 
