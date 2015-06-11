package com.compuware.toolbox.io.insert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cwfr-fdubois
 *
 */
public class FileInsertEntry {

	/** L'expression régulière identifiant l'emplacement d'insertion. */
	String regexp;
	
	/** La liste de fichiers à insérer. */
	List fileToInsert = new ArrayList();
	
	/** Accès a la liste des fichiers à insérer.
	 * @return la liste des fichiers à insérer.
	 */
	public List getFileToInsert() {
		return fileToInsert;
	}
	
	/** Ajoute un fichier pour l'insérer.
	 * @param fileToInsert le fichier à insérer.
	 */
	public void addFileToInsert(File fileToInsert) {
		this.fileToInsert.add(fileToInsert);
	}
	
	/** Accès à l'expression régulière identifiant l'emplacement d'insertion.
	 * @return l'expression régulière identifiant l'emplacement d'insertion.
	 */
	public String getRegexp() {
		return regexp;
	}
	
	/** Accès à l'expression régulière identifiant l'emplacement d'insertion.
	 * @param regexp l'expression régulière identifiant l'emplacement d'insertion.
	 */
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	
	/** vérifie si une ligne données correspond à l'expression régulière
	 * identifiant l'emplacement d'insertion.
	 * @param line la ligne à vérifier.
	 * @return true si la ligne données correspond à l'expression régulière
	 * identifiant l'emplacement d'insertion, false sinon.
	 */
	public boolean regexpMatch(String line) {
		return line.matches(regexp);
	}
	
	/** Insère le contenu du fichier dans le fichier passé en paramètre.
	 * @param out le fichier de sortie.
	 * @throws IOException
	 */
	public void write(FileWriter out) throws IOException {
		if (fileToInsert != null && fileToInsert.size() > 0) {
			File currentFile = null;
			Iterator fileIter = fileToInsert.iterator();
			while (fileIter.hasNext()) {
				currentFile = (File)fileIter.next();
				if (currentFile.exists()) {
					FileReader reader = new FileReader(currentFile);
					write(out, reader);
					reader.close();
				}
			}
		}
	}
	
	/** Insère le contenu du fichier dans le fichier passé en paramètre.
	 * @param out le fichier de sortie.
	 * @param reader le lecteur pour le fichier à insérer.
	 * @throws IOException
	 */
	protected void write(FileWriter out, FileReader reader) throws IOException {
		BufferedReader bufreader = new BufferedReader(reader);
		String line = bufreader.readLine();
		while (line != null) {
			out.write(line);
			line = bufreader.readLine();
		}
	}
	
}
