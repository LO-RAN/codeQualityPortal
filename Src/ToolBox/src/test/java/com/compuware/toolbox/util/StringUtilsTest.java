package com.compuware.toolbox.util;

import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * StringUtils Tester.
 *
 * @author cwfr-fdubois
 * @since <pre>30/05/2006</pre>
 * @version 1.0
 */
public class StringUtilsTest extends TestCase
{
    public StringUtilsTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetCollectionFromString() throws Exception
    {
        Collection result = null;
    	result = StringUtils.getCollectionFromString("one;two;three", ";");
        assertTrue(result.size() == 3);
        
    	result = StringUtils.getCollectionFromString("one;two;three;", ";");
        assertTrue(result.size() == 3);

    	result = StringUtils.getCollectionFromString("one", ";");
        assertTrue(result.size() == 1);

    	result = StringUtils.getCollectionFromString("", ";");
        assertTrue(result.size() == 0);
        
    	result = StringUtils.getCollectionFromString(null, ";");
        assertTrue(result.size() == 0);
        
    }

    public void testGetStringBetween() throws Exception
    {
        String result = null;
    	result = StringUtils.getStringBetween("com.compuware.caqs.test.Test.fonction(String[],Object)", '.', '(');
        assertEquals(result, "fonction");

        result = StringUtils.getStringBetween("com.compuware.caqs.test.Test.fonction", '.', '(');
        assertEquals(result, "fonction");

        result = StringUtils.getStringBetween("fonction(String[],Object)", '.', '(');
        assertEquals(result, "fonction");

        result = StringUtils.getStringBetween("fonction", '.', '(');
        assertEquals(result, "fonction");
    }

    public void testGetMaxString() throws Exception
    {
        String result = null;
    	result = StringUtils.getMaxString("012345", 10);
        assertEquals(result, "012345");
        
    	result = StringUtils.getMaxString("0123456789", 10);
        assertEquals(result, "0123456789");

    	result = StringUtils.getMaxString("0123456789012345", 10);
        assertEquals(result, "0123456789");
    }

    public void testStartsWithIgnoreCase() throws Exception
    {
        boolean result = false;
    	result = StringUtils.startsWithIgnoreCase("java 1.4", "java");
        assertEquals(result, true);
        
    	result = StringUtils.startsWithIgnoreCase("java 1.4", "jAVa");
        assertEquals(result, true);

        result = StringUtils.startsWithIgnoreCase("JaVa 1.4", "java");
        assertEquals(result, true);

        result = StringUtils.startsWithIgnoreCase("jav", "java");
        assertEquals(result, false);

        result = StringUtils.startsWithIgnoreCase(null, "java");
        assertEquals(result, false);
        
        result = StringUtils.startsWithIgnoreCase("java", null);
        assertEquals(result, false);
        
        result = StringUtils.startsWithIgnoreCase(null, null);
        assertEquals(result, true);
    }


    public static Test suite()
    {
        return new TestSuite(StringUtilsTest.class);
    }

}
