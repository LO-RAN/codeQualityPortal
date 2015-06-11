////////////////////////////////////////////////////////////////////////////////
// JavaStyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2005  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checks cyclomatic complexity against a specified limit. The complexity is
 * measured by the number of "if", "while", "do", "for", "?:",
 * "switch", "case", "&amp;&amp;" and "||" statements (plus one) in the body of
 * the member. It is a measure of the minimum number of possible paths through
 * the source and therefore the number of required tests. Generally 1-4 is
 * considered good, 5-7 ok, 8-10 consider re-factoring, and 11+ re-factor now!
 *
 */
public class CyclomaticComplexityCheck
        extends AbstractComplexityCheck
{
    /** default allowed complexity */
    private static final int DEFAULT_VALUE = 10;

    /** Create an instance. */
    public CyclomaticComplexityCheck()
    {
        super(DEFAULT_VALUE);
    }


    public int[] getDefaultTokens()
    {
        return new int[] {
            
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

    protected final void visitTokenHook(DetailAST aAST)
    {
        incrementCurrentValue(1);
    }

    public String getMessageID()
    {
        //System.out.println ("val trouvée dans CyclomaticComplexityCheck : "+new Integer(super.getCurrentValue()).toString());
        return new Integer(super.getCurrentValue()).toString() ;
    }
}

