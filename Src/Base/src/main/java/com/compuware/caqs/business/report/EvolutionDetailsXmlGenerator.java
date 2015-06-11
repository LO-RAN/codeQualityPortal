package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.business.consult.ActionPlan;
import com.compuware.caqs.business.consult.Baseline;
import com.compuware.caqs.business.consult.Evolution;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import java.util.Locale;

public class EvolutionDetailsXmlGenerator extends XmlGenerator {

	private List<BottomUpDetailBean> newAndBadElements;
	private List<BottomUpDetailBean> oldAndWorstElements;
	private List<BottomUpDetailBean> oldAndBetterElements;
	private List<BottomUpDetailBean> oldBetterWorstElements;
	Collection stableElements;
	private List<BottomUpDetailBean> badAndStableElements;
	
	Collection repartitionNewAndBadElements;
	Collection repartitionOldAndWorstElements;
	Collection repartitionOldAndBetterElements;
	
	private ActionPlanElementBeanCollection<ActionPlanCriterionBean> corrected = null;
	private boolean				hasPreviousActionPlan;
	private ActionPlanElementBeanCollection<ActionPlanCriterionBean> partially = null;
	private ActionPlanElementBeanCollection<ActionPlanCriterionBean> stables = null;
	private ActionPlanElementBeanCollection<ActionPlanCriterionBean> deteriorated = null;
		
	private Collection<FactorBean> factorEvolution;

    /**
     *
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public EvolutionDetailsXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
		super(eltBean, noteSeuil, loc);
	}

	public void retrieveData() {
		FilterBean filter = new FilterBean();
		Evolution evolution = new Evolution();
		newAndBadElements = evolution.retrieveNewAndBadElementsWithCriterions(eltBean.getId(), eltBean.getBaseline().getId(), eltBean.getProject().getId());
		oldAndWorstElements = evolution.retrieveOldWorstElementsWithCriterions(eltBean.getId(), eltBean.getBaseline().getId(), eltBean.getProject().getId());
		oldAndBetterElements = evolution.retrieveOldBetterElementsWithCriterions(eltBean.getId(), eltBean.getBaseline().getId(), eltBean.getProject().getId());
		oldBetterWorstElements = evolution.retrieveOldBetterWorstElementsWithCriterions(eltBean.getId(), eltBean.getBaseline().getId(), eltBean.getProject().getId());
		stableElements = evolution.retrieveStableElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
		badAndStableElements = evolution.retrieveOldBadStableElementsWithCriterions(eltBean.getId(), eltBean.getBaseline().getId(), eltBean.getProject().getId());

		repartitionNewAndBadElements = evolution.retrieveRepartitionNewAndBadElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
		repartitionOldAndWorstElements = evolution.retrieveRepartitionOldAndWorstElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
		repartitionOldAndBetterElements = evolution.retrieveRepartitionOldAndBetterElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
		factorEvolution = evolution.retrieveFactorEvolution(eltBean, eltBean.getBaseline());
		
		Baseline baselinesvc = new Baseline();
		ActionPlan actionplansvc = new ActionPlan();
		
		BaselineBean bline = baselinesvc.getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());
		hasPreviousActionPlan = false;
		ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanPreviousBaseline = null;
		if(bline!=null) {
			ActionPlanBean ap = actionplansvc.getCompleteActionPlan(eltBean, bline.getId());
			actionPlanPreviousBaseline = ap.getCorrectedElements();
			hasPreviousActionPlan = !actionPlanPreviousBaseline.isEmpty();

			if(hasPreviousActionPlan) {
				ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlanCopy = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(actionPlanPreviousBaseline);
				ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanForBline = actionplansvc.getCompleteActionPlan(eltBean,
						eltBean.getBaseline().getId()).getElementsWithProblematicElement();

				//we start searching for corrected criterions
				corrected = evolution.getCorrectedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
				//we go on with those partially corrected
				partially = evolution.getPartiallyCorrectedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
				//stables ones
				stables = evolution.getStableCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
				//and the deteriorated ones
				deteriorated = evolution.getDeterioratedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
			}
		}
	}

	public void generate(File destination) throws IOException {
        PrintWriter out = createWriter(destination, ReportConstants.EVOLUTION_DETAILS_XML_FILE_NAME + ReportConstants.XML_FILE_EXT);
        printReportHeader(out);
		if (!evolutionDataIsEmpty()) {
	        generateEvolution(out);
		}
		this.generateActionPlanEvolution(out);
        printReportFooter(out);
        out.flush();
        out.close();
	}
	
	private void generateActionPlanEvolution(PrintWriter out) {
		StringBuffer sb = new StringBuffer();
		sb.append("<ACTIONPLAN ");
		sb.append(" exists=\"").append(""+hasPreviousActionPlan).append("\" ");
		
		int nbCorrected = 0;
		if(this.corrected!=null) {
			nbCorrected = this.corrected.size();
		}
		int nbPartially = 0;
		if(this.partially!=null) {
			nbPartially = this.partially.size();
		}
		int nbDegraded = 0;
		if(this.deteriorated!=null) {
			nbDegraded = this.deteriorated.size();
		}
		int nbStable = 0;
		if(this.stables!=null) {
			nbStable = this.stables.size();
		}
		
		sb.append(" corrected=\"").append(""+this.intf.format(nbCorrected)).append("\" ");
		sb.append(" partially=\"").append(""+this.intf.format(nbPartially)).append("\" ");
		sb.append(" degraded=\"").append(""+this.intf.format(nbDegraded)).append("\" ");
		sb.append(" stable=\"").append(""+this.intf.format(nbStable)).append("\" ");
		
		sb.append(">");
		sb.append("</ACTIONPLAN >");
		out.print(sb.toString());
	}

	private boolean evolutionDataIsEmpty() {
		return oldAndWorstElements.isEmpty() &&  oldAndBetterElements.isEmpty() && oldBetterWorstElements.isEmpty() && stableElements.isEmpty();
	}
	
	private void generateEvolution(PrintWriter out) {
		addGroupWithElements("NEWBAD", this.newAndBadElements, out);
		addGroupWithElements("OLDWORST", this.oldAndWorstElements, out);
		addGroupWithElements("OLDBETTER", this.oldAndBetterElements, out);
		addGroupWithElements("OLDBETTERWORST", this.oldBetterWorstElements, out);
        addGroup("STABLE", this.stableElements, out);
        addGroupWithElements("BADSTABLE", this.badAndStableElements, out);
        this.generateGoalEvolution(out);
	}
	
	private void generateGoalEvolution(PrintWriter out) {
		out.println("<GOAL_EVOLUTION>");
		for(FactorBean fb : this.factorEvolution) {
			double note = fb.getNote();
            double tendance = fb.getTendance();
            String tendanceLabel = this.getTendanceLabel(tendance, note);
            String lib = fb.getLib(this.locale);
            if(lib!=null) {
            	lib = super.getXmlCompliantString(lib);
            } else {
            	lib = "";
            }
            out.print("<GOAL lib=\""+lib+"\" note=\""+this.nf.format(fb.getNote()-0.005)+"\" tendance=\""+tendanceLabel+"\" />");
		}
		
		out.println("</GOAL_EVOLUTION>");
	}
	
	private void addGroup(String id, Collection c, PrintWriter out) {
        out.print("<");
        out.print(id);
        out.print(" nb=\"" + this.intf.format(c.size()) + "\"");
        out.println(">");
        generateEvolutionList(c, out);
        out.print("</");
        out.print(id);
        out.println(">");
	}
	
	private void addGroupWithElements(String id, List<BottomUpDetailBean> c, PrintWriter out) {
        out.print("<");
        out.print(id);
        out.print(" nb=\"" + this.intf.format(c.size()) + "\"");
        out.println(">");
        generateEvolutionListWithElements(c, out);
        out.print("</");
        out.print(id);
        out.println(">");
	}
	
	private void generateEvolutionListWithElements(List<BottomUpDetailBean> c, PrintWriter out) {
		Iterator<BottomUpDetailBean> i = c.iterator();
		while (i.hasNext()) {
			BottomUpDetailBean evolutionBean = (BottomUpDetailBean)i.next();
			generateEvolutionWithElements(evolutionBean, out);
		}
	}
	
	private void generateEvolutionWithElements(BottomUpDetailBean budb, PrintWriter out) {
        out.print("<ELEMENT");
        out.print(" lib=\""+getXmlCompliantString(budb.getElement().getLib())+"\"");
        out.print(" desc=\""+getXmlCompliantString(budb.getElement().getDesc())+"\"");
        out.print(" nbcrit=\""+this.intf.format(budb.getCriterions().size())+"\"");
        out.println(">");
        for(Iterator it = budb.getCriterions().iterator(); it.hasNext(); ) {
        	CriterionPerFactorBean cpfb = (CriterionPerFactorBean) it.next();
        	this.generateCriterionsForElement(cpfb, out);
        }
        out.println("</ELEMENT>");
	}
	
	private void generateCriterionsForElement(CriterionPerFactorBean crit, PrintWriter out) {
		out.print("<CRITERION");
        out.print(" lib=\""+getXmlCompliantString(crit.getLib(this.locale))+"\"");
        out.print(" desc=\""+getXmlCompliantString(crit.getDesc(this.locale))+"\"");
        String tendanceLabel = this.getTendanceLabel(crit.getTendance(), crit.getNote());
        out.print(" tendance=\""+tendanceLabel+"\"");
        out.println("/>");
	}
	
	private void generateEvolutionList(Collection c, PrintWriter out) {
		Iterator i = c.iterator();
		while (i.hasNext()) {
			ElementEvolutionSummaryBean evolutionBean = (ElementEvolutionSummaryBean)i.next();
			generateEvolution(evolutionBean, out);
		}
	}
	
	private void generateEvolution(ElementEvolutionSummaryBean evolutionBean, PrintWriter out) {
        out.print("<ELEMENT");
        out.print(" lib=\""+getXmlCompliantString(evolutionBean.getLib())+"\"");
        out.print(" desc=\""+getXmlCompliantString(evolutionBean.getDesc())+"\"");
        out.print(" nbcrit=\""+this.intf.format(evolutionBean.getNbCriterions())+"\"");
        out.println("/>");
	}
	
}
