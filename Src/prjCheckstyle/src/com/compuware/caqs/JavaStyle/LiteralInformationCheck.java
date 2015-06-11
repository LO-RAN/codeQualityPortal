package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 22 mars 2006
 * Time: 17:12:39
 * To change this template use File | Settings | File Templates.
 */
public class LiteralInformationCheck extends Check
 {

    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.STRING_LITERAL
        };
    }

    public void visitToken(DetailAST aAST)
    {
        Pattern chaineVide = Pattern.compile("^[\\p{Punct}]*$");
        Pattern chainePleine = Pattern.compile("^\\p{Punct}[\\p{Graph}]*\\p{Punct}$");
        Pattern chaineDePonctuation = Pattern.compile("^\\p{Punct}[\\s|\\t|\\p{Punct}]*\\p{Punct}$");
        if(!chaineVide.matcher(aAST.getText()).matches()&&
                !chainePleine.matcher(aAST.getText()).matches()&&
                !chaineDePonctuation.matcher(aAST.getText()).matches()){
            log(aAST.getLineNo() ,"il y a 1 infos en dur "+aAST.getText() );
        }

    }

}