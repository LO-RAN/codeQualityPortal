/*
 * RetrieveEvolutionGraphDatasAction.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.evolution;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.EvolutionSvc;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Properties;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * @author cwfr-dzysman
 */
public class RetrieveVolumetryEvolutionGraphDatasAction extends ExtJSAjaxAction {

    private static final String[] CATEGORIES = {
        "ALL_CODE",
        "PCT_COMMENTS",
        "COMPLEX_DEST",
        "IFPUG"
    };

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

        EvolutionSvc evolutionSvc = EvolutionSvc.getInstance();

        String idElt = request.getParameter("idElt");
        String idBline = request.getParameter("idBline");
        Collection<BaselineBean> baselines = null;
        Map<String, Map<String, Double>> categories = null;
        ElementBean eltBean = null;

        try {
            eltBean = ElementSvc.getInstance().retrieveElement(idElt, idBline);
            if (eltBean != null) {
                baselines = evolutionSvc.retrieveBaselines(eltBean);
                categories = evolutionSvc.retrieveVolumetryMetricsEvolution(eltBean);
            }
        } catch (CaqsException exc) {
            mLog.error(exc.getMessage());
            messagesCode = MessagesCodes.DATABASE_ERROR;
        }
        if (baselines == null || categories == null) {
            messagesCode = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        if (messagesCode == MessagesCodes.NO_ERROR) {
            JSONArray globalDataArray = new JSONArray();
            for (int i = 0; i < CATEGORIES.length; i++) {
                JSONObject serie = this.fillCategorySerie(eltBean, categories,
                        baselines, CATEGORIES[i], loc, request);
                globalDataArray.add(serie);
            }
            retour.put("datas", globalDataArray);

            globalDataArray = new JSONArray();
            int i = 0;
            int increment = (baselines.size() > 10) ? (baselines.size() / 10) : 1;
            for (BaselineBean baseline : baselines) {
                JSONArray tick = new JSONArray();
                tick.add(i);
                tick.add((i % increment == 0) ? baseline.getLib() : "");
                globalDataArray.add(tick);
                i++;
            }
            retour.put("xaxisTicks", globalDataArray);

        }
        this.fillJSONObjectWithReturnCode(retour, messagesCode);

        return retour;
    }

    private JSONObject fillCategorySerie(ElementBean eltBean,
            Map<String, Map<String, Double>> categories,
            Collection<BaselineBean> baselines, String serieId, Locale loc,
            HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);
        JSONObject serie = new JSONObject();
        if ("PCT_COMMENTS".equals(serieId)) {
            Properties prop = RequestUtil.getWebResources(request.getSession());
            DialecteBean dialecte = eltBean.getDialecte();
            String idLang = (dialecte != null ? dialecte.getLangage().getId() : "obj");
            String methodLib = (prop != null) ? resources.getMessage(loc, "caqs." + prop.getProperty(idLang
                    + ".method")) : "";
            serie.put("label", resources.getMessage(loc, "caqs.evolution.volumetry.PCT_COMMENTS", methodLib));
        } else {
            serie.put("label", resources.getMessage(loc, "caqs.evolution.volumetry."
                    + serieId));
        }
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        NumberFormat percentFormatter = StringFormatUtil.getFormatter(loc,
                0, 2);
        JSONArray array = new JSONArray();
        int i = 0;
        for (BaselineBean baseline : baselines) {
            Map<String, Double> category = categories.get(baseline.getId());
            String value = "0";
            double d = 0.0;
            if (category != null) {
                Double val = category.get(serieId);
                if (val != null) {
                    if ("COMPLEX_DEST".equals(serieId)) {
                        Double allCodeMet = category.get("ALL_CODE_MET");
                        if (allCodeMet != null) {
                            d = val.doubleValue() * 100
                                    / allCodeMet.doubleValue();
                        }
                    } else {
                        d = val.doubleValue();
                    }
                    if ("COMPLEX_DEST".equals(serieId)
                            || "PCT_COMMENTS".equals(serieId)) {
                        value = percentFormatter.format(d) + "%";
                        serie.put("yaxis", 2);
                    } else {
                        value = nf.format(d);
                    }
                }
            }
            JSONArray obj = new JSONArray();
            obj.add(i);
            obj.add(d);
            obj.add(0);
            obj.add(baseline.getDmaj().getTime());
            obj.add(value);
            obj.add(baseline.getLib());
            array.add(obj);
            i++;

        }
        serie.put("data", array);
        return serie;
    }
}
