/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd;

import com.compuware.caqs.pmd.stat.Metric;

public interface ReportListener {
    void ruleViolationAdded(IRuleViolation ruleViolation);

    void metricAdded(Metric metric);
}
