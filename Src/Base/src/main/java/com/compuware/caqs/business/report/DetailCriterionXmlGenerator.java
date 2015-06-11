package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.consult.CriterionDetail;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionComparator;
import com.compuware.caqs.domain.dataschemas.CriterionComparatorFilterEnum;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparator;
import com.compuware.caqs.domain.dataschemas.comparators.I18nDefinitionComparatorFilterEnum;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import java.util.Locale;

public class DetailCriterionXmlGenerator extends XmlGenerator {
	
	private String[][] moduleArray = null; 

	List<CriterionDefinition> criterionDefinitionList = null;
    
    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param moduleArray
     * @param loc
     */
    public DetailCriterionXmlGenerator(ElementBean eltBean, double noteSeuil, String[][] moduleArray, Locale loc) {
		super(eltBean, noteSeuil, loc);
		this.moduleArray = moduleArray;
	}

    public void retrieveData() {
    	CriterionDetail detail = new CriterionDetail();
    	criterionDefinitionList = detail.retrieveCriterionDefinitions(this.eltBean.getUsage());
    	Collections.sort(criterionDefinitionList, new I18nDefinitionComparator(false, I18nDefinitionComparatorFilterEnum.LIB, locale));
    }    

    public void generate(File destination) throws IOException {
        generateCriterions(destination);
    }	

    private void generateCriterions(File destination) throws IOException {
    	if (moduleArray != null && moduleArray.length > 0) {
    		for (int moduleArrayIdx = 0; moduleArrayIdx < moduleArray.length; moduleArrayIdx++) {
    	        PrintWriter out = createWriter(destination, "criterion-details"+moduleArrayIdx+".xml");
    	        printReportHeader(out);
                out.print(" <MODULE name=\""+moduleArray[moduleArrayIdx][1]+"\" />");
    	        Iterator<CriterionDefinition> i = this.criterionDefinitionList.iterator();
    	        while (i.hasNext()) {
    	            CriterionDefinition criterionDef = i.next();
    	            generateCriterion(criterionDef, moduleArray[moduleArrayIdx][0], out);
    	        }
    	        printReportFooter(out);
    	        out.flush();
    	        out.close();
    		}
    	}
    }
    
    private void generateCriterion(CriterionDefinition criterionDef, String filter, PrintWriter out) {
        List<CriterionBean> coll = null;
    	CriterionDetail criterionDetail = new CriterionDetail();
        if (criterionDef.getIdTElt() != null && !criterionDef.getIdTElt().equalsIgnoreCase(ElementType.EA)) {
            coll = criterionDetail.retrieveCriterionDetailsForSubElts(this.eltBean, criterionDef, this.noteSeuil, filter, criterionDef.getIdTElt());
		}
        else {
            coll = criterionDetail.retrieveCriterionDetailsForElts(this.eltBean, criterionDef, this.noteSeuil, filter, criterionDef.getIdTElt());
        }
		this.purgeList(coll);
        if (coll != null && coll.size() > 0) {
            out.print("    <CRIT_DETAIL");
            out.print(" id=\""+criterionDef.getId()+"\"");
            out.print(" lib=\""+getXmlCompliantString(criterionDef.getLib(locale))+"\"");
            out.print(" desc=\""+getXmlCompliantString(criterionDef.getDesc(locale))+"\"");
            out.print(" compl=\""+getXmlCompliantString(criterionDef.getComplement(locale))+"\"");
            out.println(">");
            generateMetriquesDefinition(criterionDef.getMetriquesDefinitions(), out);
            generateCriterionDetail(coll, out);
            out.println("    </CRIT_DETAIL>");
        }
    }
    
    private void generateMetriquesDefinition(List<MetriqueDefinitionBean> metriques, PrintWriter out) {
        if (metriques != null && metriques.size() > 0) {
            out.println("        <METRIQUES>");
            Iterator<MetriqueDefinitionBean> i = metriques.iterator();
            while (i.hasNext()) {
            	MetriqueDefinitionBean met = i.next();
                out.print("            <MET");        
                out.print(" id=\""+met.getId()+"\"");
                out.print(" lib=\""+getXmlCompliantString(met.getLib(locale))+"\"");
                out.print(" desc=\""+getXmlCompliantString(met.getDesc(locale))+"\"");
                out.println(" />");
            }
            out.println("        </METRIQUES>");
        }
    }
    
    private void generateCriterionDetail(List<CriterionBean> criterions, PrintWriter out) {
        if (criterions != null && criterions.size() > 0) {
            Iterator<CriterionBean> i = criterions.iterator();
            while (i.hasNext()) {
                CriterionBean crit = i.next();
                ElementBean elt = crit.getElement();
                out.print("        <ELT");
                out.print(" id=\""+elt.getId()+"\"");
                out.print(" lib=\""+getXmlCompliantString(elt.getLib())+"\"");
                out.print(" desc=\""+getXmlCompliantString(elt.getDesc())+"\"");
                out.print(" note=\""+nf.format(crit.getNoteOrJustNote())+"\"");
                out.println(">");
                generateMetriques(crit, out);
                out.println("        </ELT>");
            }
        }
    }
    
    private void generateMetriques(CriterionBean criterion, PrintWriter out) {
        CriterionDefinition def = criterion.getCriterionDefinition();
        List<MetriqueDefinitionBean> metdefs = def.getMetriquesDefinitions();
        Map metriques = criterion.getMetriques();
        if (metdefs != null && metdefs.size() > 0 && metriques != null) {
            Iterator<MetriqueDefinitionBean> i = metdefs.iterator();
            while (i.hasNext()) {
            	MetriqueDefinitionBean md = (MetriqueDefinitionBean)i.next();
                MetriqueBean mb = (MetriqueBean)metriques.get(md.getId());
                out.print("            <MET");        
                out.print(" id=\""+md.getId()+"\"");
                if (mb != null) {
                    out.print(" value=\""+this.intf.format(mb.getValbrute())+"\"");
                    String lines = mb.getLinesAsString(',', Constants.MAX_LINES_SIZE);
                    if (lines != null && lines.length() > 0) {
                    	out.print(" lines=\"("+lines+")\"");
                    }
                	else {
                    	out.print(" lines=\"\"");
                	}
                }
                else {
                    out.print(" value=\" - \"");
                }
                out.println(" />");
            }
        }
    }
    
    private void purgeList(List<CriterionBean> criterionList) {
        Collections.sort(criterionList, new CriterionComparator<CriterionBean>("NONE", false, CriterionComparatorFilterEnum.DESC_ELT, locale));
        Iterator<CriterionBean> i = criterionList.iterator();
        String lastIdCrit = "";
        while (i.hasNext()) {
            CriterionBean crit = i.next();
            ElementBean elt = crit.getElement();
            if (elt.getId().equals(lastIdCrit)) {
                i.remove();
            }
            if(crit.getNoteOrJustNote() >= 3.0) {
            	i.remove();
            }
            lastIdCrit = elt.getId();
        }
        Collections.sort(criterionList, new CriterionComparator<CriterionBean>("NONE", false, CriterionComparatorFilterEnum.NOTE_DESC_ELT_REPORT, locale));
    }    
}
