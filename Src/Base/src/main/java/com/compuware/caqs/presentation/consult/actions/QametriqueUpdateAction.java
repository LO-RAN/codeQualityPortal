package com.compuware.caqs.presentation.consult.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.dbms.DataAccessCache;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.messages.MessagesSvc;
import net.sf.json.JSONObject;

public final class QametriqueUpdateAction extends ExtJSAjaxAction {

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
        double valbrute = RequestUtil.getDoubleParam(request, "valbrute", 0.0);

        if (eltBean != null && valbrute > 0) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
            try {
                metriqueDao.setMetrique(idElt, eltBean.getBaseline().getId(), idMet, valbrute);
                MessagesSvc.getInstance().addMessageAndSetCompleted(TaskId.JUSTIF_ACCEPTED, eltBean.getId(), eltBean.getBaseline().getId(),
                        null, null, RequestUtil.getConnectedUserId(request));
            } catch (DataAccessException e) {
                mLog.error("error while updating qametrique", e);
                code = MessagesCodes.DATABASE_ERROR;
            }
            DataAccessCache dataCache = DataAccessCache.getInstance();
            dataCache.clearCache(eltBean.getBaseline().getId());
            request.getSession().removeAttribute("critereList");
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);
        return retour;
    }
}
