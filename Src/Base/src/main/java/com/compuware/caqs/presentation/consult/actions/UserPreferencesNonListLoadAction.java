/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.SettingsSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class UserPreferencesNonListLoadAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();

        String nbDaysLastAnalysisWarning = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.LAST_ANALYSIS_DATE_WARNING, RequestUtil.getConnectedUserId(request));
        retour.put("nbDaysWarning", nbDaysLastAnalysisWarning);
        boolean displayConnections = SettingsSvc.getInstance().getBooleanPreferenceForUser(
                Settings.DISPLAY_CONNECTIONS_TIMEPLOT, RequestUtil.getConnectedUserId(request));
        retour.put("displayConnections", Boolean.valueOf(displayConnections));
        boolean displayGlobalBaselines = SettingsSvc.getInstance().getBooleanPreferenceForUser(
                Settings.DISPLAY_GLOBAL_BASELINES_TIMEPLOT, RequestUtil.getConnectedUserId(request));
        retour.put("displayGlobalBaselines", displayGlobalBaselines);
        return retour;
    }
}
