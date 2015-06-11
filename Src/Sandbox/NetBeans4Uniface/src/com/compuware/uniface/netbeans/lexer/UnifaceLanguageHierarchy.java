/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.uniface.netbeans.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

import static com.compuware.uniface.netbeans.jcclexer.UnifaceParserConstants.*;
/**
 *
 * @author cwfr-lizac
 */

public class UnifaceLanguageHierarchy extends LanguageHierarchy<UnifaceTokenId> {

    private static List<UnifaceTokenId>  tokens;
    private static Map<Integer,UnifaceTokenId>
                                    idToToken;

    private static void init () {
        tokens = Arrays.<UnifaceTokenId> asList (new UnifaceTokenId[] {
  new UnifaceTokenId ("EOF", "whitespace",                                  EOF),
  new UnifaceTokenId ("WHITESPACE", "whitespace",                           WHITESPACE),
  new UnifaceTokenId ("BR", "whitespace",                                   BR),
  new UnifaceTokenId ("COMMA", "separator",                                COMMA),
  new UnifaceTokenId ("COLON", "separator",                                COLON),
  new UnifaceTokenId ("SEMICOLON", "separator",                            SEMICOLON),
  new UnifaceTokenId ("DOLLAR", "operator",                               DOLLAR),
  new UnifaceTokenId ("LPAREN", "separator",                               LPAREN),
  new UnifaceTokenId ("RPAREN", "separator",                               RPAREN),
  new UnifaceTokenId ("LBRACKET", "separator",                             LBRACKET),
  new UnifaceTokenId ("RBRACKET", "separator",                             RBRACKET),
  new UnifaceTokenId ("AT", "separator",                                   AT),
  new UnifaceTokenId ("MULTIPLY", "operator",                             MULTIPLY),
  new UnifaceTokenId ("DIVIDE", "operator",                               DIVIDE),
  new UnifaceTokenId ("MODULO", "operator",                               MODULO),
  new UnifaceTokenId ("PLUS", "operator",                                 PLUS),
  new UnifaceTokenId ("MINUS", "operator",                                MINUS),
  new UnifaceTokenId ("EQUAL", "operator",                                EQUAL),
  new UnifaceTokenId ("LT", "operator",                                   LT),
  new UnifaceTokenId ("GT", "operator",                                   GT),
  new UnifaceTokenId ("EQ", "operator",                                   EQ),
  new UnifaceTokenId ("LE", "operator",                                   LE),
  new UnifaceTokenId ("GE", "operator",                                   GE),
  new UnifaceTokenId ("NE", "operator",                                   NE),
  new UnifaceTokenId ("PLUSASSIGN", "operator",                           PLUSASSIGN),
  new UnifaceTokenId ("MINUSASSIGN", "operator",                          MINUSASSIGN),
  new UnifaceTokenId ("STARASSIGN", "operator",                           STARASSIGN),
  new UnifaceTokenId ("SLASHASSIGN", "operator",                          SLASHASSIGN),
  new UnifaceTokenId ("REMASSIGN", "operator",                            REMASSIGN),
  new UnifaceTokenId ("NOT", "operator",                                  NOT),
  new UnifaceTokenId ("OR", "operator",                                   OR),
  new UnifaceTokenId ("AND", "operator",                                  AND),
  new UnifaceTokenId ("COMMENT", "comment",                              COMMENT),
  new UnifaceTokenId ("INTEGER_LITERAL", "number",                      INTEGER_LITERAL),
  new UnifaceTokenId ("DECIMAL_LITERAL", "number",                      DECIMAL_LITERAL),
  new UnifaceTokenId ("HEX_LITERAL", "number",                          HEX_LITERAL),
  new UnifaceTokenId ("OCTAL_LITERAL", "number",                        OCTAL_LITERAL),
  new UnifaceTokenId ("FLOATING_POINT_LITERAL", "number",               FLOATING_POINT_LITERAL),
  new UnifaceTokenId ("DECIMAL_FLOATING_POINT_LITERAL", "number",       DECIMAL_FLOATING_POINT_LITERAL),
  new UnifaceTokenId ("DECIMAL_EXPONENT", "number",                     DECIMAL_EXPONENT),
  new UnifaceTokenId ("HEXADECIMAL_FLOATING_POINT_LITERAL", "number",   HEXADECIMAL_FLOATING_POINT_LITERAL),
  new UnifaceTokenId ("HEXADECIMAL_EXPONENT", "number",                 HEXADECIMAL_EXPONENT),
  new UnifaceTokenId ("SYNTAX_STRING_LITERAL", "string",                SYNTAX_STRING_LITERAL),
  new UnifaceTokenId ("STRING_LITERAL", "string",                       STRING_LITERAL),
  new UnifaceTokenId ("PARAMS", "keyword",                               PARAMS),
  new UnifaceTokenId ("ENDPARAMS", "keyword",                            ENDPARAMS),
  new UnifaceTokenId ("VARIABLES", "keyword",                            VARIABLES),
  new UnifaceTokenId ("ENDVARIABLES", "keyword",                         ENDVARIABLES),
  new UnifaceTokenId ("IF", "keyword",                                   IF),
  new UnifaceTokenId ("ELSEIF", "keyword",                               ELSEIF),
  new UnifaceTokenId ("ELSE", "keyword",                                 ELSE),
  new UnifaceTokenId ("ENDIF", "keyword",                                ENDIF),
  new UnifaceTokenId ("WHILE", "keyword",                                WHILE),
  new UnifaceTokenId ("ENDWHILE", "keyword",                             ENDWHILE),
  new UnifaceTokenId ("REPEAT", "keyword",                               REPEAT),
  new UnifaceTokenId ("UNTIL", "keyword",                                UNTIL),
  new UnifaceTokenId ("SELECTCASE", "keyword",                           SELECTCASE),
  new UnifaceTokenId ("CASE", "keyword",                                 CASE),
  new UnifaceTokenId ("ELSECASE", "keyword",                             ELSECASE),
  new UnifaceTokenId ("ENDSELECTCASE", "keyword",                        ENDSELECTCASE),
  new UnifaceTokenId ("BOOLEAN", "keyword",                              BOOLEAN),
  new UnifaceTokenId ("DATE", "keyword",                                 DATE),
  new UnifaceTokenId ("DATETIME", "keyword",                             DATETIME),
  new UnifaceTokenId ("FLOAT", "keyword",                                FLOAT),
  new UnifaceTokenId ("HANDLE", "keyword",                               HANDLE),
  new UnifaceTokenId ("IMAGE", "keyword",                                IMAGE),
  new UnifaceTokenId ("LINEARDATE", "keyword",                           LINEARDATE),
  new UnifaceTokenId ("LINEARDATETIME", "keyword",                       LINEARDATETIME),
  new UnifaceTokenId ("LINEARTIME", "keyword",                           LINEARTIME),
  new UnifaceTokenId ("NUMERIC", "keyword",                              NUMERIC),
  new UnifaceTokenId ("RAW", "keyword",                                  RAW),
  new UnifaceTokenId ("STRING", "keyword",                               STRING),
  new UnifaceTokenId ("TIME", "keyword",                                 TIME),
  new UnifaceTokenId ("ANY", "keyword",                                  ANY),
  new UnifaceTokenId ("ENTITY", "keyword",                               ENTITY),
  new UnifaceTokenId ("OCCURRENCE", "keyword",                           OCCURRENCE),
  new UnifaceTokenId ("XMLSTREAM", "keyword",                            XMLSTREAM),
  new UnifaceTokenId ("FUNCTION", "keyword",                             FUNCTION),
  new UnifaceTokenId ("DELETEINSTANCE ", "keyword",                      DELETEINSTANCE),
  new UnifaceTokenId ("NEWINSTANCE", "keyword",                          NEWINSTANCE),
  new UnifaceTokenId ("SETFORMFOCUS", "keyword",                         SETFORMFOCUS),
  new UnifaceTokenId ("ENTITYCOPY", "keyword",                           ENTITYCOPY),
  new UnifaceTokenId ("ADDMONTHS", "keyword",                            ADDMONTHS),
  new UnifaceTokenId ("COMPARE", "keyword",                              COMPARE),
  new UnifaceTokenId ("DISPLAYLENGTH ", "keyword",                       DISPLAYLENGTH),
  new UnifaceTokenId ("LENGTH", "keyword",                               LENGTH),
  new UnifaceTokenId ("LOWERCASE", "keyword",                            LOWERCASE),
  new UnifaceTokenId ("RESET", "keyword",                                RESET),
  new UnifaceTokenId ("SCAN", "keyword",                                 SCAN),
  new UnifaceTokenId ("SET", "keyword",                                  SET),
  new UnifaceTokenId ("STRIPATTRIBUTES", "keyword",                      STRIPATTRIBUTES),
  new UnifaceTokenId ("UPPERCASE", "keyword",                            UPPERCASE),
  new UnifaceTokenId ("CLOSE", "keyword",                                CLOSE),
  new UnifaceTokenId ("COMMIT", "keyword",                               COMMIT),
  new UnifaceTokenId ("OPEN", "keyword",                                 OPEN),
  new UnifaceTokenId ("ROLLBACK", "keyword",                             ROLLBACK),
  new UnifaceTokenId ("SQL", "keyword",                                  SQL),
  new UnifaceTokenId ("ACTIVATE", "keyword",                             ACTIVATE),
  new UnifaceTokenId ("APEXIT", "keyword",                               APEXIT),
  new UnifaceTokenId ("APSTART", "keyword",                              APSTART),
  new UnifaceTokenId ("BREAK", "keyword",                                BREAK),
  new UnifaceTokenId ("DISPLAY", "keyword",                              DISPLAY),
  new UnifaceTokenId ("DONE", "keyword",                                 DONE),
  new UnifaceTokenId ("EDIT", "keyword",                                 EDIT),
  new UnifaceTokenId ("EXIT", "keyword",                                 EXIT),
  new UnifaceTokenId ("GOTO", "keyword",                                 GOTO),
  new UnifaceTokenId ("MACRO", "keyword",                                MACRO),
  new UnifaceTokenId ("PERFORM", "keyword",                              PERFORM),
  new UnifaceTokenId ("PULLDOWN", "keyword",                             PULLDOWN),
  new UnifaceTokenId ("RETURN", "keyword",                               RETURN),
  new UnifaceTokenId ("RUN", "keyword",                                  RUN),
  new UnifaceTokenId ("SPAWN", "keyword",                                SPAWN),
  new UnifaceTokenId ("IN", "keyword",                                   IN),
  new UnifaceTokenId ("OUT", "keyword",                                  OUT),
  new UnifaceTokenId ("INOUT", "keyword",                                INOUT),
  new UnifaceTokenId ("SYNC_SWITCH ", "keyword",                         SYNC_SWITCH),
  new UnifaceTokenId ("ASYNC_SWITCH", "keyword",                         ASYNC_SWITCH),
  new UnifaceTokenId ("ATTACHED_SWITCH", "keyword",                      ATTACHED_SWITCH),
  new UnifaceTokenId ("PREVIOUS_SWITCH", "keyword",                      PREVIOUS_SWITCH),
  new UnifaceTokenId ("NEXT_SWITCH", "keyword",                          NEXT_SWITCH),
  new UnifaceTokenId ("PRINT_SWITCH", "keyword",                         PRINT_SWITCH),
  new UnifaceTokenId ("FROM", "keyword",                                 FROM),
  new UnifaceTokenId ("IDENTIFIER ", "identifier",                          IDENTIFIER),
  new UnifaceTokenId ("LETTER", "keyword",                               LETTER),
  new UnifaceTokenId ("DIGIT", "keyword",                                DIGIT),
  new UnifaceTokenId ("UNDERSCORE", "keyword",                           UNDERSCORE),
  new UnifaceTokenId ("DOT", "keyword",                                  DOT),
  new UnifaceTokenId ("REGISTER", "keyword",                             REGISTER),
  new UnifaceTokenId ("GLOBALVARIABLE ", "keyword",                      GLOBALVARIABLE),
  new UnifaceTokenId ("COMPONENTVARIABLE", "keyword",                    COMPONENTVARIABLE),
  new UnifaceTokenId ("CMPSTATEMANAGEDBY", "keyword",                    CMPSTATEMANAGEDBY),
  new UnifaceTokenId ("COMPONENTNAME", "keyword",                        COMPONENTNAME),
  new UnifaceTokenId ("COMPONENTTYPE", "keyword",                        COMPONENTTYPE),
  new UnifaceTokenId ("ENTNAME", "keyword",                              ENTNAME),
  new UnifaceTokenId ("FIELDNAME", "keyword",                            FIELDNAME),
  new UnifaceTokenId ("LIBRARYNAME", "keyword",                          LIBRARYNAME),
  new UnifaceTokenId ("MODELNAME", "keyword",                            MODELNAME),
  new UnifaceTokenId ("TABLENAME", "keyword",                            TABLENAME),
  new UnifaceTokenId ("TRIGGERABBR", "keyword",                          TRIGGERABBR)
        });
        idToToken = new HashMap<Integer, UnifaceTokenId> ();
        for (UnifaceTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized UnifaceTokenId getToken (int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get (id);
    }

    @Override
    protected synchronized Collection<UnifaceTokenId> createTokenIds () {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<UnifaceTokenId> createLexer (LexerRestartInfo<UnifaceTokenId> info) {
        return new UnifaceLexer (info);
    }

    @Override
    protected String mimeType () {
        return "text/x-uniface";
    }
}