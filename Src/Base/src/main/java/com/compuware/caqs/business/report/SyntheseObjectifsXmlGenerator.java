package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.consult.SyntheseObjectifs;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionComparator;
import com.compuware.caqs.domain.dataschemas.CriterionComparatorFilterEnum;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import java.util.Locale;

public class SyntheseObjectifsXmlGenerator extends XmlGenerator {

	List<FactorBean> factorCollection;
	Map noteRepartition = new HashMap();
	
	public SyntheseObjectifsXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
		super(eltBean, noteSeuil, loc);
	}

    public void retrieveData() {
    	SyntheseObjectifs syntheseObjectifs = new SyntheseObjectifs();
        this.factorCollection = syntheseObjectifs.retrieveFactorsByElementAndBaseline(eltBean);
    	Collections.sort(this.factorCollection, new I18nDefinitionComparator(false, I18nDefinitionComparatorFilterEnum.LIB, locale));
        
        Iterator<FactorBean> i = this.factorCollection.iterator();
        while (i.hasNext()) {
            FactorBean factorBean = (FactorBean)i.next();
            List<CriterionBean> coll = syntheseObjectifs.retrieveCriterionSummary(eltBean, factorBean);
            Collections.sort(coll, new CriterionComparator<CriterionBean>("NONE", false, CriterionComparatorFilterEnum.NOTE_LIB_CRIT, locale));
            factorBean.setCriterions(coll);
            Map repartition = syntheseObjectifs.retrieveCriterionNoteRepartition(eltBean, factorBean);
            noteRepartition.put(factorBean.getId(), repartition);
        }
    }    

    public void generate(File destination) throws IOException {
        generateFactors(destination);
    }	

    private void generateFactors(File destination) throws IOException {
        PrintWriter out = createWriter(destination, "synthese-objectifs.xml");
        printReportHeader(out);
        Iterator<FactorBean> i = this.factorCollection.iterator();
        while (i.hasNext()) {
            FactorBean factorBean = i.next();
            generateFactor(factorBean, out);
        }
        printReportFooter(out);
        out.flush();
        out.close();
    }
    
    private void generateFactor(FactorBean factorBean, PrintWriter out) {
        out.print("<FACTOR");
        out.print(" id=\""+factorBean.getId()+"\"");
        out.print(" lib=\""+getXmlCompliantString(factorBean.getLib(locale))+"\"");
        out.print(" desc=\""+getXmlCompliantString(factorBean.getDesc(locale))+"\"");
        out.print(" note=\""+this.nf.format(factorBean.getNote()-0.005)+"\"");
        out.print(" comment=\""+getXmlCompliantString(factorBean.getComment())+"\"");
        boolean hasTendance = false;
        Iterator i = factorBean.getCriterions().iterator();
        while (i.hasNext() && !hasTendance) {
            CriterionBean crit = (CriterionBean)i.next();
            hasTendance = (crit.getTendance()!=0);
        }
        out.print(" hasTendance=\""+hasTendance+"\"");
        out.println(">");
        generateCriterionsSummary(factorBean.getId(), factorBean.getCriterions(), hasTendance, out);
        out.println("</FACTOR>");
    }
    
    private void generateCriterionsSummary(String idFact, Collection criterions, boolean hasTendance, PrintWriter out) {
        Iterator i = criterions.iterator();
        while (i.hasNext()) {
            CriterionBean crit = (CriterionBean)i.next();
            CriterionDefinition def = crit.getCriterionDefinition();
            out.print("<CRITERE");        
            out.print(" id=\""+def.getId()+"\"");
            out.print(" lib=\""+getXmlCompliantString(def.getLib(locale))+"\"");
            out.print(" desc=\""+getXmlCompliantString(def.getDesc(locale))+"\"");
            out.print(" note=\""+nf.format(crit.getNoteOrJustNote())+"\"");
            out.print(" weight=\""+intf.format(crit.getWeight())+"\"");
            out.print(" hasTendance=\""+hasTendance+"\"");
            out.print(" tendance=\""+super.getTendanceLabel(crit.getTendance(), crit.getNoteOrJustNote())+"\"");
            out.print(" comment=\""+getXmlCompliantString(crit.getComment())+"\"");
            out.println(">");
            generateRepartition(idFact, def.getId(), out);
            out.println("</CRITERE>");
        }
    }
    
    private void generateRepartition(String idFact, String idCrit, PrintWriter out) {
    	Map repartition = (Map)noteRepartition.get(idFact);
    	if (repartition != null) {
	    	CriterionNoteRepartition critNoteRepartition = (CriterionNoteRepartition)repartition.get(idCrit);
	    	if (critNoteRepartition != null) {
		    	for (int i = 0; i < 4; i++) {
		    		double pct = critNoteRepartition.getPct(i);
		    		double value = critNoteRepartition.getValue(i);
		    		out.print("<REPARTITION");
		    		out.print(" id=\""+i+"\"");
		    		out.print(" pct=\""+pctf.format(pct)+"\"");
		    		out.print(" value=\""+intf.format(value)+"\"");
		    		out.println(" />");
		    	}
	    	} else {
		    	for (int i = 0; i < 4; i++) {
		    		out.print("<REPARTITION");
		    		out.print(" id=\"4\"");
		    		out.print(" pct=\"0\"");
		    		out.print(" value=\"0\"");
		    		out.println(" />");
		    	}	    		
	    	}
    	}
    }
    
}
