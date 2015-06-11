package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 4 avr. 2006
 * Time: 15:01:26
 * To change this template use File | Settings | File Templates.
 */
public class getMethodList extends Check{



    public getMethodList()
    {
        super();
    }


    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.METHOD_DEF, TokenTypes.CTOR_DEF };
    }


    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public String getPackageName(DetailAST a){
        DetailAST t=setClassLevel(a);
        while(t.getPreviousSibling() != null){
            t=t.getPreviousSibling();
            log(a.getLineNo(), a.getColumnNo(),"search pack : "+t.findFirstToken(TokenTypes.IDENT ).getText());

        }
        if(t.getType() == TokenTypes.PACKAGE_DEF ){
            return t.findFirstToken(TokenTypes.IDENT).getText();
        }else{
            return "no package";
        }

    }

    public DetailAST setClassLevel(DetailAST c){
        while(c.getType() != TokenTypes.CLASS_DEF ){
            c=c.getParent();
        }
        return c;
    }

    public String getClassName(DetailAST d){

        return setClassLevel(d).findFirstToken(TokenTypes.IDENT).getText();
    }

    public void visitToken(DetailAST aAST)
    {
        if(aAST.getParent() != null){
            String classe=getClassName(aAST);
           // String pack=getPackageName(aAST);
            log(aAST.getLineNo(), aAST.getColumnNo(),"class "+classe+"."+aAST.findFirstToken(TokenTypes.IDENT ).getText());
        }else{
        log(aAST.getLineNo(), aAST.getColumnNo(),"methode : "+aAST.findFirstToken(TokenTypes.IDENT ).getText());
        }
    }


}

