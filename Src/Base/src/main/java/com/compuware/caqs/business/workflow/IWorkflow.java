package com.compuware.caqs.business.workflow;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-fdubois
 */
public interface IWorkflow {

    /**
     * The logger.
     */
    static Logger logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    /**
     * Start a new instance of the given process.
     * @param config the workflow configuration.
     * @throws com.compuware.caqs.exception.CaqsException if the workflow encounter an error.
     */
    void start(WorkflowConfigBean config) throws CaqsException;
}
