/*
 * ProcessorInterface.java
 *
 * Created on August 11, 2004, 3:53 PM
 */

package com.compuware.toolbox.util.process;

/**
 *
 * @author  cwfr-fxalbouy
 */
public interface ProcessorInterface {
    public ProcessDescriptor getProcessDescriptor();
    public ProcessDescriptor initProcessDefinition();
}
