package com.compuware.caqs.VBStyle;

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
            	//new EmptyCatchBlock(args[0]);
            	System.out.println("Module;LOC;VG;CLOC");
                new VBClassMetrics(args[0]);
                System.out.println("Function;LOC;VG;CLOC");
                new VBMethodMetrics(args[0]);
                //new detectDeadCode(args[0]);
            }else{
                System.out.println("erreur, pas de fichier ou de répertoire en entrée...");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("aie");}
    }
}
