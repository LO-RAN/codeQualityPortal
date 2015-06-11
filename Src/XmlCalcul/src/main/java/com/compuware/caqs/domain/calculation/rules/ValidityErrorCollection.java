/*
 * ValidityErrorCollection.java
 *
 * Created on 3 juin 2003, 10:05
 */

package com.compuware.caqs.domain.calculation.rules;

import java.util.Collection;

/**
 *
 * @author  cwfr-fdubois
 */
public interface ValidityErrorCollection extends Collection<ValidityError> {

    public static final String NO_ERROR = "error.noerror";
    public static final String NOT_NUMERIC_ERROR = "error.notnumeric";
    public static final String NOT_BOOLEAN_ERROR = "error.notboolean";
    public static final String NOT_STRING_ERROR = "error.notstring";
    
    public String toString();
    
    public void addError(Operand location, String errorkey);

}
