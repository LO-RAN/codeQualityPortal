package com.compuware.caqs.JavaStyle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import antlr.collections.AST;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 13 mars 2006
 * Time: 11:27:38
 * To change this template use File | Settings | File Templates.
 */
public class numberOfMethod  extends Check{

    /** minimum  depth */
    private int mMinimumDepth;

    /** maximum depth */
    private int mMaximumDepth = Integer.MAX_VALUE;

    /** minimum number */
    private int mMinimumNumber;

    /** maximum number */
    private int mMaximumNumber = Integer.MAX_VALUE;

    /** limited tokens */
    private int[] mLimitedTokens = new int[0];

    /** error message when minimum count not reached */
    private String mMinimumMessage = "descendant.token.min";

    /** error message when maximum count exceeded */
    private String mMaximumMessage = "descendant.token.max";

    /**
     * Counts of descendant tokens.
     * Indexed by (token ID - 1) for performance.
     */
    private int[] mCounts = new int[0];

     public int[] getDefaultTokens()
    {
        return new int[0];
    }

    public void visitToken(DetailAST aAST)
    {
        //reset counts
        Arrays.fill(mCounts, 0);

        countTokens(aAST, 0);

        // name of this token
        final String name = TokenTypes.getTokenName(aAST.getType());

        for (int i = 0; i < mLimitedTokens.length; i++) {
            final int tokenCount = mCounts[mLimitedTokens[i] - 1];
            if (tokenCount < mMinimumNumber) {
                final String descendantName =
                        TokenTypes.getTokenName(mLimitedTokens[i]);
                log(aAST.getLineNo(),
                        aAST.getColumnNo(),
                        mMinimumMessage,
                        new String[] {
                            "" + tokenCount,
                            "" + mMinimumNumber,
                            name,
                            descendantName,
                        });
            }
            if (tokenCount > mMaximumNumber) {
                final String descendantName =
                        TokenTypes.getTokenName(mLimitedTokens[i]);
                log(aAST.getLineNo(),
                        aAST.getColumnNo(),
                        mMaximumMessage,
                        new String[] {
                            "" + tokenCount,
                            "" + mMaximumNumber,
                            name,
                            descendantName,
                        });
            }
        }
    }

    /**
     * Counts the number of occurrences of descendant tokens.
     * @param aAST the root token for descendants.
     * @param aDepth the maximum depth of the counted descendants.
     */
    private void countTokens(AST aAST, int aDepth)
    {
        if (aDepth <= mMaximumDepth) {
            //update count
            if (aDepth >= mMinimumDepth) {
                final int type = aAST.getType();
                if (type <= mCounts.length) {
                    mCounts[type - 1]++;
                }
            }
            AST child = aAST.getFirstChild();
            final int nextDepth = aDepth + 1;
            while (child != null) {
                countTokens(child, nextDepth);
                child = child.getNextSibling();
            }
        }
    }

    public int[] getAcceptableTokens()
    {
        // Any tokens set by property 'tokens' are acceptable
        final Set tokenNames = getTokenNames();
        final int[] result = new int[tokenNames.size()];
        int i = 0;
        final Iterator it = tokenNames.iterator();
        while (it.hasNext()) {
            final String name = (String) it.next();
            result[i] = TokenTypes.getTokenId(name);
            i++;
        }
        return result;
    }

    /**
     * Sets the tokens which occurance as descendant is limited.
     * @param aLimitedTokens - list of tokens to ignore.
     */
    public void setLimitedTokens(String[] aLimitedTokens)
    {
        mLimitedTokens = new int[aLimitedTokens.length];

        int maxToken = 0;
        for (int i = 0; i < aLimitedTokens.length; i++) {
            mLimitedTokens[i] = TokenTypes.getTokenId(aLimitedTokens[i]);
            if (mLimitedTokens[i] > maxToken) {
                maxToken = mLimitedTokens[i];
            }
        }
        mCounts = new int[maxToken];
    }

    /**
     * Sets the mimimum depth for descendant counts.
     * @param aMinimumDepth the mimimum depth for descendant counts.
     */
    public void setMinimumDepth(int aMinimumDepth)
    {
        mMinimumDepth = aMinimumDepth;
    }

    /**
     * Sets the maximum depth for descendant counts.
     * @param aMaximumDepth the maximum depth for descendant counts.
     */
    public void setMaximumDepth(int aMaximumDepth)
    {
        mMaximumDepth = aMaximumDepth;
    }


}
