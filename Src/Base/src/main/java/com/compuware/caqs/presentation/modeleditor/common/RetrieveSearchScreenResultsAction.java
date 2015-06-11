package com.compuware.caqs.presentation.modeleditor.common;

import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class RetrieveSearchScreenResultsAction extends ExtJSGridAjaxAction {

    protected void sortBy(List liste, Locale loc, HttpServletRequest request) {
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            Comparator comparator = new I18nDefinitionComparator(desc,
                    I18nDefinitionComparatorFilterEnum.fromString(sort), loc);
            this.sort(liste, comparator);
        }
    }

    protected String retrieveSearchParameter(String paramId, boolean like, HttpServletRequest request) {
        String retour = "%";
        String param = request.getParameter(paramId);
        if (param != null && !"".equals(param)) {
            if (like) {
                retour = "%" + param + "%";
            } else {
                retour = param;
            }
        }
        return retour;
    }
}
