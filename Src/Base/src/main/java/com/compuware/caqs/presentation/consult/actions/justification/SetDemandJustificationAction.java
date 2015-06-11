/**
 * Titre : <p>
 * Description : <p>
 * @author David Zysman
 * @version 1.0
 */
package com.compuware.caqs.presentation.consult.actions.justification;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.util.StringFormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.JustificationSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class SetDemandJustificationAction extends ExtJSAjaxAction {

    private static final long serialVersionUID = -2397885732541239663L;

    private MessagesCodes performJustificatif(HttpServletRequest request) {
        MessagesCodes retour = MessagesCodes.CAQS_GENERIC_ERROR;
        String idElt = request.getParameter("id_elt");
        String idBline = request.getParameter("id_bline");
        String idCrit = request.getParameter("id_crit");

        String idJust = request.getParameter("id_just");
        String cuser = request.getParameter("cuser");
        String justLib = request.getParameter("just_lib");
        String justDesc = request.getParameter("just_desc");
        justDesc = StringFormatUtil.replaceCarriageReturnByHTML(justDesc);
        String justNotecalc = request.getParameter("just_notecalc");

        double justNote = (justNotecalc == null) ? 0.0 : StringFormatUtil.parseDecimal(justNotecalc);
        boolean updflg = RequestUtil.getBooleanParam(request, "update", false);
        JustificationBean just = new JustificationBean(idJust, justNote, justLib, justDesc, "DEMAND", cuser, null, null);
        JustificationSvc justifSvc = JustificationSvc.getInstance();
        try {
            if (idCrit != null && idElt != null && idBline != null) {
                justifSvc.setJustification(idElt, idBline, idCrit, just, updflg);
                List<CaqsMessageBean> liste = MessagesSvc.getInstance().getSpecificTasksForUserAndEltId(TaskId.NEW_JUSTIF,
                        idElt, idBline, idJust, RequestUtil.getConnectedUserId(request));
                if (liste != null && !liste.isEmpty()) {
                    for (CaqsMessageBean message : liste) {
                        MessagesSvc.getInstance().setMessageAsSeen(message.getIdMessage());
                    }
                }

                List<String> l = new ArrayList<String>();
                l.add(Constants.MESSAGES_ID_JUST_INFO1 + idJust);
                MessagesSvc.getInstance().addMessageAndSetCompleted(TaskId.NEW_JUSTIF,
                        idElt, idBline, l, idJust, RequestUtil.getConnectedUserId(request));
                retour = MessagesCodes.NO_ERROR;
            }
        } catch (CaqsException e) {
            mLog.error("erreur durant justification", e);
        }

        return retour;
    }

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes code = this.performJustificatif(request);
        this.fillJSONObjectWithReturnCode(obj, code);
        return obj;
    }
}
