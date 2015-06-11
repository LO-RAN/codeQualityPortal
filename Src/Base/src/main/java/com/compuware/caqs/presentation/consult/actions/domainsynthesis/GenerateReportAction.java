package com.compuware.caqs.presentation.consult.actions.domainsynthesis;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.report.ReportSvc;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class GenerateReportAction extends Action {

    @Override
    /**
     * @{@inheritDoc }
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        response.setContentType("application/excel");
        Locale locale = RequestUtil.getLocale(request);
        String domainId = request.getParameter("domainId");
        ElementBean eltBean = ElementSvc.getInstance().retrieveElementById(domainId);
        MessageResources resources = RequestUtil.getResources(request);
        response.setHeader("Content-Disposition", "attachment;filename=\""
                + resources.getMessage(locale, "caqs.domainsynthesis.synthesisFilename")
                + " " + eltBean.getLib() + ".xls\"");
        Workbook wb = ReportSvc.getInstance().generateDomainExcelSynthesis(domainId, RequestUtil.getConnectedUserId(request), locale, RequestUtil.getResources(request));
// write the workbook to the output stream
// close our file (don't blow out our file handles
        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.flush();

        return null;
    }
}
