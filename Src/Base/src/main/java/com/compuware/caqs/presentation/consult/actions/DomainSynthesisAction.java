package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.MessagesCodes;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import java.util.Iterator;

/**
 * Accede a la synthese d'un domaine
 * @author cwfr-dzysman
 */
public class DomainSynthesisAction extends ExtJSAjaxAction {

    private void getAllLanguagesForDomain(String idDomain, HttpServletRequest request) {
        ElementSvc elementSvc = ElementSvc.getInstance();

        List<ElementBean> elts = elementSvc.retrieveAllElementsForTypeBelongingToParentByUser(idDomain, RequestUtil.getConnectedUserId(request),
                ElementType.EA);

        for (Iterator<ElementBean> it = elts.iterator(); it.hasNext();) {
            ElementBean elt = it.next();
            BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(elt);
            if (bb != null) {
                bb.setProject(elt.getProject());
                elt.setBaseline(bb);
            } else {
                it.remove();
            }
        }

        request.getSession().setAttribute("allEAsFor" + idDomain, elts);
    }


    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.CAQS_GENERIC_ERROR;
        String idDomain = request.getParameter("domainId");

        if (idDomain != null) {
            ElementSvc elementSvc = ElementSvc.getInstance();
            List<ElementBean> projects = elementSvc.retrieveAllElementsForTypeBelongingToParentByUser(idDomain, RequestUtil.getConnectedUserId(request),
                    ElementType.PRJ, true);
            this.getAllLanguagesForDomain(idDomain, request);
            if (projects != null) {
                request.getSession().setAttribute("domainSynthesisProjectsList", projects);
            }
            obj.put("numberOfProjects", projects.size());
            retour = MessagesCodes.NO_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }
}
