package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
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
public class SaveDialectInfosAction extends ManageInfosAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        DialecteBean dialect = this.dialecteBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = DialecteSvc.getInstance().saveDialecteBean(dialect);
        } else if ("delete".equals(action)) {
            retour = DialecteSvc.getInstance().deleteDialecteBean(dialect.getId());
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private DialecteBean dialecteBeanFromRequest(HttpServletRequest request) {
        DialecteBean retour = new DialecteBean();
        retour.setId(request.getParameter("id"));
        retour.setLib(request.getParameter("lib"));
        retour.setDesc(request.getParameter("desc"));
        String langageId = request.getParameter("idLangage");
        if(langageId != null) {
            LanguageBean l = new LanguageBean((langageId));
            retour.setLangage(l);
        }
        return retour;
    }
}
