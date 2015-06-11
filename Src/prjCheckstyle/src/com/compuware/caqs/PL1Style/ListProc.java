package com.compuware.caqs.PL1Style;

import java.util.regex.Pattern;


/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 30 mars 2006
 * Time: 12:16:05
 * Listing de toutes les procédures avec la forme MEMBRE.procédure
 */
public class ListProc {
    String[] contents;


    public ListProc(String dir){
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContentsWithoutBlanksAndComments();
            list(fileNames[i]);
        }
    }

    private void list(String fichier){

        int line=0;
        String procName=null;
        int procNumber=0;

        fichier=fichier.substring(0,fichier.indexOf(".") );
        while (line < this.contents.length ){
            Pattern endProc=Pattern.compile( "^.*\\sEND[\\s]"+procName+".*$");
            if( procName != null && endProc.matcher(this.contents[line]).matches() ){
                System.out.println("--> fin "+fichier +"."+procName);
            }
            if(Constants.beginProc.matcher(this.contents[line]).matches() &&  !this.contents[line].startsWith("V**") ){
                procNumber++;
                procName=this.contents[line];
                if(procName.indexOf(":")>0 ) {
                    procName=procName.substring(procName.indexOf(" ")+1,procName.indexOf(":"));
                }else{
                    procName=this.contents[line-1].substring(this.contents[line-1].indexOf(" ")+1,this.contents[line-1].indexOf(":"));;
                }
                if (procName.endsWith(" ")) procName=procName.substring(0,procName.length() -1);
                if (procName.startsWith(" ")) procName=procName.substring(procName.lastIndexOf(" ")+1 );
                if(procNumber==1)
                System.out.println(fichier +"."+procName );
                else  System.out.print("--> "+fichier +"."+procName );
            }
            line=line+1;
        }
    }
}
