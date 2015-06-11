/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.uniface.netbeans.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author cwfr-lizac
 */
public class UnifaceTokenId implements TokenId {

    private final String name;
    private final String primaryCategory;
    private final int id;

    UnifaceTokenId(
            String name,
            String primaryCategory,
            int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
    private static final Language<UnifaceTokenId> language = new UnifaceLanguageHierarchy().language();

    public static final Language<UnifaceTokenId> getLanguage() {
        return language;
    }
}
