package com.compuware.caqs.metricGeneration;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 5 avr. 2006
 * Time: 11:42:00
 * To change this template use File | Settings | File Templates.
 */
public class XmlLogger extends AutomaticBean
        implements AuditListener{

    /**
     * Simple XML logger.
     * It outputs everything in UTF8 (default XML encoding is UTF8) in case
     * we want to localize error messages or simply that filenames are
     * localized and takes care about escaping as well.

     */

    /** decimal radix */
    private static final int BASE_10 = 10;

    /** hex radix */
    private static final int BASE_16 = 16;

    /** close output stream in auditFinished */
    private boolean mCloseStream;

    /** helper writer that allows easy encoding and printing */
    private PrintWriter mWriter;

    /** some known entities to detect */
    private static final String[] ENTITIES = {"gt", "amp", "lt", "apos",
                                              "quot", };

    /**
     * Creates a new <code>XMLLogger</code> instance.
     * Sets the output to a defined stream.
     * @param aOS the stream to write logs to.
     * @param aCloseStream close aOS in auditFinished
     */
    public XmlLogger(OutputStream aOS, boolean aCloseStream)
    {
        setOutputStream(aOS);
        mCloseStream = aCloseStream;
    }

    /**
     * sets the OutputStream
     * @param aOS the OutputStream to use
     **/
    private void setOutputStream(OutputStream aOS)
    {
        try {
            final OutputStreamWriter osw = new OutputStreamWriter(aOS, "UTF8");
            mWriter = new PrintWriter(osw);
        }
        catch (UnsupportedEncodingException e) {
            // unlikely to happen...
            throw new ExceptionInInitializerError(e);
        }
    }


    public void auditStarted(AuditEvent aEvt)
    {
        mWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        /*
        final ResourceBundle compilationProperties =
                ResourceBundle.getBundle("checkstylecompilation");
        final String version =
                compilationProperties.getString("checkstyle.compile.version");
        */
        mWriter.println("<JavaStyle>");
    }


    public void auditFinished(AuditEvent aEvt)
    {
        mWriter.println("</JavaStyle>");
        if (mCloseStream) {
            mWriter.close();
        }
        else {
            mWriter.flush();
        }
    }


    public void fileStarted(AuditEvent aEvt)
    {
        //mWriter.println("<file name=\"" + aEvt.getFileName() + "\">");
    }


    public void fileFinished(AuditEvent aEvt)
    {
        //mWriter.println("</file>");
    }


    public void addError(AuditEvent aEvt)
    {
        if (!SeverityLevel.IGNORE.equals(aEvt.getSeverityLevel())) {
            //mWriter.print("<metrique ");

            mWriter.print(aEvt.getMessage());
            //mWriter.println(" />");
        }
    }


    public void addException(AuditEvent aEvt, Throwable aThrowable)
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        pw.println("<exception>");
        pw.println("<![CDATA[");
        aThrowable.printStackTrace(pw);
        pw.println("]]>");
        pw.println("</exception>");
        pw.flush();
        mWriter.println(encode(sw.toString()));
    }

    /**
     * Escape &lt;, &gt; &amp; &apos; and &quot; as their entities.
     * @param aValue the value to escape.
     * @return the escaped value if necessary.
     */
    public String encode(String aValue)
    {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < aValue.length(); i++) {
            final char c = aValue.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    final int nextSemi = aValue.indexOf(";", i);
                    if ((nextSemi < 0)
                            || !isReference(aValue.substring(i, nextSemi + 1)))
                    {
                        sb.append("&amp;");
                    }
                    else {
                        sb.append('&');
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * @return whether the given argument a character or entity reference
     * @param aEnt the possible entity to look for.
     */
    public boolean isReference(String aEnt)
    {
        if (!(aEnt.charAt(0) == '&') || !aEnt.endsWith(";")) {
            return false;
        }

        if (aEnt.charAt(1) == '#') {
            int prefixLength = 2; // "&#"
            int radix = BASE_10;
            if (aEnt.charAt(2) == 'x') {
                prefixLength++;
                radix = BASE_16;
            }
            try {
                Integer.parseInt(
                        aEnt.substring(prefixLength, aEnt.length() - 1), radix);
                return true;
            }
            catch (NumberFormatException nfe) {
                return false;
            }
        }

        final String name = aEnt.substring(1, aEnt.length() - 1);
        for (int i = 0; i < ENTITIES.length; i++) {
            if (name.equals(ENTITIES[i])) {
                return true;
            }
        }
        return false;

    }
}
