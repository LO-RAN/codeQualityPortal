package com.compuware.pmd.sourcetypehandlers;

import net.sourceforge.pmd.ast.ParseException;
import com.compuware.pmd.uniface.ast.UnifaceCharStream;
import net.sourceforge.pmd.parsers.Parser;
import com.compuware.pmd.symboltable.UnifaceSymbolFacade;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.pmd.sourcetypehandlers.SourceTypeHandler;
import net.sourceforge.pmd.sourcetypehandlers.VisitorStarter;

/**
 * Implementation of SourceTypeHandler for the UNIFACE parser.
 *
 * @author laurent IZAC laurent.izac@compuware.com
 */
public class UnifaceTypeHandler implements SourceTypeHandler {
    
    public Parser getParser() {
        return new Parser() {
            public Object parse(Reader source) throws ParseException {
                return new com.compuware.pmd.uniface.ast.UnifaceParser(new UnifaceCharStream(source))
                        .CompilationUnit();
            }
            public Map<Integer, String> getExcludeMap() {
                return new HashMap<Integer, String>();
            }
            public void setExcludeMarker(String marker) {}
        };
    }

    public VisitorStarter getDataFlowFacade() {
        return VisitorStarter.dummy;
    }

    public VisitorStarter getSymbolFacade() {
        return new UnifaceSymbolFacade();
    }

    public VisitorStarter getTypeResolutionFacade(ClassLoader classLoader) {
        return VisitorStarter.dummy;
    }

}
