/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.business.workflow;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Collection;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.exception.BonitaInternalException;
import org.ow2.bonita.facade.exception.IllegalTaskStateException;
import org.ow2.bonita.facade.exception.InstanceNotFoundException;
import org.ow2.bonita.facade.exception.TaskNotFoundException;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;
import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.SimpleCallbackHandler;

/**
 *
 * @author cwfr-fdubois
 */
public class ProcessExecutor implements Runnable {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private ProcessInstanceUUID processInstanceUUID;
    private String userLogin = null;
    private String userPwd = null;

    public ProcessExecutor(ProcessInstanceUUID processInstanceUUID, String userLogin, String userPwd) {
        this.processInstanceUUID = processInstanceUUID;
        this.userLogin = userLogin;
        this.userPwd = userPwd;
    }

    public void run() {
        try {
            LoginContext login = new LoginContext("CaqsBonita", new SimpleCallbackHandler(this.userLogin, this.userPwd));
            login.login();
            RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
            QueryRuntimeAPI queryRuntimeAPI = AccessorUtil.getQueryRuntimeAPI();
            Collection<TaskInstance> taskList = queryRuntimeAPI.getTaskList(processInstanceUUID, ActivityState.READY);
            for (TaskInstance activity : taskList) {
                runtimeAPI.startTask(activity.getUUID(), true);
                runtimeAPI.finishTask(activity.getUUID(), true);
            }
            login.logout();
        }
        catch (LoginException e) {
            logger.error("Error during workflow login", e);
        }
        catch (InstanceNotFoundException e) {
            logger.error("Error during process instanciation", e);
        }
        catch (TaskNotFoundException e) {
            logger.error("Error during process instanciation", e);
        }
        catch (IllegalTaskStateException e) {
            logger.error("Error during process instanciation", e);
        }
        catch (BonitaInternalException e) {
            logger.error("Error during process instanciation", e);
        }
    }

}
