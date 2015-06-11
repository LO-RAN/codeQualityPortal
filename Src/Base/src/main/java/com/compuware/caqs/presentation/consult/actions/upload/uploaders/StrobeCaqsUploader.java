/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.toolbox.util.logging.LoggerManager;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author cwfr-dzysman
 */
public class StrobeCaqsUploader extends AbstractCaqsUploader {

    private double totalTime;

    /**
     * le logger
     */
    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger("UploadDynamique");

    /**
     * jdom : element racine
     */
    private Element rootElement;

    public StrobeCaqsUploader(ElementBean e) {
        super(e);
    }

    public StrobeCaqsUploader() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatePolicy getUpdatePolicy() {
        return UpdatePolicy.MAX;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean isCorrectExport(File f) {
        boolean retour = false;
        if (f != null && f.exists()) {
            SAXBuilder saxbuilder = new SAXBuilder();
            try {
                Document doc = saxbuilder.build(f.getAbsolutePath());
                Element root = doc.getRootElement();
                if ("profile".equals(root.getName())) {
                    Element child = root.getChild("istrobe_parms");
                    if (child != null) {
                        retour = true;
                        rootElement = root;
                    }
                }
            }
            catch (org.jdom.JDOMException e) {
                mLog.debug(f.getAbsolutePath() + " is not an iStrobe export", e);
            }
            catch (IOException e) {
                mLog.error(f.getAbsolutePath() + " : error while opening it", e);
            }
        }
        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean extractMetrics() {
        if (this.rootElement != null && ea != null) {
            this.importApplicationMetrics();
            this.importProgramsMetrics();
        }
        return !this.getMetrics().isEmpty();
    }

    private void importApplicationMetrics() {
        Element msd = this.rootElement.getChild("msd");
        if (msd != null) {
            Element measStat = msd.getChild("meas_stats");
            double totalCpuPct = getDoubleValue(measStat, "cps_pct");
            this.addMetric(this.ea.getId(), "STROBE_TOTAL_CPU_PCT", null, ElementType.EA, totalCpuPct);

            double totalWaitPct = getDoubleValue(measStat, "wait_pct");
            this.addMetric(this.ea.getId(), "STROBE_TOTAL_WAIT_PCT", null, ElementType.EA, totalWaitPct);

            this.totalTime = getDoubleValue(measStat, "session_len_tm");
            this.addMetric(this.ea.getId(), "STROBE_TOTAL_SESSION_TIME", null, ElementType.EA, this.totalTime);

            double totalCpuLengthTime = getDoubleValue(measStat, "cpu_tm");
            this.addMetric(this.ea.getId(), "STROBE_TOTAL_CPU_TIME", null, ElementType.EA, totalCpuLengthTime);

            double totalWaitLengthTime = getDoubleValue(measStat, "wait_tm");
            this.addMetric(this.ea.getId(), "STROBE_TOTAL_WAIT_TIME", null, ElementType.EA, totalWaitLengthTime);

            Element excp = measStat.getChild("excps");
            if (excp != null) {
                String sNbIO = excp.getAttributeValue("cnt");
                int nbIO = Integer.parseInt(sNbIO.trim());
                this.addMetric(this.ea.getId(), "STROBE_TOTAL_NB_IO", null, ElementType.EA, nbIO);
            }
        }
    }

    private void importProgramsMetrics() {
        Element pup = this.rootElement.getChild("pup");
        if (pup != null) {
            List children = pup.getChildren("pup_pseudo_module");
            Element pupPseudoModulesUser = null;
            if (children != null) {
                for (Iterator it = children.iterator(); it.hasNext();) {
                    Element elt = (Element) it.next();
                    if (".USER".equals(elt.getAttributeValue("name").trim())) {
                        pupPseudoModulesUser = elt;
                        break;
                    }
                }
            }
            if (pupPseudoModulesUser != null) {
                children = pupPseudoModulesUser.getChildren("pup_module");
                for (Iterator it = children.iterator(); it.hasNext();) {
                    Element elt = (Element) it.next();
                    String programName = elt.getAttributeValue("name");

                    extractPctAndTimeMetrics(elt, programName, "total_cpu_pct", "TOTAL");
                    extractPctAndTimeMetrics(elt, programName, "attributed_cpu_pct", "ATTRIBUTED");
                    extractPctAndTimeMetrics(elt, programName, "combined_cpu_pct", "COMBINED");

                    extractCpuTimeDispatching(elt, programName);
                }
            }
        }
    }

    /**
     * Extract percent and time metrics from the given element.
     * @param elt the xml element.
     * @param programName the program name.
     * @param attrName the name of the percent attribute of the element.
     * @param metricBaseName the base of the metric prefix is STROBE_PROGRAM_ and suffix _CPU_USAGE_PCT or _CPU_USAGE_TIME.
     */
    private void extractPctAndTimeMetrics(Element elt, String programName, String attrName, String metricBaseName) {
        double pct = getDoubleValue(elt, attrName);
        this.addMetric("STROBE_PROGRAM_" + metricBaseName + "_CPU_USAGE_PCT", programName.trim(), ElementType.PROGRAM, pct, true);
        double pctTime = (pct / 100.0) * this.totalTime;
        this.addMetric("STROBE_PROGRAM_" + metricBaseName + "_CPU_USAGE_TIME", programName.trim(), ElementType.PROGRAM, pctTime, true);
    }

    /**
     * Get the double value associated to the given attribute for the given xml element.
     * @param elt the xml element.
     * @param attrName the attribute name.
     * @return the double value of the given attribute.
     */
    private double getDoubleValue(Element elt, String attrName) {
        double result = 0.0;
        String sPct = elt.getAttributeValue(attrName);
        result = Double.parseDouble(sPct.trim());
        return result;
    }

    private void extractCpuTimeDispatching(Element elt, String programName) {
        List children = elt.getChildren("pup_csect");
        Element currentElement = null;
        Element programSecElement = null;
        for (Iterator it = children.iterator(); it.hasNext() && (programSecElement == null);) {
            currentElement = (Element)it.next();
            String secName = currentElement.getAttributeValue("name");
            if (secName != null && programName.equalsIgnoreCase(secName)) {
                programSecElement = currentElement;
            }
        }
        if (programSecElement != null) {
            List blocks = programSecElement.getChildren("unidxd_code");
            double maxTotalCpuPct = 0.0;
            for (Iterator it = blocks.iterator(); it.hasNext();) {
                currentElement = (Element)it.next();
                maxTotalCpuPct = Math.max(maxTotalCpuPct, getDoubleValue(currentElement, "total_cpu_pct"));
            }
            this.addMetric("STROBE_PROGRAM_MAX_SEC_CPU_USAGE_PCT", programName.trim(), ElementType.PROGRAM, maxTotalCpuPct, true);
        }
    }

}
