package com.compuware.caqs.presentation.consult.actions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import net.sf.json.JSONArray;

public final class LoadCritereDatasAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = new JSONObject();

        String idCrit = request.getParameter("id_crit");
        if (idCrit != null) {
            ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
            object.put("idEa", eltBean.getId());
            object.put("idBline", eltBean.getBaseline().getId());
            Locale locale = RequestUtil.getLocale(request);
            DaoFactory daoFactory = DaoFactory.getInstance();
            CriterionDao criterionFacade = daoFactory.getCriterionDao();
            CriterionDefinition critDef = criterionFacade.retrieveCriterionDefinitionByKey(idCrit, eltBean.getUsage().getId());
            object.put("critId", critDef.getId());
            object.put("critLib", critDef.getLib(locale));
            object.put("escapedCritLib", critDef.getEscapedLib(false, true, true, locale));
            object.put("escapedCritDesc", critDef.getEscapedDesc(false, true, true, locale));

            JSONArray array = new JSONArray();

            if (critDef != null) {
                if (critDef.getMetriquesDefinitions() != null && (critDef.getMetriquesDefinitions().size()
                        > 0)) {
                    for(MetriqueDefinitionBean met : critDef.getMetriquesDefinitions()) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", met.getId());
                        obj.put("lib", met.getEscapedLib(false, false, true, locale));
                        obj.put("desc", met.getEscapedDesc(false, false, true, locale));
                        array.add(obj);
                    }
                }
            }
            object.put("metriques", array);
        }


        return object;
    }
}
