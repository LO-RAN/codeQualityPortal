/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:25:30
*/

package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StereotypeNewAction extends StereotypeAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        return mapping.findForward("success");
    }
}
