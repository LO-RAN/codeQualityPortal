package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 22 mars 2006
 * Time: 11:37:29
 * To change this template use File | Settings | File Templates.
 */
public class CyclomaticComplexityClassCheck
        extends AbstractComplexityCheck
{
    /** default allowed complexity */
    private static final int DEFAULT_VALUE = 10;

    /** Create an instance. */
    public CyclomaticComplexityClassCheck()
    {
        super(DEFAULT_VALUE);
    }


    public int[] getDefaultTokens()
    {
        return new int[] {

            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,
            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_CASE,

            TokenTypes.QUESTION,
            TokenTypes.LAND,
            TokenTypes.LOR,
        };
    }

    protected final void visitTokenHook(DetailAST aAST)
    {
        incrementCurrentValue(1);
    }

    public String getMessageID()
    {

        return new Integer(super.getCurrentValue()).toString() ;
    }
}
