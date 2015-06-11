package com.compuware.caqs.metricsCollector;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pmd.PMD;

import com.compuware.caqs.FileManager.FileManage;
import com.compuware.caqs.FileManager.DirectoryManage;

public class PHPMetrics {
	String[] contents;
	
	String[] contentsWithoutComments;
	//tableau des fonctions
	String [][]tableFu;
	//tableau des fichiers
	String [][]tableFi;
	public static String functionResults;
	public static String fileResults;
	String directory;
	//nombre de mi et de fu
	int nbF;
	private Hashtable functionMetrics;
	private Hashtable fileMetrics;
	
	public PHPMetrics(String dir){
		
		directory=new String(dir);
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir,"php");
		
		
		for(int i=0;i<fileNames.length;i++){
			FileManage file=new FileManage(fileNames[i]);
			this.contents=file.getContents();
			this.contentsWithoutComments=file.replaceCommentsByBlanks();
			//System.out.println("collect du fichier : "+fileNames[i]);
			collectCppMetrics(fileNames[i]);
		}
		
	}
	
	private void collectCppMetrics(String fichier){
		int vgFile=0;
		int locFile=0;
		int clocFile=0;
		//fichier - fonction - loc - vg - cloc
		String[] infos=new String[2];
		int loc=0;
		int vg=0;
		int cloc=0;
		
		for(int i=0;i<this.contentsWithoutComments.length;i++){
			if(PHPConstants.detectFu.matcher(this.contentsWithoutComments[i]).matches()){
				if(infos[1]!=""){
					functionResults+=fichier+";"+infos[1]+";"+loc+";"+vg+";"+cloc+PMD.EOL;
					System.out.println("<metric " +
							"fichier=\""+fichier+"\" " +
							"fonction=\""+infos[1]+"\" " +
							"loc=\""+loc+"\" " +
							"vg=\""+vg+"\" " +
							"cloc=\""+cloc+"\"/>");
				}
				Matcher matcher=Pattern.compile("\\s*(function\\s+)(\\w+).*").matcher(this.contentsWithoutComments[i]);
				int deb=0;
				int fin=0;
				if(matcher.find() ){
					deb=matcher.start(2);
					fin=matcher.end(2);
				}
				infos[0]=fichier;
				infos[1]=this.contentsWithoutComments[i].substring(deb,fin);
				loc=0;
				vg=0;
				cloc=0;
			}
			if(this.contentsWithoutComments[i]!=""){
				locFile++;
				if(infos[1]!=""){
					loc++;
				}
			}
			if(PHPConstants.ifKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.elseIfKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.questionKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.whileKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.doKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.forKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.foreachKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.doKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.caseKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.orKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.andKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.ORKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.XORKw.matcher(this.contentsWithoutComments[i]).matches()
					|| PHPConstants.ANDKw.matcher(this.contentsWithoutComments[i]).matches()
					
			){
				vgFile++;
				if(infos[1]!=""){
					vg++;
				}
			}
			if(!this.contentsWithoutComments[i].equals(this.contents[i])){
				clocFile++;
				if(infos[1]!=""){
					cloc++;
				}
			}
		}
		fileResults+=fichier+";"+loc+";"+vg+";"+cloc+PMD.EOL;
		System.out.println("<metric " +
				"fichier=\""+fichier+"\" " +
				"fonction=\"null\" " +
				"loc=\""+locFile+"\" " +
				"vg=\""+vgFile+"\" " +
				"cloc=\""+clocFile+"\"/>");
	}
	
}
