/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:24:25
*/

package com.compuware.caqs.presentation.admin.actions;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.toolbox.util.logging.LoggerManager;

public class StereotypeMgmtElementUpdateAction extends StereotypeAction {

    public static final String SELECTED_ELEMENTS_KEY = "selectedElements";
    public static final String SELECTED_STEREOTYPE_KEY = "selectedStereotype";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        LoggerManager.pushContexte("StereotypeMgmtElementUpdateAction");

        String[] elementArray = request.getParameterValues(SELECTED_ELEMENTS_KEY);
        String stereotype = request.getParameter(SELECTED_STEREOTYPE_KEY);

        if (elementArray != null) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            ElementDao elementDao = daoFactory.getElementDao();
            elementDao.setElementStereotype(Arrays.asList(elementArray), stereotype);
        }

        LoggerManager.popContexte();

        return mapping.findForward(FORWARD_SUCCESS);
    }
}
