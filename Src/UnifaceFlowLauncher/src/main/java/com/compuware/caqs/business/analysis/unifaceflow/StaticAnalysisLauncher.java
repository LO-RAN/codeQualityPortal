package com.compuware.caqs.business.analysis.unifaceflow;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.compuware.optimal.flow.Actor;
import com.compuware.optimal.flow.AvailableTask;
import com.compuware.optimal.flow.AvailableTaskFilter;
import com.compuware.optimal.flow.AvailableTaskList;
import com.compuware.optimal.flow.Operator;
import com.compuware.optimal.flow.ParameterData;
import com.compuware.optimal.flow.Task;
import com.compuware.optimal.flow.TaskList;
import com.compuware.optimal.flow.WorkflowConnector;
import com.compuware.optimal.flow.WorkflowException;

/**
 * @author cwfr-lizac
 * 
 * NB: make sure this class does not depend on CAQS classes
 *     as it is to be called also from Ant in a minimal context.
 * 
 */
public class StaticAnalysisLauncher {

	private static Logger logger;
	
    public StaticAnalysisLauncher(Logger logger) {
    	this.logger=logger;
    }

	/**
	 * @param ActorId        : User Id (from UnifaceView authentication)
	 * @param connectionCfg  : UnifaceFlow connection parameters (typically : "UNET:localhost+14000|caqs|cpwrcaqs|uniface_flow")
	 * @param projectId      : Project Id 
	 * @param projectName    : project Name (given to help end user when monitoring process execution)
	 * @param eaList         : list of EAs to process (Comma Separated Values)
	 * @param baselineName   : Baseline Name (if empty, a default name will be constructed from timestamp)
   * @param eaOptionList   : list of EA Options (Comma Separated Values)
	 * @throws WorkflowException
	 */
	public void run(String ActorId, String connectionCfg, String projectId,
			String projectName, String eaList, String baselineName, String eaOptionList)
			throws WorkflowException {
		
		boolean taskFound = false;
		
			// Start a session without password validation:
			// session is started for the given Actor :
			WorkflowConnector w = new WorkflowConnector(connectionCfg, ActorId);
	
			// once connected, get the Actor
			Actor actor = w.getActor();
	
			// build a list of available tasks
			AvailableTaskList userdriventasklist = new AvailableTaskList(w);
	
			// set filter on tasks related to our actor
			AvailableTaskFilter availfilter = new AvailableTaskFilter(
					AvailableTask.ACTOR_ID, Operator.STRING_EQUALS, actor.getID());
	
			// apply filter
			userdriventasklist.setFilter(availfilter);
	
			// retrieve list of filtered tasks
			// (only top of list; let's hope there are not too many available tasks
			// for this user)
			List<AvailableTask> udtasks = userdriventasklist.getTop();
	
			logger.debug("Number of Available tasks returned: "
					+ userdriventasklist.getNumber());
	
			// Iterate through the available tasks
			Iterator<AvailableTask> udtasksiterator = udtasks.iterator();
	
			while (!taskFound && udtasksiterator.hasNext()) {
	
				AvailableTask udtask = (AvailableTask) udtasksiterator.next();
	
				// consider only the task named "Static analysis".
				// TODO : find a safer way to recognize this task
				if (udtask.getName().contains("Static analysis")) {
	
					logger.debug(udtask.getTaskID() + "," + udtask.getName());
	
					// now that we know the task Id, let's get the actual task
	
					// build a list of all tasks
					TaskList tasklist = new TaskList(w);
					// get the desired task
					Task task = (Task) tasklist.getItemFromID(udtask.getTaskID());
	
					// get its parameters
					ParameterData paramdata = task.getParameters();
	
					// we are interested in so called "output" parameters
					// which are the values this manual task outputs to the rest
					// of the process (following tasks) after they are typed in
					// by an end user (in an interactive scenario) or filled by
					// a batch process (which is the case here)
	
					Set<String> outputs = paramdata.getOutputs();
					Iterator<String> outputsit = outputs.iterator();
	
					while (outputsit.hasNext()) {
	
						// get the parameter's name
						String paramName = (String) outputsit.next();
	
						logger.debug("Found parameter : " + paramName);
	
						// if we found the parameter named "projectid"
						// TODO: find a safer way to recognize this parameter
						if (paramName.equalsIgnoreCase("projectid")) {
	
							logger.debug("String value is set for parameter "
									+ paramName + " = " + projectId);
	
							// give it the value of the project Id we want to start
							// this process for
							paramdata.setValue(paramName, projectId);
						}
	
						if (paramName.equalsIgnoreCase("projectName")) {
	
							logger.debug("String value is set for parameter "
									+ paramName + " = " + projectName);
	
							// give it the value of the project Id we want to start
							// this process for
							paramdata.setValue(paramName, projectName);
						}
	
						if (paramName.equalsIgnoreCase("eaList")) {
	
							logger.debug("String value is set for parameter "
									+ paramName + " = " + eaList);
	
							// give it the value of the project Id we want to start
							// this process for
							paramdata.setValue(paramName, eaList);
						}
	
						if (paramName.equalsIgnoreCase("eaOptionList")) {
	
							logger.debug("String value is set for parameter "
									+ paramName + " = " + eaOptionList);
	
							// give it the value of the project Id we want to start
							// this process for
							paramdata.setValue(paramName, eaOptionList);
						}
	
						// if we found the parameter named "baselinename"
						// TODO: find a safer way to recognize this parameter
						if (paramName.equalsIgnoreCase("baselinename")) {
	
							logger.debug("String value is set for parameter "
									+ paramName + " = " + baselineName);
	
							// give it the value of the baseline name to label this
							// version of the static analysis
							paramdata.setValue(paramName, baselineName);
						}
					}
	
					// once values have been given to the parameters,
					// complete the task.
					// (same as pushing "ok" button after selecting project
					// and filling baseline name in an interactive scenario)
					task.complete(paramdata);
					taskFound = true;
				}
	
			}
	
			// We are done; let's close the session (implicit logoff):
			w.close();
		
		if (!taskFound) {
			throw new WorkflowException("Task not found");
		}
	}

	public static void main(String[] args) {
		StaticAnalysisLauncher launcher = new StaticAnalysisLauncher(Logger.getRootLogger());

		try {
			launcher.run( "QUA1"
					     ,"UNET:localhost+14000|caqs|cpwrcaqs|uniface_flow"
					     ,"20080218153749968119437"
					     ,"project name"
					     ,"ea1"
					     ,"baseline name"
					     , "option1");
		}
		catch (WorkflowException e) {
			logger.error("UnifaceFlow Task execution", e);
		}
	}
}
