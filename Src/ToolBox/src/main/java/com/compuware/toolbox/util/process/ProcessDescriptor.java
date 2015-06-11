/*
 * Process.java
 *
 * Created on August 11, 2004, 2:38 PM
 */
package com.compuware.toolbox.util.process;

import java.util.Vector;

import com.compuware.toolbox.util.process.listener.AbstractProcessListener;
import com.compuware.toolbox.util.process.listener.ProcessListenerManager;

public class ProcessDescriptor{ 
    protected AbstractProcessListener mListener = ProcessListenerManager.getListener();    
       
    private static final int HUNDRED_PERCENT = 100;
    
    protected String mId;
    protected String mName;
    protected String mDescription;    
    protected boolean mStarted = false;
    protected boolean mCompleted = false;
    protected boolean mAcknowledged = false;
    protected int mPercentage = 0;
    protected ProcessDescriptor mParent;
    protected Vector mSubProcessesList = new Vector();
     
    /** Creates a new instance of Process */
    public ProcessDescriptor(String aName) {
        this.mName = aName;
        this.mListener.updateInfo(this);
    }    
    
    //accessors
    public void setParent(ProcessDescriptor parent){
        this.mParent = parent;
    }
    
    public ProcessDescriptor getParent(){
        return this.mParent;
    }
    
    public void setId(String id){
        this.mId = id;
    }
    
    public String getId(){
        return this.mId;
    }
    
    public void setDescription(String aDescription){
        this.mDescription = aDescription;
    }
    
    public String getDesciption(){
        return this.mDescription;
    }
    
    public void setStarted(boolean start){
        if(null != this.mParent){
            this.mParent.setStarted(true);
        }
        this.mStarted = true;
        this.mListener.updateInfo(this);
    }
    
    public boolean isStarter(){
        return this.mStarted;
    }
    
    public void setCompleted(boolean completed){
        this.mCompleted = completed;
        if(this.mCompleted && (HUNDRED_PERCENT > this.mPercentage) ){
            this.mPercentage = HUNDRED_PERCENT;
        }
        if(!this.mStarted){
            this.mStarted = true;
        }
        this.mListener.updateInfo(this);
    }
    
    public boolean isCompleted(){
        return this.mCompleted;
    }
    
    public void setAcknowledged(boolean acknowledged){
        this.mAcknowledged = acknowledged;
        for(int i = 0 ; i < this.mSubProcessesList.size() ; i++){
            ProcessDescriptor currentChild = (ProcessDescriptor) this.mSubProcessesList.elementAt(i);
            currentChild.setAcknowledged(true);
        }
        this.mListener.updateInfo(this);
    }
    
    public boolean isAcknowledged(){
        return this.mAcknowledged;
    }
    
    public void setPercentage(int percentage){                
        this.mPercentage = percentage;
        
        if (!this.mStarted && (0 < this.mPercentage)){
            this.mStarted = true;
        }
        if((HUNDRED_PERCENT == this.mPercentage)&& (!this.mCompleted)){
            this.mCompleted = true;
        }
        this.mListener.updateInfo(this);
    }
    
    public int getPercentage(){
        return this.mPercentage;
    }
    
    public void addSubProcess(ProcessDescriptor aStep){
        this.mSubProcessesList.add(aStep);
        aStep.setParent(this);
        this.mListener.updateInfo(this);
    }
    
    public Vector getSubProcesses(){
        return this.mSubProcessesList;
    }
    
    public String toString(int level){
        String out = "Process : "+ this.mName+ " ";
        out += "started : " + this.mStarted + " ";
        out += "completed : "+ this.mCompleted +" ";
        out += "acknowledged : "+ this.mAcknowledged + " ";
        out += "completion : " + this.mPercentage + "%\n";
        int ajustedlevel = level + 1;
        for (int i = 0 ; i < this.mSubProcessesList.size() ; i++){            
            for (int j = 0; j < ajustedlevel ; j++){
                out+="\t\t";
            }
            out+= ((ProcessDescriptor) this.mSubProcessesList.elementAt(i)).toString(ajustedlevel);
        }
        return out;
    }
}
