package com.compuware.caqs.domain.dataschemas.settings;

import com.compuware.caqs.domain.dataschemas.comparators.SettingValueComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SettingValuesBean {
	private String 			settingId;
	private List<SettingValue>	settingValues;
	public SettingValuesBean() {
		this.settingValues = new ArrayList<SettingValue>();
	}
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
	public List<SettingValue> getSettingValues() {
		return settingValues;
	}
	public void setSettingValues(List<SettingValue> settingValues) {
		this.settingValues = settingValues;
	}
	
	public void addSettingValue(SettingValue val) {
		this.settingValues.add(val);
	}
	public String getSettingId() {
		return this.settingId;
	}

    public void sort(String col, boolean desc, Locale loc) {
        if(this.settingValues != null) {
            Collections.sort(this.settingValues, new SettingValueComparator(col, desc, loc));
        }
    }
}
