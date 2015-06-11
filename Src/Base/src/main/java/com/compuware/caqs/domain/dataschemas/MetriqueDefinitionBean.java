package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class MetriqueDefinitionBean extends DefinitionBean implements Internationalizable, Serializable {

    private OutilBean outil;

    private int nbCriterionsAssociated;

    public void setOutil(OutilBean o) {
        this.outil = o;
    }

    public OutilBean getOutil() {
        return this.outil;
    }
    private static final long serialVersionUID = -3228760037566845577L;

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getLib(loc), quote, bracket, doubleQuote);
    }

    /** Get the metric label.
     * @param loc the locale 
     * @return the metric label.
     */
    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getDesc(loc), quote, bracket, doubleQuote);
    }

    /** Get the metric description.
     * @param loc the locale 
     * @return the metric description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    public String getEscapedComplement(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getComplement(loc), quote, bracket, doubleQuote);
    }

    /** Get the metric complement.
     * @param loc the locale 
     * @return the metric complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc, "");
    }

    public String getBundleName() {
        return I18nUtil.METRIQUE_BUNDLE_NAME;
    }

    public int getNbCriterionsAssociated() {
        return nbCriterionsAssociated;
    }

    public void setNbCriterionsAssociated(int nbCriterionsAssociated) {
        this.nbCriterionsAssociated = nbCriterionsAssociated;
    }
    
}
