/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.i18n;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 * @author cwfr-fdubois
 *
 */
public class UnifaceViewData implements Internationalizable, Serializable {

	private String id = null;
	
	/* (non-Javadoc)
	 * @see com.compuware.toolbox.util.resources.Internationalizable#getTextKey(java.lang.String)
	 */
	public String getTextKey(String columnName) {
		return this.getBundleName() + Internationalizable.KEY_SEPARATOR + columnName + Internationalizable.KEY_SEPARATOR + getId();
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.toolbox.util.resources.Internationalizable#getId()
	 */
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.toolbox.util.resources.Internationalizable#getLib(java.util.Locale)
	 */
	public String getLib(Locale loc) {
    	return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
	}

	/* (non-Javadoc)
	 * @see com.compuware.toolbox.util.resources.Internationalizable#getDesc(java.util.Locale)
	 */
	public String getDesc(Locale loc) {
    	return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
	}

	/* (non-Javadoc)
	 * @see com.compuware.toolbox.util.resources.Internationalizable#getComplement(java.util.Locale)
	 */
	public String getComplement(Locale loc) {
    	return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc);
	}

    public String getBundleName() {
        return I18nUtil.UNIFACEVIEW_BUNDLE_NAME;
    }

}
