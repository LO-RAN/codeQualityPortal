/**
*  Created by Struts Assistant.
*  Date: 05.01.2006  Time: 03:12:21
*/

package com.compuware.carscode.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.carscode.dbms.ProjectDao;
import com.compuware.optimalview.tasklist.User;

public class RetrieveProjectAction extends Action {

    public static final String PROJECT_LIST_KEY = "projectList";

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

    	User user = (User)request.getAttribute("user");
    	
        propageRequestAttributes(request, "task");
        propageRequestAttributes(request, "user");
        propageRequestAttributes(request, "exception");

        try {
            ProjectDao dao = new ProjectDao();
            List projectList = dao.retrieveProjectByUser(user.getUserId());
            request.setAttribute(PROJECT_LIST_KEY, projectList);
        }
        catch (Exception e) {
            servlet.log("Error during project retrieve", e);
            e.printStackTrace();
        }

        return (mapping.findForward("success"));
    }

    private void propageRequestAttributes(HttpServletRequest request, String attrName) {
        request.setAttribute(attrName, request.getAttribute(attrName));
    }

}
