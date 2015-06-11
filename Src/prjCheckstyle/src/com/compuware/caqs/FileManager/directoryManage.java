package com.compuware.caqs.FileManager;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 24 mars 2006
 * Time: 15:52:46
 * Gestion du répertoire et de ses fichiers
 */

public class DirectoryManage {
	File dir;
	private static Set liste;
	//private static int i=0;
	
	public DirectoryManage(String dir){
		liste=new HashSet();
		try{
			this.dir = new File(dir);
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
	private void explore(String rep, String lang){
		String[] sources=null;
		
		File repos= new File(rep);
		
		try{
			sources=repos.list();
			
			for(int i=0;i<sources.length;i++){
				if(new File(repos+"\\"+sources[i]).isDirectory()){
					explore(repos+"\\"+sources[i], lang);
				}else{
					if(lang=="cpp"){
						if(sources[i].endsWith(".h") || sources[i].endsWith(".cpp") || 
								sources[i].endsWith(".hpp") || sources[i].endsWith(".c") ||
								sources[i].endsWith(".pc")){
							liste.add(rep+"\\"+sources[i]);
						}
					}
					if(lang=="vb"){
						if(sources[i].endsWith(".vb")
								||sources[i].endsWith(".bas")
								||sources[i].endsWith(".BAS")
								||sources[i].endsWith(".frm")
								||sources[i].endsWith(".FRM")
								||sources[i].endsWith(".cls")
								||sources[i].endsWith(".CLS")
								||sources[i].endsWith(".ctl")
								||sources[i].endsWith(".CTL")){
							liste.add(rep+"\\"+sources[i]);
						}
					}
					if(lang=="php"){
						if(sources[i].endsWith(".php") ){
							liste.add(rep+"\\"+sources[i]);
						}
					}
					if(lang=="java"){
						if(sources[i].endsWith(".java")){
							liste.add(rep+"\\"+sources[i]);
						}
					}
					if(lang=="abap"){						
							liste.add(rep+"\\"+sources[i]);						
					}
					if(lang=="cobol"){						
						if(sources[i].endsWith(".cbl") ){
							
							liste.add(rep+"\\"+sources[i]);
							
							//System.err.println("add source :"+sources[i]);
						}						
				}
				}
			}
			
		}catch(Exception e){
			System.err.println("sortie d'erreur de DirectoryManage");
			e.printStackTrace();
		}
		
	} 
	
	public String[] getFileNames(String rep, String language){
		String[] sources=null;
		explore(rep, language);
		sources=new String[liste.size()];
		int i = 0;
		final Iterator it = liste.iterator();
		while (it.hasNext()) {
			final String name = (String) it.next();
			sources[i] = name;			
			i++;
		}
		return sources;
	}	
}

