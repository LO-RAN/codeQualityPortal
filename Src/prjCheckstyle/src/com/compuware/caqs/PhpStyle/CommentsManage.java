package com.compuware.caqs.PhpStyle;

import com.compuware.caqs.FileManager.FileManage;
import com.compuware.caqs.FileManager.DirectoryManage;

public class CommentsManage {
	
	public CommentsManage(String dir){
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir,"php");
		
		for(int i=0;i<fileNames.length;i++){
			//System.out.println("collect du fichier : "+fileNames[i]);
			replaceCommentsByBlanks(fileNames[i]);
			
		}
	}
	
	public String[] replaceCommentsByBlanks(String file){
		FileManage f=new FileManage(file);
		String[] s=f.getContents();
		String totalFile="";
		for(int j=0;j<s.length;j++){
			totalFile=totalFile+s[j]+"\r\n";
		}
		System.out.println("----------------------------------------------");
		//System.out.println(totalFile);
		totalFile=totalFile.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)|(?:#.*)","");
		//System.out.println(totalFile);
		String[] test=totalFile.split("\\r\\n");
		System.out.println(test[0]);
		System.out.println(test[1]);
		System.out.println(test[2]);
		System.out.println(test[3]);
		System.out.println(test[13]);
		return test;
	}
}
