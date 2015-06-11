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
 * Constants for the Web Console sample application.
 *
 */

public final class Constants {


    /**
     * The package name for this application.
     */
    public static final String PACKAGE = "com.compuware.optimal.flow.webconsole";


    /**
     * The session scope attribute under which the <strong>User</strong> object
     * for the currently logged-in user is stored.
     */
    public static final String USER_KEY = "user";

    /**
     * The session scope attribute under which the Connection configuration string object
     * for the current user is stored.
     */
    public static final String CONNECTIONCONFIG_KEY = "connectionconfig";

    /**
     * The session scope attribute under which the <strong>WorkflowConnector</strong> object
     * for the current user is stored.
     */
    public static final String CONNECTOR_KEY = "connector";

    /**
     * The request scope attribute under which the <strong>ActorTaskList</strong> object
     * is stored.
     */
    public static final String USERDRIVEN_KEY = "userdrivenitemsbean";

    /**
     * The request scope attribute under which the <strong>WorkFlowItemsBean</strong> object
     * is stored.
     */
    public static final String WORKFLOWDRIVEN_KEY = "workflowitemsbean";

    /**
     * The request scope attribute under which the <strong>TaskList</strong> object for
     * processed tasks is stored.
     */
    public static final String HISTORY_KEY = "historytasklist";

    /**
     * The session scope attribute under which the <strong>Position</strong> is stored.
     */
    public static final String POSITION_KEY = "position";

    /**
     * The request/session scope attribute under which the <strong>Task</strong> object
     * is stored.
     */
    public static final String TASK_KEY = "task";

    /**
     * The session scope attribute under which the task name
     * is stored.
     */
    public static final String TASKNAME_KEY = "taskname";

    /**
     * The request scope attribute under which the <strong>ParameterData</strong> object
     * is stored.
     */
    public static final String PARAMETERITEMS_KEY = "parameteritems";

    /**
     * The request scope attribute under which the current action is stored.
     */
    public static final String ACTION_KEY = "action";

    /**
     * The request scope attribute under which the current <strong>PackageProcessDefItemsBean</strong>
     * is stored.
     */
    public static final String PACKAGEPROCESSDEFITEMS_KEY = "ppbeans";

    /**
     * The request scope attribute under which the current <strong>PackageProcessInsItemsBean</strong>
     * is stored.
     */
    public static final String PACKAGEPROCESSINSITEMS_KEY = "ppbeans";

    /**
     * The request scope attribute under which the current List of Process Instances
     * is stored.
     */
    public static final String PROCESSLIST_KEY = "processlist";

    /**
     * The request scope attribute under which the processor of a task
     * is stored.
     */
    public static final String PROCESSOR_KEY = "processor";

    /**
     * The session scope attribute under which the current Activity Instance
     * is stored.
     */
    public static final String ACTIVITY_KEY = "activity";

    /**
     * The session scope attribute under which the current Activity Definition
     * is stored.
     */
    public static final String ACTIVITYDEF_KEY = "activitydef";

	/**
     * The session scope attribute under which the current ProcessDef definition
     * is stored.
     */
    public static final String PROCESSDEF_KEY = "processdef";

    /**
     * The request scope attribute under which the current list of activity instances
     * is stored.
     */
    public static final String ACTIVITYLIST_KEY = "activitylist";

    /**
     * The request scope attribute under which the current Organization Tree
     * is stored.
     */
    public static final String UNITLIST_KEY = "unitlist";

    /**
     * The request scope attribute under which the unit details are stored.
     */
    public static final String UNITDETAILS_KEY = "unitdetails";

    /**
     * The request scope attribute under which the actor details are stored.
     */
    public static final String ACTORDETAILS_KEY = "actordetails";

    /**
     * The request scope attribute under which the recurrenceComponent details are stored.
     */
    public static final String RECURRENCE_COMPONENT_KEY = "recurrencecomponent";

    /**
     * The request scope attribute under which the role details are stored.
     */
    public static final String ROLEDETAILS_KEY = "roledetails";

    /**
     * The session scope attribute under which the svgDiagram is stored.
     */
    public static final String SVGDIAGRAM_KEY = "svgdiagram";

    /**
     * The session scope attribute under which the BreadCrumb object is stored.
     */
    public static final String BREADCRUMB_KEY = "breadcrumb";
}
