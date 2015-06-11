package com.compuware.caqs.AbapStyle;

import java.io.File;
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
public class ABAPFunctionMetrics {
	private String[] contents;
	private int numSub;
	private String[][] subContents;
	private String[] subNames;
	
	public ABAPFunctionMetrics(String dir){
		int loc, vg, cloc;
		
		if(new File(dir).isDirectory()){
			DirectoryManage directory=new  DirectoryManage(dir);
			String[] fileNames=directory.getFileNames(dir, "abap");
			for(int i =0; i<fileNames.length;i++){
				FileManage file=new FileManage(fileNames[i]);
				this.contents=file.getContents();
				
				subdivideFile();
				for(int j=0;j<numSub;j++){
					loc=CountNumberOfLOC(j);
					vg=CountNumberOfVG(j);
					cloc=CountNumberOfCLOC(j);
					
					subNames[j]=subNames[j].substring(subNames[j].lastIndexOf(".")+1);
					
					System.out.println(fileNames[i].substring(0,fileNames[i].lastIndexOf("."))+"."+subNames[j]+";"+loc+";"+vg+";"+cloc);
				}
			}
		}else{
			
			FileManage file=new FileManage(dir);
			this.contents=file.getContents();
			subdivideFile();
			for(int j=0;j<numSub;j++){
				loc=CountNumberOfLOC(j);
				vg=CountNumberOfVG(j);
				cloc=CountNumberOfCLOC(j);
				System.out.println(dir+"."+subNames[j]+";"+loc+";"+vg+";"+cloc);
			}
			
		}
	}
	
	private void subdivideFile(){
		ArrayList l ;
		subNames=new String[1000];
		subContents=new String[1000][0];
		numSub=0;
		String rep="FORM";
		
		for(int i =0; i < this.contents.length;i++){
			int ind1=0;
			int ind2=0;
			if(Constants.functionBegin.matcher(contents[i]).matches()){			
				if((ind1=contents[i].toUpperCase().indexOf("FORM"))>-1){
					if((ind2=(contents[i].indexOf(" ",ind1+7)))>-1){
						subNames[numSub]= contents[i].substring(ind1+5,ind2);
						
					}else{
						subNames[numSub]= contents[i].substring(ind1+5);
						
					}
					
					if((ind2=subNames[numSub].indexOf("."))>-1){							
						subNames[numSub]= subNames[numSub].substring(0,ind2);
						
					}
					
					
				}else{
					if((ind1=contents[i].toUpperCase().indexOf("MODULE"))>-1){
						if((ind2=(contents[i].indexOf(" ",ind1+8)))>-1){
							subNames[numSub]= contents[i].substring(ind1+7,ind2);
						}else{
							if((ind2=contents[i].indexOf(".",ind1+8))>-1){
								subNames[numSub]= contents[i].substring(ind1+7,ind2);
							}else{
								subNames[numSub]= contents[i].substring(ind1+7);}
						}
					}else{
						if((ind1=contents[i].toUpperCase().indexOf("FUNCTION"))>-1){
							
							if((ind2=(contents[i].indexOf(" ",ind1+11)))>-1){
								
								subNames[numSub]= contents[i].substring(ind1+9,ind2);
							}else{
								if((ind2=contents[i].indexOf(".",ind1+11))>-1){
									subNames[numSub]= contents[i].substring(ind1+9,ind2);
								}else{
									subNames[numSub]= contents[i].substring(ind1+9);}
							}
						}else{
							if((ind1=contents[i].toUpperCase().indexOf("METHOD"))>-1){
								
								if((ind2=(contents[i].indexOf(" ",ind1+11)))>-1){
									
									subNames[numSub]= contents[i].substring(ind1+7,ind2);
								}else{
									if((ind2=contents[i].indexOf(".",ind1+11))>-1){
										subNames[numSub]= contents[i].substring(ind1+7,ind2);
									}else{
										subNames[numSub]= contents[i].substring(ind1+7);}
								}
						}
						
					}
				}
				}
				
				int j=0;
				l = new ArrayList();
				while(! Constants.functionEnd.matcher(contents[i]).matches()){
					l.add(contents[i]);
					
					j=j+1;
					i=i+1;
				}
				String[] t = new String[j];
				subContents[numSub]= new String[j];
				subContents[numSub]=(String[]) l.toArray(t);
				if(Constants.functionEnd.matcher(contents[i]).matches()){
					
					numSub=numSub+1;
				}
			}
		}
	}
	
	private int CountNumberOfLOC(int numFunc){
		
		int tot=0;
		
		for(int i=0;i<subContents[numFunc].length;i++){
			if(Constants.sourceCodeLine.matcher(subContents[numFunc][i]).matches()){
				tot=tot+1;
			}
		}
		return tot;
	}
	
	private int CountNumberOfVG(int numFunc){
		
		int tot=1;
		for(int i=0;i<subContents[numFunc].length;i++){
			if(Constants.kw.matcher(subContents[numFunc][i]).matches()){
				//System.out.println("kw : "+subContents[numFunc][i]);
				tot=tot+1;
			}
		}
		return tot;
	}
	
	private int CountNumberOfCLOC(int numFunc){
		int tot=0;
		for(int i=0;i<subContents[numFunc].length;i++){
			if(Constants.CommentLine.matcher(subContents[numFunc][i]).matches()){
				tot=tot+1;
			}
		}
		return tot;
	}
}