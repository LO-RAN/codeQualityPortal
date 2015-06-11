
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;


public class ExceptionNaming
        extends Check
{
    /** Stack of class' members. */


    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.EXTENDS_CLAUSE};
    }


    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public void visitToken(DetailAST aAST)
    {
        //if(aAST.getType() == TokenTypes.EXTENDS_CLAUSE && aAST.findFirstToken(TokenTypes.IDENT).getText().endsWith("Exception") ){
        //  log(aAST.getLineNo(), aAST.getColumnNo(),"Attention, la classe exception "+aAST.getPreviousSibling().getType()+"devrait se terminer par : Exception");
        //}else{
        DetailAST extendsAST=aAST;
        DetailAST classAST=aAST;
        try{
            while(extendsAST.findFirstToken(TokenTypes.DOT) != null ){
                extendsAST =  extendsAST.findFirstToken(TokenTypes.DOT);
            }
            if(extendsAST.findFirstToken(TokenTypes.IDENT) != null){
                while(classAST.getType() != TokenTypes.IDENT ){

                    classAST = classAST.getPreviousSibling() ;
                }
                if(extendsAST.findFirstToken(TokenTypes.IDENT).getText().endsWith("Exception") && !classAST.getText().endsWith("Exception") ){
                    log(classAST.getLineNo(), classAST.getColumnNo(),"Attention, la clause "+classAST.getText()+" devrait se terminer par : Exception");
                }
            }
        }catch(Exception e){
            log(classAST.getLineNo(), classAST.getColumnNo(),"erreur dans ExceptionNaming.java");

        }


        //}
    }

}



