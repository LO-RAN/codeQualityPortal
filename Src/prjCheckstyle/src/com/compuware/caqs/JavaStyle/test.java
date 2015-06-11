package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.*;


/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 9 mars 2006
 * Time: 11:25:08
 * To change this template use File | Settings | File Templates.
 */
public class test extends Check {
    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.CLASS_DEF };
    }

    public void visitToken(DetailAST aAST)
    {
        String tree=aAST.toStringTree();

        log(aAST.getLineNo(), aAST.getColumnNo(), "number of methods : "+tree );
    }

}
