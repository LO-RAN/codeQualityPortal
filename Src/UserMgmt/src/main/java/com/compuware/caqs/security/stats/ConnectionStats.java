/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.stats;

import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The connection statistic manager.
 * @author cwfr-fdubois
 */
public final class ConnectionStats {

    /** The connection statistic manager unique instance. */
    private static ConnectionStats singleton = new ConnectionStats();

    /** RW reentrant lock. */
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    /** Lock for read access. */
    private final Lock r = rwl.readLock();
    /** Lock for write access. */
    private final Lock w = rwl.writeLock();

    /** The current day. */
    private Calendar currentDay;

    /** The next day. */
    private Calendar nextDay;

    /** The current number of connections. */
    private int currentNbUser;

    /** The total number of connections for the day. */
    private int sumNbUser;

    /** The maximum simultaneous connections for the day. */
    private int maxNbUser;

    /** Private constructor to instanciate the unique instance. */
    private ConnectionStats() {
        this.currentDay = Calendar.getInstance();
        this.currentDay.set(Calendar.HOUR_OF_DAY, 0);
        this.currentDay.set(Calendar.MINUTE, 0);
        this.currentDay.set(Calendar.SECOND, 0);
        this.currentDay.set(Calendar.MILLISECOND, 0);
        this.nextDay = (Calendar)this.currentDay.clone();
        this.nextDay.add(Calendar.DAY_OF_MONTH, 1);
    }

    /**
     * Get access to the unique connection manager instance.
     * @return the unique connection manager instance.
     */
    public static ConnectionStats getInstance() {
        return singleton;
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

    /**
     * Get the current number of connected users.
     * @return the current number of connected users.
     */
    public int getCurrentNbUser() {
        return currentNbUser;
    }

    /**
     * Increment the number of connected users.
     */
    public void incUser(boolean sameUser) {
        w.lock();
        try {
            this.currentNbUser++;
            updateStats();
            this.sumNbUser++;
            if (!sameUser && this.maxNbUser < this.currentNbUser) {
                this.maxNbUser = this.currentNbUser;
            }
        }
        finally {
            w.unlock();
        }
    }

    /**
     * Decrement the number of connected users.
     */
    public void decUser() {
        w.lock();
        try {
            this.currentNbUser--;
            updateStats();
        }
        finally {
            w.unlock();
        }
    }

    /**
     * Update the statistics.
     */
    private void updateStats() {
        Calendar cal = Calendar.getInstance();
        if (cal.after(this.nextDay)) {
            this.currentNbUser = 0;
            this.maxNbUser = 0;
            this.sumNbUser = 0;
            this.currentDay = (Calendar)this.nextDay.clone();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            this.nextDay = cal;
        }
    }

    /**
     * Get the statistic data for the current day.
     * @return the statistic data for the current day.
     */
    public ConnectionStatData getStatData() {
        ConnectionStatData result = null;
        r.lock();
        try {
            this.updateStats();
            result = new ConnectionStatData((Calendar)this.currentDay.clone(), this.sumNbUser, this.maxNbUser);
        }
        finally {
            r.unlock();
        }
        return result;
    }

    /**
     * Set the statistic data from the given parameter the they are for the current day.
     * @param newData the new statistic data.
     */
    public void setStatData(ConnectionStatData newData) {
        if (newData != null && newData.getDay() != null && this.currentDay.equals(newData.getDay())) {
            this.currentDay = newData.getDay();
            this.maxNbUser = newData.getMaxNbUser();
            this.sumNbUser = newData.getSumNbUser();
            this.nextDay = (Calendar)this.currentDay.clone();
            this.nextDay.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

}
