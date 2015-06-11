package com.compuware.caqs.CobolStyle;




/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 18:00:36
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args){
        try{

            if(args.length > 0){
                System.out.println("audit Cobol du répertoire : "+args[0]);
                
                //new Metrics(args[0]);
                //new CallList(args[0]);

            }else{
                System.out.println("erreur, pas de fichier ou de répertoire en entrée...");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("aïeu");}
    }
}
