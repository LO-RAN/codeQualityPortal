package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.settings.Settings;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.settings.SettingValue;
import com.compuware.caqs.domain.dataschemas.settings.SettingValuesBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.SettingsSvc;
import java.util.Iterator;

public class GetPreferencesListAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject root = new JSONObject();
        String settingId = request.getParameter("settingId");
        Settings setting = Settings.fromString(settingId);
        if (!setting.equals(Settings.UNKNOWN)) {

            SettingsSvc settingSvc = SettingsSvc.getInstance();

            SettingValuesBean prefs = settingSvc.getAvailablePreferences(setting);
            prefs.sort("lib", false, RequestUtil.getLocale(request));

            List<SettingValue> prefsNames = prefs.getSettingValues();
            root.put("totalCount", prefsNames.size());
            List<JSONObject> l = new ArrayList<JSONObject>();

            String defaultValue = SettingsSvc.getInstance().getPreferenceForUser(
                    setting, RequestUtil.getConnectedUserId(request));

            for (SettingValue sv : prefsNames) {
                l.add(this.settingValueToJSONObject(sv, defaultValue, request));
            }
            this.executeFiltersOnValues(l, setting, request);
            root.put("prefs", l.toArray(new JSONObject[0]));
        }
        return root;
    }

    private JSONObject settingValueToJSONObject(SettingValue elt, String defaultValue, HttpServletRequest request) {
        JSONObject retour = new JSONObject();
        retour.put("idSetting", elt.getId());
        retour.put("valueSetting", elt.getLib(RequestUtil.getLocale(request)));
        retour.put("defaultValue", defaultValue);
        return retour;
    }

    private void executeFiltersOnValues(List<JSONObject> l, Settings setting, HttpServletRequest request) {
        if(setting.equals(Settings.STARTING_PAGE)) {
            Users user = RequestUtil.getConnectedUser(request);
            boolean canAccessAdministration = user.access("ADMINISTRATION_ACCESS");
            boolean canAccessArchitecture = user.access("ARCHITECTURE_ACCESS");
            boolean canAccessModelEditor = user.access("Model_Editor");
            for(Iterator<JSONObject> it = l.iterator(); it.hasNext(); ) {
                JSONObject obj = it.next();
                if(!canAccessAdministration && "administration".equals(obj.getString("idSetting"))) {
                    it.remove();
                }
                if(!canAccessArchitecture && "architecture".equals(obj.getString("idSetting"))) {
                    it.remove();
                }
                if(!canAccessModelEditor && "modelEditor".equals(obj.getString("idSetting"))) {
                    it.remove();
                }
            }
        }
    }
}
    

