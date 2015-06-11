package com.compuware.caqs.metricsCollector;

import java.util.regex.Pattern;
import net.sourceforge.pmd.PMD;
import java.io.*;
import com.compuware.caqs.FileManager.*;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:57:31
 * To change this template use File | Settings | File Templates.
 */
public class VBClassMetrics {
	String[] contents;
	String className;
	public static String results;
	
	public VBClassMetrics(String dir){
		int loc, vg, cloc;
		results="";
		if(new File(dir).isDirectory()){
			DirectoryManage directory=new  DirectoryManage(dir);
			
			String[] fileNames=directory.getFileNames(dir, "vb");
			
			for(int i =0; i<fileNames.length;i++){
				//System.out.println("fichier : "+fileNames[i]);
				FileManage file=new FileManage(fileNames[i]);
				//System.out.println(fileNames[i]);
				this.contents=file.getContents();
				loc=CountNumberOfLOC();
				vg=CountNumberOfVG();
				cloc=CountNumberOfCLOC();
				results+=fileNames[i]+";"+loc+";"+vg+";"+cloc+PMD.EOL;
				System.out.println(fileNames[i]+";"+loc+";"+vg+";"+cloc);
			}
		}else{
			FileManage file=new FileManage(dir);
			this.contents=file.getContents();
			loc=CountNumberOfLOC();
			vg=CountNumberOfVG();
			cloc=CountNumberOfCLOC();
			results+=dir+";"+loc+";"+vg+";"+cloc+PMD.EOL;
			System.out.println(dir+";"+loc+";"+vg+";"+cloc);
		}
	}
	
	private int CountNumberOfLOC(){
		Pattern beginClass = Pattern.compile("^[\\s|\\t]*((Public|Private)[\\s|\\t]+[a-zA-Z]*)?[\\s|\\t]*(Class|Interface|Module|Property).*$");
		Pattern lineOfCode = Pattern.compile("^[\\s|\\t]*[a-z|A-Z]+.*$");
		Pattern endClass = Pattern.compile("^[\\s|\\t]*End[\\s|\\t]*(Class|Interface|Module|Property).*$");
		int tot=0;
		int deb=0;
		//System.out.println(this.contents.length);
		/*while (!beginClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length ){
			deb=deb+1;
		}*/
		//System.out.println("sortie boucle "+deb);
		if(deb<this.contents.length){
		if(contents[deb].indexOf("Class")>-1){
			this.className=contents[deb].substring(contents[deb].indexOf("Class")+7);
		}else{
			if(contents[deb].indexOf("Interface")>-1){
				this.className=contents[deb].substring(contents[deb].indexOf("Interface")+10);
			}else{
				if(contents[deb].indexOf("Property")>-1){
				this.className=contents[deb].substring(contents[deb].indexOf("Property")+10);
			}
				else{
					if(contents[deb].indexOf("Module")>-1){
					this.className=contents[deb].substring(contents[deb].indexOf("Module")+10);
					}
				}
			}
		}
		while (deb<contents.length){//!endClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length ){
			if(lineOfCode.matcher(contents[deb]).matches()){
				tot=tot+1;
				// System.out.println("loc "+tot+" : "+contents[i]);
			}
			deb=deb+1;
		}
		}
		return tot;
	}
	
	private int CountNumberOfVG(){
		Pattern beginClass = Pattern.compile("^[\\s|\\t]*((Public|Private)[\\s|\\t]+)?[a-zA-Z]*[\\s|\\t]*(Class|Interface|Module|Property).*$");
		Pattern endClass = Pattern.compile("^[\\s|\\t]*End[\\s|\\t]*(Class|Interface|Module|Property).*$");

		Pattern ifKey = Pattern.compile("^[^'|^#]*\\sIf[\\s|\\(].*$");
		Pattern loopKey = Pattern.compile("^[^'|^#]*\\sLoop\\s.*$");
		Pattern doKey = Pattern.compile("^[^'|^#]*\\sDo\\s.*$");
		Pattern forKey = Pattern.compile("^[^'|^#]*\\sFor\\s.*$");
		Pattern caseKey = Pattern.compile("^[^'|^#]*\\sCase\\s.*$");
		Pattern andKey = Pattern.compile("^[^'|^#]*\\sAnd\\s.*$");
		Pattern orKey = Pattern.compile("^[^'|^#]*\\sOr\\s.*$");
		
		int tot=1;
		int deb=0;
		/*while (!beginClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length){
			deb=deb+1;
		}*/
		while (deb<contents.length){//!endClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length){
			if(ifKey.matcher(contents[deb]).matches() ||
					loopKey.matcher(contents[deb]).matches() ||
					doKey.matcher(contents[deb]).matches() ||
					forKey.matcher(contents[deb]).matches() ||
					caseKey.matcher(contents[deb]).matches() ||
					andKey.matcher(contents[deb]).matches() ||
					orKey.matcher(contents[deb]).matches()){
				tot=tot+1;
				//System.out.println("vg "+tot+" : "+contents[deb]);
			}
			deb=deb+1;
		}
		return tot;
	}
	
	private int CountNumberOfCLOC(){
		Pattern beginClass = Pattern.compile("^[\\s|\\t]*((Public|Private)[\\s|\\t]+)?[a-zA-Z]*[\\s|\\t]*(Class|Interface|Module|Property).*$");
		Pattern endClass = Pattern.compile("^[\\s|\\t]*End[\\s|\\t]*(Class|Interface|Module|Property).*$");

		Pattern clocKey = Pattern.compile("^.*'.*$");
		
		int tot=0;
		int deb=0;
		/*while (!beginClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length){
			deb=deb+1;
		}*/
		while (deb<contents.length){//<!endClass.matcher(contents[deb]).matches() && (deb+1)<this.contents.length){
			if(clocKey.matcher(contents[deb]).matches()){
				tot=tot+1;
				//System.out.println("cloc "+tot+" : "+contents[i]);
			}
			deb=deb+1;
		}
		return tot;
	}
	
}