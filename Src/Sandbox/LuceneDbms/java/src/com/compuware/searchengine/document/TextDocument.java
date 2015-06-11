package com.compuware.searchengine.document;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateField;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 21 nov. 2005
 * Time: 15:06:44
 * To change this template use File | Settings | File Templates.
 */
public class TextDocument {

      /** Makes a document for a File.
        <p>
        The document has three fields:
        <ul>
        <li><code>path</code>--containing the pathname of the file, as a stored,
        tokenized field;
        <li><code>modified</code>--containing the last modified date of the file as
        a keyword field as encoded by <a
        href="lucene.document.DateField.html">DateField</a>; and
        <li><code>contents</code>--containing the full contents of the file, as a
        Reader field;
        */
      public static Document Document(String[] fields, String[] values)
           throws java.io.FileNotFoundException {

            // make a new, empty document
            Document doc = new Document();
            for (int i = 0; i < fields.length; i++) {
                System.out.println("Adding filed="+fields[i]+", value="+values[i]);
                doc.add(Field.Text(fields[i], values[i]));
            }

            // return the document
            return doc;
      }

      private TextDocument() {}
}

