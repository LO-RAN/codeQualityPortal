package com.compuware.caqs.domain.dataschemas.actionplan.list;

import java.util.ArrayList;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import java.util.List;

public class ActionPlanElementBeanCollection<T extends ActionPlanElementBean> extends ArrayList<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * ea id
     */
    private String idEa = null;
    /**
     * baseline id
     */
    private String idBline = null;

    private ActionPlanElementBeanCollection() {
    }

    public ActionPlanElementBeanCollection(String idEa, String idBline) {
        this();
        this.idEa = idEa;
        this.idBline = idBline;
    }

    public ActionPlanElementBeanCollection(ActionPlanElementBeanCollection<T> other) {
        super(other);
        this.idBline = other.getIdBline();
        this.idEa = other.getIdEa();
    }

    @Override
    public boolean add(T fb) {
        boolean retour = false;
        if (!this.contains(fb)) {
            retour = super.add(fb);
        }
        return retour;
    }

    public T get(T apcb) {
        T retour = null;
        if (apcb != null) {
            for (T elt : this) {
                if (elt.getId().equals(apcb.getId())) {
                    retour = elt;
                    break;
                }
            }
        }
        return retour;
    }

    public T get(String id) {
        T retour = null;
        if (id != null) {
            for (T elt : this) {
                if (elt.getId().equals(id)) {
                    retour = elt;
                    break;
                }
            }
        }
        return retour;
    }

    public String getIdEa() {
        return idEa;
    }

    public void setIdEa(String idEa) {
        this.idEa = idEa;
    }

    public String getIdBline() {
        return idBline;
    }

    public void setIdBline(String idBline) {
        this.idBline = idBline;
    }

}
