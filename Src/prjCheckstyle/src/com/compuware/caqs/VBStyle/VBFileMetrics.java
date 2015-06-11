package com.compuware.caqs.VBStyle;

import java.util.regex.Pattern;
import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:57:31
 * To change this template use File | Settings | File Templates.
 */
public class VBFileMetrics {
    String[] contents;

    public VBFileMetrics(String dir){
        int loc, vg, cloc;
        DirectoryManage directory=new  DirectoryManage(dir);
        String[] fileNames=directory.getFileNames(dir, "vb");
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(dir+"\\"+fileNames[i]);
            this.contents=file.getContents();
            loc=CountNumberOfLOC();
            vg=CountNumberOfVG();
            cloc=CountNumberOfCLOC();
            System.out.println(fileNames[i]+";"+loc+";"+vg+";"+cloc);
        }
    }

    private int CountNumberOfLOC(){
        Pattern lineOfCode = Pattern.compile("^[\\s|\\t|#]*[a-z|A-Z]+.*$");
        int tot=0;
          for(int i=0;i<contents.length;i++){
              if(lineOfCode.matcher(contents[i]).matches()){
                  tot=tot+1;
                 // System.out.println("loc "+tot+" : "+contents[i]);
              }
          }
        return tot;
    }

    private int CountNumberOfVG(){
        Pattern ifKey = Pattern.compile("^[^'|^#]*\\sIf[\\s|\\(].*$");
        Pattern loopKey = Pattern.compile("^[^'|^#]*\\sLoop\\s.*$");
        Pattern doKey = Pattern.compile("^[^'|^#]*\\sDo\\s.*$");
        Pattern forKey = Pattern.compile("^[^'|^#]*\\sFor\\s.*$");
        Pattern caseKey = Pattern.compile("^[^'|^#]*\\sCase\\s.*$");
        Pattern andKey = Pattern.compile("^[^'|^#]*\\sAnd\\s.*$");
        Pattern orKey = Pattern.compile("^[^'|^#]*\\sOr\\s.*$");

        int tot=1;
        for(int i=0;i<contents.length;i++){
            if(ifKey.matcher(contents[i]).matches() ||
                    loopKey.matcher(contents[i]).matches() ||
                    doKey.matcher(contents[i]).matches() ||
                    forKey.matcher(contents[i]).matches() ||
                    caseKey.matcher(contents[i]).matches() ||
                    andKey.matcher(contents[i]).matches() ||
                    orKey.matcher(contents[i]).matches()){
                tot=tot+1;
                //System.out.println("vg "+tot+" : "+contents[i]);
            }
        }
        return tot;
    }

    private int CountNumberOfCLOC(){
        Pattern clocKey = Pattern.compile("^[^#]*'.*$");

        int tot=0;
        for(int i=0;i<contents.length;i++){
            if(clocKey.matcher(contents[i]).matches()){
                tot=tot+1;
                //System.out.println("cloc "+tot+" : "+contents[i]);
            }
        }
        return tot;
    }

}