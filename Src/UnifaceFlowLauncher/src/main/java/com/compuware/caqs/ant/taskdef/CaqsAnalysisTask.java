package com.compuware.caqs.ant.taskdef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.compuware.caqs.business.analysis.unifaceflow.StaticAnalysisLauncher;
import com.compuware.optimal.flow.WorkflowException;


public class CaqsAnalysisTask extends Task {
	
    private String actorId;
    private String projectId;
    private String projectName="";
    private String baselineName="";
    private Connection connection;
    private List<Entity> entitySet = new ArrayList<Entity>();
    
    public void setActorId(String id) {
    	this.actorId = id;
    }

    public void setProjectId(String id) {
    	this.projectId = id;
    }

    public void setProjectName(String name) {
    	this.projectName = name;
    }

    public void setBaselineName(String name) {
    	this.baselineName = name;
    }

    public void addEntity(Entity ea) {
         this.entitySet.add(ea);
    }
    
    public Connection createConnection() {
        if (this.connection != null) {
            throw new BuildException("Only one connection element allowed.", getLocation());
        }
        this.connection = new Connection();
        return this.connection;
    }    

    @Override
	public void execute() throws BuildException {

    	// actorId is a mandatory attribute
        if (this.actorId == null) {
            throw new BuildException("actorId attribute is missing.", getLocation());
        }
        
        // projectId is a mandatory attribute
        if (this.projectId == null) {
            throw new BuildException("projectId attribute is missing.", getLocation());
        }
        
        // check at least one entity has been provided 
        if (this.entitySet == null || this.entitySet.isEmpty()) {
            throw new BuildException("At least one entity element is required.", getLocation());
        }
        
        // no connection provided ?
        if (this.connection == null) {
        	// try the default one
            this.connection=new Connection();
        }
        processcaqsAnalysis();
    }
    private void processcaqsAnalysis() {
		StaticAnalysisLauncher launcher = new StaticAnalysisLauncher(Logger.getRootLogger());
 
		log("sending process request from user "+this.actorId+" for project "+this.projectName+" ("+this.projectId+")" );
		
		try {
			launcher.run( this.actorId
					     ,this.connection.toString()
					     ,this.projectId
					     ,this.projectName
					     ,getEntityList()
					     ,this.baselineName
					     ,getEntityOptionList()
					     );
		}
		catch (WorkflowException e) {
			throw new BuildException(e, getLocation());
		}
    }

    private String getEntityList() {
    	String list="";
    	   for (Iterator<Entity> it = this.entitySet.iterator (); it.hasNext (); ) {
    		    Entity ea = it.next ();
    		    list+=ea.getId();
    		    if (it.hasNext ()) {
    		    	list+=",";
    		    }
    		  }
    	   return list;
    }

    private String getEntityOptionList() {
    	String list="";
 	   for (Iterator<Entity> it = this.entitySet.iterator (); it.hasNext (); ) {
 		    Entity ea = it.next ();
 		    list+=ea.getOption();
 		    if (it.hasNext ()) {
 		    	list+=",";
 		    }
 		  }
 	   return list;
   }


}
