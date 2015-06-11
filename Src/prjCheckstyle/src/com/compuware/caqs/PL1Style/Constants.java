package com.compuware.caqs.PL1Style;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 30 mars 2006
 * Time: 15:10:44
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
    public static final Pattern endDo=Pattern.compile("^.*END[\\s]*;.*$");
    public static final Pattern beginIfDo=Pattern.compile("^.*DO[\\s]*;.*$");
    public static final Pattern blankLine=Pattern.compile( "^[\\s|\\t]*$");
    public static final Pattern CommentLine=Pattern.compile( "^[V]*\\*\\*.*$");
    public static final Pattern beginProc=Pattern.compile(".*\\w[\\s]*:[\\s]*PROC.*$");
    public static final Pattern instrDo=Pattern.compile("^.*DO;.*$");
    public static final Pattern findInclude=Pattern.compile("^.*INCLUDE.*;.*$");
    public static final Pattern findDcl=Pattern.compile("^.*DCL.*;.*$");
    public static final Pattern beginDcl=Pattern.compile("^.*DCL.*,[^;]*$");
}
