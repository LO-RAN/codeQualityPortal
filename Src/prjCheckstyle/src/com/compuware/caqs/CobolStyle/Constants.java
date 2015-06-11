package com.compuware.caqs.CobolStyle;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 17:30:32
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    // patterns pour les vg
    //en double, pour gérer le kw en début de ligne
    public static final Pattern ifKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sIF\\s*.*$");
    public static final Pattern endIfKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sEND-IF\\s*.*$");
    public static final Pattern performKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\s(PERFORM|CALL|go\\sto|GO\\sTO|EXEC|exec)(\\s|\\.)\\s*.*$");
    public static final Pattern whenKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sWHEN\\s*.*$");
    public static final Pattern readKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sREAD\\s*.*$");
    public static final Pattern onExceptionKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sON\\sEXCEPTION\\s*.*$");
    public static final Pattern onOverflowKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sON\\sOVERFLOW\\s*.*$");
    public static final Pattern onSizeErrorKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sON\\sSIZE\\sERROR\\s*.*$");
    public static final Pattern onInvalidKeyKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sON\\sINVALID\\sKEY\\s*.*$");
    public static final Pattern atEndKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sAT END\\s*.*$");
    public static final Pattern atEndOfPageKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sAT\\sEND_OF_PAGE\\s*.*$");
    public static final Pattern or1Kw=Pattern.compile("^([0-9]{6})?[^\\*]*\\s\\|.*$");
    public static final Pattern or2Kw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sOR.*$");
    public static final Pattern and1Kw=Pattern.compile("^([0-9]{6})?[^\\*]*\\s&.*$");
    public static final Pattern and2Kw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sAND.*$");

    //patterns pour le nombre de ligne de code
    public static final Pattern noSourceComment=Pattern.compile("^([0-9]{6})?[\\s|\\t]*\\*.*$");
    public static final Pattern blankLine=Pattern.compile("^([0-9]{6})?[\\s|\\t]*$");

    //patterns pour le nombre de lignes de commentaires
    public static final Pattern commKw=Pattern.compile("^.*\\*.*$");

    public static final Pattern dot=Pattern.compile("^.*\\..*$");

    //pattern pour trouver les mots clés
    // - mi (method implementation),
    // - fu(function)
    // - catch(catch externes considérés comme des fonctions)
    public static final Pattern fu=Pattern.compile("^.*fu}.*$");
    public static final Pattern mi=Pattern.compile("^.*mi}.*$");
    public static final Pattern catchFu=Pattern.compile("^.*catch fu}.*$");

    public static final Pattern callCALLKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sCALL\\s.*$");
    public static final Pattern callPERFORMKw=Pattern.compile("^([0-9]{6})?[^\\*]*\\sPERFORM\\s.*$");

}
