package com.compuware.caqs.metricsCollector;

import java.io.File;
import java.util.*;
import net.sourceforge.pmd.PMD;
import java.util.regex.Pattern;
import java.util.ArrayList;

import com.compuware.caqs.CPD.*;
import com.compuware.caqs.CPD.CPDNullListener;
import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:57:31
 * To change this template use File | Settings | File Templates.
 */
public class VBMethodMetrics {
	private CPDListener listener = new CPDNullListener();
    private String[] contents;
    private int numSub;
    private String[][] subContents;
    private String[] subNames;
    public static String results;
    
    Hashtable ht=new Hashtable();
    
    public VBMethodMetrics(String dir){
        int loc, vg, cloc;
        results="";
        if(new File(dir).isDirectory()){
        DirectoryManage directory=new  DirectoryManage(dir);
        String[] fileNames=directory.getFileNames(dir, "vb");
       // System.out.println("il y a "+fileNames.length +" fichiers");
        for(int i =0; i<fileNames.length;i++){
            FileManage file=new FileManage(fileNames[i]);
            this.contents=file.getContents();
           // System.out.println("-------------"+fileNames[i]+"--------------");
            subdivideFile();
            for(int j=0;j<numSub;j++){
                loc=CountNumberOfLOC(j);
                vg=CountNumberOfVG(j);
                cloc=CountNumberOfCLOC(j);
                subNames[j]=subNames[j].replaceAll("\\s{2,}"," ");
                //subNames[j]=subNames[j].substring(subNames[j].lastIndexOf(".")+1);
                results+=fileNames[i].substring(0,fileNames[i].lastIndexOf("."))+"."+subNames[j]+";"+loc+";"+vg+";"+cloc+PMD.EOL;
                //System.out.println(fileNames[i].substring(0,fileNames[i].lastIndexOf("."))+"."+subNames[j]+";"+loc+";"+vg+";"+cloc);
                
            	ht.put(fileNames[i].substring(0,fileNames[i].lastIndexOf("."))+"."+subNames[j],new String(loc+";"+vg+";"+cloc));
            	
            }
            GUI.phaseLabel.setText("calculating metrics : "+fileNames[i]);
            double ratio=((double)((double)(i+1)/(double)fileNames.length))*100;
            System.err.println(i+" sur "+fileNames.length+"progress : "+ratio);
            GUI.tokenizingFilesBar.setValue((int)ratio);
        }
        }else{
        		
                FileManage file=new FileManage(dir);
                this.contents=file.getContents();
                subdivideFile();
                for(int j=0;j<numSub;j++){
                    loc=CountNumberOfLOC(j);
                    vg=CountNumberOfVG(j);
                    cloc=CountNumberOfCLOC(j);
                    results+=dir+"."+subNames[j]+";"+loc+";"+vg+";"+cloc;
                    System.out.println(dir+"."+subNames[j]+";"+loc+";"+vg+";"+cloc);
                }
            
        }
    }

    public Hashtable getHashTable(){
    	
    	return ht;
    	
    }
    
    private void subdivideFile(){
        ArrayList l ;
        String portee="Private|Public|Protected";
        String carac="Overloads|Overrides|Shadows|ReadOnly|WriteOnly|\\s";
        String typeFunc="Sub|Function|Property";
        String interf="Interface";
        subNames=new String[1000];
        subContents=new String[1000][0];
        boolean endNeeded=true;
        numSub=0;
        //[\\s|\\t]*(<[a-z|A-Z|0-9|\\(|\\)|\\.]>)*[\\s|\\t]*(Private|Public|Protected)?\\s+(Overloads|Overrides|Shadows|ReadOnly|WriteOnly|\\s)*(Sub|Function|Property).*
        //^[\\s|\\t]*(<([a-z|A-Z|0-9|\\(|\\)|\\.])*>)?[\\s|\\t]*(Private|Public|Protected|Friend)?\\s+(Overloads|Overrides|Shadows|ReadOnly|WriteOnly|Shared|Overridable|MustOverride|MustInherit|\\s)*(Sub|Function|Property).*$
        Pattern beginFunc = Pattern.compile("^[\\s|\\t]*(<([a-z|A-Z|0-9|\\(|\\)|\\.])*>)?[\\s|\\t]*(Private|Public|Protected|Friend)?\\s*(Overloads|Overrides|Shadows|ReadOnly|WriteOnly|Shared|Overridable|MustOverride|MustInherit|Delegate|Static|\\s)*(Sub|Function|Property)\\s.*$");
        Pattern endFunc = Pattern.compile("^[\\s|\\t]*End\\s+(Sub|Function|Property)[\\s]?.*$");
        Pattern detectInterf=Pattern.compile("^[\\s|\\t]*("+portee+")[\\s|\\t]+"+interf+"\\s+.*$");
        for(int i =0; i < this.contents.length;i++){
        	if(detectInterf.matcher(contents[i]).matches()){
        		endNeeded=false;
        	}
            if(beginFunc.matcher(contents[i]).matches()){
            	System.out.println(numSub+" fonction detectee : "+contents[i]);
            	if(contents[i].indexOf("Sub")>-1){
            		subNames[numSub]= contents[i].substring(contents[i].indexOf("Sub")+4);
            		System.out.println(numSub+"  fonction detectee : "+subNames[numSub]);
            	}else{
            		if(contents[i].indexOf("Function")>-1){
            			subNames[numSub]= contents[i].substring(contents[i].indexOf("Function")+9);
            		}else{
            			subNames[numSub]= contents[i].substring(contents[i].indexOf("Property")+9);
            		}
            	}
                //subNames[numSub]= contents[i].substring(contents[i].indexOf(" ",10)+1 ) ;
            	//System.out.println("fonction détectée : "+subNames[numSub]);
                while(contents[i].indexOf(" _") > -1){

                    subNames[numSub]=subNames[numSub].substring(0,subNames[numSub].indexOf(" _"))+contents[i+1];
                    
                    i=i+1;
                }
                if(subNames[numSub].indexOf(")")>-1){
                	subNames[numSub]=subNames[numSub].substring(0,subNames[numSub].indexOf(")")+1);
                	
                }
                int j=0;
                l = new ArrayList();
                while(i < this.contents.length && ! endFunc.matcher(contents[i]).matches() && endNeeded){
                    l.add(contents[i]);
                  
                    j=j+1;
                    i=i+1;
                }
                //System.out.println("ajout"+contents[i]);
                //l.add(contents[i]);
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
        /*Pattern ifKey = Pattern.compile("^[^'|^#]*If[\\s|\\(].*$");
        Pattern loopKey = Pattern.compile("^[^'|^#]*Loop\\s.*$");
        Pattern doKey = Pattern.compile("^[^'|^#]*Do\\s.*$");
        Pattern forKey = Pattern.compile("^[^'|^#]*For\\s.*$");
        Pattern caseKey = Pattern.compile("^[^'|^#]*Case\\s.*$");
        Pattern andKey = Pattern.compile("^[^'|^#]*And\\s.*$");
        Pattern orKey = Pattern.compile("^[^'|^#]*Or\\s.*$");*/

        Pattern ifKey = Pattern.compile("^[^'|^#]*\\sIf[\\s|\\(].*$");
		Pattern loopKey = Pattern.compile("^[^'|^#]*\\sLoop\\s.*$");
		Pattern doKey = Pattern.compile("^[^'|^#]*\\sDo\\s.*$");
		Pattern forKey = Pattern.compile("^[^'|^#]*\\sFor\\s.*$");
		Pattern caseKey = Pattern.compile("^[^'|^#]*\\sCase\\s.*$");
		Pattern andKey = Pattern.compile("^[^'|^#]*\\sAnd\\s.*$");
		Pattern orKey = Pattern.compile("^[^'|^#]*\\sOr\\s.*$");
        
        
        int tot=1;
        for(int i=0;i<subContents[numFunc].length;i++){
            if(ifKey.matcher(subContents[numFunc][i]).matches() ||
                    loopKey.matcher(subContents[numFunc][i]).matches() ||
                    doKey.matcher(subContents[numFunc][i]).matches() ||
                    forKey.matcher(subContents[numFunc][i]).matches() ||
                    caseKey.matcher(subContents[numFunc][i]).matches() ||
                    andKey.matcher(subContents[numFunc][i]).matches() ||
                    orKey.matcher(subContents[numFunc][i]).matches()){
            	//System.out.println("keyword trouve : "+subContents[numFunc][i]);
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