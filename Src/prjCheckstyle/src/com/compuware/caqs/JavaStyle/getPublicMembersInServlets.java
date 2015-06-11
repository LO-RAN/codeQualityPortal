package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public final class getPublicMembersInServlets extends Check
{

    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.VARIABLE_DEF
        };
    }

    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public void visitToken(DetailAST aAST)
    {
        DetailAST modifiers=aAST.findFirstToken(TokenTypes.MODIFIERS );

        if(modifiers != null ){
            if(!modifiers.branchContains(TokenTypes.FINAL) &&
                    modifiers.branchContains(TokenTypes.LITERAL_PUBLIC )){
                log(aAST.getLineNo(), aAST.getColumnNo(),
                        "public member"+aAST.getText() );
            }
        }
    }
}
