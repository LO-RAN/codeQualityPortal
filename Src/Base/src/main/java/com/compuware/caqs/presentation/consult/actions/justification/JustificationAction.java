/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) Fr�d�ric Dubois<p>
 * Soci�t� : Compuware<p>
 * @author Fr�d�ric Dubois
 * @version 1.0
 */
package com.compuware.caqs.presentation.consult.actions.justification;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.StringFormatUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.JustificationSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class JustificationAction extends Action {

    private static final long serialVersionUID = -2397885732541239663L;

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");
        String requestFrom = request.getParameter("from");
        if (requestFrom.equals("justificatif")) {
            performJustificatif(session, request, response);
        } else if (requestFrom.equals("justification")) {
            performJustification(session, request, response);
        }
        return null;
    }

    private void performJustificatif(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pageSuivante = request.getParameter("page");
        String idPere = request.getParameter("id_pere");
        String idElt = request.getParameter("id_elt");
        String idTelt = request.getParameter("id_telt");
        String subElt = request.getParameter("sub_elt");
        String onlyCrit = request.getParameter("only_crit");
        String critList = request.getParameter("crit_list");
        String libElt = request.getParameter("lib_elt");
        String idBline = request.getParameter("id_bline");
        String idPro = request.getParameter("id_pro");
        String idCrit = request.getParameter("id_crit");
        String libCrit = request.getParameter("lib_crit");
        String idFac = request.getParameter("id_fac");
        String idMet = request.getParameter("id_met");

        String action = request.getParameter("action");

        String error = null;
        if (!"cancel".equals(action)) {
            String idJust = request.getParameter("id_just");
            String cuser = request.getParameter("cuser");
            String statut = request.getParameter("statut");
            String justLib = request.getParameter("just_lib");
            String justDesc = request.getParameter("just_desc");
            justDesc = StringFormatUtil.replaceCarriageReturnByHTML(justDesc);
            String justNotecalc = request.getParameter("just_notecalc");
            if ("valid".equals(action)) {
                double justNote = justNotecalc == null ? 0.0 : Double.parseDouble(justNotecalc);
                String update = request.getParameter("update");
                boolean updflg = false;
                if (update != null) {
                    updflg = true;
                }
                JustificationBean just = new JustificationBean(idJust, justNote, justLib, justDesc, statut, cuser, null, null);
                JustificationSvc justifSvc = JustificationSvc.getInstance();
                try {
                    if (idCrit != null) {
                        justifSvc.setJustification(idElt, idBline, idCrit, just, updflg);
                    } else if (idMet != null) {
                        justifSvc.setJustification(idElt, idBline, idMet, just, updflg);
                    }
                    if ("DEMAND".equals(statut)) {
                        List<CaqsMessageBean> liste = MessagesSvc.getInstance().getSpecificTasksForUserAndEltId(TaskId.NEW_JUSTIF,
                                idElt, idBline, idJust, RequestUtil.getConnectedUserId(request));
                        if (liste != null && !liste.isEmpty()) {
                            for (CaqsMessageBean message : liste) {
                                MessagesSvc.getInstance().setMessageAsSeen(message.getIdMessage());
                            }
                        }

                        List<String> l = new ArrayList<String>();
                        l.add(Constants.MESSAGES_ID_JUST_INFO1+idJust);
                        MessagesSvc.getInstance().addMessageAndSetCompleted(TaskId.NEW_JUSTIF,
                                idElt, idBline, l, idJust, RequestUtil.getConnectedUserId(request));
                    }
                } catch (CaqsException e) {
                    error = e.getMessage();
                }
            }
        }
        String redir = pageSuivante + "?id_fac=" + idFac + "&id_crit=" + idCrit +
                "&lib_crit=" + libCrit + "&id_elt=" + idPere + "&lib_elt=" +
                libElt + "&id_bline=" + idBline + "&id_pro=" + idPro +
                "&id_telt=" + idTelt + "&sub_elt=" + subElt + "&only_crit=" +
                onlyCrit + "&crit_list=" + critList;
        if (error != null) {
            redir += "&error=" + error;
        }
        response.sendRedirect(redir);

    }

    private void performJustification(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String req = request.getParameter("req");
        String error = null;

        String action = request.getParameter("action");

        if (!"cancel".equals(action)) {
            String justNotecalc = request.getParameter("note_just");
            double justNote = justNotecalc == null ? 0.0 : Double.parseDouble(justNotecalc);

            String statut;
            if ("valid".equals(action)) {
                statut = "VALID";
            } else {
                if ("cancel_justif".equals(action)) {
                    statut = "CANCEL";
                } else {
                    statut = "REJET";
                    justNote = 0.0;
                }
            }

            String cuser = request.getParameter("cuser");

            String idElt = request.getParameter("id_elt");
            String idBline = request.getParameter("id_bline");
            String idCritfac = request.getParameter("id_critfac");
            String dbtable = request.getParameter("dbtable");

            String idJustOld = request.getParameter("id_just");
            String justLib = request.getParameter("just_lib");
            String justDescValid = request.getParameter("just_desc_valid");
            String libElt = request.getParameter("lib_elt");

            JustificationSvc justifSvc = JustificationSvc.getInstance();
            try {
                //si le pr�c�dent message indiquant que la justif a �t� demand�e n'est toujours pas lu,
                //on le place comme lu afin que seulement celui qui va �tre cr�� apparaisse
                List<CaqsMessageBean> liste = MessagesSvc.getInstance().getSpecificTasksForUserAndEltId(TaskId.NEW_JUSTIF,
                        idElt, idBline, idJustOld, RequestUtil.getConnectedUserId(request));
                if (liste != null && !liste.isEmpty()) {
                    for (CaqsMessageBean message : liste) {
                        MessagesSvc.getInstance().setMessageAsSeen(message.getIdMessage());
                    }
                }

                if (!"CANCEL".equals(statut)) {
                    JustificationBean just = new JustificationBean(justNote, justLib, justDescValid, statut, cuser);
                    justifSvc.setJustification(idElt, idBline, idCritfac, just, false);
                    justifSvc.linkJustificatifs(idJustOld, just.getId());
                    List<String> l = new ArrayList<String>();
                    l.add(Constants.MESSAGES_ID_JUST_INFO1+just.getId());

                    TaskId taskid = null;
                    if ("VALID".equals(statut)) {
                        taskid = TaskId.JUSTIF_ACCEPTED;
                    } else {
                        taskid = TaskId.JUSTIF_REJECTED;
                    }
                    MessagesSvc.getInstance().addMessageAndSetCompleted(taskid, idElt, idBline, l,
                            just.getId(), RequestUtil.getConnectedUserId(request));
                } else {
                    List<String> l = new ArrayList<String>();
                    l.add(Constants.MESSAGES_ID_JUST_INFO1+idJustOld);
                    MessagesSvc.getInstance().addMessageAndSetCompleted(TaskId.JUSTIF_CANCELLED, idElt, idBline, l,
                            idJustOld, RequestUtil.getConnectedUserId(request));
                    //on retire la validation ou le rejet
                    justifSvc.removeJustificatif(idElt, idBline, idCritfac, idJustOld);
                }
            } catch (CaqsException e) {
                error = e.getMessage();
            }

        }
        String redir = "JustificationListRetrieve.do?req=" + req;
        if (error != null) {
            redir += "&error=" + error;
        }
        response.sendRedirect(redir);

    }
}
