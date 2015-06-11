package code2html;

import java.util.Vector;
import javax.swing.text.Segment;

// Referenced classes of package org.gjt.sp.jedit.syntax:
//            SyntaxUtilities

public class KeywordMap
{
    class Keyword {
    public char keyword[];
    public byte id;
    public Keyword next;

    public Keyword(char keyword[], byte id, Keyword next)
    {
        this.keyword = keyword;
        this.id = id;
        this.next = next;
    }
    }


    public KeywordMap(boolean ignoreCase)
    {
        this(ignoreCase, 52);
        this.ignoreCase = ignoreCase;
        noWordSep = new StringBuffer();
    }

    public KeywordMap(boolean ignoreCase, int mapLength)
    {
        this.mapLength = mapLength;
        this.ignoreCase = ignoreCase;
        map = new Keyword[mapLength];
    }

    public byte lookup(Segment text, int offset, int length)
    {
        if(length == 0)
            return 0;
        for(Keyword k = map[getSegmentMapKey(text, offset, length)]; k != null;)
            if(length != k.keyword.length)
            {
                k = k.next;
            } else
            {
                if(SyntaxUtilities.regionMatches(ignoreCase, text, offset, k.keyword))
                    return k.id;
                k = k.next;
            }

        return 0;
    }

    public void add(String keyword, byte id)
    {
        add(keyword.toCharArray(), id);
    }

    public void add(char keyword[], byte id)
    {
        int key = getStringMapKey(keyword);
label0:
        for(int i = 0; i < keyword.length; i++)
        {
            char ch = keyword[i];
            if(Character.isLetterOrDigit(ch))
                continue;
            for(int j = 0; j < noWordSep.length(); j++)
                if(noWordSep.charAt(j) == ch)
                    continue label0;

            noWordSep.append(ch);
        }

        map[key] = new Keyword(keyword, id, map[key]);
    }

    public String getNonAlphaNumericChars()
    {
        return noWordSep.toString();
    }

    public String[] getKeywords()
    {
        Vector vector = new Vector(100);
        for(int i = 0; i < map.length; i++)
        {
            for(Keyword keyword = map[i]; keyword != null; keyword = keyword.next)
                vector.addElement(new String(keyword.keyword));

        }

        String retVal[] = new String[vector.size()];
        vector.copyInto(retVal);
        return retVal;
    }

    public boolean getIgnoreCase()
    {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase)
    {
        this.ignoreCase = ignoreCase;
    }

    public void add(KeywordMap map)
    {
        for(int i = 0; i < map.map.length; i++)
        {
            for(Keyword k = map.map[i]; k != null; k = k.next)
                add(k.keyword, k.id);

        }

    }

    private int getStringMapKey(char s[])
    {
        return (Character.toUpperCase(s[0]) + Character.toUpperCase(s[s.length - 1])) % mapLength;
    }

    protected int getSegmentMapKey(Segment s, int off, int len)
    {
        return (Character.toUpperCase(s.array[off]) + Character.toUpperCase(s.array[(off + len) - 1])) % mapLength;
    }

    private int mapLength;
    private Keyword map[];
    private boolean ignoreCase;
    private StringBuffer noWordSep;
}
