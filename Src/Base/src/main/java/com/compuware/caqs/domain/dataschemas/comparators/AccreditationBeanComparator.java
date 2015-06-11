/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.rights.AccreditationBean;
import com.compuware.caqs.presentation.applets.scatterplot.data.ElementType;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class AccreditationBeanComparator implements Comparator<AccreditationBean> {

    private boolean desc;
    private String field;

    public AccreditationBeanComparator(String f, boolean d) {
        this.desc = d;
        this.field = f;
    }

    public int compare(AccreditationBean elt1, AccreditationBean elt2) {
        int retour = 0;
        if("rightName".equals(this.field)) {
            retour = elt1.getRightLib().compareToIgnoreCase(elt2.getRightLib());
        } else {
            Boolean b1 = elt1.getRights().get(this.field);
            Boolean b2 = elt2.getRights().get(this.field);

            if(b1!=null && b2!=null) {
                retour = b1.compareTo(b2);
            }
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
