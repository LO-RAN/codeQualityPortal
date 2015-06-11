package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.parsers.Java14Parser;
import com.compuware.caqs.pmd.parsers.Parser;

public class Java14Handler extends JavaTypeHandler {

    public Parser getParser() {
        return new Java14Parser();
    }

}
