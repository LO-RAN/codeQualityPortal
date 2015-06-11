package com.compuware.caqs.FileManager;

import java.io.*;

import java.util.ArrayList;
import java.util.regex.*;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 15:20:33
 * Gestion des fichiers et de leur contenu.
 */
public class FileManage {
	private String fileName;
	private BufferedReader buffer;
	private String[] conts;
	
	public FileManage(String name){
		this.fileName = name;
		this.openFile();
		//System.out.println(name);
		this.conts=this.setContents();
		this.closeFile();
	}
	
	private void closeFile(){
		try{
			buffer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void openFile(){
		
		try{
			
			if(new File(this.fileName).isFile() && new File(this.fileName).canRead() ){
				this.buffer = new BufferedReader(new FileReader(new File(this.fileName)));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String[] setContents(){
		ArrayList l = new ArrayList();
		String s=null;
		
		try{
			
			while((s=this.buffer.readLine()) != null){
				l.add(s);
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		String[] t = new String[0];
		return (String[]) l.toArray(t);
	}
	
	public String[] replaceCommentsByBlanks(){
		FileManage f=new FileManage(this.fileName);
		Matcher matcher;
		String[] s=f.getContents();
		String totalFile="";
		boolean trouve=true;
		
		for(int j=0;j<s.length;j++){
			
			s[j]=s[j].replaceAll("(#|//)[^\\*]*","");
			s[j]=s[j].replaceAll("/\\*.*\\*/","");
			
			totalFile=totalFile+s[j]+"\r\n";
		}
		
		matcher=Pattern.compile("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)|(?:#.*)").matcher(totalFile);
		int deb=0;
		int inter=0;
		int fin=0;
		String res="";
		deb=0;
		while (matcher.find()){
			
			inter=matcher.start();
			fin=matcher.end();
			
			res=res+totalFile.substring(deb,inter);
			res=res+totalFile.substring(inter,fin).replaceAll(".*\\\r\\\n.*","\\\r\\\n");
			
			deb=fin+1;
		}
		
		res=res+totalFile.substring(fin,totalFile.length());
		/*
		while(trouve==true){
	//		matcher=Pattern.compile("").matcher(totalFile);
			
			if(matcher.find()){
				int deb=matcher.start();
				int fin=matcher.end();
				String str="";
				str=totalFile.substring(deb,fin).replaceAll(".*\\\r\\\n.*","\\\r\\\n");
			//	totalFile=Pattern.compile("").matcher(totalFile).replaceFirst(str);
			//}
			//else{
				//trouve = false;
			//}*/
		//}
		
		return res.split("\\\r\\\n");
	}
	
	public String[] getContents(){
		return this.conts;
	}
	
	public int getFileLength(){
		return this.conts.length;
	}
	
}
