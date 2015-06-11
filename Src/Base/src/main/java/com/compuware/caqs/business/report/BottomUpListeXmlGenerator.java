package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.business.consult.BottomUp;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.util.Locale;

public class BottomUpListeXmlGenerator extends XmlGenerator {

	List<BottomUpSummaryBean> criterionSummary;
	
    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public BottomUpListeXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
		super(eltBean, noteSeuil, loc);
	}

	public void retrieveData() {
    	BottomUp bottomUp = new BottomUp();
		criterionSummary = bottomUp.retrieveCriterionBottomUpSummary(eltBean, new FilterBean(JdbcDAOUtils.DATABASE_STRING_NOFILTER, ElementType.ALL));
	}

	public void generate(File destination) throws IOException {
        PrintWriter out = createWriter(destination, ReportConstants.BOTTOMUP_LIST_XML_FILE_NAME + ReportConstants.XML_FILE_EXT);
        printReportHeader(out);
        generateBottomUpList(out);
        printReportFooter(out);
        out.flush();
        out.close();
	}

	private void generateBottomUpList(PrintWriter out) {
		Iterator<BottomUpSummaryBean> i = criterionSummary.iterator();
		while (i.hasNext()) {
			BottomUpSummaryBean summaryBean = i.next();
			generateBottomUp(summaryBean, out);
		}
	}
	
	private void generateBottomUp(BottomUpSummaryBean summaryBean, PrintWriter out) {
        out.print("<ELEMENT");
        out.print(" lib=\""+summaryBean.getElement().getLib()+"\"");
        out.print(" desc=\""+getXmlCompliantString(summaryBean.getElement().getDesc())+"\"");
        out.print(" nbnote1=\""+this.intf.format(summaryBean.getNote(0))+"\"");
        out.print(" nbnote2=\""+this.intf.format(summaryBean.getNote(1))+"\"");
        out.print(" nbnote3=\""+this.intf.format(summaryBean.getNote(2))+"\"");
        out.print(" nbnote4=\""+this.intf.format(summaryBean.getNote(3))+"\"");
        out.println("/>");
	}
	
}
