package com.compuware.caqs.PhpStyle;

import com.compuware.caqs.FileManager.FileManage;
import com.compuware.caqs.FileManager.DirectoryManage;
import java.util.regex.Pattern;

public class FormalParameterReaffected {
	String[] contents;
	String[] snavContents;
	//tableau des fonctions
	String [][]tableFu;
	//tableau des fichiers
	String [][]tableFi;
	
	String directory;
	//nombre de mi et de fu
	int nbF;
	
	public FormalParameterReaffected(String dir) {
		directory=new String(dir);
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir,"php");
		nbF=0;
		
		this.tableFu=new String[10000][4];
		
		for(int i =0; i<fileNames.length;i++){
			findFunc(fileNames[i]);
		}
		//System.out.println("FormalParameter reaffected:");
		//for(int i =0; i<fileNames.length;i++){
			//searchFormalParameter(fileNames[i]);
		//}
		
	}
	
	public String getParameters(String func){
		int deb=func.indexOf("(")+1;
		int fin=func.indexOf(")",deb);
		return func.substring(deb,fin);
	}
	private void findFunc(String file){
		
		FileManage sourceFile=new FileManage(file);
		System.out.println("fichier : "+file);
        this.contents = sourceFile.getContents();
        int debfile=0;
        int finfile=this.contents.length;
        nbF=0;
        for(int line=debfile;line<finfile;line++){
        	//System.out.println("txt : "+this.contents [line]);
        	String[] param=new String[100];
		if(Pattern.compile("^\\s*function\\s+[^\\s]+\\s*\\(.*\\).*$").matcher(this.contents [line]).matches()){
			System.out.println("txt : "+this.contents [line]);
			String functionName=this.contents [line];
			tableFu[nbF][0]=functionName;
			tableFu[nbF][1]=file;
			tableFu[nbF][2]=Integer.toString(line);
			nbF=nbF+1;
			
			int j=0;
			String parameters=getParameters(functionName);System.out.println("param : "+parameters);
			while(parameters.indexOf("$")>-1){
				if(parameters.indexOf(",")>0){
					param[j]=parameters.substring(parameters.indexOf("$")+1,parameters.indexOf(","));
				parameters=parameters.substring(parameters.indexOf("$",parameters.indexOf(",")));
				if(param[j].indexOf("=")>0) param[j]=param[j].substring(0,param[j].indexOf("="));
				System.out.println("--"+param[j]);
				j=j+1;
				}else{
					
					parameters=parameters.substring(parameters.indexOf("$")+1);
					
					param[j]=parameters;
					System.out.println("--"+param[j]);
					j=j+1;
				}
			}
		}else{
			for(int p=0;p<param.length;p++){
			if(Pattern.compile("^\\s*"+param[p]+"\\s*=.*$").matcher(this.contents [line]).matches()){
				System.out.println("attention, ce paramètre est peut-être re-affecte : "+param[p]);
			}
			}
		}
        }
		
	}
	
	
}
