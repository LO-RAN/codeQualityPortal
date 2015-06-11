/**
 * 
 */
package com.compuware.caqs.flow;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.compuware.optimal.flow.ParameterData;
import com.compuware.optimal.flow.Task;
import com.compuware.optimal.flow.WorkflowException;

/**
 * @author cwfr-fdubois
 *
 */
public class BatchTask implements Runnable, Comparable<BatchTask> {

	private static Logger logger = Logger.getLogger("TaskServer");
	
	private Task task = null;
	private String batchClassName = null;
	private String batchMethodName = null;
	private ParameterData flowParameters = null;
	
	private Date processCreationTime = null;
	
	public BatchTask(Task t, String batchClassName, String batchMethodName, ParameterData flowParameters, Date processCreationTime) {
		this.task = t;
		this.batchClassName = batchClassName;
		this.batchMethodName = batchMethodName;
		this.flowParameters = flowParameters;
		this.processCreationTime = processCreationTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			// Report the beginning of the task to the workflow system
			logger.info("...Executing task : " + task.getName());
			logger.info("...Parameters     : " + flowParameters);
			
			ParameterData newParameters = executeBatchTask(this.batchClassName, this.batchMethodName, this.flowParameters);
			logger.info("...Execution result: " + newParameters);

			// Report the completion of the task component to the workflow system,
			// passing in the return parameter.
			logger.info("...Completing task : " + task.getName());
			task.complete(newParameters);
		}
		catch (IllegalArgumentException e) {
			failTask("Illegal Argument " + this.flowParameters, e);
		}
		catch (WorkflowException e) {
			failTask("Error during task execution ", e);
		}
	}
	
	private ParameterData executeBatchTask(String className, String methodName,
			ParameterData flowParameters) throws WorkflowException {
		ParameterData result;
		try {
			Class classs = Class.forName(className);

			Set inParams = flowParameters.getInputs();
			logger.debug("Input parameters :" + inParams.toString());

			// Construct an array of the types of input parameters, necessary to find the
			// method with reflective API.
			// Construct an array of values of input parameters, necessary to do the actual invoke
			// of the found method.
			Class[] parameterTypes = new Class[inParams.size()];
			Object[] parameterValues = new Object[inParams.size()];
			Iterator inIterator = inParams.iterator();
			int count = 0;
			while (inIterator.hasNext()) {
				String paramName = (String) inIterator.next();
				Class paramTypeClass = flowParameters.getType(paramName);

				// Convert the two primitives used in OptimalFlow (Boolean and Double) to
				// the actual primitive type.
				if (paramTypeClass.isAssignableFrom(Class.forName("java.lang.Boolean"))) {
					parameterTypes[count] = Boolean.TYPE;
				}
				else if (paramTypeClass.isAssignableFrom(Class.forName("java.lang.Double"))) {
					parameterTypes[count] = Double.TYPE;
				}
				else {
					parameterTypes[count] = paramTypeClass;
				}

				parameterValues[count] = flowParameters.getValue(paramName);
				logger.debug("...Input parameter # " + count + ", type : " + parameterTypes[count] + ", value : " + parameterValues[count]);
				count++;
			}

			//Instantiate the class and invoke the specified method.
			logger.debug("Creating an instance of class: " + className);
			Object obj = classs.newInstance();
			java.lang.reflect.Method method = classs.getMethod(methodName, parameterTypes);
			logger.debug("Invoking method: " + method.toString());
			Object outParamValue = method.invoke(obj, parameterValues);

			//Place the result of the method in the first modeled output parameter.
			Set outParamNames = flowParameters.getOutputs();
			Iterator outIterator = outParamNames.iterator();
			if (outIterator.hasNext()) {
				String outParamName = (String) outIterator.next();
				flowParameters.put(outParamName, outParamValue);
			}
			return flowParameters;

		}
		catch (InvocationTargetException e) {
			throw new WorkflowException("Error executing: " + className + "." + methodName + ":" + e.getTargetException());
		}
		catch (Exception e) {
			throw new WorkflowException("Error executing: " + className + "." + methodName + ":" + e);
		}
	}
	
	private void failTask(String message, Exception e) {
		try {
			this.task.fail(message + ':' + e.toString());
		}
		catch (WorkflowException e1) {
			logger.error("Error during task fail", e);
		}
	}
	
	public Date getProcessCreationTime() {
		return this.processCreationTime;
	}

	public int compareTo(BatchTask p) {
		int result = 0;
		if (this.processCreationTime != null && p != null && p.getProcessCreationTime() != null) {
			result = this.processCreationTime.compareTo(p.getProcessCreationTime());
		}
		return result;
	}
	
}
