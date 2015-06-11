package com.compuware.toolbox.io.insert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe représentant un fichier XML à insérer dans un autre principal.
 * @author cwfr-fdubois
 */
public class XmlFileInsertEntry extends FileInsertEntry {

	/**
	 * Un ensemble d'expression régulières permettant de détecter les zones de texte
	 * à exclure lors de l'insertion.
	 */
	String[] excludeRegexp;

	/**
	 * @return les expressions régulières d'exclusion.
	 */
	public String[] getExcludeRegexp() {
		return (String[])excludeRegexp.clone();
	}

	/**
	 * @param excludeRegexp les expressions régulières d'exclusion.
	 */
	public void setExcludeRegexp(String[] excludeRegexp) {
		this.excludeRegexp = new String[excludeRegexp.length];
		for (int i = 0; i < excludeRegexp.length; i++) {
			this.excludeRegexp[i] = excludeRegexp[i];
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.toolbox.io.insert.FileInsertEntry#write(java.io.FileWriter, java.io.FileReader)
	 */
	protected void write(FileWriter out, FileReader reader) throws IOException {
		BufferedReader bufreader = new BufferedReader(reader);
		String line = bufreader.readLine();
		while (line != null) {
			line = removeExcludedContent(line);
			out.write(line);
			line = bufreader.readLine();
		}
	}
	
	/**
	 * Méthode de suppression du texte à exclure dans une ligne.
	 * @param line la ligne analysée.
	 * @return la ligne purgée de son contenu à exclure.
	 */
	private String removeExcludedContent(String line) {
		String result = line;
		for (int i = 0; i < excludeRegexp.length; i++) {
			result = result.replaceAll(excludeRegexp[i], "");
		}
		return result;
	}
	
}
