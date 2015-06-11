package com.compuware.caqs.PhpStyle;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 17:30:32
 * To change this template use File | Settings | File Templates.
 */
public class PHPConstants {

	//patterns pour les regles
	 public static final Pattern detectCl=Pattern.compile("^[\\s|\\t]*class\\s+.*$");
	 public static final Pattern detectFu=Pattern.compile("^[\\s|\\t]*function\\s+.*$");
	 public static final Pattern detectVar=Pattern.compile("^[\\s|\\t]*var\\s+\\$.*$");
	 public static final Pattern detectVL=Pattern.compile("^[\\s|\\t]*\\$\\w+\\s*=.*;.*$");
	 public static final Pattern detectCte=Pattern.compile("^[\\s|\\t]*define\\s*\\(.*\\)\\s*;\\s*$");
	 public static final Pattern detectReturn=Pattern.compile("^[\\s|\\t]*return\\s*\\$?[\\w|0-9|_]+\\s*;$");
	 public static final Pattern detectMultipleAffectation=Pattern.compile(
			 "^[^\\/|^\\*|^\\'|^\\\"|^#]*\\$\\w+\\s*=\\s*[\\$]?\\w+(\\s*=.*|\\+\\+.*|\\s*\\+=.*)$");
	 public static final Pattern detectLoopParameter=Pattern.compile("^[\\s|\\t]*(for|while|foreach)\\s*\\(.*\\)\\s*\\{?$");
    // patterns pour les vg
    //en double, pour gérer le kw en début de ligne
    public static final Pattern ifKw=Pattern.compile("^[^\\/|^\\']*\\sif\\s*\\(.*$");
    public static final Pattern elseIfKw=Pattern.compile("^[^\\/|^\\']*\\selseif\\s*\\(.*$");
    public static final Pattern questionKw=Pattern.compile("^[^\\/|^\\']*\\?.*$");
    public static final Pattern whileKw=Pattern.compile("^[^\\/|^\\']*\\swhile\\s*\\(.*$");
    public static final Pattern doKw=Pattern.compile("^[^\\/|^\\']*do[\\s|{]+.*$");
    public static final Pattern forKw=Pattern.compile("^[^\\/|^\\']*\\sfor[\\s|(]+.*$");
    public static final Pattern foreachKw=Pattern.compile("^[^\\/|^\\']*\\sforeach[\\s|(]+.*$");
    public static final Pattern caseKw=Pattern.compile("^[^\\/|^\\']*case\\s.*$");
    
    public static final Pattern ORKw=Pattern.compile("^[^\\/|^\\']*\\sor\\s.*$");
    public static final Pattern XORKw=Pattern.compile("^[^\\/|^\\']*\\sxor\\s.*$");
    public static final Pattern ANDKw=Pattern.compile("^[^\\/|^\\']*\\sand\\s.*$");
    public static final Pattern orKw=Pattern.compile("^[^\\/|^\\']*\\|\\|.*$");
    public static final Pattern andKw=Pattern.compile("^[^\\/|^\\']*&&.*$");

    //patterns pour le nombre de ligne de code
    public static final Pattern noSourceComment=Pattern.compile("^[\\s|\\t]*\\/.*$");
    public static final Pattern blankLine=Pattern.compile("^[\\s|\\t]*$");

    //patterns pour le nombre de lignes de commentaires
    public static final Pattern commKw=Pattern.compile("^.*[\\/\\/|#].*$");

    public static final Pattern blocCommKw=Pattern.compile("^[\\s|\\t]*\\/\\*.*$");

    //pattern pour trouver les mots clés
    // - mi (method implementation),
    // - fu(function)
    // - catch(catch externes considérés comme des fonctions)
    public static final Pattern fu=Pattern.compile("^.*fu}.*$");
    public static final Pattern mi=Pattern.compile("^.*mi}.*$");
    public static final Pattern catchFu=Pattern.compile("^.*catch fu}.*$");


}
