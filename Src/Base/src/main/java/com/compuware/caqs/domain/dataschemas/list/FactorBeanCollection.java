package com.compuware.caqs.domain.dataschemas.list;

import java.util.ArrayList;

import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;

public class FactorBeanCollection extends ArrayList<FactorBean> {

    /**
     * Associated ea id
     */
    private String idElt = null;
    /**
     * Associated baseline id
     */
    private String idBline = null;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private FactorBeanCollection() {
    }

    public FactorBeanCollection(String idElt, String idBline) {
        this();
        this.idElt = idElt;
        this.idBline = idBline;
    }

    public FactorBeanCollection(FactorBeanCollection other) {
        this(other.getIdElt(), other.getIdBline());
        super.addAll(other);
    }

    public boolean containsByObject(Object o) {
        boolean retour = false;
        if (o != null) {
            if (o instanceof FactorBean) {
                FactorBean fb = (FactorBean) o;
                for (FactorBean thisFb : this) {
                    if (thisFb.getId().equals(fb.getId())) {
                        retour = true;
                        break;
                    }
                }
            } else if (o instanceof String) {
                String fb = (String) o;
                for (FactorBean thisFb : this) {
                    if (thisFb.getId().equals(fb)) {
                        retour = true;
                        break;
                    }
                }
            } else if (o instanceof ActionPlanFactorBean) {
                ActionPlanFactorBean fb = (ActionPlanFactorBean) o;
                for (FactorBean thisFb : this) {
                    if (thisFb.getId().equals(fb.getId())) {
                        retour = true;
                        break;
                    }
                }
            }
        }
        return retour;
    }

    @Override
    public boolean add(FactorBean fb) {
        boolean retour = false;
        if (!this.contains(fb)) {
            retour = super.add(fb);
        }
        return retour;
    }

    public String getIdElt() {
        return idElt;
    }

    public void setIdElt(String idElt) {
        this.idElt = idElt;
    }

    public String getIdBline() {
        return idBline;
    }

    public void setIdBline(String idBline) {
        this.idBline = idBline;
    }

    public FactorBeanCollection cloneAndDuplicateContent() {
        FactorBeanCollection retour = new FactorBeanCollection(this.idElt, this.idBline);
        for (FactorBean fb : this) {
            retour.add(fb.clone());
        }
        return retour;
    }

    public FactorBean get(String id) {
        FactorBean retour = null;
        for (FactorBean thisFb : this) {
            if (thisFb.getId().equals(id)) {
                retour = thisFb;
                break;
            }
        }
        return retour;
    }
}
