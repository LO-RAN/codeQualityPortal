package com.compuware.caqs.presentation.consult.actions.evolution;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.consult.actions.ElementSelectedActionAbstract;
import com.compuware.caqs.presentation.util.EvolutionsUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class EvolutionRepartitionChartSelectAction extends ElementSelectedActionAbstract {

    // --------------------------------------------------------- Public Methods
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // Extract attributes we will need

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        retrieveRepartition(mapping, request, session, errors, response);

        return mapping.findForward("success");

    }
    public static final String NEW_BAD_ATTR = "newBad";
    public static final String OLD_WORST_ATTR = "oldWorst";
    public static final String OLD_BETTER_ATTR = "oldBetter";
    public static final String OLD_BETTER_WORST_ATTR = "oldBetterWorst";
    public static final String STABLE_ATTR = "Stable";
    public static final String BAD_STABLE_ATTR = "badStable";

    protected void retrieveRepartition(ActionMapping mapping,
            HttpServletRequest request,
            HttpSession session,
            ActionErrors errors,
            HttpServletResponse response) throws IOException {
        String idElt = request.getParameter("idElt");
        String idBline = request.getParameter("idBline");
        String idPreviousBline = request.getParameter("idPreviousBline");
        int target = RequestUtil.getIntParam(request, "target", -1);
        if (target != -1) {
            ElementsCategory category = ElementsCategory.fromCode(target);
            String filterDesc = RequestHelper.retrieve(request, session, "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
            String filterTypeElt = RequestHelper.retrieve(request, session, "typeElt", ElementType.ALL);
            FilterBean filter = new FilterBean(filterDesc, filterTypeElt);

            String fileName = EvolutionsUtils.generatePieChart(idElt, idBline, idPreviousBline, category, filter, request);
            request.setAttribute("piechartFileName", fileName);
            request.setAttribute("piechartImageMap", EvolutionsUtils.createImageMap(fileName));
            request.setAttribute("target", request.getParameter("target"));
            request.setAttribute("idElt", idElt);
            request.setAttribute("idBline", idBline);
            request.setAttribute("idPreviousBline", idPreviousBline);
        }

    }
}
