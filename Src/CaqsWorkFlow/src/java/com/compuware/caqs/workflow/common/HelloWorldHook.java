package com.compuware.caqs.workflow.common;
import org.ow2.bonita.definition.Hook;
import org.ow2.bonita.facade.QueryAPIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;

/**
 * Print all variable values.
 * @author cwfr-fdubois
 *
 */
public class HelloWorldHook implements Hook {

	/**
	 * 
	 */
	public void execute(QueryAPIAccessor accessor, ActivityInstance activity) throws Exception {
		System.out.println("Hello World");
	}

}
