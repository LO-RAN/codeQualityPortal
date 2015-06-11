package com.compuware.caqs.CppStyle;

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
    public static final Pattern ifKw=Pattern.compile("^[^\\/|^#]*[^\\w][i|I]f\\s*\\(.*$");
	//public static final Pattern ifKw=Pattern.compile("^[^\\w][i|I]f[^\\w]$");
    public static final Pattern if2Kw=Pattern.compile("^[i|I]f\\s*\\(.*$");
    public static final Pattern questionKw=Pattern.compile("^[^\\/|^#]*\\?:.*$");
    public static final Pattern whileKw=Pattern.compile("^[^\\/|^#]*[^\\w][w|W]hile\\s*\\(.*$");
    public static final Pattern while2Kw=Pattern.compile("^[w|W]hile[^\\w]?\\s*\\(.*$");
    public static final Pattern forKw=Pattern.compile("^[^\\/|^#]*\\s*[^\\w]?[f|F]or\\s*\\(.*$");
    public static final Pattern for2Kw=Pattern.compile("^[f|F]or\\s*\\(.*$");
    public static final Pattern doKw=Pattern.compile("^[^\\/|^#]*[^\\w]?[d|D]o\\s*\\(.*$");
    public static final Pattern do2Kw=Pattern.compile("^[d|D]o([^w].*)?$");
    public static final Pattern switchKw=Pattern.compile("^[^\\/|^#]*[^\\w]?switch[^\\w]?.*$");
    public static final Pattern caseKw=Pattern.compile("^[^\\/|^#]*[^\\w]?case[^\\w]?.*$");
    public static final Pattern orKw=Pattern.compile("^[^\\/|^#]*\\|\\|.*$");
    public static final Pattern andKw=Pattern.compile("^[^\\/|^#]*&&.*$");
    public static final Pattern tryKw=Pattern.compile("^[^\\/|^#]*[^\\w]try[^\\w].*$");
    public static final Pattern catchKw=Pattern.compile("^[^\\/|^#]*[^\\w]catch[^\\w].*$");

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
    public static final Pattern returnKw=Pattern.compile("^\\s+return[\\s|\\(].*;.*$");

}
