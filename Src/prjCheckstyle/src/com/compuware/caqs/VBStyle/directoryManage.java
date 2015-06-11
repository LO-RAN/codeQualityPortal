package com.compuware.caqs.VBStyle;

import java.io.*;
import java.util.HashSet;
import java.util.*;
/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 25 avr. 2006
 * Time: 14:52:52
 * To change this template use File | Settings | File Templates.
 */

public class directoryManage {
	File dir;
	private static Set liste;
	
	directoryManage(String dir){
		liste=new HashSet();
		try{
			this.dir = new File(dir);
		}catch(Exception e){
		}
	}
	
	private void explore(String rep){
		String[] sources=null;
		File repos= new File(rep);
		try{
			sources=repos.list();
			for(int i=0;i<sources.length;i++){
				if(new File(repos+"\\"+sources[i]).isDirectory()){
					explore(repos+"\\"+sources[i]);
				}else{
					liste.add(rep+"\\"+sources[i]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	} 
	
	public String[] getFileNames(String rep){
		String[] sources=null;
		explore(rep);
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
