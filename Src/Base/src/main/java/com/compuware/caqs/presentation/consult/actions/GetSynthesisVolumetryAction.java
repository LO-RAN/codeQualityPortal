package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.util.StringFormatUtil;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.JustificationSvc;
import com.compuware.caqs.service.SyntheseSvc;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import org.apache.struts.util.MessageResources;

public class GetSynthesisVolumetryAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        JSONObject obj = this.doRetrieve(request, request.getSession());
        return obj;
    }

    private JSONObject doRetrieve(HttpServletRequest request,
            HttpSession session) {
        JSONObject retour = new JSONObject();

        ElementBean eltBean = null;
        //les recuperations de idelt, idbline et idpro sont specifiques
        //a l'affichage de la synthese du topdown dans la labellisation
        String idElt = request.getParameter("id_elt");
        if(idElt != null && !"".equals(idElt)) {
            eltBean = new ElementBean();
            eltBean.setId(idElt);
        } else {
            eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        }
        String idBline = request.getParameter("baselineId");
        if(idBline != null && !"".equals(idBline)) {
            BaselineBean bb = new BaselineBean();
            bb.setId(idBline);
            eltBean.setBaseline(bb);
        }
        String idPro = request.getParameter("id_pro");
        if(idPro != null && !"".equals(idPro)) {
            ProjectBean prj = new ProjectBean();
            prj.setId(idPro);
            eltBean.setProject(prj);
            eltBean.getBaseline().setProject(prj);
        }
        if (eltBean != null) {
            retour.put("telt", eltBean.getTypeElt());
            retour.put("isEA", ElementType.EA.equals(eltBean.getTypeElt()));
            BaselineBean previousBaseline = BaselineSvc.getInstance().getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());

            //recuperation de la volumetrie
            this.retrieveVolumetrie(request, eltBean, retour, previousBaseline);

            //recuperation des justifications
            this.retrieveJustifications(eltBean, retour);
        }
        return retour;
    }

    private void retrieveVolumetrie(HttpServletRequest request,
            ElementBean eltBean, JSONObject object, BaselineBean previousBaseline) {
        MessageResources resources = RequestUtil.getResources(request);
        SyntheseSvc syntheseSvc = SyntheseSvc.getInstance();
        Map<String, Double> volumetryMetrics = syntheseSvc.retrieveVolumetryMetrics(eltBean, eltBean.getBaseline().getId());
        Map<String, Double> previousVolumetryMetrics = null;
        if (previousBaseline != null) {
            previousVolumetryMetrics = syntheseSvc.retrieveVolumetryMetrics(eltBean, previousBaseline.getId());
        }
        List<Volumetry> vols = syntheseSvc.retrieveVolumetry(eltBean);

        if (volumetryMetrics != null && !volumetryMetrics.isEmpty()) {

            Locale locale = RequestUtil.getLocale(request);
            NumberFormat nf = StringFormatUtil.getIntegerFormatter(locale);
            NumberFormat percentFormatter = StringFormatUtil.getFormatter(locale,
                    0, 2);

            Properties prop = RequestUtil.getWebResources(request.getSession());
            DialecteBean dialecte = eltBean.getDialecte();
            String idLang = (dialecte != null ? dialecte.getLangage().getId() : "obj");
            String methodLib = (prop != null) ? resources.getMessage(locale, "caqs." + prop.getProperty(idLang
                    + ".method")) : "";

            Double allCode = (Double) volumetryMetrics.get("ALL_CODE");
            Double pctComments = (Double) volumetryMetrics.get("PCT_COMMENTS");
            Double complexDest = (Double) volumetryMetrics.get("COMPLEX_DEST");
            Double allCodeMet = (Double) volumetryMetrics.get("ALL_CODE_MET");
            Double ifpug = (Double) volumetryMetrics.get("IFPUG");
            Double prevAllCode = null;
            Double prevPctComments = null;
            Double prevComplexDest = null;
            Double prevAllCodeMet = null;
            Double prevIfpug = null;
            if (previousVolumetryMetrics != null) {
                prevAllCode = (Double) previousVolumetryMetrics.get("ALL_CODE");
                prevPctComments = (Double) previousVolumetryMetrics.get("PCT_COMMENTS");
                prevComplexDest = (Double) previousVolumetryMetrics.get("COMPLEX_DEST");
                prevAllCodeMet = (Double) previousVolumetryMetrics.get("ALL_CODE_MET");
                prevIfpug = (Double) previousVolumetryMetrics.get("IFPUG");
            }

            if (ifpug != null) {
                object.put("ifpug", true);
                object.put("ifpug_format", nf.format(ifpug.doubleValue()));
                if (prevIfpug != null) {
                    object.put("prev_ifpug", true);
                    double diff = ifpug.doubleValue() - prevIfpug.doubleValue();
                    object.put("prev_ifpug_format", ((diff > 0) ? "+" : "")
                            + nf.format(diff));
                }
            }

            if (allCode != null) {
                object.put("all_code", true);
                object.put("all_code_format", nf.format(allCode.doubleValue()));
                if (prevAllCode != null) {
                    object.put("prev_all_code", true);
                    double diff = allCode.doubleValue()
                            - prevAllCode.doubleValue();
                    object.put("prev_all_code_format", ((diff > 0) ? "+" : "")
                            + nf.format(diff));
                }
            }

            if (pctComments != null) {
                object.put("pct_comments", true);
                object.put("pct_txt", resources.getMessage(locale, "caqs.synthese.commentaires", methodLib));
                object.put("pct_comments_format", nf.format(pctComments.doubleValue()));
                if (prevPctComments != null) {
                    object.put("prev_pct_comments", true);
                    double diff = pctComments.doubleValue()
                            - prevPctComments.doubleValue();
                    object.put("prev_pct_comments_format", ((diff > 0) ? "+" : "")
                            + nf.format(diff));
                }
            }

            if (complexDest != null && allCodeMet != null && allCodeMet.doubleValue()
                    != 0.0) {
                object.put("complex_dest", true);
                object.put("complex_dest_format", percentFormatter.format(complexDest.doubleValue()
                        * 100 / allCodeMet.doubleValue()) + "%");
                if (prevComplexDest != null && prevAllCodeMet != null && prevAllCodeMet.doubleValue()
                        != 0.0) {
                    object.put("prev_complex_dest", true);
                    double diff = (complexDest.doubleValue()
                            * 100 / allCodeMet.doubleValue()) - (prevComplexDest.doubleValue()
                            * 100 / prevAllCodeMet.doubleValue());
                    object.put("prev_complex_dest_format", ((diff > 0) ? "+" : "")
                            + percentFormatter.format(diff) + "%");
                }
            }

            if (vols != null) {
                JSONObject[] objects = new JSONObject[vols.size()];
                int i = 0;
                for (Iterator it = vols.iterator(); it.hasNext();) {
                    Volumetry vol = (Volumetry) it.next();
                    objects[i] = new JSONObject();
                    objects[i].put("label", resources.getMessage(locale, "caqs.synthese.nb", vol.getElementType().getLib(locale)));
                    objects[i].put("total", nf.format(vol.getTotal()));
                    String msg = resources.getMessage(locale, "caqs.synthese.creessupp", vol.getCreated(), vol.getDeleted());
                    msg = "<I>&nbsp;&nbsp;" + msg + "</I>";
                    objects[i].put("creesup", msg);
                    i++;
                }
                JSONArray array = JSONArray.fromObject(objects);
                object.put("volumetry", array);
            }

        }
    }

    private void retrieveJustifications(
            ElementBean eltBean,
            JSONObject object) {
        JustificationSvc justificationSvc = JustificationSvc.getInstance();
        //les demandes
        int nbDemandes = 0;
        try {
            nbDemandes += justificationSvc.getNbCriterionJustificationsForElement("DEMAND",
                    eltBean.getProject().getId(), eltBean.getBaseline().getId(), eltBean.getId());
        } catch (CaqsException e) {
            mLog.error("retrieveJustifications:" + e.getMessage());
        }
        object.put("justifDemand", "" + nbDemandes);

        //les validees
        int nbValid = 0;
        try {
            nbValid += justificationSvc.getNbCriterionJustificationsForElement("VALID",
                    eltBean.getProject().getId(), eltBean.getBaseline().getId(), eltBean.getId());
        } catch (CaqsException e) {
            mLog.error("retrieveJustifications:" + e.getMessage());
        }
        object.put("justifValid", "" + nbValid);

        //les refusees
        int nbRefus = 0;
        try {
            nbRefus = justificationSvc.getNbCriterionJustificationsForElement("REJET",
                    eltBean.getProject().getId(), eltBean.getBaseline().getId(), eltBean.getId());
        } catch (CaqsException e) {
            mLog.error("retrieveJustifications:" + e.getMessage());
        }
        object.put("justifRejet", "" + nbRefus);
    }
}
