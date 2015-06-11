package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 7 mars 2006
 * Time: 13:57:09
 * To change this template use File | Settings | File Templates.
 */
public class sourceCodeLineNumberClass extends Check
 {

    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.CLASS_DEF,
                          TokenTypes.INTERFACE_DEF,
        };
    }

    public void visitToken(DetailAST aAST)
    {
        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.LCURLY);
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.RCURLY);
            int length =
                    closingBrace.getLineNo() - openingBrace.getLineNo() + 1;

                final FileContents contents = getFileContents();
                final int lastLine = closingBrace.getLineNo();

                for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {
                    if(contents.hasIntersectionWithComment(i,0,i,1000) || contents.lineIsBlank(i) ){
                        length--;
                    };
                }
            log(aAST.getLineNo(), aAST.getColumnNo(), "class "+aAST.findFirstToken(TokenTypes.IDENT ).getText() +" LOC : "+new Integer(length),new Integer(length));
        }
    }

}