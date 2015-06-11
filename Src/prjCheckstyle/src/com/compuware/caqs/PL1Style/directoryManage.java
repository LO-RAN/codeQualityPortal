package com.compuware.caqs.PL1Style;

import java.io.*;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 15:52:46
 * Gestion du répertoire et de ses fichiers
 */
public class directoryManage {
    File dir;

    directoryManage(String dir){
        this.dir = new File(dir);
    }

    public String[] getFileNames(){
        String[] sources=null;
        try{
            sources=this.dir.list(new FilenameFilter()
        {
                public boolean accept(File d, String name){return name.endsWith(".pl1"); }
            });

        }catch(Exception e){e.printStackTrace();}
        return sources;
    }

}
