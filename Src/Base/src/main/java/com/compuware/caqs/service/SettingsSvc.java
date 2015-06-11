package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.SettingsDao;
import com.compuware.caqs.domain.dataschemas.settings.SettingValuesBean;
import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;

public class SettingsSvc {

    private static final SettingsSvc instance = new SettingsSvc();

    private SettingsSvc() {
    }

    public static SettingsSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * @return all available themes
     */
    public SettingValuesBean getAvailablePreferences(Settings setting) {
        SettingValuesBean retour = null;
        SettingsDao dao = DaoFactory.getInstance().getSettingsDao();
        retour = dao.getSettingValues(setting);
        return retour;
    }

    /**
     * @param setting the setting for which the value has to be retrieved
     * @param userId the user for which the setting must be retrieved
     * @return the preference value for the provided user
     */
    public String getPreferenceForUser(Settings setting, String userId) {
        String retour = null;
        SettingsDao dao = DaoFactory.getInstance().getSettingsDao();
        retour = dao.getSettingValueForUser(setting, userId);
        return retour;
    }

    /**
     * @param setting the setting for which the value has to be retrieved
     * @param userId the user for which the setting must be retrieved
     * @return the preference for the user, as a boolean
     */
    public boolean getBooleanPreferenceForUser(Settings setting, String userId) {
        boolean retour = false;
        SettingsDao dao = DaoFactory.getInstance().getSettingsDao();
        String val = dao.getSettingValueForUser(setting, userId);
        if(val != null) {
            retour = Boolean.valueOf(val);
        }
        return retour;
    }

    public MessagesCodes updatePreferenceForUser(Settings setting, String userId, String newTheme) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        SettingsDao dao = DaoFactory.getInstance().getSettingsDao();
        try {
            dao.updateSettingValueForUser(setting, userId, newTheme);
        } catch(DataAccessException exc) {
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

}
