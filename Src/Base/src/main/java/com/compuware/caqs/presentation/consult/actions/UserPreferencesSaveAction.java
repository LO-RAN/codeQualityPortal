package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.settings.Settings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.SettingsSvc;
import net.sf.json.JSONObject;

public class UserPreferencesSaveAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;

        SettingsSvc settingSvc = SettingsSvc.getInstance();
        String userId = RequestUtil.getConnectedUserId(request);

        String newTheme = request.getParameter("idTheme");
        if (newTheme != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.COLOR_THEME, userId, newTheme);
            if (ret == MessagesCodes.NO_ERROR) {
                RequestUtil.updateSelectedThemeForUser(newTheme, request);
                obj.put("newTheme", newTheme);
            } else {
                retour = ret;
            }
        }

        String msgLoc = request.getParameter("msgLoc");
        if (msgLoc != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.MESSAGE_LOCATION,
                    userId, msgLoc);
            if (ret == MessagesCodes.NO_ERROR) {
                RequestUtil.updateMessageLocationForUser(newTheme, request);
            } else {
                retour = ret;
            }
        }

        String startingPage = request.getParameter("startingPage");
        if (startingPage != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.STARTING_PAGE,
                    userId, startingPage);
            if (ret == MessagesCodes.NO_ERROR) {
                RequestUtil.updateStartingPageForUser(startingPage, request);
            } else {
                retour = ret;
            }
        }

        String nbDaysAnalysisWarning = request.getParameter("nbDaysAnalysisWarning");
        if (nbDaysAnalysisWarning != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.LAST_ANALYSIS_DATE_WARNING,
                    userId, nbDaysAnalysisWarning);
            if (ret != MessagesCodes.NO_ERROR) {
                retour = ret;
            }
        }

        String displayGlobalBaselines = request.getParameter("displayGlobalBaselines");
        if (nbDaysAnalysisWarning != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.DISPLAY_GLOBAL_BASELINES_TIMEPLOT,
                    userId, displayGlobalBaselines);
            if (ret != MessagesCodes.NO_ERROR) {
                retour = ret;
            }
        }

        String displayConnections = request.getParameter("displayConnections");
        if (displayConnections != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.DISPLAY_CONNECTIONS_TIMEPLOT,
                    userId, displayConnections);
            if (ret != MessagesCodes.NO_ERROR) {
                retour = ret;
            }
        }

        String dashboardDefaultDomain = request.getParameter("dashboardDefaultCB");
        if (dashboardDefaultDomain != null) {
            MessagesCodes ret = settingSvc.updatePreferenceForUser(Settings.DASHBOARD_DEFAULT_DOMAIN,
                    userId, dashboardDefaultDomain);
            if (ret != MessagesCodes.NO_ERROR) {
                retour = ret;
            }
        }

        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }
}
