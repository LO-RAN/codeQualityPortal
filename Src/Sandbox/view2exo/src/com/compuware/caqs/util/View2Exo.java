/**
 * 
 */
package com.compuware.caqs.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author cwfr-lizac
 * 
 */
public class View2Exo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		View2Exo v2e = new View2Exo();
		v2e.process(args[0], args[1], args[2]);

	}

	public void process(String inputUsersFile, String inputRolesFile,
			String outputFile) {
		try {
			String[] nextLine;

			ArrayList<String[]> usersList = new ArrayList<String[]>();
			System.out.println("Reading input users file...");

			CSVReader usersReader = new CSVReader(
					new FileReader(inputUsersFile), ';');
			while ((nextLine = usersReader.readNext()) != null) {
				usersList.add(nextLine);
			}
			usersReader.close();

			ArrayList<String[]> rolesList = new ArrayList<String[]>();
			System.out.println("Reading input roles file...");

			CSVReader rolesReader = new CSVReader(
					new FileReader(inputRolesFile), ';');
			while ((nextLine = rolesReader.readNext()) != null) {
				rolesList.add(nextLine);
			}
			rolesReader.close();

			System.out.println("Sorting roles on user id...");

			// sort the results according to user id (first column)
			Collections.sort(rolesList, new MyComp());

			System.out.println("Writing output file...");

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					outputFile));

			for (int i = 0; i < usersList.size(); i++) {

				writer.write("<value>");
				writer.newLine();

				writer.write(" <object type=\"org.exoplatform.services.organization.OrganizationConfig$User\">");
				writer.newLine();
				writer.write("  <field  name=\"userName\"><string>"+usersList.get(i)[0]+"</string></field>");
				writer.newLine();
				writer.write("  <field  name=\"password\"><string>"+usersList.get(i)[0]+"</string></field>");
				writer.newLine();
				writer.write("  <field  name=\"firstName\"><string>"+usersList.get(i)[1]+"</string></field>");
				writer.newLine();
				writer.write("  <field  name=\"lastName\"><string>"+usersList.get(i)[2]+"</string></field>");
				writer.newLine();
				writer.write("  <field  name=\"email\"><string>"+usersList.get(i)[3]+"</string></field>");
				writer.newLine();
				writer.write("  <field  name=\"groups\">");
				writer.newLine();
				writer.write("    <string>");
				writer.newLine();
				
				String roles="";
				for (int j = 0; j < rolesList.size(); j++) {
					if(rolesList.get(j)[0].equals(usersList.get(i)[0])){

					roles+=", member:"+rolesList.get(j)[1];
					}
				}
				
				writer.write("      member:/Caqs/ROLE_USER"+roles);
				writer.newLine();
				writer.write("    </string>");
				writer.newLine();
				writer.write("  </field>");
				writer.newLine();
				writer.write(" </object>");
				writer.newLine();
				writer.write("</value>");
				writer.newLine();
			}
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage : " + View2Exo.class.getName()
				+ " <inputUsersFile> <inputRolesFile> <outputFile>");
	}

	// implements a comparator on the first column (user id)
	private class MyComp implements Comparator<String[]> {

		public int compare(String[] s1, String[] s2) {

			return s1[0].compareTo(s2[0]);
		}

	}
}
