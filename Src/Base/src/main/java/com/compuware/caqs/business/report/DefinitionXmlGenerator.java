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

public class DefinitionXmlGenerator extends XmlGenerator {

    List<FactorBean> factorList;
    List<CriterionDefinition> criterionList;
    List<MetriqueDefinitionBean> metriqueList;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public DefinitionXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        ModelDefinition model = new ModelDefinition();
        factorList = model.retrieveFactorDefinition(eltBean);
        Collections.sort(factorList, new I18nDefinitionComparator(false,
                I18nDefinitionComparatorFilterEnum.LIB, locale));

        if (ElementType.EA.equalsIgnoreCase(eltBean.getTypeElt())) {
            criterionList = model.retrieveCriterionDefinition(eltBean);
            Collections.sort(criterionList, new I18nDefinitionComparator(false, I18nDefinitionComparatorFilterEnum.LIB, locale));
            metriqueList = model.retrieveMetriquesDefinition(eltBean);
            Collections.sort(metriqueList, new I18nDefinitionComparator(false, I18nDefinitionComparatorFilterEnum.LIB, locale));
        }
    }

    public void generate(File destination) throws IOException {
        generateDefinition(destination, factorList, "factor-definition.xml", "FACTOR");
        if (ElementType.EA.equalsIgnoreCase(eltBean.getTypeElt())) {
            generateCriterionDefinition(destination, criterionList, ReportConstants.CRITERION_DEFINITION_XML_FILE_NAME+".xml", "CRITERION");
            generateDefinition(destination, metriqueList, ReportConstants.METRIC_DEFINITION_XML_FILE_NAME+".xml", "METRIC");
        }
    }

    public void generateDefinition(File destination, Collection definitionCollection, String fileName, String tagName) throws IOException {
        PrintWriter out = createWriter(destination, fileName);
        printReportHeader(out);
        Iterator i = definitionCollection.iterator();
        while (i.hasNext()) {
            Internationalizable bean = (Internationalizable) i.next();
            generateDefinition(bean, tagName, out, true);
        }
        printReportFooter(out);
        out.flush();
        out.close();
    }

    public void generateDefinition(Internationalizable bean, String tagName, PrintWriter out, boolean closeTag) {
        out.print("<" + tagName);
        out.print(" id=\"" + bean.getId() + "\"");
        out.print(" lib=\"" + getXmlCompliantString(bean.getLib(locale)) + "\"");
        out.print(" desc=\"" + getXmlCompliantString(bean.getDesc(locale))
                + "\"");
        out.print(" compl=\""
                + getXmlCompliantString(bean.getComplement(locale)) + "\"");
        if (closeTag) {
            out.print(" /");
        }
        out.println(">");
    }

    public void generateCriterionDefinition(File destination, List<CriterionDefinition> definitionCollection, String fileName, String tagName) throws IOException {
        String usageId = this.eltBean.getUsage().getId();
        UsageCalculator calculator = new UsageCalculator();
        calculator.init("method-" + usageId.toLowerCase() + ".xml");
        PrintWriter out = createWriter(destination, fileName);
        printReportHeader(out);
        Iterator<CriterionDefinition> i = definitionCollection.iterator();
        while (i.hasNext()) {
            Internationalizable bean = (Internationalizable) i.next();
            generateDefinition(bean, tagName, out, false);
            out.print(calculator.getCriterionDefintionAsString(usageId, bean.getId()));
            out.println("</" + tagName + ">");
        }
        printReportFooter(out);
        out.flush();
        out.close();
    }
}
