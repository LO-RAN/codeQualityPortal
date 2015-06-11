/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.renderers;

import com.compuware.caqs.pmd.IRuleViolation;
import com.compuware.caqs.pmd.Report;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class EmacsRenderer extends AbstractRenderer {

    protected static final String EOL = System.getProperty("line.separator", "\n");

    public void render(Writer writer, Report report) throws IOException {
        StringBuffer buf = new StringBuffer();
        for (Iterator i = report.iterator(); i.hasNext();) {
            IRuleViolation rv = (IRuleViolation) i.next();
            buf.setLength(0);
            buf.append(EOL).append(rv.getFilename());
            buf.append(':').append(Integer.toString(rv.getBeginLine()));
            buf.append(": ").append(rv.getDescription());
            writer.write(buf.toString());
        }
    }
}
