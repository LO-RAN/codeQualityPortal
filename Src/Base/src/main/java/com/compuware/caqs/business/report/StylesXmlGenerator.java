package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import java.util.Locale;

public class StylesXmlGenerator extends XmlGenerator {

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public StylesXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
		super(eltBean, noteSeuil, loc);
	}

	public void retrieveData() {
	}    

	public void generate(File destination) throws IOException {
		PrintWriter out = createWriter(destination, "styles.xml");
		printReportHeader(out);
		out.print("<STYLES01 />");
		out.print("<STYLES02 />");
		out.print("<STYLES03 />");
		out.print("<STYLES04 />");
		printReportFooter(out);
		out.flush();
		out.close();
	}
}
