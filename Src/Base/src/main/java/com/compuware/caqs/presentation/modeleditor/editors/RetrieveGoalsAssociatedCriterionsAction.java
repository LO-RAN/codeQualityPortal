package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.comparators.CriterionDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.CriterionSvc;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveGoalsAssociatedCriterionsAction extends ExtJSGridAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String goalId = request.getParameter("goalId");
        String modelId = request.getParameter("modelId");
        MessageResources resources = RequestUtil.getResources(request);
        List<CriterionDefinition> result = null;
        if (goalId != null && modelId!=null) {
            result = CriterionSvc.getInstance().retrieveCriterionDefinitionByGoalAndModel(goalId, modelId);
        } else {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
            mLog.error("RetrieveGoalsAssociatedCriterionsAction => Erreur lors de l'envoi de parametres : goalId="+goalId+", modelId="+modelId);
        }

        if (result != null) {
            CaqsQualimetricModelManager modelManager = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
            Locale loc = RequestUtil.getLocale(request);
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equalsIgnoreCase(sens.toLowerCase());
                Collections.sort(result, new CriterionDefinitionComparator(!asc,
                        sort, loc));
            }

            int gridSize = result.size();
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (CriterionDefinition elt : result) {
                JSONObject obj = new JSONObject();
                obj.put("id", elt.getId());
                obj.put("lib", elt.getLib(loc));
                obj.put("desc", elt.getDesc(loc));
                obj.put("compl", elt.getComplement(loc));
                obj.put("weight", elt.getWeight());
                obj.put("telt", elt.getElementType().getId());
                boolean formulas = false;
                boolean agregationsPresent = false;
                if(modelManager!=null) {
                    modelManager.setCritere(elt.getId());
                    List<FormuleForm> formulasList = modelManager.getFormulas();
                    if(!formulasList.isEmpty()) {
                        formulas = true;
                        JSONArray array = new JSONArray();
                        for(FormuleForm ff : formulasList) {
                            JSONObject formObj = new JSONObject();
                            String sFormula = ff.getReadableFormula(false, resources, loc);
                            if("".equals(sFormula)) {
                                sFormula = resources.getMessage(loc, "caqs.modeleditor.modelEdition.formula.alwaysTrue");
                            }
                            formObj.put("formula", sFormula);
                            formObj.put("score", ff.getScore());
                            array.add(formObj);
                        }
                        obj.put("formulaArray", array);
                    }
                    agregationsPresent = !modelManager.getAggregations().isEmpty();
                }
                obj.put("formulasPresent", formulas);
                obj.put("agregationsPresent", agregationsPresent);
                l.add(obj);
            }
            root.put("totalCount", gridSize);
            root.put("criterions", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(root, retour);
        return root;
    }
}
