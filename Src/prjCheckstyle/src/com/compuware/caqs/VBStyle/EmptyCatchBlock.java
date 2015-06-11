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
public class EmptyCatchBlock {
	String[] contents;
	String[] listFunc;
	
	public EmptyCatchBlock(String dir){
		listFunc=new String[100];
		
		DirectoryManage directory=new  DirectoryManage(dir);
		String[] fileNames=directory.getFileNames(dir, ".vb");
		for(int i =0; i<fileNames.length;i++){
			FileManage file=new FileManage(fileNames[i]);
			this.contents=file.getContents();
			//System.out.println(fileNames[i]);
			detectEmptyCatchBlocks(fileNames[i]);
			
		}
		
	}
	
	private void detectEmptyCatchBlocks(String dir){
		Pattern catchBlock = Pattern.compile("^[\\s|\\t]*Catch[\\s]+[a-zA-Z]+[\\s]+As[\\s]+[a-zA-Z]+[\\s]*$");
		Pattern catchEndBlock = Pattern.compile("^[\\s|\\t]*End[\\s]+Try[\\s]*$");
		Pattern comment = Pattern.compile("^[\\s|\\t]*\\'.*$");
		Pattern emptyLine = Pattern.compile("^[\\s|\\t]*$");
		for(int i=0;i<this.contents.length;i++){
			if(catchBlock.matcher(this.contents[i]).matches()){
				int debLine=i+1;
				boolean empty=true;
				i=i+1;
				while(!catchEndBlock.matcher(this.contents[i]).matches() && i<this.contents.length){
					if(!comment.matcher(this.contents[i]).matches() && !emptyLine.matcher(this.contents[i]).matches() ){
						empty=false;
					}
					i=i+1;
				}
				if(empty){
					System.out.println("EmptyCatch : "+dir+" line "+debLine);
				}
			}
			
		}
	}
}
