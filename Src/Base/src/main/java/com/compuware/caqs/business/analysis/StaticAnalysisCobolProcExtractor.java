/*
 * CobolProcExtractor.java
 *
 * Created on 17 mars 2004, 10:16
 */

package com.compuware.caqs.business.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.io.filter.RegexpFilter;
import com.compuware.toolbox.io.filter.RegexpTools;
import com.compuware.toolbox.io.filter.RootNameExtractor;

/**
 *
 * @author  cwfr-fdubois
 */
public class StaticAnalysisCobolProcExtractor extends StaticAnalysis {

	private Properties props = null;
    
    /** Creates a new instance of CobolProcExtractor */
    public StaticAnalysisCobolProcExtractor() {
        super() ;
        props = PropertiesReader.getProperties("cobolproc.properties", this);
        logger.info("Cobol procedure extract");
    }
    
    protected void initSpecific(Properties dbProps){
        
    }
    
    protected void loadData(EA curEA) throws LoaderException {
    }
    
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        File srcBase = new File(curEA.getSourceDir());
        File srcCopy = new File(srcBase, "lib"+File.separator+props.getProperty("copy.ref.dir.name")+File.separator);
        logger.info(srcCopy.getAbsolutePath());
        File srcProg = new File(srcBase, "src/");
        logger.info(srcProg.getAbsolutePath());
        exec(srcCopy, srcProg, srcBase);
    }

	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        return result;
    }

    private void exec(File dirSrcCopy, File dirSrcProg, File destDir) throws AnalysisException {
        String[] progList = dirSrcProg.list();
        RegexpFilter filter = new RegexpFilter();
        filter.setRegexp(props.getProperty("copy.toanalyse.regexp"));
        Collection coll = getList(dirSrcCopy, filter, progList);
        exportList(coll, props.getProperty("proc.entry.regexp"), destDir);
    }
    
    private Collection getList(File dirSrcCopy, RegexpFilter filter, String[] progList) {
        File[] listCopy = dirSrcCopy.listFiles(filter);
        ArrayList coll = new ArrayList();
        int rootSize = Integer.parseInt(props.getProperty("proc.entry.startsize"));
        for (int i = 0; i < listCopy.length; i++) {
            String progName = RootNameExtractor.getRootName(listCopy[i].getName(), rootSize);
            int found = Arrays.binarySearch(progList, progName+"."+props.getProperty("file.ext"));
            if (found >= 0) {
                coll.add(listCopy[i]);
            }
        }
        return coll;
    }
    
    private void exportList(Collection coll, String regexp, File destDir) throws AnalysisException {
        Iterator i = coll.iterator();
        File fo = new File(destDir, "proclist.txt");
        try {
	        FileWriter fw = new FileWriter(fo);
	        BufferedWriter bufout = new BufferedWriter(fw);
	        int rootSize = Integer.parseInt(props.getProperty("proc.entry.startsize"));
	        int group = Integer.parseInt(props.getProperty("proc.entry.regexp.group"));
	        while (i.hasNext()) {
	            File f = (File)i.next();
	            String progName = RootNameExtractor.getRootName(f.getName(), rootSize);
	            String str = RegexpTools.getListFromFile(f, regexp, group, progName+".", "\r\n");
	            bufout.write(str);
	        }
	        bufout.flush();
	        bufout.close();
        }
        catch (IOException e) {
            logger.error("Error exporting cobol proc list", e);
            throw new AnalysisException("Error exporting cobol proc list", e);
        }
	        
    }
    
}
