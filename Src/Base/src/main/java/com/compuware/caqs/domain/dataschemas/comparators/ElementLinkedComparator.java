/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class ElementLinkedComparator implements Comparator<ElementLinked> {

    private boolean desc;
    private String field;
    private Locale locale;

    public ElementLinkedComparator(String f, boolean d, Locale loc) {
        this.desc = d;
        this.field = f;
        this.locale = loc;
    }

    public int compare(ElementLinked elt1, ElementLinked elt2) {
        int retour = 0;
        if("lib".equals(this.field)) {
            retour = elt1.getLib().compareToIgnoreCase(elt2.getLib());
        } else if("desc".equals(this.field)) {
            retour = elt1.getDesc().compareToIgnoreCase(elt2.getDesc());
        } else if("dperemption".equals(this.field)) {
            retour = elt1.getDperemption().compareTo(elt2.getDperemption());
        } else if("telt".equals(this.field)) {
            ElementType telt1 = new ElementType(elt1.getTypeElt());
            ElementType telt2 = new ElementType(elt2.getTypeElt());
            retour = telt1.getLib(this.locale).compareToIgnoreCase(telt2.getLib(this.locale));
        } else if("fatherLib".equals(this.field)) {
            if(elt1.getFather()!=null && elt2.getFather()!=null
                    && elt1.getFather().getLib()!=null && elt2.getFather().getLib()!=null) {
                retour = elt1.getFather().getLib().compareToIgnoreCase(elt2.getFather().getLib());
            }
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
