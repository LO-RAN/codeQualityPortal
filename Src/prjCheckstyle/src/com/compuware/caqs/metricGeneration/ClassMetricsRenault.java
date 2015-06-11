package com.compuware.caqs.metricGeneration;


import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.Check;
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
public class ClassMetricsRenault extends Check {

    private int vg;
    private int loc;
    private int cloc;
    private int startLine;
    /**the current visited class */
    private DetailAST currentClass;

    public ClassMetricsRenault()
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
        String type="";
        String visu="";
        int nl=aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.RCURLY).getLineNo()-aAST.getLineNo();
        this.currentClass=aAST;
        loc=getLoc(aAST);

        cloc=getCloc(aAST);
        startLine=aAST.getLineNo();
        if(Utils.isAbstract(aAST)) {
            type="Abstract";
        }else{
            if(aAST.getType() == TokenTypes.INTERFACE_DEF ){
                type="Interface";
            }else{
                type=isInnerClass(aAST);
            }
        }

        if(Utils.isPublic(aAST)){
            visu="Public";
        }else{
            if(aAST.findFirstToken(TokenTypes.MODIFIERS).findFirstToken(TokenTypes.LITERAL_PROTECTED) != null){
                visu="Protected";
            }else{
                visu="Private";
            }
        }
        //if(Utils.isFinal(aAST)) System.out.println(this.getClassName(this.currentClass)+" is final");
        // if(Utils.isPublic(aAST)) System.out.println(this.getClassName(this.currentClass)+" is public");
        //if(Utils.isStatic(aAST)) System.out.println(this.getClassName(this.currentClass)+" is static");

        log(aAST,"<class name=\""+this.getClassName(this.currentClass)+"\" " +
                "startLine=\""+this.startLine+"\" " +
                "type=\""+type+"\" " +
                "visibility=\""+visu+"\" " +
                "loc=\""+this.loc+"\" " +
                "vg=\""+this.vg+"\" " +
                "cloc=\""+this.cloc+"\" " +
                "nl=\"nl\" value=\""+nl+"\"/>");
    }

    private String isInnerClass(DetailAST classe){
        String is="Normal";

        while((classe=classe.getPreviousSibling()) != null){
            if(classe.getParent() != null) {
                is="Inner";
                break;
            }
        }
        return is;
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

        //indice indiquant si on est dans une zone commentée ou non
        boolean isCommented=false;
        int length=0;

        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.LCURLY);
        if (openingBrace != null) {
            final DetailAST closingBrace =
                    aAST.findFirstToken(TokenTypes.OBJBLOCK ).findFirstToken(TokenTypes.RCURLY);
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

