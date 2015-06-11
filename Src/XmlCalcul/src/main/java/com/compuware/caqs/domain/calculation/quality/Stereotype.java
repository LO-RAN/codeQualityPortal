package com.compuware.caqs.domain.calculation.quality;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 8 févr. 2006
 * Time: 11:55:57
 * To change this template use File | Settings | File Templates.
 */
public class Stereotype {

    Collection<String> includedStereotypes = new ArrayList<String>();
    Collection<String> excludedStereotypes = new ArrayList<String>();

    public Stereotype() {
    }

    public Collection<String> getIncludedStereotypes() {
        return includedStereotypes;
    }

    public void setIncludedStereotypes(Collection<String> includedStereotypes) {
        this.includedStereotypes = includedStereotypes;
    }

    public void addIncludedStereotypes(Collection<String> includedStereotypes) {
        this.includedStereotypes.addAll(includedStereotypes);
    }

    public Collection<String> getExcludedStereotypes() {
        return excludedStereotypes;
    }

    public void setExcludedStereotypes(Collection<String> excludedStereotypes) {
        this.excludedStereotypes = excludedStereotypes;
    }

    public void addExcludedStereotypes(Collection<String> excludedStereotypes) {
        this.excludedStereotypes.addAll(excludedStereotypes);
    }

    public boolean isEmpty() {
        boolean result = false;
        if (includedStereotypes.isEmpty() && excludedStereotypes.isEmpty()) {
            result = true;
        }
        return result;
    }

    public boolean allow(String stereotype) {
        boolean result = false;
        if (stereotype == null && includedStereotypes.isEmpty()) {
            result = true;
        }
        else if (excludedStereotypes.contains(stereotype)) {
            result = false;
        }
        else if (includedStereotypes.contains(stereotype) || includedStereotypes.isEmpty()) {
            result = true;
        }
        return result;
    }
}
