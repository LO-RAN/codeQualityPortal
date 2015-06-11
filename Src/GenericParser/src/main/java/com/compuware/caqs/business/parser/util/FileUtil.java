/**
 * 
 */
package com.compuware.caqs.business.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author cwfr-fdubois
 *
 */
public class FileUtil {

	private static final int BSIZE = 1024;

	public static String extractContent(File currentJsp) throws IOException {
		String content = FileUtil.extractContent(currentJsp, "utf-8");
		if (content == null  || content.length() == 0) {
			content = FileUtil.extractContent(currentJsp, "iso8859-2");
		}
		return content;
	}
	
	public static String extractContent(File currentJsp, String format) throws IOException {
		StringBuilder fileContent = new StringBuilder(BSIZE);
		Scanner scanner = null;
	    try {
	    	scanner = new Scanner(currentJsp, format);
	    	while(scanner.hasNextLine()){
	    		fileContent.append(format(scanner.nextLine())).append('\n');
	    	}
	    }
	    finally {
	      //ensure the underlying stream is always closed
	    	if (scanner != null) {
	    		scanner.close();
	    	}
	    }
	    return fileContent.toString();
	}
	
	private static String format(String line) {
		String result = line;
		if (result.length() > 72) {
			result = result.substring(0, 73);
		}
		return result;
	}
	
}
