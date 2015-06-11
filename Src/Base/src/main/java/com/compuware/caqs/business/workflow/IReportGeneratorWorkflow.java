package com.compuware.caqs.business.workflow;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.workflow.ReportGeneratorWorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-dzysman
 */
public interface IReportGeneratorWorkflow {

    /**
     * The logger.
     */
    static Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * Start a new instance of the given process.
     * @param config the workflow configuration.
     * @throws com.compuware.caqs.exception.CaqsException if the workflow encounter an error.
     */
    void start(ReportGeneratorWorkflowConfigBean config) throws CaqsException;
}
