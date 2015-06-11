/*
 * Qametrique.java
 *
 * Created on 22 octobre 2002, 12:02
 */

package com.compuware.caqs.business.calculation;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Definit une metrique.
 * @author  cwfr-fdubois
 */
public class Qametrique implements ValuedObject {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);

    /** Identifiant de la metrique. */
    private String mIdMet = null;
    /** Valeur brute de la metrique. */
    private double mValbrute;
    /** Note calculee de la metrique. */
    private double mNotecalc;
    /** Valeur brute du justificatif de la metrique. */
    private double mJustValbrute;
    /** Note forcee du justificatif de la metrique. */
    private double mJustNotecalc;
    
    /** Cree une nouvelle instance de Qametrique.
     * @param id_met l'identifiant de la metrique.
     * @param valbrute la valeur brute de la metrique.
     * @param notecalc note calculee de la metrique.
     * @param just_met l'identifiant du justificatif de la metrique.
     * @param just_valbrute la valeur brute du justificatif de la metrique.
     * @param just_note la note forcee du justificatif de la metrique.
     */
    public Qametrique(String idMet, double valbrute, double notecalc,
            double justValbrute, double justNote) {
        // Initialisation des attributs.
        // Attributs de la metrique.
        mIdMet= idMet;
        mValbrute = valbrute;
        mNotecalc = notecalc;
        // Justificatif.
        mJustValbrute = justValbrute;
        mJustNotecalc = justNote;
    }

    /** Methode d'acces a la valeur brute de la metrique.
     * @return la valeur brute de la metrique.
     */
    public double getValbrute() {
        // Variable resultat.
        double result;
        if (mIdMet != null && mJustValbrute > 0) {
            // La valeur brute a ete forcee.
            result = mJustValbrute;
        }
        else {
            // La valeur brute est l'originale.
            result = mValbrute;
        }
        return result;
    }
    
    public double getValue() {
        return getValbrute();
    }
    
    /** Methode d'acces a la note de la metrique.
     * @return la note de la metrique.
     */
    public double getNote() {
        // Variable resultat.
        double result;
        if (mIdMet != null && mJustNotecalc > 0) {
            // La note a ete forcee.
            result = mJustNotecalc;
        }
        else {
            // La note est l'originale.
            result = mNotecalc;
        }
        return result;
    }
    
    public double divise(Qametrique met2) {
        double result = 0;
        double valb = getValbrute();
        double valb2 = met2.getValbrute();
        if (valb2 >= valb && valb2 > 0) {
            result = valb/valb2;
        }
        else {
            result = valb2/valb;
        }
        return result;
    }
}
