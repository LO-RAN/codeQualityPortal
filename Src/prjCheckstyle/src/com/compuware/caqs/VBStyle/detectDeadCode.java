package com.compuware.caqs.VBStyle;

import java.util.regex.Pattern;
import java.util.ArrayList;
import com.compuware.caqs.FileManager.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 26 avr. 2006
 * Time: 15:07:10
 * To change this template use File | Settings | File Templates.
 */
public class detectDeadCode {
	String[] contents;
	String[] listFunc;
	
	public detectDeadCode(String dir){
		listFunc=new String[100];
		
			DirectoryManage directory=new  DirectoryManage(dir);
			String[] fileNames=directory.getFileNames(dir, "vb");
			for(int i =0; i<fileNames.length;i++){
				FileManage file=new FileManage(fileNames[i]);
				this.contents=file.getContents();
				//System.out.println(fileNames[i]+" "+this.contents.length );
				listPrivateFunctions();
				inspectCalls(fileNames[i]);
			}
		
	}
	
	private void listPrivateFunctions(){
		ArrayList l = new ArrayList();
		Pattern beginFunc = Pattern.compile("^[\\s|\\t]*(Private)\\s+(Sub|Function).*$");
		int j=0;
		
		for(int i =0; i < this.contents.length;i++){
			if(beginFunc.matcher(contents[i]).matches()){
				l.add(contents[i].substring(contents[i].indexOf("Sub")+4, contents[i].indexOf("(")));
				//System.out.println("-- "+contents[i].substring(contents[i].indexOf(" ",10)+1, contents[i].indexOf("(")));
				j=j+1;
			}
		}
		String[] t = new String[j];
		//listFunc= new String[j];
		listFunc=(String[]) l.toArray(t);
	}
	
	private void inspectCalls(String file){
		
		int callFind;
		for(int f =0; f < this.listFunc.length;f++){
			callFind=0;
			if(listFunc[f].indexOf("_")<0 ){
				for(int i =0; i < this.contents.length;i++){
					if(contents[i].indexOf(" "+listFunc[f]) > -1){
						callFind=callFind+1;
						//System.out.println(listFunc[f]+";"+callFind+"call find "+contents[i]);
					}
				}
				if(callFind<2){
					System.out.println(file+" : "+listFunc[f]+" est a priori du code mort");
				}
			}
		}
	}
}
