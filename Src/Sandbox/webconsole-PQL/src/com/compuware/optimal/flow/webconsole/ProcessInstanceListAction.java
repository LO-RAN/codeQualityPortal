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

// Pay attention to the following import statements.
import com.compuware.optimal.flow.Package;
import com.compuware.optimal.flow.Process;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import com.compuware.optimal.flow.*;

/**
 * This <strong>Action</strong> class builds a list of package and process
 * definitions and displays process instance information about the selected
 * process definition.
 * <br>
 * On input, the <code>action</code> parameter is read. If no action parameter
 * is  passed, this is the initial call to this Action and the tree of packages
 * and process definitions is retrieved. Each package or process definition is
 * stored in a <strong>PackageProcessDefItemBean</strong>. A package can
 * contain other packages or process definitions, and this child information
 * is also stored in the PackageProcessDefItemBean as a
 * <strong>PackageProcessDefItemsBean</strong>. The PackageProcessDefItemsBean
 * holds a Collection of PackageProcessDefItemBeans.
 * The whole tree of package and process definition information is built in
 * this recursive way. When complete, the top-level PackageProcessDefItemsBean
 * object holding all other information is stored in request scope. The JSP
 * showDefinitionList.jsp can display the information.
 * <br>
 * If the action input parameter contains the value ShowProcesses, the Process
 * instances of the selected Process definition are retrieved. The ProcessList
 * object is saved in request scope and the information is displayed by the JSP
 * showProcessInstanceList.jsp.
 * <br>
 * The information built by this class is displayed using a frame set that
 * shows the main Menu at the top, the list of packages and process definitions
 * at the bottom left, and the process instances at the bottom right. The
 * packageProcessDef.html document is the main document and is displayed by
 * choosing the option List Process Instance information for a selected Process
 * Definition from the main menu.
 *
 * @see PackageProcessDefItemBean
 * @see PackageProcessDefItemsBean
 */

public final class ProcessInstanceListAction extends Action {
    
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another Web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping ActionMapping used to select this instance.
     * @param actionForm Optional ActionForm bean for this request (if any).
     * @param request HTTP request being processed.
     * @param response HTTP response being created.
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        // Extract attributes needed
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        
        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();
        
        HttpSession session  = request.getSession();
        Constants cst        = (Constants)session.getAttribute(Constants.CST_KEY);
        User user            = (User) session.getAttribute(Constants.USER_KEY);
        // WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);
        WorkflowConnector w = null;
        try {
            w = new WorkflowConnector(cst.getConnectionCfg(), user.getUsername());
        }
        catch (WorkflowException e) {
            if (servlet.getDebug() >= 1)
                servlet.log("ProcessInstanceListAction, WorkFlowException on retrieve of definitions: "
                + e.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.fatal.retrievepackages") );
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
            return (mapping.findForward("failure"));
        }
        
        String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);
        
        // There is an initial action that will retrieve all packages and process definitions.
        // There is an additional action when the user selected a process definition, then
        // process instance data needs to be displayed.
        
        // Check the incoming action parameter, if null this is the initial call the
        // Definition tree has to be build.
        // If action is ShowProcesses the Process Instances need to be displayed.
        String action = null;
        action = request.getParameter("action");
        
        if (action == null) {
            // Save the information in PackageProcessDefItemsBean.
            PackageProcessDefItemsBean ppbeans = new PackageProcessDefItemsBean();
            try {
                createDefinitionTree(ppbeans, w, cst);
                w.close();
            }
            catch (WorkflowException e) {
                if (servlet.getDebug() >= 1)
                    servlet.log("ProcessInstanceListAction, WorkFlowException on retrieve of definitions: "
                    + e.getMessage());
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.fatal.retrievepackages") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.consult") );
                saveErrors(request, errors);
                // show the error on the main menu
                return (mapping.findForward("failure"));
            }
            // Save the reference to the packageprocessdefitemsbean in request scope.
            request.setAttribute(Constants.PACKAGEPROCESSDEFITEMS_KEY, ppbeans);
            
            // Forward control to the specified success URI
            return (mapping.findForward("showdefinitions"));
        }
        
        if (action.equals("ShowProcesses")) {
            ProcessList processlist = null;
            ProcessList manualProcesslist = null;
            LinkedList allProcessList = null;
            String projectId = null;
            UserDrivenItemBean manualTask = null;
            try {
                String id = request.getParameter("processDefID");
                processlist = getProcessInstances(id, w);
                projectId = getProjectId(processlist);
                manualProcesslist = getManualProcessInstances(projectId, w, cst);
                SortedSet p1 = processlist.getAll();
                SortedSet p2 = manualProcesslist.getAll();
                select(p2, projectId);
                allProcessList = concatAndOrder(p1, p2);
                manualTask = getManualTaskID(w);
                w.close();
            }
            catch (WorkflowException e) {
                if (servlet.getDebug() >= 1)
                    servlet.log("ProcessInstanceListAction, WorkFlowException on get Process Instances: "
                    + e.getMessage());
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.fatal.retrievepackages") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.consult") );
                saveErrors(request, errors);
                // show the error on the main menu
                return (mapping.findForward("failure"));
            }
            // Save the Process Instance List object
            request.setAttribute(Constants.PROCESSLIST_KEY, allProcessList);
            request.setAttribute("projectId", projectId);
            request.setAttribute("manualTask", manualTask);
            
            // Forward control to the specified success URI
            return (mapping.findForward("showprocessinstances"));
            
        }
        
        // program flow should never reach this point...
        errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.fatal.programflow") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.consult") );
        saveErrors(request, errors);
        return (mapping.findForward("failure"));
        
    }
    
    
    // --------------------------------------------------------- Private Methods
    
    /**
     * The tree-like structure of packages and process definitions
     * is build. The main part of this method build the first level of
     * information itself being the top-package list. The child information
     * is build by calling the method getDefintions.
     */
    
    private PackageProcessDefItemsBean createDefinitionTree(PackageProcessDefItemsBean ppbeans,
    WorkflowConnector w, Constants cst)
    throws WorkflowException {
        
        PackageList   packagelist     = null;
        PackageFilter packagefilter   = null;
        Package       packagex        = null;
        List          packages        = null;
        Iterator      packageiterator = null;
        
        packagefilter = new PackageFilter(Package.ISTOPPACKAGE, Operator.BOOLEAN_EQUALS, new Boolean(true));
        packagefilter.add(Package.NAME, Operator.STRING_EQUALS, cst.getPqlPackageName());
        packagelist = new PackageList(w);
        packagelist.setFilter(packagefilter);
        packages    = packagelist.getTop();
        packageiterator = packages.iterator();
        
        // For each Package or ProcessDef an item is created PackageProcessDefItem.
        // All the beans are stored as a ArrayList in the PackageProcessDefItemsBean object.
        // The PackageProcessDefItemsBean has a ArrayList for each level.
        Hashtable recurrenceList = getRecurrenceList(w);
        
        // The packagelist is retrieved now.
        while (packageiterator.hasNext()) {
            PackageProcessDefItemBean ppbean = new PackageProcessDefItemBean();
            packagex = (Package)packageiterator.next();
            if (packagex.getIsVersioned() == true) {
                ppbean.setName(packagex.getName());
                ppbean.setId(packagex.getID());
                ppbean.setEntityName(packagex.getEntityName());
                // Recursive check for subpackages and process/sub process definitions
                PackageProcessDefItemsBean ppbs = getDefinitions(ppbean, recurrenceList, w, cst);
                if (!(ppbs == null))
                    ppbean.setPpbeans(ppbs);
                
                Boolean b = ppbeans.add(ppbean);
                // check if add was ok...
            }
        }
        return(ppbeans);
        
    }
    
    public Hashtable getRecurrenceList(WorkflowConnector w) throws WorkflowException {
        Hashtable result = new Hashtable();
        RecurrenceList recurrenceList = new RecurrenceList(w);
        // Create a new filter for processes
        RecurrenceFilter filter = new RecurrenceFilter("");
        
        // Parse the previous string back into a Date.
        
        Date date = new Date();
        Date date2 = new Date();
        date2.setMonth(date.getMonth()+1);
        
        filter.add(Recurrence.SCHEDULED_TIME, Operator.DATE_GE, date);
        filter.add(Recurrence.SCHEDULED_TIME, Operator.DATE_LE, date2);
        recurrenceList.setFilter(filter);
        
        // Retrieve the matching list of processes.
        java.util.SortedSet recurrences = recurrenceList.getAll();
        
        // Create a new filter for tasks by adding
        // constraint properties
        Iterator i = recurrences.iterator();
        while (i.hasNext()) {
            com.compuware.optimal.flow.Recurrence recurrence = (com.compuware.optimal.flow.Recurrence)i.next();
            String recurrenceID = recurrence.getID();
            result.put(recurrence.getDefinitionID(), recurrence);
        }
        return result;
    }
    
    /**
     * The information of Process instances is retrieved for a selected
     * process definition. The process definition is identified by its id.
     */
    
    private ProcessList getProcessInstances(String id, WorkflowConnector w)
    throws WorkflowException {
        
        List     pinstances         = null;
        
        ProcessFilter processfilter = null;
        ProcessList   processlist   = null;
        
        processfilter = new ProcessFilter(Process.PROCESSDEF_ID, Operator.STRING_EQUALS, id);
        processlist   = new ProcessList(w);
        processlist.setFilter(processfilter);
        
        return(processlist);
        
    }
    
    /**
     * The information of Process instances is retrieved for a selected
     * process definition. The process definition is identified by its id.
     */
    
    private ProcessList getManualProcessInstances(String projectId, WorkflowConnector w, Constants cst)
    throws WorkflowException {
        
        List     pinstances         = null;
        
        ProcessFilter processfilter = null;
        ProcessList   processlist   = null;
        
        processfilter = new ProcessFilter(Process.NAME, Operator.STRING_EQUALS, cst.getManualProcName());
        processlist   = new ProcessList(w);
        processlist.setFilter(processfilter);
        
        return(processlist);
        
    }
    
    private String getProjectId(ProcessList procList) throws WorkflowException {
        String result = null;
        List list = procList.getTop();
        Iterator i = list.iterator();
        while (i.hasNext() && result == null) {
            Process proc = (Process)i.next();
            ParameterData params = proc.getActualVariables();
            result = (String)params.get("vp_projectid");
        }
        return result;
    }
    
    /**
     * This method builds the child information for each top-level package.
     */
    
    private PackageProcessDefItemsBean getDefinitions(PackageProcessDefItemBean parentppbean, Hashtable recurrenceList,
    WorkflowConnector w, Constants cst)
    throws WorkflowException {
        
        // return value
        PackageProcessDefItemsBean ppbeans = null;
        
        EntityDefinition entitydef = null;
        List pps;
        Iterator ppsiterator;
        PackageProcessDefItemBean ppbean = null;
        
        
        ppbeans = new PackageProcessDefItemsBean();
        // Lets see if ppbean has children.
        switch (parentppbean.getEntityName().compareTo("Package")) {
            case 0:
                // Check for Sub-Package or Top-Process Definition.
                PackageContents packagecontents = new PackageContents(w);
                packagecontents.setIdentifier(parentppbean.getId());
                pps = packagecontents.getTop();
                ppsiterator = pps.iterator();
                while (ppsiterator.hasNext()) {
                    entitydef = (EntityDefinition)ppsiterator.next();
                    // make a bean
                    ppbean = new PackageProcessDefItemBean();
                    ppbean.setId(entitydef.getID());
                    ppbean.setName(entitydef.getName());
                    ppbean.setEntityName(entitydef.getEntityName());
                    
                    String recurrenceId = getRecurrenceId(entitydef.getID(), w);
                    
                    if (recurrenceId != null) {
                        Recurrence recurrenceItem = (Recurrence)recurrenceList.get(recurrenceId);
                        if (recurrenceItem != null)
                            ppbean.setScheduledTime(recurrenceItem.getScheduledTime());
                    }
                    
                    // Check again for children
                    PackageProcessDefItemsBean ppbs = getDefinitions(ppbean, recurrenceList, w, cst);
                    if (!(ppbs == null))
                        ppbean.setPpbeans(ppbs);
                    
                    if (!(ppbean.getName().equals(cst.getManualProcName())))
                        ppbeans.add(ppbean);
                }
                
                break;
                
            default:
                
                // Dealing with a Process.
                // Check for Sub-Process Definitions.
                ProcessDefContents processdefcontents = new ProcessDefContents(w);
                processdefcontents.setIdentifier(parentppbean.getId());
                pps = processdefcontents.getTop();
                ppsiterator = pps.iterator();
                while (ppsiterator.hasNext()) {
                    entitydef = (EntityDefinition)ppsiterator.next();
                    // check if we are dealing with a ProcessDef.
                    // Other objects like TaskDef's can be skipped.
                    if (entitydef.getEntityName().equals("ProcessDef")) {
                        // make a bean
                        ppbean = new PackageProcessDefItemBean();
                        ppbean.setId(entitydef.getID());
                        ppbean.setName(entitydef.getName());
                        ppbean.setEntityName(entitydef.getEntityName());
                        
                        // Check again for children
                        PackageProcessDefItemsBean ppbs = getDefinitions(ppbean, recurrenceList, w, cst);
                        if (!(ppbs == null))
                            ppbean.setPpbeans(ppbs);
                        ppbeans.add(ppbean);
                    }
                }
                
                break;
                
        }
        
        return(ppbeans);
        
    }
    
    private String getRecurrenceId(String entityDefId, WorkflowConnector w) throws WorkflowException {
        String result = null;
        ProcessDefContents processdefcontents = new ProcessDefContents(w);
        processdefcontents.setIdentifier(entityDefId);
        List pps = processdefcontents.getTop();
        Iterator ppsiterator = pps.iterator();
        while (ppsiterator.hasNext() && result == null) {
            Object next = ppsiterator.next();
            if (next instanceof TaskDef) {
                TaskDef task = (TaskDef)next;
                if (task.getName().equals("Recurrence")) {
                    result = task.getID();
                }
            }
        }
        return result;
    }
    
    private UserDrivenItemBean getManualTaskID(WorkflowConnector w) throws WorkflowException {
        UserDrivenItemBean userdrivenitembean = null;

        // Define the Workflow API lists that will be saved in request scope
        AvailableTaskList userdriventasklist = null;
        List udtasks = null;
        Iterator udtasksiterator;
        AvailableTask udtask      = null;
        TaskDefList   taskdeflist = null;
        TaskDef       taskdef     = null;
        
        userdriventasklist              = new AvailableTaskList(w);
        ActorList actorlist             = new ActorList(w);
        Actor actor                     = actorlist.getActorFromName("ADMINISTRATOR");
        AvailableTaskFilter availfilter = new AvailableTaskFilter(AvailableTask.ACTOR_ID, Operator.STRING_EQUALS, actor.getID());
        //availfilter.add(AvailableTask.NAME, Operator.STRING_EQUALS, Constants.MANUALPROCNAME_KEY);
        userdriventasklist.setFilter(availfilter);
        udtasks = userdriventasklist.getTop();

        // Iterate through the available tasks and for each availabl task get its TaskType from the
        // definition.
        taskdeflist         = new TaskDefList(w);
        udtasksiterator     = udtasks.iterator();
        if (udtasksiterator.hasNext()) {
            udtask  = (AvailableTask)udtasksiterator.next();
            taskdef = (TaskDef)taskdeflist.getItemFromID(udtask.getDefinitionID());
            userdrivenitembean = new UserDrivenItemBean();
            userdrivenitembean.setId(udtask.getID());
            userdrivenitembean.setName(udtask.getName());
            userdrivenitembean.setReleaseName(udtask.getReleaseName());
            userdrivenitembean.setReleaseState(udtask.getReleaseState());
            userdrivenitembean.setReleaseVersion(udtask.getReleaseVersion());
            userdrivenitembean.setTaskId(udtask.getTaskID());
            userdrivenitembean.setTaskDefId(taskdef.getID());
            userdrivenitembean.setTaskType(taskdef.getTaskType().toString());
        }
        return userdrivenitembean;
    }
    
    private void select(SortedSet proc, String projectId) throws WorkflowException {
        Iterator i = proc.iterator();
        while (i.hasNext()) {
            Process tmp = (Process)i.next();
            ParameterData params = tmp.getActualVariables();
            String actualProjectId = (String)params.get("vp_projectid");
            if (!actualProjectId.equals(projectId))
                proc.remove(tmp);
        }
    }
    
    private LinkedList concatAndOrder(SortedSet p1, SortedSet p2) {
        LinkedList list = new LinkedList(p1);
        list.addAll(p2);
        Collections.sort(list, new ProcessComparator());
        return list;
    }
    
}
