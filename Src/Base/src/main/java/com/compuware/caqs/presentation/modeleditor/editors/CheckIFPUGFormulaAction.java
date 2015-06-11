package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPart;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class CheckIFPUGFormulaAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String formula = request.getParameter("formula");
        String idUsa = request.getParameter("id_usa");
        if (formula!=null && !"".equals(formula)) {
            FormulaPart ff = CaqsFormulaManager.getFormulaFromFormattedFormula(formula);
            if(ff!=null) {
                Locale loc = RequestUtil.getLocale(request);
                MessageResources resources = RequestUtil.getResources(request);
                FormuleForm formuleForm = new FormuleForm();
                formuleForm.setFormula(ff);
                CaqsFormulaManager.checkIFPUGFormulaPart(idUsa, ff);
                retour.put("ifpugHasError", CaqsFormulaManager.formulaHasError(ff));
                retour.put("ifpug", formuleForm.getReadableFormula(true, resources, loc));
                retour.put("ifpugWithoutErrors", formuleForm.getReadableFormula(false, resources, loc));
            }
        } else {
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
