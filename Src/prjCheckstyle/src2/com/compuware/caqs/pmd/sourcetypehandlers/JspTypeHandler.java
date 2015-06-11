package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.ast.ParseException;
import com.compuware.caqs.pmd.jsp.ast.JspCharStream;
import com.compuware.caqs.pmd.parsers.Parser;
import com.compuware.caqs.pmd.symboltable.JspSymbolFacade;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of SourceTypeHandler for the JSP parser.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public class JspTypeHandler implements SourceTypeHandler {
    
    public Parser getParser() {
        return new Parser() {
            public Object parse(Reader source) throws ParseException {
                return new com.compuware.caqs.pmd.jsp.ast.JspParser(new JspCharStream(source))
                        .CompilationUnit();
            }
            public Map getExcludeMap() {
                return new HashMap();
            }
            public void setExcludeMarker(String marker) {}
        };
    }

    public VisitorStarter getDataFlowFacade() {
        return VisitorStarter.dummy;
    }

    public VisitorStarter getSymbolFacade() {
        return new JspSymbolFacade();
    }

    public VisitorStarter getTypeResolutionFacade() {
        return VisitorStarter.dummy;
    }

}
