package com.compuware.caqs.presentation.consult.actions;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;

public class GetFavoriteInfosAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idFav = request.getParameter("idFav");
        JSONObject retour = new JSONObject();

        if (idFav != null && !"".equals(idFav)) {
            ElementBean elt = null;
            List<ElementBean> elts = (List<ElementBean>) request.getSession().getAttribute("favoritesEltBeans");
            if (elts != null) {
                for (ElementBean e : elts) {
                    if (e.getId().equals(idFav)) {
                        elt = e;
                        break;
                    }
                }
            }
            if (elt != null) {
                ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
                Synthese synthese = new Synthese();
                if (elt != null) {
                    String datePattern = resources.getString("caqs.dateFormat.withHour");
                    SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                    String meteoImg = "";
                    String meteoTooltip = "";
                    //on recupere la meteo
                    BaselineBean previousBaseline = BaselineSvc.getInstance().getPreviousBaseline(elt.getBaseline(), elt.getId());
                    if (previousBaseline != null) {
                        Collection<FactorBean> currentmarkFB = synthese.retrieveKiviat(elt, elt.getBaseline().getId());
                        Collection<FactorBean> previousmarkFB = synthese.retrieveKiviat(elt, previousBaseline.getId());
                        double currentMark = 0.0;
                        double previousMark = 0.0;
                        for (FactorBean fb : currentmarkFB) {
                            currentMark += fb.getNote();
                        }
                        currentMark = currentMark / currentmarkFB.size();
                        for (FactorBean fb : previousmarkFB) {
                            previousMark += fb.getNote();
                        }
                        previousMark = previousMark / previousmarkFB.size();

                        if (currentMark < previousMark) {
                            if (currentMark >= 3.0) {
                                meteoImg = "few-clouds";
                                meteoTooltip = resources.getString("caqs.homepage.weather.lessAcceptable");
                            } else {
                                meteoImg = "storm";
                                meteoTooltip = resources.getString("caqs.homepage.weather.lessNotAcceptable");
                            }
                        } else if (currentMark == previousMark) {
                            if (currentMark >= 3.0) {
                                meteoImg = "clear";
                                meteoTooltip = resources.getString("caqs.homepage.weather.equalAcceptable");
                            } else {
                                meteoImg = "overcast";
                                meteoTooltip = resources.getString("caqs.homepage.weather.equalNotAcceptable");
                            }
                        } else if (currentMark > previousMark) {
                            if (currentMark < 3.0) {
                                meteoImg = "few-clouds";
                                meteoTooltip = resources.getString("caqs.homepage.weather.moreNotAcceptable");
                            } else {
                                meteoImg = "clear";
                                meteoTooltip = resources.getString("caqs.homepage.weather.moreAcceptable");
                            }
                        }
                    }
                    retour.put("meteoImg", meteoImg);
                    retour.put("meteoTooltip", meteoTooltip);
                    retour.put("eltDesc", elt.getDesc());
                    retour.put("eltLib", elt.getLib());
                    retour.put("eltId", elt.getId());
                    retour.put("prjId", elt.getProject().getId());
                    retour.put("prjLib", elt.getProject().getLib());
                    retour.put("telt", elt.getTypeElt());
                    retour.put("libTelt", new ElementType(elt.getTypeElt()).getLib(RequestUtil.getLocale(request)));
                    retour.put("blineLib", elt.getBaseline().getLib());
                    retour.put("dmaj", sdf.format(elt.getBaseline().getDmaj()));
                }
            }
        }

        return retour;
    }
}
