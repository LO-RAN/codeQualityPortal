package com.compuware.caqs.PL1Style;


/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 28 mars 2006
 * Time: 16:00:19
 * Tous les commentaires doivent être fermés.
 * On recherche les chaines '/*' et on vérifie qu'ils ont une fermeture
 */
public class AllOpenedCommentMustBeClosed {
    String[] contents;
    private static int checkC;
    private static int line;

    public AllOpenedCommentMustBeClosed(String dir){
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){

            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            checkC=0;
            line=0;
            if(checkComments(1)!=0) System.out.println("un commentaire non fermé dans le fichier "+fileNames[i]);
            else System.out.println("tous les commentaires sont fermés dans le fichier : "+fileNames[i]);
        }
    }
    
    private int checkComments( int indice){

        boolean ok=true;

        while (line < this.contents.length ){
            ok=true;

            while (ok){
                if((indice=this.contents[line].indexOf("/*",indice))>0 ){
                    checkC=checkC+1;
                    indice=indice+1;
                }else{
                    if((indice=this.contents[line].indexOf("*/",indice))>0 ){
                        checkC=checkC-1;
                        ok=false;
                    }else {

                        ok=false;
                    }
                }
            }
            indice=0;
            line=line+1;
        }

        return checkC;
    }


}
