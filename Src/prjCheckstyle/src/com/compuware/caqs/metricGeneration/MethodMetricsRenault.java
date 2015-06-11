package com.compuware.caqs.metricGeneration;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.checks.j2ee.Utils;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 11 mai 2006
 * Time: 17:35:48
 * To change this template use File | Settings | File Templates.
 */
public class MethodMetricsRenault extends Check {
    private int vg;
    private int loc;
    private int cloc;
    private int startLine;
    private int evg;
    static int comptEvg;
    /**the current visited method */
    private DetailAST currentMEthod;
    /**the current visited class */
    private DetailAST currentClass;

    public MethodMetricsRenault()
    {
        this.evg=0;
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
        this.vg=1;

    }

    public void visitClassDef(DetailAST aAST){

    }

    public void visitKeywordDef(DetailAST aAST){

        this.vg=this.vg+1;
    }

    protected void visitTokenHook(DetailAST aAST)
    {
    }

    public void leaveMethodDef(DetailAST aAST){
        String type="";
        String visu;
        int nl;
        //nl. Attention, il faut tester la présence d'un corps dans la méthode, au cas où.
        if(aAST.findFirstToken(TokenTypes.SLIST)!=null){
            nl=aAST.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.RCURLY).getLineNo()-aAST.getLineNo()+1;
        }else{
            nl=0;
        }
        this.currentMEthod=aAST;
        //visibility
        if(Utils.isPublic(aAST)){
            visu="Public";
        }else{
            if(aAST.findFirstToken(TokenTypes.MODIFIERS).findFirstToken(TokenTypes.LITERAL_PROTECTED) != null){
                visu="Protected";
            }else{
                visu="Private";
            }
        }
        if(Utils.isAbstract(aAST)) {
            type="Abstract";
        }else{
            type="Normal";
        }
        loc=getLoc(aAST);

        cloc=getCloc(aAST);
        startLine=aAST.getLineNo();
        log(aAST,"<method a-name=\""+this.getCurrentMethodName()+"\" " +
                "b-startLine=\""+this.startLine+"\" " +
                "c-type=\""+type+"\" " +
                "d-visibility=\""+visu+"\" " +
                "e-loc=\""+this.loc+"\" " +
                "f-vg=\""+this.vg+"\" " +
                "g-cloc=\""+this.cloc+"\" " +
                "h-nl=\""+nl+"\" " +
                "i-evg=\""+evgCalc(aAST)+"\"/>\n");

    }

    private int evgCalc(DetailAST ast){

        this.evg=1;
        expl(ast.findFirstToken(TokenTypes.SLIST ) , 0);
        return this.evg;
    }

    private void expl(DetailAST ast, int i){

        DetailAST tempAst=ast;
        comptEvg=i;
        //System.out.println("visite "+this.getCurrentMethodName() +" : "+ast.getText()+": "+this.evg);
        for(tempAst = (DetailAST)ast.getFirstChild(); tempAst != null; tempAst = (DetailAST)tempAst.getNextSibling()) {
            if(isKeyWord(tempAst)){
                comptEvg=comptEvg+1;

                expl(tempAst, comptEvg);
                if (comptEvg>0) comptEvg=comptEvg-1;

            }else{
                if(isOutKeyWord(tempAst)){
                    if(tempAst.getType() == TokenTypes.LAND||
                            tempAst.getType() == TokenTypes.LOR ){
                        comptEvg++;
                    }
                    this.evg=this.evg+comptEvg;

                    if (comptEvg>0) comptEvg=0;
                }else{
                    expl(tempAst, comptEvg);
                }
            }
        }
    }

    private boolean isKeyWord(DetailAST mt){

        if(mt.getType() == TokenTypes.LITERAL_IF  ||
                mt.getType() == TokenTypes.LITERAL_FOR  ||
                mt.getType() == TokenTypes.LITERAL_WHILE  ){

            return true;
        }else{
            return false;
        }
    }

    private boolean isOutKeyWord(DetailAST mt){
        if(mt.getType() == TokenTypes.LAND||
                mt.getType() == TokenTypes.LOR ||
                mt.getType() == TokenTypes.LITERAL_RETURN ||
                mt.getType() == TokenTypes.LITERAL_THROW ||
                mt.getType() == TokenTypes.LITERAL_BREAK ||
                mt.getType() == TokenTypes.LITERAL_CONTINUE||
                mt.getType() == TokenTypes.METHOD_CALL){

            return true;
        }else{
            return false;
        }
    }


    public void leaveClassDef(DetailAST aAST){
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
        //pattern d'un comment sur une ligne
        Pattern fullSlashComment=Pattern.compile("^\\s*/\\*.*\\*/.*$") ;
        //pattern d'un comment commencant au début d'une ligne
        Pattern BeginSlashComment=Pattern.compile("^\\s*/\\*.*$") ;
        //pattern d'un comment commencant au milieu d'une ligne
        Pattern MiddleSlashComment=Pattern.compile("^\\s[a-zA-Z]+.*/\\*.*$") ;
        //pattern repérant la fin d'un comment sans rien après
        Pattern FullEndSlashComment=Pattern.compile("^.*\\*/\\s*$") ;
        //pattern repérant la fin d'un comment avec du code après
        Pattern partialEndSlashComment=Pattern.compile("^.*\\*/.*[a-zA-Z]+.*$") ;
        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST);

        //indice indiquant si on est dans une zone commentée ou non
        boolean isCommented=false;
        int length=0;

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
                    }else{
                        if(fullSlashComment.matcher(contents.getLines()[i]).matches()) {
                            length--;
                        }
                        else{
                            if(!isCommented){

                                if(BeginSlashComment.matcher(contents.getLines()[i]).matches()){
                                    isCommented=true;
                                    length--;
                                }else{
                                    if(MiddleSlashComment.matcher(contents.getLines()[i]).matches()){
                                        isCommented=true;
                                    }
                                }
                            }else{
                                if(FullEndSlashComment.matcher(contents.getLines()[i]).matches()){
                                    isCommented=false;
                                    length--;
                                }else{
                                    if(partialEndSlashComment.matcher(contents.getLines()[i]).matches()){
                                        isCommented=false;
                                    }else{
                                        length--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return length;
    }

    public int getCloc(DetailAST aAST)
    {

        int count=0;
        int lastLine;
        final FileContents contents = getFileContents();

        try{
            if(aAST.findFirstToken(TokenTypes.SLIST ) != null){
                final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST );
                if(openingBrace != null){
                    final DetailAST closingBrace =
                            aAST.findFirstToken(TokenTypes.SLIST ).findFirstToken(TokenTypes.RCURLY );
                    lastLine=closingBrace.getLineNo();


                    if(lastLine-openingBrace.getLineNo()>1){
                        for (int i = openingBrace.getLineNo(); i < lastLine; i++) {

                            if(contents.hasIntersectionWithComment(i,0,i,1000)){
                            	
                                count++;
                            };

                        }
                    }else{
                        count=0;
                    }
                }else{
                    count=0;
                }
            }else{
                count=0;
            }
        }catch(Exception e){
            log(aAST.getLineNo(), aAST.getColumnNo(), "erreur dans method metrics");
        }
        return count;
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
                    p=p+","+type;
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
                parameters= parameters+tabParam.substring(1) ;
            }   else{
                parameters= parameters+tabParam;
            }
            parameters=parameters+")";
        }catch(Exception e){
            //log(this.currentMEthod, "erreur dans getCurrentMethodName() ");
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
            if(this.currentClass.getParent() != null) this.currentClass=this.currentClass.getParent();
            if(this.currentClass.getType() == TokenTypes.CLASS_DEF){
            	
            	className = this.currentClass.findFirstToken(TokenTypes.IDENT).getText() + "#" + className;
            }
            if(this.currentClass.getType() == TokenTypes.PACKAGE_DEF ){
                className=FullIdent.createFullIdent((DetailAST)this.currentClass.getFirstChild().getNextSibling()).getText()+"."+className;
            }
        }
        return className;

    }



}

