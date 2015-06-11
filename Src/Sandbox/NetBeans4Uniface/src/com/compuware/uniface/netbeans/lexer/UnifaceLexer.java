/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.uniface.netbeans.lexer;

import com.compuware.uniface.netbeans.jcclexer.JavaCharStream;
import com.compuware.uniface.netbeans.jcclexer.Token;
import com.compuware.uniface.netbeans.jcclexer.UnifaceParserTokenManager;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author cwfr-lizac
 */

class UnifaceLexer implements Lexer<UnifaceTokenId> {

    private LexerRestartInfo<UnifaceTokenId> info;
    private UnifaceParserTokenManager unifaceParserTokenManager;


    UnifaceLexer (LexerRestartInfo<UnifaceTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream (info.input ());
        unifaceParserTokenManager = new UnifaceParserTokenManager (stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<UnifaceTokenId> nextToken () {
        Token token = unifaceParserTokenManager.getNextToken ();
        if (info.input ().readLength () < 1) {
            return null;
        }
        return info.tokenFactory ().createToken (UnifaceLanguageHierarchy.getToken (token.kind));
    }

    @Override
    public Object state () {
        return null;
    }

    @Override
    public void release () {
    }
}