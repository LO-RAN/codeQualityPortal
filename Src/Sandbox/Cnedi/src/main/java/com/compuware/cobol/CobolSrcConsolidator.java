/*
 * Test.java
 *
 * Created on 12 mars 2004, 16:52
 */

package com.compuware.cobol;

import java.io.*;
import com.compuware.io.*;
import com.compuware.io.filter.*;

import java.util.*;

/**
 *
 * @author  cwfr-fdubois
 */
public class CobolSrcConsolidator {
    
    public static final String PROP_FILE_NAME = "consolidator.properties";
    
    public static final String BATCH_TYPE = "BATCH";
    public static final String TP_TYPE = "TP";
    
    private static final String BATCH_REGEX_KEY="cobol.batch.regexp";
    private static final String TP_REGEX_KEY="cobol.tp.regexp";
    
    /** Creates a new instance of Test */
    public CobolSrcConsolidator() {
    }
    
    public void exec(File dirBase, File dirSrcCopy, File dirSrcProg, File destDirProg, File destDirCopy, String type) {
        RegexpFilter filter = new RegexpFilter();
        filter.setRegexp(getRegexp(type));
        makeDestinationTree(destDirProg, destDirCopy);
        makeCopy(dirBase, dirSrcCopy, filter, dirSrcProg, destDirProg);
        copyLib(dirBase, dirSrcProg, destDirCopy);
    }
    
    private String getRegexp(String type) {
        String result = null;
        Properties props = PropertiesReader.getProperties(PROP_FILE_NAME, this);
        if (type != null) {
            if (type.equals(BATCH_TYPE)) {
                result = props.getProperty(BATCH_REGEX_KEY);
            }
            if (type.equals(TP_TYPE)) {
                result = props.getProperty(TP_REGEX_KEY);
            }
        }
        return result;
    }
    
    private void makeDestinationTree(File destDirProg, File destDirCopy) {
        if (!destDirProg.exists()) {
            destDirProg.mkdir();
        }
        if (!destDirCopy.exists()) {
            destDirCopy.mkdir();
        }
    }
    
    private void makeCopy(File dirBase, File dirSrcCopy, RegexpFilter filter, File dirSrcProg, File dirDest) {
        File[] listCopy = dirSrcCopy.listFiles(filter);
        ArrayList coll = new ArrayList();
        for (int i = 0; i < listCopy.length; i++) {
            System.out.println(listCopy[i].getName()+"->"+RootNameExtractor.getRootName(listCopy[i].getName(), 6));
            coll.add(RootNameExtractor.getRootName(listCopy[i].getName(), 6));
        }
        Collections.sort(coll);
        Iterator it = coll.iterator();
        String lastName = "";
        while (it.hasNext()) {
            String name = (String)it.next();
            if (name.equals(lastName)) {
                it.remove();
            }
            lastName = name;
        }
        it = coll.iterator();
        while (it.hasNext()) {
            String name = (String)it.next();
            try {
                File f = new File(dirSrcProg, name+".cob");
                FileTools.copy(f, dirDest);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void copyLib(File dirBase, File dirSrcProg, File dirDest) {
        File[] subdirs = dirBase.listFiles();
        for (int i = 0; i < subdirs.length; i++) {
            File dir = subdirs[i];
            if (dir.isDirectory() && !dir.equals(dirSrcProg)) {
                try {
                    FileTools.rcopy(dir, dirDest);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File dirSrc = new File(args[0]);
        File dirSrcCopy = new File(args[1]);
        System.out.println(dirSrcCopy.getAbsolutePath());
        File dirSrcProg = new File(args[2]);
        File destDirProg = new File(args[3]);
        File destDirCopy = new File(args[4]);
        CobolSrcConsolidator test = new CobolSrcConsolidator();
        test.exec(dirSrc, dirSrcCopy, dirSrcProg, destDirProg, destDirCopy, args[5]);
    }
    
}
