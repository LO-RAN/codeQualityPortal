/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:23:06
*/

package com.compuware.caqs.presentation.admin.actions;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.StereotypeDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementRetrieveFilterBean;
import com.compuware.caqs.presentation.admin.forms.ElementRetrieveFilterForm;
import com.compuware.toolbox.util.logging.LoggerManager;

public class StereotypeMgmtElementRetrieveAction extends StereotypeAction {

    public static final String FILTER_BEAN_KEY = "filterBeanKey";
    public static final String SUBMIT_KEY = "action";

    public static final String ELEMENT_RETRIEVED_COLLECTION = "elementRetrieved";
    public static final String STEREOTYPE_RETRIEVED_COLLECTION = "stereotypeRetrieved";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        LoggerManager.pushContexte("StereotypeMgmtElementRetrieveAction");

        HttpSession session = request.getSession();

        ElementRetrieveFilterForm filterForm = (ElementRetrieveFilterForm)form;
        ElementBean eltBean = (ElementBean)session.getAttribute(StereotypeAction.CURRENT_EA);

        if (request.getParameter(SUBMIT_KEY) == null) {
            loadFilter(filterForm, request);
        }

        ElementRetrieveFilterBean filterBean = saveFilter(filterForm, request);

        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementDao = daoFactory.getElementDao();
        Collection<ElementBean> elementCollection = elementDao.retrieveSubElementForStereotypeMgmt(eltBean.getId(), filterBean);

        StereotypeDao stereotypeAf = daoFactory.getStereotypeDao();
        Collection stereotypeCollection = stereotypeAf.retrieveAllStereotype();

        LoggerManager.popContexte();

        request.setAttribute(ELEMENT_RETRIEVED_COLLECTION, elementCollection);
        request.setAttribute(STEREOTYPE_RETRIEVED_COLLECTION, stereotypeCollection);

        return mapping.findForward(FORWARD_SUCCESS);
    }

    private ElementRetrieveFilterBean saveFilter(ElementRetrieveFilterForm filterForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ElementRetrieveFilterBean filterBean = new ElementRetrieveFilterBean();
        filterBean.setDescElt(filterForm.getDescElt());
        filterBean.setTypeElt(filterForm.getTypeElt());
        filterBean.setIdStereotype(filterForm.getIdStereotype());
        filterBean.setEmptyOnly(filterForm.getEmptyOnly() != null);
        session.setAttribute(FILTER_BEAN_KEY, filterBean);
        return filterBean;
    }

    private void loadFilter(ElementRetrieveFilterForm filterForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ElementRetrieveFilterBean filterBean = (ElementRetrieveFilterBean)session.getAttribute(FILTER_BEAN_KEY);
        if (filterBean != null) {
          filterForm.setDescElt(filterBean.getDescElt());
          filterForm.setTypeElt(filterBean.getTypeElt());
          filterForm.setIdStereotype(filterBean.getIdStereotype());
          if (filterBean.getEmptyOnly()) {
              filterForm.setEmptyOnly("on");
          }
          else {
              filterForm.setEmptyOnly(null);
          }
        }
    }
}
