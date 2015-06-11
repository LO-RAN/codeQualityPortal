package com.compuware.caqs.presentation.consult.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.JustificationSvc;

public final class RetrieveJustificationForDemandAction extends ExtJSAjaxAction {

    public void executeDemand(JSONObject obj, HttpServletRequest request) {
        String idJust = request.getParameter("id_just");
        
        try {
            String userId = RequestUtil.getConnectedUserId(request);
            JustificationSvc justifSvc = JustificationSvc.getInstance();
            JustificationBean justif = null;
            boolean update = true;
            if(idJust!=null && !"".equals(idJust)){
                justif = justifSvc.getJustificatif(idJust, 0.0);
            }
            if (justif == null) {
                justif = new JustificationBean();
                update = false;
            }
            obj.put("idJust", justif.getId());
            obj.put("libJust", justif.getEscapedLib(true, false, false));
            obj.put("descJust", justif.getEscapedDesc(true, false, false));
            obj.put("cuser", (justif.getUser()!=null) ? justif.getUser() : userId);
            obj.put("update", update);
        } catch (CaqsException e) {
            mLog.error("erreur durant recuperation de justification", e);
        }

    }

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        this.executeDemand(retour, request);
        return retour;
    }
}
