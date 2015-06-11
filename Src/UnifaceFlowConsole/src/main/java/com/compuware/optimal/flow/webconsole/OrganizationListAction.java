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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
 * This <strong>Action</strong> class builds a tree list of Organization
 * (Units->Actors->Roles) and displays the details of a selected
 * <strong>Unit</strong>, <strong>Actor</strong>, or <strong>Role</strong>.
 * <br>
 * On input, the <code>action</code> parameter is read. If no action parameter
 * is passed, this is the initial call to this Action and the tree of Units,
 * Actors, and Roles is built. The Units are saved in the
 * <strong>OrganizationItemsBean</strong>. The OrganizationItemsBean has a
 * Collection of <strong>OrganizationItemBeans</strong>, with each
 * OrganizationItemBean holding a Unit. Each Actor that is related to a Unit
 * is saved in the internal List of the OrganizationItemBean that also holds
 * OrganizationItemBeans. Each Role related to an Actor is saved again in the
 * internal list of the OrganizationItemBean that holds the Actor. The
 * OrganizationItemsBean is saved in request scope and is used by the JSP
 * showOrganizationTree.jsp.
 * <br>
 * If the action parameter contains <code>showorganizationdetails</code>, the
 * details of the selected item are retrieved and saved in request scope. The
 * details are used by the JSP  showOrganizationDetails.jsp.
 * <br>
 * The information built by this class is displayed using a frame set that
 * shows the main Menu at the top, the tree-like Unit-Actor-Role at the bottom
 * left, and the details of the selected Unit, Actor, or Role at the bottom
 * right. The HTML document organizationDiagram.html is the main document and
 * is called by choosing the option Organization definitions on the main
 * Menu (mainMenu.jsp).
 *
 * @see OrganizationItemBean
 * @see OrganizationItemsBean
 */

public final class OrganizationListAction extends Action {


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

    // Synchronize on the WorkflowConnector to prevent that deleteInstances() influences the
    // different actions.
	synchronized(w) {

    // There is an initial action that will retrieve all Units.
    // There is an additional action when the user selected a unit, actor or role.
    // Then the details of the unit, actor or role will be displayed.
    String action = null;
    action = request.getParameter("action");

    if (action == null) {
        OrganizationItemsBean unitlist = new OrganizationItemsBean();
        try {
     		// build the initial UnitList, including its actors and roles.
            unitlist = getUnitList(w);
			// Free up resources, no Workflow objects needed by JSP view.
			w.deleteInstances();
		}
		catch (WorkflowException e) {
    	if (servlet.getDebug() >= 1)
  			servlet.log("OrganizationListAction, WorkFlowException on retrieve of organization Tree: "
			            + e.getMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,
        	       new ActionError("error.fatal.retrieveorganizationtree") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        	   	   new ActionError("error.consult") );
        saveErrors(request, errors);
        // show the error in the select_view
    	return (mapping.findForward("failure"));
	    }
    	// Save the reference to the organizationitemsbean in request scope.
		request.setAttribute(Constants.UNITLIST_KEY, unitlist);

		// Forward control to the specified showorganizationtree URI
		return (mapping.findForward("showorganizationtree"));
	}

    if (action.equals("ShowDetails")) {
		// Details for a Unit, Actor or Role can be requested.
		String unitid, actorid, roleid;
		Unit  unitdetails  = null;
		Actor actordetails = null;
		Role  roledetails  = null;
		unitid  = request.getParameter("UnitID");
		actorid = request.getParameter("ActorID");
		roleid  = request.getParameter("RoleID");
        try {
    		// We need to distinguish between details for a Unit, Actor or Role.
			if (unitid != null)
			    unitdetails = getUnitDetails(unitid, w);
			if (actorid != null)
			    actordetails = getActorDetails(actorid, w);
			if (roleid != null)
			    roledetails = getRoleDetails(roleid, w);
			// Free up resources, no Workflow objects needed by JSP view.
			w.deleteInstances();
		}
		catch (WorkflowException e) {
    	if (servlet.getDebug() >= 1)
  			servlet.log("OrganizationListAction, WorkFlowException on get Details: "
			            + e.getMessage());
        errors.add(ActionErrors.GLOBAL_ERROR,
        	       new ActionError("error.fatal.organizationdetails") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        	   	   new ActionError("error.consult") );
        saveErrors(request, errors);
        // use the failure JSP.
    	return (mapping.findForward("failure"));
	    }
	    // Save the Organization details object
	    if (unitid != null)
		    request.setAttribute(Constants.UNITDETAILS_KEY, unitdetails);
	    if (actorid != null)
		    request.setAttribute(Constants.ACTORDETAILS_KEY, actordetails);
	    if (roleid != null)
		    request.setAttribute(Constants.ROLEDETAILS_KEY, roledetails);

		// Forward control to the specified showorganizationdetails URI
		return (mapping.findForward("showorganizationdetails"));

    }
    } // End of synchronization on the WorkflowConnector object.

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
     * This method builds the list of Unit's. Each Unit holding Actors and
     * each Actor holding Roles.
     */

    private OrganizationItemsBean getUnitList(WorkflowConnector w)
    throws WorkflowException {

    	UnitList unitlist;
    	Unit     unit;
    	List     units;
    	Iterator unititerator, actoriditerator, roleiditerator;

    	ActorList actorlist;
    	Actor     actor;

    	RoleList  rolelist;
    	Role      role;

        // All Units are retrieved.
	    unitlist     = new UnitList(w);
	    units        = unitlist.getTop();
	    unititerator = units.iterator();

	    actorlist    = new ActorList(w);
	    rolelist     = new RoleList(w);

        // For each Unit the Actors are retrieved.
        // For each retrieved Actor the Roles are retrieved.
        OrganizationItemsBean orgbeans = new OrganizationItemsBean();

        while (unititerator.hasNext()) {
   	    	unit = (Unit)unititerator.next();
   	        OrganizationItemBean orgunit = new OrganizationItemBean();
            orgunit.setName(unit.getName());
            orgunit.setId(unit.getID());
            orgunit.setEntityName(unit.getEntityName());

            // retrieve the Actors. Method getActors returns a list of actorids.
            Set actorids = unit.getActors();
            actoriditerator = actorids.iterator();
            while (actoriditerator.hasNext()) {
				actor = (Actor)actorlist.getItemFromID( (String)actoriditerator.next() );
                OrganizationItemBean orgactor = new OrganizationItemBean();
                orgactor.setName(actor.getName());
                orgactor.setId(actor.getID());
                orgactor.setEntityName(actor.getEntityName());

                // retrieve the Roles for the Actor.
                Set roleids = actor.getRoles();
                roleiditerator = roleids.iterator();
                while (roleiditerator.hasNext()) {
					role = (Role)rolelist.getItemFromID( (String)roleiditerator.next() );
					OrganizationItemBean orgrole = new OrganizationItemBean();
					orgrole.setName(role.getName());
					orgrole.setId(role.getID());
					orgrole.setEntityName(role.getEntityName());

					// add the Role to the Actor.
					orgactor.add(orgrole);
				}

				// add the Actor to the Unit.
				orgunit.add(orgactor);
			}

			// add the Unit to the orgbeans
			orgbeans.add(orgunit);
		}

		return (orgbeans);

	}

    /**
     * Retrieve the Unit identified by the id.
     */

    private Unit getUnitDetails(String id, WorkflowConnector w)
    throws WorkflowException {

        Unit unit;

        UnitList unitlist = new UnitList(w);
        unit = (Unit)unitlist.getItemFromID(id);

        return(unit);

	}

    /**
     * Retrieve the Actor identified by the id.
     */

    private Actor getActorDetails(String id, WorkflowConnector w)
    throws WorkflowException {

        Actor actor;

        ActorList actorlist = new ActorList(w);
        actor = (Actor)actorlist.getItemFromID(id);

        return(actor);

	}

    /**
     * Retrieve the Role identified by the id.
     */

    private Role getRoleDetails(String id, WorkflowConnector w)
    throws WorkflowException {

        Role role;

        RoleList rolelist = new RoleList(w);
        role = (Role)rolelist.getItemFromID(id);

        return(role);

	}

}
