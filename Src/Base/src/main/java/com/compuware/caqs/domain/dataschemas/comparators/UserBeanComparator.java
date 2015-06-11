package com.compuware.caqs.domain.dataschemas.comparators;

import com.compuware.caqs.domain.dataschemas.UserBean;
import java.util.Comparator;

/**
 *
 * @author cwfr-lizac
 */
public class UserBeanComparator
        implements Comparator<UserBean> {

    private boolean desc;
    private String field;

    public UserBeanComparator(String f, boolean d) {
        this.desc = d;
        this.field = f;
    }

    public int compare(UserBean elt1, UserBean elt2) {
        int retour = 0;
        if("lastname".equals(this.field)) {
            retour = elt1.getLastName().compareToIgnoreCase(elt2.getLastName());
        } else if("id".equals(this.field)) {
            retour = elt1.getId().compareToIgnoreCase(elt2.getId());
        } else if("firstname".equals(this.field)) {
            retour = elt1.getFirstName().compareToIgnoreCase(elt2.getFirstName());
        } else if("email".equals(this.field)) {
            retour = elt1.getEmail().compareToIgnoreCase(elt2.getEmail());
        }
        if(this.desc) {
            retour *= -1;
        }
        return retour;
    }
}
