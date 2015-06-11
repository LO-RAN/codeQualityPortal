package com.compuware.caqs.pmd.ast;

import com.compuware.caqs.pmd.Rule;

public interface CanSuppressWarnings {
    boolean hasSuppressWarningsAnnotationFor(Rule rule);
}
