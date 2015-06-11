/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.business.workflow;

import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;

/**
 *
 * @author cwfr-fdubois
 */
public abstract class AbstractWorkflow implements IWorkflow {

    /**
     * The workflow configuration.
     */
    protected WorkflowConfigBean config;

    /**
     * Get the workflow configuration.
     * @return the workflow configuration.
     */
    public WorkflowConfigBean getConfig() {
        return config;
    }

    /**
     * set the workflow configuration.
     * @param config the new workflow configuration.
     */
    public void setConfig(WorkflowConfigBean config) {
        this.config = config;
    }

}
