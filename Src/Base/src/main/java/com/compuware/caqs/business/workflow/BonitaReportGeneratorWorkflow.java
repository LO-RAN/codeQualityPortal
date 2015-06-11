package com.compuware.caqs.business.workflow;

import com.compuware.caqs.domain.dataschemas.workflow.ReportGenerationWorkflowThreadPoolConfigBean;
import com.compuware.caqs.domain.dataschemas.workflow.ReportGeneratorWorkflowConfigBean;
import java.util.concurrent.ThreadPoolExecutor;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.compuware.caqs.domain.dataschemas.workflow.WorkflowProcessDefinitionBean;
import com.compuware.caqs.exception.CaqsException;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.Configuration;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.exception.BonitaInternalException;
import org.ow2.bonita.facade.exception.ProcessNotFoundException;
import org.ow2.bonita.facade.exception.VariableNotFoundException;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.SimpleCallbackHandler;

/**
 * The Bonita report generator workflow connector.
 * @author cwfr-dzysman
 */
public final class BonitaReportGeneratorWorkflow implements IReportGeneratorWorkflow {
    public static final String HTTP_SERVER_CONTEXT_PATH = "HTTP_SERVER_CONTEXT_PATH";

    /** The lock for unique instance creation. */
    private static final Object lock = new Object();

    /** The connector unique instance. */
    private static BonitaReportGeneratorWorkflow instance;

    /** The Bonita user login. */
    private String userLogin;

    /** The Bonita user password. */
    private String userPwd;

    private ReportGenerationWorkflowThreadPoolConfigBean workflowThreadPool;

    private ThreadPoolExecutor threadPool;

    /**
     * The connector constructor.
     * @param userLogin the user login.
     * @param userPwd the user password.
     * @param wtp the thread pool configuration.
     */
    private BonitaReportGeneratorWorkflow(String userLogin, String userPwd, ReportGenerationWorkflowThreadPoolConfigBean wtp) {
        this.userLogin = userLogin;
        this.userPwd = userPwd;
        this.workflowThreadPool = wtp;
        this.threadPool = new ThreadPoolExecutor(
                this.workflowThreadPool.getThreadCount(),
                this.workflowThreadPool.getThreadMaxPoolSize(), 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
//        this.threadPool.setCorePoolSize(this.workflowThreadPool.getThreadCount());
//        this.threadPool.setMaximumPoolSize(this.workflowThreadPool.getThreadMaxPoolSize());
    }

    /**
     * Get an instance of the connector and create one if no instance exists.
     * @param userLogin the user login.
     * @param userPwd the user password.
     * @param threadPoolConfigList the different thread pool configurations.
     * @return the unique instance of the connector.
     */
    public static BonitaReportGeneratorWorkflow getInstance(String userLogin, String userPwd, ReportGenerationWorkflowThreadPoolConfigBean wtp) {
        synchronized (lock) {
            if (instance == null) {
                instance = new BonitaReportGeneratorWorkflow(userLogin, userPwd, wtp);
            }
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    public void start(ReportGeneratorWorkflowConfigBean config) throws CaqsException {
        LoginContext login;
        SimpleCallbackHandler handler = new SimpleCallbackHandler(this.userLogin, this.userPwd);
        try {
            login = new LoginContext("CaqsBonita", handler);
            login.login();
            RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
            QueryDefinitionAPI queryDefinitionAPI = AccessorUtil.getQueryDefinitionAPI();
            WorkflowProcessDefinitionBean processDefinitionBean = this.workflowThreadPool.getProcessDefinition();
            ProcessDefinition definition = queryDefinitionAPI.getProcess(
                    processDefinitionBean.getProcessName(),
                    processDefinitionBean.getProcessVersion());
            ProcessInstanceUUID processInstance = runtimeAPI.instantiateProcess(definition.getUUID(),
                    getProcessParameters(processDefinitionBean, config));
            this.threadPool.execute(new ProcessExecutor(processInstance, this.userLogin, this.userPwd));
            login.logout();
        }
        catch (LoginException e) {
            try {
                Configuration.getConfiguration().refresh();
                login = new LoginContext("CaqsBonita", handler);
                login.login();

            }
            catch (LoginException e2) {
                logger.error("Error during workflow login", e);
                throw new CaqsException("Error during workflow login", e);
            }
        }
        catch (ProcessNotFoundException e) {
            logger.error("Error during process instanciation", e);
            throw new CaqsException("Error during process instanciation", e);
        }
        catch (VariableNotFoundException e) {
            logger.error("Error during process instanciation", e);
            throw new CaqsException("Error during process instanciation", e);
        }
        catch (BonitaInternalException e) {
            logger.error("Error during process instanciation", e);
            throw new CaqsException("Error during process instanciation", e);
        }
    }

    private Map<String, Object> getProcessParameters(WorkflowProcessDefinitionBean processDefinitionBean, ReportGeneratorWorkflowConfigBean config) {
        Map<String, Object> result = config.asMap();
        result.put(HTTP_SERVER_CONTEXT_PATH, processDefinitionBean.getHttpServerContextPath());
        return result;
    }

}
