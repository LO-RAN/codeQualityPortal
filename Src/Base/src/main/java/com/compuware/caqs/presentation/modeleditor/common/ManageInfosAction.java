package com.compuware.caqs.presentation.modeleditor.common;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.DefinitionBean;
import com.compuware.caqs.domain.dataschemas.InternationalizationBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.InternationalizationSvc;
import com.compuware.caqs.service.LangueSvc;
import com.compuware.toolbox.util.resources.Internationalizable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class ManageInfosAction extends ExtJSAjaxAction {

    protected void fillDatesFromRequest(DefinitionBean db, HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(loc, "caqs.dateFormat"));
        db.setDmaj(new Timestamp(new Date().getTime()));
        db.setDapplication(RequestUtil.getTimestampParam(request, "dapplication", sdf));
        db.setDperemption(RequestUtil.getTimestampParam(request, "dperemption", sdf));
        db.setDinst(RequestUtil.getTimestampParam(request, "dinstallation", sdf));
    }

    protected MessagesCodes saveI18nInfosFromJSON(HttpServletRequest request,
            Internationalizable bean) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String updateString = request.getParameter("i18n");
        if (updateString != null && !"".equals(updateString)) {
            try {
                JSONArray i18nArray = JSONArray.fromObject(updateString);
                if (i18nArray != null) {
                    InternationalizationSvc i18nSvc = InternationalizationSvc.getInstance();
                    int nbI18N = i18nArray.size();
                    for (int i = 0; i < nbI18N && retour ==
                            MessagesCodes.NO_ERROR; i++) {
                        JSONObject obj = i18nArray.getJSONObject(i);
                        String fieldId = obj.getString("id");
                        JSONArray datas = obj.getJSONArray("datas");
                        int nbDatasToUpdate = datas.size();
                        InternationalizationBean i18n = null;
                        for (int j = 0; j < nbDatasToUpdate && retour ==
                                MessagesCodes.NO_ERROR; j++) {
                            JSONObject dataToUpdate = datas.getJSONObject(j);
                            i18n = new InternationalizationBean();
                            i18n.setColumnName(fieldId);
                            i18n.setTableName(bean.getBundleName());
                            i18n.setLanguageId(dataToUpdate.getString("language"));
                            i18n.setTableId(bean.getId());
                            i18n.setText(dataToUpdate.getString("text"));
                            if ("lib".equals(i18n.getColumnName())) {
                                if (i18n.getText() == null ||
                                        "".equals(i18n.getText())) {
                                    i18n.setText(i18n.getTableId());
                                }
                            }
                            retour = i18nSvc.updateData(i18n, bean);
                        }
                    }
                }
            } catch (JSONException exc) {
                mLog.error(exc);
                retour = MessagesCodes.CAQS_GENERIC_ERROR;
            }
        }
        return retour;
    }

    protected MessagesCodes deleteI18nInfosFromJSON(HttpServletRequest request,
            Internationalizable bean) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        List<LangueBean> langues = LangueSvc.getInstance().retrieveAllLanguages();
        for (LangueBean lb : langues) {
            InternationalizationBean i18n = null;
            String[] cols = {"lib", "desc", "compl"};
            for (int j = 0; j < cols.length && retour ==
                    MessagesCodes.NO_ERROR; j++) {
                i18n = new InternationalizationBean();
                i18n.setColumnName(cols[j]);
                i18n.setTableName(bean.getBundleName());
                i18n.setLanguageId(lb.getId());
                i18n.setTableId(bean.getId());
                i18n.setText(null);
                retour = InternationalizationSvc.getInstance().updateData(i18n, bean);
            }
        }
        return retour;
    }

    protected void addTimestamps(DefinitionBean elt, JSONObject retour, MessageResources resources, Locale loc) {
        SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(loc, "caqs.dateFormat"));
        if (elt != null) {
            Timestamp t = elt.getDmaj();
            if (t != null) {
                retour.put("dmaj", sdf.format(t));
            } else {
                retour.put("dmaj", "-");
            }
            t = elt.getDapplication();
            if (t != null) {
                retour.put("dapplication", sdf.format(t));
            } else {
                retour.put("dapplication", "-");
            }
            t = elt.getDperemption();
            if (t != null) {
                retour.put("dperemption", sdf.format(t));
            } else {
                retour.put("dperemption", "-");
            }
            t = elt.getDinst();
            if (t != null) {
                retour.put("dinstallation", sdf.format(t));
            } else {
                retour.put("dinstallation", "-");
            }
        } else {
            retour.put("dinstallation", "");
            retour.put("dperemption", "");
            retour.put("dapplication", "");
            retour.put("dmaj", "");
        }
    }

    protected void addLanguagesFieldsToJSON(Internationalizable bean,
            JSONObject objetRetour, String[] fields, MessageResources resources, Locale loc) {
        JSONObject obj = new JSONObject();
        JSONArray languagesDefinitions = new JSONArray();
        List<LangueBean> langues = LangueSvc.getInstance().retrieveAllLanguages();
        for (LangueBean lb : langues) {
            JSONObject langueJSON = new JSONObject();
            langueJSON.put("id", lb.getId());
            langueJSON.put("lib", lb.getLib());
            languagesDefinitions.add(langueJSON);
        }
        obj.put("languagesDefinitions", languagesDefinitions);
        JSONArray languagesDatas = new JSONArray();
        for (int i = 0; i < fields.length; i++) {
            languagesDatas.add(this.retrieveLanguageDatasArray(bean, langues, fields[i], resources, loc));
        }
        obj.put("languagesDatas", languagesDatas);
        objetRetour.put("i18n", obj);
    }

    private JSONArray retrieveLanguageDatasArray(Internationalizable bean,
            List<LangueBean> langues, String field, MessageResources resources, Locale loc) {
        JSONArray languageDatas = new JSONArray();
        languageDatas.add(field);
        languageDatas.add(resources.getMessage(loc, "caqs.modeleditor.grid." +
                field));
        for (LangueBean lb : langues) {
            languageDatas.add(this.getInternationalizedData(bean, field, lb));
        }
        return languageDatas;
    }

    private String getInternationalizedData(Internationalizable bean, String field, LangueBean langue) {
        String retour = "";
        if ("lib".equals(field)) {
            retour = (bean != null) ? bean.getLib(langue.getLocale()) : "";
        } else if ("desc".equals(field)) {
            retour = (bean != null) ? bean.getDesc(langue.getLocale()) : "";
        } else if ("compl".equals(field)) {
            retour = (bean != null) ? bean.getComplement(langue.getLocale()) : "";
        }
        return retour;
    }
}
