/*
 * Class.java
 *
 * Created on 27 janvier 2004, 15:17
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public class CriterionDefinition extends DefinitionBean implements Serializable, Internationalizable {

    private static final long serialVersionUID = -8922198729272222485L;
    private double weight;
    private ElementType elementType;
    private List<MetriqueDefinitionBean> metriquesDefs = null;
    private boolean isInActionPlan = false;

    /** Creates a new instance of Class */
    public CriterionDefinition() {
    }

    /** Creates a new instance of Class */
    public CriterionDefinition(String id) {
        this.id = id;
    }

    public double getWeight() {
        return this.weight;
    }

    public ElementType getElementType() {
        return this.elementType;
    }

    public String getIdTElt() {
        return (this.elementType!=null) ? this.elementType.getId() : null;
    }

    public List<MetriqueDefinitionBean> getMetriquesDefinitions() {
        return this.metriquesDefs;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setIdTElt(String idTelt) {
        this.elementType = new ElementType(idTelt);
    }

    public void setMetriquesDefinitions(List<MetriqueDefinitionBean> pMetriquesDefs) {
        this.metriquesDefs = pMetriquesDefs;
    }

    public boolean hasDetail() {
        boolean result = false;
        if (this.id.matches("R\\d[\\d]?")) {
            result = true;
        }
        return result;
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getLib(loc), quote, bracket, doubleQuote);
    }

    /** Get the criterion label.
     * @param loc the locale 
     * @return the criterion label.
     */
    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getDesc(loc), quote, bracket, doubleQuote);
    }

    /** Get the criterion description.
     * @param loc the locale 
     * @return the criterion description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    public String getEscapedComplement(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
        return StringFormatUtil.escapeString(this.getComplement(loc), quote, bracket, doubleQuote);
    }

    /** Get the criterion complement.
     * @param loc the locale 
     * @return the criterion complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc, "");
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append(", WEIGHT=" + this.weight);
        buffer.append(", ID_TELT=" + ((this.elementType!=null)?this.elementType.getId():null));
        if (this.metriquesDefs != null) {
            buffer.append("[");
            Iterator<MetriqueDefinitionBean> i = this.metriquesDefs.iterator();
            while (i.hasNext()) {
                MetriqueDefinitionBean md = i.next();
                buffer.append(md);
            }
            buffer.append("]");
        }
        buffer.append("]");
        return buffer.toString();
    }

    public boolean isInActionPlan() {
        return isInActionPlan;
    }

    public void setInActionPlan(boolean pIsInActionPlan) {
        this.isInActionPlan = pIsInActionPlan;
    }

    public String getBundleName() {
        return I18nUtil.CRITERE_BUNDLE_NAME;
    }
}
