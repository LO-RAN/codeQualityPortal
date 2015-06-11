package com.compuware.caqs.pmd.sourcetypehandlers;

import com.compuware.caqs.pmd.ast.ParseException;
import com.compuware.caqs.pmd.cpp.ast.SimpleCharStream;
import com.compuware.caqs.pmd.parsers.Parser;
import com.compuware.caqs.pmd.symboltable.CppSymbolFacade;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of SourceTypeHandler for the JSP parser.
 *
 * @author pieter_van_raemdonck - Application Engineers NV/SA - www.ae.be
 */
public class CppTypeHandler implements SourceTypeHandler {
    
    public Parser getParser() {
        return new Parser() {
            public Object parse(Reader source) {
            	 
                try {
					return new com.compuware.caqs.pmd.cpp.ast.CPPParser(source).translation_unit();
				} catch (com.compuware.caqs.pmd.cpp.ast.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
            	
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
        return new CppSymbolFacade();
    }

    public VisitorStarter getTypeResolutionFacade() {
        return VisitorStarter.dummy;
    }

}
