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

import com.compuware.optimal.flow.Package;
import com.compuware.optimal.flow.Process;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
 * This <strong>Action</strong> class builds a tree list of Processes
 * (Package->ProcessDef->Process) and displays the activities of a Process when
 * it is selected.
 * <br>
 * On input, the <code>action</code> parameter is read. If no action parameter
 * is passed, this is the initial call to this Action and the tree of Packages,
 * ProcessDef's and Processes is built. If the parameter exists and it contains
 * the value <code>ShowActivities</code>, the activities of the selected
 * process are retrieved.
 * <br>
 * The information built by this class is displayed using a frame set that
 * shows the main Menu at the top, the tree-like Package, ProcessDef, and
 * Process at the bottom left, and the details of the selected Process at the
 * bottom right. The HTML document packageProcessInstances.html is the main
 * document and is started after choosing the option Process activities from
 * the main menu (mainMenu.jsp). The tree is  displayed by the JSP
 * showProcessSelectList.jsp and the list of activities is displayed by the JSP
 * showActivityInstanceList.jsp.
 *
 * @see PackageProcessInsItemBean
 * @see PackageProcessInsItemsBean
 */

public final class ActivityInstanceListAction extends Action {


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
	User user            = (User) session.getAttribute(Constants.USER_KEY);
	WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);
	String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);

    // There is an initial action that will retrieve all sub-packages, top-process definitions and
    // process instances of all top-packages.
    // There is an additional action when the user selected a process instance, then
    // activity instance data needs to be displayed.

    // Check the incoming action parameter, if null this is the initial call the
    // Package/Top-ProcessDef/Process tree has to be build.
    // If action is ShowActivities the Activity Instances need to be displayed.
    String action = null;
    action = request.getParameter("action");

    if (servlet.getDebug() >= 1 )
        servlet.log("ActivityInstanceListAction action = " + action);

    if (action == null) {
   	    // Save the information in PackageProcessInsItemsBean.
        PackageProcessInsItemsBean ppbeans = new PackageProcessInsItemsBean();
        try {
            createPackage_ProcessTree(ppbeans, w);
		}
		catch (WorkflowException e) {
    	if (servlet.getDebug() >= 1)
  			servlet.log("ActivityInstanceListAction, WorkFlowException on retrieve of processes: "
			            + e.getMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,
        	       new ActionError("error.fatal.retrieveprocesses") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        	   	   new ActionError("error.consult") );
        saveErrors(request, errors);
        // show the error on the main menu
    	return (mapping.findForward("failure"));
	    }
    	// Save the reference to the packageprocessinsitemsbean in request scope.
		request.setAttribute(Constants.PACKAGEPROCESSINSITEMS_KEY, ppbeans);

		// Forward control to the specified success URI
		return (mapping.findForward("showprocesslist"));
	}

    if (action.equals("ShowActivities")) {
		ActivityList activitylist = null;
        try {
			String id = request.getParameter("processID");
            activitylist = getActivityInstances(id, w);
		}
		catch (WorkflowException e) {
    	if (servlet.getDebug() >= 1)
  			servlet.log("ActivityInstanceListAction, WorkFlowException on get Activity Instances: "
			            + e.getMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,
        	       new ActionError("error.fatal.retrieveactivities") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        	   	   new ActionError("error.consult") );
        saveErrors(request, errors);
        // show the error on the main menu
    	return (mapping.findForward("failure"));
	    }
	    // Save the Process Instance List object
		request.setAttribute(Constants.ACTIVITYLIST_KEY, activitylist);

		// Forward control to the specified success URI
		return (mapping.findForward("showactivityinstances"));

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
     * This method builds the top-level list of Packages. For each package the method
     * getPackage_ProcessInstances method is called. The complete list containing every
     * top-Package in a <strong>PackageProcessInsItemBean</strong> is returned in a
     * ArrayList that is kept by the returned <strong>PackageProcessInsItemsBean</strong>.
     */

    private PackageProcessInsItemsBean createPackage_ProcessTree(PackageProcessInsItemsBean ppbeans,
                                                                 WorkflowConnector w)
    throws WorkflowException {

    	PackageList   packagelist     = null;
     	PackageFilter packagefilter   = null;
    	Package       packagex        = null;
    	List          packages        = null;
    	Iterator      packageiterator = null;

        // Create a filter to only return top-level packages.
	    packagefilter = new PackageFilter(Package.ISTOPPACKAGE, Operator.BOOLEAN_EQUALS,
	                                      new Boolean(true));
	    packagelist = new PackageList(w);
	    packagelist.setFilter(packagefilter);
	    packages    = packagelist.getTop();
	    packageiterator = packages.iterator();

       	// For each Package/Top-ProcessDef/Process an item is created PackageProcessInsItemBean.
       	// All the beans are stored as a ArrayList in the PackageProcessInsItemsBean object.
       	// The PackageProcessInsItemsBean has a ArrayList for each level.

	    // The packagelist is retrieved now (all top-level packages).
        while (packageiterator.hasNext()) {
   	        PackageProcessInsItemBean ppbean = new PackageProcessInsItemBean();
   	    	packagex = (Package)packageiterator.next();
   	    	// We are only interested in Process Packages here.
   	    	if (packagex.getIsVersioned() == true) {
                ppbean.setName(packagex.getName());
                ppbean.setId(packagex.getID());
                ppbean.setEntityName(packagex.getEntityName());
   	    		// Recursive check for subpackages/top-ProcessDef and process/sub process instances.
   	    		PackageProcessInsItemsBean ppbs = getPackage_ProcessInstances(ppbean, w);
   	    		if (!(ppbs == null))
                    ppbean.setPpbeans(ppbs);

         		Boolean b = ppbeans.add(ppbean);
         		// check if add was ok...
	    	}
	    }
	    return(ppbeans);

	}

    /**
     * When a Process is selected in the tree this method retrieves the activity
     * information.
     */

    private ActivityList getActivityInstances(String id, WorkflowConnector w)
    throws WorkflowException {

		List     ainstances         = null;

        ActivityFilter activityfilter = null;
        ActivityList   activitylist   = null;

        activityfilter = new ActivityFilter(Activity.PROCESS_ID, Operator.STRING_EQUALS, id);
        activitylist   = new ActivityList(w);
        activitylist.setFilter(activityfilter);

        return(activitylist);

	}

    /**
     * This method retrieves the child information for each PackageProcessInsItemBean that
     * is passed as a parameter. This method is recursively called until no more child
     * information is available.
     */

    private PackageProcessInsItemsBean getPackage_ProcessInstances(PackageProcessInsItemBean parentppbean,
                                                                   WorkflowConnector w)
    throws WorkflowException {

        // return value
		PackageProcessInsItemsBean ppbeans = null;

        ProcessList   processlist;
        ProcessFilter processfilter;
        EntityDefinition entitydef;
        List pps;
        Iterator ppsiterator;
        PackageProcessInsItemBean ppbean = null;
        int parenttype;

        ppbeans = new PackageProcessInsItemsBean();

		// Check if we are dealing with a Package, ProcessDef or Process
		parenttype = 9;
		if (parentppbean.getEntityName().equals("Package"))
		    parenttype = 0;
		if (parentppbean.getEntityName().equals("ProcessDef"))
		    parenttype = 1;
		if (parentppbean.getEntityName().equals("Process"))
		    parenttype = 2;


		// Lets see if ppbean has children.
		switch (parenttype)
		{
			case 0:
			    // EntityName == Package

			    // Check for Sub-Package or Top-Process Definition.
			    PackageContents packagecontents = new PackageContents(w);
			    packagecontents.setIdentifier(parentppbean.getId());
			    pps = packagecontents.getTop();
			    ppsiterator = pps.iterator();
                while (ppsiterator.hasNext()) {
					entitydef = (EntityDefinition)ppsiterator.next();
					// make a bean
					ppbean = new PackageProcessInsItemBean();
					ppbean.setId(entitydef.getID());
					ppbean.setName(entitydef.getName());
					ppbean.setEntityName(entitydef.getEntityName());

                    // Check again for children
         			PackageProcessInsItemsBean ppbs = getPackage_ProcessInstances(ppbean, w);
        			if (!(ppbs == null))
                        ppbean.setPpbeans(ppbs);
                    ppbeans.add(ppbean);
				}

 			    break;

			case 1:
			    // EntityName == ProcessDef

			    // Check for Process Instances.
			    processlist   = new ProcessList(w);
                processfilter = new ProcessFilter(Process.PROCESSDEF_ID,
                                                                Operator.STRING_EQUALS,
                                                                parentppbean.getId());
                processlist.setFilter(processfilter);
                pps = processlist.getTop();
                ppsiterator = pps.iterator();
                while (ppsiterator.hasNext()) {
					Process process = (Process)ppsiterator.next();
					ppbean = new PackageProcessInsItemBean();
                    // Formatter for the Date.
                    SimpleDateFormat formatter = new SimpleDateFormat ("MMM dd, yyyy HH:mm:ss");
 					ppbean.setCreationTime(formatter.format(process.getCreationTime()));
					ppbean.setId(process.getID());
					ppbean.setName(process.getName());
					ppbean.setEntityName(process.getEntityName());
					ppbean.setVar1(process.getVar1());
                    // Check again for children
        			PackageProcessInsItemsBean ppbs = getPackage_ProcessInstances(ppbean, w);
           			if (!(ppbs == null))
                        ppbean.setPpbeans(ppbs);
                    ppbeans.add(ppbean);
				}

			    break;

			case 2:
			    // EntityName == Process

			    // we need to retrieve process instances belonging to this process.
			    processlist   = new ProcessList(w);
                processfilter = new ProcessFilter(Process.PROCESS_ID,
                                                                Operator.STRING_EQUALS,
                                                                parentppbean.getId());
                processlist.setFilter(processfilter);
                pps = processlist.getTop();
                ppsiterator = pps.iterator();
                while (ppsiterator.hasNext()) {
					Process process = (Process)ppsiterator.next();
					ppbean = new PackageProcessInsItemBean();
                    // Formatter for the Date.
                    SimpleDateFormat formatter = new SimpleDateFormat ("MMM dd, yyyy HH:mm:ss");
 					ppbean.setCreationTime(formatter.format(process.getCreationTime()));
					ppbean.setId(process.getID());
					ppbean.setName(process.getName());
					ppbean.setEntityName(process.getEntityName());
					ppbean.setVar1(process.getVar1());
                    // Check again for children
        			PackageProcessInsItemsBean ppbs = getPackage_ProcessInstances(ppbean, w);
           			if (!(ppbs == null))
                        ppbean.setPpbeans(ppbs);
                    ppbeans.add(ppbean);
				}

			    break;

			default:
			    ppbeans = null;
			    break;

		}

        return(ppbeans);

    }

}
