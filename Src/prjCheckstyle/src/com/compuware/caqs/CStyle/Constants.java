package com.compuware.caqs.CStyle;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 17:30:32
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

	//	il faut aussi vérifier qu on a pas 2 _ a suivre car s est reserve pour les cte system
    public static final Pattern constantName=Pattern.compile("^[A-Z|_]+$");
    public static final Pattern functionName=Pattern.compile("^[a-z|_][a-z|A-Z|_|\\$]+$");
    public static final Pattern questionKw=Pattern.compile("^[^\\/|^#]*\\?:.*$");
    public static final Pattern whileKw=Pattern.compile("^[^\\/|^#]*\\s[w|W]hile\\s*\\(.*$");
    public static final Pattern while2Kw=Pattern.compile("^[w|W]hile\\s*\\(.*$");
    public static final Pattern forKw=Pattern.compile("^[^\\/|^#]*\\s[f|F]or\\s*\\(.*$");
    public static final Pattern for2Kw=Pattern.compile("^[f|F]or\\s*\\(.*$");
    public static final Pattern doKw=Pattern.compile("^[^\\/|^#]*[^a-zA-Z][d|D]o\\s*\\(.*$");
    public static final Pattern caseKw=Pattern.compile("^[^\\/|^#]*case.*$");
    public static final Pattern orKw=Pattern.compile("^[^\\/|^#]*\\|\\|.*$");
    public static final Pattern andKw=Pattern.compile("^[^\\/|^#]*&&.*$");

    //patterns pour le nombre de ligne de code
    public static final Pattern noSourceComment=Pattern.compile("^[\\s|\\t]*\\/.*$");
    public static final Pattern blankLine=Pattern.compile("^[\\s|\\t]*$");

    //patterns pour le nombre de lignes de commentaires
    public static final Pattern commKw=Pattern.compile("^.*\\/\\/.*$");

    public static final Pattern blocCommKw=Pattern.compile("^[\\s|\\t]*\\/\\*.*$");

    //pattern pour trouver les mots clés
    // - mi (method implementation),
    // - fu(function)
    // - catch(catch externes considérés comme des fonctions)
    public static final Pattern fu=Pattern.compile("^.*\\sfu\\}.*$");
    public static final Pattern mi=Pattern.compile("^.*\\smi\\}.*$");
    public static final Pattern catchFu=Pattern.compile("^.*catch fu;.*$");


}
