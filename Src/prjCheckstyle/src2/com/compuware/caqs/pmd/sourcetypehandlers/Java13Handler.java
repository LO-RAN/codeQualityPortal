package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.parsers.Java13Parser;
import com.compuware.caqs.pmd.parsers.Parser;

public class Java13Handler extends JavaTypeHandler {

    public Parser getParser() {
        return new Java13Parser();
    }

}
