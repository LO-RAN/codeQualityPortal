package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.comparators.FormulaFormComparator;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.util.Collections;
import net.sf.json.JSONArray;

public class CritereDisplayFormulasAction extends ExtJSGridAjaxAction {
    
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.CAQS_GENERIC_ERROR;
        String idCrit = request.getParameter("id_crit");
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        if(idCrit!=null && eltBean!=null) {
            String idUsa = eltBean.getUsage().getId();
            CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(idUsa);
            if(man != null) {
                man.setCritere(idCrit);
                List<FormuleForm> formules = man.getFormulas();
                Collections.sort(formules, new FormulaFormComparator(false, "score"));
                JSONArray array = new JSONArray();
                if(formules!=null) {
                    for(FormuleForm formule : formules) {
                        JSONObject obj = new JSONObject();
                        obj.put("score", formule.getScore());
                        obj.put("formula", formule.getReadableFormula(false, RequestUtil.getResources(request), RequestUtil.getLocale(request)));
                        array.add(obj);
                    }
                }
                this.putArrayIntoObject(array, retour);
                code = MessagesCodes.NO_ERROR;
            }
        }
        this.fillJSONObjectWithReturnCode(retour, code);
        return retour;
    }

}
