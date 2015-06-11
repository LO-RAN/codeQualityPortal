package com.compuware.caqs.PL1Style;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 29 mars 2006
 * Time: 19:15:41
 * Listing de toutes les procédures, par fichier, n'étant pas appelées par son propre membre.
 * On oubliera pas qu'un include marche comme une macro
 * Ce résultat est donc a utiliser avec prudence
 */
public class ProcNeverCalled {
    private String[] contents;

    public ProcNeverCalled(String dir){
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            checkProcAreCalled(fileNames[i]);
        }
    }

    private boolean checkProcAreCalled(String fichier){

        String regexp;
        String procName=null;
        int procNumber=0;
        boolean callFound=false;
        int i=0;

        while (i<this.contents.length ){

            if(Constants.beginProc.matcher(this.contents[i]).matches() && !this.contents[i].startsWith("V**") ){
                procNumber=procNumber+1;
                callFound=false;
                if(this.contents[i].indexOf(":")>0 ) {
                    procName=this.contents[i].substring(this.contents[i].indexOf(" ")+1,this.contents[i].indexOf(":"));

                }else{
                    procName=this.contents[i-1].substring(this.contents[i-1].indexOf(" ")+1,this.contents[i-1].indexOf(":"));;

                }
                if (procName.endsWith(" ")) procName=procName.substring(0,procName.length() -1);
                if (procName.startsWith(" ")) procName=procName.substring(procName.lastIndexOf(" ")+1 );
                int j=0;
                regexp="^.*CALL[\\s]*"+procName+".*$";
                Pattern callProc=Pattern.compile(regexp);
                while (j<this.contents.length ){
                    if(callProc.matcher(this.contents[j]).matches()){
                        callFound=true;
                        break;
                    }
                    j=j+1;
                }
                if(!callFound && procNumber>1) System.out.println("la proc '"+procName+"' ligne "+i+" n est pas appelee dans le fichier "+fichier+" ("+procNumber+" proc)");

            }
            i=i+1;
        }


        return callFound;

    }

}
