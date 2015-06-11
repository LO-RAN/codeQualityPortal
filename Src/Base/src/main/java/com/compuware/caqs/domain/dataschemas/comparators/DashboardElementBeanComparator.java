/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import java.util.Comparator;

/**
 *
 * @author cwfr-dzysman
 */
public class DashboardElementBeanComparator implements Comparator<DashboardElementBean> {

    private boolean desc;
    private String field;

    public DashboardElementBeanComparator(String f, boolean d) {
        this.desc = d;
        this.field = f;
    }

    public int compare(DashboardElementBean elt1, DashboardElementBean elt2) {
        int retour = 0;
        if ("lib".equals(this.field)) {
            retour = elt1.getLib().compareToIgnoreCase(elt2.getLib());
        } else if ("desc".equals(this.field)) {
            retour = elt1.getDesc().compareToIgnoreCase(elt2.getDesc());
        } else if ("dperemption".equals(this.field)) {
            retour = elt1.getDperemption().compareTo(elt2.getDperemption());
        } else if ("dmaj".equals(this.field)) {
            retour = elt1.getBaseline().getDmaj().compareTo(elt2.getBaseline().getDmaj());
        } else if ("meteoImg".equals(this.field)) {
            retour = elt1.getMeteo().compareTo(elt2.getMeteo());
        } else if ("tendanceLabel".equals(this.field)) {
            String tendanceLabel1 = StringFormatUtil.getTendanceLabel(elt1.getPreviousGoalsAverage(), elt1.getGoalsAverage());
            String tendanceLabel2 = StringFormatUtil.getTendanceLabel(elt2.getPreviousGoalsAverage(), elt2.getGoalsAverage());
            retour = tendanceLabel1.compareTo(tendanceLabel2);
        } else if ("nbLOC".equals(this.field)) {
            retour = Integer.valueOf(elt1.getNbLOC()).compareTo(Integer.valueOf(elt2.getNbLOC()));
        } else if ("nbFileElements".equals(this.field)) {
            retour = Integer.valueOf(elt1.getNbFileElements()).compareTo(Integer.valueOf(elt2.getNbFileElements()));
        } else if ("goalsAvg".equals(this.field)) {
            retour = Double.valueOf(elt1.getGoalsAverage()).compareTo(Double.valueOf(elt2.getGoalsAverage()));
        }
        if (this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
