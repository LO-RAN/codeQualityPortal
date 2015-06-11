package com.compuware.carscode.metrics;

import com.compuware.carscode.metrics.bean.ProcMetric;
import com.compuware.toolbox.io.filter.RegexpFilter;
import com.compuware.toolbox.io.filter.RegexpTools;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 27 févr. 2006
 * Time: 11:58:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class MetricExtractor {

    protected Configuration config = new Configuration();

    public void init(File propertyFile) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(propertyFile));
        config.init(prop);
    }

    public void extractAll(File dir, PrintWriter wprog, PrintWriter wproc) throws IOException {
        RegexpFilter filter = new RegexpFilter();
        filter.setAcceptDirectory(true);
        filter.setRegexp(config.getFileFilterRegexp());
        List l = RegexpTools.searchFiles(dir, filter);
        Iterator i = l.iterator();
        while (i.hasNext()) {
            File f = (File)i.next();
            System.out.print("Parsing " + f.getName());
            extractProgram(f, wprog);
            extractProc(f, wproc);
            System.out.println();
        }
    }

    protected abstract void extractProgram(File f, PrintWriter w) throws IOException;
    protected abstract void extractProc(File f, PrintWriter w) throws IOException;

    protected Pattern[] createPatterns(String[] regexps) {
    	Pattern[] result = new Pattern[regexps.length];
    	for (int i = 0; i < regexps.length; i++) {
			result[i] = Pattern.compile(regexps[i]);
		}
    	return result;
    }
    
    protected Matcher getMatcher(String line, Pattern[] patterns) {
    	Matcher result = null;
    	for (int i = 0; i < patterns.length; i++) {
    		result = patterns[i].matcher(line);
			if (result.matches()) {
				break;
			}
		}
    	return result;
    }
    
    protected ProcMetric extractProcMetrics(String fileName, String procName, String procContent) throws IOException {
        int nbLines = 0;
        int nbComment = 0;
        int complexity = 0;

        Pattern[] commentPatterns = createPatterns(config.getCommentRegexp());
        Pattern emptyPattern = Pattern.compile(config.getEmptyRegexp());

        ProcMetric result = new ProcMetric();
        StringReader sreader = new StringReader(procContent);
        BufferedReader breader = new BufferedReader(sreader);
        while(true) {
            String line = breader.readLine();
            if (line == null) {
                break;
            }
            else {
                Matcher m = getMatcher(line, commentPatterns);
                if (m.matches()) {
                    nbComment++;
                    String beforeComments = m.group(1);
                    Matcher mEmpty = emptyPattern.matcher(beforeComments);
                    if (!mEmpty.matches() && !beforeComments.endsWith("'")) {
                        nbLines++;
                        complexity = complexity + countComplexityKeyWord(line);
                    }
                }
                else {
                    complexity = complexity + countComplexityKeyWord(line);
                    Matcher mEmpty = emptyPattern.matcher(line);
                    if (!mEmpty.matches()) {
                        nbLines++;
                    }
                }
            }
        }
        if (procName != null) {
        	result.setName(fileName+"."+procName);
        }
        else {
        	result.setName(fileName);
        }
        result.setNocl(nbLines);
        result.setVg(complexity);
        result.setCloc(nbComment);
        return result;
    }

    private int countComplexityKeyWord(String line) {
        int result = 0;
        String[] complexityRegexp = config.getComplexityRegexp();
        for (int i = 0; i < complexityRegexp.length; i++) {
            result += addComplexity(line, complexityRegexp[i]);
        }
        return result;
    }

    private int addComplexity(String line, String keyWord) {
        int result = 0;
        int pos = 0;
        while ((pos = line.indexOf(keyWord, pos)) > 0) {
            result += 1;
            pos += keyWord.length();
            if (keyWord.length() == 1) {
            	if (line.charAt(pos) == keyWord.charAt(0)) {
            		result -=1;
            	}
        		pos++;
            }
        }
        return result;
    }

}
