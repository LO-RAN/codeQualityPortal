package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
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
public class SaveLanguageInfosAction extends ExtJSAjaxAction  {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        LanguageBean lb = this.langueBeanFromRequest(request);
        String action = request.getParameter("action");
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        if ("save".equals(action)) {
            retour = DialecteSvc.getInstance().saveLanguageBean(lb);
        } else if ("delete".equals(action)) {
            retour = DialecteSvc.getInstance().deleteLanguageBean(lb.getId());
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }

    private LanguageBean langueBeanFromRequest(HttpServletRequest request) {
        LanguageBean retour = new LanguageBean(request.getParameter("id"));
        retour.setLib(request.getParameter("lib"));
        retour.setDesc(request.getParameter("desc"));
        return retour;
    }

}
