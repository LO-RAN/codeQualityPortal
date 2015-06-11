package com.compuware.caqs.JavaStyle;


import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;

public class sourceCodeLineNumber extends Check
	   	{
    /**the current visited class */
    private DetailAST currentClass;

    private DetailAST currentMEthod;

    private static final int DEFAULT_MAX_LINES = 150;

    private Integer res;

    private int mMax = DEFAULT_MAX_LINES;

    public sourceCodeLineNumber(){
       this.res=new Integer(0);
    }

    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.CTOR_DEF,
                          TokenTypes.METHOD_DEF
        };
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


                    p=p+", "+type + " "+param.getText();
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

            if(tabParam.length() >2){
                parameters= parameters+tabParam.substring(2) ;
            }   else{
                parameters= parameters+tabParam;
            }
            parameters=parameters+")";


        return this.getClassName(this.currentMEthod)+"."+this.currentMEthod.findFirstToken(TokenTypes.IDENT).getText()+parameters ;
    }

    public String getResult(DetailAST aAST){

        visitToken(aAST);
        return this.res.toString();
    }

    public void visitToken(DetailAST aAST)
    {

        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST);
        int length=0;
        System.out.println("visitToken");
        this.currentMEthod =aAST;
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    openingBrace.findFirstToken(TokenTypes.RCURLY);
            length =
                    closingBrace.getLineNo() - openingBrace.getLineNo() + 1;
            System.out.println("NOCL : "+length);
            if (length>1) {
                final FileContents contents = getFileContents();
                final int lastLine = closingBrace.getLineNo();
                for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {
                    if (contents.lineIsBlank(i) || contents.lineIsComment(i)) {
                        length--;
                    }
                }
                this.res=new Integer(length);
            }else{
                    this.res=new Integer(0);
                }
            
        }


        log(aAST.getLineNo(), aAST.getColumnNo(), this.getCurrentMethodName()+" LOC : "+new Integer(length),new Integer(length), new Integer(mMax));

    }


}
