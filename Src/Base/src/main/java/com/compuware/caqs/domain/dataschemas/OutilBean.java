package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class OutilBean extends DefinitionBean implements Internationalizable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7929881516187788264L;
    private int nbMetricsAssociated = 0;
    private int nbModelsUsingIt = 0;

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc, "");
    }

    public String getBundleName() {
        return I18nUtil.OUTIL_BUNDLE_NAME;
    }

    public int getNbMetricsAssociated() {
        return nbMetricsAssociated;
    }

    public void setNbMetricsAssociated(int nbMetricsAssociated) {
        this.nbMetricsAssociated = nbMetricsAssociated;
    }

    public int getNbModelsUsingIt() {
        return nbModelsUsingIt;
    }

    public void setNbModelsUsingIt(int nbModelsUsingIt) {
        this.nbModelsUsingIt = nbModelsUsingIt;
    }

    @Override
    public boolean equals(Object ob) {
        boolean retour = false;
        if(ob instanceof OutilBean) {
            retour = this.getId().equals(((OutilBean)ob).getId());
        }
        return retour;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
