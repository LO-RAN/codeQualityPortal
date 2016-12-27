package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class FactorRepartitionBean extends RepartitionBean implements Serializable {

    private static final long serialVersionUID = 2057976080495250263L;

	public FactorRepartitionBean(String id, int nb) {
    	super(id, nb); 
    }
    
	public String getTextKey(String columnName) {
		return this.getBundleName() + Internationalizable.KEY_SEPARATOR + columnName + Internationalizable.KEY_SEPARATOR + getId();
	}
    
    /** Get the criterion label.
     * @param loc the locale 
     * @return the criterion label.
     */
    public String getLib(Locale loc) {
    	return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getBundleName() {
        return I18nUtil.FACTEUR_BUNDLE_NAME;
    }
	
}