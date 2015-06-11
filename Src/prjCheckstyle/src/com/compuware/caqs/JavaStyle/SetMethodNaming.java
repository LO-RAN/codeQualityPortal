
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Stack;


public class SetMethodNaming
        extends Check
{
    /** Stack of class' members. */
    private Stack mMemberNamesStack = new Stack();
    private DetailAST param;

    /** Creates a new <code>MemberNameCheck</code> instance. */
    public SetMethodNaming()
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
        DetailAST nextVarA;
        DetailAST nextVarB;

            param= aAST;
            nextVarA= aAST;
            nextVarB= aAST;
            nextVar= aAST;


        if( aAST.getType() == TokenTypes.VARIABLE_DEF && !ScopeUtils.isLocalVariableDef(nextVar)){
            addVariableReference(aAST.findFirstToken(TokenTypes.IDENT ) );
        }
        else {

            if(aAST.getType() == TokenTypes.METHOD_DEF){

                if(!aAST.branchContains(TokenTypes.LITERAL_RETURN) && aAST.findFirstToken(TokenTypes.PARAMETERS) != null){
                    //extraction du nom du paramètre
                    ParameterNameExtract(aAST);



                //extraction des nom de la variable affectée et de la vriable a affecter
                if(aAST.findFirstToken(TokenTypes.SLIST) != null){
                    nextVar=aAST.findFirstToken(TokenTypes.SLIST);

                    if(nextVar.getChildCount() == 3 && nextVar.findFirstToken(TokenTypes.EXPR) != null){

                        nextVar=nextVar.findFirstToken(TokenTypes.EXPR);

                        if(nextVar.findFirstToken(TokenTypes.ASSIGN) != null ){

                            //nextVarA est la variable a gauche du token ASSIGN ("=")
                            nextVarA=nextVar.findFirstToken(TokenTypes.ASSIGN);
                            nextVarB=nextVarA;

                            if(nextVarA.findFirstToken(TokenTypes.DOT) != null ){
                                nextVarA=nextVarA.findFirstToken(TokenTypes.DOT);
                                nextVarB=(DetailAST)nextVarA.getNextSibling();
                                nextVarA=nextVarA.findFirstToken(TokenTypes.IDENT);



                            }else{
                                 nextVarA=nextVarA.findFirstToken(TokenTypes.IDENT);
                                 nextVarB=nextVarA;
                                 nextVarB=(DetailAST)nextVarA.getNextSibling() ;
                            }


                            try{

                            //nextVarB est la variable a droite du token ASSIGN ("=")



                            //si le token n'est pas un IDENT, alors il s'agit d'une expression.                                         x²
                            if(nextVarB.getType() == TokenTypes.IDENT ){



                                //on commence par comparer NextVarA avec les noms des attributs
                                //s il y a correspondance, on compare nextVarB avec le nom du paramètre
                                for(int i=0; i<mMemberNamesStack.size();i++){
                                    if(mMemberNamesStack.elementAt(i).toString().startsWith(nextVarA.getText() ) ){
                                        if(param.getText().startsWith(nextVarB.getText())){
                                            SameIdentFound(aAST, nextVarB.getText());
                                        }
                                    }
                                }

                            }}catch(Exception e){
            log(aAST.getLineNo(), aAST.getColumnNo(),"erreur dans visitToken pour la fonction : "+aAST.findFirstToken(TokenTypes.IDENT ));
        }
                        }
                    }
                    }
                }
            }
        }
    }


    public void ParameterNameExtract(DetailAST idAst){
       
            param=idAst.findFirstToken(TokenTypes.PARAMETERS);
            //on s assure qu il n y a qu un seul parametre
            if(param.getChildCount(TokenTypes.PARAMETER_DEF ) == 1 && param.findFirstToken(TokenTypes.PARAMETER_DEF) != null){

                param=param.findFirstToken(TokenTypes.PARAMETER_DEF);

                if(param.findFirstToken(TokenTypes.IDENT) != null){
                    param=param.findFirstToken(TokenTypes.IDENT);
                }
            }
    }

//Cette méthode complète le tableau d attributs
    private void addVariableReference(DetailAST id) {
        mMemberNamesStack.addElement(id);
    }


    public void SameIdentFound(DetailAST id, String memberName){

            if(!id.findFirstToken(TokenTypes.IDENT ).getText().startsWith("set") ){
                log(id.getLineNo(), id.getColumnNo(),"Attention. La méthode "+id.findFirstToken(TokenTypes.IDENT ).getText()+" devrait s'appeler : set"+memberName);

            }


    }

}

