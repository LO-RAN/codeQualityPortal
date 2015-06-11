/*
 * ProcessListenerDataBase.java
 *
 * Created on August 25, 2004, 7:12 PM
 */

package com.compuware.toolbox.util.process.listener;

import com.compuware.toolbox.util.process.ProcessDescriptor;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class ProcessListenerDataBase extends AbstractProcessListener {
    
    /** Creates a new instance of ProcessListenerDataBase */
    public ProcessListenerDataBase() {
    }
    
    public void updateInfo(ProcessDescriptor aProcess){
       System.out.println("SHOULD BE IN THE DB : " + aProcess.toString(0));
    }
    
}
