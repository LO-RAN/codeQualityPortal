/*
 * @author laurent IZAC laurent.izac@compuware.com
*/
package com.compuware.pmd.parsers;

import net.sourceforge.pmd.ast.ParseException;
import com.compuware.pmd.uniface.ast.UnifaceCharStream;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.pmd.parsers.Parser;

public class UnifaceParser implements Parser {

    public Object parse(Reader source) throws ParseException {
        return new com.compuware.pmd.uniface.ast.UnifaceParser(new UnifaceCharStream(source)).CompilationUnit();
    }

    public Map<Integer, String> getExcludeMap() {
        return new HashMap<Integer, String>(); // FIXME
    }

    public void setExcludeMarker(String marker) {
        // FIXME
    }

}
