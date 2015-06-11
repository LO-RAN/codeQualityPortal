package com.compuware.caqs.CobolStyle;

import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 3 mai 2006
 * Time: 17:28:59
 * To change this template use File | Settings | File Templates.
 */
public class Metrics {
	
	String[] contents;
	String[] snavContents;
	//tableau des fonctions
	String [][]tableFu;
	//tableau des fichiers
	String [][]tableFi;
	
	String directory;
	//nombre de mi et de fu
	int nbF;
	
	public Metrics(String dir){
		directory=new String(dir);
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir,"cobol");
		System.err.println("analysing snav results ...");
		FileManage snav=new FileManage(this.directory+"\\fil_file.csv");
		this.snavContents=snav.getContents();
		nbF=0;
		this.tableFu=new String[snavContents.length][9];
		this.tableFi=new String[fileNames.length][5];
		
		System.err.println("searching functions ...");
		for(int i =0; i<snavContents.length;i++){
			findFunc(i);
		}
		System.err.println("calculate metrics for each function ...");
		System.out.println("Fichiers;Classe;méthodes/fonctions;LOC;VG;CLOC");
		for(int i =0; i<nbF;i++){
			collectCppMetrics(null,i);
		}
		/*System.err.println("calculate metrics for each file ...");
		System.out.println("");
		System.out.println(fileNames.length+"Fichiers;LOC;VG;CLOC");
		for(int i=0;i<fileNames.length;i++){
			collectCppMetrics(fileNames[i],i);
		}*/
		
	}
	private String getParameters(int line){
		int deb=this.snavContents [line].lastIndexOf("{")+1;
		int fin=this.snavContents [line].indexOf("}",deb);
		return this.snavContents [line].substring(deb,fin);
	}
	private void findFunc(int line){
		
		//nombre de mi et de fu
		//System.out.println(this.snavContents [line]);
		if(Constants.fu.matcher(this.snavContents [line]).matches() ||
				Constants.mi.matcher(this.snavContents [line]).matches() ||
				Constants.catchFu.matcher(this.snavContents [line]).matches()){
			//int df= this.snavContents [line].indexOf("}");
			
			int df1= this.snavContents [line].indexOf("{",2)+1 ;
			int df2= this.snavContents [line].indexOf(".",df1);
			
			int ef1= this.snavContents [line].indexOf(" ",df2);
			int ef2= this.snavContents [line].indexOf(".",ef1);
			//System.out.println(this.snavContents [line].substring(df1,df2)+";"+ef2+";"+line+";"+this.snavContents [line]);
			String i=this.snavContents [line].substring(df1,df2);
			String j=this.snavContents [line].substring(ef1+1,ef2);
			
			String fichier=this.snavContents [line].substring(
					this.snavContents [line].indexOf("{",1)+1 ,
					this.snavContents [line].indexOf(".cbl")+4
			);
			int dc=this.snavContents [line].indexOf(" ",df1)+1;
			int fc=this.snavContents [line].indexOf("#")+2;
			
			//String classe=this.snavContents [line].substring(dc,fc);
			String fonction=this.snavContents [line].substring(
					fc,
					this.snavContents [line].indexOf("fu")-1);
			
			if(Constants.catchFu.matcher(this.snavContents [line]).matches()){
				//ici, on rattache le catch externe en donnant sa ligne de fin à la fonction précédente
				tableFu[nbF-1][4]=j;
			}else{
				tableFu[nbF][0]=fichier;
				tableFu[nbF][1]="";//classe;
				tableFu[nbF][2]=fonction+"("+this.getParameters(line)+")" ;
				tableFu[nbF][3]=j;
				tableFu[nbF][4]=i;
				//System.out.println(tableFu[nbF][0]+"-"+tableFu[nbF][1]+"-"+tableFu[nbF][2]+"-"+tableFu[nbF][3]+"-"+tableFu[nbF][4]);
				nbF++;
			}
		}
		
	}
	
	private String getElement(int line, int elNum){
		return tableFu[line][elNum];
	}
	
	private void collectCppMetrics(String file,int line){
		
		//booleen specifiant que la zone est implicitement commentée (en bloc)
		
		//marqueur de début et de fin
		int df;
		int ff;
		
		//si file est null, alors on étudie par fonction, sinon, c'est le fichier en entier
		if(file==null){
			//System.err.println("Metrics : "+getElement(line,0));
			FileManage sourceFile=new FileManage(getElement(line,0));
			this.contents = sourceFile.getContents();
			df=new Integer(tableFu[line][3]).intValue();
			ff=new Integer(tableFu[line][4]).intValue();
		}else{
			
			FileManage sourceFile=new FileManage(file);
			this.contents = sourceFile.getContents();
			df=0;
			ff=this.contents.length;
			
		}
		
		//init métriques
		int vg=1;
		int loc=0;
		int cloc=0;
		int evg=1;
		int inIf=0;
		//parcours du tableau contenant les lignes de code en fonction des marqueurs spécifiés
		for (int i =df;i<ff;i++){
			
			if(Constants.noSourceComment.matcher(this.contents[i]).matches()
					|| Constants.commKw.matcher(this.contents[i]).matches()){
				
				cloc++;
				
			}else{
				
				if(Constants.ifKw.matcher(this.contents[i]).matches()
						|| Constants.performKw.matcher(this.contents[i]).matches()
						|| Constants.whenKw.matcher(this.contents[i]).matches()
						|| Constants.onExceptionKw.matcher(this.contents[i]).matches()
						|| Constants.onOverflowKw.matcher(this.contents[i]).matches()
						|| Constants.onSizeErrorKw.matcher(this.contents[i]).matches()
						|| Constants.onInvalidKeyKw.matcher(this.contents[i]).matches()
						|| Constants.atEndKw.matcher(this.contents[i]).matches()
						|| Constants.atEndOfPageKw.matcher(this.contents[i]).matches()
						|| Constants.or1Kw.matcher(this.contents[i]).matches()
						|| Constants.or2Kw.matcher(this.contents[i]).matches()
						|| Constants.and1Kw.matcher(this.contents[i]).matches()
						|| Constants.and2Kw.matcher(this.contents[i]).matches()
				){
					if(Constants.ifKw.matcher(this.contents[i]).matches() 
							||Constants.readKw.matcher(this.contents[i]).matches()){
						
						inIf++;
						//System.out.println("if trouve : "+inIf+" ligne "+i);
					}
					if(Constants.performKw.matcher(this.contents[i]).matches()&&inIf>0){
						if(inIf+1>evg){
							//System.out.println("perform trouve : "+this.contents[i]);
							evg=inIf+1;
						}
					}
					vg++;
				}
				if((Constants.endIfKw.matcher(this.contents[i]).matches()
						||Constants.dot.matcher(this.contents[i]).matches())
						&& inIf>0){            		
					inIf--;
					//System.out.println("if ferme : "+inIf+" ligne "+i);
				}
				
				if(!(Constants.noSourceComment.matcher(this.contents[i]).matches()
						|| Constants.blankLine.matcher(this.contents[i]).matches())
				){
					
					loc++;
				}
			}
			
			
		}
		if(file==null){
			tableFu[line][5]=new Integer(loc).toString();
			tableFu[line][6]=new Integer(vg).toString();
			tableFu[line][7]=new Integer(cloc).toString();
			tableFu[line][8]=new Integer(evg).toString();
			System.out.println(tableFu[line][0]+";"+tableFu[line][1]+";"+tableFu[line][2]+";"+tableFu[line][5]+";"+tableFu[line][6]+";"+tableFu[line][7]+";"+tableFu[line][8]);
		}else{
			//System.err.println("Metrics : "+file);
			tableFi[line][0]=file;
			tableFi[line][1]=new Integer(loc).toString();
			tableFi[line][2]=new Integer(vg).toString();
			tableFi[line][3]=new Integer(cloc).toString();
			tableFi[line][4]=new Integer(evg).toString();
			System.out.println(tableFi[line][0]+";"+tableFi[line][1]+";"+tableFi[line][2]+";"+tableFi[line][3]+";"+tableFi[line][4]);
		}
	}
}
