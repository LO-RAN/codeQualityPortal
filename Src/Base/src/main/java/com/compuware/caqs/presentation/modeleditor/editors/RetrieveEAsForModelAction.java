package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.service.ElementSvc;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveEAsForModelAction extends ExtJSGridAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String idUsa = request.getParameter("idUsa");
        Map<String, List<ElementBean>> result = null;
        if (idUsa != null) {
            result = ElementSvc.getInstance().retrieveAllApplicationEntitiesForModel(idUsa);
        } else {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        if (result != null) {
            int gridSize = result.size();
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (Map.Entry<String, List<ElementBean>> entry : result.entrySet()) {
                List<ElementBean> eas = entry.getValue();
                for (ElementBean ea : eas) {
                    JSONObject obj = new JSONObject();
                    obj.put("eaId", ea.getId());
                    obj.put("eaLib", ea.getLib());
                    obj.put("projectId", ea.getProject().getId());
                    obj.put("projectLib", ea.getProject().getLib());
                    l.add(obj);
                }
            }
            root.put("totalCount", gridSize);
            root.put("elements", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(root, retour);
        return root;
    }
}
