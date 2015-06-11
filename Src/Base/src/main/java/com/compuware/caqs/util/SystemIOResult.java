/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cwfr-fdubois
 */
public class SystemIOResult {

    /** The system IO result code. */
    private int resultCode;

    /** The search string map result. */
    private Map<StreamStringSearchKey,Boolean> stringSearchResult = new HashMap<StreamStringSearchKey,Boolean>();

    /**
     * Get the value of resultCode
     *
     * @return the value of resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * Set the value of resultCode
     *
     * @param resultCode new value of resultCode
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Get the value of stringSearchResult
     *
     * @return the value of stringSearchResult
     */
    public Map<StreamStringSearchKey,Boolean> getStringSearchResult() {
        return stringSearchResult;
    }

    /**
     * Add the value of stringSearchResult.
     * If a result already exists for the given key with the value true, does not
     * change that value.
     * @param newSearchStringResult new value of newSearchStringResult
     */
    public void addStringSearchResult(Map<StreamStringSearchKey,Boolean> newSearchStringResult) {
        for (StreamStringSearchKey searchStringKey: newSearchStringResult.keySet()) {
            Boolean value = this.stringSearchResult.get(searchStringKey);
            if ((value != null && value.equals(Boolean.FALSE)) || value == null) {
                value = newSearchStringResult.get(searchStringKey);
            }
            this.stringSearchResult.put(searchStringKey, value);
        }
    }


}
