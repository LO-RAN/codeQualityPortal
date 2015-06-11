package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.DialecteSvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveLangagesListAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();

        List<LanguageBean> collection = DialecteSvc.getInstance().retrieveLanguages();

        List<JSONObject> liste = new ArrayList<JSONObject>();
        JSONObject obj = null;

        for (LanguageBean language : collection) {
            obj = new JSONObject();
            obj.put("id", language.getId());
            obj.put("lib", language.getLib());
            liste.add(obj);
        }

        this.putArrayIntoObject(JSONArray.fromObject(liste.toArray(new JSONObject[0])), retour);

        return retour;
    }
}
