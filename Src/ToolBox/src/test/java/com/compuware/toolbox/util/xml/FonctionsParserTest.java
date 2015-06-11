package com.compuware.toolbox.util.xml;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * FonctionsParser Tester.
 *
 * @author <Authors name>
 * @since <pre>05/26/2005</pre>
 * @version 1.0
 */
public class FonctionsParserTest extends TestCase
{
    Element         elt = null;

    public FonctionsParserTest(String name)
    {
        super(name);
    }

    public void setUp() throws Exception
    {
        super.setUp();
        SAXBuilder builder = new SAXBuilder();
        String basedir = System.getProperty("basedir");
        if (basedir == null) {
			basedir = "";
		}
		else {
			basedir = basedir + "/";
		}
        Document doc = builder.build(basedir+"src/resources/method-standard-java.modified.xml");
        if(doc!=null) {
            elt = doc.getRootElement();
        }
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testSetParseur() throws Exception
    {
    	FonctionsParser.setParseur(FonctionsParser.ENTREPRISE_ARCHITECT);
        assertTrue(FonctionsParser.getParser()==FonctionsParser.ENTREPRISE_ARCHITECT);
        FonctionsParser.setParseur(FonctionsParser.UNKNOWN_PARSER);
        assertTrue(FonctionsParser.getParser()==FonctionsParser.UNKNOWN_PARSER);
        FonctionsParser.setParseur(-12);
        assertTrue(FonctionsParser.getParser()==FonctionsParser.UNKNOWN_PARSER);
    }

    public void testGetPropertyString() throws Exception
    {
        //TODO: Test goes here...
    }

    public void testGetPropertyFloat() throws Exception
    {
        //TODO: Test goes here...
    }

    public void testGetPropertyInt() throws Exception
    {
        //TODO: Test goes here...
    }

    public void testFindElement() throws Exception
    {
        assertTrue(elt!=null);
        //<VAR id="MAGICNUMBERCHECK" type="NUMERIC"/>
        //premier test. On recherche une balise qui existe.
        Element e = FonctionsParser.findElement(elt, "VAR", "id", "MAGICNUMBERCHECK");
        assertTrue(e!=null);
        //et on teste les mêmes valeurs avec la balise en lowerCase.
        e = FonctionsParser.findElement(elt, "var", "id", "MAGICNUMBERCHECK");
        assertTrue(e==null);
        //deuxième test. On fait une recherche avec tous les éléments incorrects.
        e = FonctionsParser.findElement(elt, "var", "toto", "toto");
        assertTrue(e==null);
        //troisième test. On fait une recherche avec la balise juste et les autres éléments incorrects.
        e = FonctionsParser.findElement(elt, "VAR", "toto", "toto");
        assertTrue(e==null);
        //quatrième test. seul la valeur est fausse.
        e = FonctionsParser.findElement(elt, "VAR", "id", "toto");
        assertTrue(e==null);
    }

    public void testFindAllElements() throws Exception
    {
        assertTrue(elt!=null);
        ArrayList al = FonctionsParser.findAllElements(elt, "VAR", "id", "MAGICNUMBERCHECK");
        assertTrue(al.size()==1);
        al = FonctionsParser.findAllElements(elt, "VAR", "id", "toto");
        assertTrue(al.size()==0);
    }

    public void testFindAllBalises() throws Exception
    {
        assertTrue(elt!=null);
        ArrayList al = FonctionsParser.findAllBalises(elt, "CRITDEF");
        assertTrue(al.size()==4);
        al = FonctionsParser.findAllBalises(elt, "VAL");
        assertTrue(al.size()==9);
        al = FonctionsParser.findAllBalises(elt, "toot");
        assertTrue(al.isEmpty());
    }

    public void testFindFirstBalise() throws Exception
    {
        assertTrue(elt!=null);
        Element e = FonctionsParser.findFirstBalise(elt, "CRITDEF");
        assertTrue(e!=null);
        String value = e.getAttributeValue("id");
        assertTrue(value.equals("AVOIDSTARIMPORTCHECK"));
    }

    public static Test suite()
    {
        return new TestSuite(FonctionsParserTest.class);
    }

}
