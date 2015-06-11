package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.SyntheseObjectifsSvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import java.util.Locale;
import org.apache.struts.util.MessageResources;

/**
 * gere la mise a jour des commentaires pour un critere et un element donnes
 * @author cwfr-dzysman
 */
public class FacteurSyntheseManageCommentAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = new JSONObject();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);

        String idCrit = request.getParameter("idCrit");
        String comment = request.getParameter("comment");
        String idFact = request.getParameter("idFact");

        MessagesCodes retour = MessagesCodes.NO_ERROR;

        if (comment != null) {
            comment = StringFormatUtil.replaceCarriageReturnByHTML(comment);
            ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
            if (idCrit != null) {
                retour = SyntheseObjectifsSvc.getInstance().updateCommentForCriterion(eltBean, idCrit, comment);
            } else if (idFact != null) {
                retour = SyntheseObjectifsSvc.getInstance().updateCommentForFactor(eltBean, idFact, comment);
            }
        }

        object.put("comment", comment);

        this.fillJSONObjectWithReturnCode(object, retour);
        return object;
    }
}
