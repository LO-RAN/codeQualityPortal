package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 27 janv. 2006
 */



public class AvoidUsingPublicData extends Check{

    public int[] getDefaultTokens() {
        return new int[] {TokenTypes.VARIABLE_DEF };
    }

    public void visitToken(DetailAST aAST){

        if(!ScopeUtils.isLocalVariableDef(aAST)){
            if(aAST.branchContains(TokenTypes.LITERAL_PUBLIC))
            if(!(aAST.branchContains(TokenTypes.LITERAL_STATIC ) && aAST.branchContains(TokenTypes.FINAL ))){
                log(aAST.getLineNo(), aAST.getColumnNo(),aAST.findFirstToken(TokenTypes.IDENT ).getText()+" : Utilisation d une variable publique non constante"  );

            }

        }


    }
}


