/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import java.util.Iterator;

/**
 * @author Philippe T'Seyen
 */
public interface Renderer {
    String render(Iterator matches);
}
