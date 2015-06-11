package code2html;

import com.microstar.xml.HandlerBase;
import gnu.regexp.RE;
import gnu.regexp.REException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Stack;

// Referenced classes of package org.gjt.sp.jedit.syntax:
//            TokenMarker, ParserRuleSet, Token, KeywordMap, 
//            ParserRule

public abstract class XModeHandler extends HandlerBase
{

    public XModeHandler(String modeName)
    {
        lastDefaultID = 0;
        termChar = -1;
        lastIgnoreCase = true;
        this.modeName = modeName;
        marker = new TokenMarker();
        marker.addRuleSet(new ParserRuleSet(modeName, "MAIN"));
        stateStack = new Stack();
        lastNoWordSep = "_";
    }

    public Object resolveEntity(String publicId, String systemId)
    {
        if("xmode.dtd".equals(systemId))
            return new StringReader("<!-- -->");
        else
            return null;
    }

    public void attribute(String aname, String value, boolean isSpecified)
    {
        aname = aname != null ? aname.intern() : null;
        if(aname == "NAME")
            propName = value;
        else
        if(aname == "VALUE")
            propValue = value;
        else
        if(aname == "TYPE")
        {
            lastTokenID = Token.stringToToken(value);
            if(lastTokenID == -1)
                error("token-invalid", value);
        } else
        if(aname == "AT_LINE_START")
            lastAtLineStart = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "AT_WHITESPACE_END")
            lastAtWhitespaceEnd = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "AT_WORD_START")
            lastAtWordStart = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "NO_LINE_BREAK")
            lastNoLineBreak = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "NO_WORD_BREAK")
            lastNoWordBreak = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "NO_ESCAPE")
            lastNoEscape = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "EXCLUDE_MATCH")
            lastExcludeMatch = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "IGNORE_CASE")
            lastIgnoreCase = isSpecified ? value.equals("TRUE") : true;
        else
        if(aname == "HIGHLIGHT_DIGITS")
            lastHighlightDigits = isSpecified ? value.equals("TRUE") : false;
        else
        if(aname == "DIGIT_RE")
            lastDigitRE = value;
        else
        if(aname == "NO_WORD_SEP")
        {
            if(isSpecified)
                lastNoWordSep = value;
        } else
        if(aname == "AT_CHAR")
            try
            {
                if(isSpecified)
                    termChar = Integer.parseInt(value);
            }
            catch(NumberFormatException e)
            {
                error("termchar-invalid", value);
                termChar = -1;
            }
        else
        if(aname == "ESCAPE")
            lastEscape = value;
        else
        if(aname == "SET")
            lastSetName = value;
        else
        if(aname == "DELEGATE")
        {
            if(value != null)
            {
                int index = value.indexOf("::");
                String delegateMode;
                String delegateSetName;
                if(index != -1)
                {
                    delegateMode = value.substring(0, index);
                    delegateSetName = value.substring(index + 2);
                } else
                {
                    delegateMode = modeName;
                    delegateSetName = value;
                }
                TokenMarker delegateMarker = getTokenMarker(delegateMode);
                if(delegateMarker == null)
                {
                    error("delegate-invalid", value);
                } else
                {
                    lastDelegateSet = delegateMarker.getRuleSet(delegateSetName);
                    if(delegateMarker == marker && lastDelegateSet == null)
                    {
                        lastDelegateSet = new ParserRuleSet(delegateMode, delegateSetName);
                        lastDelegateSet.setDefault((byte)7);
                        marker.addRuleSet(lastDelegateSet);
                    } else
                    if(lastDelegateSet == null)
                        error("delegate-invalid", value);
                }
            }
        } else
        if(aname == "DEFAULT")
        {
            lastDefaultID = Token.stringToToken(value);
            if(lastDefaultID == -1)
            {
                error("token-invalid", value);
                lastDefaultID = 0;
            }
        } else
        if(aname == "HASH_CHAR")
            if(value.length() != 1)
            {
                error("hash-char-invalid", value);
                lastDefaultID = 0;
            } else
            {
                lastHashChar = value.charAt(0);
            }
    }

    public void doctypeDecl(String name, String publicId, String systemId)
        throws Exception
    {
        if("MODE".equalsIgnoreCase(name))
        {
            return;
        } else
        {
            error("doctype-invalid", name);
            return;
        }
    }

    public void charData(char c[], int off, int len)
    {
        String tag = peekElement();
        String text = new String(c, off, len);
        if(tag == "EOL_SPAN" || tag == "EOL_SPAN_REGEXP" || tag == "MARK_PREVIOUS" || tag == "MARK_FOLLOWING" || tag == "SEQ" || tag == "SEQ_REGEXP" || tag == "BEGIN")
        {
            lastStart = text;
            lastStartPosMatch = (lastAtLineStart ? 2 : 0) | (lastAtWhitespaceEnd ? 4 : 0) | (lastAtWordStart ? 8 : 0);
            lastAtLineStart = false;
            lastAtWordStart = false;
            lastAtWhitespaceEnd = false;
        } else
        if(tag == "END")
        {
            lastEnd = text;
            lastEndPosMatch = (lastAtLineStart ? 2 : 0) | (lastAtWhitespaceEnd ? 4 : 0) | (lastAtWordStart ? 8 : 0);
            lastAtLineStart = false;
            lastAtWordStart = false;
            lastAtWhitespaceEnd = false;
        } else
        {
            lastKeyword = text;
        }
    }

    public void startElement(String tag)
    {
        tag = pushElement(tag);
        if(tag == "WHITESPACE")
            System.out.println(modeName + ": WHITESPACE rule " + "no longer needed");
        else
        if(tag == "KEYWORDS")
            keywords = new KeywordMap(rules.getIgnoreCase());
        else
        if(tag == "RULES")
        {
            if(lastSetName == null)
                lastSetName = "MAIN";
            rules = marker.getRuleSet(lastSetName);
            if(rules == null)
            {
                rules = new ParserRuleSet(modeName, lastSetName);
                marker.addRuleSet(rules);
            }
            rules.setIgnoreCase(lastIgnoreCase);
            rules.setHighlightDigits(lastHighlightDigits);
            if(lastDigitRE != null)
                try
                {
                    rules.setDigitRegexp(new RE(lastDigitRE, lastIgnoreCase ? 2 : 0, ParserRule.RE_SYNTAX_JEDIT));
                }
                catch(REException e)
                {
                    error("regexp", e);
                }
            if(lastEscape != null)
                rules.setEscapeRule(ParserRule.createEscapeRule(lastEscape));
            rules.setDefault(lastDefaultID);
            rules.setNoWordSep(lastNoWordSep);
        }
    }

    public void endElement(String name)
    {
        if(name == null)
            return;
        String tag = popElement();
        if(name.equals(tag))
        {
            if(tag == "PROPERTY")
                props.put(propName, propValue);
            else
            if(tag == "PROPS")
            {
                if(peekElement().equals("RULES"))
                    rules.setProperties(props);
                else
                    modeProps = props;
                props = new Hashtable();
            } else
            if(tag == "RULES")
            {
                rules.setKeywords(keywords);
                keywords = null;
                lastSetName = null;
                lastEscape = null;
                lastIgnoreCase = true;
                lastHighlightDigits = false;
                lastDigitRE = null;
                lastDefaultID = 0;
                lastNoWordSep = "_";
                rules = null;
            } else
            if(tag == "IMPORT")
            {
                rules.addRuleSet(lastDelegateSet);
                lastDelegateSet = null;
            } else
            if(tag == "TERMINATE")
            {
                rules.setTerminateChar(termChar);
                termChar = -1;
            } else
            if(tag == "SEQ")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "SEQ");
                    return;
                }
                rules.addRule(ParserRule.createSequenceRule(lastStartPosMatch, lastStart, lastDelegateSet, lastTokenID));
                reset();
            } else
            if(tag == "SEQ_REGEXP")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "SEQ_REGEXP");
                    return;
                }
                try
                {
                    rules.addRule(ParserRule.createRegexpSequenceRule(lastHashChar, lastStartPosMatch, lastStart, lastDelegateSet, lastTokenID, lastIgnoreCase));
                }
                catch(REException re)
                {
                    error("regexp", re);
                }
                reset();
            } else
            if(tag == "SPAN")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "BEGIN");
                    return;
                }
                if(lastEnd == null)
                {
                    error("empty-tag", "END");
                    return;
                }
                rules.addRule(ParserRule.createSpanRule(lastStartPosMatch, lastStart, lastEndPosMatch, lastEnd, lastDelegateSet, lastTokenID, lastExcludeMatch, lastNoLineBreak, lastNoWordBreak, lastNoEscape));
                reset();
            } else
            if(tag == "SPAN_REGEXP")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "BEGIN");
                    return;
                }
                if(lastEnd == null)
                {
                    error("empty-tag", "END");
                    return;
                }
                try
                {
                    rules.addRule(ParserRule.createRegexpSpanRule(lastHashChar, lastStartPosMatch, lastStart, lastEndPosMatch, lastEnd, lastDelegateSet, lastTokenID, lastExcludeMatch, lastNoLineBreak, lastNoWordBreak, lastIgnoreCase, lastNoEscape));
                }
                catch(REException re)
                {
                    error("regexp", re);
                }
                reset();
            } else
            if(tag == "EOL_SPAN")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "EOL_SPAN");
                    return;
                }
                rules.addRule(ParserRule.createEOLSpanRule(lastStartPosMatch, lastStart, lastDelegateSet, lastTokenID, lastExcludeMatch));
                reset();
            } else
            if(tag == "EOL_SPAN_REGEXP")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "EOL_SPAN_REGEXP");
                    return;
                }
                try
                {
                    rules.addRule(ParserRule.createRegexpEOLSpanRule(lastHashChar, lastStartPosMatch, lastStart, lastDelegateSet, lastTokenID, lastExcludeMatch, lastIgnoreCase));
                }
                catch(REException re)
                {
                    error("regexp", re);
                }
                reset();
            } else
            if(tag == "MARK_FOLLOWING")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "MARK_FOLLOWING");
                    return;
                }
                rules.addRule(ParserRule.createMarkFollowingRule(lastStartPosMatch, lastStart, lastTokenID, lastExcludeMatch));
                reset();
            } else
            if(tag == "MARK_PREVIOUS")
            {
                if(lastStart == null)
                {
                    error("empty-tag", "MARK_PREVIOUS");
                    return;
                }
                rules.addRule(ParserRule.createMarkPreviousRule(lastStartPosMatch, lastStart, lastTokenID, lastExcludeMatch));
                reset();
            } else
            {
                byte token = Token.stringToToken(tag);
                if(token != -1)
                    addKeyword(lastKeyword, token);
            }
        } else
        {
            throw new InternalError();
        }
    }

    public void startDocument()
    {
        props = new Hashtable();
        pushElement(null);
    }

    public void endDocument()
    {
        ParserRuleSet rulesets[] = marker.getRuleSets();
        for(int i = 0; i < rulesets.length; i++)
            rulesets[i].resolveImports();

    }

    public TokenMarker getTokenMarker()
    {
        return marker;
    }

    public Hashtable getModeProperties()
    {
        return modeProps;
    }

    protected abstract void error(String s, Object obj);

    protected abstract TokenMarker getTokenMarker(String s);

    private void reset()
    {
        lastHashChar = '\0';
        lastStartPosMatch = 0;
        lastStart = null;
        lastEndPosMatch = 0;
        lastEnd = null;
        lastDelegateSet = null;
        lastTokenID = 0;
        lastExcludeMatch = false;
        lastNoLineBreak = false;
        lastNoWordBreak = false;
        lastNoEscape = false;
    }

    private void addKeyword(String k, byte id)
    {
        if(k == null)
        {
            error("empty-keyword", null);
            return;
        }
        if(keywords == null)
        {
            return;
        } else
        {
            keywords.add(k, id);
            return;
        }
    }

    private String pushElement(String name)
    {
        name = name != null ? name.intern() : null;
        stateStack.push(name);
        return name;
    }

    private String peekElement()
    {
        return (String)stateStack.peek();
    }

    private String popElement()
    {
        return (String)stateStack.pop();
    }

    private String modeName;
    private TokenMarker marker;
    private KeywordMap keywords;
    private Stack stateStack;
    private String propName;
    private String propValue;
    private Hashtable props;
    private Hashtable modeProps;
    private String lastStart;
    private String lastEnd;
    private String lastKeyword;
    private String lastSetName;
    private String lastEscape;
    private ParserRuleSet lastDelegateSet;
    private String lastNoWordSep;
    private ParserRuleSet rules;
    private byte lastDefaultID;
    private byte lastTokenID;
    private int termChar;
    private boolean lastNoLineBreak;
    private boolean lastNoWordBreak;
    private boolean lastExcludeMatch;
    private boolean lastIgnoreCase;
    private boolean lastHighlightDigits;
    private boolean lastAtLineStart;
    private boolean lastAtWhitespaceEnd;
    private boolean lastAtWordStart;
    private boolean lastNoEscape;
    private int lastStartPosMatch;
    private int lastEndPosMatch;
    private String lastDigitRE;
    private char lastHashChar;
}
