
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Stack;


public class GetMethodNaming
        extends Check
{
    /** Stack of class' members. */
    private final Stack mMemberNamesStack = new Stack();

    /** Creates a new <code>MemberNameCheck</code> instance. */
    public GetMethodNaming()
    {
        super();
    }


    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.VARIABLE_DEF, TokenTypes.METHOD_DEF };
    }


    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public void visitToken(DetailAST aAST)
    {

        DetailAST nextVar;
        nextVar= aAST;

            if( aAST.getType() == TokenTypes.VARIABLE_DEF && !ScopeUtils.isLocalVariableDef(aAST)){
                addVariableReference(aAST);
            }
            else {

                if(aAST.getType() == TokenTypes.METHOD_DEF && aAST.findFirstToken(TokenTypes.TYPE ).findFirstToken(TokenTypes.LITERAL_BOOLEAN ) == null ){
                    if(aAST.findFirstToken(TokenTypes.SLIST) != null){
                        nextVar=aAST.findFirstToken(TokenTypes.SLIST);
                        if(nextVar.getChildCount() <3 ){
                            if(nextVar.findFirstToken(TokenTypes.LITERAL_RETURN) != null){
                                nextVar=nextVar.findFirstToken(TokenTypes.LITERAL_RETURN);
                                if(nextVar.findFirstToken(TokenTypes.EXPR) != null){
                                    nextVar=nextVar.findFirstToken(TokenTypes.EXPR);

                                    try{
                                    //Ce cas est traité lorsqu'on retourne l variable sans this spécifié
                                    if(nextVar.findFirstToken(TokenTypes.DOT) != null){
                                        nextVar=nextVar.findFirstToken(TokenTypes.DOT);
                                    }
                                    if(nextVar.findFirstToken(TokenTypes.IDENT) != null){
                                    for(int i=0; i<mMemberNamesStack.size();i++){
                                        if(mMemberNamesStack.elementAt(i).toString().equals(nextVar.findFirstToken(TokenTypes.IDENT ).getText() )){
                                            SameIdentFound(aAST, nextVar.findFirstToken(TokenTypes.IDENT ).getText());
                                        }
                                    }
                                    }}catch(Exception e){
            log(aAST.getLineNo(), aAST.getColumnNo(),"erreur dans getMethodNaming pour la fonction : "+aAST.findFirstToken(TokenTypes.IDENT ) );
            }
                                }
                            }
                        }
                    }
                }

        }
    }

    //Cette méthode complète le tableau d attributs
    private void addVariableReference(DetailAST aAST) {
        mMemberNamesStack.addElement(aAST.findFirstToken(TokenTypes.IDENT));
    }


    public void SameIdentFound(DetailAST id, String memberName){
        if(!id.findFirstToken(TokenTypes.IDENT ).getText().startsWith("get")){
            log(id.getLineNo(), id.getColumnNo(),"Attention. La méthode "+id.findFirstToken(TokenTypes.IDENT ).getText()+" devrait s'appeler : get"+memberName);

        }






    }

}

