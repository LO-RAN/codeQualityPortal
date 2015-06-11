package code2html;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParserRule.java

import gnu.regexp.*;

// Referenced classes of package org.gjt.sp.jedit.syntax:
//            ParserRuleSet

public class ParserRule
{

    public static final ParserRule createSequenceRule(int posMatch, String seq, ParserRuleSet delegate, byte id)
    {
        return new ParserRule(0, seq.charAt(0), posMatch, seq.toCharArray(), null, 0, null, delegate, id);
    }

    public static final ParserRule createRegexpSequenceRule(char hashChar, int posMatch, String seq, ParserRuleSet delegate, byte id, boolean ignoreCase)
        throws REException
    {
        return new ParserRule(8192, hashChar, posMatch, null, new RE("\\A" + seq, ignoreCase ? 2 : 0, RE_SYNTAX_JEDIT), 0, null, delegate, id);
    }

    public static final ParserRule createSpanRule(int startPosMatch, String start, int endPosMatch, String end, ParserRuleSet delegate, byte id, boolean excludeMatch, boolean noLineBreak, 
            boolean noWordBreak, boolean noEscape)
    {
        int ruleAction = 2 | (noLineBreak ? 0x200 : 0) | (excludeMatch ? 0x100 : 0) | (noWordBreak ? 0x400 : 0) | (noEscape ? 0x1000 : 0);
        return new ParserRule(ruleAction, start.charAt(0), startPosMatch, start.toCharArray(), null, endPosMatch, end.toCharArray(), delegate, id);
    }

    public static final ParserRule createRegexpSpanRule(char hashChar, int startPosMatch, String start, int endPosMatch, String end, ParserRuleSet delegate, byte id, boolean excludeMatch, 
            boolean noLineBreak, boolean noWordBreak, boolean ignoreCase, boolean noEscape)
        throws REException
    {
        int ruleAction = 0x2002 | (noLineBreak ? 0x200 : 0) | (excludeMatch ? 0x100 : 0) | (noWordBreak ? 0x400 : 0) | (noEscape ? 0x1000 : 0);
        return new ParserRule(ruleAction, hashChar, startPosMatch, null, new RE("\\A" + start, ignoreCase ? 2 : 0, RE_SYNTAX_JEDIT), endPosMatch, end.toCharArray(), delegate, id);
    }

    public static final ParserRule createEOLSpanRule(int posMatch, String seq, ParserRuleSet delegate, byte id, boolean excludeMatch)
    {
        int ruleAction = 0x10 | (excludeMatch ? 0x100 : 0) | 0x200;
        return new ParserRule(ruleAction, seq.charAt(0), posMatch, seq.toCharArray(), null, 0, null, delegate, id);
    }

    public static final ParserRule createRegexpEOLSpanRule(char hashChar, int posMatch, String seq, ParserRuleSet delegate, byte id, boolean excludeMatch, boolean ignoreCase)
        throws REException
    {
        int ruleAction = 0x2010 | (excludeMatch ? 0x100 : 0) | 0x200;
        return new ParserRule(ruleAction, hashChar, posMatch, null, new RE("\\A" + seq, ignoreCase ? 2 : 0, RE_SYNTAX_JEDIT), 0, null, delegate, id);
    }

    public static final ParserRule createMarkFollowingRule(int posMatch, String seq, byte id, boolean excludeMatch)
    {
        int ruleAction = 8 | (excludeMatch ? 0x100 : 0);
        return new ParserRule(ruleAction, seq.charAt(0), posMatch, seq.toCharArray(), null, 0, null, null, id);
    }

    public static final ParserRule createMarkPreviousRule(int posMatch, String seq, byte id, boolean excludeMatch)
    {
        int ruleAction = 4 | (excludeMatch ? 0x100 : 0);
        return new ParserRule(ruleAction, seq.charAt(0), posMatch, seq.toCharArray(), null, 0, null, null, id);
    }

    public static final ParserRule createEscapeRule(String seq)
    {
        int ruleAction = 2048;
        return new ParserRule(ruleAction, seq.charAt(0), 0, seq.toCharArray(), null, 0, null, null, (byte)0);
    }

    private ParserRule(int action, char hashChar, int startPosMatch, char start[], RE startRegexp, int endPosMatch, char end[], 
            ParserRuleSet delegate, byte token)
    {
        this.action = action;
        this.hashChar = hashChar;
        this.startPosMatch = startPosMatch;
        this.start = start;
        this.startRegexp = startRegexp;
        this.endPosMatch = endPosMatch;
        this.end = end;
        _flddelegate = delegate;
        this.token = token;
        if(_flddelegate == null && (action & 0xff) != 0)
            _flddelegate = ParserRuleSet.getStandardRuleSet(token);
    }

    public static final RESyntax RE_SYNTAX_JEDIT;
    public static final int MAJOR_ACTIONS = 255;
    public static final int SEQ = 0;
    public static final int SPAN = 2;
    public static final int MARK_PREVIOUS = 4;
    public static final int MARK_FOLLOWING = 8;
    public static final int EOL_SPAN = 16;
    public static final int ACTION_HINTS = 65280;
    public static final int EXCLUDE_MATCH = 256;
    public static final int NO_LINE_BREAK = 512;
    public static final int NO_WORD_BREAK = 1024;
    public static final int IS_ESCAPE = 2048;
    public static final int NO_ESCAPE = 4096;
    public static final int REGEXP = 8192;
    public static final int AT_LINE_START = 2;
    public static final int AT_WHITESPACE_END = 4;
    public static final int AT_WORD_START = 8;
    public final char hashChar;
    public final int startPosMatch;
    public final char start[];
    public final RE startRegexp;
    public final int endPosMatch;
    public final char end[];
    public final int action;
    public final byte token;
    public ParserRuleSet _flddelegate;
    public ParserRule next;

    static 
    {
        RE_SYNTAX_JEDIT = (new RESyntax(RESyntax.RE_SYNTAX_PERL5)).set(2).setLineSeparator("\n");
    }
}
