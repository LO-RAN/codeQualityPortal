package code2html;
import gnu.regexp.RE;
import gnu.regexp.REMatch;
import java.util.Hashtable;
import javax.swing.text.Segment;

public class TokenMarker
{
    class LineContext {
        /**
         * @return
         */
        public LineContext intern()
        {
            Object obj = intern.get(this);
            if(obj == null)
            {
                intern.put(this, this);
                return this;
            } else
            {
                return (LineContext)obj;
            }
        }

        public int hashCode()
        {
            if(inRule != null)
                return inRule.hashCode();
            if(rules != null)
                return rules.hashCode();
            else
                return 0;
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof LineContext)
            {
                LineContext lc = (LineContext)obj;
                return lc.inRule == inRule && lc.rules == rules && objectsEqual(parent, lc.parent) && charArraysEqual(spanEndSubst, lc.spanEndSubst);
            } else
            {
                return false;
            }
        }

        public boolean objectsEqual(Object o1, Object o2)
        {
            if(o1 == null)
                return o2 == null;
            if(o2 == null)
                return false;
            else
                return o1.equals(o2);
        }

        public Object clone()
        {
            LineContext lc = new LineContext();
            lc.inRule = inRule;
            lc.rules = rules;
            lc.parent = parent != null ? (LineContext)parent.clone() : null;
            lc.spanEndSubst = spanEndSubst;
            return lc;
        }

        private boolean charArraysEqual(char c1[], char c2[])
        {
            if(c1 == null)
                return c2 == null;
            if(c2 == null)
                return c1 == null;
            if(c1.length != c2.length)
                return false;
            for(int i = 0; i < c1.length; i++)
                if(c1[i] != c2[i])
                    return false;

            return true;
        }

        private Hashtable intern = new Hashtable();
        public LineContext parent;
        public ParserRule inRule;
        public ParserRuleSet rules;
        public char spanEndSubst[];


        public LineContext(ParserRuleSet rs, LineContext lc)
        {
            rules = rs;
            parent = lc != null ? (LineContext)lc.clone() : null;
        }

        public LineContext()
        {
        }

    }


    public TokenMarker()
    {
        pattern = new Segment();
        ruleSets = new Hashtable(64);
    }

    public void addRuleSet(ParserRuleSet rules)
    {
        ruleSets.put(rules.getSetName(), rules);
        if(rules.getSetName().equals("MAIN"))
            mainRuleSet = rules;
    }

    public ParserRuleSet getMainRuleSet()
    {
        return mainRuleSet;
    }

    public ParserRuleSet getRuleSet(String setName)
    {
        return (ParserRuleSet)ruleSets.get(setName);
    }

    public ParserRuleSet[] getRuleSets()
    {
        return (ParserRuleSet[])ruleSets.values().toArray(new ParserRuleSet[ruleSets.size()]);
    }

    public LineContext markTokens(LineContext prevContext, TokenHandler tokenHandler, Segment line)
    {
        this.tokenHandler = tokenHandler;
        this.line = line;
        lastOffset = line.offset;
        lineLength = line.count + line.offset;
        context = new LineContext();
        if(prevContext == null)
        {
            context.rules = getMainRuleSet();
        } else
        {
            context.parent = prevContext.parent;
            context.inRule = prevContext.inRule;
            context.rules = prevContext.rules;
            context.spanEndSubst = prevContext.spanEndSubst;
        }
        keywords = context.rules.getKeywords();
        escaped = false;
        seenWhitespaceEnd = false;
        whitespaceEnd = line.offset;
        int terminateChar = context.rules.getTerminateChar();
        boolean terminated = false;
label0:
        for(pos = line.offset; pos < lineLength; pos++)
        {
        	
        	
            if(terminateChar >= 0 && pos - line.offset >= terminateChar && !terminated)
            {
                terminated = true;
                context = new LineContext(ParserRuleSet.getStandardRuleSet(context.rules.getDefault()), context);
                keywords = context.rules.getKeywords();
            }
            if(context.parent != null)
            {
                ParserRule rule = context.parent.inRule;
                if(rule != null && checkDelegateEnd(rule))
                {
                    seenWhitespaceEnd = true;
                    continue;
                }
            }
            char ch = line.array[pos];
            for(ParserRule rule = context.rules.getRules(ch); rule != null; rule = rule.next)
            {
                if(!handleRule(rule, false))
                    continue;
                seenWhitespaceEnd = true;
                continue label0;
            }

            if(Character.isWhitespace(ch))
            {
                if(!seenWhitespaceEnd)
                    whitespaceEnd = pos + 1;
                if(context.inRule != null)
                    handleRule(context.inRule, true);
                handleNoWordBreak();
                markKeyword(false);
                if(lastOffset != pos)
                    tokenHandler.handleToken(line, context.rules.getDefault(), lastOffset - line.offset, pos - lastOffset, context);
                tokenHandler.handleToken(line, context.rules.getDefault(), pos - line.offset, 1, context);
                lastOffset = pos + 1;
                escaped = false;
            } else
            {
                if(keywords != null || context.rules.getRuleCount() != 0)
                {
                    String noWordSep = context.rules.getNoWordSep();
                    if(!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)
                    {
                        if(context.inRule != null)
                            handleRule(context.inRule, true);
                        handleNoWordBreak();
                        markKeyword(true);
                        tokenHandler.handleToken(line, context.rules.getDefault(), lastOffset - line.offset, 1, context);
                        lastOffset = pos + 1;
                    }
                }
                seenWhitespaceEnd = true;
                escaped = false;
            }
        }

        pos = lineLength;
        if(context.inRule != null)
            handleRule(context.inRule, true);
        handleNoWordBreak();
        markKeyword(true);
        while(context.parent != null) 
        {
            ParserRule rule = context.parent.inRule;
            if((rule == null || (rule.action & 0x200) != 512) && !terminated)
                break;
            context = context.parent;
            keywords = context.rules.getKeywords();
            context.inRule = null;
        }
        tokenHandler.handleToken(line, (byte)127, pos - line.offset, 0, context);
        context = context.intern();
        //tokenHandler.setLineContext(context);
        this.line = null;
        return context;
    }

    private boolean checkDelegateEnd(ParserRule rule)
    {
        if(rule.end == null)
            return false;
        LineContext tempContext = context;
        context = context.parent;
        keywords = context.rules.getKeywords();
        boolean tempEscaped = escaped;
        boolean b = handleRule(rule, true);
        context = tempContext;
        keywords = context.rules.getKeywords();
        if(b && !tempEscaped)
        {
            if(context.inRule != null)
                handleRule(context.inRule, true);
            markKeyword(true);
            context = (LineContext)context.parent.clone();
            tokenHandler.handleToken(line, (context.inRule.action & 0x100) != 256 ? context.inRule.token : context.rules.getDefault(), pos - line.offset, pattern.count, context);
            keywords = context.rules.getKeywords();
            context.inRule = null;
            lastOffset = pos + pattern.count;
            pos += pattern.count - 1;
            return true;
        }
        if((rule.action & 0x1000) == 0)
        {
            ParserRule escape = context.parent.rules.getEscapeRule();
            if(escape != null && handleRule(escape, false))
                return true;
        }
        return false;
    }

    private boolean handleRule(ParserRule checkRule, boolean end)
    {
        if(!end && Character.toUpperCase(checkRule.hashChar) != Character.toUpperCase(line.array[pos]))
            return false;
        int offset = (checkRule.action & 4) == 0 ? pos : lastOffset;
        int posMatch = end ? checkRule.endPosMatch : checkRule.startPosMatch;
        if((posMatch & 2) == 2)
        {
            if(offset != line.offset)
                return false;
        } else
        if((posMatch & 4) == 4)
        {
            if(offset != whitespaceEnd)
                return false;
        } else
        if((posMatch & 8) == 8 && offset != lastOffset)
            return false;
        int matchedChars = 1;
        CharIndexedSegment charIndexed = null;
        REMatch match = null;
        if(!end || (checkRule.action & 8) == 0)
            if((checkRule.action & 0x2000) == 0 || end)
            {
                if(end)
                {
                    if(context.spanEndSubst != null)
                        pattern.array = context.spanEndSubst;
                    else
                        pattern.array = checkRule.end;
                } else
                {
                    pattern.array = checkRule.start;
                }
                pattern.offset = 0;
                pattern.count = pattern.array.length;
                matchedChars = pattern.count;
                if(!SyntaxUtilities.regionMatches(context.rules.getIgnoreCase(), line, pos, pattern.array))
                    return false;
            } else
            {
                int matchStart = pos - line.offset;
                charIndexed = new CharIndexedSegment(line, matchStart);
                match = checkRule.startRegexp.getMatch(charIndexed, 0, 64);
                if(match == null)
                    return false;
                if(match.getStartIndex() != 0)
                    throw new InternalError("Can't happen");
                matchedChars = match.getEndIndex();
                if(matchedChars == 0)
                    matchedChars = 1;
            }
        if((checkRule.action & 0x800) == 2048)
        {
            if(context.inRule != null)
                handleRule(context.inRule, true);
            escaped = !escaped;
            pos += pattern.count - 1;
        } else
        if(escaped)
        {
            escaped = false;
            pos += pattern.count - 1;
        } else
        if(!end)
        {
            if(context.inRule != null)
                handleRule(context.inRule, true);
            markKeyword((checkRule.action & 4) != 4);
            switch(checkRule.action & 0xff)
            {
            case 0: // '\0'
                context.spanEndSubst = null;
                if((checkRule.action & 0x2000) != 0)
                    handleTokenWithSpaces(tokenHandler, checkRule.token, pos - line.offset, matchedChars, context);
                else
                    tokenHandler.handleToken(line, checkRule.token, pos - line.offset, matchedChars, context);
                if(checkRule._flddelegate != null)
                {
                    context = new LineContext(checkRule._flddelegate, context.parent);
                    keywords = context.rules.getKeywords();
                }
                break;

            case 2: // '\002'
            case 16: // '\020'
                context.inRule = checkRule;
                byte tokenType = (checkRule.action & 0x100) != 256 ? checkRule.token : context.rules.getDefault();
                if((checkRule.action & 0x2000) != 0)
                    handleTokenWithSpaces(tokenHandler, tokenType, pos - line.offset, matchedChars, context);
                else
                    tokenHandler.handleToken(line, tokenType, pos - line.offset, matchedChars, context);
                char spanEndSubst[] = (char[])null;
                if(charIndexed != null && checkRule.end != null)
                    spanEndSubst = substitute(match, checkRule.end);
                context.spanEndSubst = spanEndSubst;
                context = new LineContext(checkRule._flddelegate, context);
                keywords = context.rules.getKeywords();
                break;

            case 8: // '\b'
                tokenHandler.handleToken(line, (checkRule.action & 0x100) != 256 ? checkRule.token : context.rules.getDefault(), pos - line.offset, pattern.count, context);
                context.spanEndSubst = null;
                context.inRule = checkRule;
                break;

            case 4: // '\004'
                context.spanEndSubst = null;
                if((checkRule.action & 0x100) == 256)
                {
                    if(pos != lastOffset)
                        tokenHandler.handleToken(line, checkRule.token, lastOffset - line.offset, pos - lastOffset, context);
                    tokenHandler.handleToken(line, context.rules.getDefault(), pos - line.offset, pattern.count, context);
                } else
                {
                    tokenHandler.handleToken(line, checkRule.token, lastOffset - line.offset, (pos - lastOffset) + pattern.count, context);
                }
                break;

            default:
                throw new InternalError("Unhandled major action");
            }
            pos += matchedChars - 1;
            lastOffset = pos + 1;
        } else
        if((context.inRule.action & 8) != 0)
        {
            if(pos != lastOffset)
                tokenHandler.handleToken(line, context.inRule.token, lastOffset - line.offset, pos - lastOffset, context);
            lastOffset = pos;
            context.inRule = null;
        }
        return true;
    }

    private void handleNoWordBreak()
    {
        if(context.parent != null)
        {
            ParserRule rule = context.parent.inRule;
            if(rule != null && (context.parent.inRule.action & 0x400) != 0)
            {
                if(pos != lastOffset)
                    tokenHandler.handleToken(line, rule.token, lastOffset - line.offset, pos - lastOffset, context);
                lastOffset = pos;
                context = context.parent;
                keywords = context.rules.getKeywords();
                context.inRule = null;
            }
        }
    }

    private void handleTokenWithSpaces(TokenHandler tokenHandler, byte tokenType, int start, int len, LineContext context)
    {
        int last = start;
        int end = start + len;
        for(int i = start; i < end; i++)
            if(Character.isWhitespace(line.array[i + line.offset]))
            {
                if(last != i)
                    tokenHandler.handleToken(line, tokenType, last, i - last, context);
                tokenHandler.handleToken(line, tokenType, i, 1, context);
                last = i + 1;
            }

        if(last != end)
            tokenHandler.handleToken(line, tokenType, last, end - last, context);
    }

    private void markKeyword(boolean addRemaining)
    {
        int len = pos - lastOffset;
        if(len == 0)
            return;
        if(context.rules.getHighlightDigits())
        {
            boolean digit = false;
            boolean mixed = false;
            for(int i = lastOffset; i < pos; i++)
            {
                char ch = line.array[i];
                if(Character.isDigit(ch))
                    digit = true;
                else
                    mixed = true;
            }

            if(mixed)
            {
                RE digitRE = context.rules.getDigitRegexp();
                if(digit)
                    if(digitRE == null)
                    {
                        digit = false;
                    } else
                    {
                        CharIndexedSegment seg = new CharIndexedSegment(line, false);
                        int oldCount = line.count;
                        int oldOffset = line.offset;
                        line.offset = lastOffset;
                        line.count = len;
                        if(!digitRE.isMatch(seg))
                            digit = false;
                        line.offset = oldOffset;
                        line.count = oldCount;
                    }
            }
            if(digit)
            {
                tokenHandler.handleToken(line, (byte)5, lastOffset - line.offset, len, context);
                lastOffset = pos;
                return;
            }
        }
        if(keywords != null)
        {
            byte id = keywords.lookup(line, lastOffset, len);
            if(id != 0)
            {
                tokenHandler.handleToken(line, id, lastOffset - line.offset, len, context);
                lastOffset = pos;
                return;
            }
        }
        if(addRemaining)
        {
            tokenHandler.handleToken(line, context.rules.getDefault(), lastOffset - line.offset, len, context);
            lastOffset = pos;
        }
    }

    private char[] substitute(REMatch match, char end[])
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < end.length; i++)
        {
            char ch = end[i];
            if(ch == '$')
            {
                if(i == end.length - 1)
                {
                    buf.append(ch);
                } else
                {
                    char digit = end[i + 1];
                    if(!Character.isDigit(digit))
                    {
                        buf.append(ch);
                    } else
                    {
                        buf.append(match.toString(digit - 48));
                        i++;
                    }
                }
            } else
            {
                buf.append(ch);
            }
        }

        char returnValue[] = new char[buf.length()];
        buf.getChars(0, buf.length(), returnValue, 0);
        return returnValue;
    }

    private Hashtable ruleSets;
    private ParserRuleSet mainRuleSet;
    private TokenHandler tokenHandler;
    private Segment line;
    private LineContext context;
    private KeywordMap keywords;
    private Segment pattern;
    private int lastOffset;
    private int lineLength;
    private int pos;
    private boolean escaped;
    private int whitespaceEnd;
    private boolean seenWhitespaceEnd;
}
