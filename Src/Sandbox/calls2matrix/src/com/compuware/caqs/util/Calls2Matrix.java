/**
 * 
 */
package com.compuware.caqs.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author cwfr-lizac
 * 
 */

public class Calls2Matrix {

	private static final String METHOD_SEPARATOR = "."; //$NON-NLS-1$

	// private static String inputFile ;//=
	// "D:/Analyses/Hewitt/Sample/reports/CALLSTO.MODIFIED.CSV";
	// private static String outputFile;// =
	// "D:/Analyses/Hewitt/Sample/reports/matrix.csv";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			usage();
			System.exit(-1);
		}
		Calls2Matrix c2m = new Calls2Matrix();
		System.out.println("Generating components matrix...");
		c2m.process(args[0], false);
		System.out.println("Generating operations matrix...");
		c2m.process(args[0], true);

	}

	public void process(String dir, boolean isDetailed) {

		try {
			CSVReader reader = new CSVReader(new FileReader(dir+File.separatorChar+"CALLSTO.MODIFIED.CSV"), ';');
			String[] nextLine;

			ArrayList<String> callers = new ArrayList<String>();
			ArrayList<String> callees = new ArrayList<String>();
			ArrayList<Point> calls = new ArrayList<Point>();
			int callerPos = 0;
			int calleePos = 0;
			String caller;
			String callee;

			while ((nextLine = reader.readNext()) != null) {
				if(isDetailed){
					// nextLine[0] is the caller
					caller = nextLine[0];
					// nextLine[1] is the callee
					callee = nextLine[1];
				} else {
				// nextLine[0] is the caller (we take only the component
				// part and drop the operation name)
				caller = nextLine[0].substring(0, nextLine[0]
						.indexOf(METHOD_SEPARATOR));
				// nextLine[1] is the callee (we take only the component
				// part and drop the operation name)
				callee = nextLine[1].substring(0, nextLine[1]
						.indexOf(METHOD_SEPARATOR));
				}
				// System.out.println(nextLine[0]+" "+nextLine[1]);

				// if not detailed, do not consider calls to an operation in the same
				// component
				if ( isDetailed | (! caller.equals(callee))) {
					callerPos = callers.indexOf(caller);
					// if caller not found
					if (callerPos == -1) {
						// add it
						callers.add(caller);
						callerPos = callers.size() - 1;
					}

					calleePos = callees.indexOf(callee);
					// if callee not found
					if (calleePos == -1) {
						// add it
						callees.add(callee);
						calleePos = callees.size() - 1;
					}

					calls.add(new Point(callerPos, calleePos));

				}
			}

			int[][] valMatrix = new int[callers.size()][callees.size()];

			// Arrays.fill(valMatrix, 0);

			String[][] matrix = new String[callers.size() + 1][callees.size() + 1];
			matrix[0][0] = ""; //$NON-NLS-1$

			// fill first line with callees
			for (int i = 0; i < callees.size(); i++) {
				matrix[0][i + 1] = callees.get(i);
			}

			// fill first column with callers
			for (int i = 0; i < callers.size(); i++) {
				matrix[i + 1][0] = callers.get(i);
			}

			// fill array with calls
			for (int i = 0; i < calls.size(); i++) {
				Point p = calls.get(i);

				valMatrix[p.x][p.y]++;

			}

			// fill array with calls
			for (int i = 0; i < calls.size(); i++) {
				Point p = calls.get(i);

				matrix[p.x + 1][p.y + 1] = Integer
						.toString(valMatrix[p.x][p.y]);

			}
			FileWriter file;
			if (isDetailed){
				file=new FileWriter(dir+File.separatorChar+"operations.matrix.csv");
			}else{
				file=new FileWriter(dir+File.separatorChar+"components.matrix.csv");
			}
			CSVWriter writer = new CSVWriter(file, ';',CSVWriter.NO_QUOTE_CHARACTER);

			writer.writeAll(Arrays.asList(matrix));
			// writer.flush();
			writer.close();

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void usage() {
		System.err.println("Usage : " + Calls2Matrix.class.getName()
				+ " <Directory>");
	}

	private class Point {
		   public int x;
		   public int y;
		   
		   public Point(int x1, int y1){
		      this.x=x1;
		      this.y=y1;
		   }
		}}
