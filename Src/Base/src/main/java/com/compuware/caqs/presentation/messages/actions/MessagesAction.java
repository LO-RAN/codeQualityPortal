package com.compuware.caqs.presentation.messages.actions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.comparators.CaqsMessageBeanComparator;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatusBean;
import com.compuware.caqs.domain.dataschemas.tasks.TaskType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.CaqsMessagesUtil;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import java.util.Collections;
import java.util.Properties;

public abstract class MessagesAction extends ExtJSAjaxAction {

    protected void retrieveMessagesForUserIntoResponse(String idUser, HttpServletRequest request, HttpServletResponse response, JSONObject object, boolean warnIfTooManyMessages) {
        JSONArray array = null;
        MessagesSvc mlm = MessagesSvc.getInstance();

        int nbRunning = 0;
        int nbSuccess = 0;
        int nbFailed = 0;

        List<CaqsMessageBean> liste = mlm.getMessageList(idUser);

        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        int maxMessages=Integer.parseInt(prop.getProperty(Constants.MESSAGES_DISPLAY_LIMIT_KEY));

        if (liste != null) {
            //on tri les messages car, potentiellement, ils ne le sont pas
            Collections.sort(liste, new CaqsMessageBeanComparator(true, "dates", RequestUtil.getLocale(request)));

            JSONObject[] objects = new JSONObject[Math.min(liste.size(), maxMessages)];
            ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
            SimpleDateFormat sdf = null;
            try {
                sdf = new SimpleDateFormat(resources.getString("caqs.message.dateFormat"));
            } catch (java.util.MissingResourceException exc) {
                sdf = new SimpleDateFormat("dd/MM/yyyy");
            }

            int i = 0;

            Locale loc = RequestUtil.getLocale(request);

            for (CaqsMessageBean message : liste) {

                if (message.getStatus().equals(MessageStatus.IN_PROGRESS)) {
                    nbRunning++;
                } else if (message.getStatus().equals(MessageStatus.COMPLETED)) {
                    nbSuccess++;
                } else if (message.getStatus().equals(MessageStatus.FAILED)) {
                    nbFailed++;
                }


                // do not return more than MESSAGES_DISPLAY_LIMIT messages to the client
                if (i < maxMessages) {
                    objects[i] = new JSONObject();
                    objects[i].put("id", message.getIdMessage());
                    objects[i].put("type", message.getTask().getType());
                    objects[i].put("taskId", message.getTask().getId());
                    objects[i].put("idBline", message.getIdBline());
                    if (message.getEltBean() != null) {
                        objects[i].put("prjLib", message.getEltBean().getProject().getLib());
                        objects[i].put("eltLib", message.getEltBean().getLib());
                        objects[i].put("eltId", message.getEltBean().getId());
                    }
                    objects[i].put("status", message.getStatus().toString());
                    MessageStatusBean msb = new MessageStatusBean(message.getStatus(), message.getTask().getTaskId());
                    objects[i].put("statusLib", msb.getLib(loc));
                    if (message.getEndDate() != null) {
                        objects[i].put("endDate", sdf.format(message.getEndDate()));
                    }
                    if (TaskType.PROGRESS.equals(message.getTask().getType())) {
                        objects[i].put("percentage", message.getPercent());
                    }
                    if (message.getInfo1() != null && !message.getInfo1().isEmpty()) {
                        for (String s : message.getInfo1()) {
                            if (s.startsWith(Constants.MESSAGES_LIBPRJ_INFO1)) {
                                objects[i].put("prjLib", s.substring(Constants.MESSAGES_LIBPRJ_INFO1.length()));
                            } else if (s.startsWith(Constants.MESSAGES_ARGS_INFO1)) {
                                String sArgs = s.substring(Constants.MESSAGES_ARGS_INFO1.length());
                                String[] args = sArgs.split(",");
                                List<String> l = Arrays.asList(args);
                                objects[i].put("statusLib", msb.getLib(l, loc));
                            } else if (s.startsWith(Constants.MESSAGES_STEP_INFO1)) {
                                String step = MessagesSvc.getInstance().getStepFromInProgressMessage(s.substring(Constants.MESSAGES_STEP_INFO1.length()), RequestUtil.getCaqsResourceBundle(request));
                                if (step != null) {
                                    objects[i].put("step", step);
                                }
                                objects[i].put("step", step);
                            }
                        }
                    }
                    //un toast a afficher ????
                    String toastMsgKey = "caqs.message.toast."
                            + message.getTask().getId().toLowerCase()
                            + message.getStatus().toString().toLowerCase();
                    String toastTitleKey = "caqs.message.toast.title"
                            + message.getTask().getId().toLowerCase();
                    try {
                        String toastMsg = resources.getString(toastMsgKey);
                        String toastTitle = resources.getString(toastTitleKey);
                        if (toastMsg != null && !"".equals(toastMsg)) {
                            objects[i].put("toastMsg", toastMsg);
                            if (toastTitle == null) {
                                toastTitle = "";
                            }
                            objects[i].put("toastTitle", toastTitle);
                        }
                    } catch (MissingResourceException exc) {
                        //il n'y a pas de toast a ajouter
                    }

                    CaqsMessagesUtil.fillSpecialMessageTaskInfos(objects[i], message, request);
                    i++;
                }
            }
            array = JSONArray.fromObject(objects);
        }

        this.putArrayIntoObject(array, object);
        if (warnIfTooManyMessages) {
            // warn if more than MESSAGES_DISPLAY_LIMIT messages in the list
            object.put("tooManyMessages", (liste.size() > maxMessages));
        }

        object.put("nbRunning", nbRunning);
        object.put("nbSuccess", nbSuccess);
        object.put("nbFailed", nbFailed);


    }
}
