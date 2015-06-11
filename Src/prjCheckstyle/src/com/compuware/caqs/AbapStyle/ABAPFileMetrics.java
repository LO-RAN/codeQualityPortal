package com.compuware.caqs.AbapStyle;

import java.io.File;
import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:57:31
 * To change this template use File | Settings | File Templates.
 */
public class ABAPFileMetrics {
	String[] contents;
	String className;
	
	public ABAPFileMetrics(String dir){
		int loc, vg, cloc;
		if(new File(dir).isDirectory()){
			DirectoryManage directory=new  DirectoryManage(dir);
			
			String[] fileNames=directory.getFileNames(dir, "abap");
			
			for(int i =0; i<fileNames.length;i++){
				//System.out.println("fichier : "+fileNames[i]);
				FileManage file=new FileManage(fileNames[i]);
				//System.out.println(fileNames[i]);
				this.contents=file.getContents();
				loc=CountNumberOfLOC();
				vg=CountNumberOfVG();
				cloc=CountNumberOfCLOC();
				System.out.println(fileNames[i]+";"+loc+";"+vg+";"+cloc);
			}
		}else{
			FileManage file=new FileManage(dir);
			this.contents=file.getContents();
			loc=CountNumberOfLOC();
			vg=CountNumberOfVG();
			cloc=CountNumberOfCLOC();
			System.out.println(dir+";"+loc+";"+vg+";"+cloc);
		}
	}
	
	private int CountNumberOfLOC(){
		int tot=0;	
		for(int i=0;i<this.contents.length;i++){
			if(Constants.sourceCodeLine.matcher(contents[i]).matches()){
				tot=tot+1;
			}			
		}
		return tot;
	}
	
	private int CountNumberOfVG(){
		int tot=1;
		for(int i=0;i<this.contents.length;i++){
			if(Constants.kw.matcher(contents[i]).matches()){
				tot=tot+1;
			}			
		}
		return tot;
	}
	
	private int CountNumberOfCLOC(){
		int tot=1;
		for(int i=0;i<this.contents.length;i++){
			if(Constants.CommentLine.matcher(contents[i]).matches()){
				tot=tot+1;
			}
			
		}
		return tot;
	}
}