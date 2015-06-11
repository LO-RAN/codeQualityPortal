package com.compuware.caqs.VBStyle;

import java.util.regex.Pattern;
import java.util.ArrayList;
import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:57:31
 * To change this template use File | Settings | File Templates.
 */
public class VBMetrics {
    private String[] contents;
    private int numSub;
    private String[][] subContents;
    private String[] subNames;

    public VBMetrics(String dir){
        int loc, vg, cloc;
        DirectoryManage directory=new  DirectoryManage(dir);
        String[] fileNames=directory.getFileNames(dir,"vb");
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            subdivideFile();
            for(int j=0;j<numSub;j++){
                loc=CountNumberOfLOC(j);
                vg=CountNumberOfVG(j);
                cloc=CountNumberOfCLOC(j);
                System.out.println(fileNames[i]+";"+subNames[j]+";"+loc+";"+vg+";"+cloc);
            }
        }
    }

    private void subdivideFile(){
        ArrayList l ;
        subNames=new String[100];
        subContents=new String[100][0];
        numSub=0;
        Pattern beginFunc = Pattern.compile("^[\\s|\\t]*(Private|Public)\\s+(Sub|Function|Property).*$");
        Pattern endFunc = Pattern.compile("^[\\s|\\t]*End\\s+(Sub|Function|Property).*$");
        for(int i =0; i < this.contents.length;i++){
            if(beginFunc.matcher(contents[i]).matches()){
                subNames[numSub]= contents[i].substring(contents[i].indexOf(" ",10)+1 ) ;

                while(contents[i].indexOf(" _") > -1){

                    subNames[numSub]=subNames[numSub]+contents[i+1] ;
                    i=i+1;
                }
                int j=0;
                l = new ArrayList();
                while(i < this.contents.length && ! endFunc.matcher(contents[i]).matches()){
                    l.add(contents[i]);
                    j=j+1;
                    i=i+1;
                }
                l.add(contents[i]);
               String[] t = new String[j];
                subContents[numSub]= new String[j];
               subContents[numSub]=(String[]) l.toArray(t);
                numSub=numSub+1;
            }
        }
    }

    private int CountNumberOfLOC(int numFunc){
        Pattern lineOfCode = Pattern.compile("^[\\s|\\t|#]*[a-z|A-Z]+.*$");
        int tot=0;
          for(int i=0;i<subContents[numFunc].length;i++){
              if(lineOfCode.matcher(subContents[numFunc][i]).matches()){
                  tot=tot+1;
              }
          }
        return tot;
    }

    private int CountNumberOfVG(int numFunc){
        Pattern ifKey = Pattern.compile("^[^'|^#]*If[\\s|\\(].*$");
        Pattern loopKey = Pattern.compile("^[^'|^#]*Loop\\s.*$");
        Pattern doKey = Pattern.compile("^[^'|^#]*Do\\s.*$");
        Pattern forKey = Pattern.compile("^[^'|^#]*For\\s.*$");
        Pattern caseKey = Pattern.compile("^[^'|^#]*Case\\s.*$");
        Pattern andKey = Pattern.compile("^[^'|^#]*And\\s.*$");
        Pattern orKey = Pattern.compile("^[^'|^#]*Or\\s.*$");

        int tot=1;
        for(int i=0;i<subContents[numFunc].length;i++){
            if(ifKey.matcher(subContents[numFunc][i]).matches() ||
                    loopKey.matcher(subContents[numFunc][i]).matches() ||
                    doKey.matcher(subContents[numFunc][i]).matches() ||
                    forKey.matcher(subContents[numFunc][i]).matches() ||
                    caseKey.matcher(subContents[numFunc][i]).matches() ||
                    andKey.matcher(subContents[numFunc][i]).matches() ||
                    orKey.matcher(subContents[numFunc][i]).matches()){
                tot=tot+1;
            }
        }
        return tot;
    }

    private int CountNumberOfCLOC(int numFunc){
        Pattern clocKey = Pattern.compile("^[^#]*'.*$");
        int tot=0;
        for(int i=0;i<subContents[numFunc].length;i++){
            if(clocKey.matcher(subContents[numFunc][i]).matches()){
                tot=tot+1;
            }
        }
        return tot;
    }
}