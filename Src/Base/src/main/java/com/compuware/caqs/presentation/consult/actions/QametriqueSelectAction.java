package com.compuware.caqs.presentation.consult.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import net.sf.json.JSONObject;

public final class QametriqueSelectAction extends ExtJSAjaxAction {

    // --------------------------------------------------------- Public Methods
    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idElt = request.getParameter("id_elt");
        String idMet = request.getParameter("id_met");
        request.setAttribute("id_crit", request.getParameter("id_crit"));
        request.setAttribute("all", request.getParameter("all"));
        request.setAttribute("allInfos", request.getParameter("allInfos"));
        if (eltBean != null && idMet != null && idElt != null) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
            MetriqueBean result = metriqueFacade.retrieveQametriqueFromMetEltBline(eltBean.getBaseline().getId(), idMet, idElt);
            beanToJSON(result, getLocale(request), retour);
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);
        return retour;
    }


    private JSONObject beanToJSON(MetriqueBean bean, Locale loc, JSONObject retour) {
        retour.put("libElt", bean.getLib(loc));
        double valbrute = (bean.getValbrute() > 0) ? bean.getValbrute() : 1;
        retour.put("valbrute", valbrute);
        return retour;
    }
}
