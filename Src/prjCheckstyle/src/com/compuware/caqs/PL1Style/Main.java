package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 16:08:03
 * Analyse des fichiers PL/I
 */
public final class Main {

    public static void main(String[] args){
        try{

            if(args.length > 0){
                System.out.println("audit du répertoire : "+args[0]);
                new FileLengthCheck(args[0]);
                new IfDoDeepCheck(args[0]);
                new OneStatementPerLine(args[0]);
                new StatementPerFunction(args[0]);
                new AllOpenedCommentMustBeClosed(args[0]);
                new nonInitializedData(args[0]);
                new ProcNeverCalled(args[0]);
                new ListProc(args[0]);
            }else{
                System.out.println("erreur, pas de fichier ou de répertoire en entrée...");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("aie");}
    }
}
