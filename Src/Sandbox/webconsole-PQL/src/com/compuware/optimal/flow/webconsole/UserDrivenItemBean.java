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
package com.compuware.optimal.flow.webconsole;


/**
 *
 * This bean holds some of the properties of the Workflow Connector
 * <strong>AvailableTask</strong> object combined with the taskType
 * property of the <strong>TaskDef</strong> object.
 *
 * @see UserDrivenTaskListAction
 * @see UserDrivenItemsBean
 */

public class UserDrivenItemBean {

   /**
    * The id of the User-driven task.
    */
   private String id;

   /**
    * The name of the User-driven task.
    */
   private String name;

   /**
    * The release name of the User-driven task.
    */
   private String releasename;

   /**
    * The release state of the User-driven task.
    */
   private String releasestate;

   /**
    * The release version of the User-driven task.
    */
   private String releaseversion;

   /**
    * The id of the related task.
    */
   private String taskid;

   /**
    * The definition id of the related task.
    */
   private String taskdefid;

   /**
    * The type of the User-driven task.
    */
   private String tasktype;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */
   public UserDrivenItemBean() {
      this.id = "";
      this.name = "";
      this.releasename = "";
      this.releasestate = "";
      this.releaseversion = "";
      this.taskid = "";
      this.taskdefid = "";
      this.tasktype = "";
   }

   /**
    * Set the id of the User-driven task.
    */
   public void setId(String id) {
	   this.id = id;
   }

   /**
    * Get the id of the User-driven task.
    */
   public String getId() {
	   return this.id;
   }

   public void setName(String name) {
	   this.name = name;
   }

   public String getName() {
	   return this.name;
   }

   public void setReleaseName(String releasename) {
	   this.releasename = releasename;
   }

   public String getReleaseName() {
	   return this.releasename;
   }

   public void setReleaseState(String releasestate) {
	   this.releasestate = releasestate;
   }

   public String getReleaseState() {
	   return this.releasestate;
   }

   public void setReleaseVersion(String releaseversion) {
	   this.releaseversion = releaseversion;
   }

   public String getReleaseVersion() {
	   return this.releaseversion;
   }

   public void setTaskId(String taskid) {
	   this.taskid = taskid;
   }

   public String getTaskId() {
	   return this.taskid;
   }

   public void setTaskDefId(String taskdefid) {
	   this.taskdefid = taskdefid;
   }

   public String getTaskDefId() {
	   return this.taskdefid;
   }

   public void setTaskType(String tasktype) {
	   this.tasktype = tasktype;
   }

   public String getTaskType() {
	   return this.tasktype;
   }

}
