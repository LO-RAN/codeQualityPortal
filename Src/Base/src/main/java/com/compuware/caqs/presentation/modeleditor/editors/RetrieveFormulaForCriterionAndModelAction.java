package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.util.Iterator;
import java.util.Locale;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;

public class RetrieveFormulaForCriterionAndModelAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject root = new JSONObject();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        String modelId = request.getParameter("modelId");
        String idCrit = request.getParameter("critId");
        int totalCount = 0;
        List<JSONObject> l = new ArrayList<JSONObject>();
        List<FormuleForm> formules = null;
        boolean firstLoad = true;
        String sFirstLoad = request.getParameter("firstLoad");
        if (sFirstLoad != null) {
            firstLoad = Boolean.parseBoolean(sFirstLoad);
        }
        if (idCrit != null && modelId != null) {
            List<MetriqueDefinitionBean> metriques = MetricSvc.getInstance().retrieveMetriqueDefinitionByIdCrit(idCrit, modelId);
            List<String> ids = new ArrayList<String>();
            for (MetriqueDefinitionBean mdf : metriques) {
                ids.add(mdf.getId());
            }
            formules = (List<FormuleForm>) request.getSession().getAttribute("formulasFor" +
                    modelId + idCrit);
            FormuleForm costFormula = (FormuleForm) request.getSession().getAttribute("costFormulaFor" +
                    modelId + idCrit);
            if (formules == null || firstLoad) {
                CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
                if (manager != null) {
                    if (manager.setCritere(idCrit)) {
                        formules = manager.getFormulas();
                        request.getSession().setAttribute("formulasFor" +
                                modelId + idCrit, formules);
                        costFormula = manager.getCostFormula();
                        request.getSession().setAttribute("costFormulaFor" +
                                modelId + idCrit, costFormula);
                    }
                }
            }
            if (costFormula != null) {
                CaqsFormulaManager.checkFormulaPart(costFormula.getFormula(), ids);
                root.put("costFormulaWithErrors", costFormula.getReadableFormula(true, resources, loc));
                root.put("costFormulaWithoutErrors", costFormula.getReadableFormula(false, resources, loc));
                root.put("costFormulaHasErrors", CaqsFormulaManager.formulaHasError(costFormula.getFormula()));
            }
            totalCount = formules.size();
            boolean alwaysTrueFormula = false;
            int alwaysTrueScore = 1;
            for (Iterator<FormuleForm> it = formules.iterator(); it.hasNext();) {
                FormuleForm elt = it.next();
                CaqsFormulaManager.checkFormulaPart(elt.getFormula(), ids);
                if (!elt.isAlwaysTrue()) {
                    JSONObject o = this.formulaFormToJSONObject(elt, resources, loc);
                    l.add(o);
                } else {
                    alwaysTrueFormula = true;
                    alwaysTrueScore = elt.getScore();
                    //on sait que l'on a une formule always true, on la retire
                    //de la liste des formules à afficher dans le tableau
                    it.remove();
                }
            }
            root.put("alwaysTrueFormula", "" + alwaysTrueFormula);
            root.put("alwaysTrueScore", "" + alwaysTrueScore);
        }
        root.put("totalCount", totalCount);
        root.put("elements", l.toArray(new JSONObject[0]));

        return root;
    }

    private JSONObject formulaFormToJSONObject(FormuleForm elt, MessageResources resources, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("score", elt.getScore());
        retour.put("formulaWithErrors", elt.getReadableFormula(true, resources, loc));
        retour.put("formulaWithoutErrors", elt.getReadableFormula(false, resources, loc));
        retour.put("hasError", CaqsFormulaManager.formulaHasError(elt.getFormula()));
        return retour;
    }
}
