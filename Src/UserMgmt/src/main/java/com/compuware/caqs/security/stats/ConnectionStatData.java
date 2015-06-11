/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.stats;

import java.util.Calendar;

/**
 * Connection statistics including the day, the total number of connections and the maximum
 * simultaneous connections.
 * @author cwfr-fdubois
 */
public class ConnectionStatData {

    /** The day of the statistics. */
    private Calendar day;

    /** The total number of connections for the day. */
    private int sumNbUser;

    /** The maximum simultaneous connections for the day. */
    private int maxNbUser;

    /**
     * Constructor with every data as parameters.
     * @param day the statistics day.
     * @param sumNbUser the total number of connections for the day.
     * @param maxNbUser the maximum simultaneous connections for the day.
     */
    public ConnectionStatData(Calendar day, int sumNbUser, int maxNbUser) {
        this.day = day;
        this.sumNbUser = sumNbUser;
        this.maxNbUser = maxNbUser;
    }

    /**
     * Get the statistics day.
     * @return the statistics day.
     */
    public Calendar getDay() {
        return day;
    }

    /**
     * Set the statistics day.
     * @param day the statistics day.
     */
    public void setDay(Calendar day) {
        this.day = day;
    }

    /**
     * Get the maximum simultaneous connections for the day.
     * @return the maximum simultaneous connections for the day.
     */
    public int getMaxNbUser() {
        return maxNbUser;
    }

    /**
     * Set the maximum simultaneous connections for the day.
     * @param maxNbUser the maximum simultaneous connections for the day.
     */
    public void setMaxNbUser(int maxNbUser) {
        this.maxNbUser = maxNbUser;
    }

    /**
     * Get the total number of connections for the day.
     * @return the total number of connections for the day.
     */
    public int getSumNbUser() {
        return sumNbUser;
    }

    /**
     * Set the total number of connections for the day.
     * @param sumNbUser the total number of connections for the day.
     */
    public void setSumNbUser(int sumNbUser) {
        this.sumNbUser = sumNbUser;
    }

}
