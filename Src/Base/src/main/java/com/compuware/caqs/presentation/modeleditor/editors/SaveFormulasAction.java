package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
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
public class SaveFormulasAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String critId = request.getParameter("critId");
        String modelId = request.getParameter("modelId");
        boolean alwaysTrue = RequestUtil.getBooleanParam(request, "alwaysTrueFormula", false);
        int alwaysTrueScore = RequestUtil.getIntParam(request, "alwaysTrueFormulaScore", 1);
        if (critId != null && modelId != null && !"".equals(critId) &&
                !"".equals(modelId)) {
            List<FormuleForm> formules = (List<FormuleForm>) request.getSession().getAttribute("formulasFor" +
                    modelId + critId);
            if (formules != null) {
                if (alwaysTrue) {
                    FormuleForm ff = new FormuleForm();
                    ff.setAlwaysTrue(alwaysTrue);
                    ff.setScore(alwaysTrueScore);
                    formules.add(ff);
                }
            }
            FormuleForm cost = (FormuleForm) request.getSession().getAttribute("costFormulaFor" +
                    modelId + critId);
            CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(modelId);
            if (manager != null) {
                manager.setCritere(critId);
                if (cost != null) {
                    manager.setCostFormula(cost);
                }
                manager.saveFormulasToDisk(formules);
            }
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
