package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.LabelSvc;
import org.apache.struts.util.MessageResources;

public final class LabelUpdateAjaxAction extends ExtJSAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        // ActionErrors needed for error passing
        HttpSession session = request.getSession();

        LabelBean label = doUpdate(request, session);
        obj.put("labelId", label.getId());

        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        obj.put("newButtonLabel", resources.getMessage(loc, "caqs.synthese.labellisationdemandee"));

        return obj;
    }

    private LabelBean doUpdate(HttpServletRequest request,
            HttpSession session) {
        LabelBean retour = null;
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        try {
            retour = createBean(eltBean, request);
            LabelSvc.getInstance().store(retour, eltBean);
        } catch (CaqsException e) {
            mLog.error("Error during label store", e);
        }
        return retour;
    }

    private LabelBean createBean(ElementBean eltBean, HttpServletRequest request) {
        LabelBean result = null;
        String id = request.getParameter("labelId");
        String lib = request.getParameter("labelLib");
        String desc = request.getParameter("labelDesc");
        String user = request.getParameter("labelUser");
        result = new LabelBean();
        result.setId(id);
        result.setLib(lib);
        if (desc != null) {
            desc = StringFormatUtil.replaceCarriageReturnByHTML(desc);
        }
        result.setDesc(desc);
        result.setStatut("DEMAND");
        result.setUser(user);
        result.setBaseline(eltBean.getBaseline());
        return result;
    }
}
