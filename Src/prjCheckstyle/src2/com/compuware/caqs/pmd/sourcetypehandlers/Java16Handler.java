package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.parsers.Java16Parser;
import com.compuware.caqs.pmd.parsers.Parser;

public class Java16Handler extends JavaTypeHandler {

    public Parser getParser() {
        return new Java16Parser();
    }

}
