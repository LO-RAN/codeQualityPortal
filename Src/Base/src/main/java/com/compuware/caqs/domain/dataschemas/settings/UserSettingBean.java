package com.compuware.caqs.domain.dataschemas.settings;

public class UserSettingBean {
	private String userId;
	private Settings setting;
	private String settingValue;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Settings getSetting() {
		return setting;
	}
	public void setSettingId(Settings settingId) {
		this.setting = settingId;
	}
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
}
