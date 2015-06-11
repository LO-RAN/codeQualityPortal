package com.compuware.caqs.presentation.common.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;

public class SearchByNamePanelDatasAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        HttpSession session = request.getSession();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);

        String filterDesc = RequestHelper.retrieve(request, session, "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
        String filterTypeElt = RequestHelper.retrieve(request, session, "typeElt", ElementType.ALL);
        retour.put("filter", filterDesc);
        retour.put("typeElt", filterTypeElt);

        List<ElementType> elementTypes = (List<ElementType>) session.getAttribute(Constants.CAQS_SUB_ELEMENT_TYPES_SESSION_KEY);
        if (elementTypes == null) {
            elementTypes = new ArrayList<ElementType>();
        }
        JSONArray ets = new JSONArray();
        JSONObject all = new JSONObject();
        all.put("id", "ALL");
        all.put("lib", resources.getMessage(loc, "caqs.select.all"));
        ets.add(all);
        for (ElementType et : elementTypes) {
            JSONObject obj = new JSONObject();
            obj.put("id", et.getId());
            obj.put("lib", et.getLib(loc));
            ets.add(obj);
        }
        retour.put("elementTypes", ets);
        return retour;
    }
}
