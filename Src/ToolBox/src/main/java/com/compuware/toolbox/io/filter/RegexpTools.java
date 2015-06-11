/*
 * RegexpTools.java
 *
 * Created on 15 mars 2004, 15:04
 */

package com.compuware.toolbox.io.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A set of useful methods for regexp parsing on files.
 * @author cwfr-fdubois
 */
public class RegexpTools {
    
    /** Creates a new instance of RegexpTools */
    public RegexpTools() {
    }
    
    /** Create the matcher on the given file and the given regexp.
     * @param input the input file for the parsing.
     * @param regexp the regexp to apply on the input file.
     * @return the matcher for the regexp applied on the given file.
     */
    private static Matcher getMatcherForFile(File input, String regexp) throws FileNotFoundException, IOException, CharacterCodingException {
        // Create a pattern to match comments
        Pattern p = Pattern.compile(regexp, Pattern.MULTILINE);
        FileInputStream fis = new FileInputStream(input);
        FileChannel fc = fis.getChannel();
        
        // Get a CharBuffer from the source file
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int)fc.size());
        Charset cs = Charset.forName("8859_1");
        CharsetDecoder cd = cs.newDecoder();
        CharBuffer cb = cd.decode(bb);

        // Run some matches
        Matcher m = p.matcher(cb);
        return m;
    }
    
    /** Retrieve the sets of values corresponding to a given regexp applied on the given input file.
     * The regexp contains many groups, each result is return via an array of string (one cell per group result).
     * @return the collection of the set corresponding to all the group resul of the regexp.
     * @param input the input file.
     * @param regexp the regexp to apply on the inut file.
     * @param nbGroup the number of group contained into the regexp to retrieve.
     * @throws FileNotFoundException the input file does not exist.
     * @throws IOException a IO exception occured parsing the file.
     * @throws CharacterCodingException the input file character encoding is not allowed.
     */
    public static Collection getListForAllGroupFromFile(File input, String regexp, int nbGroup) throws FileNotFoundException, IOException, CharacterCodingException {
        // The result list.
        ArrayList result = new ArrayList();
        
        Matcher m = getMatcherForFile(input, regexp);
        while (m.find()) {
            String[] groupResults = new String[nbGroup];
            for (int i = 0; i < nbGroup; i++) {
                groupResults[i] = m.group(i+1);
            }
            result.add(groupResults);
        }
        
        return result;
    }
    
    /** Retrieve the first result found by the regexp on the input file for the given group.
     * @param input the input file.
     * @param regexp the regexp to apply on the inut file.
     * @param group the regexp group used to retrieve the information.
     * @throws FileNotFoundException the input file does not exist.
     * @throws IOException a IO exception occured parsing the file.
     * @throws CharacterCodingException the input file character encoding is not allowed.
     * @return the first result found by the regexp on the input file for the given group.
     */    
    public static String getFirstFromFile(File input, String regexp, int group) throws FileNotFoundException, IOException, CharacterCodingException {
        // The result string.
        String result = "";
        
        Matcher m = getMatcherForFile(input, regexp);
        if (m.find()) {
            result = m.group(group);
        }
        
        return result;
    }
    
    /** Retrieve all results found by the regexp on the input file for the given group as a collection.
     * @param input the input file.
     * @param regexp the regexp to apply on the inut file.
     * @param group the regexp group used to retrieve the information.
     * @throws FileNotFoundException the input file does not exist.
     * @throws IOException a IO exception occured parsing the file.
     * @throws CharacterCodingException the input file character encoding is not allowed.
     * @return all results found by the regexp on the input file for the given group as a collection
     */    
    public static Collection getListFromFile(File input, String regexp, int group) throws FileNotFoundException, IOException, CharacterCodingException {
        // The result list.
        ArrayList result = new ArrayList();
        
        Matcher m = getMatcherForFile(input, regexp);
        while (m.find()) {
            result.add(m.group(group));
        }
        
        return result;
    }
    
    /** Retrieve all results found by the regexp on the input file for the given group as a collection.
     * It adds prefix and suffix to each result.
     * @param input the input file.
     * @param regexp the regexp to apply on the inut file.
     * @param group the regexp group used to retrieve the information.
     * @param before the prefix to add to the result.
     * @param after the suffix to add to the result.
     * @throws FileNotFoundException the input file does not exist.
     * @throws IOException a IO exception occured parsing the file.
     * @throws CharacterCodingException the input file character encoding is not allowed.
     * @return all results found by the regexp on the input file for the given group as a collection adding prefix and suffix.
     */    
    public static String getListFromFile(File input, String regexp, int group, String before, String after) throws FileNotFoundException, IOException, CharacterCodingException {
        // The result list.
        StringBuffer result = new StringBuffer();
        
        Matcher m = getMatcherForFile(input, regexp);
        while (m.find()) {
            result.append(before);
            result.append(m.group(group));
            result.append(after);
        }
        
        return result.toString();
    }
    
    /** Filter the content of the given file using a regexp and a list of filter.
     * @param input the input file.
     * @param filter the result filter.
     * @param regexp the regexp to apply on the inut file.
     * @param group the regexp group used to retrieve the information.
     * @param skip the number of line to skip.
     * @throws IOException a IO exception occured parsing the file.
     * @return the content of the given file using a regexp and a list of filter.
     */
    public static String getFilteredFile(File input, List filter, String regexp, int group, int skip) throws IOException {
        StringBuffer result = new StringBuffer();
        FileReader fis = new FileReader(input);
        BufferedReader bufis = new BufferedReader(fis);
        Pattern p = Pattern.compile(regexp);
        String line = "";
        int i = 0;
        while ((line = bufis.readLine()) != null) {
            if (i++ < skip) {
                result.append(line).append("\r\n");
            }
            else {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String part = m.group(group);
                    if (Collections.binarySearch(filter, part) >= 0) {
                        result.append(line).append("\r\n");
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Create a pattern list from a regexp list.
     * @param regexpList a regexp list.
     * @return a pattern list.
     */
    private static List getPatternList(List regexpList) {
        List result = new ArrayList();
        if (regexpList != null) {
            Iterator i = regexpList.iterator();
            while (i.hasNext()) {
                String regexp = (String)i.next();
                Pattern p = Pattern.compile(regexp);
                result.add(p);
            }
        }
        return result;
    }

    private static boolean applyPattern(String line, List patternList) {
        boolean result = false;
        if (line != null && line.length() > 0 && patternList != null && patternList.size() > 0) {
            Iterator i = patternList.iterator();
            while (i.hasNext() && !result) {
                Pattern p = (Pattern)i.next();
                Matcher m = p.matcher(line);
                if (m.find()) {
                    result = true;
                }
            }
        }
        return result;
    }

    /** Filter the content of the given file using a regexp and a list of filter.
     * @param input the input file.
     * @param regexpList a regexp list.
     * @throws IOException a IO exception occured parsing the file.
     * @return the content of the given file using a regexp and a list of filter.
     */
    public static String getFileLines(File input, List regexpList) throws IOException {
        StringBuffer result = new StringBuffer();
        FileReader fis = new FileReader(input);
        BufferedReader bufis = new BufferedReader(fis);
        List patternList = getPatternList(regexpList);
        String line = "";
        int i = 1;
        while ((line = bufis.readLine()) != null) {
            if (applyPattern(line, patternList)) {
                result.append('[').append(i).append("] ").append(line).append("\r\n");
            }
            i++;
        }
        return result.toString();
    }

    public static List searchFiles(File dir, FileFilter filter) {
        List result = new ArrayList();
        File[] tmpList = dir.listFiles(filter);
        if (tmpList != null) {
            for (int i = 0; i < tmpList.length; i++) {
                File f = tmpList[i];
                if (f.isDirectory()) {
                    List subDirList = searchFiles(f, filter);
                    result.addAll(subDirList);
                }
                else {
                    result.add(f);
                }
            }
        }
        return result;
    }

}
