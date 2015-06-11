package com.compuware.carscode.metrics;

import com.compuware.carscode.metrics.bean.ProcContent;
import com.compuware.carscode.metrics.bean.ProcMetric;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 24 févr. 2006
 * Time: 09:39:44
 * To change this template use File | Settings | File Templates.
 */
public class Pl1ProcExtract extends MetricExtractor {

    public void extractProc(File f, PrintWriter w) throws IOException {
        FileReader reader = new FileReader(f);
        BufferedReader breader = new BufferedReader(reader);
        Pattern[] mainPatterns = createPatterns(config.getMainProcRegexp());
        ProcMetric mainMetrics = null;
        boolean foundMain = false;
        String fileName = f.getName();
        String mainProc = null;
        while (true) {
            String line = breader.readLine();
            if (line == null) {
                break;
            }
            else {
                Matcher m = getMatcher(line, mainPatterns);
                if (m.matches()) {
                	String procName = "";
                	if (m.groupCount() > 0) {
                		procName = m.group(1);
                	}
                    if (procName.equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf(".")))) {
                    	foundMain = true;
                    	mainProc = procName;
                    }
                    ProcContent procContent = getProcContent(procName, line, breader);
                    if (foundMain || procContent.isFoundSubProc()) {
                    	foundMain = true;
                    	mainProc = procName;
                    	if (mainProc.length() < 1) {
                    		mainProc = fileName.substring(0, fileName.lastIndexOf("."));
                    	}
                    	mainMetrics = extractProcMetrics(fileName.substring(0, fileName.lastIndexOf(".")), procName, procContent.getContent());
                    }
                	break;
                }
            }
        }
        breader.close();
        reader = new FileReader(f);
        breader = new BufferedReader(reader);
        Collection procMetricColl = new ArrayList();
        Pattern p = Pattern.compile(config.getProcRegexp());
        while (true) {
            String line = breader.readLine();
            if (line == null) {
                break;
            }
            else {
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    String procName = m.group(1);
                    if (!procName.equalsIgnoreCase(mainProc)) {
	                    ProcContent procContent = getProcContent(procName, line, breader);
	                    ProcMetric procMetrics = extractProcMetrics(fileName.substring(0, fileName.lastIndexOf(".")), procName, procContent.getContent());
	                    w.println(procMetrics);
	                    procMetricColl.add(procMetrics);
                    }
                }
            }
        }
        if (mainMetrics != null) {
            mainMetrics.remove(procMetricColl);
            w.println(mainMetrics);
        }
        else {
            System.out.print(" : No main found.");
        }
    }

    public void extractProgram(File f, PrintWriter w) throws IOException {
        FileReader reader = new FileReader(f);
        BufferedReader breader = new BufferedReader(reader);
        ProcMetric mainMetrics = null;
        String fileName = f.getName();
    	StringBuffer progContent = new StringBuffer();
        while (true) {
            String line = breader.readLine();
            if (line == null) {
                break;
            }
            else {
            	progContent.append(line).append("\n");
            }
        }
        breader.close();
        mainMetrics = extractProcMetrics(fileName.substring(0, fileName.lastIndexOf(".")), null, progContent.toString());
        w.println(mainMetrics);
    }

    private ProcContent getProcContent(String procName, String firstLine, BufferedReader breader) throws IOException {
        ProcContent content = new ProcContent();
    	StringBuffer result = new StringBuffer(firstLine);
        result.append("\n");
        int needEndCount = 0;
        Pattern[] commentPatterns = createPatterns(config.getCommentRegexp());
        Pattern[] endPatterns = createPatterns(config.getEndRegexp());
        /*
        Pattern[] endPatterns = new Pattern[2];
        endPatterns[0] = Pattern.compile("V[ ]+[A-Z0-9 :]*END[ ]*;.*");
        endPatterns[1] = Pattern.compile("V[ ]+[A-Z0-9 :]*END[ ]+"+procName+".*");
        */
        Pattern p = Pattern.compile(config.getProcRegexp());
        while (needEndCount >= 0) {
            String line = breader.readLine();
            if (line == null) {
                break;
            }
            else {
                Matcher cm = getMatcher(line, commentPatterns);
                if (cm.matches()) {
                    String beforeComments = cm.group(1);
                    Matcher m = getMatcher(beforeComments, endPatterns);
                    if (m.matches()) {
                    	if (needEndCount > 0) {
                    		needEndCount--;
                    		/* System.out.println(needEndCount+" - END trouvé:" + line);*/
                    	}
                    	else {
                    		break;
                    	}
                    }
                    needEndCount += countNeedEndKeyWord(beforeComments, needEndCount);
                }
                else {
                    Matcher m = getMatcher(line, endPatterns);
                    if (m.matches()) {
                    	if (needEndCount > 0) {
                    		needEndCount--;
                    		/* System.out.println(needEndCount+" - END trouvé:" + line); */
                    	}
                    	else {
                    		break;
                    	}
                    }
                    needEndCount += countNeedEndKeyWord(line, needEndCount);
                }
                result.append(line).append("\n");
                Matcher pm = p.matcher(line);
                if (pm.matches()) {
                	content.setFoundSubProc(true);
                	needEndCount++;
                }
            }
        }
        content.setContent(result.toString());
        return content;
    }

    private int countNeedEndKeyWord(String line, int needEndCount) {
        int result = 0;
        String[] complexityRegexp = config.getNeedEndRegexp();
        for (int i = 0; i < complexityRegexp.length; i++) {
            result += addNeedEnd(line, complexityRegexp[i]);
        }
        if (result > 0) {
    		/* System.out.println((needEndCount+result)+" - NEED END trouvé:" + line); */
        }
        return result;
    }

    private int addNeedEnd(String line, String keyWord) {
        int result = 0;
        int pos = 0;
        while ((pos = line.indexOf(keyWord, pos)) >= 0) {
            result += 1;
            pos += keyWord.length();
            if (keyWord.length() == 1) {
                pos++;
            }
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: Pl1ProcExtract SourceDir ConfigFile ProgramOutputFile ProcedureOutputFile");
        }
        File outProgram = new File(args[2]);
        PrintWriter writerProgram = new PrintWriter(new FileWriter(outProgram));
        File outProc = new File(args[3]);
        PrintWriter writerProc = new PrintWriter(new FileWriter(outProc));

        File f = new File(args[0]);
        File propFile = new File(args[1]);
        Pl1ProcExtract extractor = new Pl1ProcExtract();
        extractor.init(propFile);
        extractor.extractAll(f, writerProgram, writerProc);
        writerProgram.flush();
        writerProgram.close();
        writerProc.flush();
        writerProc.close();
    }
}
