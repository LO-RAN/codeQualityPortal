/*
 * ProcessListenerManager.java
 *
 * Created on August 25, 2004, 7:02 PM
 */

package com.compuware.toolbox.util.process.listener;

/**
 *
 * @author  cwfr-fxalbouy
 */
public class ProcessListenerManager {
    static {
        System.setProperty("processListener", "logfile") ;
    }
    
    static String mType = System.getProperty("processListener");
    
    public static final String TYPE_SYSTEM_OUT = "systemout";
    public static final String TYPE_LOGGER = "logfile";
    public static final String TYPE_DATABASE = "database";
    
    private static AbstractProcessListener mListener = null;
    
    /** Creates a new instance of ProcessListenerManager */
    public ProcessListenerManager() {
    }
    
    public static final synchronized AbstractProcessListener getListener(){
        if(null == mListener){
            if(null!=mType){
                if(TYPE_SYSTEM_OUT.compareTo(mType)==0){
                    mListener = new ProcessListenerSystemOut();
                }
                else if(TYPE_LOGGER.compareTo(mType)==0){
                    mListener = new ProcessListenerLogFile();
                }
                else if(TYPE_DATABASE.compareTo(mType)==0){
                    mListener = new ProcessListenerDataBase();
                }
                else{
                    mListener = new ProcessListenerSystemOut();
                }
            }
            else{
                // default minimalistic listener implementation
                mListener = new ProcessListenerSystemOut();
            }
        }
        
        return mListener;
    }
    
}
