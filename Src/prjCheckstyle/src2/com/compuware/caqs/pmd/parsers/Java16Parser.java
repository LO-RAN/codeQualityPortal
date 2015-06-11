package com.compuware.caqs.pmd.parsers;

import com.compuware.caqs.pmd.ast.JavaCharStream;
import com.compuware.caqs.pmd.ast.JavaParser;
import com.compuware.caqs.pmd.ast.ParseException;

import java.io.Reader;
import java.util.Map;

/**
 * Adapter for the JavaParser, using Java 1.6 grammar.
 *
 */
public class Java16Parser implements Parser {

    private JavaParser parser;
    private String marker;

    public Object parse(Reader source) throws ParseException {
        parser = new JavaParser(new JavaCharStream(source));
        parser.setJDK15();
        parser.setExcludeMarker(marker);
        return parser.CompilationUnit();
    }

    public Map getExcludeMap() {
        return parser.getExcludeMap();
    }

    public void setExcludeMarker(String marker) {
        this.marker = marker;
    }

}
