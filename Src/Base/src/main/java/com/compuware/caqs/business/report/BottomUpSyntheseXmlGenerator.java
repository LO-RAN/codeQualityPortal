package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.business.consult.BottomUp;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.RepartitionBean;
import com.compuware.caqs.domain.dataschemas.SyntheseCorrectionBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.util.Locale;

public class BottomUpSyntheseXmlGenerator extends XmlGenerator {

	private static final String FACTOR_TAG = "FACTOR";
	private static final String CRITERION_TAG = "CRITERION";
	
	SyntheseCorrectionBean syntheseCorr;
	Collection repartitionByCriterion;
	Collection repartitionByfactor;
	
	public BottomUpSyntheseXmlGenerator(ElementBean eltBean, double noteSeuil, Locale locale) {
		super(eltBean, noteSeuil, locale);
        this.syntheseCorr = new SyntheseCorrectionBean(locale);
	}

	public void retrieveData() {
    	BottomUp bottomUp = new BottomUp();
    	bottomUp.retrieveCorrectionSynthese(eltBean, new FilterBean(JdbcDAOUtils.DATABASE_STRING_NOFILTER, ElementType.ALL), syntheseCorr);
		repartitionByCriterion = bottomUp.retrieveRepartitionByCriterion(eltBean, JdbcDAOUtils.DATABASE_STRING_NOFILTER, JdbcDAOUtils.DATABASE_STRING_NOFILTER);
		repartitionByfactor = bottomUp.retrieveRepartitionByFactor(eltBean, JdbcDAOUtils.DATABASE_STRING_NOFILTER, JdbcDAOUtils.DATABASE_STRING_NOFILTER);
	}

	public void generate(File destination) throws IOException {
        PrintWriter out = createWriter(destination, ReportConstants.SYNTHESE_BOTTOMUP_XML_FILE_NAME + ReportConstants.XML_FILE_EXT);
        printReportHeader(out);
        generateVolumetrie(out);
        generateRepartition(repartitionByCriterion, BottomUpSyntheseXmlGenerator.CRITERION_TAG, out);
        generateRepartition(repartitionByfactor, BottomUpSyntheseXmlGenerator.FACTOR_TAG, out);
        printReportFooter(out);
        out.flush();
        out.close();
	}

	private void generateVolumetrie(PrintWriter out) {
        out.println("<VOLUMETRIE>");
        out.print("<NBELEMENT");
        out.print(" accept=\""+this.intf.format(syntheseCorr.getNbEltsAccepte())+"\"");
        out.print(" reserve=\""+this.intf.format(syntheseCorr.getNbEltsReserve())+"\"");
        out.print(" rejet=\""+this.intf.format(syntheseCorr.getNbEltsRejets())+"\"");
        out.print(" pctamelior=\""+syntheseCorr.getPctEltsCorr()+"\"");
        out.println("/>");
        out.print("<NBAMELIOR");
        out.print(" nblevel1=\""+this.intf.format(syntheseCorr.getNbCorrRejets())+"\"");
        out.print(" pctlevel1=\""+syntheseCorr.getPctCorrRejets()+"\"");
        out.print(" nblevel2=\""+this.intf.format(syntheseCorr.getNbCorrReserve())+"\"");
        out.print(" pctlevel2=\""+syntheseCorr.getPctCorrReserve()+"\"");
        out.print(" nbamelior=\""+this.intf.format(syntheseCorr.getNbCorrTotal())+"\"");
        out.println("/>");
        out.println("</VOLUMETRIE>");
	}
	
	private void generateRepartition(Collection rep, String tagName, PrintWriter out) {
        out.println("<" + tagName + "REPARTITION>");
        Iterator i = rep.iterator();
        while (i.hasNext()) {
        	RepartitionBean bean = (RepartitionBean)i.next();
        	generateRepartition(bean, tagName, out);
        }
        out.println("</" + tagName + "REPARTITION>");
	}
	
	private void generateRepartition(RepartitionBean bean, String tagName, PrintWriter out) {
        out.print("<"+tagName);
        out.print(" lib=\""+getXmlCompliantString(bean.getLib(locale))+"\"");
        out.print(" nb=\""+bean.getNb()+"\"");
        out.println("/>");
	}
	
}
