package com.compuware.caqs.presentation.consult.actions.dashboard;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.compuware.caqs.security.stats.ConnectionStatData;
import com.compuware.caqs.service.SecuritySvc;
import java.util.Calendar;

/**
 *
 * @author cwfr-dzysman
 */
public class TimeplotConnectionStatsAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = this.retrieveDatas();
        return obj;
    }

    private JSONObject retrieveDatas() {
        JSONObject retour = new JSONObject();
        MessagesCodes messagesCode = MessagesCodes.NO_ERROR;

        List<ConnectionStatData> datas = null;
        try {
            datas = SecuritySvc.getInstance().retrieveAllStatistics();
        } catch (CaqsException exc) {
            mLog.error("Error while retrieving connections stats", exc);
            messagesCode = MessagesCodes.DATABASE_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, messagesCode);
        if (messagesCode == MessagesCodes.NO_ERROR) {
            JSONArray globalDataArray = new JSONArray();

            JSONArray array = new JSONArray();
            ConnectionStatData previousData = null;
            for (ConnectionStatData data : datas) {
                if (previousData != null) {
                    //tant qu'il y a plus d'un jour d'ecart entre la date
                    //precedente et celle-ci
                    while(previousData.getDay().get(Calendar.DAY_OF_YEAR) != (data.getDay().get(Calendar.DAY_OF_YEAR)-1)) {
                        previousData.getDay().add(Calendar.DAY_OF_YEAR, 1);
                        array.add(this.createJSONData(previousData.getDay(), 0));
                    }
                }
                array.add(this.createJSONData(data.getDay(), data.getSumNbUser()));
                previousData = data;
            }
            globalDataArray.add(array);

            array = new JSONArray();
            previousData = null;
            for (ConnectionStatData data : datas) {
                if (previousData != null) {
                    //tant qu'il y a plus d'un jour d'ecart entre la date
                    //precedente et celle-ci
                    while(previousData.getDay().get(Calendar.DAY_OF_YEAR) != (data.getDay().get(Calendar.DAY_OF_YEAR)-1)) {
                        previousData.getDay().add(Calendar.DAY_OF_YEAR, 1);
                        array.add(this.createJSONData(previousData.getDay(), 0));
                    }
                }
                array.add(this.createJSONData(data.getDay(), data.getMaxNbUser()));
                previousData = data;
            }
            globalDataArray.add(array);

            retour.put("datas", globalDataArray);
        }

        return retour;
    }

    private JSONArray createJSONData(Calendar date, int value) {
        JSONArray obj = new JSONArray();
        obj.add(date.getTimeInMillis());
        obj.add(value);
        return obj;
    }
}
