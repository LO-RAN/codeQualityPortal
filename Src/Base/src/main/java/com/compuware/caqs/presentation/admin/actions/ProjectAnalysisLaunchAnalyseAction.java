package com.compuware.caqs.presentation.admin.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.WorkflowSvc;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import java.util.Locale;
import net.sf.json.JSONArray;

public class ProjectAnalysisLaunchAnalyseAction extends ExtJSAjaxAction {

    private static final String WORKFLOW_ANALYSIS_CONFIG = "workflow_analysis_config";

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();

        String eaList = request.getParameter("eaList");
        String eaOptionList = request.getParameter("eaOptionList");
        String baselineName = request.getParameter("baselineName");
        String projectId = request.getParameter("projectId");
        String projectName = request.getParameter("projectName");

        retour.put("projectName", projectName);

        Users user = RequestUtil.getConnectedUser(request);

        WorkflowSvc workflowSvc = new WorkflowSvc();
        WorkflowConfigBean config = (WorkflowConfigBean) BusinessFactory.getInstance().getBean(WORKFLOW_ANALYSIS_CONFIG);
        config.setUserId(user.getId());
        config.setUserEmail(user.getEmail());
        config.setBaselineName(baselineName);
        config.setEaList(eaList);
        config.setEaOptionList(eaOptionList);
        config.setProjectId(projectId);
        config.setProjectName(projectName);

        try {
            workflowSvc.startProcess(config);
            //recuperation des elements pour affichage du resume
            JSONArray array = new JSONArray();
            String[] ids = eaList.split(",");
            Locale loc = RequestUtil.getLocale(request);
            if (ids != null) {
                ElementSvc svc = ElementSvc.getInstance();
                for (int i = 0; i < ids.length; i++) {
                    ElementBean elt = svc.retrieveAllElementDataById(ids[i]);
                    if (elt != null) {
                        JSONArray obj = new JSONArray();
                        obj.add(elt.getLib());
                        String desc = (elt.getDesc()!=null)?elt.getDesc():"-";
                        obj.add(desc);
                        obj.add(elt.getPoids());
                        obj.add(elt.getSourceDir());
                        obj.add(elt.getUsage().getLib(loc));
                        obj.add(elt.getDialecte().getLib());
                        array.add(obj);
                    }
                }
            }
            this.putArrayIntoObject(array, retour);
            retour.put("ok", true);
        } catch (CaqsException e) {
            retour.put("ok", false);
            retour.put("errorMsgKey", "caqs.analysis.workflow.unexpectedexception");
            mLog.error("ProjectAnalysisLaunchAnalyseAction: "
                    + "Unexpected workflow exception : " + e.getMessage());
        }

        return retour;
    }
}
