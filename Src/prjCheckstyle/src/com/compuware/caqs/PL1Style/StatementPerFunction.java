package com.compuware.caqs.PL1Style;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 27 mars 2006
 * Time: 14:24:47
 * To change this template use File | Settings | File Templates.
 */
public class StatementPerFunction {
    String[] contents;

    public StatementPerFunction(String dir){
        directoryManage directory=new  directoryManage(dir);
        String[] fileNames=directory.getFileNames();
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            CountNumberOfStatementPerFunc(fileNames[i]);
        }
    }

    private void CountNumberOfStatementPerFunc(String fichier){
        int line = 0;
        String procName=null;
        int num=0;
        int memNum=0;
        boolean dclFound=false;

        while (line < this.contents.length ){
            if(Constants.beginProc.matcher(this.contents[line]).matches()){
                num=0;
                procName= this.contents[line];

                line=line+1;

                while(line < this.contents.length && !Constants.beginProc.matcher(this.contents[line]).matches()  ){
                    if(Constants.beginDcl.matcher(this.contents[line]).matches()){
                        dclFound=true;
                    }
                    if(!Constants.instrDo.matcher(this.contents[line]).matches()
                            &&!Constants.findInclude.matcher(this.contents[line]).matches()
                            &&!Constants.findDcl.matcher(this.contents[line]).matches()
                            && !dclFound
                            && this.contents[line].indexOf(";")>0 ) {

                        if(num==0) memNum=line;
                        num=num+1;
                        if(num>50) {
                            if(!fichier.substring(0,fichier.length() -4).equals(
                                    procName.substring(procName.indexOf(" ")+1,procName.indexOf(":") )) &&
                                    procName.substring(procName.indexOf(" ")+1,procName.indexOf(":") ) != "MAIN"){
                                System.out.println("Dans "+fichier+" : "+
                                        procName.substring(procName.indexOf(" ")+1,procName.indexOf(":") )
                                        +" a plus de 50 instructions "+memNum);
                                break;                                          }
                        }
                    }
                    line=line+1;
                }
            }
            line=line+1;
        }
    }
}



