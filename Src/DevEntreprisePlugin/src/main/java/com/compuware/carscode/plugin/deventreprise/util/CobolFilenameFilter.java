package com.compuware.carscode.plugin.deventreprise.util;

import java.io.File;
import java.io.FilenameFilter;

public class CobolFilenameFilter implements FilenameFilter {
	private static final String extension = ".cbl";
	
	public boolean accept(File dir, String name) {
		boolean retour = false;
		
		File f = new File(dir, name);
		if(f!=null && f.isFile()) {
			if(name.toLowerCase().endsWith(extension)) {
				retour = true;
			}
		}
		
		return retour;
	}

}
