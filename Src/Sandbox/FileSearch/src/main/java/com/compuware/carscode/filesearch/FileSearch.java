package com.compuware.carscode.filesearch;

import com.compuware.io.filter.RegexpFilter;
import com.compuware.io.filter.RegexpTools;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 9 févr. 2006
 * Time: 16:03:31
 * To change this template use File | Settings | File Templates.
 */
public class FileSearch {

    private static FileSearch singleton = new FileSearch();

    private FileSearch() {
    }

    /**
     * Obtain an instance of FileSearch.
     * @return an instance of FileSearch.
     */
    public static FileSearch getInstance() {
        return singleton;
    }

    /**
     * Search all the lines corresponding to a regexp list in every files contained into a given directory.
     * Write the result into a given writer.
     * @param dir the base directory.
     * @param fileFilter a filter for file contained into the directories.
     * @param regexpList a regular expression list.
     * @param writer the writer for the result.
     * @throws IOException for I/O access error.
     */
    public void search(File dir, FileFilter fileFilter, List regexpList, PrintWriter writer) throws IOException {
        // Retrieve the list of valid files for the given filter (recursive retrieve).
        List foundFiles = RegexpTools.searchFiles(dir, fileFilter);
        // Loop on the list.
        Iterator i = foundFiles.iterator();
        while (i.hasNext()) {
            File f = (File)i.next();
            // Retrieve the lines matching with the regexp list.
            String res = RegexpTools.getFileLines(f, regexpList);
            if (res != null && res.length() > 0) {
                // Display the file name and the found lines if not empty.
                writer.println("Fichier source: "+f.getAbsolutePath());
                res = res.replaceAll("<", "&lt;");
                writer.println(res);
            }
        }
    }

    /**
     * Search all the lines corresponding to a given search rule in every files contained into a given directory.
     * Write the result into a given writer.
     * @param dir the base directory.
     * @param searchRule a rule containing a file filter and a regexp list.
     * @param writer the writer for the result.
     * @throws IOException for I/O access error.
     */
    public void search(File dir, SearchRule searchRule, PrintWriter writer) throws IOException {
        RegexpFilter filter = new RegexpFilter();
        filter.setAcceptDirectory(true);
        filter.setRegexp(searchRule.getFileFilterRegexp());
        search(dir, filter, searchRule.getRegexpList(), writer);
    }

    /**
     * Search all the lines corresponding to a given search rule in every files contained into a given directory.
     * Write the result into a given writer.
     * @param dir the base directory.
     * @param searchRuleList a list of search rule.
     * @param writer the writer for the result.
     * @throws IOException for I/O access error.
     */
    public void search(File dir, List searchRuleList, PrintWriter writer) throws IOException {
        if (searchRuleList != null) {
            writer.println("<RESULT>");
            Iterator i = searchRuleList.iterator();
            while (i.hasNext()) {
                SearchRule searchRule = (SearchRule)i.next();
                writer.println("<SEARCHRULE name=\""+searchRule.getName()+"\">");
                search(dir, searchRule, writer);
                writer.println("</SEARCHRULE>");
            }
            writer.println("</RESULT>");
        }
    }

    /**
     * Entry point:
     * @param args arguments: 0- base directory 1- xml config file (optional 2- result file)
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: FileSearch SourceDir ConfigFile [outputFile]");
        }
        File dir = new File(args[0]);
        XmlRuleParser parser = new XmlRuleParser();
        parser.init(args[1]);
        PrintWriter writer = null;
        if (args.length > 2) {
            File out = new File(args[2]);
            writer = new PrintWriter(new FileWriter(out));
        }
        else {
            writer = new PrintWriter(System.out);
        }
        FileSearch fileSearch = FileSearch.getInstance();
        List ruleSet = parser.getRuleSet();
        fileSearch.search(dir, ruleSet, writer);
        writer.flush();
    }
}
