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
public class LineCommentsCountClass extends Check
 {
    private static final int DEFAULT_MAX_LINES = 150;

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
            int length =0;
            final FileContents contents = getFileContents();
            final int lastLine = closingBrace.getLineNo();

            try{
                if(contents.getJavadocBefore(openingBrace.getLineNo()).getEndLineNo() -
                        contents.getJavadocBefore(openingBrace.getLineNo()).getStartLineNo() > 0){
                    length=contents.getJavadocBefore(openingBrace.getLineNo()).getEndLineNo() -
                            contents.getJavadocBefore(openingBrace.getLineNo()).getStartLineNo();
                }
            }catch(Exception e){
            }
            for (int i = openingBrace.getLineNo() ; i < lastLine; i++) {
                if(contents.hasIntersectionWithComment(i,0,i,1000) ){
                    length++;
                };
            }
            log(aAST.getLineNo(), aAST.getColumnNo(), "class "+aAST.findFirstToken(TokenTypes.IDENT ).getText()
                    +" CLOC : "+new Integer(length),new Integer(length));
        }
    }

}