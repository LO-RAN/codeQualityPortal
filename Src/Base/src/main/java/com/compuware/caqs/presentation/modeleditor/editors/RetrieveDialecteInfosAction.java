package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorDialecteBean;
import com.compuware.caqs.presentation.modeleditor.common.ManageInfosAction;
import com.compuware.caqs.service.DialecteSvc;
import java.lang.String;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveDialecteInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;

        String id = request.getParameter("id");
        boolean success = false;
        if (id != null && !"".equals(id)) {
            ModelEditorDialecteBean metric = DialecteSvc.getInstance().retrieveDialectWithAssociationCountById(id);
            if (metric != null) {
                success = true;
                retour.put("id", metric.getId());
                retour.put("lib", metric.getLib());
                retour.put("desc", metric.getDesc());
                if(metric.getLangage()!=null) {
                    retour.put("idLangage", metric.getLangage().getId());
                }
                retour.put("nbEAs", metric.getNbEAsAssociated());
            } else {
                code = MessagesCodes.DATABASE_ERROR;
            }
        } else {
            retour.put("id", "");
            retour.put("lib", "");
            retour.put("desc", "");
            retour.put("idLangage", "");
            retour.put("nbEAs", 0);
            success = true;
        }

        if (!success) {
            mLog.error("no metric retrieved for id " + id);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, code);

        return retour;
    }
}
