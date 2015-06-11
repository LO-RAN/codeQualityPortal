/**
 * 
 */
package com.compuware.caqs.presentation.process.servlets;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.business.analysis.StaticAnalysisConfig;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.presentation.common.servlets.ResultServlet;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class ProcessServlet extends ResultServlet {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private static final String STEP_PARAMETER_KEY = "step";
	public static final String MASTER_PARAMETER_KEY = "master";
	public static final String TOOL_PARAMETER_KEY = "tool";
	public static final String EA_LIST_SEPARATOR_KEY = ",";
	public static final String EA_LIST_PARAMETER_KEY = "eaList";
	public static final String EA_OPTION_LIST_PARAMETER_KEY = "eaOptionList";
	public static final String BASELINE_ID_PARAMETER_KEY = "baselineId";
	public static final String PROJECT_ID_PARAMETER_KEY = "projectId";
    public static final String ANALYSE_AND_LOAD = "analyseandload";
    public static final String ANALYSE = "analyse";
    public static final String LOAD = "load";

	protected AnalysisConfig extractAnalisysConfig(HttpServletRequest request) {
		AnalysisConfig result = new AnalysisConfig();
		initAnalysisConfig(result, request);
		return result;
	}

	protected StaticAnalysisConfig extractStaticAnalisysConfig(HttpServletRequest request) {
		StaticAnalysisConfig result = new StaticAnalysisConfig();
		initAnalysisConfig(result, request);
		initParsingConfig(result, request);
		return result;
	}

	private void initAnalysisConfig(AnalysisConfig config, HttpServletRequest request) {
		config.setProjectId(request.getParameter(PROJECT_ID_PARAMETER_KEY));
		config.setBaselineId(request.getParameter(BASELINE_ID_PARAMETER_KEY));

		String eaListStr = request.getParameter(EA_LIST_PARAMETER_KEY);
        if (eaListStr != null) {
        	config.setModuleArray(eaListStr.split(EA_LIST_SEPARATOR_KEY));
        }
        
        String eaOptionListStr = request.getParameter(EA_OPTION_LIST_PARAMETER_KEY);
        if (eaOptionListStr != null) {
        	eaOptionListStr += " ";
        	config.setModuleOptionArray(eaOptionListStr.split(EA_LIST_SEPARATOR_KEY));
        }
        
	}

	private void initParsingConfig(StaticAnalysisConfig config, HttpServletRequest request) {
        config.setToolId(request.getParameter(TOOL_PARAMETER_KEY));

        String master = request.getParameter(MASTER_PARAMETER_KEY);
        String step = request.getParameter(STEP_PARAMETER_KEY);

        if (master != null) {
            config.setMasterTool(Boolean.valueOf(master).booleanValue());
        }

        boolean analyse = false;
        boolean load = false;
        if (ANALYSE_AND_LOAD.compareTo(step) == 0) {
            analyse = true;
            load = true;
        } else if (ANALYSE.compareTo(step) == 0) {
            analyse = true;
        } else if (LOAD.compareTo(step) == 0) {
            load = true;
        }
        
        config.setAnalyse(analyse);
        config.setLoad(load);
    }	
}
