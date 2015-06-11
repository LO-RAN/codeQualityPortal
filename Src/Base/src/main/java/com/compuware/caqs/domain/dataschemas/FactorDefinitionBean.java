/*
 * ElementBean.java
 *
 * Created on 1 d�cembre 2003, 10:40
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public class FactorDefinitionBean extends DefinitionBean implements Serializable, Internationalizable {

    private static final long serialVersionUID = -1309287857595577812L;
    private Collection criterionsDefs = null;

    /** Creates a new instance of ElementBean */
    public FactorDefinitionBean() {
    }

    public FactorDefinitionBean(String id) {
        this.id = id;
    }

    public Collection getCriterionsDefs() {
        return this.criterionsDefs;
    }

    public void setCriterionsDefs(Collection criterionsDefs) {
        this.criterionsDefs = criterionsDefs;
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getLib(loc), quote, bracket, doubleQuote);
    }

    /** Get the factor label.
     * @param loc the locale 
     * @return the factor label.
     */
    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getDesc(loc), quote, bracket, doubleQuote);
    }

    /** Get the factor description.
     * @param loc the locale 
     * @return the factor description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    public String getEscapedComplement(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getComplement(loc), quote, bracket, doubleQuote);
    }

    /** Get the factor complement.
     * @param loc the locale 
     * @return the factor complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc, "");
    }

    public String getBundleName() {
        return I18nUtil.FACTEUR_BUNDLE_NAME;
    }
}
