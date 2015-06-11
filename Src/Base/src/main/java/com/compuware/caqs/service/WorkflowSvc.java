package com.compuware.caqs.service;

import com.compuware.caqs.business.workflow.IReportGeneratorWorkflow;
import java.util.List;

import com.compuware.caqs.business.workflow.IWorkflow;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.workflow.ReportGeneratorWorkflowConfigBean;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import com.compuware.caqs.util.IDCreator;

/**
 *
 * @author cwfr-fdubois
 */
public class WorkflowSvc {

    /**
     * The workflow bean factory ID.
     */
    private static final String WORKFLOW = "workflow";

    /**
     * The workflow bean factory ID.
     */
    private static final String REPORT_GENERATION_WORKFLOW = "reportGenerationWorkflow";

    /** Start a new workflow process.
     * @param config the process config.
     * @throws CaqsException if the workflow encounter an error.
     */
    public void startProcess(WorkflowConfigBean config) throws CaqsException {
        ProjectSvc projectSvc = ProjectSvc.getInstance();
        ProjectBean projectBean = projectSvc.retrieveProjectById(config.getProjectId());
        if (projectBean != null) {
            DialecteSvc dialecteSvc = DialecteSvc.getInstance();
            List<DialecteBean> dialects = dialecteSvc.retrieveDialects(config.getEaList().split(","));
            config.setDialects(dialects);
            if (config.getEaList() == null || config.getEaList().length() < 1) {
                ElementSvc elementSvc = ElementSvc.getInstance();
                ElementBean project = new ElementBean();
                project.setId(config.getProjectId());
                project.setProject(projectBean);
                List<ElementBean> eaList = elementSvc.retrieveAllApplicationEntitiesForProject(project);
                config.setEaList(listAsString(eaList));
            }
            config.setBaselineId(IDCreator.getID());
            BusinessFactory factory = BusinessFactory.getInstance();
            IWorkflow workflow = (IWorkflow) factory.getBean(WORKFLOW);
            workflow.start(config);
        } else {
            throw new CaqsException("caqs.analysis.workflow.invalidprojectid", "Invalid project ID");
        }
    }

    /** Start a new report generation workflow process.
     * @param config the report generation process config.
     * @throws CaqsException if the workflow encounter an error.
     */
    public void startProcess(ReportGeneratorWorkflowConfigBean config) throws CaqsException {
        BusinessFactory factory = BusinessFactory.getInstance();
        IReportGeneratorWorkflow workflow = (IReportGeneratorWorkflow) factory.getBean(REPORT_GENERATION_WORKFLOW);
        workflow.start(config);
    }

    private String listAsString(List<ElementBean> l) {
        StringBuilder result = new StringBuilder();
        if (l != null && l.size() > 0) {
            for (ElementBean elt : l) {
                if (result.length() > 0) {
                    result.append(',');
                }
                result.append(elt.getId());
            }
        }
        return result.toString();
    }
}
