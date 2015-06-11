/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.settings.SettingValue;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class SettingValueComparator
        implements Comparator<SettingValue> {

    private boolean desc;
    private String field;
    private Locale locale;

    public SettingValueComparator(String f, boolean d, Locale loc) {
        this.desc = d;
        this.field = f;
        this.locale = loc;
    }

    public int compare(SettingValue elt1, SettingValue elt2) {
        int retour = 0;
        if("lib".equals(this.field)) {
            retour = elt1.getLib(this.locale).compareToIgnoreCase(elt2.getLib(this.locale));
        } else if("id".equals(this.field)) {
            retour = elt1.getId().compareToIgnoreCase(elt2.getId());
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
