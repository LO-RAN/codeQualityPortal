package code2html;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParserRuleSet.java


import gnu.regexp.RE;
import java.util.*;

// Referenced classes of package org.gjt.sp.jedit.syntax:
//            ParserRule, KeywordMap

public class ParserRuleSet
{

    public static ParserRuleSet getStandardRuleSet(byte id)
    {
        return standard[id];
    }

    public ParserRuleSet(String modeName, String setName)
    {
        terminateChar = -1;
        ignoreCase = true;
        this.modeName = modeName;
        this.setName = setName;
        ruleMapFirst = new ParserRule[128];
        ruleMapLast = new ParserRule[128];
        imports = new LinkedList();
    }

    public String getModeName()
    {
        return modeName;
    }

    public String getSetName()
    {
        return setName;
    }

    public String getName()
    {
        return modeName + "::" + setName;
    }

    public Hashtable getProperties()
    {
        return props;
    }

    public void setProperties(Hashtable props)
    {
        this.props = props;
        _noWordSep = null;
    }

    public void resolveImports()
    {
        for(Iterator iter = imports.iterator(); iter.hasNext();)
        {
            ParserRuleSet ruleset = (ParserRuleSet)iter.next();
            for(int i = 0; i < ruleset.ruleMapFirst.length; i++)
            {
                for(ParserRule rule = ruleset.ruleMapFirst[i]; rule != null; rule = rule.next)
                    addRule(rule);

            }

            if(ruleset.keywords != null)
            {
                if(keywords == null)
                    keywords = new KeywordMap(ignoreCase);
                keywords.add(ruleset.keywords);
            }
        }

        imports.clear();
    }

    public void addRuleSet(ParserRuleSet ruleset)
    {
        imports.add(ruleset);
    }

    public void addRule(ParserRule r)
    {
        ruleCount++;
        int key = Character.toUpperCase(r.hashChar) % 128;
        ParserRule last = ruleMapLast[key];
        if(last == null)
        {
            ruleMapFirst[key] = ruleMapLast[key] = r;
        } else
        {
            last.next = r;
            ruleMapLast[key] = r;
        }
    }

    public ParserRule getRules(char ch)
    {
        int key = Character.toUpperCase(ch) % 128;
        return ruleMapFirst[key];
    }

    public int getRuleCount()
    {
        return ruleCount;
    }

    public int getTerminateChar()
    {
        return terminateChar;
    }

    public void setTerminateChar(int atChar)
    {
        terminateChar = atChar < 0 ? -1 : atChar;
    }

    public boolean getIgnoreCase()
    {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean b)
    {
        ignoreCase = b;
    }

    public KeywordMap getKeywords()
    {
        return keywords;
    }

    public void setKeywords(KeywordMap km)
    {
        keywords = km;
        _noWordSep = null;
    }

    public boolean getHighlightDigits()
    {
        return highlightDigits;
    }

    public void setHighlightDigits(boolean highlightDigits)
    {
        this.highlightDigits = highlightDigits;
    }

    public RE getDigitRegexp()
    {
        return digitRE;
    }

    public void setDigitRegexp(RE digitRE)
    {
        this.digitRE = digitRE;
    }

    public ParserRule getEscapeRule()
    {
        return escapeRule;
    }

    public void setEscapeRule(ParserRule escapeRule)
    {
        addRule(escapeRule);
        this.escapeRule = escapeRule;
    }

    public byte getDefault()
    {
        return defaultToken;
    }

    public void setDefault(byte def)
    {
        defaultToken = def;
    }

    public String getNoWordSep()
    {
        if(_noWordSep == null)
        {
            _noWordSep = noWordSep;
            if(noWordSep == null)
                noWordSep = "";
            if(keywords != null)
                noWordSep += keywords.getNonAlphaNumericChars();
        }
        return noWordSep;
    }

    public void setNoWordSep(String noWordSep)
    {
        this.noWordSep = noWordSep;
        _noWordSep = null;
    }

    public boolean isBuiltIn()
    {
        return builtIn;
    }

    public String toString()
    {
        return getClass().getName() + "[" + modeName + "::" + setName + "]";
    }

    private static ParserRuleSet standard[];
    private static final int RULE_BUCKET_COUNT = 128;
    private String modeName;
    private String setName;
    private Hashtable props;
    private KeywordMap keywords;
    private int ruleCount;
    private ParserRule ruleMapFirst[];
    private ParserRule ruleMapLast[];
    private LinkedList imports;
    private int terminateChar;
    private boolean ignoreCase;
    private byte defaultToken;
    private ParserRule escapeRule;
    private boolean highlightDigits;
    private RE digitRE;
    private String _noWordSep;
    private String noWordSep;
    private boolean builtIn;

    static 
    {
        standard = new ParserRuleSet[19];
        for(byte i = 0; i < standard.length; i++)
        {
            standard[i] = new ParserRuleSet(null, null);
            standard[i].setDefault(i);
            standard[i].builtIn = true;
        }

    }
}
