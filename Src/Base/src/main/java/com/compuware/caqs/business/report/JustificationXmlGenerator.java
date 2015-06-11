package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.JustificationSvc;
import java.util.Locale;

public class JustificationXmlGenerator extends XmlGenerator {

	private Collection<JustificatifResume> criterionValidated = null;
	private Collection<JustificatifResume> criterionWaiting = null;
	private Collection<JustificatifResume> criterionRefused = null;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public JustificationXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
		super(eltBean, noteSeuil, loc);
	}

	public void retrieveData() {
		JustificationSvc justificationSvc = JustificationSvc.getInstance();
		try {
			this.criterionValidated =  justificationSvc.getAllCriterionJustificationsForElement(
					"VALID", 
					eltBean.getId(), 
					eltBean.getBaseline().getId());
			this.criterionWaiting =  justificationSvc.getAllCriterionJustificationsForElement(
					"DEMAND", 
					eltBean.getId(), 
					eltBean.getBaseline().getId());
			this.criterionRefused =  justificationSvc.getAllCriterionJustificationsForElement(
					"REJET", 
					eltBean.getId(), 
					eltBean.getBaseline().getId());
		} catch(CaqsException e) {
			logger.error("Report generation : Error while retrieving justifications data");
		}
	}    

	public void generate(File destination) throws IOException {
		PrintWriter out = createWriter(destination, "justifications.xml");
		printReportHeader(out);
		
		int nbJust = this.criterionValidated.size() + this.criterionRefused.size() + this.criterionWaiting.size();
		
		out.print("<JUSTIFICATIONS ");
		out.print(" nb=\""+this.intf.format(nbJust)+"\" ");
		out.print(" nbCV=\""+this.intf.format(this.criterionValidated.size())+"\" ");
		out.print(" nbCR=\""+this.intf.format(this.criterionRefused.size())+"\" ");
		out.print(" nbCW=\""+this.intf.format(this.criterionWaiting.size())+"\" ");
		out.println(">");		
		out.println("</JUSTIFICATIONS>");

		if(!this.criterionValidated.isEmpty()) {
			this.generateJustifications(this.criterionValidated, "cv", out);
		}
		
		if(!this.criterionRefused.isEmpty()) {
			this.generateJustifications(this.criterionRefused, "cr", out);
		}
		
		if(!this.criterionWaiting.isEmpty()) {
			this.generateJustifications(this.criterionWaiting, "cw", out);
		}

		printReportFooter(out);
		out.flush();
		out.close();
	}	

	private void generateJustifications(Collection<JustificatifResume> coll,
			String type, PrintWriter out) throws IOException {		
		out.println("<JUSTIFICATIONS_LIST type=\""+type+"\" >");
		for(JustificatifResume just : coll) {
			out.print("  <JUST ");
			out.print(" author=\""+getXmlCompliantString(just.getJustificatifAuteur())+"\" ");
			out.print(" justDesc=\""+getXmlCompliantString(just.getJustificatifDesc())+"\" ");
			out.print(" critFacLib=\""+getXmlCompliantString(just.getCritfacLabel(this.locale))+"\" ");
			out.print(" eltLib=\""+getXmlCompliantString(just.getElementLabel())+"\" ");
			out.print(" eltType=\""+getXmlCompliantString(just.getElementType(this.locale))+"\" ");
			out.print(" oldMark=\""+this.nf.format(just.getOldMark())+"\" ");
			out.print(" mark=\""+this.nf.format(just.getNoteJustificatif())+"\" ");
			out.println(">");
			out.println("  </JUST>");
		}
		out.println("</JUSTIFICATIONS_LIST>");
	}

}
