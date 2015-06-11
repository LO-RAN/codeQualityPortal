package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 15:20:14
 * Pour chaque fichier .pl1, on verifie qu'il ne fait pas plus de 3000 lignes.
 */
public class FileLengthCheck {
    private static boolean OVER_MAX=false;
    public final static int MAX_LENGTH=3000;

    FileLengthCheck(String dir){
        try{
            directoryManage directory=new  directoryManage(dir);
            String[] fileNames=directory.getFileNames();

            for(int i =0; i<fileNames.length;i++){

                FileManage file=new FileManage(dir+"\\"+fileNames[i]);
                if(file.getFileLength()>MAX_LENGTH){
                    System.out.println("le fichier "+fileNames[i]+" dépasse les  "+MAX_LENGTH+" lignes");
                    OVER_MAX=true;
                }
            }
            if(!OVER_MAX) System.out.println("pas de source de plus de 3000 lignes.");
        }catch(Exception e){e.printStackTrace();}
    }

}
