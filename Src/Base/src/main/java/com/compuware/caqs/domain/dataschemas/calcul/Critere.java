/*
 * Critere.java
 *
 * Created on 2 octobre 2002, 14:08
 */

package com.compuware.caqs.domain.dataschemas.calcul;

import com.compuware.caqs.domain.calculation.rules.Aggregable;

/**
 * Définit un critère et les règles de calculs des critères.
 * @author  cwfr-fdubois
 */
public class Critere implements Aggregable {
    
    /** Identifiant du critère. */
    private String idCrit;
    /** Note du critère. */
    private double note;
    /** Identifiant du justificatif associé au critère. */
    private String idJustification;
    /** Note forcée justifiée. */
    private double noteJustifiee;

    /** The estimated cost to fix the quality defect. */ 
    private double cost;
    
    /**
     * Poids d'architecture.
     * Définit l'importance d'un élément dans le projet.
     */
    private double weightArchi;

    /** Crée une nouvelle instance de Critere.
     * @param id_crit identifiant du critère.
     * @param just critère justifié.
     */
    public Critere(String idCrit, Critere just, double weightArchi) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.weightArchi = weightArchi;
        // Initialisation des données de justification.
        initJustificationImpl(just);
    }
    
    /** Crée une nouvelle instance de Critere.
     * @param id_crit identifiant du critère.
     * @param poids_archi poids du critère.
     * @param just critère justifié.
     */
    public Critere(String idCrit, double weightArchi, Critere just) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.weightArchi = weightArchi;
        // Initialisation des données de justification.
        initJustificationImpl(just);
    }
    
    /** Crée une nouvelle instance de Critere.
     * @param idCrit identifiant du critère.
     * @param note note du critère.
     * @param weightArchi poids du critère.
     * @param just critère justifié.
     */
    public Critere(String idCrit, double note, double weightArchi, Critere just) {
        // Initialisation des attributs.
        this.idCrit = idCrit;
        this.note = note;
        this.weightArchi = weightArchi;
        // Initialisation des données de justification.
        initJustificationImpl(just);
    }
    
    /** Crée une nouvelle instance de Critere.
     * @param idCrit identifiant du critère.
     * @param idJustification critère justifié.
     * @param noteJustifiee note justifiée.
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
    
    /** Initialisation des données de justification.
     * @param just critère justifié.
     */
    public void initJustification(Critere just) {
        initJustificationImpl(just);
    }

    /** Initialisation des données de justification.
     * @param just critère justifié.
     */
    private void initJustificationImpl(Critere just) {
        if (just != null) {
            // Mise à jour des données (identifiant + note forcée).
            this.idJustification = just.getIdJustification();
            this.noteJustifiee = just.getNoteJustifiee();
        }
    }

    /** Méthode d'accès à l'identifiant du justificatif.
     * @return l'identifiant du justificatif.
     */
    public String getIdJustification() {
        // l'identifiant du justificatif.
        return this.idJustification;
    }

    /** Méthode d'accès à la note justifiée.
     * @return la note justifiée.
     */
    public double getNoteJustifiee() {
        // La note justifiée.
        return this.noteJustifiee;
    }
    
    public double getCalculatedNote() {
    	return this.note;
    }
    
    /** Méthode d'accès à la note du critère.
     * @return la note du critère.
     */
    public double getNote() {
        // Variable résultat.
        double result;
        if (this.noteJustifiee > this.note) {
            // La note justifiée > note calculée
            // La note du critère vaut note justifiée*poids
            result = this.noteJustifiee;
        }
        else {
            result = this.note;
        }
        return result;
    }
    
    /** Méthode d'accès à la note du critère.
     * @param note_cribl la note du critère*poids.
     */
    public void setNote(double note) {
        // Mise à jour de la note du critère.
    	this.note = note;
    }
    
    /** Setter for the estimated cost.
     * @param cost the estimated cost.
     */
    public void setCost(double cost) {
        // Set the estimated cost.
    	this.cost = cost;
    }
    
    /** Méthode d'accès au poids du critère.
     * @return le poids du critère.
     */
    public double getPoidsArchi() {
        // Le poids du critère.
        return this.weightArchi;
    }
    
    public double getValue() {
        // Variable résultat.
        double result;
        if (this.noteJustifiee > this.note) {
            // La note justifiée > note calculée
            // La note du critère vaut note justifiée*poids
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
