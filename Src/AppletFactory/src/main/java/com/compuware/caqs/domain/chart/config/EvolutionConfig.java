/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.chart.config;

/**
 *
 * @author cwfr-fdubois
 */
public class EvolutionConfig extends ChartConfig {

    private String rejectedLabel;
    private String reserveLabel;
    private String acceptedLabel;

    public String getAcceptedLabel() {
        return acceptedLabel;
    }

    public void setAcceptedLabel(String acceptedLabel) {
        this.acceptedLabel = acceptedLabel;
    }

    public String getRejectedLabel() {
        return rejectedLabel;
    }

    public void setRejectedLabel(String rejectedLabel) {
        this.rejectedLabel = rejectedLabel;
    }

    public String getReserveLabel() {
        return reserveLabel;
    }

    public void setReserveLabel(String reserveLabel) {
        this.reserveLabel = reserveLabel;
    }

}
