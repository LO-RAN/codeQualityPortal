/**
 * 
 */
package com.compuware.caqs.util;

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
public class CleanTrace {

	private static final char TYPE_SEP=':';
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		CleanTrace t2c = new CleanTrace();
		t2c.process(args[0], args[1]);

	}

	/**
	 * process : requirements are that the input file be a Uniface profiling trace.
	 * <pre>
	 * Exemple Uniface ASN file : 
	 * 		[SETTINGS]
	 * 		$PROC_PROFILING =TRUE 
	 * 		$PROC_LOGFILE= idf_profiling.csv
	 * 		$PROC_PROFILING_SEPARATOR=semicolon
	 * </pre>
	 * This will clean the input file from non profiling info and sort it in sequence order.
	 * The output will then be in proper format to be used as input for Trace2Calls.java.
	 *
	 * @param inputFile		Uniface Profiling file produced by a Uniface Runtime execution with the aforementioned options set in its ASN file.
	 * @param outputFile	the result of cleaning the file from non profiling info and sorting in sequence order.
	 *
	 * @see Trace2Calls
	 */
	public void process(String inputFile, String outputFile) {
		try {
			String[] nextLine;
			String component;
			String module;
			String type;
			String field;
			String entity;
			String model;
			String library;
			String procedure;
			String trigger;
			
			String id = "";
			String value = "";
			int nestLevel;
			int seqNumber;
			
			ArrayList<String[]> list= new ArrayList<String[]>();

			System.out.println("Reading input file...");

			CSVReader reader = new CSVReader(new FileReader(inputFile), ';');
			
			while ((nextLine = reader.readNext()) != null) {
				// if the line is a profiling line
				if (nextLine[0].equals("REC=PROFILING")) {
					component = "";
					module = "";
					type="";
					field="";
					entity="";
					model="";
					library="";
					procedure="";
					trigger="";
					nestLevel = 0;
					seqNumber = 0;

					for (int i = 0; i < nextLine.length; i++) {
						int pos = nextLine[i].indexOf('=');
						if (pos >= 0) {
							id = nextLine[i].substring(0, pos);
							value = nextLine[i].substring(pos + 1);
							
							// pick-up interesting values
							if ("CPTNAM".equals(id)) {
								component = value;
							} else if ("MODNAM".equals(id)) {
								module = value;
							} else if ("TYPE".equals(id)) {
								type = value;
							} else if ("FIELD".equals(id)) {
								field = value;
							} else if ("ENTITY".equals(id)) {
								entity = value;
							} else if ("MODEL".equals(id)) {
								model = value;
							} else if ("LIBNAM".equals(id)) {
								library = value;
							} else if ("CENNAM".equals(id)) {
								procedure = value;
							} else if ("TRIG".equals(id)) {
								trigger = value;
							} else if ("NEST".equals(id)) {
								nestLevel = Integer.parseInt(value);
							} else if ("SEQ".equals(id)) {
							seqNumber = Integer.parseInt(value);
						    }
					    }
					}
					String[] line=new String[4];
					line[0]=Integer.toString(seqNumber);
					line[1]=Integer.toString(nestLevel);
					
					
					// a central procedure
					if("CEN".equals(type)){
						line[2]="l"+TYPE_SEP+library;
						line[3]="g"+TYPE_SEP+procedure;
					}else{ 
						line[2]="c"+TYPE_SEP+component;
						// a trigger
						if("TRG".equals(type)){
							// at field level
							if(! "".equals(field)){
								line[3]="t"+TYPE_SEP+trigger+"("+field+":"+entity+":"+model+")";
							// at entity level
							}else if(! "".equals(entity)){
								line[3]="t"+TYPE_SEP+trigger+"("+entity+":"+model+")";
							// at model level (if it is even possible)
							}else if(! "".equals(model)){
								line[3]="t"+TYPE_SEP+trigger+"("+model+")";
							// at component level
							}else{	
								line[3]="t"+TYPE_SEP+trigger;
							}
						}
						else{// "MOD" (local proc module) or "OPR" (operation)
							line[3]=type.substring(0,1).toLowerCase()+TYPE_SEP+module;
						}
					}
											
					list.add(line);
				}
			}
			
		reader.close();
		
		System.out.println("Sorting results on sequence number...");

		// sort the results according to sequence number (first column)
		Collections.sort(list, new MyComp());
				
		System.out.println("Writing output file...");

		CSVWriter writer = new CSVWriter(new FileWriter(outputFile),';',CSVWriter.NO_QUOTE_CHARACTER);
		writer.writeAll(list);
		writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage : " + CleanTrace.class.getName()
				+ " <inputFile> <outputFile>");
	}
	
	// implements a comparator on the first column (trace sequence number)
	private class MyComp implements Comparator<String[]>{

		public int compare(String[] s1, String[] s2) {
			
			return (Integer.parseInt(s1[0])-Integer.parseInt(s2[0]));
		}

		
	}
}
