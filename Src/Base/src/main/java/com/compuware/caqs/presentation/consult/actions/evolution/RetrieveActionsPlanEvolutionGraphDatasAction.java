/*
 * RetrieveElementsEvolutionGraphDatasAction.java
 *
 * Created on 29 janvier 2010, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.ElementSvc;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * cree les deux listes a afficher par critere, toujours selon le precedent plan d'actions
 * la premiere liste contient :
 * - le nombre d'elements a corriger
 * la deuxieme liste contient :
 * - les elements non corriges
 * - les elements corriges
 * - les elements nouveaux a corriger
 * - les elements corriges par suppression
 * @author cwfr-dzysman
 */
public class RetrieveActionsPlanEvolutionGraphDatasAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = this.retrieveDatas(request);
        return obj;
    }

    private JSONObject retrieveDatas(HttpServletRequest request) {
        JSONObject retour = new JSONObject();
        MessagesCodes messagesCode = MessagesCodes.NO_ERROR;
        Locale loc = RequestUtil.getLocale(request);
        MessageResources resources = RequestUtil.getResources(request);
        String idElt = request.getParameter("idElt");
        String currentIdBline = request.getParameter("currentIdBline");
        String previousIdBline = request.getParameter("previousIdBline");
        String previousBaselineLib = request.getParameter("previousBlineLib");
        String currentBaselineLib = request.getParameter("currentBlineLib");
        ElementBean eltBean = ElementSvc.getInstance().retrieveElementById(idElt);
        BaselineBean bb = new BaselineBean();
        bb.setId(currentIdBline);
        eltBean.setBaseline(bb);

        List<JSONObject> series = new ArrayList<JSONObject>();


        ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(eltBean,
                previousIdBline, true, request);
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getCorrectedElements();
        if (ap != null) {
            Map<String, Integer> numberOfElementsToCorrect = ActionPlanSvc.getInstance().getNumberOfElementsInActionsPlanForCriterion(eltBean, previousIdBline);
            Map<String, ActionPlanImpactedElementBeanCollection> correctedElements = ActionPlanSvc.getInstance().getCorrectedElementsByCriterions(
                    eltBean, previousIdBline);
            Map<String, ActionPlanImpactedElementBeanCollection> stableElements = ActionPlanSvc.getInstance().getStableElementsByCriterions(
                    eltBean, previousIdBline);
            Map<String, ActionPlanImpactedElementBeanCollection> partiallyCorrectedElements = ActionPlanSvc.getInstance().getPartiallyCorrectedElementsByCriterions(
                    eltBean, previousIdBline);
            Map<String, ActionPlanImpactedElementBeanCollection> degradedElements = ActionPlanSvc.getInstance().getDegradedElementsByCriterions(
                    eltBean, previousIdBline);
            Map<String, ActionPlanImpactedElementBeanCollection> suppressedElements = ActionPlanSvc.getInstance().getCorrectedBecauseSuppressedElementsByCriterions(
                    eltBean, previousIdBline);
            series.addAll(this.createNumberOfElementsToCorrectSeries(criterions, numberOfElementsToCorrect, loc, resources, previousBaselineLib));
            series.addAll(this.createNumberOfCorrectedElements(criterions, correctedElements, loc, resources, currentBaselineLib));
            series.addAll(this.createNumberOfDegradedElements(criterions, degradedElements, loc, resources, currentBaselineLib));
            series.addAll(this.createNumberOfPartiallyCorrectedElements(criterions, partiallyCorrectedElements, loc, resources, currentBaselineLib));
            series.addAll(this.createNumberOfStableElements(criterions, stableElements, loc, resources, currentBaselineLib));
            series.addAll(this.createNumberOfCorrectedBecauseSuppressedElements(criterions, suppressedElements, loc, resources, currentBaselineLib));
            this.sortSeries(series);
        }

        if (messagesCode == MessagesCodes.NO_ERROR) {
            retour.put("datas", series.toArray(new JSONObject[series.size()]));

            //on met en place les ticks de l'axe des abscisses, ce sont les criteres
            JSONArray ticksDataArray = new JSONArray();
            int i = 1;
            for (ActionPlanCriterionBean criterion : criterions) {
                JSONArray tick = new JSONArray();
                tick.add(i);
                tick.add(criterion.getInternationalizableProperties().getLib(loc));
                ticksDataArray.add(tick);
                i += 3;
            }
            retour.put("xaxisTicks", ticksDataArray);

        }
        this.fillJSONObjectWithReturnCode(retour, messagesCode);

        return retour;
    }

    /**
     * cree la premiere serie :
     * - le nombre d'elements a corriger par critere
     * @return
     */
    private List<JSONObject> createNumberOfElementsToCorrectSeries(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, Integer> numberOfElementsToCorrect, Locale loc, MessageResources resources, String previousBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i => premiere barre
        //i+1=> deuxiemme barre
        //i+2 => espace de separation
        int i = 1;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            Integer iNb = numberOfElementsToCorrect.get(criterion.getId());
            int nb = (iNb == null) ? 0 : iNb.intValue();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfElementsToCorrect", criterion.getInternationalizableProperties().getLib(loc), previousBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.ELEMENTS_TO_CORRECT.getCode());
            obj.add(false);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#000000");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfElementsToCorrectLabel", previousBaselineLib));
        retour.add(serie);
        return retour;
    }

    /**
     * cree la deuxieme serie :
     * - le nombre d'elements corriges
     * @return
     */
    private List<JSONObject> createNumberOfCorrectedElements(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, ActionPlanImpactedElementBeanCollection> correctedElements, Locale loc, MessageResources resources, String currentBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i=1 => premiere barre
        //i=2=> deuxieme barre
        //i=3 => espace de separation
        //ici nous sommes en deuxieme barre
        int i = 2;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            ActionPlanImpactedElementBeanCollection coll = correctedElements.get(criterion.getId());
            int nb = (coll == null) ? 0 : coll.size();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfCorrectedElements", criterion.getInternationalizableProperties().getLib(loc), currentBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.CORRECTED_ELEMENTS.getCode());
            obj.add(true);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#AFD8F8");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfCorrectedElementsLabel"));
        retour.add(serie);
        return retour;
    }

    /**
     * cree la troisieme serie :
     * - le nombre d'elements degrades
     * @return
     */
    private List<JSONObject> createNumberOfDegradedElements(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, ActionPlanImpactedElementBeanCollection> elements, Locale loc, MessageResources resources, String currentBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i=1 => premiere barre
        //i=2=> deuxieme barre
        //i=3 => espace de separation
        //ici nous sommes en deuxieme barre
        int i = 2;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            ActionPlanImpactedElementBeanCollection coll = elements.get(criterion.getId());
            int nb = (coll == null) ? 0 : coll.size();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfDegradedElements", criterion.getInternationalizableProperties().getLib(loc), currentBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.DEGRADED_ELEMENTS.getCode());
            obj.add(true);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#4DA74D");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfDegradedElementsLabel"));
        retour.add(serie);
        return retour;
    }

    /**
     * cree la troisieme serie :
     * - le nombre d'elements partiellement corriges
     * @return
     */
    private List<JSONObject> createNumberOfPartiallyCorrectedElements(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, ActionPlanImpactedElementBeanCollection> elements, Locale loc, MessageResources resources, String currentBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i=1 => premiere barre
        //i=2=> deuxieme barre
        //i=3 => espace de separation
        //ici nous sommes en deuxieme barre
        int i = 2;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            ActionPlanImpactedElementBeanCollection coll = elements.get(criterion.getId());
            int nb = (coll == null) ? 0 : coll.size();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfPartiallyCorrectedElements", criterion.getInternationalizableProperties().getLib(loc), currentBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.PARTIALLY_CORRECTED_ELEMENTS.getCode());
            obj.add(true);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#9E53EF");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfPartiallyCorrectedElementsLabel"));
        retour.add(serie);
        return retour;
    }

    /**
     * cree la troisieme serie :
     * - le nombre d'elements stables
     * @return
     */
    private List<JSONObject> createNumberOfStableElements(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, ActionPlanImpactedElementBeanCollection> correctedElements, Locale loc, MessageResources resources, String currentBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i=1 => premiere barre
        //i=2=> deuxieme barre
        //i=3 => espace de separation
        //ici nous sommes en deuxieme barre
        int i = 2;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            ActionPlanImpactedElementBeanCollection coll = correctedElements.get(criterion.getId());
            int nb = (coll == null) ? 0 : coll.size();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfStableElements", criterion.getInternationalizableProperties().getLib(loc), currentBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.STABLE_ELEMENTS.getCode());
            obj.add(true);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#D15C5C");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfStableElementsLabel"));
        retour.add(serie);
        return retour;
    }

    /**
     * cree la troisieme serie :
     * - le nombre d'elements en anomalie supprimes
     * @return
     */
    private List<JSONObject> createNumberOfCorrectedBecauseSuppressedElements(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions,
            Map<String, ActionPlanImpactedElementBeanCollection> elements, Locale loc, MessageResources resources, String currentBaselineLib) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray seriesData = new JSONArray();
        //on incremente de 3 sur l'axe des abscisse.
        //les indices sont
        //i=1 => premiere barre
        //i=2=> deuxieme barre
        //i=3 => espace de separation
        //ici nous sommes en deuxieme barre
        int i = 2;
        for (ActionPlanCriterionBean criterion : criterions) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            ActionPlanImpactedElementBeanCollection coll = elements.get(criterion.getId());
            int nb = (coll == null) ? 0 : coll.size();
            obj.add(nb);
            obj.add(0);
            obj.add(resources.getMessage(loc, "caqs.evolution.actionplan.numberOfCorrectedBecauseSuppressedElements", criterion.getInternationalizableProperties().getLib(loc), currentBaselineLib));
            obj.add(criterion.getId());
            obj.add(ElementsCategory.SUPPRESSED_ELEMENTS.getCode());
            obj.add(true);
            obj.add(nf.format(nb));
            seriesData.add(obj);
            i += 3;
        }
        JSONObject serie = new JSONObject();
        serie.put("data", seriesData);
        serie.put("color", "#EDC240");
        serie.put("label", resources.getMessage(loc, "caqs.evolution.actionplan.numberOfCorrectedBecauseSuppressedElementsLabel"));
        retour.add(serie);
        return retour;
    }

    private void sortSeries(List<JSONObject> series) {
        Collections.sort(series, new Comparator<JSONObject>() {

            public int compare(JSONObject o1, JSONObject o2) {
                int retour = 0;
                JSONArray obj = o1.getJSONArray("data");
                JSONArray obj2 = o2.getJSONArray("data");
                if (!obj.isEmpty() && !obj2.isEmpty()) {
                    JSONArray tmp = obj.getJSONArray(0);
                    Integer a = tmp.getInt(0);
                    tmp = obj2.getJSONArray(0);
                    Integer b = tmp.getInt(0);
                    retour = a.compareTo(b);
                } else if(obj.isEmpty()) {
                    retour = 1;
                } else {
                    retour = -1;
                }
                return retour;
            }
        });
    }
}
