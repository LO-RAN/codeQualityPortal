/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.util.CaqsConfigUtil;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class GenerateProjectAnalysisBatchParametersAction extends ExtJSAjaxAction {
    
    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject object = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;

        Properties confTxt = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        if(confTxt != null) {
            String params = confTxt.getProperty(Constants.BATCH_LAUNCH_PARAMETERS);
            if(params != null) {
                //on substitue
                String projectId = request.getParameter("projectId");
                if(projectId != null) {
                    params = params.replaceAll("%PROJECT_ID%", projectId);
                }
                String projectName = request.getParameter("projectName");
                if(projectName != null) {
                    params = params.replaceAll("%PROJECT_NAME%", projectName);
                }
                String baselineName = request.getParameter("baselineName");
                if(baselineName == null) {
                    baselineName = "";
                }
                params = params.replaceAll("%BASELINE_NAME%", baselineName);
                Users connectedUser = RequestUtil.getConnectedUser(request);
                if(connectedUser != null) {
                    params = params.replaceAll("%USER_ID%", connectedUser.getId());
                    params = params.replaceAll("%USER_EMAIL%", connectedUser.getEmail());
                }
                String eaList = request.getParameter("eaList");
                if(eaList != null) {
                    params = params.replaceAll("%_EA_LIST%", eaList);
                }
                String eaOptionList = request.getParameter("eaOptionList");
                if(eaOptionList != null) {
                    params = params.replaceAll("%EA_OPTION_LIST%", eaOptionList);
                }
                object.put("batchLaunchParameters", params);
            } else {
                retour = MessagesCodes.CONFIGURATION_ERROR;
            }
        }
        this.fillJSONObjectWithReturnCode(object, retour);

        return object;
    }
}
