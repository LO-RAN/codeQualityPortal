package com.compuware.caqs.domain.dataschemas.tasks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.ElementBean;

public class CaqsMessageBean {

    public static final String INFO1_SEPARATOR = Character.toString((char)0X1e);

    private String idMessage = null;
    private ElementBean elt = null;
    private String idBline = null;
    private String idUser = null;
    private TaskBean task = null;
    private MessageStatus status = MessageStatus.NOT_STARTED;
    private Timestamp beginDate = null;
    private Timestamp endDate = null;
    private float percent = 0.0f;
    private boolean seen = false;
    private List<String> info1 = null;
    private String otherId = null;

    public List<String> getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = new ArrayList<String>();
        if (info1 != null) {
            String[] infos = info1.split(INFO1_SEPARATOR);
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    this.info1.add(infos[i]);
                }
            }
        }
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public ElementBean getEltBean() {
        return elt;
    }

    public void setEltBean(ElementBean idElt) {
        this.elt = idElt;
    }

    public String getIdBline() {
        return idBline;
    }

    public void setIdBline(String idBline) {
        this.idBline = idBline;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public TaskBean getTask() {
        return task;
    }

    public void setTask(TaskBean task) {
        this.task = task;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }
}
