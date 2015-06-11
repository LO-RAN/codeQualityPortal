package com.compuware.caqs.CStyle;

import com.compuware.caqs.CStyle.Constants;
import com.compuware.caqs.FileManager.*;

public class CommentsCountExtractor {
	private String[] contents;
	private static int cloc;
	
	public CommentsCountExtractor(String input){
		FileManage f= new FileManage(input);
		this.contents=f.getContents();
		
	}
	
	public int count(int deb, int fin){
		boolean isCommented=false;
		cloc=0;
		
		for(int i=deb;i<fin;i++){
			if(Constants.commKw.matcher(this.contents[i]).matches() && !isCommented){
				
				cloc++;
				
			}else{
				if(this.contents[i].indexOf("/*")>-1 && !isCommented){
					
					cloc++;
					isCommented=true;
				}
			}
			if(this.contents[i].indexOf("*/")>-1 ){
				
				isCommented=false;
				
			}
		}
		return cloc;
	}
}
