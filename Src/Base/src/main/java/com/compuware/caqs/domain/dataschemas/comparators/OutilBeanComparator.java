/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.OutilBean;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class OutilBeanComparator implements Comparator<OutilBean> {

    private boolean desc;
    private String field;
    private Locale loc;

    public OutilBeanComparator(String f, boolean d, Locale loc) {
        this.desc = d;
        this.field = f;
        this.loc = loc;
    }

    public int compare(OutilBean elt1, OutilBean elt2) {
        int retour = 0;
        if("lib".equals(this.field)) {
            retour = elt1.getLib(loc).compareToIgnoreCase(elt2.getLib(loc));
        } else if("nbAssociatedMetrics".equals(this.field)) {
            retour = Integer.valueOf(elt1.getNbMetricsAssociated()).compareTo(Integer.valueOf(elt2.getNbMetricsAssociated()));
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
