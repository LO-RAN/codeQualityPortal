package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.comparators.DialecteBeanComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.DialecteSvc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveLanguagesAssociatedDialectsAction extends ExtJSGridAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 5);
        JSONObject root = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String toolId = request.getParameter("languageId");
        List<DialecteBean> result = null;
        if (toolId != null) {
            result = DialecteSvc.getInstance().retrieveDialectsByLanguages(toolId);
        } else {
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        if (result != null) {
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equalsIgnoreCase(sens.toLowerCase());
                Collections.sort(result, new DialecteBeanComparator(sort, !asc));
            }

            int gridSize = result.size();
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (int i = startIndex; i < (startIndex + limitIndex) && i
                    < gridSize; i++) {
                DialecteBean elt = result.get(i);
                JSONObject obj = new JSONObject();
                obj.put("id", elt.getId());
                obj.put("lib", elt.getLib());
                obj.put("desc", elt.getDesc());
                l.add(obj);
            }
            root.put("totalCount", gridSize);
            root.put("dialects", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(root, retour);
        return root;
    }
}
