/**
 * 
 */
package com.compuware.caqs.util.i18n;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

/**
 * @author cwfr-fdubois
 *
 */
public class CodePageTransformer {
	
	private String charsetName = null;
	
	/**
	 * Constructor.
	 * @param charsetName the target charset.
	 */
	public CodePageTransformer(String charsetName) {
		this.charsetName = charsetName;
	}

	/**
	 * Transform the content of the src directory in UTF-8 and copy it to the dest directory.
	 * @param src the source directory.
	 * @param dest the destination directory.
	 * @throws IOException
	 */
	public void transform(File src, File dest) throws IOException {
		if (src != null && dest != null && src.exists()) {
			if (!dest.exists()) {
				dest.mkdirs();
			}
			transform(src, dest, src, false);
		}
	}
	
	/**
	 * Transform the source file or directory in UTF-8 and copy it to the destination directory.
	 * @param src the source file or directory.
	 * @param dest the destination directory.
	 * @param baseDir
	 * @param createSrcSubDir true if the src directory has to be created in the destination directory, else false.
	 * @throws IOException
	 */
	private void transform(File src, File dest, File baseDir, boolean createSrcSubDir) throws IOException {
		if (src != null && dest != null && baseDir != null) {
			if (src.isDirectory()) {
				File newDir = dest;
				if (createSrcSubDir) {
					newDir = new File(dest, src.getName());
					if (!newDir.exists()) {
						newDir.mkdirs();
					}
				}
				File[] contentFiles = src.listFiles();
				for (int i = 0; i < contentFiles.length; i++) {
					transform(contentFiles[i], newDir, baseDir, true);
				}
			}
			else {
				transformAndCopy(src, new File(dest, src.getName()));
			}
		}
	}
	
	/**
	 * Transform the source file in UTF-8 and copy it to the destination file.
	 * @param src the source file.
	 * @param dest the destination file.
	 * @throws IOException
	 */
	private void transformAndCopy(File src, File dest) throws IOException {
	    CharsetDetector detector;
	    CharsetMatch match;

	    InputStream streamData = new BufferedInputStream(new FileInputStream(src));
	    UnicodeReader unicodeReader;

	    detector = new CharsetDetector();

	    detector.setText(streamData);
	    match = detector.detect();
	    try{

	    unicodeReader = new UnicodeReader(streamData,match.getName());
	    } catch (NullPointerException e){
	    	System.out.println("Error: could not transform file: "+src+" to "+this.charsetName+" !");
	    	return;
	    }
	    
	    OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(dest), this.charsetName);
	    
	    char[] contentBuffer = new char[8192];
        int bytesRead = 0;
        while( (bytesRead = unicodeReader.read(contentBuffer, 0, 8192)) > 0 ) {
            // encode the output with the encoding guessed by the SmartEncodingInputStream
        	fw.append(new String(contentBuffer, 0, bytesRead));
        }
        unicodeReader.close();
        fw.close();
    }
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println("Usage: ");
			System.out.println("   java -cp cptransform-1.0.jar;icu4j-4.0.jar com.compuware.caqs.util.i18n.CodePageTransformer srcDir destDir charsetName");
		}
		else {
		    CodePageTransformer cpt = new CodePageTransformer(args[2]);
		    cpt.transform(new File(args[0]), new File(args[1]));
		}
    }

}
