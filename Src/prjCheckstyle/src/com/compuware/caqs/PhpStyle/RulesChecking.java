package com.compuware.caqs.PhpStyle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.compuware.caqs.FileManager.FileManage;
import com.compuware.caqs.FileManager.DirectoryManage;
import java.util.regex.Matcher;

public class RulesChecking {
	private String[] contents;
	private HashSet varTable;
	private HashSet funcTable;
	
	public RulesChecking(String dir){
		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		System.out.println("<phpRulesChecker version=\"1.00\">");
		if(!dir.endsWith(".php")){
			DirectoryManage directory=new  DirectoryManage(dir);
			String[] fileNames=directory.getFileNames(dir,"php");
			for(int i =0; i<fileNames.length;i++){
				System.err.println(fileNames[i].substring(dir.length()));
				checkingRules(fileNames[i]);
			}
		}else{
			checkingRules(dir);
		}
		
		
		System.out.println("</phpRulesChecker>");
	}
	
	private void checkingRules(String file){
		varTable=new HashSet();
		funcTable=new HashSet();
		FileManage sourceFile=new FileManage(file);
		
		this.contents = sourceFile.getContents();
		int debfile=0;
		int finfile=this.contents.length;
		boolean inFunction=false;
		boolean inLoop=false;
		HashSet parameterTable=new HashSet();
		HashSet fullParameterTable=new HashSet();
		String varLoop="";
		for(int line=debfile;line<finfile;line++){
			
			//detection d une classe
			if(Constants.detectCl.matcher(this.contents [line]).matches()){
				
				checkClassName(this.contents [line],line,file);
			}
			if(Constants.detectFu.matcher(this.contents [line]).matches()){
				
				inFunction=true;
				funcTable.add(checkFunctionName(this.contents [line],line,file));
			}
			if(Constants.detectVar.matcher(this.contents [line]).matches()){
				
				varTable.add(checkVarName(this.contents [line],line,file));
			}	
			if(Constants.detectVL.matcher(this.contents [line]).matches() && inFunction){
				
				checkVLName(this.contents [line],line,file);
			}
			if(Constants.detectCte.matcher(this.contents [line]).matches()){
				
				checkCteName(this.contents [line],line,file);
			}
			if(Constants.detectReturn.matcher(this.contents [line]).matches()){
				
				checkReturnName(this.contents [line],line,file);
			}
			if(Constants.detectMultipleAffectation.matcher(this.contents [line]).matches()){
				
				checkMultipleAffectation(this.contents [line],line,file);
			}
			
			//affectation de parametre formel
			if(Constants.detectFu.matcher(this.contents [line]).matches()){
				parameterTable=new HashSet();
				inFunction=true;
				inLoop=false;
				varLoop="";
				Matcher matcher=Pattern.compile("\\$\\w+").matcher(this.contents [line]);
				while (matcher.find())
				{
					int deb=matcher.start(0);
					int fin=matcher.end(0);
					String cl=this.contents [line].substring(deb,fin);
					parameterTable.add(cl);
					fullParameterTable.add(cl);
				}
			}else{
				String param="";
				Matcher matcher=Pattern.compile("^[^\\/|^\\*|^\\'|^\\\"|^#]*&?(\\$\\w+)\\s*=\\s*[^=|\\)].*$").matcher(this.contents [line]);
				if(matcher.find()){
					int deb=matcher.start(1);
					int fin=matcher.end(1);
					String v=this.contents [line].substring(deb,fin);	
					if(parameterTable.contains(v)){
						checkFormalParameterAffectation(param,line,file);
						System.out.println("<error name=\"DA_PHP03\" message = \"reaffectation de parametre formel "
								+param+"\" ligne = \""+new Integer(line+1)+"\" fichier = \""+file+"\"/>");
					}
				}
			}
			
			if(Constants.detectLoopParameter.matcher(this.contents [line]).matches()){
				
				Matcher matcher=Pattern.compile("^[\\s|\\t]*(for|while|foreach)\\s*\\((\\$[^\\s]+)\\s.*\\)\\s*\\{?$").matcher(this.contents [line]);
				int deb=0;
				int fin=0;
				if(matcher.find() ){
					deb=matcher.start(2);
					fin=matcher.end(2);
					varLoop=this.contents [line].substring(deb,fin);
				}else varLoop=null;
				
				inLoop = true;
			}else{
				
				String search="^[\\s|\\t]*"+varLoop+"\\s*=[^=].*$";
				if(varLoop!=null && inLoop && Pattern.compile(search).matcher(this.contents [line]).matches()){
					System.out.println("<error name=\"DA_PHP03\" message = \"reaffectation de parametre de boucle "
							+varLoop+" ligne "+new Integer(line+1)+"\", fichier = \""+file+"\"/>");
				}
			}
			
		}
		
		String fu="";
		for(Iterator iter = funcTable.iterator();iter.hasNext();fu = (String)iter.next()){
			
			if(fu.length()>0){
				if(varTable.contains(fu)){
					System.out.println("<error name=\"N_PHP13\" message = \""+
							fu+"\" ligne = \"null\" fichier = \""+file+"\"/>");
				}
			}
		}
		String param="";
		for(Iterator iter=fullParameterTable.iterator() ;iter.hasNext() ;param=(String)iter.next() ){
			
			if(varTable.contains(param)){
				System.out.println("<error name=\"S2\" message = \"deux variables de meme nom ("+
						param+")\" ligne = \"null\" fichier = \""+file+"\"/>");
			}
		}
		checkHardCode(sourceFile.replaceCommentsByBlanks(),file);
		
		
	}
	
	private void checkHardCode(String[] content, String file){
		for(int i=0;i<content.length;i++){
			if(content[i].indexOf("require")<0 
					&& content[i].indexOf("include")<0 
					&& (Pattern.compile("^.*\\\"[^\\\"]+.+$").matcher(content[i]).matches())
					|| Pattern.compile("^.*[=|\\[]\\s*([3-9]|[0-9]{2,})\\s*.*$").matcher(content[i]).matches()){
				System.out.println("<error name=\"code en dur\" message = \"null\" ligne = \""
						+new Integer(i+1)+"\" fichier = \""+file+"\"/>");
			}
		
		}
	}
	
	private void checkClassName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("^\\s*(class\\s+)(\\w+).*$").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(2);
			fin=matcher.end(2);
		}
		
		String clName=clLine.substring(deb,fin);
		if(!Pattern.compile("^[A-Z][a-zA-Z]*$").matcher(clName).matches()){
			System.out.println("<error name=\"N_PHP01\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		};
		
	}
	
	private String checkFunctionName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("\\s*(function\\s+)(\\w+).*").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(2);
			fin=matcher.end(2);
		}
		
		String clName=clLine.substring(deb,fin);
		if(!Pattern.compile("^[a-z][a-zA-Z]*$").matcher(clName).matches()){
			System.out.println("<error name=\"N_PHP02\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		};
		return clName;
	}
	
	private String checkVarName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("\\s*(var\\s+)\\$([\\w|_|0-9]+).*").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(2);
			fin=matcher.end(2);
		}
		String clName=clLine.substring(deb,fin);
		
		if(!Pattern.compile("^[a-z][a-zA-Z]*$").matcher(clName).matches()){
			System.out.println("<error name=\"N_PHP03\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		};
		return clName;
	}
	
	private void checkVLName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("\\s*\\$([\\w]+)\\s*=.*$").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(1);
			fin=matcher.end(1);
		}
		String clName=clLine.substring(deb,fin);
		if(!Pattern.compile("^[a-z]+$").matcher(clName).matches() && !varTable.contains(clName)){
			System.out.println("<error name=\"N_PHP04\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		}
	}
	
	private void checkCteName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("^\\s*define\\s*\\(\\'([\\w+|0-9|_]+)\\',.*$").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(1);
			fin=matcher.end(1);
		}
		
		String clName=clLine.substring(deb,fin);
		
		if(!Pattern.compile("^[A-Z|_]+$").matcher(clName).matches()){
			System.out.println("<error name=\"N_PHP06\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		}
	}
	
	private void checkReturnName(String clLine,int ligne, String fichier){
		Matcher matcher=Pattern.compile("return\\s*\\$?([\\w|0-9|_]+).*").matcher(clLine);
		int deb=0;
		int fin=0;
		if(matcher.find() ){
			deb=matcher.start(1);
			fin=matcher.end(1);
		}
		
		String clName=clLine.substring(deb,fin);
		
		if(varTable.contains(clName)){
			System.out.println("<error name=\"N_PHP07\" message = \""+
					clName+"\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
		}
	}
	
	private void checkMultipleAffectation(String clLine,int ligne, String fichier){
		System.out.println("<error name=\"DA_PHP01\" message = \"multiple affectation detected ("+ 
				clLine.replaceAll("[é|è|ë|ê|à|â|ï|î|ô|ö|ù|\\\"]","\\?")+")\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
	}
	
	private void checkFormalParameterAffectation(String clLine,int ligne, String fichier){
		System.out.println("<error name=\"DA_PHP03\" message = \"formal parameter affectation detected ("+ 
				clLine+")\" ligne = \""+new Integer(ligne+1)+"\" fichier = \""+fichier+"\"/>");
	}
}
