/**
 * 
 */
package com.compuware.caqs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author cwfr-lizac
 * 
 */
public class Trace2Calls {

	private static final String CALLSTO_FILE = "CALLSTO.MODIFIED.CSV"; //$NON-NLS-1$
	private static final String CLASSES_FILE = "ALLCLASSESMETRICS.CSV";
	private static final String METHODS_FILE = "traitedALLMODULESMETRICS.CSV"; 
	private static final String ACTION_FILE = "sequence.sdx.action"; 
	private static final String SEQUENCE_FILE = "sequence.sdx"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length < 2) {
			usage();
			System.exit(-1);
		}
		Trace2Calls t2c = new Trace2Calls();
		t2c.process(args[0], args[1]);

	}

	/**
	 * process : requirements are that the input file be a Uniface profiling
	 * trace that has been sorted by sequence Id.
	 * 
	 * @param inputFile
	 * @param outputFile
	 */
	public void process(String inputFile, String outputDir) {
		try {
			CSVReader reader = new CSVReader(new FileReader(inputFile), ';');
			CSVWriter callWriter = new CSVWriter(new FileWriter(outputDir+ File.separatorChar + CALLSTO_FILE), ';',CSVWriter.NO_QUOTE_CHARACTER);
			File temp=File.createTempFile(ACTION_FILE,null);
			BufferedWriter sdxaWriter = new BufferedWriter(new FileWriter(temp));
			
			String[] line;
			String cptName = "";
//			String previousCptName="";
			String modName = "";
			int previousLevel = 0;
			int currentLevel = 1;
			Stack<String> stack = new Stack<String>();
			ArrayList<String> components = new ArrayList<String>();
			ArrayList<String> operations = new ArrayList<String>();
			String[] call = new String[2];
			String sdxCallerMnemonic;
			String sdxCalleeMnemonic;
			String sdxCaller;
			String sdxCallee;

			BufferedWriter cptWriter = new BufferedWriter(new FileWriter(outputDir + File.separatorChar + CLASSES_FILE));
			cptWriter.write("component;vg");
			cptWriter.newLine();
			cptWriter.flush();

			BufferedWriter opeWriter = new BufferedWriter(new FileWriter(outputDir + File.separatorChar + METHODS_FILE));
			opeWriter.write("operation;vg;parent");
			opeWriter.newLine();
			opeWriter.flush();

			String operation = "";

			while ((line = reader.readNext()) != null) {
				// line[0] is the sequence number we can now safely ignore
				// (the file has been sorted by CleanTrace, right ?)
				currentLevel = Integer.parseInt(line[1]);
				cptName = line[2];
				modName = line[3];

				// at the same level or lower
				if (currentLevel <= previousLevel) {
					// remove stack top (no more relevant) down to the current level
					for (int i = 0; i < (previousLevel - currentLevel) + 1; i++) {
						stack.pop();
					}
				}

				operation = cptName + "." + modName;

				// if we don't have this component yet in the list
				if (components.indexOf(cptName) == -1) {
					// add it to the list for next check
					components.add(cptName);
					// write it to file
					cptWriter.write(cptName + ";0");
					cptWriter.newLine();
				}

				// if we don't have this operation/trigger yet in the list
				if (operations.indexOf(operation) == -1) {
					// add it to the list for next check
					operations.add(operation);
					// write it to file
					opeWriter.write(operation + ";0;" + cptName);
					opeWriter.newLine();
				}

				sdxCalleeMnemonic="[m"+(stack.size()+1)+"]";
				sdxCallee=(cptName +sdxCalleeMnemonic+"."+ modName).replace(':',' ');

				// is there at least already one ancestor in the stack
				if (stack.size() != 0) {
					// caller is the last in the stack
					call[0] = stack.peek();
					// callee is the current item
					call[1] = operation;
					callWriter.writeNext(call);

					sdxCallerMnemonic="[m"+stack.size() + "]";
					stack.capacity();
					sdxCaller=(stack.peek().substring(0, stack.peek().indexOf("."))+sdxCallerMnemonic).replace(':',' ');

					// write the call to file for the sequence diagram
					sdxaWriter.write(sdxCaller + ":" + sdxCallee);
					sdxaWriter.newLine();
				}else{
					// first call
					sdxaWriter.write("APPL:" + sdxCallee);
					sdxaWriter.newLine();					
				}
				// become the new stack top
				stack.push(operation);				
				previousLevel = currentLevel;
			}
			
			reader.close();
			callWriter.close();
			cptWriter.close();
			opeWriter.close();			

			sdxaWriter.close();
			
			// format actual sdx file that can be parsed by SDEdit ( http://sdedit.sf.net )
			
			BufferedWriter sdxWriter = new BufferedWriter(new FileWriter(outputDir + File.separatorChar + SEQUENCE_FILE));
			// write header
			sdxWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><diagram><source><![CDATA[APPL:aps");
			sdxWriter.newLine();
			// write component list
			for(int i=0;i<components.size();i++){
				sdxWriter.write(components.get(i).replace(':', ' ')+":cpt");				
				sdxWriter.newLine();
			}
			// write a blank line separating components from sequence of actions
			sdxWriter.newLine();
			
			// write actual sequence of actions
			BufferedReader sdxaReader = new BufferedReader(new FileReader(temp));		
			String sdxLine;
			while((sdxLine=sdxaReader.readLine())!=null){
				sdxWriter.write(sdxLine);
				sdxWriter.newLine();				
			}
			sdxaReader.close();
			temp.delete();
			
			// write footer
			sdxWriter.write("]]></source>");
			sdxWriter.write("<configuration>");
			sdxWriter.write("<property name=\"actorWidth\" value=\"25\"/>");
			sdxWriter.write("<property name=\"arrowSize\" value=\"6\"/>");
			sdxWriter.write("<property name=\"colorizeThreads\" value=\"true\"/>");
			sdxWriter.write("<property name=\"destructorWidth\" value=\"30\"/>");
			sdxWriter.write("<property family=\"Dialog\" name=\"font\" size=\"12\" style=\"0\"/>");
			sdxWriter.write("<property name=\"fragmentMargin\" value=\"8\"/>");
			sdxWriter.write("<property name=\"fragmentPadding\" value=\"10\"/>");
			sdxWriter.write("<property name=\"fragmentTextPadding\" value=\"3\"/>");
			sdxWriter.write("<property name=\"glue\" value=\"10\"/>");
			sdxWriter.write("<property name=\"headHeight\" value=\"35\"/>");
			sdxWriter.write("<property name=\"headLabelPadding\" value=\"5\"/>");
			sdxWriter.write("<property name=\"headWidth\" value=\"100\"/>");
			sdxWriter.write("<property name=\"initialSpace\" value=\"10\"/>");
			sdxWriter.write("<property name=\"leftMargin\" value=\"5\"/>");
			sdxWriter.write("<property name=\"lineWrap\" value=\"false\"/>");
			sdxWriter.write("<property name=\"lowerMargin\" value=\"5\"/>");
			sdxWriter.write("<property name=\"mainLifelineWidth\" value=\"8\"/>");
			sdxWriter.write("<property name=\"messageLabelSpace\" value=\"3\"/>");
			sdxWriter.write("<property name=\"messagePadding\" value=\"6\"/>");
			sdxWriter.write("<property name=\"noteMargin\" value=\"6\"/>");
			sdxWriter.write("<property name=\"notePadding\" value=\"6\"/>");
			sdxWriter.write("<property name=\"opaqueMessageText\" value=\"false\"/>");
			sdxWriter.write("<property name=\"returnArrowVisible\" value=\"true\"/>");
			sdxWriter.write("<property name=\"rightMargin\" value=\"5\"/>");
			sdxWriter.write("<property name=\"selfMessageHorizontalSpace\" value=\"15\"/>");
			sdxWriter.write("<property name=\"separatorBottomMargin\" value=\"8\"/>");
			sdxWriter.write("<property name=\"separatorTopMargin\" value=\"15\"/>");
			sdxWriter.write("<property name=\"spaceBeforeActivation\" value=\"2\"/>");
			sdxWriter.write("<property name=\"spaceBeforeAnswerToSelf\" value=\"10\"/>");
			sdxWriter.write("<property name=\"spaceBeforeConstruction\" value=\"6\"/>");
			sdxWriter.write("<property name=\"spaceBeforeSelfMessage\" value=\"7\"/>");
			sdxWriter.write("<property name=\"subLifelineWidth\" value=\"6\"/>");
			sdxWriter.write("<property name=\"threadNumbersVisible\" value=\"false\"/>");
			sdxWriter.write("<property name=\"threaded\" value=\"true\"/>");
			sdxWriter.write("<property name=\"upperMargin\" value=\"5\"/>");
			sdxWriter.write("<property name=\"verticallySplit\" value=\"false\"/>");
			sdxWriter.write("</configuration>");
			sdxWriter.write("</diagram>");
			sdxWriter.close();						


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage : " + Trace2Calls.class.getName()	+ " <inputFile> <outputDirectory>");
	}
}
