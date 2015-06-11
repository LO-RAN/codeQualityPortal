/*
 * Critere.java
 *
 * Created on 2 octobre 2002, 14:08
 */

package com.compuware.caqs.domain.dataschemas.calcul;

import com.compuware.caqs.domain.calculation.rules.Aggregable;

/**
 * D�finit un crit�re et les r�gles de calculs des crit�res.
 * @author  cwfr-fdubois
 */
public class Critere implements Aggregable {
    
    /** Identifiant du crit�re. */
    private String idCrit;
    /** Note du crit�re. */
    private double note;
    /** Identifiant du justificatif associ� au crit�re. */
    private String idJustification;
    /** Note forc�e justifi�e. */
    private double noteJustifiee;

    /** The estimated cost to fix the quality defect. */ 
    private double cost;
    
    /**
     * Poids d'architecture.
     * D�finit l'importance d'un �l�ment dans le projet.
     */
    private double weightArchi;

    /** Cr�e une nouvelle instance de Critere.
     * @param id_crit identifiant du crit�re.
     * @param just crit�re justifi�.
     */
    public Critere(String idCrit, Critere just, double weightArchi) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.weightArchi = weightArchi;
        // Initialisation des donn�es de justification.
        initJustificationImpl(just);
    }
    
    /** Cr�e une nouvelle instance de Critere.
     * @param id_crit identifiant du crit�re.
     * @param poids_archi poids du crit�re.
     * @param just crit�re justifi�.
     */
    public Critere(String idCrit, double weightArchi, Critere just) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.weightArchi = weightArchi;
        // Initialisation des donn�es de justification.
        initJustificationImpl(just);
    }
    
    /** Cr�e une nouvelle instance de Critere.
     * @param idCrit identifiant du crit�re.
     * @param note note du crit�re.
     * @param weightArchi poids du crit�re.
     * @param just crit�re justifi�.
     */
    public Critere(String idCrit, double note, double weightArchi, Critere just) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.note = note;
        this.weightArchi = weightArchi;
        // Initialisation des donn�es de justification.
        initJustificationImpl(just);
    }
    
    /** Cr�e une nouvelle instance de Critere.
     * @param idCrit identifiant du crit�re.
     * @param idJustification crit�re justifi�.
     * @param noteJustifiee note justifi�e.
     */
    public Critere(String idCrit, String idJustification, double noteJustifiee) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.idJustification = idJustification;
        this.noteJustifiee = noteJustifiee;
    }
    
    public String getId() {
        return this.idCrit;
    }
    
    /** Initialisation des donn�es de justification.
     * @param just crit�re justifi�.
     */
    public void initJustification(Critere just) {
        initJustificationImpl(just);
    }

    /** Initialisation des donn�es de justification.
     * @param just crit�re justifi�.
     */
    private void initJustificationImpl(Critere just) {
        if (just != null) {
            // Mise � jour des donn�es (identifiant + note forc�e).
            this.idJustification = just.getIdJustification();
            this.noteJustifiee = just.getNoteJustifiee();
        }
    }

    /** M�thode d'acc�s � l'identifiant du justificatif.
     * @return l'identifiant du justificatif.
     */
    public String getIdJustification() {
        // l'identifiant du justificatif.
        return this.idJustification;
    }

    /** M�thode d'acc�s � la note justifi�e.
     * @return la note justifi�e.
     */
    public double getNoteJustifiee() {
        // La note justifi�e.
        return this.noteJustifiee;
    }
    
    public double getCalculatedNote() {
    	return this.note;
    }
    
    /** M�thode d'acc�s � la note du crit�re.
     * @return la note du crit�re.
     */
    public double getNote() {
        // Variable r�sultat.
        double result;
        if (this.noteJustifiee > this.note) {
            // La note justifi�e > note calcul�e
            // La note du crit�re vaut note justifi�e*poids
            result = this.noteJustifiee;
        }
        else {
            result = this.note;
        }
        return result;
    }
    
    /** M�thode d'acc�s � la note du crit�re.
     * @param note_cribl la note du crit�re*poids.
     */
    public void setNote(double note) {
        // Mise � jour de la note du crit�re.
    	this.note = note;
    }
    
    /** Setter for the estimated cost.
     * @param cost the estimated cost.
     */
    public void setCost(double cost) {
        // Set the estimated cost.
    	this.cost = cost;
    }
    
    /** M�thode d'acc�s au poids du crit�re.
     * @return le poids du crit�re.
     */
    public double getPoidsArchi() {
        // Le poids du crit�re.
        return this.weightArchi;
    }
    
    public double getValue() {
        // Variable r�sultat.
        double result;
        if (this.noteJustifiee > this.note) {
            // La note justifi�e > note calcul�e
            // La note du crit�re vaut note justifi�e*poids
            result = this.noteJustifiee;
        }
        else {
        	result = this.note;
        }
        return result;
    }
    
    public double getWeight() {
        return this.weightArchi;
    }
    
    /**
     * Getter for the estimated cost.
     * @return the estimated cost for the criterion.
     */
    public double getCost() {
    	return this.cost;
    }
    
}
