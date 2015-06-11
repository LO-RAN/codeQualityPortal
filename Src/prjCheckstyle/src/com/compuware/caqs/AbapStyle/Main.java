package com.compuware.caqs.AbapStyle;

import com.compuware.caqs.VBStyle.VBClassMetrics;
import com.compuware.caqs.VBStyle.VBMethodMetrics;
import com.compuware.caqs.VBStyle.detectDeadCode;

public class Main {
	public static void main(String[] args){
        try{

            if(args.length > 0){
            	//new EmptyCatchBlock(args[0]);
            	System.out.println("Module;LOC;VG;CLOC");
                new ABAPFileMetrics(args[0]);
                System.out.println("Function;LOC;VG;CLOC");
                new ABAPFunctionMetrics(args[0]);
                
            }else{
                System.out.println("erreur, pas de fichier ou de répertoire en entrée...");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("aie");}
    }
}
