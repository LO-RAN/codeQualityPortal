package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.business.consult.ModelDefinition;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.toolbox.util.resources.Internationalizable;
import java.util.Locale;

public class AnnexeTitleXmlGenerator extends XmlGenerator {

    /**
     *
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public AnnexeTitleXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
    }

    public void generate(File destination) throws IOException {
        generateDefinition(destination, ReportConstants.ANNEXE_TITLE_XML_FILE_NAME+ReportConstants.XML_FILE_EXT, "MODEL");
    }

    public void generateDefinition(File destination, String fileName, String tagName) throws IOException {
        PrintWriter out = createWriter(destination, fileName);
        printReportHeader(out);
        Internationalizable bean = (Internationalizable) eltBean.getUsage();
        generateDefinition(bean, tagName, out, true);
        printReportFooter(out);
        out.flush();
        out.close();
    }

    public void generateDefinition(Internationalizable bean, String tagName, PrintWriter out, boolean closeTag) {
        out.print("<" + tagName);
        out.print(" id=\"" + bean.getId() + "\"");
        out.print(" lib=\"" + getXmlCompliantString(bean.getLib(locale)) + "\"");
        out.print(" desc=\"" + getXmlCompliantString(bean.getDesc(locale)) +
                "\"");
        out.print(" compl=\"" +
                getXmlCompliantString(bean.getComplement(locale)) + "\"");
        if (closeTag) {
            out.print(" /");
        }
        out.println(">");
    }
}
