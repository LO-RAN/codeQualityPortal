package com.compuware.caqs.presentation.modeleditor;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.PrintModelCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import java.beans.EventHandler;
import java.io.ByteArrayOutputStream;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveModelPrintableVersionAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        response.setContentType("application/pdf");

        String modelId = request.getParameter("modelId");
        MessageResources resources = RequestUtil.getResources(request);
        Locale locale = RequestUtil.getLocale(request);
        PrintModelCreator creator = new PrintModelCreator();
        ByteArrayOutputStream baosPDF = creator.createPrintableVersion(modelId, resources, request, locale);
        response.setContentLength(baosPDF.size());

        ServletOutputStream sos = response.getOutputStream();
        baosPDF.writeTo(sos);
        sos.flush();
        return null;
    }
}
