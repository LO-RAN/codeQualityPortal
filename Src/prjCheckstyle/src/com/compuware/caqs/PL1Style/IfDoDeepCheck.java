package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 16:10:29
 * Recherche des profondeurs des strucutres de type DO ou IF
 */
public class IfDoDeepCheck {
    String[] contents;
    private static int max;

    public IfDoDeepCheck(String dir){
        boolean OVER_DEEP=false;
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){

            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            max=0;
            lookAtIf(fileNames[i]);
        }
        if(!OVER_DEEP) System.out.println("pas de source avec une imbrication de IF-DO > 5");
    }



    private boolean lookAtIf(String fichier){
        boolean OVER_DEEP=false;
        int memDoLine=0;
        int num=0;
        int line =0;

        while (line < this.contents.length ){
            if( Constants.beginIfDo.matcher(contents[line]).matches() ){
                if(num==0) memDoLine=line;
                if(num>max) max= num;
                num=num+1;
            }else{
                if( Constants.endDo.matcher(contents[line]).matches() && num>0){
                    num=num-1;
                }
            }
            line=line+1;
            if(max>=5) {
                System.out.println(fichier +" !!! debut :" +memDoLine+" fin :"+line);
                OVER_DEEP=true;
                break;
            }
        }
        return OVER_DEEP;
    }
}




