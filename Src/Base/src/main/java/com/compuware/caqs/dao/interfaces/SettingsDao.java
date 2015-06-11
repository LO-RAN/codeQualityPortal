package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.settings.SettingValuesBean;
import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.domain.dataschemas.settings.UserSettingBean;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;

public interface SettingsDao {
	
	/**
	 * @param setting the setting id for which the values has to be retrieved
	 * @return a SettingValuesBean which will contain every values possible for the provided setting
	 */
	public SettingValuesBean getSettingValues(Settings setting);
	
	/**
	 * @param setting the setting id for which the value has to be retrieved
	 * @param userId    the user id
	 * @return the setting value for the user, or the setting's default value if the user doesn't have one
	 */
	public String getSettingValueForUser(Settings setting, String userId);
	
	/**
	 * @param setting the setting to be updated
	 * @param userId  the user id for which the setting has to be updated
	 * @param newValue the new value for the setting
	 */
	public void updateSettingValueForUser(Settings setting, String userId, String newValue) throws DataAccessException;

    /**
     * recherche la liste des valeurs des param�trages pour chaque utilisateur
     * @param setting le param�trage pour lequel rechercher
     * @return la liste
     */
    public List<UserSettingBean> getSettingValuesByUsers(Settings setting);
	
}
