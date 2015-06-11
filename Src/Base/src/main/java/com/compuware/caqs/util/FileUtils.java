package com.compuware.caqs.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static boolean deleteDirectory(String path) {
		boolean retour = false;
		
		File directory = new File(path);
		if(directory!=null && directory.exists() && directory.isDirectory()) {
			File[] listing = directory.listFiles();
			if(listing!=null) {
				for(int i=0; i<listing.length; i++) {
					if(listing[i].isDirectory()) {
						deleteDirectory(listing[i].getAbsolutePath());
					} else {
						listing[i].delete();
					}
				}
				retour = directory.delete();
			}
		}
		return retour;
	}	
	
	private static final Object lock = new Object();
	
	public static void append(File f, String content) {
		if (f != null && content != null && content.length() > 0) {
			synchronized (lock) {
				try {
					if (!f.getParentFile().exists()) {
						f.getParentFile().mkdirs();
					}
					FileWriter w = new FileWriter(f, true);
					BufferedWriter bw = new BufferedWriter(w);
					bw.write(content);
					bw.flush();
					bw.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void append(String dir, String f, String content) {
		if (dir != null && f != null && content != null && content.length() > 0) {
			append(new File(dir, f), content);
		}
	}
	
}
