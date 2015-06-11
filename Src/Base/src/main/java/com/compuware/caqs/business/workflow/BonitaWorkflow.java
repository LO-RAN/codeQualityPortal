package com.compuware.caqs.business.workflow;

import com.compuware.caqs.domain.dataschemas.DialecteBean;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.compuware.caqs.domain.dataschemas.workflow.WorkflowConfigBean;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowProcessDefinitionBean;
import com.compuware.caqs.domain.dataschemas.workflow.WorkflowThreadPoolConfigBean;
import com.compuware.caqs.exception.CaqsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * The Bonita workflow connector.
 * @author cwfr-fdubois
 */
public final class BonitaWorkflow implements IWorkflow {

    public static final String DEFAULT_CONFIG_KEY = "default";
    public static final String HTTP_SERVER_CONTEXT_PATH = "HTTP_SERVER_CONTEXT_PATH";
    public static final String HTTP_SERVER_CONTEXT_PATH_2 = "HTTP_SERVER_CONTEXT_PATH_2";
    public static final String HTTP_SERVER_CONTEXT_PATH_3 = "HTTP_SERVER_CONTEXT_PATH_3";
    /** The lock for unique instance creation. */
    private static final Object lock = new Object();
    /** The connector unique instance. */
    private static BonitaWorkflow instance;
    /** The Bonita user login. */
    private String userLogin;
    /** The Bonita user password. */
    private String userPwd;
    /** The thread pool for process multithreading. */
    private final Map<String, ThreadPoolExecutor> threadPoolMap = new HashMap<String, ThreadPoolExecutor>();
    /** The workflow process map. */
    private final Map<String, WorkflowProcessDefinitionBean> workflowProcessMap = new HashMap<String, WorkflowProcessDefinitionBean>();

    /**
     * The connector constructor.
     * @param userLogin the user login.
     * @param userPwd the user password.
     * @param threadPoolConfigList the thread pool configuration list.
     */
    private BonitaWorkflow(String userLogin, String userPwd, List<WorkflowThreadPoolConfigBean> threadPoolConfigList) {
        this.userLogin = userLogin;
        this.userPwd = userPwd;
        for (WorkflowThreadPoolConfigBean threadPoolConfig : threadPoolConfigList) {
            initThreadPoolMap(threadPoolConfig);
        }
    }

    /**
     * Init the thread pool and workflow process maps from the given configuration.
     * @param threadPoolConfig the thread pool configuration.
     */
    private void initThreadPoolMap(WorkflowThreadPoolConfigBean threadPoolConfig) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(threadPoolConfig.getThreadCount(), threadPoolConfig.getThreadMaxPoolSize(), 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPool.setCorePoolSize(threadPoolConfig.getThreadCount());
        threadPool.setMaximumPoolSize(threadPoolConfig.getThreadCount());
        for (String dialect : threadPoolConfig.getDialectList()) {
            threadPoolMap.put(dialect, threadPool);
            workflowProcessMap.put(dialect, threadPoolConfig.getProcessDefinition());
        }
    }

    /**
     * Get an instance of the connector and create one if no instance exists.
     * @param userLogin the user login.
     * @param userPwd the user password.
     * @param threadPoolConfigList the different thread pool configurations.
     * @return the unique instance of the connector.
     */
    public static BonitaWorkflow getInstance(String userLogin, String userPwd, List<WorkflowThreadPoolConfigBean> threadPoolConfigList) {
        synchronized (lock) {
            if (instance == null) {
                instance = new BonitaWorkflow(userLogin, userPwd, threadPoolConfigList);
            }
        }
        return instance;
    }

    /**
     * Retrieve the thread pool executor for the given dialects.
     * If no thread pool executor is found for the dialects, the default one is
     * returned.
     * @param languageList the language list.
     * @return the ThreadPool executor associated to the dialect list.
     */
    private ThreadPoolExecutor getThreadPoolExecutorForDialects(List<DialecteBean> dialectList) {
        ThreadPoolExecutor result = null;
        for (DialecteBean dialect : dialectList) {
            result = this.threadPoolMap.get(dialect.getId());
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            result = this.threadPoolMap.get(DEFAULT_CONFIG_KEY);
        }
        return result;
    }

    /**
     * Retrieve the workflow process definition for the given dialects.
     * If no workflow process definition is found for the dialects, the default
     * one is returned.
     * @param languageList the language list.
     * @return the workflow process definition associated to the dialect list.
     */
    private WorkflowProcessDefinitionBean getWorkflowProcessForDialects(List<DialecteBean> dialectList) {
        WorkflowProcessDefinitionBean result = null;
        for (DialecteBean dialect : dialectList) {
            result = this.workflowProcessMap.get(dialect.getId());
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            result = this.workflowProcessMap.get(DEFAULT_CONFIG_KEY);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void start(WorkflowConfigBean config) throws CaqsException {
        LoginContext login;
        SimpleCallbackHandler handler = new SimpleCallbackHandler(this.userLogin, this.userPwd);
        try {
            login = new LoginContext("CaqsBonita", handler);
            login.login();
            RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
            QueryDefinitionAPI queryDefinitionAPI = AccessorUtil.getQueryDefinitionAPI();
            WorkflowProcessDefinitionBean processDefinitionBean = getWorkflowProcessForDialects(config.getDialects());
            ProcessDefinition definition = queryDefinitionAPI.getProcess(processDefinitionBean.getProcessName(), processDefinitionBean.getProcessVersion());
            ProcessInstanceUUID processInstance = runtimeAPI.instantiateProcess(definition.getUUID(), getProcessParameters(processDefinitionBean, config));
            ThreadPoolExecutor threadPool = getThreadPoolExecutorForDialects(config.getDialects());
            threadPool.execute(new ProcessExecutor(processInstance, this.userLogin, this.userPwd));
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

    private Map<String, Object> getProcessParameters(WorkflowProcessDefinitionBean processDefinitionBean, WorkflowConfigBean config) {
        Map<String, Object> result = config.asMap();
        result.put(HTTP_SERVER_CONTEXT_PATH, processDefinitionBean.getHttpServerContextPath());
        result.put(HTTP_SERVER_CONTEXT_PATH_2, processDefinitionBean.getHttpServerContextPath2());
        result.put(HTTP_SERVER_CONTEXT_PATH_3, processDefinitionBean.getHttpServerContextPath3());
        return result;
    }
}
