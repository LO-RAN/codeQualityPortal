package com.compuware.carscode.unifaceflow.kpi.dao;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 14 mars 2006
 * Time: 14:01:24
 * To change this template use File | Settings | File Templates.
 */
public class ProcessSummaryBean {
    private String id;
    private String name;
    private Calendar avgExecutionTime;
    private Calendar avgWaitingTime;
    private Calendar avgGlobalTime;
    private int nbExecutions;

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

    public Calendar getAvgExecutionTime() {
        return avgExecutionTime;
    }

    public int getAvgExecutionDays() {
        int result = avgExecutionTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getAvgExecutionHrs() {
        return avgExecutionTime.get(Calendar.HOUR);
    }

    public int getAvgExecutionMin() {
        return avgExecutionTime.get(Calendar.MINUTE);
    }

    public int getAvgExecutionSec() {
        return avgExecutionTime.get(Calendar.SECOND);
    }

    public void setAvgExecutionTime(Calendar avgExecutionTime) {
        this.avgExecutionTime = avgExecutionTime;
    }

    public Calendar getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public int getAvgWaitingDays() {
        int result = avgWaitingTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getAvgWaitingHrs() {
        return avgWaitingTime.get(Calendar.HOUR);
    }

    public int getAvgWaitingMin() {
        return avgWaitingTime.get(Calendar.MINUTE);
    }

    public int getAvgWaitingSec() {
        return avgWaitingTime.get(Calendar.SECOND);
    }

    public void setAvgWaitingTime(Calendar avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public Calendar getAvgGlobalTime() {
        return avgGlobalTime;
    }

    public int getAvgGlobalDays() {
        int result = avgGlobalTime.get(Calendar.DAY_OF_MONTH);
        if (result == 31) {
            result = 0;
        }
        return result;
    }

    public int getAvgGlobalHrs() {
        return avgGlobalTime.get(Calendar.HOUR);
    }

    public int getAvgGlobalMin() {
        return avgGlobalTime.get(Calendar.MINUTE);
    }

    public int getAvgGlobalSec() {
        return avgGlobalTime.get(Calendar.SECOND);
    }


    public void setAvgGlobalTime(Calendar avgGlobalTime) {
        this.avgGlobalTime = avgGlobalTime;
    }

    public int getNbExecutions() {
        return nbExecutions;
    }

    public void setNbExecutions(int nbExecutions) {
        this.nbExecutions = nbExecutions;
    }
}
