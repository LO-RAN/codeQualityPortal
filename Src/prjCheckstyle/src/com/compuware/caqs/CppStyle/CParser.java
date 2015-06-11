package com.compuware.caqs.CppStyle;


import java.io.File;
import com.compuware.caqs.FileManager.DirectoryManage;

public class CParser {
	String directory;
	String cmd;
	
	
	public CParser(String dir){
		
		try{
		directory=new String(dir);
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir, "cpp");
		
		findSNav();
		for(int i=0;i<fileNames.length;i++){
			
			
			try {
				Runtime r = Runtime.getRuntime();
				System.out.println(cmd+" "+fileNames[i]+" "+Main.outDir+"\\test.fil");
				Process p = r.exec(cmd+" "+fileNames[i]+" "+Main.outDir+"\\test.fil");
				p.waitFor();//si l'application doit attendre que ce process soit fini
				System.out.println("parsing -- "+fileNames[i]);
				
			}catch(Exception e) {
				System.out.println("erreur d'execution " + cmd + e.toString());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void findSNav(){
		File is;
		
		try{
			//extraction du répertoire ou ont ete lance les .class
			is=new File(System.getProperty("java.class.path") );
			//extraction du jar qui nous interesse
			cmd=is.getAbsolutePath().substring(0,is.getAbsolutePath().lastIndexOf("\\"))+"\\runCParser.bat";
			File x=new File(Main.outDir+"\\test.fil");
			
			if(x.exists() && !x.delete()){
				System.out.println("on ne peut pas supprimer le fichier "+Main.outDir+"\\test.fil");
			}
		}catch(Exception e){
			e.printStackTrace(); 
			cmd="";
		}
		
	}
	
}
