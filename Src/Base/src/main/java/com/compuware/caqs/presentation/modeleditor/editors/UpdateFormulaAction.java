package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class UpdateFormulaAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String critId = request.getParameter("critId");
        String modelId = request.getParameter("modelId");
        String formula = request.getParameter("formula");
        int formulaIndex = RequestUtil.getIntParam(request, "formulaIndex", -1);
        String action = request.getParameter("action");
        if (critId != null && modelId != null && !"".equals(critId) &&
                !"".equals(modelId)) {
            if ("updateCostCorrection".equals(action)) {
                FormuleForm ff = (FormuleForm) request.getSession().getAttribute("costFormulaFor" +
                        modelId + critId);
                if(ff == null) {
                    //il n'y en avait pas
                    ff = new FormuleForm();
                }
                ff.setFormula(CaqsFormulaManager.getFormulaFromFormattedFormula(formula));
                request.getSession().setAttribute("costFormulaFor" +
                        modelId + critId, ff);
            } else {
                List<FormuleForm> formules = (List<FormuleForm>) request.getSession().getAttribute("formulasFor" +
                        modelId + critId);
                if (formules != null) {
                    if ("update".equals(action) && formula != null) {
                        if (formulaIndex >= 0) {
                            FormuleForm ff = formules.get(formulaIndex);
                            if (ff != null) {
                                ff.setFormula(CaqsFormulaManager.getFormulaFromFormattedFormula(formula));
                            }
                        } else {
                            //nous inserons une nouvelle formule.
                            FormuleForm ff = new FormuleForm();
                            ff.setFormula(CaqsFormulaManager.getFormulaFromFormattedFormula(formula));
                            formules.add(ff);
                        }
                    } else if ("delete".equals(action)) {
                        formules.remove(formulaIndex);
                    } else if ("updatescore".equals(action)) {
                        if (formulaIndex >= 0) {
                            FormuleForm ff = formules.get(formulaIndex);
                            if (ff != null) {
                                ff.setScore(RequestUtil.getIntParam(request, "score", 1));
                            }
                        }
                    }
                }
            }
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
