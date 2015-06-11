package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorLanguageBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.service.DialecteSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveLanguageInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        if (id != null && !"".equals(id)) {
            ModelEditorLanguageBean lb = DialecteSvc.getInstance().retrieveLanguageWithAssociationCountById(id);
            if (lb != null) {
                success = true;
                retour.put("id", lb.getId());
                retour.put("lib", lb.getLib());
                retour.put("desc", lb.getDesc());
                retour.put("nbDialects", lb.getNbDialectsAssociated());
            }
        } else {
            retour.put("id", "");
            retour.put("lib", "");
            retour.put("desc", "");
            retour.put("nbDialects", 0);
            success = true;
        }

        if (!success) {
            mLog.error("no language retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
