/*
 * FactorEvolutionBean.java
 *
 * Created on 1 d�cembre 2003, 14:00
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public class CriterionEvolutionBean extends EvolutionBean implements Serializable, Internationalizable {

    /**
     *
     */
    private static final long serialVersionUID = 7604445211545864731L;

    /** Creates a new instance of FactorEvolutionBean */
    public CriterionEvolutionBean(String id) {
        super(id);
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR
                + columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    /** Get the criterion label.
     * @param loc the locale
     * @return the criterion label.
     */
    public String getLibWithoutTruncate(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    /** Get the criterion label.
     * @param loc the locale 
     * @return the criterion label.
     */
    public String getLib(Locale loc) {
        return truncate(DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc));
    }

    /** Get the criterion description.
     * @param loc the locale 
     * @return the criterion description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    /** Get the criterion complement.
     * @param loc the locale 
     * @return the criterion complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc);
    }

    public String getBundleName() {
        return I18nUtil.CRITERE_BUNDLE_NAME;
    }
}
