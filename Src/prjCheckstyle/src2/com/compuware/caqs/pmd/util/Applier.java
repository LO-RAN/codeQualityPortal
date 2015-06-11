/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.util;

import java.util.Iterator;

public class Applier {

    public static void apply(UnaryFunction f, Iterator i) {
        while (i.hasNext()) {
            f.applyTo(i.next());
        }
    }
}
