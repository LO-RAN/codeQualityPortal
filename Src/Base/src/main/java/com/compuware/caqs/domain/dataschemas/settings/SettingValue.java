package com.compuware.caqs.domain.dataschemas.settings;

import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class SettingValue implements Internationalizable {
	private String settingId;

	private SettingValue() {}
	
	public SettingValue(String id) {
		this.settingId = id;
	}
	
	public String getComplement(Locale arg0) {
		return null;
	}

	public String getDesc(Locale arg0) {
		return null;
	}

	public String getId() {
		return this.settingId;
	}

	public String getLib(Locale loc) {
		return DbmsResourceBundle.getString(this, this.getBundleName(),
				Internationalizable.LIB_PROPERTY_KEY, loc);
	}

	public String getTextKey(String columnName) {
		return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
		columnName + Internationalizable.KEY_SEPARATOR + getId();
	}

    public String getBundleName() {
        return I18nUtil.SETTING_VALUE_BUNDLE_NAME;
    }

}
