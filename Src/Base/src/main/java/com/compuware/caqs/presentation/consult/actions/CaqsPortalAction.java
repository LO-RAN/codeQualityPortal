package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.SettingsSvc;
import com.compuware.caqs.service.UserRightsSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class CaqsPortalAction extends Action {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    // --------------------------------------------------------- Public Methods

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String userId = RequestUtil.getConnectedUserId(request);
        String msgLoc = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.MESSAGE_LOCATION, userId);
        if (msgLoc != null) {
            msgLoc = msgLoc.substring("message-location-".length());
        } else {
            msgLoc = "north";
        }
        request.setAttribute("messageLocation", msgLoc);

        String startingPage = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.STARTING_PAGE, userId);
        request.setAttribute("startingPage", startingPage);

        String seeConnectionTimeline = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.DISPLAY_CONNECTIONS_TIMEPLOT, userId);
        request.setAttribute("seeConnectionTimeline", seeConnectionTimeline);

        String seeGlobalTimeline = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.DISPLAY_GLOBAL_BASELINES_TIMEPLOT, userId);
        request.setAttribute("seeGlobalTimeline", seeGlobalTimeline);
        String dashboardDefaultDomain = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.DASHBOARD_DEFAULT_DOMAIN, userId);
        request.setAttribute("dashboardDefaultDomain", dashboardDefaultDomain);

        File documentationDirectory = new File(CaqsConfigUtil.getCaqsHome() +
                File.separator + "Documentation");
        if (documentationDirectory != null && documentationDirectory.exists() &&
                documentationDirectory.isDirectory()) {
            File[] documents = documentationDirectory.listFiles();
			java.util.Arrays.sort(documents, new java.util.Comparator()
			{
				public int compare(Object o1, Object o2)
				{
					return ((File) o1).getName().compareToIgnoreCase(((File) o2).getName());
				}
			});
            request.setAttribute("documentations", documents);
        }

        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        request.setAttribute("customHeaderRight", prop.getProperty(Constants.CAQS_CUSTOM_HEADER_RIGHT));
        request.setAttribute("customHeaderLeft", prop.getProperty(Constants.CAQS_CUSTOM_HEADER_LEFT));

        request.setAttribute("messagesDisplayLimit", prop.getProperty(Constants.MESSAGES_DISPLAY_LIMIT_KEY));

        List<RoleBean> roleList = UserRightsSvc.getInstance().getAllCaqsRoles();
        request.setAttribute("rolesList", roleList);


        return mapping.findForward("success");
    }
}
