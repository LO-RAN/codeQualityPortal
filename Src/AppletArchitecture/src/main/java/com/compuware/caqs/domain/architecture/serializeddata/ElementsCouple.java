/*
 * ElementsCouple.java
 *
 * Created on 4 novembre 2002, 16:55
 */
package com.compuware.caqs.domain.architecture.serializeddata;

/**
 *
 * @author  cwfr-fxalbouy
 */
import java.io.Serializable;

public class ElementsCouple implements Serializable {

    protected Element from;
    protected Element to;
    protected String id;

    /** Creates a new instance of ElementsCouple */
    public ElementsCouple(String id, Element from, Element to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public String getId() {
        return this.id;
    }

    public Element getFrom() {
        return this.from;
    }

    public Element getTo() {
        return this.to;
    }
}
