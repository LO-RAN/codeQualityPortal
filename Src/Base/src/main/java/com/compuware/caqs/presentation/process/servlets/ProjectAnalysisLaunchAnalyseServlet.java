package com.compuware.caqs.presentation.process.servlets;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.WorkflowSvc;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class ProjectAnalysisLaunchAnalyseServlet extends ProcessServlet {
	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private static final String WORKFLOW_ANALYSIS_CONFIG = "workflow_analysis_config";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        AnalysisResult result = new AnalysisResult();

		String eaList = RequestHelper.getParameterAndForward(request, "eaList");
		String eaOptionList = RequestHelper.getParameterAndForward(request, "eaOptionList");
		String baselineName = RequestHelper.getParameterAndForward(request, "baselineName");
		String projectId = request.getParameter("projectId");
		String projectName = RequestHelper.getParameterAndForward(request, "projectName");
		String userId = RequestHelper.getParameterAndForward(request, "userId");
		String email = RequestHelper.getParameterAndForward(request, "email");

        WorkflowSvc workflowSvc = new WorkflowSvc();
        WorkflowConfigBean config = (WorkflowConfigBean)BusinessFactory.getInstance().getBean(WORKFLOW_ANALYSIS_CONFIG);
        config.setUserId(userId);
        config.setUserEmail(email);
        config.setBaselineName(baselineName);
        config.setEaList(eaList);
        config.setEaOptionList(eaOptionList);
        config.setProjectId(projectId);
        config.setProjectName(projectName);

        try {
            workflowSvc.startProcess(config);
        	result.setSuccess(true);
		}
        catch (CaqsException e) {
        	result.setSuccess(false);
            if (e.getMsgKey() != null && e.getMsgKey().length() > 0) {
            	result.setMessage(resources.getString(e.getMsgKey()));
            }
            else {
            	result.setMessage(resources.getString("caqs.analysis.workflow.unexpectedexception"));
            }
            logger.error("Error launching a new analysis process", e);
		}

        return result;
	}

    public String getServletInfo() {
        return "Project Analysis launcher Servlet";
    }

}
