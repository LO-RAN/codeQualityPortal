package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 11 mai 2006
 * Time: 17:35:48
 * To change this template use File | Settings | File Templates.
 */
public class ClassMetrics extends Check {
    private int vg;
    private DetailAST currentMEthod;
    /**the current visited class */
    private DetailAST currentClass;

    public ClassMetrics()
    {
        this.vg=0;
    }


    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,

            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,

            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,

            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_CASE,

            TokenTypes.QUESTION,
            TokenTypes.LAND,
            TokenTypes.LOR
        };
    }

    public void visitToken(DetailAST aAST)
    {

        switch (aAST.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
                visitMethodDef(aAST);
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.INTERFACE_DEF:
                visitClassDef(aAST);
                break;
            case TokenTypes.LITERAL_WHILE:
            case TokenTypes.LITERAL_DO:
            case TokenTypes.LITERAL_FOR:
            case TokenTypes.LITERAL_IF:
            case TokenTypes.LITERAL_CASE:
            case TokenTypes.QUESTION:
            case TokenTypes.LAND:
            case TokenTypes.LOR:
                visitKeywordDef(aAST);
            default:
                visitTokenHook(aAST);
        }

    }

    public void leaveToken(DetailAST aAST)
    {

        switch (aAST.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
                leaveMethodDef(aAST);
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.INTERFACE_DEF:
                leaveClassDef(aAST);
                break;
            case TokenTypes.LITERAL_WHILE:
            case TokenTypes.LITERAL_DO:
            case TokenTypes.LITERAL_FOR:
            case TokenTypes.LITERAL_IF:
            case TokenTypes.LITERAL_CASE:
            case TokenTypes.QUESTION:
            case TokenTypes.LAND:
            case TokenTypes.LOR:
                leaveKeywordDef();
            default:
                leaveTokenHook(aAST);
        }

    }

    public void visitMethodDef(DetailAST aAST){


    }

    public void visitClassDef(DetailAST aAST){
        this.vg=1;
    }

    public void visitKeywordDef(DetailAST aAST){

        this.vg=this.vg+1;
    }

    protected void visitTokenHook(DetailAST aAST)
    {
    }

    public void leaveMethodDef(DetailAST aAST){

    }

    public void leaveClassDef(DetailAST aAST){
        this.currentMEthod=aAST;
        System.out.println(" : "+this.getCurrentMethodName()+" : "+getLoc(aAST)+" : "+this.vg+" : "+getCloc(aAST));
    }

    public void leaveKeywordDef(){

    }

    protected void leaveTokenHook(DetailAST aAST)
    {
    }

    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public int getLoc(DetailAST aAST){
        int length=0;
        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.LCURLY);
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.RCURLY);
            length =
                    closingBrace.getLineNo() - openingBrace.getLineNo() + 1;

            final FileContents contents = getFileContents();
            final int lastLine = closingBrace.getLineNo();

            for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {
                if(contents.hasIntersectionWithComment(i,0,i,1000) || contents.lineIsBlank(i) ){
                    length--;
                };
            }
           // log(aAST.getLineNo(), aAST.getColumnNo(), "class "+aAST.findFirstToken(TokenTypes.IDENT ).getText() +" LOC : "+new Integer(length),new Integer(length));
        }
        return length;
    }

    public int getCloc(DetailAST aAST)
    {

        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.LCURLY);
        int length =0;
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.RCURLY);

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

        }
        return length;
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


                    final FullIdent full;
                    if(paramType.getType() == TokenTypes.ARRAY_DECLARATOR ){
                        DetailAST temp=paramType;
                        type="[]";

                        while(temp.findFirstToken(TokenTypes.ARRAY_DECLARATOR) != null){
                            type=type+"[]";
                            temp=temp.findFirstToken(TokenTypes.ARRAY_DECLARATOR);
                        }

                        full = FullIdent.createFullIdent((DetailAST)temp.getFirstChild());
                        type=full.getText()+type;
                    }else{
                        full = FullIdent.createFullIdent(paramType);
                        type=full.getText();
                    }
                   p=p+", "+type;
                }
            }
        }
        return p;
    }


    public static FullIdent createFullType(DetailAST aTypeAST)
    {
        DetailAST arrayDeclAST =
                aTypeAST.findFirstToken(TokenTypes.ARRAY_DECLARATOR);

        return createFullTypeNoArrays(arrayDeclAST == null ? aTypeAST
                : arrayDeclAST);
    }


    /*
    * @param aTypeAST a type node (no array)
    * @return <code>FullIdent</code> for given type.
    */
    private static FullIdent createFullTypeNoArrays(DetailAST aTypeAST)
    {
        return FullIdent.createFullIdent((DetailAST) aTypeAST.getFirstChild());
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

    public DetailAST setClassLevel(DetailAST c){
        while(c.getType() != TokenTypes.CLASS_DEF && c.getType() != TokenTypes.INTERFACE_DEF ){
            c=c.getParent();
        }
        return c;
    }

    public String getClassName(DetailAST d){

        this.currentClass= setClassLevel(d);
        String className= setClassLevel(d).findFirstToken(TokenTypes.IDENT).getText();

        while((this.currentClass=this.currentClass.getPreviousSibling()) != null){
            if(this.currentClass.getType() == TokenTypes.PACKAGE_DEF ){
                className=FullIdent.createFullIdent((DetailAST)this.currentClass.getFirstChild().getNextSibling()).getText()+"."+className;
            }
        }
        return className;

    }
}

