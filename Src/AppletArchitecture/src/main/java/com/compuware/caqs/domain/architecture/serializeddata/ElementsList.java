/*
 * ElementList.java
 *
 * Created on 21 novembre 2002, 15:10
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @author cwfr-fxalbouy
 */
public class ElementsList extends Vector<Element> {

    transient protected Hashtable masterElements;
    transient protected Vector keysVector;

    /** Creates a new instance of ElementList */
    public ElementsList() {
    }

    public ElementsList(Vector<Element> elts) {
        for (int i = 0; i < elts.size(); i++) {
            this.addElement(elts.elementAt(i));
        }
    }

    public Vector getKeys() {
        this.compute();
        return this.keysVector;
    }

    public Hashtable getMasterizedHash() {
        this.compute();
        return this.masterElements;
    }

    private void compute() {
        this.masterElements = new Hashtable();
        for (int i = 0; i < this.size(); i++) {
            Element curElt = (Element) this.elementAt(i);
            String masterElement = curElt.getLabel();
            this.masterElements.put(masterElement, curElt);
        }

        Enumeration keys = this.masterElements.keys();

        this.keysVector = new Vector();
        ArrayList al = new ArrayList();

        while (keys.hasMoreElements()) {
            al.add(keys.nextElement());
        }

        Collections.sort(al);

        this.keysVector.addAll(al);

    }
}
