package com.compuware.caqs.CobolStyle;

import com.compuware.caqs.FileManager.FileManage;
import com.compuware.caqs.FileManager.DirectoryManage;

public class CallList {
	String[] contents;
	String[] snavContents;
	//tableau des fonctions
	String [][]tableFu;
	String directory;
	//nombre de mi et de fu
	int nbF;
	
	public CallList(String dir){
		directory=new String(dir);
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir,".cbl");
		FileManage snav=new FileManage(this.directory+"\\fil_file.csv");
		this.snavContents=snav.getContents();
		nbF=0;
		this.tableFu=new String[snavContents.length][3];
		for(int i =0; i<snavContents.length;i++){
			findFunc(i);
		}
		for(int i=0;i<fileNames.length;i++){
			if(fileNames[i]!=null)
			findCall(fileNames[i]);
		}
		for(int i =0;i<nbF;i++){
			if(tableFu[i][2]=="false"){
				System.out.println(tableFu[i][0]+";"+tableFu[i][1]);
			}
		}
	}
	
	private void findCall(String file){
		int df, ff;
		FileManage sourceFile=new FileManage(file);
		//System.out.println("----------------------------etude du fichier "+file);
		this.contents = sourceFile.getContents();
		df=0;
		ff=this.contents.length;
		String calledFunc=null;
		for (int i =df;i<ff;i++){
			if(Constants.callPERFORMKw.matcher( this.contents[i]).matches() || 
					Constants.callCALLKw.matcher( this.contents[i]).matches() ){
			if(Constants.callPERFORMKw.matcher( this.contents[i]).matches() ){
				
				int performAdress=this.contents[i].indexOf("PERFORM");
				int debFunc=this.contents[i].indexOf(" ",performAdress)+1;
				int finFunc=this.contents[i].indexOf(" ",performAdress+10);
				if(finFunc==-1){
					calledFunc=this.contents[i].substring(debFunc);
				}else{
					calledFunc=this.contents[i].substring(debFunc,finFunc);
				}
				
				while(calledFunc.startsWith(" ")){
					calledFunc=calledFunc.substring(1);
				}
				//System.out.println("la fonction '"+calledFunc+"' est appelée");
			}
			if(Constants.callCALLKw.matcher( this.contents[i]).matches() ){
				int performAdress=this.contents[i].indexOf("CALL");
				int debFunc=this.contents[i].indexOf(" ",performAdress)+1;
				int finFunc=this.contents[i].indexOf(" ",performAdress+7);
				
				if(finFunc==-1){
					calledFunc=this.contents[i].substring(debFunc);
				}else{
					calledFunc=this.contents[i].substring(debFunc,finFunc);
				}
				while(calledFunc.startsWith(" ")){
					calledFunc=calledFunc.substring(1);
				}
			//	System.out.println("la fonction '"+calledFunc+"' est appelée");
			}
			for(int j=0;j<nbF;j++){
				//System.out.println(tableFu[j][0].substring(tableFu[j][0].lastIndexOf("\\")+1));
				//System.out.println(file.substring(file.lastIndexOf("\\")+1));
				//System.out.println("'"+tableFu[j][1]+"' - '"+calledFunc+"' - '"+
					//	tableFu[j][0].substring(tableFu[j][0].lastIndexOf("\\")+1)+"' - '"+
					//	file.substring(file.lastIndexOf("\\")+1));
			
				if(tableFu[j][0].substring(tableFu[j][0].lastIndexOf("\\")+1)
						.equals(file.substring(file.lastIndexOf("\\")+1)) 
						&& tableFu[j][1].equals(calledFunc)){
					//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!comp ok. la fonction "+calledFunc+" a ete trouvee");
					tableFu[j][2]="true";
					break;
				}
			}
			}
		}
		
		
	}
	
	private void findFunc(int line){
		
		if(Constants.fu.matcher(this.snavContents [line]).matches() ||
				Constants.mi.matcher(this.snavContents [line]).matches() ||
				Constants.catchFu.matcher(this.snavContents [line]).matches()){
			
			String fichier=this.snavContents [line].substring(
					this.snavContents [line].indexOf(this.directory)+this.directory.length()+3 ,
					this.snavContents [line].indexOf(".cbl")+4
			);
			
			int fc=this.snavContents [line].indexOf("#")+2;
			
			String fonction=this.snavContents [line].substring(
					fc,
					this.snavContents [line].indexOf("fu")-1);
			
			tableFu[nbF][0]=this.directory +"\\"+fichier;
			tableFu[nbF][1]=fonction;
			tableFu[nbF][2]="false";
			nbF++;
			
		}
	}
	
}
