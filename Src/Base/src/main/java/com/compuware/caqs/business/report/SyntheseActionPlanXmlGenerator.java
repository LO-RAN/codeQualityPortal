package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.compuware.caqs.business.consult.SyntheseActionPlan;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ActionPlanUnitDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import java.util.Locale;

public class SyntheseActionPlanXmlGenerator extends XmlGenerator {

    private ActionPlanBean actionplan;
    private boolean savedActionPlan = false;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public SyntheseActionPlanXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        SyntheseActionPlan syntheseActionPlan = new SyntheseActionPlan();
        this.actionplan = syntheseActionPlan.retrieveSavedActionPlanForEaAndBaseline(
                this.eltBean, this.eltBean.getBaseline().getId());
        savedActionPlan = !this.actionplan.getCorrectedElements().isEmpty();
    }

    public void generate(File destination) throws IOException {
        generateCriterions(destination);
    }

    private void generateCriterions(File destination) throws IOException {
        PrintWriter out = createWriter(destination, "synthese-actionplan.xml");
        printReportHeader(out);
        double totalCost = 0.0;

        double nbShort = 0;
        double nbMedium = 0;
        double nbLong = 0;
        double shortCost = 0;
        double mediumCost = 0;
        double longCost = 0;

        StringBuffer sb = new StringBuffer();

        List<ActionPlanCriterionBean> criterionList = this.actionplan.getElements();
        Collections.sort(criterionList, new Comparator<ActionPlanCriterionBean>() {

            public int compare(ActionPlanCriterionBean o1, ActionPlanCriterionBean o2) {
                return o1.getInternationalizableProperties().getDesc(locale).compareTo(o2.getInternationalizableProperties().getDesc(locale));
            }
        });

        for (ActionPlanCriterionBean criterion : criterionList) {
            if (criterion.isCorrected()) {
                totalCost += criterion.getCost();
                if (ActionPlanPriority.SHORT_TERM.equals(criterion.getPriority())) {
                    nbShort++;
                    shortCost += criterion.getCost();
                } else if (ActionPlanPriority.MEDIUM_TERM.equals(criterion.getPriority())) {
                    nbMedium++;
                    mediumCost += criterion.getCost();
                } else if (ActionPlanPriority.LONG_TERM.equals(criterion.getPriority())) {
                    nbLong++;
                    longCost += criterion.getCost();
                }
            }
        }

        for (ActionPlanCriterionBean criterion : criterionList) {
            if (criterion.isCorrected() || (criterion.getComment() != null &&
                    !criterion.getComment().equals(""))) {
                this.generateCriterion(criterion, totalCost, sb);
            }
        }

        out.print("<ACTIONPLAN ");
        out.print("saved=\"" + savedActionPlan + "\" ");
        out.print("totalCost=\"" + nf.format(totalCost) + "\" ");
        out.print("nbShort=\"" + intf.format(nbShort) + "\" ");
        out.print("nbMedium=\"" + intf.format(nbMedium) + "\" ");
        out.print("nbLong=\"" + intf.format(nbLong) + "\" ");
        out.print("shortCost=\"" + nf.format(shortCost) + "\" ");
        out.print("mediumCost=\"" + nf.format(mediumCost) + "\" ");
        out.print("longCost=\"" + nf.format(longCost) + "\" ");

        String comment = (this.actionplan.getActionPlanComment() == null) ? "" : getXmlCompliantString(this.actionplan.getActionPlanComment());
        out.print("comment=\"" + comment + "\" ");
        String userComment = (this.actionplan.getCommentUser() == null) ? "" : getXmlCompliantString(this.actionplan.getCommentUser());
        out.print("userComment=\"" + userComment + "\" ");
        out.println(">");
        ActionPlanUnitDao unitDao = DaoFactory.getInstance().getActionPlanUnitDao();
        List<ActionPlanUnit> units = unitDao.retrieveAllActionPlanUnits();
        for(ActionPlanUnit unit : units) {
            out.println("<WORKUNIT unit=\"" + unit.getNbUnits() + "\" type=\"" + unit.getLib(locale) +
                "\" />");
        }
        out.print(sb.toString());

        out.print("</ACTIONPLAN>");
        printReportFooter(out);
        out.flush();
        out.close();
    }

    private void generateCriterion(ActionPlanCriterionBean criterion, double totalCost, StringBuffer out) {
        out.append("<CRITERE");
        out.append(" id=\"" + criterion.getId() + "\"");
        out.append(" lib=\"" + getXmlCompliantString(criterion.getInternationalizableProperties().getLib(locale)) +
                "\"");
        out.append(" desc=\"" + getXmlCompliantString(criterion.getInternationalizableProperties().getDesc(locale)) +
                "\"");
        out.append(" cost=\"" + nf.format(criterion.getCost()) + "\"");
        out.append(" corrected=\"" + criterion.isCorrected() + "\"");
        out.append(" nbImpacted=\"" + intf.format(criterion.getNumberElt()) +
                "\"");
        double percent = (100.0 * criterion.getCost()) / totalCost;
        out.append(" percent=\"" +
                ((criterion.isCorrected() && totalCost > 0) ? pctf.format(percent) : 0.0) +
                "\"");
        out.append(" priority=\"" + criterion.getPriority().toString() + "\" ");
        String comment = (criterion.getComment() == null) ? "" : getXmlCompliantString(criterion.getComment());
        String userComment = (criterion.getCommentUser() == null) ? "" : getXmlCompliantString(criterion.getCommentUser());
        out.append("comment=\"" + comment + "\" ");
        out.append("userComment=\"" + userComment + "\" ");
        out.append(">\n");
        out.append("</CRITERE>\n");
    }
}
