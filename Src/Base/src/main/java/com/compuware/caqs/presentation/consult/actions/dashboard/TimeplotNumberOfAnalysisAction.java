package com.compuware.caqs.presentation.consult.actions.dashboard;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.SyntheseSvc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.DashboardSvc;

/**
 *
 * @author cwfr-dzysman
 */
public class TimeplotNumberOfAnalysisAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        List<DashboardElementBean> elts = (List<DashboardElementBean>) request.getSession().getAttribute("dashboardElementsBean");
        DashboardElementBean elt = null;
        if (elts != null) {
            String idElt = request.getParameter("idElt");
            if (idElt != null && !"".equals(idElt)) {
                for (DashboardElementBean e : elts) {
                    if (e.getId().equals(idElt)) {
                        elt = e;
                        break;
                    }
                }
            }
        }
        JSONObject obj = this.retrieveDatas(elt, request);
        return obj;
    }

    private JSONObject retrieveDatas(DashboardElementBean elt, HttpServletRequest request) {
        JSONObject retour = new JSONObject();
        MessagesCodes messagesCode = MessagesCodes.NO_ERROR;

        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        SimpleDateFormat dayMonthYearDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Collection<BaselineBean> baselines = null;
        JSONArray numberOfAnalysisArray = null;
        JSONArray analysisArray = null;
        Users connectedUser = RequestUtil.getConnectedUser(request);

        if (elt != null) {
            baselines = BaselineSvc.getInstance().retrieveBaselinesAndInstanciationByProjectId(elt.getProject().getId());
            if (baselines != null) {
                analysisArray = this.retrieveBaselinesData(elt, baselines,
                        dayMonthYearDateFormat);
                numberOfAnalysisArray = this.retrieveNumberOfAnalysisForAllMonthes(baselines, dayMonthYearDateFormat);
            }
        } else {
            //cela ne concerne pas un element en particulier, donc cela concerne tous les elements du dashboard
            List<DashboardElementBean> elts = null;
            String domainId = request.getParameter("domainId");
            String rootId = "ENTRYPOINT";
            if (domainId != null && !Constants.DASHBOARD_ALL_DOMAINS.equals(domainId)) {
                rootId = domainId;
            }
            if (connectedUser.isInRole(new String[]{"ADMINISTRATOR"})) {
                elts = DashboardSvc.getInstance().retrieveAllElementsForDashboard(ElementType.PRJ, resources, loc, null, rootId, request);
            } else {
                elts = (List<DashboardElementBean>) request.getSession().getAttribute("dashboardElementsBean");
            }
            baselines = new ArrayList<BaselineBean>();

            if (elts != null) {
                for (DashboardElementBean dashboardElement : elts) {
                    baselines.addAll(BaselineSvc.getInstance().retrieveBaselinesAndInstanciationByProjectId(dashboardElement.getProject().getId()));
                }
                numberOfAnalysisArray = this.retrieveNumberOfAnalysisForAllMonthes(baselines, dayMonthYearDateFormat);
            }
        }

        this.fillJSONObjectWithReturnCode(retour, messagesCode);
        if (messagesCode == MessagesCodes.NO_ERROR) {
            JSONArray globalDataArray = new JSONArray();

            if (analysisArray != null) {
                globalDataArray.add(analysisArray);
            }
            if (numberOfAnalysisArray != null) {
                globalDataArray.add(numberOfAnalysisArray);
            }

            retour.put("datas", globalDataArray);
        }
        return retour;
    }

    /**
     * renvoie un tableau json concernant les analyses.
     * plusieurs analyses le meme jour sont sur la meme barre dans le graphique.
     * @param elt
     * @param baselines
     * @param dayMonthYearDateFormat
     * @return
     */
    private JSONArray retrieveBaselinesData(DashboardElementBean elt,
            Collection<BaselineBean> baselines,
            SimpleDateFormat dayMonthYearDateFormat) {
        JSONArray analysisArray = new JSONArray();
        //on commence par les données sur les baselines
        Map<String, List<BaselineBean>> baselinesMap = new HashMap<String, List<BaselineBean>>();
        Map<String, Integer> nbBaselinesForADay = new HashMap<String, Integer>();
        for (BaselineBean baseline : baselines) {
            if (Constants.INSTANCIATION_BASELINE_NAME.equals(baseline.getLib())) {
                continue;
            }
            Date dMaj = baseline.getDmaj();
            String day = dayMonthYearDateFormat.format(dMaj);
            List<BaselineBean> liste = baselinesMap.get(day);
            Integer nb = nbBaselinesForADay.get(day);
            if (liste == null) {
                liste = new ArrayList<BaselineBean>();
                baselinesMap.put(day, liste);
                nb = Integer.valueOf(0);
            }
            nb = Integer.valueOf(nb.intValue() + 1);
            liste.add(baseline);
            nbBaselinesForADay.put(day, nb);
        }
        //on crée les données
        for (Map.Entry<String, List<BaselineBean>> entry : baselinesMap.entrySet()) {
            try {
                String day = entry.getKey();
                List<BaselineBean> baselinesOfTheDay = entry.getValue();
                JSONArray obj = new JSONArray();
                obj.add(dayMonthYearDateFormat.parse(day).getTime());
                BaselineBean lastBaselineOfTheDay = null;
                double lastScore = 0.0;
                double bestScore = 0.0;
                double worstScore = 4.0;
                for (Iterator<BaselineBean> it = baselinesOfTheDay.iterator(); it.hasNext();) {
                    BaselineBean bb = it.next();
                    double score = 0.0;
                    List<FactorBean> factors = SyntheseSvc.getInstance().retrieveKiviat(elt, bb.getId());
                    if (factors != null) {
                        score = this.getAverageScore(factors);
                    }
                    if (score == 0.0) {
                        it.remove();
                        continue;
                    }

                    if ((lastBaselineOfTheDay == null) ||
                            (bb.getDmaj().after(lastBaselineOfTheDay.getDmaj()))) {
                        lastBaselineOfTheDay = bb;
                        lastScore = score;
                    }
                    if (score < worstScore) {
                        worstScore = score;
                    }
                    if (score > bestScore) {
                        bestScore = score;
                    }
                }
                if (lastBaselineOfTheDay == null) {
                    continue;
                }
                obj.add(lastScore);
                obj.add(0);//bottom a ajouter pour les dernieres versions de flot
                obj.add(baselinesOfTheDay.size());

                obj.add(worstScore);
                obj.add(bestScore);
                analysisArray.add(obj);
            } catch (ParseException e) {
            }
        }
        return analysisArray;
    }

    /**
     * renvoie les donnees concernant la courbe des analyses mensuelles
     * @param baselines
     * @param dayMonthYearDateFormat
     * @return
     */
    private JSONArray retrieveNumberOfAnalysisForAllMonthes(Collection<BaselineBean> baselines,
            SimpleDateFormat dayMonthYearDateFormat) {
        JSONArray numberOfAnalysisArray = new JSONArray();
        SimpleDateFormat monthYearDateFormat = new SimpleDateFormat("yyyy-MM");
        Date firstBaseline = null;
        Date lastBaseline = null;
        Map<String, Integer> numbersOfAnalysisPerMonth = new HashMap<String, Integer>();
        boolean lastDateFound = false;
        Map<Long, Integer> completeDates = new HashMap<Long, Integer>();

        for (BaselineBean baseline : baselines) {
            Date d = baseline.getDmaj();
            if (!Constants.INSTANCIATION_BASELINE_NAME.equals(baseline.getLib())) {
                if (lastBaseline == null) {
                    lastBaseline = d;
                } else {
                    if (d.after(lastBaseline)) {
                        lastBaseline = d;
                    }
                }
                String st = monthYearDateFormat.format(d);
                Integer i = numbersOfAnalysisPerMonth.get(st);
                if (i == null) {
                    i = new Integer(1);
                } else {
                    i = new Integer(i.intValue() + 1);
                }
                numbersOfAnalysisPerMonth.put(st, i);
            } else {
                //baseline d'instanciation
                if (firstBaseline == null) {
                    firstBaseline = baseline.getDinst();
                } else if (baseline.getDinst().before(firstBaseline)) {
                    //cela peut arriver dans le cas ou l'on a plusieurs baselines d'instanciation
                    //cad pour le graphe global des analyses
                    firstBaseline = baseline.getDinst();
                }
            }
        }

        if (firstBaseline == null || lastBaseline == null) {
            numberOfAnalysisArray = null;
        } else {
            Calendar now = Calendar.getInstance();
            //pour chaque mois ayant des analyses
            for (Map.Entry<String, Integer> entries : numbersOfAnalysisPerMonth.entrySet()) {
                String date = entries.getKey();
                try {
                    Date d = monthYearDateFormat.parse(date);
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(d.getTime());
                    //si ce n'est pas le mois du jour
                    if (!this.isInSameMonth(now, c)) {
                        //on ajoute la derniere date du mois
                        int lastDay = c.getActualMaximum(Calendar.DATE);
                        c.set(Calendar.DATE, lastDay);
                    } else {
                        //sinon on ajoute la date du jour et on declare la
                        //derniere date comme ajoutee
                        c = now;
                        lastDateFound = true;
                    }
                    date = dayMonthYearDateFormat.format(new Date(c.getTimeInMillis()));
                    completeDates.put(c.getTimeInMillis(), entries.getValue());
                } catch (ParseException exc) {
                    mLog.error("TimeplotNumberOfAnalysisAction : impossible to parse " +
                            entries.getKey() + " : " + exc.getMessage());
                }
            }

            //si la derniere date n'a pas ete deja ajoutee, cela signifie qu'aucune
            //analyse n'a ete faite de mois-ci
            if (!lastDateFound) {
                //on ajoute la date du jour
                /*boolean addNowDate = true;
                if (lastBaseline != null) {
                Calendar lastAnalysisMonth = Calendar.getInstance();
                lastAnalysisMonth.setTimeInMillis(lastBaseline.getTime());
                if (this.isInSameMonth(lastAnalysisMonth, now)) {
                addNowDate = false;
                }
                }
                if (addNowDate) {*/
                completeDates.put(now.getTimeInMillis(), Integer.valueOf(0));
            //}
            }

            //on ajoute la date de la baseline d'instanciation comme première date
            completeDates.put(firstBaseline.getTime(), Integer.valueOf(0));

            this.retrieveAllDates(completeDates);


            List<Map.Entry<Long, Integer>> sortedList = this.sortEntries(completeDates);
            for (Map.Entry<Long, Integer> entries : sortedList) {
                JSONArray obj = new JSONArray();
                obj.add(entries.getKey());
                obj.add(entries.getValue());
                numberOfAnalysisArray.add(obj);
            }
        }
        return numberOfAnalysisArray;
    }

    private List<Map.Entry<Long, Integer>> sortEntries(Map<Long, Integer> map) {
        List<Map.Entry<Long, Integer>> retour = new ArrayList<Map.Entry<Long, Integer>>();
        for (Map.Entry<Long, Integer> entries : map.entrySet()) {
            retour.add(entries);
        }
        Collections.sort(retour, new Comparator<Map.Entry<Long, Integer>>() {

            public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return retour;
    }

    /**
     * remplit la map des dates connues avec les dates correspondant aux mois n'ayant
     * aucune analyse
     * @param knownDates
     */
    private void retrieveAllDates(Map<Long, Integer> knownDates) {
        //on recherche la première date et la dernière
        Date earlest = null;
        Date latest = null;
        for (Map.Entry<Long, Integer> entries : knownDates.entrySet()) {
            Date d = new Date(entries.getKey());
            if (earlest == null) {
                earlest = d;
            } else {
                if (d.before(earlest)) {
                    earlest = d;
                }
            }
            if (latest == null) {
                latest = d;
            } else {
                if (d.after(latest)) {
                    latest = d;
                }
            }
        }
        if (earlest != null && latest != null) {
            Calendar current = Calendar.getInstance();
            current.setTimeInMillis(earlest.getTime());
            Calendar lastMonth = Calendar.getInstance();
            lastMonth.setTimeInMillis(latest.getTime());
            while (!this.isInSameMonth(current, lastMonth)) {
                Date d = new Date(current.getTimeInMillis());
                if (knownDates.get(d.getTime()) == null) {
                    //le mois n'existe pas, on le place a 0
                    knownDates.put(d.getTime(), Integer.valueOf(0));
                }
                //on passe au dernier jour du mois suivant (passer directement au mois suivant fonctionne-t-il dans le cas de janvier->fevrier
                //on passe au mois suivant
                current.add(Calendar.DAY_OF_MONTH, 1);
                //on passe au dernier jour
                current.set(Calendar.DATE, current.getActualMaximum(Calendar.DATE));
            }
        }

    }

    private boolean isInSameMonth(Calendar current, Calendar tested) {
        return (current.get(Calendar.MONTH) == tested.get(Calendar.MONTH)) &&
                (current.get(Calendar.YEAR) == tested.get((Calendar.YEAR)));
    }

    private boolean isSameDay(Calendar current, Calendar tested) {
        return ((current.get(Calendar.DAY_OF_MONTH) ==
                tested.get(Calendar.DAY_OF_MONTH))) &&
                (current.get(Calendar.MONTH) == tested.get(Calendar.MONTH)) &&
                (current.get(Calendar.YEAR) == tested.get((Calendar.YEAR)));
    }

    private double getAverageScore(Collection<FactorBean> factors) {
        double retour = 0.0;
        int nbFact = 0;
        for (FactorBean factor : factors) {
            retour += factor.getNote();
            if (factor.getNote() > 0.0) {
                nbFact++;
            }
        }
        if (nbFact > 0) {
            retour /= nbFact;
        } else {
            retour = 0.0;
        }
        return retour;
    }
}
