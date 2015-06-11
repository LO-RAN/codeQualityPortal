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
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.HashMap;

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
import com.compuware.carscode.dbms.ProjectDao;

/**
 * This <strong>Action</strong> class builds a tree list of Processes
 * (Package->ProcessDef->Process) and displays the details, breadcrumb trail and
 * SVG Diagram of a Process when it is selected.
 * <br>
 * On input, the <code>action</code> parameter is read. If no action parameter
 * is passed, this is the initial call to this Action and the tree of Packages,
 * ProcessDef's and Processes is built. If the parameter exists and it contains
 * the value <code>ShowActivities</code>, the Activity details and SVG Diagram of the selected
 * process are retrieved. Further more the breadcrumb trail is created. When the action parameter
 * contains <code>ShowBreadCrumbItem</code>
 * the SVG Diagram and Activity/ProcessDef of the selected BreadCrumbItem is retrieved.
 * Next BreadCrumbItems including the selected item are removed from the BreadCrumbBean.
 * When the action parameter contains <code>ShowSVGDiagramItem</code> a Process was selected
 * within the SVG Diagram the previous Activity/ProcessDef needs to be added to the BreadCrumbBean,
 * the SVG Diagram of the selected item needs to be retrieved next.
 * <br>
 * The information built by this class is displayed using a frame set that
 * shows the main Menu at the top, the tree-like Package, ProcessDef, and
 * Process at the bottom left, and the details of the selected Process at the
 * bottom right. The HTML document packageProcessInstances.html is the main
 * document and is started after choosing the option Process activities from
 * the main menu (mainMenu.jsp). The tree is  displayed by the JSP
 * showProcessSelectList.jsp and the Activity/ProcessDef details, breadcrumb trail and
 * SVG Diagram is displayed by the JSP showMainSVGDiagram.jsp. The SVG Diagram is an embedded
 * object that is displayed by the JSP contentMainSVGDiagram.jsp.
 *
 * @see PackageProcessInsItemBean
 * @see PackageProcessInsItemsBean
 * @see BreadCrumbBean
 * @see BreadCrumbItemBean
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
	String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);
	WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);

	// Sychronize on the WorkflowConnector, to prevent that deleteInstances() influences the
	// other actions.
	synchronized(w) {

    String         id             = null;
    String         svgDiagram     = null;
	Activity       activity       = null;
	ProcessDef     processdef     = null;
	Activity       activityprev   = null;
	ProcessDef     processdefprev = null;
	BreadCrumbBean breadcrumb     = null;

    // Check the incoming action parameter, if null this is the initial call the
    // Package/Top-ProcessDef/Process tree has to be build.
    // If action is ShowActivities, ShowSVGDiagramItem or ShowBreadCrumbItem the Activity/ProcessDef
    // and SVG Diagram is retrieved. The BreadCrumb is modified dependend on the action.
    String action = null;
    action = request.getParameter("action");

    if (servlet.getDebug() >= 1 ) {
        servlet.log("ActivityInstanceListAction action = " + action);
 	    servlet.log("getMethod: " + request.getMethod());
	    servlet.log("getPathInfo: " + request.getPathInfo());
	    servlet.log("getQueryString: " + request.getQueryString());
	    servlet.log("getRequestURI: " + request.getRequestURI());
	    servlet.log("getRequestURL: " + request.getRequestURL());
	}

    if (action == null) {
        // This is the initial action that will retrieve all sub-packages, top-process definitions and
        // process instances of all top-packages.
   	    // The information is saved in PackageProcessInsItemsBean.
        PackageProcessInsItemsBean ppbeans = new PackageProcessInsItemsBean();
        try {
            createPackage_ProcessTree(ppbeans, w);
		}
		catch (WorkflowException e) {
        	if (servlet.getDebug() >= 1) {
  	    		servlet.log("ActivityInstanceListAction(Init); WorkflowException on retrieve of processes: "
	    		            + e.getMessage());
			}
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

    if (session.getAttribute("projectMap") == null) {
        ProjectDao dao = new ProjectDao();
        HashMap projects = dao.retrieveAllProject();
        session.setAttribute("projectMap", projects);
    }

		// Forward control to the specified success URI
		return (mapping.findForward("showprocesslist"));
	}

    if (action.equals("ShowActivities") || action.equals("ShowSVGDiagramItem") ||
        action.equals("ShowBreadCrumbItem")) {

		id = request.getParameter("processID");
		if (servlet.getDebug() >= 1) {
			servlet.log("processID = " + id);
		}

        // ShowActivities
        // This is always a select Process Activity from the tree.
        // Needed actions:
        // A1. Read the Activity
        // A2. Read the SVG Diagram
        // A3. Create the BreadCrumb, in the tree the Top Process or a Sub Process can be selected.
        //    If we are dealing with a Top Process no BreadCrumb will be created, the process itself
        //    will be displayed in the breadcrumbtrail as text only.
        //    If we are dealing with a Sub Process a BreadCrumb trail needs to be created.


        // ShowSVGDiagramItem
        // This is a select Process Activity or ProcessDef within the SVG Diagram.
        // Needed actions:
        // B1. Save the previous Activity or ProcessDef.
        // B2. Read the new Activity/ProcessDef
        // B3. Read the SVG Diagram
        // B4. Modify the BreadCrumb, add the previous Activity/processDef to the
        //    breadcrumb trail.


        // ShowBreadCrumbItem
        // This is a select Process Activity or ProcessDef within the breadcrumb trail.
        // Needed actions:
        // C1. Read the new Activity/ProcessDef
        // C2. Read the SVG Diagram
        // C3. Modify the BreadCrumb, remove all BreadCrumbItems from the end including the
        //    current one.


        // B1. Save the previous Activity or ProcessDef.
        if (action.equals("ShowSVGDiagramItem")) {
            // B1. Save the previous Activity or ProcessDef
            activityprev = (Activity)session.getAttribute(Constants.ACTIVITY_KEY);
            processdefprev = (ProcessDef)session.getAttribute(Constants.PROCESSDEF_KEY);
	    }

        // A1, B2, C1. Read the Activity/ProcessDef
	    //  First try to get Activity details. When the Activity does not exist,
	    //  the Process Definition is read.
        try {
			activity = getActivity(id, w);
			if (activity == null) {
                processdef = getProcessDef(id, w);
			}
		}
		catch (WorkflowException e) {
            if (servlet.getDebug() >= 1) {
         		servlet.log("ActivityInstanceListAction(Show action); " +
         		            "WorkFlowException on get activity/processdef: " +
        		            e.getMessage());
     		}
			errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.fatal.retrieveactivity") );
            errors.add(ActionErrors.GLOBAL_ERROR,
               	   	   new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
            return (mapping.findForward("failure"));
    	}

        // A2, B3, C2. Read the SVG Diagram
        try {
			svgDiagram = getSVGDiagram(id, w);
		}
		catch (WorkflowException e) {
        	if (servlet.getDebug() >= 1)
  	    		servlet.log("ActivityInstanceListAction(Show action); " +
  	    		            "WorkFlowException on get SVGDiagram: " +
	    		            e.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,
            	       new ActionError("error.fatal.retrievesvgdiagram") );
            errors.add(ActionErrors.GLOBAL_ERROR,
            	   	   new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
        	return (mapping.findForward("failure"));
	    }

  	    // A3. Create the breadcrumb trail.
        if (action.equals("ShowActivities")) {
            try {
				// always create a breadcrumb object, even an empty one.
				// Later on BreadCrumbItems are added when a process is selected
				// in the SVG Diagram.
   			    breadcrumb = createBreadCrumbInit(activity.getID(), w);
    		}

    		catch (WorkflowException e) {
        	    if (servlet.getDebug() >= 1) {
    		    	servlet.log("ActivityInstanceListAction(ShowActivities); " +
    		    	            "WorkFlowException on get breadcrumb: " +
    		    	            e.getMessage(), e);
    			}
                errors.add(ActionErrors.GLOBAL_ERROR,
                	       new ActionError("error.fatal.createbreadcrumb") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                	   	   new ActionError("error.consult") );
                saveErrors(request, errors);
                // show the error on the main menu
                return (mapping.findForward("failure"));
    	    }
		}

        // B4. Modify the BreadCrumb, add the previous Activity/processDef to the
        //     breadcrumb trail.
        if (action.equals("ShowSVGDiagramItem")) {
            try {
    			breadcrumb = (BreadCrumbBean)session.getAttribute(Constants.BREADCRUMB_KEY);
    			// On a refresh the current activity/processdef can be the same as the previous
    			// activity/processdef, at that moment no addition should be made to the breadcrumb.
    			if (processdefprev == null) {
					if ( activity == null || !(activityprev.getID().equals(activity.getID())) ) {
    			        addBreadCrumbItem(breadcrumb, activityprev.getID(), w);
					}
    		    }
    			else {
					if ( !(processdefprev.getID().equals(processdef.getID())) ) {
        				addBreadCrumbItem(breadcrumb, processdefprev.getID(), w);
					}
    			}
    		}

    		catch (WorkflowException e) {
        	    if (servlet.getDebug() >= 1) {
    		    	servlet.log("ActivityInstanceListAction(ShowSVGDiagramItem); " +
    		    	            "WorkFlowException on addBreadCrumbItem: " +
    		    	            e.getMessage(), e);
    			}
                errors.add(ActionErrors.GLOBAL_ERROR,
                	       new ActionError("error.fatal.createbreadcrumb") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                	   	   new ActionError("error.consult") );
                saveErrors(request, errors);
               // show the error on the main menu
               return (mapping.findForward("failure"));
    	    }
		}

        // C3. Modify the BreadCrumb, remove all BreadCrumbItems from the end including the
        //     current one.
        if (action.equals("ShowBreadCrumbItem")) {
    		breadcrumb = (BreadCrumbBean)session.getAttribute(Constants.BREADCRUMB_KEY);
            try {
    		    if (processdef == null) {
    		        removeBreadCrumbItems(breadcrumb, activity.getID());
    	        }
    		    else {
    		    	removeBreadCrumbItems(breadcrumb, processdef.getID());
    		    }
    		}

    		catch (WorkflowException e) {
        	    if (servlet.getDebug() >= 1) {
    		    	servlet.log("ActivityInstanceListAction(ShowBreadCrumbItem); " +
    		    	            "WorkFlowException on removeBreadCrumbItems: " +
    		    	            e.getMessage(), e);
    			}
                errors.add(ActionErrors.GLOBAL_ERROR,
                	       new ActionError("error.fatal.createbreadcrumb") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                	   	   new ActionError("error.consult") );
                saveErrors(request, errors);
                // show the error on the main menu
                return (mapping.findForward("failure"));
    	    }
		}

		// The reference to the svgDiagram is saved.
		session.setAttribute(Constants.SVGDIAGRAM_KEY, svgDiagram);

		// The reference to the process activity or definition is saved.
		if (activity != null) {
    		session.setAttribute(Constants.ACTIVITY_KEY, activity);
		}
		else {
			session.removeAttribute(Constants.ACTIVITY_KEY);
		}
		if (processdef != null) {
    		session.setAttribute(Constants.PROCESSDEF_KEY, processdef);
		}
		else {
			session.removeAttribute(Constants.PROCESSDEF_KEY);
		}

        // The reference to the breadcrumb trail is saved
		session.setAttribute(Constants.BREADCRUMB_KEY, breadcrumb);

    	// Free up resources, no 4GL Objects are referred in JSP view.
    	try {
    		w.deleteInstances();
    	}

    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1) {
    	       servlet.log("ActivityInstanceListAction, deleteInstances: " + e.getMessage());
    	    }
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.fatal.deleteinstances") );
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
            return (mapping.findForward("failure"));
        }

		return (mapping.findForward("showmainsvgdiagram"));

    }
    } // End synchronization on WorkflowConnector.

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
     * When a Process is selected in the tree this method retrieves the svgDiagram.
     */

    private String getSVGDiagram(String id, WorkflowConnector w)
    throws WorkflowException {

        ProcessList   processlist   = null;
        Process       process       = null;
        String        svgDiagram    = null;
        List          processes     = null;
        Iterator      processiterator = null;

        processlist = new ProcessList(w);

        process = (Process)processlist.getItemFromID(id);

        if (process == null) {
			// We are dealing with a ProcessDef
        	// get hold of a process instance
    		processlist.setStepSize(1);
    		ProcessFilter p = new ProcessFilter();
    		processlist.setFilter(p);
    		processes = processlist.getTop();
    		processiterator = processes.iterator();
     		process = (Process)processiterator.next();
		}

        svgDiagram  = (String)process.getSVGDiagram(id);

        return(svgDiagram);

	}

    /**
     * When a Process is selected in the tree this method retrieves the related activity.
     * The reference to this activity can be used by the JSP to display the process
     * details.
     */

    private Activity getActivity(String id, WorkflowConnector w)
    throws WorkflowException {

        ActivityList activitylist = null;
        Activity     activity     = null;

        activitylist = new ActivityList(w);

        try {
            activity = (Activity)activitylist.getItemFromID(id);
		}

		catch (WorkflowException e) {
			return null;
		}

        return(Activity)activity;
	}

    /**
     * When a Process is selected in the tree this method retrieves the related process definition.
     * The reference to this process definition can be used by the JSP to display the process
     * definition details.
     */

    private ProcessDef getProcessDef(String id, WorkflowConnector w)
    throws WorkflowException {

        ProcessDefList processdeflist = null;
        ProcessDef     processdef     = null;

        processdeflist = new ProcessDefList(w);
        processdef     = (ProcessDef)processdeflist.getItemFromID(id);

        return(ProcessDef)processdef;

	}

    /**
     * When a Process is selected in the tree this method creates the Initial BreadCrumbBean.
     * The BreadCrumbBean is use by the JSP showMainSVGDiagram.jsp to display the breadcrumb trail.
     */

    private BreadCrumbBean createBreadCrumbInit(String id, WorkflowConnector w)
    throws WorkflowException {

		BreadCrumbBean     breadcrumb;
		BreadCrumbItemBean breadcrumbitem;
		boolean proceed    = true;

        Activity     activity     = null;
        ActivityList activitylist = null;
        String       processid    = null;

        activitylist = new ActivityList(w);
		breadcrumb   = new BreadCrumbBean();

        // The breadcrumb is created up to the final Top Process.
        // The Init breadcrumb trail creation only deals with Activities.
        processid = id;

   		activity = (Activity)activitylist.getItemFromID(processid);
   		// Get the parent
   		processid = activity.getProcessID();
   		if (processid.equals("")) {
   			// The Top Process is reached.
   			proceed = false;
   		}

   		while (proceed) {
            activity = (Activity)activitylist.getItemFromID(processid);
            breadcrumbitem = new BreadCrumbItemBean();
            breadcrumbitem.setId(activity.getID());
            breadcrumbitem.setName(activity.getName());
            breadcrumbitem.setDefinitionOrActivity("Activity");
  		    breadcrumb.addFirst(breadcrumbitem);
            processid = activity.getProcessID();
            if (processid.equals("")) {
       			proceed = false;
   			}
   		} // End while add BreadCrumbItems.

		return breadcrumb;
	}

    /**
     * When a Process is selected in the SVG Diagram this method adds a
     * BreadCrumbItem to the breadcrumb.
     */

    private void addBreadCrumbItem(BreadCrumbBean breadcrumb, String id, WorkflowConnector w)
    throws WorkflowException {

		BreadCrumbItemBean breadcrumbitem;

        Activity   activity   = null;
        ProcessDef processdef = null;
        String     processid  = null;
        boolean    definition = false;

        ActivityList   activitylist   = new ActivityList(w);
        ProcessDefList processdeflist = new ProcessDefList(w);

        // Is the ID belonging to a Activity or Definition?
        processid = id;

    	activity = (Activity)activitylist.getItemFromID(id);
		if (activity == null) {
		  	processdef = (ProcessDef)processdeflist.getItemFromID(id);
		   	definition = true;
		}

        breadcrumbitem = new BreadCrumbItemBean();
		if (definition) {
    	   	processdef = (ProcessDef)processdeflist.getItemFromID(processid);
            breadcrumbitem.setId(processdef.getID());
            breadcrumbitem.setName(processdef.getName());
            breadcrumbitem.setDefinitionOrActivity("Definition");
		    breadcrumb.add(breadcrumbitem);
		}
		else {
            breadcrumbitem = new BreadCrumbItemBean();
            breadcrumbitem.setId(activity.getID());
            breadcrumbitem.setName(activity.getName());
            breadcrumbitem.setDefinitionOrActivity("Activity");
          	breadcrumb.add(breadcrumbitem);
		}

		return;

	}

    /**
     * When a Process is selected in the breadcrumb trail this method removes
     * the BreadCrumbItem's including the item of the id that is past.
     */

    private void removeBreadCrumbItems(BreadCrumbBean breadcrumb, String id) {

		BreadCrumbItemBean breadcrumbitem;

		// Only remove the breadcrumb items if the id to be removed is in the list,
		// on a refresh the item is already removed by the previous action.
		LinkedList ll = (LinkedList)breadcrumb.getBreadCrumb();
		ListIterator li = ll.listIterator(0);
		boolean itemexists = false;

		while (li.hasNext()) {
			breadcrumbitem = (BreadCrumbItemBean)li.next();
			if (breadcrumbitem.getId().equals(id)) {
				itemexists = true;
				break;
			}
		}

        if (itemexists) {
            breadcrumbitem = (BreadCrumbItemBean)breadcrumb.removeLast();
            while (!(breadcrumbitem.getId().equals(id))) {
                breadcrumbitem = breadcrumb.removeLast();
    		}
		}

		return;

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
