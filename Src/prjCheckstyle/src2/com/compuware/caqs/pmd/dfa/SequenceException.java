/*
 * Created on 12.07.2004
 */
package com.compuware.caqs.pmd.dfa;

/**
 * @author raik
 */
public class SequenceException extends Exception {

    public SequenceException() {
        super("Sequence error."); //TODO redefinition
    }

    public SequenceException(String message) {
        super(message);
    }
}
