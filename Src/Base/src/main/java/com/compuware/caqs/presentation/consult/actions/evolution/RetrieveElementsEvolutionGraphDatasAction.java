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
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.EvolutionSvc;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author cwfr-fdubois
 */
public class RetrieveElementsEvolutionGraphDatasAction extends ExtJSAjaxAction {

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

        EvolutionSvc evolutionSvc = EvolutionSvc.getInstance();

        String idElt = request.getParameter("idElt");
        ElementBean ea = ElementSvc.getInstance().retrieveElementById(idElt);
        String currentIdBline = request.getParameter("currentIdBline");
        String previousIdBline = request.getParameter("previousIdBline");
        BaselineBean previousBline = BaselineSvc.getInstance().getRealBaselineForEA(ea, previousIdBline);

        String filterDesc = JdbcDAOUtils.DATABASE_STRING_NOFILTER;
        String filterTypeElt = ElementType.ALL;
        FilterBean filter = new FilterBean(filterDesc, filterTypeElt);

        List<ElementEvolutionSummaryBean> newAndBadElements = evolutionSvc.retrieveNewAndBadElements(idElt, currentIdBline, previousBline, filter);
        List<ElementEvolutionSummaryBean> oldAndWorstElements = evolutionSvc.retrieveOldAndWorstElements(idElt, currentIdBline, previousBline, filter);
        List<ElementEvolutionSummaryBean> oldAndBetterElements = evolutionSvc.retrieveOldAndBetterElements(idElt, currentIdBline, previousBline, filter);
        List<ElementEvolutionSummaryBean> oldBetterWorstElements = evolutionSvc.retrieveOldBetterAndWorstElements(idElt, currentIdBline, previousBline, filter);
        List<ElementEvolutionSummaryBean> stableElements = evolutionSvc.retrieveStableElements(idElt, currentIdBline, previousBline, filter);
        List<ElementEvolutionSummaryBean> badAndStableElements = evolutionSvc.retrieveBadAndStableElements(idElt, currentIdBline, previousBline, filter);
        ElementBean eltBean = new ElementBean();
        eltBean.setId(idElt);
        BaselineBean bb = BaselineSvc.getInstance().getRealBaselineForEA(eltBean, currentIdBline);
        bb.setId(currentIdBline);
        eltBean.setBaseline(bb);
        BaselineBean prevBb = BaselineSvc.getInstance().getRealBaselineForEA(eltBean, previousIdBline);
        Volumetry volumetry = evolutionSvc.retrieveSumVolumetryBetweenBaselines(idElt, bb, prevBb);

        if (messagesCode == MessagesCodes.NO_ERROR) {
            List<JSONObject> series = new ArrayList<JSONObject>();
            series.addAll(this.getNewSeries(newAndBadElements, volumetry, resources, loc));
            series.addAll(this.getEvolvedSeries(oldAndWorstElements, oldAndBetterElements, oldBetterWorstElements, resources, loc));
            series.addAll(this.getStableSeries(stableElements, badAndStableElements, resources, loc));
            series.addAll(this.getDeletedSeries(volumetry, resources, loc));
            this.sortSeries(series);
            retour.put("datas", series.toArray(new JSONObject[series.size()]));

            //on met en place les ticks de l'axe des abscisses
            JSONArray ticksDataArray = new JSONArray();
            JSONArray tick = new JSONArray();
            tick.add(1);
            tick.add(resources.getMessage(loc, "caqs.evolution.elts.category.new"));
            ticksDataArray.add(tick);
            tick = new JSONArray();
            tick.add(2);
            tick.add(resources.getMessage(loc, "caqs.evolution.elts.category.evolved"));
            ticksDataArray.add(tick);
            tick = new JSONArray();
            tick.add(3);
            tick.add(resources.getMessage(loc, "caqs.evolution.elts.category.stable"));
            ticksDataArray.add(tick);
            tick = new JSONArray();
            tick.add(4);
            tick.add(resources.getMessage(loc, "caqs.evolution.elts.category.deleted"));
            ticksDataArray.add(tick);
            retour.put("xaxisTicks", ticksDataArray);

        }
        this.fillJSONObjectWithReturnCode(retour, messagesCode);

        return retour;
    }

    /**
     * l'ajout de 0 en troisieme position concerne flot : bottom
     * ensuite, le libelle de la donnees
     * puis l'indice servant a reconnaitre la serie
     * on place la premiere barre : elements nouveaux
     * deux sections : avec anomalies / sans anomalies
     * ce sera la valeur 1 sur l'axe des abscisses
     * @param newAndBadElements
     * @param volumetry
     * @param resources
     * @param loc
     * @return
     */
    private List<JSONObject> getNewSeries(List<ElementEvolutionSummaryBean> newAndBadElements, Volumetry volumetry, MessageResources resources, Locale loc) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray series = new JSONArray();
        JSONArray obj = new JSONArray();
        //il faut deux series
        //on place la serie des nouveaux sans anomalies
        obj.add(1);
        obj.add(volumetry.getCreated() - newAndBadElements.size());
        obj.add(0);
        obj.add(ElementsCategory.NEW_OK.getCode());
        obj.add(false);
        obj.add(nf.format(volumetry.getCreated() - newAndBadElements.size()));
        series.add(obj);
        JSONObject serie = new JSONObject();
        JSONArray a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.newOk"));
        retour.add(serie);
        //on place la serie des nouveaux en anomalie
        series = new JSONArray();
        obj = new JSONArray();
        obj.add(1);
        obj.add(newAndBadElements.size());
        obj.add(0);
        obj.add(ElementsCategory.NEW_BAD.getCode());
        obj.add(true);
        obj.add(nf.format(newAndBadElements.size()));
        series.add(obj);
        serie = new JSONObject();
        a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.newBad"));
        retour.add(serie);
        //this.sortSeries(retour);
        return retour;
    }

    /**
     * l'ajout de 0 en troisieme position concerne flot : bottom
     * ensuite, le libelle de la donnees
     * puis l'indice servant a reconnaitre la serie
     * on place la deuxieme barre : elements ayant evolues
     * trois sections : degrades, ameliores, degrades et ameliores
     * ce sera la valeur 2 sur l'axe des abscisses
     * @param oldAndWorstElements
     * @param oldAndBetterElements
     * @param oldBetterWorstElements
     * @param resources
     * @param loc
     * @return
     */
    private List<JSONObject> getEvolvedSeries(List<ElementEvolutionSummaryBean> oldAndWorstElements,
            List<ElementEvolutionSummaryBean> oldAndBetterElements,
            List<ElementEvolutionSummaryBean> oldBetterWorstElements,
            MessageResources resources, Locale loc) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        //degrades
        JSONArray series = new JSONArray();
        JSONArray obj = new JSONArray();
        obj.add(2);
        obj.add(oldAndWorstElements.size());
        obj.add(0);
        obj.add(ElementsCategory.OLD_WORST.getCode());
        obj.add(true);
        obj.add(nf.format(oldAndWorstElements.size()));
        series.add(obj);
        JSONObject serie = new JSONObject();
        JSONArray a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.oldWorst"));
        retour.add(serie);
        //ameliores
        series = new JSONArray();
        obj = new JSONArray();
        obj.add(2);
        obj.add(oldAndBetterElements.size());
        obj.add(0);
        obj.add(ElementsCategory.OLD_BETTER.getCode());
        obj.add(true);
        obj.add(nf.format(oldAndBetterElements.size()));
        series.add(obj);
        serie = new JSONObject();
        a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.oldBetter"));
        retour.add(serie);
        //degrades et ameliores
        series = new JSONArray();
        obj = new JSONArray();
        obj.add(2);
        obj.add(oldBetterWorstElements.size());
        obj.add(0);
        obj.add(ElementsCategory.OLD_BETTER_WORST.getCode());
        obj.add(true);
        obj.add(nf.format(oldBetterWorstElements.size()));
        series.add(obj);
        serie = new JSONObject();
        a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.oldBetterWorst"));
        retour.add(serie);
        //this.sortSeries(retour);
        return retour;
    }

    /**
     * l'ajout de 0 en troisieme position concerne flot : bottom
     * ensuite, le libelle de la donnees
     * puis l'indice servant a reconnaitre la serie
     * on place la troisieme barre : elements stables
     * deux sections : stables / stables en anomalie
     * ce sera la valeur 3 sur l'axe des abscisses
     * stables
     * @param stableElements
     * @param badAndStableElements
     * @param oldBetterWorstElements
     * @param resources
     * @param loc
     * @return
     */
    private List<JSONObject> getStableSeries(List<ElementEvolutionSummaryBean> stableElements,
            List<ElementEvolutionSummaryBean> badAndStableElements,
            MessageResources resources, Locale loc) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray series = new JSONArray();
        JSONArray obj = new JSONArray();
        //stables
        obj.add(3);
        obj.add(stableElements.size());
        obj.add(0);
        obj.add(ElementsCategory.STABLE_OK.getCode());
        obj.add(false);
        obj.add(nf.format(stableElements.size()));
        series.add(obj);
        JSONObject serie = new JSONObject();
        JSONArray a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.stable"));
        retour.add(serie);
        //stables en anomalie
        series = new JSONArray();
        obj = new JSONArray();
        obj.add(3);
        obj.add(badAndStableElements.size());
        obj.add(0);
        obj.add(ElementsCategory.BAD_STABLE.getCode());
        obj.add(true);
        obj.add(nf.format(badAndStableElements.size()));
        series.add(obj);
        serie = new JSONObject();
        a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.badStable"));
        retour.add(serie);
        //this.sortSeries(retour);
        return retour;
    }

    /**
     * l'ajout de 0 en troisieme position concerne flot : bottom
     * ensuite, le libelle de la donnees
     * puis l'indice servant a reconnaitre la serie
     * on place la quatrieme barre : elements supprimes
     * @param volumetry
     * @param resources
     * @param loc
     * @return
     */
    private List<JSONObject> getDeletedSeries(Volumetry volumetry,
            MessageResources resources, Locale loc) {
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        List<JSONObject> retour = new ArrayList<JSONObject>();
        JSONArray series = new JSONArray();
        JSONArray obj = new JSONArray();
        //deleted
        obj.add(4);
        obj.add(volumetry.getDeleted());
        obj.add(0);
        obj.add(ElementsCategory.DELETED.getCode());
        obj.add(false);
        obj.add(nf.format(volumetry.getDeleted()));
        series.add(obj);
        JSONObject serie = new JSONObject();
        JSONArray a = new JSONArray();
        a.add(obj);
        serie.put("data", a);
        serie.put("label", resources.getMessage(loc, "caqs.evolution.elts.delElts"));
        retour.add(serie);
        //this.sortSeries(retour);
        return retour;
    }

    private void sortSeries(List<JSONObject> series) {
        Collections.sort(series, new Comparator<JSONObject>() {

            public int compare(JSONObject o1, JSONObject o2) {
                JSONArray obj1 = o1.getJSONArray("data");
                JSONArray tmp = obj1.getJSONArray(0);
                Integer a = tmp.getInt(0);
                JSONArray obj2 = o2.getJSONArray("data");
                tmp = obj2.getJSONArray(0);
                Integer b = tmp.getInt(0);
                int retour = a.compareTo(b);
                if (retour == 0) {
                    tmp = obj1.getJSONArray(0);
                    a = tmp.getInt(1);
                    tmp = obj2.getJSONArray(0);
                    b = tmp.getInt(1);
                    retour = a.compareTo(b);
                }
                return retour;
            }
        });
    }
}
