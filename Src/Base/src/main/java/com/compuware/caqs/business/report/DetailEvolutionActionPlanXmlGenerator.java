package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.compuware.caqs.business.consult.ActionPlan;
import com.compuware.caqs.business.consult.Baseline;
import com.compuware.caqs.business.consult.Evolution;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import java.util.Locale;

public class DetailEvolutionActionPlanXmlGenerator extends XmlGenerator {

    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> corrected = null;
    private boolean hasPreviousActionPlan;
    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> partially = null;
    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> stables = null;
    private ActionPlanElementBeanCollection<ActionPlanCriterionBean> deteriorated = null;

    public DetailEvolutionActionPlanXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        Baseline baselinesvc = new Baseline();
        ActionPlan actionplansvc = new ActionPlan();

        BaselineBean bline = baselinesvc.getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());
        this.hasPreviousActionPlan = false;
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanPreviousBaseline = null;
        if (bline != null) {
            ApplicationEntityActionPlanBean ap = actionplansvc.getCompleteActionPlan(eltBean, bline.getId());
            actionPlanPreviousBaseline = ap.getCorrectedElements();
            hasPreviousActionPlan = !actionPlanPreviousBaseline.isEmpty();

            if (hasPreviousActionPlan) {
                ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlanCopy = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(actionPlanPreviousBaseline);
                ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanForBline = ap.getElementsWithProblematicElement();
                Evolution evolution = new Evolution();

                //we start searching for corrected criterions
                corrected = evolution.getCorrectedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
                //we go on with those partially corrected
                partially = evolution.getPartiallyCorrectedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
                //stables ones
                stables = evolution.getStableCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
                //and the deteriorated ones
                deteriorated = evolution.getDeterioratedCriterionsFromPreviousActionPlan(previousActionPlanCopy, actionPlanForBline);
                for (ActionPlanCriterionBean criterion : this.stables) {
                    criterion.setStablesElts(
                            actionplansvc.getStablesElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                }
                for (ActionPlanCriterionBean criterion : this.corrected) {
                    criterion.setCorrectedElts(
                            actionplansvc.getCorrectedElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                }
                for (ActionPlanCriterionBean criterion : this.partially) {
                    criterion.setPartiallyCorrectedElts(
                            actionplansvc.getPartiallyCorrectedElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                    criterion.setDeterioratedElts(
                            actionplansvc.getDeterioratedElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                    criterion.setCorrectedElts(
                            actionplansvc.getCorrectedElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                }
                for (ActionPlanCriterionBean criterion : this.deteriorated) {
                    criterion.setDeterioratedElts(
                            actionplansvc.getDeterioratedElementsForCriterion(eltBean.getId(), criterion.getId(),
                            eltBean.getBaseline().getId(), bline.getId(), eltBean.getUsage().getId()));
                }
            }
        }
    }

    public void generate(File destination) throws IOException {
        generateCriterions(destination);
    }

    private void generateCorrectedCriterions(StringBuffer out) {
        out.append("  <CORRECTED nb=\"" + this.corrected.size() + "\">");
        for (ActionPlanCriterionBean criterion : this.corrected) {
            this.generateCriterion(criterion, Constants.ACTION_PLAN_CORRECTED, out);
        }
        out.append("  </CORRECTED>");
    }

    private void generatePartiallyCorrectedCriterions(StringBuffer out) {
        out.append("  <PARTIALLYCORRECTED nb=\"" + this.partially.size() + "\">");
        for (ActionPlanCriterionBean criterion : this.partially) {
            this.generateCriterion(criterion, Constants.ACTION_PLAN_PARTIALLY_CORRECTED, out);
        }
        out.append("  </PARTIALLYCORRECTED>");
    }

    private void generateDegradedCriterions(StringBuffer out) {
        out.append("  <DEGRADED nb=\"" + this.deteriorated.size() + "\">");
        for (ActionPlanCriterionBean criterion : this.deteriorated) {
            this.generateCriterion(criterion, Constants.ACTION_PLAN_DETERIORATED, out);
        }
        out.append("  </DEGRADED>");
    }

    private void generateStablesCriterions(StringBuffer out) {
        out.append("  <STABLES nb=\"" + this.stables.size() + "\">");
        for (ActionPlanCriterionBean criterion : this.stables) {
            this.generateCriterion(criterion, Constants.ACTION_PLAN_STABLES, out);
        }
        out.append("  </STABLES>");
    }

    private void generateCriterions(File destination) throws IOException {
        PrintWriter out = createWriter(destination, ReportConstants.ACTIONPLAN_EVOLUTION_DETAILS_XML_FILE_NAME +
                ReportConstants.XML_FILE_EXT);
        printReportHeader(out);

        StringBuffer sb = new StringBuffer();
        sb.append("<ACTIONPLAN ");
        sb.append(" exists=\"").append("" + hasPreviousActionPlan).append("\" ");
        sb.append(">");
        sb.append("</ACTIONPLAN >");
        if (hasPreviousActionPlan) {
            this.generateCorrectedCriterions(sb);
            this.generateDegradedCriterions(sb);
            this.generateStablesCriterions(sb);
            this.generatePartiallyCorrectedCriterions(sb);
        }

        out.print(sb.toString());
        printReportFooter(out);
        out.flush();
        out.close();
    }

    private void generateCriterion(ActionPlanCriterionBean criterion, String target, StringBuffer out) {
        out.append("<CRITERE");
        out.append(" id=\"" + criterion.getId() + "\"");
        out.append(" lib=\"" +
                getXmlCompliantString(criterion.getInternationalizableProperties().getLib(locale)) +
                "\"");
        out.append(" desc=\"" +
                getXmlCompliantString(criterion.getInternationalizableProperties().getDesc(locale)) +
                "\"");
        out.append(" compl=\"" +
                getXmlCompliantString(criterion.getInternationalizableProperties().getComplement(locale)) +
                "\"");
        out.append(" nbImpacted=\"" + nf.format(criterion.getNumberElt()) + "\"");
        out.append(">\n");
        List<ActionPlanImpactedElementBean> elements = new ArrayList<ActionPlanImpactedElementBean>();
        if (Constants.ACTION_PLAN_CORRECTED.equals(target)) {
            elements.addAll(criterion.getCorrectedElts());
        } else if (Constants.ACTION_PLAN_DETERIORATED.equals(target)) {
            elements.addAll(criterion.getDeterioratedElts());
        } else if (Constants.ACTION_PLAN_PARTIALLY_CORRECTED.equals(target)) {
            elements.addAll(criterion.getPartiallyCorrectedElts());
            elements.addAll(criterion.getCorrectedElts());
            elements.addAll(criterion.getDeterioratedElts());
        } else if (Constants.ACTION_PLAN_STABLES.equals(target)) {
            elements.addAll(criterion.getStablesElts());
        }
        for (ActionPlanImpactedElementBean elt : elements) {
            this.generateElement(elt, out);
        }
        out.append("</CRITERE>\n");
    }

    private void generateElement(ActionPlanImpactedElementBean element, StringBuffer out) {
        out.append("<ELEMENT");
        out.append(" id=\"" + element.getIdElt() + "\"");
        out.append(" lib=\"" + getXmlCompliantString(element.getLibElt()) + "\"");
        out.append(" desc=\"" + getXmlCompliantString(element.getDescElt()) +
                "\"");
        out.append(">\n");
        out.append("</ELEMENT>\n");
    }
}
