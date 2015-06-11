/*
 * Created on 14.07.2004
 */
package com.compuware.caqs.pmd.dfa.variableaccess;

/**
 * @author raik
 */
public class VariableAccessException extends Exception {

    public VariableAccessException() {
        super("VariableAccess error."); //TODO redefinition
    }

    public VariableAccessException(String message) {
        super(message);
    }
}
