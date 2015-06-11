package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorModelBean;
import com.compuware.caqs.domain.dataschemas.modelmanager.IFPUGFormulaForm;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ModelSvc;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
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
public class RetrieveModelInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (id != null && !"".equals(id)) {
            ModelEditorModelBean model = ModelSvc.getInstance().retrieveModelWithAssociationCountById(id);
            if (model != null) {
                success = true;
                retour.put("id", model.getId());
                retour.put("nbEAsUsingIt", model.getNbEAAssociated());
                retour.put("mediumRun", model.getLowerLimitMediumRun());
                retour.put("longRun", model.getLowerLimitLongRun());
                CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(model.getId());
                if (man != null) {
                    IFPUGFormulaForm ifpug = man.getIFPUGFormula();
                    if (ifpug != null) {
                        String ifpugFormula = "";
                        if (ifpug != null) {
                            ifpugFormula = ifpug.getReadableFormula(true, resources, loc);
                        }
                        retour.put("ifpug", ifpugFormula);
                        retour.put("idfugIdTelt", ifpug.getElementType().getId());
                        retour.put("idfugLibTelt", ifpug.getElementType().getLib(loc));
                        if (ifpug != null) {
                            ifpugFormula = ifpug.getReadableFormula(false, resources, loc);
                        }
                        retour.put("ifpugWithoutErrors", ifpugFormula);
                        retour.put("ifpugHasErrors", CaqsFormulaManager.formulaHasError(ifpug.getFormula()));
                    }
                }
                this.addTimestamps(model, retour, resources, loc);
                this.addLanguagesFieldsToJSON(model, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            retour.put("apuType", "");
            retour.put("mediumRun", 10);
            retour.put("longRun", 20);
            retour.put("nbEAsUsingIt", 0);
            this.addTimestamps(null, retour, resources, loc);
            this.addLanguagesFieldsToJSON(null, retour, new String[]{"lib", "desc", "compl"}, resources, loc);
            success = true;
        }

        if (!success) {
            mLog.error("no model retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
