package com.compuware.carscode.unifaceflow.kpi.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 13 mars 2006
 * Time: 15:26:38
 * To change this template use File | Settings | File Templates.
 */
public class ProcessTaskBean {
    private String id;
    private String name;
    private String state;
    private String type;

    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp endTime;

    private Calendar executionTime;
    private Calendar waitingTime;
    private Calendar globalTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public String getStartTimeStr() {
        return getFormattedDate(this.startTime);
    }

    public String getFormattedDate(Timestamp d) {
        String result = null;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        result = df.format(d);
        return result;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getEndTimeStr() {
        return getFormattedDate(endTime);
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public String getCreateTimeStr() {
        return getFormattedDate(createTime);
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Calendar getExecutionTime() {
        return executionTime;
    }

    public int getExecutionDays() {
        int result = executionTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getExecutionHrs() {
        return executionTime.get(Calendar.HOUR);
    }

    public int getExecutionMin() {
        return executionTime.get(Calendar.MINUTE);
    }

    public int getExecutionSec() {
        return executionTime.get(Calendar.SECOND);
    }

    public void setExecutionTime(Calendar executionTime) {
        this.executionTime = executionTime;
    }

    public Calendar getWaitingTime() {
        return waitingTime;
    }

    public int getWaitingDays() {
        int result = waitingTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getWaitingHrs() {
        return waitingTime.get(Calendar.HOUR);
    }

    public int getWaitingMin() {
        return waitingTime.get(Calendar.MINUTE);
    }

    public int getWaitingSec() {
        return waitingTime.get(Calendar.SECOND);
    }

    public void setWaitingTime(Calendar waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Calendar getGlobalTime() {
        return globalTime;
    }

    public int getGlobalDays() {
        int result = globalTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getGlobalHrs() {
        return globalTime.get(Calendar.HOUR);
    }

    public int getGlobalMin() {
        return globalTime.get(Calendar.MINUTE);
    }

    public int getGlobalSec() {
        return globalTime.get(Calendar.SECOND);
    }


    public void setGlobalTime(Calendar globalTime) {
        this.globalTime = globalTime;
    }
}
