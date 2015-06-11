package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import java.util.Locale;

public class SyntheseXmlGenerator extends XmlGenerator {

    List<FactorBean> factorSyntheseInfos;
    Map<String, Double> syntheseVolumetrie;
    List<Volumetry> volumetry = null;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public SyntheseXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        Synthese synthese = new Synthese();
        factorSyntheseInfos = synthese.retrieveKiviat(this.eltBean);
        Collections.sort(factorSyntheseInfos, new I18nDefinitionComparator(false, I18nDefinitionComparatorFilterEnum.LIB, locale));
        syntheseVolumetrie = synthese.retrieveVolumetryMetrics(this.eltBean, this.eltBean.getBaseline().getId());
        volumetry = synthese.retrieveVolumetry(eltBean);
    }

    public void generate(File destination) throws IOException {
        PrintWriter out = createWriter(destination, ReportConstants.SYNTHESE_XML_FILE_NAME
                + ReportConstants.XML_FILE_EXT);
        printReportHeader(out);
        generateFactorSyntheseInfos(out);
        generateVolumetry(out);
        generateMetrics(out);
        printReportFooter(out);
        out.flush();
        out.close();
    }

    private void generateFactorSyntheseInfos(PrintWriter out) {
        boolean hasTendance = false;
        Iterator i = this.factorSyntheseInfos.iterator();
        while (i.hasNext() && !hasTendance) {
            FactorBean factorBean = (FactorBean) i.next();
            hasTendance = (factorBean.getTendance() != 0);
        }

        out.println("<FACTORSYNTHESE hasTendance=\"" + hasTendance + "\">");
        i = this.factorSyntheseInfos.iterator();
        while (i.hasNext()) {
            FactorBean factorBean = (FactorBean) i.next();
            generateFactor(factorBean, hasTendance, out);
        }
        out.println("</FACTORSYNTHESE>");
    }

    private void generateFactor(FactorBean factorBean, boolean hasTendance, PrintWriter out) {
        out.print("    <FACTOR");
        out.print(" id=\"" + factorBean.getId() + "\"");
        out.print(" lib=\"" + getXmlCompliantString(factorBean.getLib(locale))
                + "\"");
        out.print(" desc=\"" + getXmlCompliantString(factorBean.getDesc(locale))
                + "\"");
        out.print(" note=\"" + this.nf.format(factorBean.getNote() - 0.005)
                + "\"");
        out.print(" comment=\"" + getXmlCompliantString(factorBean.getComment())
                + "\"");
        out.print(" hasTendance=\"" + hasTendance + "\"");
        out.print(" tendance=\""
                + super.getTendanceLabel(factorBean.getTendance(), factorBean.getNote())
                + "\"");
        out.println(" />");
    }

    private void generateMetrics(PrintWriter out) {
        out.println("<METRICS>");
        Set<String> keySet = this.syntheseVolumetrie.keySet();
        Iterator<String> i = keySet.iterator();
        while (i.hasNext()) {
            String idMet = i.next();
            Double value = syntheseVolumetrie.get(idMet);
            generateVolumetrie(idMet, value, out);
        }
        out.println("</METRICS>");
    }

    private void generateVolumetrie(String idMet, Double value, PrintWriter out) {
        out.print("    <" + idMet);
        out.print(" note=\"" + this.intf.format(value.intValue()) + "\"");
        out.println(" />");
    }

    private void generateVolumetry(PrintWriter out) {
        if (this.volumetry != null) {
            Iterator<Volumetry> volumetryIter = this.volumetry.iterator();
            Volumetry currentVolumetry = null;
            ElementType currentVolumetryElementType = null;
            while (volumetryIter.hasNext()) {
                currentVolumetry = volumetryIter.next();
                currentVolumetryElementType = new ElementType(currentVolumetry.getIdTElt());
                out.print("        <VOLUMETRY");
                out.print(" type=\""
                        + currentVolumetryElementType.getLib(locale) + "\"");
                out.print(" total=\""
                        + this.intf.format(currentVolumetry.getTotal()) + "\"");
                out.print(" created=\""
                        + this.intf.format(currentVolumetry.getCreated()) + "\"");
                out.print(" deleted=\""
                        + this.intf.format(currentVolumetry.getDeleted()) + "\"");
                out.println(" />");
            }
        }
    }
}
