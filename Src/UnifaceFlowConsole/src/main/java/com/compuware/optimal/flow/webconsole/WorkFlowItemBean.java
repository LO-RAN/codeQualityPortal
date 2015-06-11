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
 * This bean holds the data of the Workflow Connector <strong>Task</strong>
 * object. A choice has been made not to directly access the list object
 * <strong>TaskList</strong> of the Workflow Connector. This to show that in
 * this way you could also add other information to these beans. For example,
 * you could obtain a specific parameter of a task and add this to the
 * <strong>WorkFlowItemBean</strong>. Or you could translate the State values and store
 * the translated values in the bean. If you do not need extra information or
 * do not want to translate values, you can also save the reference to the TaskList
 * in request scope. The JSP can then use the saved TaskList directly.
 *
 * @see UserDrivenTaskListAction
 * @see WorkFlowItemsBean
 */

public class WorkFlowItemBean {

   /**
    * The id of the task.
    */
   private String id;

   /**
    * The name of the task.
    */
   private String name;

   /**
    * The state of the task.
    */
   private String state;

   /**
    * The type of the task.
    */
   private String type;

   /**
    * The priority of the task.
    */
   private String priority;

   /**
    * The var1 property of the task. This property is used for unique identification
    * of the task. Var1 holds the value of the process variable identified by var1label.
    */
   private String var1;

   /**
    * The var1label property of the task. Var1Label has the name of a process variable that
    * was choosen to be used to identify the task.
    */
   private String var1label;

   /**
    * The var2 property of the task. This property is used for unique identification
    * of the task. Var2 holds the value of the process variable identified by var2label.
    */
   private String var2;

   /**
    * The var2label property of the task. Var2Label has the name of a process variable that
    * was choosen to be used to identify the task.
    */
   private String var2label;

   /**
    * The var3 property of the task. This property is used for unique identification
    * of the task. Var3 holds the value of the process variable identified by var3label.
    */
   private String var3;

   /**
    * The var3label property of the task. Var3Label has the name of a process variable that
    * was choosen to be used to identify the task.
    */
   private String var3label;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */
   public WorkFlowItemBean() {
      this.id = "";
      this.name = "";
      this.state = "";
      this.type = "";
      this.priority = "";
      this.var1 = "";
      this.var1label = "";
      this.var2 = "";
      this.var2label = "";
      this.var3 = "";
      this.var3label = "";
   }

   /**
    * Set the id of the task.
    */
   public void setId(String id) {
	   this.id = id;
   }

   /**
    * Get the id of the task.
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

   public void setState(String state) {
	   this.state = state;
   }

   public String getState() {
	   return this.state;
   }

   public void setType(String type) {
	   this.type = type;
   }

   public String getType() {
	   return this.type;
   }

   public void setPriority(String priority) {
	   this.priority = priority;
   }

   public String getPriority() {
	   return this.priority;
   }

   public void setVar1(String var1) {
	   this.var1 = var1;
   }

   public String getVar1() {
	   return this.var1;
   }

   public void setVar1Label(String var1label) {
	   this.var1label = var1label;
   }

   public String getVar1Label() {
	   return this.var1label;
   }

   public void setVar2(String var2) {
	   this.var2 = var2;
   }

   public String getVar2() {
	   return this.var2;
   }

   public void setVar2Label(String var2label) {
	   this.var2label = var2label;
   }

   public String getVar2Label() {
	   return this.var2label;
   }

   public void setVar3(String var3) {
	   this.var3 = var3;
   }

   public String getVar3() {
	   return this.var3;
   }

   public void setVar3Label(String var3label) {
	   this.var3label = var3label;
   }

   public String getVar3Label() {
	   return this.var3label;
   }

}
