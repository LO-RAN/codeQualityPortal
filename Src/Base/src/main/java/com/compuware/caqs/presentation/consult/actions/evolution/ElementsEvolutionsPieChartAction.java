package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.EvolutionsUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.io.IOException;

public class ElementsEvolutionsPieChartAction extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        String idElt = request.getParameter("idElt");
        String idBline = request.getParameter("idBline");
        String idPreviousBline = request.getParameter("idPreviousBline");
        MessagesCodes mc = MessagesCodes.CAQS_GENERIC_ERROR;
        int target = RequestUtil.getIntParam(request, "target", -1);
        if (target != -1) {
            ElementsCategory category = ElementsCategory.fromCode(target);
            if (category != null) {
                String filterDesc = RequestHelper.retrieve(request, request.getSession(), "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
                request.getSession().setAttribute("filter", filterDesc);
                String filterTypeElt = request.getParameter("typeElt");
                if (filterTypeElt == null) {
                    filterTypeElt = ElementType.ALL;
                }
                FilterBean filter = new FilterBean(filterDesc, filterTypeElt);
                this.createPieChart(idElt, idBline, idPreviousBline, category, filter, request, root);
                mc = MessagesCodes.NO_ERROR;
            }
        }
        this.fillJSONObjectWithReturnCode(root, mc);
        return root;
    }

    private void createPieChart(String idElt, String idBline, String idPreviousBline,
            ElementsCategory category, FilterBean filter,
            HttpServletRequest request, JSONObject root) {
        try {
            String fileName = EvolutionsUtils.generatePieChart(idElt, idBline, idPreviousBline, category, filter, request);
            root.put("piechartFileName", fileName);
            root.put("piechartImageMap", EvolutionsUtils.createImageMap(fileName));
        } catch (IOException exc) {
            mLog.error(exc.getMessage());
        }
    }
}
