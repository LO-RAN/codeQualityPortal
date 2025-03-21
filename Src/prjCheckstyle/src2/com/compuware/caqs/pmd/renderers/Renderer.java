/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.renderers;

import java.io.IOException;
import java.io.Writer;

import com.compuware.caqs.pmd.Report;

public interface Renderer {

    void showSuppressedViolations(boolean show);

    /**
     * 
     * @deprecated This method consumes too much memory.
     * Use the render method with the Writer argument instead.
     * 
     */
    String render(Report report);

    void render(Writer writer, Report report) throws IOException;

}
