/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveDomainLanguageGroupsListAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String idDomain = request.getParameter("domainId");

        JSONArray array = this.getAllLanguagesForDomain(idDomain, request);
        this.putArrayIntoObject(array, retour);
        return retour;
    }

    private JSONArray getAllLanguagesForDomain(String idDomain, HttpServletRequest request) {
        JSONArray retour = null;
        List<ElementBean> elts = (List<ElementBean>) request.getSession().getAttribute("allEAsFor" + idDomain);
        if (elts != null) {
            Map<String, List<ElementBean>> languagesElts = new HashMap<String, List<ElementBean>>();
            Map<String, LanguageBean> languageBeansMap = new HashMap<String, LanguageBean>();

            for (ElementBean elt : elts) {
                if(elt.getDialecte()==null) {
                    continue;
                }
                LanguageBean lb = elt.getDialecte().getLangage();
                BaselineBean bb = elt.getBaseline();
                if (lb != null && bb != null) {
                    List<ElementBean> liste = languagesElts.get(lb.getId());
                    if (languageBeansMap.get(lb.getId()) == null) {
                        languageBeansMap.put(lb.getId(), lb);
                        liste = new ArrayList<ElementBean>();
                        languagesElts.put(lb.getId(), liste);
                    }
                    liste.add(elt);
                }
            }

            request.getSession().setAttribute("languagesElts", languagesElts);

            List<JSONObject> liste = new ArrayList<JSONObject>();
            for (Map.Entry<String, LanguageBean> entry : languageBeansMap.entrySet()) {
                JSONObject obj = new JSONObject();
                obj.put("title", entry.getValue().getLib());
                obj.put("idLanguage", entry.getKey());
                liste.add(obj);
            }
            retour = JSONArray.fromObject(liste.toArray(new JSONObject[0]));
        }
        return retour;
    }
}
