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
public class MoveFormulaAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String critId = request.getParameter("critId");
        String modelId = request.getParameter("modelId");
        String sens = request.getParameter("sens");
        int formulaIndex = RequestUtil.getIntParam(request, "formulaIndex", -1);

        if (critId != null && modelId != null && sens != null &&
                !"".equals(critId) && !"".equals(modelId)) {
            List<FormuleForm> formules = (List<FormuleForm>) request.getSession().getAttribute("formulasFor" +
                    modelId + critId);
            if (formules != null && formulaIndex >= 0) {
                FormuleForm ff = null;
                if ("up".equals(sens)) {
                    if (formulaIndex > 0) {
                        ff = formules.remove(formulaIndex);
                        formules.add(formulaIndex - 1, ff);
                    }
                } else if ("down".equals(sens)) {
                    if (formulaIndex != (formules.size() - 1)) {
                        ff = formules.remove(formulaIndex);
                        if (formulaIndex < (formules.size() - 1)) {
                            formules.add(formulaIndex, ff);
                        } else {
                            formules.add(ff);
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
