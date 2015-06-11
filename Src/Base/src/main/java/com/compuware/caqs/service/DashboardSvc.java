/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import java.util.Iterator;

/**
 *
 * @author cwfr-dzysman
 */
public class DashboardSvc {

    private static final DashboardSvc instance = new DashboardSvc();

    private DashboardSvc() {
    }

    public static DashboardSvc getInstance() {
        return instance;
    }

    public List<DashboardElementBean> retrieveAllElementsForDashboard(String elementTypeToRetrieve,
            MessageResources ms, Locale loc, String userId, HttpServletRequest request) {
        return this.retrieveAllElementsForDashboard(elementTypeToRetrieve, ms, loc, userId, "ENTRYPOINT", request);
    }

    public List<DashboardElementBean> retrieveAllElementsForDashboard(String elementTypeToRetrieve,
            MessageResources ms, Locale loc, String userId, String rootId, HttpServletRequest request) {
        SyntheseSvc synthese = SyntheseSvc.getInstance();
        List<DashboardElementBean> retour = new ArrayList<DashboardElementBean>();
        List<ElementBean> collEltBean = ElementSvc.getInstance().retrieveAllElementsForTypeBelongingToParentByUser(rootId,
                    userId, elementTypeToRetrieve);
        for (ElementBean eltBean : collEltBean) {
            if (eltBean.getBaseline() == null) {
                BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(eltBean);
                if (bb != null) {
                    bb.setProject(eltBean.getProject());
                    eltBean.setBaseline(bb);
                }
            }
            if (eltBean.getBaseline() != null) {
                DashboardElementBean elt = new DashboardElementBean(eltBean);
                String meteoImg = "";
                String meteoTooltip = "";
                double allCode = MetricSvc.getInstance().getAllCodeValue(elt);
                elt.setNbLOC((int) allCode);
                List<ElementBean> eas = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProject(eltBean);
                double allFileElts = 0.0;
                if(!eas.isEmpty()) {
                    allFileElts = ElementSvc.getInstance().retrieveGlobalNumberOfElements(eas, true);
                }
                elt.setNbFileElements((int) allFileElts);
                //on recupere la meteo
                BaselineBean previousBaseline = BaselineSvc.getInstance().getPreviousBaseline(elt.getBaseline(), elt.getId());
                List<FactorBean> currentmarkFB = synthese.retrieveKiviat(elt, elt.getBaseline().getId());
                double currentMark = 0.0;
                for (FactorBean fb : currentmarkFB) {
                    currentMark += fb.getNote();
                    elt.addCurrentScoreToGoal(fb.getId(), fb.getNote());
                }
                currentMark = currentMark / currentmarkFB.size();
                elt.setGoalsAverage(currentMark);

                if (previousBaseline != null) {
                    List<FactorBean> previousmarkFB = synthese.retrieveKiviat(elt, previousBaseline.getId());
                    double previousMark = 0.0;
                    for (FactorBean fb : previousmarkFB) {
                        previousMark += fb.getNote();
                        elt.addPreviousScoreToGoal(fb.getId(), fb.getNote());
                    }
                    previousMark = previousMark / previousmarkFB.size();
                    elt.setPreviousGoalsAverage(previousMark);

                    if (currentMark < previousMark) {
                        if (currentMark >= 3.0) {
                            meteoImg = "few-clouds";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.lessAcceptable");
                        } else {
                            meteoImg = "storm";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.lessNotAcceptable");
                        }
                    } else if (currentMark == previousMark) {
                        if (currentMark >= 3.0) {
                            meteoImg = "clear";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.equalAcceptable");
                        } else {
                            meteoImg = "rain";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.equalNotAcceptable");
                        }
                    } else if (currentMark > previousMark) {
                        if (currentMark < 3.0) {
                            meteoImg = "overcast";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.moreNotAcceptable");
                        } else {
                            meteoImg = "clear";
                            meteoTooltip = ms.getMessage(loc, "caqs.homepage.weather.moreAcceptable");
                        }
                    }
                }
                elt.setMeteo(meteoImg);
                elt.setMeteoTooltip(meteoTooltip);
                retour.add(elt);
            }
        }
        return retour;
    }
}
