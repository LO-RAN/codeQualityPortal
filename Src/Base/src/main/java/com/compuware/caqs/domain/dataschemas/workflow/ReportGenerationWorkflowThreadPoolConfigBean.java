package com.compuware.caqs.domain.dataschemas.workflow;

/**
 * Define a workflow thread pool configuration including:
 * <ul>
 *  <li>the thread count</li>
 *  <li>the thread max pool size</li>
 *  <li>the language list associated to the pool</li>
 *  <li>the process definition</li>
 * </ul>
 * @author FredericD
 */
public class ReportGenerationWorkflowThreadPoolConfigBean {

    private int threadCount = 1;
    private int threadMaxPoolSize = 1;

    /**
     * The workflow process definition.
     */
    private WorkflowProcessDefinitionBean processDefinition;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadMaxPoolSize() {
        return threadMaxPoolSize;
    }

    public void setThreadMaxPoolSize(int threadMaxPoolSize) {
        this.threadMaxPoolSize = threadMaxPoolSize;
    }

    public WorkflowProcessDefinitionBean getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(WorkflowProcessDefinitionBean processDefinition) {
        this.processDefinition = processDefinition;
    }

}
