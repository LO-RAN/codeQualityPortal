package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;

import com.compuware.caqs.business.consult.SyntheseActionPlan;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ActionPlanUnitDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.domain.dataschemas.actionplan.comparator.ActionPlanElementBeanComparator;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.service.ActionPlanSvc;
import java.util.Locale;

public class DetailActionPlanXmlGenerator extends XmlGenerator {

    private ActionPlanBean actionplan;
    private boolean savedActionPlan = false;

    /**
     * 
     * @param eltBean
     * @param noteSeuil
     * @param loc
     */
    public DetailActionPlanXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
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
        PrintWriter out = createWriter(destination, "details-actionplan.xml");
        printReportHeader(out);

        double totalCost = 0.0;
        int nbShort = 0;
        int nbMedium = 0;
        int nbLong = 0;
        int shortCost = 0;
        int mediumCost = 0;
        int longCost = 0;

        StringBuffer sb = new StringBuffer();

        List<ActionPlanElementBean> elementList = this.actionplan.getElements();
        Collections.sort(elementList, new ActionPlanElementBeanComparator(locale, "lib", true));

        for (ActionPlanElementBean element : elementList) {
            if (element instanceof ActionPlanElementBean) {
                ActionPlanCriterionBean criterion = (ActionPlanCriterionBean) element;
                if (criterion.isCorrected()) {
                    totalCost += criterion.getCost();
                    if (ActionPlanPriority.SHORT_TERM.equals(element.getPriority())) {
                        nbShort++;
                        shortCost += criterion.getCost();
                    } else if (ActionPlanPriority.MEDIUM_TERM.equals(element.getPriority())) {
                        nbMedium++;
                        mediumCost += criterion.getCost();
                    } else if (ActionPlanPriority.LONG_TERM.equals(element.getPriority())) {
                        nbLong++;
                        longCost += criterion.getCost();
                    }
                }
                if (criterion.isCorrected() || (criterion.getComment() != null &&
                        !criterion.getComment().equals(""))) {
                    this.generateCriterion(criterion, totalCost, sb);
                }
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
        this.generateImpactedElementsList(sb);
        out.print(sb.toString());
        out.print("</ACTIONPLAN>");
        printReportFooter(out);
        out.flush();
        out.close();
    }

    private void generateCriterion(ActionPlanCriterionBean criterion, double totalCost, StringBuffer out) {
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
        out.append(" comment=\"" + comment + "\" ");
        out.append(" userComment=\"" + userComment + "\" ");
        out.append(">\n");

        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        List<MetriqueDefinitionBean> metriques = metriqueDao.retrieveMetriqueDefinitionByIdCrit(criterion.getId(),
                this.eltBean.getUsage().getId());

        if (metriques != null && !metriques.isEmpty()) {
            for (MetriqueDefinitionBean metrique : metriques) {
                out.append("  <METRIC");
                out.append("    id=\"" + metrique.getId() + "\"");
                out.append("    lib=\"" +
                        getXmlCompliantString(metrique.getLib(this.locale)) +
                        "\"");
                out.append("    desc=\"" +
                        getXmlCompliantString(metrique.getDesc(this.locale)) +
                        "\"");
                out.append("    compl=\"" +
                        getXmlCompliantString(metrique.getComplement(this.locale)) +
                        "\"");
                out.append("  />\n");
            }
        }

        out.append("</CRITERE>\n");
    }

    private void generateImpactedElementsList(StringBuffer out) {
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanElementBeanMap elementMap = actionPlanSvc.getElementsImpactedByActionPlan(this.actionplan.getElements(), super.eltBean.getUsage().getId());
        if (elementMap != null) {
            //on les trie par type d'element
            Map<String, List<ActionPlanImpactedElementBean>> tri = new HashMap<String, List<ActionPlanImpactedElementBean>>();
            for (ActionPlanImpactedElementBean elt : elementMap.values()) {
                List<ActionPlanImpactedElementBean> liste = tri.get(elt.getIdTelt());
                if (liste == null) {
                    liste = new ArrayList<ActionPlanImpactedElementBean>();
                    tri.put(elt.getIdTelt(), liste);
                }
                liste.add(elt);
            }
            for (String teltId : tri.keySet()) {
                ElementType et = new ElementType(teltId);
                List<ActionPlanImpactedElementBean> elts = (List<ActionPlanImpactedElementBean>) tri.get(teltId);
                BeanComparator descEltComparator = new BeanComparator("descElt");
                Collections.sort(elts, descEltComparator);
                this.generateElementListByType(et, elts, out);
            }
        }
    }

    private void generateElementListByType(ElementType et, List<ActionPlanImpactedElementBean> elts, StringBuffer out) {
        out.append("<ELEMENTTYPE");
        out.append(" id=\"" + et.getId() + "\"");
        out.append(" lib=\"" + getXmlCompliantString(et.getLib(this.locale)) +
                "\"");
        out.append(">\n");
        for (ActionPlanImpactedElementBean element : elts) {
            this.generateElement(element, out);
        }
        out.append("</ELEMENTTYPE>\n");
    }

    private void generateElement(ActionPlanImpactedElementBean element, StringBuffer out) {
        out.append("<ELEMENT");
        out.append(" id=\"" + element.getIdElt() + "\"");
        out.append(" lib=\"" + getXmlCompliantString(element.getLibElt()) + "\"");
        out.append(" desc=\"" + getXmlCompliantString(element.getDescElt()) +
                "\"");
        out.append(">\n");
        for (ActionPlanCriterionBean criterion : element.getCriterions()) {
            this.generateCriterion(criterion, 0.0, out);
        }
        out.append("</ELEMENT>\n");
    }
}
