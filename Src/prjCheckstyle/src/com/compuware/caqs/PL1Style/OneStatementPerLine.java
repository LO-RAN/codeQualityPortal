package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 1 juin 2006
 * Time: 11:54:24
 * To change this template use File | Settings | File Templates.
 */
public class OneStatementPerLine {
    private String[] contents;

    public OneStatementPerLine(String dir){
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
        int line=0;

        for(int i=0;i<this.contents.length;i++){
            ind=0;
            if((!Constants.CommentLine.matcher(this.contents[i]).matches()) && (ind=this.contents[i].indexOf(";")) >0){
                if(this.contents[i].indexOf(";", ind+1)>0){
                    line =i+1;
                System.out.println(fichier+" : More than one statement on the line "+line+" : "+this.contents[i]);
                }
            }
        }
    }
}
