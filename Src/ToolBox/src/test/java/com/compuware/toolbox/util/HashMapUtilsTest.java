package com.compuware.toolbox.util;

import java.util.HashMap;

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
public class HashMapUtilsTest extends TestCase
{
    public HashMapUtilsTest(String name)
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

    public void testGet() throws Exception
    {
    	HashMap var = new HashMap();
    	var.put("COMMENTS", "COMMENTS");
    	var.put("LOC", "LOC");
    	
    	String[] keysNull = new String[] {"NOCL", "CLOC"}; 
    	String[] keysNotNull = new String[] {"NOCL", "LOC", "CLOC"}; 
    	
        Object result = null;
    	result = MapUtils.get(var, keysNull);
        assertNull(result);
        
    	result = MapUtils.get(var, keysNotNull);
        assertTrue(result.equals("LOC"));

    }

    public static Test suite()
    {
        return new TestSuite(HashMapUtilsTest.class);
    }
}
