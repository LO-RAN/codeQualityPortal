package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import java.util.Comparator;

/**
 *
 * @author cwfr-dzysman
 */
public class LangueBeanComparator
        implements Comparator<LangueBean> {

    private boolean desc;
    private String field;

    public LangueBeanComparator(String f, boolean d) {
        this.desc = d;
        this.field = f;
    }

    public int compare(LangueBean elt1, LangueBean elt2) {
        int retour = 0;
        if("lib".equals(this.field)) {
            retour = elt1.getLib().compareToIgnoreCase(elt2.getLib());
        } else if("id".equals(this.field)) {
            retour = elt1.getId().compareToIgnoreCase(elt2.getId());
        } else if("desc".equals(this.field)) {
            retour = elt1.getDesc().compareToIgnoreCase(elt2.getDesc());
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
