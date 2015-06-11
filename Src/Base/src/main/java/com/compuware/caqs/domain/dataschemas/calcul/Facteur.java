/*
 * Facteur.java
 *
 * Created on 2 octobre 2002, 14:08
 */

package com.compuware.caqs.domain.dataschemas.calcul;

import com.compuware.caqs.domain.calculation.rules.Aggregable;

/**
 * Definit un Facteur.
 * @author  cwfr-fdubois
 */
public class Facteur implements Aggregable {
    
    /** Identifiant du facteur. */
    private String idFac;
    /** Note du facteur. */
    private double note;
    
    private double weightArchi = 1.0;

    /** Cree une nouvelle instance de Facteur.
     * @param idFac identifiant du facteur.
     */
    public Facteur(String idFac, double weight) {
        this.idFac = idFac;
        this.weightArchi = weight;
    }
        
    /** Cree une nouvelle instance de Facteur.
     * @param idFac identifiant du facteur.
     * @param note note du facteur.
     * @param weight poids du facteur.
     */
    public Facteur(String idFac, double weight, double note) {
        this.idFac = idFac;
        this.note = note;
        this.weightArchi = weight;
    }
    
    /** Cree une nouvelle instance de Facteur.
     * @param idFac identifiant du facteur.
     */
    public Facteur(String idFac) {
    	this.idFac = idFac;
    }

    public String getId() {
        return this.idFac;
    }
    
    /** Verifie si la note du facteur est valide.
     * @return true si note*poids > 0, false sinon.
     */
    public boolean isNotNull() {
        // true si note*poids > 0, false sinon.
        return this.note > 0;
    }
    
    public double getCalculatedNote() {
    	return this.note;
    }
    
    /** Methode d'acces a la note du facteur.
     * @return la note.
     */
    public double getNote() {
        // Variable resultat.
        double result;
        // La note calculee est retournee.
        result = this.note;
        return result;
    }
    
    /** Methode d'acces a la note du facteur.
     * @param note la note du facteur.
     */
    public void setNote(double note) {
        // Initialisation de la note du facteur.
    	this.note = note;
    }
    
    public double getValue() {
        return getNote();
    }
    
    public double getWeight() {
        return this.weightArchi;
    }
    

    
}
