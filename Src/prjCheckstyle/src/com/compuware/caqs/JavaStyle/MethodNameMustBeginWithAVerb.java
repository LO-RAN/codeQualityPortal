package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 27 janv. 2006
 */



public class MethodNameMustBeginWithAVerb extends Check{

    /** Creates a new <code>MethodNameCheck</code> instance. */
    public void visitToken(DetailAST aAST)
    {                
            if(aAST.getType() == TokenTypes.METHOD_DEF ){
            	log(aAST.getLineNo(), aAST.getColumnNo(),aAST.findFirstToken(TokenTypes.IDENT).getText());
            }           
    }
    
    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.METHOD_DEF, TokenTypes.VARIABLE_DEF };
    }
}