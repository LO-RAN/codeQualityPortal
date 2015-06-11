package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 29 mars 2006
 * Time: 16:35:18
 * Recherche des variables non initialisées
 * Pour cela, on recherche toutes les variables déclarées,
 * sauf celles suivies d'un DEF ou d'un BASED
 */
public class nonInitializedData {
    private String[] contents;

    public nonInitializedData(String dir){
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){

            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            find(fileNames[i]);
        }
    }

    private void find(String fichier){
        int ind=0;
        int i2=0;
        int memLine=0;
        String var=null;
        boolean initTrouve=false;

        for(int i=0;i<this.contents.length;i++){
            ind=0;
            initTrouve=false;
            if((ind=this.contents[i].indexOf("DCL "))>0
                    && this.contents[i].indexOf(" DEF ")==0
                    && this.contents[i].indexOf(" BASED")==0
                    && this.contents[i].indexOf(" INIT")==0){
                memLine=i;
                var=this.contents[i].substring(ind+4,this.contents[i].indexOf(" ",ind+4) );

                i2=i;

                while (i2<this.contents.length){
                    if(this.contents[i2].indexOf(var+"=")>0 || this.contents[i2].indexOf(var+" =")>0){
                        initTrouve=true;
                        break;
                    }
                    i2=i2+1;
                }
                if(!initTrouve) System.out.println(fichier+" line "+memLine+": non init : "+var);
                else System.out.println(fichier+" ok ");

            }

        }
    }
}
