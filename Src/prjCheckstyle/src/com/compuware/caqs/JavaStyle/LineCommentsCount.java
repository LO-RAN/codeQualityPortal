package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.checks.AbstractOptionCheck;
import com.puppycrawl.tools.checkstyle.checks.blocks.BlockOption;


/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 7 mars 2006
 * Time: 13:57:09
 * To change this template use File | Settings | File Templates.
 */
public class LineCommentsCount extends AbstractOptionCheck {
    private DetailAST currentMEthod;
    /**the current visited class */
    private DetailAST currentClass;
    private String res;

    public LineCommentsCount()
    {
        super(BlockOption.STMT);
    }

    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF
        };
    }

    public String getResult(DetailAST aAST){
        visitToken(aAST);
        return this.res.toString();
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

    public void visitToken(DetailAST aAST)
    {

        int count=0;
        int lastLine;
        final FileContents contents = getFileContents();
        this.currentMEthod = aAST; 
        try{
            if(aAST.findFirstToken(TokenTypes.SLIST ) != null){
                final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST );
                if(openingBrace != null){
                    final DetailAST closingBrace =
                            aAST.findFirstToken(TokenTypes.SLIST ).findFirstToken(TokenTypes.RCURLY );
                    lastLine=closingBrace.getLineNo();


                    if(lastLine-openingBrace.getLineNo()>1){
                        for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {

                            if(contents.hasIntersectionWithComment(i,0,i,1000) ){
                                count++;
                            };

                        }


                        log(aAST.getLineNo(), aAST.getColumnNo(),this.getCurrentMethodName() +" cloc :"+count);
                    }else{
                        log(aAST.getLineNo(), aAST.getColumnNo(),this.getCurrentMethodName() +" cloc :0");

                    }
                }else{
                    log(aAST.getLineNo(), aAST.getColumnNo(),this.getCurrentMethodName() +" cloc :0");

                }
            }else{
                log(aAST.getLineNo(), aAST.getColumnNo(),this.getCurrentMethodName() +" cloc :0");
            }

        }catch(Exception e){
            log(aAST.getLineNo(), aAST.getColumnNo(), "erreur dans LineCommentsCount");
        }
        this.res=new Integer(count).toString() ;
    }


}

