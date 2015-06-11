package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.modelmanager.IFPUGFormulaForm;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import java.util.Locale;

public class IFPUGXmlGenerator extends XmlGenerator {

    private IFPUGFormulaForm ifpug = null;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public IFPUGXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        String idUsa = this.eltBean.getUsage().getId();
        CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(idUsa);
        if (manager != null) {
            this.ifpug = manager.getIFPUGFormula();
        }
    }

    public void generate(File destination) throws IOException {
        generateDefinition(destination, ReportConstants.IFPUG_DEFINITION_XML_FILE_NAME
                + ".xml");
    }

    public void generateDefinition(File destination, String fileName) throws IOException {
        PrintWriter out = createWriter(destination, fileName);
        printReportHeader(out);
        out.print("<IFPUG>");
        String ifpugFormula = (this.ifpug!=null) ? this.ifpug.getReadableFormula(false, null, this.locale): "N/A";
        out.print(ifpugFormula);
        out.print("</IFPUG>");
        printReportFooter(out);
        out.flush();
        out.close();
    }

}
