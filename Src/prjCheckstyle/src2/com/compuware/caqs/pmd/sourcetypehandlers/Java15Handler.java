package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.parsers.Java15Parser;
import com.compuware.caqs.pmd.parsers.Parser;

public class Java15Handler extends JavaTypeHandler {

    public Parser getParser() {
        return new Java15Parser();
    }

}
