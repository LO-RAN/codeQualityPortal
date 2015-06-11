package com.compuware.caqs.flow;
/*
 * © 2002 Compuware Corporation. All rights reserved.
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 */

/* Disclaimer
 * You have a royalty-free right to use, modify, reproduce and distribute this
 * sample code (and/or any modified version) in any way you find useful,
 * provided that you agree that Compuware has no warranty obligations or
 * liability for any sample code which has been modified.
 */
//package com.compuware.optimal.flow.example;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

import com.compuware.caqs.flow.bean.ProcessInstance;
import com.compuware.optimal.flow.Activity;
import com.compuware.optimal.flow.ActivityFilter;
import com.compuware.optimal.flow.ActivityList;
import com.compuware.optimal.flow.Operator;
import com.compuware.optimal.flow.ParameterData;
import com.compuware.optimal.flow.Process;
import com.compuware.optimal.flow.ProcessFilter;
import com.compuware.optimal.flow.ProcessList;
import com.compuware.optimal.flow.Task;
import com.compuware.optimal.flow.TaskDef;
import com.compuware.optimal.flow.TaskDefList;
import com.compuware.optimal.flow.WorkflowConnector;
import com.compuware.optimal.flow.WorkflowException;

/**
 * Demo of a Java Task server serving Java batch tasks.
 *
 * @author  Herm Flink
 * @version 2.0
 *
 */
public class TaskServer {

	private static final String DEFAULT_CONNECTION_URL = "UNET:localhost+14000|caqs|cpwrcaqs|unet";
	private static final int DEFAULT_THREADPOOL_SIZE = 10;
	private static final int DEFAULT_THREAD_COUNT = 2;
	
	protected final String connectionUrl;
	private final ThreadPoolExecutor threadPool;
	
	private static Logger logger = Logger.getLogger("TaskServer");
	private WorkflowConnector w;

	private boolean shutdown = false;
	
	//Contructor
	public TaskServer(int threadCount, int threadPoolSize, String connectionUrl) {
		this.threadPool = new ThreadPoolExecutor(threadCount, threadCount, threadPoolSize, TimeUnit.SECONDS, new PriorityBlockingQueue());
		this.connectionUrl = connectionUrl;
	}

	public static void main(String[] args) {
		int threadCount = DEFAULT_THREAD_COUNT;
		int threadPoolSize = DEFAULT_THREADPOOL_SIZE;
		String connectionUrl = DEFAULT_CONNECTION_URL;
		if (args.length == 1) {
			File resourceDirectory = new File(args[0]);
			if (resourceDirectory.exists() && resourceDirectory.isDirectory()) {
				File configFile = new File(resourceDirectory, "config.properties");
				Properties config = TaskServer.readPropertiesFromFile(configFile);
				if (config != null) {
					String urlProp = config.getProperty("unifaceflow.connection.url");
					if (urlProp != null && urlProp.length() > 0) {
						connectionUrl = urlProp;
					}
					String threadCountProp = config.getProperty("unifaceflow.taskserver.threadpool.count");
					if (threadCountProp != null && threadCountProp.length() > 0) {
						threadCount = Integer.parseInt(threadCountProp);
					}
					String threadPoolSizeProp = config.getProperty("unifaceflow.taskserver.threadpool.size");
					if (threadPoolSizeProp != null && threadPoolSizeProp.length() > 0) {
						threadPoolSize = Integer.parseInt(threadPoolSizeProp);
					}
				}
				File log4jFile = new File(resourceDirectory, "log4j.properties");
				PropertyConfigurator.configure(log4jFile.getAbsolutePath());
			}
			else {
				printUsage();
			}
		}
		else {
			printUsage();
		}
		TaskServer ts = new TaskServer(threadCount, threadPoolSize, connectionUrl);
		ts.run();
	}
	
	private static void printUsage() {
		System.out.println("Usage: ");
		System.out.println("       java -cp taskserver-1.0.jar;uniface_flow.jar;unet.jar com.compuware.caqs.flow.TaskServer [CONFIG_DIR]");
	}

    /**
     * Read a Properties file from a filePath.
     * @param fileName the properties file path.
     * @return the Properties definition found.
     */
    private static Properties readPropertiesFromFile(File f) {
        Properties prop = new Properties();
        try {
            if (f.exists()) {
                prop.load(new FileInputStream(f));
            }
        }
        catch (java.io.IOException io) {
            logger.error("### PropertiesReader, IO Exception, could not find the file "+f.getPath()+" ###");
            logger.error("### PropertiesReader, IO Exception: " + io.getMessage());
        }
        return prop;
    }
	
	public void run() {
		try {
			// Open a connection for the task server
			logger.info("Logging on to the workflow system.");
			w = new WorkflowConnector(connectionUrl, "TaskServer", "_");
			while (!shutdown) {
				mainLoop();
				//Wait for one minute
				Thread.sleep(6000);
				if (this.threadPool.getActiveCount() == 0) {
					// Refresh the connection.
					w.close();
					w = new WorkflowConnector(connectionUrl, "TaskServer", "_");
				}
			}
		}
		catch (WorkflowException e) {
			logger.error("Error: Failed to run the main loop. " + e);
		}
		catch (InterruptedException e) {
			logger.error("Interrupt from another thread.");
		}
		finally {
			//Logoff and close the session
			try {
				w.close();
			} catch (WorkflowException e) {
				logger.error("Abnormal termination.");
			}
			threadPool.shutdown();
		}
	}

	public void mainLoop() throws WorkflowException {
		try {
			
			logger.trace("Checking for scheduled batch tasks ...");

			ActivityList activityList = new ActivityList(w);
			logger.trace("ActivityList created...");

			// Set a filter to retrieve Running batch task activities
			ActivityFilter activityFilter = new ActivityFilter("");
			activityFilter.add(Activity.EXTENDED_TYPE, Operator.TOKEN_EQUALS_STRING, "TaskBatch");
			activityFilter.add(Activity.EXTENDED_STATE, Operator.TOKEN_EQUALS_STRING, "Running");
			logger.trace("ActivityFilter created...");
			activityList.setFilter(activityFilter);
			logger.trace("ActivityFilter affected...");

			// Retrieve the matching set of activities
			List activities = activityList.getTop();
			if (activities != null && activities.size() > 0) {
				logger.info("Nr of running batch tasks found: " + activities.size());
	
				Map<String,ProcessInstance> processMap = extractProcessesDef(w);
				
				// Loop the activities and check that the state is scheduled
				Iterator actIterator = activities.iterator();
				while (actIterator.hasNext()) {
					logger.debug("Task found...");
					Activity activity = (Activity) actIterator.next();
	
					if (activity.getState().equals("Scheduled")) {
						
						logger.info("Handling scheduled batch task : " + activity.getName());
						//Obtain the component properties from the task definition
						TaskDefList taskDefList = new TaskDefList(w);
						TaskDef taskDef = (TaskDef) taskDefList.getItemFromID(activity.getDefinitionID());
						Map compSpec = taskDef.getCSComponent();
						logger.debug("...Comp spec. : " + compSpec);
						logger.debug("...Parameters : " + activity.getParameters());
	
						String methodName = (String) compSpec.get("operation");
						String className = (String) compSpec.get("component");
						Task task = (Task) activity;
						String processId = activity.getProcessID();
						ProcessInstance procInstance = processMap.get(processId);
						try {
							// Get the set of parameters for this activity.
							ParameterData parameters = activity.getParameters();
							task.begin();
							threadPool.execute(new BatchTask(task, className, methodName, parameters, procInstance.getCreationTime()));
						}
						catch (WorkflowException wfe) {
							// Report failure and reason to the workflow system.
							task.fail(wfe.toString());
							logger.error("Failed to execute batch task :" + wfe);
						}
					}
				}
			}
		}
		catch (WorkflowException wfe) {
			//Fatal error: stop the main loop.
			logger.error("Fatal error: message=" + wfe.getMessage());
			logger.error("Failed to execute batch task :" + wfe);
			w = new WorkflowConnector(connectionUrl, "TaskServer", "_");
			//throw new WorkflowException();
		}
	}
	
	private Map<String,ProcessInstance> extractProcessesDef(WorkflowConnector w) throws WorkflowException {
		Map<String,ProcessInstance> result = new HashMap<String,ProcessInstance>();
		
		ProcessList processList = new ProcessList(w);

		// Set a filter to retrieve Executing processes
		ProcessFilter processFilter = new ProcessFilter("");
		processFilter.add(Process.STATE, Operator.TOKEN_EQUALS_STRING, "Executing");
		processList.setFilter(processFilter);

		// Retrieve the matching set of activities
		List processes = processList.getTop();
		Iterator i = processes.iterator();
		ProcessInstance currentInstance = null;
		ProcessInstance parentInstance = null;
		while (i.hasNext()) {
			Process p = (Process)i.next();
			currentInstance = new ProcessInstance(p.getID());
			currentInstance.setCreationTime(p.getCreationTime());
			parentInstance = result.get(p.getProcessID());
			if (parentInstance != null) {
				currentInstance.setParent(parentInstance);
				parentInstance.addChild(currentInstance);
			}
			result.put(currentInstance.getId(), currentInstance);
		}
		return result;
	}
	
	public void shutdown() throws WorkflowException {
	    this.shutdown = true;
	    threadPool.shutdownNow();
	    w.close();
	}

}
