/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class VolumetryComparator implements Comparator<Volumetry> {

    final private boolean desc;
    final private String field;
    final private Locale locale;

    public VolumetryComparator(final String f, final boolean d, final Locale loc) {
        this.desc = d;
        this.field = f;
        this.locale = loc;
    }

    public int compare(Volumetry elt1, Volumetry elt2) {
        int retour = 0;
        if ("libTelt".equals(this.field)) {
            retour = elt1.getElementType().getLib(this.locale).compareToIgnoreCase(elt2.getElementType().getLib(this.locale));
        } else if ("nbElts".equals(this.field)) {
            retour = Integer.valueOf(elt1.getTotal()).compareTo(Integer.valueOf(elt2.getTotal()));
        } else if ("nbCrees".equals(this.field)) {
            retour = Integer.valueOf(elt1.getCreated()).compareTo(Integer.valueOf(elt2.getCreated()));
        } else if ("nbSupp".equals(this.field)) {
            retour = Integer.valueOf(elt1.getDeleted()).compareTo(Integer.valueOf(elt2.getDeleted()));
        }
        if (this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
