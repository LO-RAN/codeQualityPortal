/*
 * RetrieveEvolutionGraphDatasAction.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.evolution;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.EvolutionBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.EvolutionSvc;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-dzysman
 */
public class RetrieveEvolutionGraphDatasAction extends ExtJSAjaxAction {

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
        String target = request.getParameter("target");
        Collection<BaselineBean> baselines = null;
        Collection<EvolutionBean> categories = null;

        try {
            ElementBean eltBean = ElementSvc.getInstance().retrieveElement(idElt, idBline);
            baselines = evolutionSvc.retrieveBaselines(eltBean);
            categories = evolutionSvc.retrieveEvolution(eltBean, target);
        } catch (CaqsException exc) {
            mLog.error(exc.getMessage());
            messagesCode = MessagesCodes.DATABASE_ERROR;
        }

        if (messagesCode == MessagesCodes.NO_ERROR) {
            JSONArray globalDataArray = new JSONArray();
            for (EvolutionBean category : categories) {
                JSONObject serie = this.fillCategorySerie(category, baselines, loc);
                globalDataArray.add(serie);
            }
            retour.put("datas", globalDataArray);

            globalDataArray = new JSONArray();
            int i = 0;
            int increment = (baselines.size()>10)?(baselines.size() / 10) : 1;
            for (BaselineBean baseline : baselines) {
                JSONArray tick = new JSONArray();
                tick.add(i);
                tick.add((i%increment==0)?baseline.getLib():"");
                globalDataArray.add(tick);
                i++;
            }
            retour.put("xaxisTicks", globalDataArray);

        }
        this.fillJSONObjectWithReturnCode(retour, messagesCode);

        return retour;
    }

    private JSONObject fillCategorySerie(EvolutionBean category, Collection<BaselineBean> baselines, Locale loc) {
        JSONObject serie = new JSONObject();
        serie.put("label", category.getLibWithoutTruncate(loc));
        JSONArray array = new JSONArray();
        int i = 0;
        for (BaselineBean baseline : baselines) {
            JSONArray obj = new JSONArray();
            obj.add(i);
            double d = (category.getDoubleEntry(baseline.getId()) - 0.005);
            obj.add(d);
            obj.add(0);
            obj.add(baseline.getDmaj().getTime());
            obj.add(baseline.getLib());
            array.add(obj);
            i++;
        }
        serie.put("data", array);
        return serie;
    }
}
