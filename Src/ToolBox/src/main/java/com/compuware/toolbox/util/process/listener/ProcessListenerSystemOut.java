/*
 * ProcessListenerSystemOut.java
 *
 * Created on August 11, 2004, 5:17 PM
 */

package com.compuware.toolbox.util.process.listener;

import com.compuware.toolbox.util.process.ProcessDescriptor;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class ProcessListenerSystemOut extends AbstractProcessListener  {
    
    /** Creates a new instance of ProcessListenerSystemOut */
    public ProcessListenerSystemOut() {
    }
    
    public void updateInfo(ProcessDescriptor aProcess){
       System.out.println(aProcess.toString(0));
    }
    
}
