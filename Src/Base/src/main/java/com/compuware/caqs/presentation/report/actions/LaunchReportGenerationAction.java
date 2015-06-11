package com.compuware.caqs.presentation.report.actions;


import com.compuware.caqs.constants.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.domain.dataschemas.workflow.ReportGeneratorWorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.WorkflowSvc;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import com.compuware.caqs.service.messages.MessagesSvc;
import java.util.Locale;

public class LaunchReportGenerationAction extends ExtJSAjaxAction {

    private static final String WORKFLOW_REPORT_GENERATOR_CONFIG = "workflow_report_generator_config";

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();

        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        Locale locale = RequestUtil.getLocale(request);
        Users user = RequestUtil.getConnectedUser(request);

        String idGeneratingReportMsg = MessagesSvc.getInstance().addMessageWithStatus(TaskId.GENERATING_REPORT, eltBean.getId(),
                        eltBean.getBaseline().getId(), user.getId(), null, null, MessageStatus.IN_PROGRESS);
        MessagesSvc.getInstance().setInProgressMessageToStep(idGeneratingReportMsg, "caqs.process.step.reportwait");

        WorkflowSvc workflowSvc = new WorkflowSvc();
        ReportGeneratorWorkflowConfigBean config = (ReportGeneratorWorkflowConfigBean) BusinessFactory.getInstance().getBean(WORKFLOW_REPORT_GENERATOR_CONFIG);
        config.setUserId(user.getId());
        config.setIdMess(idGeneratingReportMsg);
        if(eltBean != null) {
            if(ElementType.EA.equals(eltBean.getTypeElt())) {
                config.setEaId(eltBean.getId());
            }
            config.setBaselineId(eltBean.getBaseline().getId());
            config.setProjectId(eltBean.getProject().getId());
        }
        config.setLanguageId(locale.getLanguage());

        try {
            workflowSvc.startProcess(config);
        } catch (CaqsException e) {
            retour.put("errorMsgKey", "caqs.analysis.workflow.unexpectedexception");
            mLog.error("ProjectAnalysisLaunchAnalyseAction: "
                    + "Unexpected workflow exception : " + e.getMessage());
            MessagesSvc.getInstance().setMessageTaskStatus(idGeneratingReportMsg, MessageStatus.FAILED);
        }

        return retour;
    }
}
