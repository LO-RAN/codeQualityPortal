package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * <p>
 * Disallow assignment of parameters.
 * </p>
 * <p>
 * Rationale:
 * Parameter assignment is often considered poor
 * programming practice. Forcing developers to declare
 * parameters as final is often onerous. Having a check
 * ensure that parameters are never assigned would give
 * the best of both worlds.
 * </p>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 */
public final class getModifiedMembersInServlets extends Check
{
    /** Stack of methods' parameters. */
    private final Stack mMembersNamesStack = new Stack();
    /** Current set of perameters. */
    private Set mMembersNames;
    private static int ind;
    private static String methodVisited;

    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.METHOD_DEF,
            TokenTypes.ASSIGN,
            TokenTypes.PLUS_ASSIGN,
            TokenTypes.MINUS_ASSIGN,
            TokenTypes.STAR_ASSIGN,
            TokenTypes.DIV_ASSIGN,
            TokenTypes.MOD_ASSIGN,
            TokenTypes.SR_ASSIGN,
            TokenTypes.BSR_ASSIGN,
            TokenTypes.SL_ASSIGN,
            TokenTypes.BAND_ASSIGN,
            TokenTypes.BXOR_ASSIGN,
            TokenTypes.BOR_ASSIGN,
            TokenTypes.INC,
            TokenTypes.POST_INC,
            TokenTypes.DEC,
            TokenTypes.POST_DEC,
        };
    }


    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }


    public void beginTree(DetailAST aRootAST)
    {
        // clear data
        mMembersNamesStack.clear();
        mMembersNames = null;
    }


    public void visitToken(DetailAST aAST)
    {
        switch (aAST.getType()) {
            case TokenTypes.CLASS_DEF:
            	methodVisited="";
                visitClassDef(aAST);
                break;
            case TokenTypes.METHOD_DEF:
                visitMethodDef(aAST);
                break;
            case TokenTypes.ASSIGN:
            case TokenTypes.PLUS_ASSIGN:
            case TokenTypes.MINUS_ASSIGN:
            case TokenTypes.STAR_ASSIGN:
            case TokenTypes.DIV_ASSIGN:
            case TokenTypes.MOD_ASSIGN:
            case TokenTypes.SR_ASSIGN:
            case TokenTypes.BSR_ASSIGN:
            case TokenTypes.SL_ASSIGN:
            case TokenTypes.BAND_ASSIGN:
            case TokenTypes.BXOR_ASSIGN:
            case TokenTypes.BOR_ASSIGN:
                visitAssign(aAST);
                break;
            case TokenTypes.INC:
            case TokenTypes.POST_INC:
            case TokenTypes.DEC:
            case TokenTypes.POST_DEC:
                visitIncDec(aAST);
                break;
            default:
                throw new IllegalStateException(aAST.toString());
        }
    }


    public void leaveToken(DetailAST aAST)
    {
        switch (aAST.getType()) {
            case TokenTypes.CLASS_DEF:
                leaveClassDef(aAST);
                break;
            case TokenTypes.METHOD_DEF:
                leaveMethodDef(aAST);
                break;
            case TokenTypes.ASSIGN:
            case TokenTypes.PLUS_ASSIGN:
            case TokenTypes.MINUS_ASSIGN:
            case TokenTypes.STAR_ASSIGN:
            case TokenTypes.DIV_ASSIGN:
            case TokenTypes.MOD_ASSIGN:
            case TokenTypes.SR_ASSIGN:
            case TokenTypes.BSR_ASSIGN:
            case TokenTypes.SL_ASSIGN:
            case TokenTypes.BAND_ASSIGN:
            case TokenTypes.BXOR_ASSIGN:
            case TokenTypes.BOR_ASSIGN:
            case TokenTypes.INC:
            case TokenTypes.POST_INC:
            case TokenTypes.DEC:
            case TokenTypes.POST_DEC:
                // Do nothing
                break;
            default:
                throw new IllegalStateException(aAST.toString());
        }
    }

    /**
     * Ckecks if this is assignments of parameter.
     * @param aAST assignment to check.
     */
    private void visitAssign(DetailAST aAST)
    {
        checkIdent(aAST);
    }

    /**
     * Checks if this is increment/decrement of parameter.
     * @param aAST dec/inc to check.
     */
    private void visitIncDec(DetailAST aAST)
    {
        checkIdent(aAST);
    }

    /**
     * Check if ident is parameter.
     * @param aAST ident to check.
     */
    private void checkIdent(DetailAST aAST)
    {
        DetailAST parent;
        ind=0;
        if (mMembersNames != null && !mMembersNames.isEmpty()) {
            final DetailAST identAST = (DetailAST) aAST.getFirstChild();

            if (identAST != null
                    && identAST.getType() == TokenTypes.IDENT
                    && mMembersNames.contains(identAST.getText()))
            {
                parent=aAST;
                
                while(parent.getParent().getType() != TokenTypes.METHOD_DEF
                        && parent.getParent().getType() != TokenTypes.CTOR_DEF
                        && parent.getParent().getType() != TokenTypes.CLASS_DEF)
                {
                    ind++;
                    parent=parent.getParent();
                }
                parent=parent.getParent();
                if(parent.getType() == TokenTypes.METHOD_DEF){
                    DetailAST modifiers=parent.findFirstToken(TokenTypes.MODIFIERS );
                    try{
                        if(modifiers != null ){
                            if(!modifiers.branchContains(TokenTypes.LITERAL_STATIC )){
                                log(aAST.getLineNo(), aAST.getColumnNo(),
                                        "member assignment "+identAST.getText() );
                            }
                        }
                    }catch(Exception e){
                        log(parent.getLineNo(), aAST.getColumnNo(),
                                "erreur dans checkIdent ", identAST.getText());
                    }
                }
            }
        }
    }

    /**
     * Creates new set of parameters and store old one in stack.
     * @param aAST a method to process.
     */
    private void visitMethodDef(DetailAST aAST)
    {
    	methodVisited=aAST.findFirstToken(TokenTypes.IDENT).getText() ;
        
    }

    /**
     * Creates new set of parameters and store old one in stack.
     * @param aAST a method to process.
     */
    private void visitClassDef(DetailAST aAST)
    {

        mMembersNamesStack.push(mMembersNames);
        mMembersNames = new HashSet();

        visitClassMembers(aAST.findFirstToken(TokenTypes.OBJBLOCK ) );
    }


    /** Restores old set of parameters. */
    private void leaveMethodDef(DetailAST aAST)
    {

        methodVisited="";;
    }

    /** Restores old set of parameters. */
    private void leaveClassDef(DetailAST aAST)
    {
        mMembersNames = (Set) mMembersNamesStack.pop();
    }

    /**
     * Creates new parameter set for given method.
     * @param aAST a method for process.
     */
    private void visitClassMembers(DetailAST aAST)
    {
        DetailAST variableDefAST =
                aAST.findFirstToken(TokenTypes.VARIABLE_DEF);

        for (; variableDefAST != null;
             variableDefAST = (DetailAST) variableDefAST.getNextSibling())
        {
            if (variableDefAST.getType() == TokenTypes.VARIABLE_DEF) {
                final DetailAST param =
                        variableDefAST.findFirstToken(TokenTypes.IDENT);
                mMembersNames.add(param.getText());
            }
        }
    }
}
