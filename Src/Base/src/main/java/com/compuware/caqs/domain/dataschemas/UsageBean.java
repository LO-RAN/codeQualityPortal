/*
 * ElementBean.java
 *
 * Created on 1 d�cembre 2003, 10:40
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public class UsageBean extends DefinitionBean implements Serializable, Internationalizable {

    /**
     *
     */
    private static final long serialVersionUID = 1164654446112720517L;
    private Map<String, Map<String, Double>> factorDefinition = null;
    private double lowerLimitMediumRun = 0;
    private double lowerLimitLongRun = 0;


    public UsageBean() {
    }

    public UsageBean(String id) {
        this.id = id;
    }

    public void setFactorDefinition(Map<String, Map<String, Double>> fd) {
        this.factorDefinition = fd;
    }

    public Map<String, Map<String, Double>> getFactorDefinition() {
        return this.factorDefinition;
    }

    /** Get the usage complement.
     * @param loc the locale 
     * @return the usage complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc);
    }

    /** Get the usage description.
     * @param loc the locale 
     * @return the usage description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    /** Get the usage label.
     * @param loc the locale 
     * @return the usage label.
     */
    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getBundleName() {
        return I18nUtil.MODELE_BUNDLE_NAME;
    }

    public double getLowerLimitLongRun() {
        return lowerLimitLongRun;
    }

    public void setLowerLimitLongRun(double lowerLimitLongRun) {
        this.lowerLimitLongRun = lowerLimitLongRun;
    }

    public double getLowerLimitMediumRun() {
        return lowerLimitMediumRun;
    }

    public void setLowerLimitMediumRun(double lowerLimitMediumRun) {
        this.lowerLimitMediumRun = lowerLimitMediumRun;
    }


}
