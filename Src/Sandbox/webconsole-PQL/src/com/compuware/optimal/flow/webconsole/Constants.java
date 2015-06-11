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

import java.io.*;
import java.util.*;

/**
 * Constants for the Web Console sample application.
 *
 */

public final class Constants {

    public static final String CST_KEY = "constants";

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
     * The request scope attribute under which the <strong>Task</strong> object
     * is stored.
     */
    public static final String TASK_KEY = "task";

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
     * The request scope attribute under which the role details are stored.
     */
    public static final String ROLEDETAILS_KEY = "roledetails";
    
    private String manualProcNameKey = "Analyse Manuelle";

    private String pqlPackageNameKey = "Package PQL 1.0";
    
    private String userName = null;
    private String userPwd = null;
    private String connectionCfg = null;
    
    public String getManualProcName() {
        return this.manualProcNameKey;
    }
    
    public String getPqlPackageName() {
        return this.pqlPackageNameKey;
    }
    
    public String getConnectionCfg() {
        return this.connectionCfg;
    }
    
    public String getUserName() {
        return this.userName;
    }
        
    public String getUserPassword() {
        return this.userPwd;
    }
        
    public Constants() {
        init();
    }
    
    private void init() {
    	System.out.println("Init");
        // Lecture du fichier de configuration.
        InputStream is = getClass().getResourceAsStream("/webconsole.properties");
        Properties wcProps = new Properties();
        try {
            // Récupération des paramètres de connexion.
            wcProps.load(is);
            this.connectionCfg = wcProps.getProperty("connectionCfg");            
            this.manualProcNameKey = wcProps.getProperty("manualProcName");            
            this.pqlPackageNameKey = wcProps.getProperty("pqlPackageName");            
            this.userName = wcProps.getProperty("user");  
            this.userPwd = wcProps.getProperty("password");
        }
        catch (Exception e) {
            // Errueur de lecture de fichier.
            System.err.println("Can't read the properties file. " +
            "Make sure configPortailQualite.conf is in the CLASSPATH");
            return;
        }
        finally {
            try {
                // Fermeture du fichier.
                is.close();
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        
    }
}
