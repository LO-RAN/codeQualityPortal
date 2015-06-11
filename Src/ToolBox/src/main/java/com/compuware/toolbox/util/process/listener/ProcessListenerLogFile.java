/*
 * ProcessListenerLogFile.java
 *
 * Created on August 11, 2004, 5:11 PM
 */

package com.compuware.toolbox.util.process.listener;

import org.apache.log4j.Logger;

import com.compuware.toolbox.util.process.ProcessDescriptor;

public class ProcessListenerLogFile extends AbstractProcessListener {
    static protected Logger mLog = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Process");
    
    public void updateInfo(ProcessDescriptor aProcess){
        ProcessListenerLogFile.mLog.info("\n"+aProcess.toString(0));
    }
    
}
