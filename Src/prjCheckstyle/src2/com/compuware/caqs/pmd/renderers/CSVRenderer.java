/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.renderers;

import com.compuware.caqs.pmd.IRuleViolation;
import com.compuware.caqs.pmd.PMD;
import com.compuware.caqs.pmd.Report;
import com.compuware.caqs.pmd.util.StringUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class CSVRenderer extends AbstractRenderer {

    public void render(Writer writer, Report report) throws IOException {
        StringBuffer buf = new StringBuffer(300);
        quoteAndCommify(buf, "Problem");
        quoteAndCommify(buf, "Package");
        quoteAndCommify(buf, "File");
        quoteAndCommify(buf, "Priority");
        quoteAndCommify(buf, "Line");
        quoteAndCommify(buf, "Description");
        quoteAndCommify(buf, "Rule set");
        quote(buf, "Rule");
        buf.append(PMD.EOL);
        writer.write(buf.toString());

        addViolations(writer, report, buf);
    }

	private void addViolations(Writer writer, Report report, StringBuffer buf) throws IOException {
		int violationCount = 1;
		IRuleViolation rv;
        for (Iterator i = report.iterator(); i.hasNext();) {
            buf.setLength(0);
            rv = (IRuleViolation) i.next();
            quoteAndCommify(buf, Integer.toString(violationCount));
            quoteAndCommify(buf, rv.getPackageName());
            quoteAndCommify(buf, rv.getFilename());
            quoteAndCommify(buf, Integer.toString(rv.getRule().getPriority()));
            quoteAndCommify(buf, Integer.toString(rv.getBeginLine()));
            quoteAndCommify(buf, StringUtil.replaceString(rv.getDescription(), '\"', "'"));
            quoteAndCommify(buf, rv.getRule().getRuleSetName());
            quote(buf, rv.getRule().getName());
            buf.append(PMD.EOL);
            writer.write(buf.toString());
            violationCount++;
        }
	}

    private void quote(StringBuffer sb, String d) {
        sb.append('"').append(d).append('"');
    }

    private void quoteAndCommify(StringBuffer sb, String d) {
    	quote(sb, d);
        sb.append(',');
    }
}
