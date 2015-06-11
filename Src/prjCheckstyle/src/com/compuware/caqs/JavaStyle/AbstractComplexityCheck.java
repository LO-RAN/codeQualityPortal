
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.FullIdent;

import java.util.Stack;

/**
 * Base class for checks the calculate complexity based around methods.
 */
public abstract class AbstractComplexityCheck
        extends Check
{
    /** the initial current value */
    private static final int INITIAL_VALUE = 1;
    /**the current visited method */
    private DetailAST currentMEthod;
    /**the current visited class */
    private DetailAST currentClass;
    /** stack of values - all but the current value */
    private final Stack mValueStack = new Stack();
    /** the current value */
    private int mCurrentValue;

    /** threshold to report error for */
    private int mMax;

    /**
     * Creates an instance.
     * @param aMax the threshold of when to report an error
     */
    public AbstractComplexityCheck(int aMax)
    {
        mMax = aMax;
    }

    /**
     * @return the message ID to log violations with
     */
    protected abstract String getMessageID();

    /**
     * Hook called when visiting a token. Will not be called the method
     * definition tokens.
     *
     * @param aAST the token being visited
     */
    protected void visitTokenHook(DetailAST aAST)
    {
    }

    /**
     * Hook called when leaving a token. Will not be called the method
     * definition tokens.
     *
     * @param aAST the token being left
     */
    protected void leaveTokenHook(DetailAST aAST)
    {
    }


    public final int[] getRequiredTokens()
    {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,
        };
    }

    /** @return the maximum threshold allowed */
    public final int getMax()
    {
        return mMax;
    }

    /**
     * Set the maximum threshold allowed.
     *
     * @param aMax the maximum threshold
     */
    public final void setMax(int aMax)
    {
        mMax = aMax;
    }

    public void visitToken(DetailAST aAST)
    {
        System.out.println("un token a été trouve dans AbstractComplexityCheck");
        switch (aAST.getType()) {
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
                visitMethodDef();
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.INTERFACE_DEF:
                visitClassDef();
                break;
            default:
                visitTokenHook(aAST);
        }

    }


    public void leaveToken(DetailAST aAST)
    {
        switch (aAST.getType()) {

            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
                System.out.println("sortie de la méthode");
                leaveMethodDef(aAST);
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.INTERFACE_DEF:
                System.out.println("sortie de la classe");
                leaveClassDef(aAST);
                break;
            default:
                leaveTokenHook(aAST);
        }
    }

    /**
     * @return the current value
     */
    protected final int getCurrentValue()
    {
        return mCurrentValue;
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


    protected final String getCurrentClassName(){
              String pac="";
        String className= this.currentClass.findFirstToken(TokenTypes.IDENT).getText();
        while((pac=this.currentClass.getPreviousSibling().getText()) != null){
            System.out.println(pac);
        }
        return className;
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
            log(this.currentMEthod, "erreur dans getCurrentMethodName() ",
                    new Integer(mCurrentValue),
                    new Integer(mMax));
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

    /**
     * Set the current value
     * @param aValue the new value
     */
    protected final void setCurrentValue(int aValue)
    {
        mCurrentValue = aValue;
    }

    /**
     * Increments the current value by a specified amount.
     *
     * @param aBy the amount to increment by
     */
    protected final void incrementCurrentValue(int aBy)
    {
        setCurrentValue(getCurrentValue() + aBy);
    }

    /** Push the current value on the stack */
    protected final void pushValue()
    {
        mValueStack.push(new Integer(mCurrentValue));
        mCurrentValue = INITIAL_VALUE;
    }

    /**
     * @return pop a value off the stack and make it the current value
     */
    protected final int popValue()
    {
        mCurrentValue = ((Integer) mValueStack.pop()).intValue();

        return mCurrentValue;
    }


    /** Process the start of the method definition */
    private void visitMethodDef()
    {
        pushValue();
    }

    private void visitClassDef()
    {
        pushValue();
    }

    private void leaveClassDef(DetailAST aAST)
    {
        this.currentClass = aAST;
        log(aAST, getMessageID(),
                new Integer(mCurrentValue),
                new Integer(mMax));

        popValue();
    }

    /**
     * Process the end of a method definition.
     *
     * @param aAST the token representing the method definition
     */
    private void leaveMethodDef(DetailAST aAST)
    {
        this.currentMEthod = aAST;
        log(aAST, getMessageID(),
                new Integer(mCurrentValue),
                new Integer(mMax));

        popValue();
    }
}
