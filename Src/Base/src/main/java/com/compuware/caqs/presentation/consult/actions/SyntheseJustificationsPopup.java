package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.presentation.util.RequestUtil;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.JustificationSvc;

public class SyntheseJustificationsPopup extends ExtJSAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject root = new JSONObject();
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        if (eltBean != null) {
            String type = request.getParameter("type");
            Collection<JustificatifResume> liste = this.retrieveJustifications(request, eltBean, type);
            if (liste != null) {
                JSONObject[] objects = new JSONObject[liste.size()];
                int i = 0;
                for (JustificatifResume resume : liste) {
                    objects[i] = new JSONObject();
                    objects[i].put("lib_just", resume.getJustificatifLibelle());
                    objects[i].put("dmaj", java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT,
                            RequestUtil.getLocale(request)).format(resume.getLastModifDate()));
                    objects[i].put("lib_crit", resume.getCritfacLabel(RequestUtil.getLocale(request)));
                    objects[i].put("auteur", resume.getJustificatifAuteur());
                    objects[i].put("desc", resume.getJustificatifDesc());
                    objects[i].put("eltLib", resume.getElementLabel());
                    objects[i].put("before", resume.getOldMark());
                    objects[i].put("note", resume.getNoteJustificatif());
                    objects[i].put("type", type);
                    objects[i].put("id", resume.getId());
                    i++;
                }

                JSONArray array = JSONArray.fromObject(objects);
                root.put("justifs", array);
                root.put("totalCount", "" + objects.length);
            }
        }
        return root;
    }

    private Collection<JustificatifResume> retrieveJustifications(HttpServletRequest request, ElementBean eltBean, String type) {
        JustificationSvc justificationSvc = JustificationSvc.getInstance();
        Collection<JustificatifResume> retour = null;
        try {
            retour = justificationSvc.getAllCriterionJustificationsForElement(type,
                    eltBean.getId(), eltBean.getBaseline().getId());
        } catch (CaqsException e) {
            mLog.error("retrieveJustifications:" + e.getMessage());
        }
        return retour;
    }
}
