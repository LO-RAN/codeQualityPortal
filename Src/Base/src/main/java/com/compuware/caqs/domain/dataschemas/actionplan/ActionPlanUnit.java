package com.compuware.caqs.domain.dataschemas.actionplan;

import java.io.Serializable;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class ActionPlanUnit implements Internationalizable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6715248433838729177L;
    private String id;
    private int nbUnits;

    public ActionPlanUnit() {
        this.nbUnits = 0;
    }

    public ActionPlanUnit(String id) {
        this.nbUnits = 0;
        this.id = id;
    }

    public String getComplement(Locale loc) {
        return null;
    }

    public String getDesc(Locale loc) {
        return null;
    }

    public String getId() {
        return this.id;
    }

    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getLib(loc), quote, bracket, doubleQuote);
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() +
                Internationalizable.KEY_SEPARATOR +
                columnName +
                Internationalizable.KEY_SEPARATOR + getId();
    }

    public int getNbUnits() {
        return nbUnits;
    }

    public void setNbUnits(int nbUnits) {
        this.nbUnits = nbUnits;
    }

    public String getBundleName() {
        return I18nUtil.ACTION_PLAN_UNIT_BUNDLE_NAME;
    }
}
