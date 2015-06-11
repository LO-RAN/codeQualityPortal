package com.compuware.caqs.pmd.parsers;

import com.compuware.caqs.pmd.ast.ParseException;
import com.compuware.caqs.pmd.cpp.ast.CPPParser;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class CppParser implements Parser {

    public Object parse(Reader source) {
        try {
			return new CPPParser(source).translation_unit();
		} catch (com.compuware.caqs.pmd.cpp.ast.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    public Map getExcludeMap() {
        return new HashMap(); // FIXME
    }

    public void setExcludeMarker(String marker) {
        // FIXME
    }


}
