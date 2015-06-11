package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 5 avr. 2006
 * Time: 12:01:27
 * To change this template use File | Settings | File Templates.
 */
public class collectMetrics  extends Check
	   	{

    private DetailAST currentMEthod;

    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.CTOR_DEF,
                          TokenTypes.METHOD_DEF,

        };
    }
    public DetailAST setClassLevel(DetailAST c){
        while(c.getType() != TokenTypes.CLASS_DEF && c.getType() != TokenTypes.INTERFACE_DEF ){
            c=c.getParent();
        }

        return c;
    }

    public String getClassName(DetailAST d){

        return setClassLevel(d).findFirstToken(TokenTypes.IDENT).getText();
    }

    protected final String getParameters(){

        DetailAST temp0;
        DetailAST parameterDefAST;
        String p="";
        String type="";
        if(this.currentMEthod.findFirstToken(TokenTypes.PARAMETERS) != null){
            temp0= this.currentMEthod.findFirstToken(TokenTypes.PARAMETERS );

            parameterDefAST= temp0.findFirstToken(TokenTypes.PARAMETER_DEF );

            for (; parameterDefAST != null;
                 parameterDefAST = (DetailAST) parameterDefAST.getNextSibling())

            {
                if (parameterDefAST.getType() == TokenTypes.PARAMETER_DEF) {

                    final DetailAST param =
                            parameterDefAST.findFirstToken(TokenTypes.IDENT);
                    final DetailAST paramType=
                            parameterDefAST.findFirstToken(TokenTypes.TYPE).getLastChild();

                    // try{
                    if(paramType.getType() == TokenTypes.ARRAY_DECLARATOR ){

                        DetailAST temp=paramType;
                        type="[]";
                        String typeName;
                        while(temp.findFirstToken(TokenTypes.IDENT) == null &&
                                temp.findFirstToken(TokenTypes.LITERAL_INT)==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_BOOLEAN)==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_BYTE)==null &&
                                temp.findFirstToken( TokenTypes.LITERAL_CHAR )==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_DOUBLE  )==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_FLOAT )==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_LONG )==null &&
                                temp.findFirstToken(TokenTypes.LITERAL_SHORT )==null &&
                                temp.findFirstToken(TokenTypes.STRING_LITERAL)==null){

                            type=type+"[]";
                            temp=temp.findFirstToken(TokenTypes.ARRAY_DECLARATOR);
                        }

                        typeName= temp.getFirstChild().getText();
                        type=typeName+type;
                    }else{
                        final FullIdent full = FullIdent.createFullIdent(paramType);
                        type=full.getText() ;
                    }
                    // }catch(Exception e){
                    //    log(this.currentMEthod, "erreur dans getParameters() ");
                    // }
                    p=p+", "+type + " "+param.getText();
                }
            }
        }
        return p;
    }

    protected final String getCurrentMethodName(){

        String tabParam;
        String parameters;
        tabParam= getParameters();
        parameters="(";
        try{
            if(tabParam.length() >2){
                parameters= parameters+tabParam.substring(2) ;
            }   else{
                parameters= parameters+tabParam;
            }
            parameters=parameters+")";
        }catch(Exception e){
            log(this.currentMEthod, "erreur dans getCurrentMethodName() ");
        }
        return this.getClassName(this.currentMEthod)+"."+this.currentMEthod.findFirstToken(TokenTypes.IDENT).getText()+parameters ;
    }

    public int getMethodLoc(DetailAST aAST){
        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST);
        int length=0;

        this.currentMEthod =aAST;
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    openingBrace.findFirstToken(TokenTypes.RCURLY);
            length =
                    closingBrace.getLineNo() - openingBrace.getLineNo() + 1;

            if (length>1) {
                final FileContents contents = getFileContents();
                final int lastLine = closingBrace.getLineNo();
                for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {
                    if (contents.lineIsBlank(i) || contents.lineIsComment(i)) {
                        length--;
                    }
                }
            }
        }
        return length;
    }

    public void visitToken(DetailAST aAST)
    {

        log(aAST.getLineNo(), aAST.getColumnNo(), this.getCurrentMethodName()+" LOC : "+getMethodLoc(aAST));

    }


}
