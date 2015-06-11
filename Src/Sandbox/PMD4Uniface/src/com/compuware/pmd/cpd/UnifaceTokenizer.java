/*
 * @author laurent IZAC laurent.izac@compuware.com
*/
package com.compuware.pmd.cpd;

import com.compuware.pmd.uniface.ast.UnifaceCharStream;
import com.compuware.pmd.uniface.ast.UnifaceParserTokenManager;
import com.compuware.pmd.uniface.ast.Token;

import java.io.StringReader;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;

public class UnifaceTokenizer implements Tokenizer {

    public void tokenize(SourceCode tokens, Tokens tokenEntries) {
        StringBuffer buffer = tokens.getCodeBuffer();
        UnifaceParserTokenManager tokenMgr = new UnifaceParserTokenManager(new UnifaceCharStream(new StringReader(buffer.toString())));
        Token currentToken = tokenMgr.getNextToken();
        while (currentToken.image.length() > 0) {
            tokenEntries.add(new TokenEntry(String.valueOf(currentToken.kind), tokens.getFileName(), currentToken.beginLine));
            currentToken = tokenMgr.getNextToken();
        }
        tokenEntries.add(TokenEntry.getEOF());
    }
}

