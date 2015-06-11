/**
 * 
 */
package com.compuware.caqs.business.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compuware.caqs.business.parser.bean.IElementBean;
import com.compuware.caqs.business.parser.bean.Section;
import com.compuware.caqs.business.parser.config.AbstractConfigurationFactory;
import com.compuware.caqs.business.parser.config.IParserConfiguration;
import com.compuware.caqs.business.parser.util.FileUtil;
import com.compuware.caqs.business.parser.writer.XmlWriter;

/**
 * @author cwfr-fdubois
 *
 */
public class GenericParser {

    IParserConfiguration config = null;

    public GenericParser(IParserConfiguration config) {
        this.config = config;
    }

    public List<IElementBean> analyze(String basePath) throws IOException {
        List<IElementBean> result = new ArrayList<IElementBean>();
        if (this.config != null && this.config.getSourceFileList() != null) {
            Iterator<File> fileIter = this.config.getSourceFileList().iterator();
            File currentFile = null;
            while (fileIter.hasNext()) {
                currentFile = fileIter.next();
                result.addAll(analyzeFile(currentFile, basePath));
            }
        }
        XmlWriter writer = new XmlWriter();
        writer.print(result, this.config.getResultFile());
        return result;
    }

    public List<IElementBean> analyzeFile(File f, String basePath) throws IOException {
        String fileContent = FileUtil.extractContent(f);
        String filePath = extractRelativeFilePath(f.getAbsolutePath(), basePath);
        String[] fileCommentSeparation = separateComments(fileContent);
        int[] lineIndexes = extractLineIndexes(fileContent);
        List<Detector> detectorList = this.config.getDetectorList();
        List<IElementBean> sections = extractSections(filePath, fileCommentSeparation[CODEONLY_IDX], this.config.getSectionList());
        Iterator<IElementBean> sectionIter = sections.iterator();
        while (sectionIter.hasNext()) {
            IElementBean elementBean = sectionIter.next();
            elementBean.setFilePath(filePath);
            Iterator<Detector> detectorIter = detectorList.iterator();
            Detector currentDetector = null;
            while (detectorIter.hasNext()) {
                currentDetector = detectorIter.next();
                if (currentDetector.useDetector(elementBean.getTypeElt())) {
                    elementBean.setMetric(currentDetector.inspect(fileCommentSeparation[CODE_IDX], fileCommentSeparation[CODEONLY_IDX], fileCommentSeparation[COMMENTONLY_IDX], elementBean.getStartIdx(), elementBean.getEndIdx(), lineIndexes));
                }
            }
        }
        System.out.println(sections);
        return sections;
    }

    private List<IElementBean> extractSections(String fileName, String fileContent, List<Section> sectionList) {
        List<IElementBean> result = new ArrayList<IElementBean>();
        if (sectionList != null) {
            Iterator<Section> sectionIter = sectionList.iterator();
            while (sectionIter.hasNext()) {
                result.addAll(sectionIter.next().extractSections(fileName, fileContent, null));
            }
        }
        return result;
    }

    private String extractRelativeFilePath(String fullPath, String basePath) {
        String result = fullPath;
        if (basePath != null) {
            result = result.substring(basePath.length());
        }
        result = result.replaceAll("\\\\", "/");
        if (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }
    private static final int CODE_IDX = 0;
    private static final int CODEONLY_IDX = 1;
    private static final int COMMENTONLY_IDX = 2;

    /**
     * Separate code and comments in two different Strings.
     * Comments or code are replaced by empty lines or strings.
     * @param fileContent the file content.
     * @return two strings with code only and comment only.
     */
    private String[] separateComments(String fileContent) {
        String[] result = new String[3];
        result[CODE_IDX] = fileContent;
        result[CODEONLY_IDX] = fileContent;
        result[COMMENTONLY_IDX] = fileContent;
        if (this.config != null && this.config.getCommentPatternList() != null) {
            List<Pattern> commentPatternList = this.config.getCommentPatternList();
            Iterator<Pattern> commentPatternIter = commentPatternList.iterator();
            Map<Integer, String> commentMap = new HashMap<Integer, String>();
            while (commentPatternIter.hasNext()) {
                StringBuffer tmpCode = new StringBuffer();
                Matcher m = commentPatternIter.next().matcher(result[CODEONLY_IDX]);
                while (m.find()) {
                    m.appendReplacement(tmpCode, clearLines(m.group()));
                    commentMap.put(m.start(), m.group());
                }
                m.appendTail(tmpCode);
                result[CODEONLY_IDX] = tmpCode.toString();
            }
            result[COMMENTONLY_IDX] = keepCommentsOnly(fileContent, commentMap);
        }
        return result;
    }

    private String keepCommentsOnly(String fileContent,
            Map<Integer, String> commentMap) {
        StringBuffer result = new StringBuffer();
        Set<Integer> idxSet = commentMap.keySet();
        List<Integer> idxList = new ArrayList<Integer>(idxSet);
        Collections.sort(idxList);
        Iterator<Integer> idxIter = idxList.iterator();
        int lastIdx = 0;
        int currentIdx = 0;
        String currentComment = null;
        while (idxIter.hasNext()) {
            currentIdx = idxIter.next();
            result.append(clearLines(fileContent.substring(lastIdx, currentIdx)));
            currentComment = commentMap.get(currentIdx);
            result.append(currentComment);
            lastIdx = currentIdx + currentComment.length();
        }
        result.append(clearLines(fileContent.substring(lastIdx, fileContent.length())));
        return result.toString();
    }
    private static final Pattern LINE_CONTENT_PATTERN = Pattern.compile("[^\n]");

    private String clearLines(String value) {
        return LINE_CONTENT_PATTERN.matcher(value).replaceAll(" ");
    }

    protected int[] extractLineIndexes(String content) {
        Pattern linePattern = Pattern.compile("^.*$", Pattern.MULTILINE);
        int[] result = new int[linePattern.split(content).length + 1];
        Matcher m = linePattern.matcher(content);
        int i = 0;
        while (m.find()) {
            result[i++] = m.start() + 1;
        }
        return result;
    }

    /**
     * Methode main
     * @param args prend en argument :
     *      - une liste de repertoir sources, non vide, separes par des virgules
     *      - le repertoire de resultats, non vide
     *      - le langage, non vide
     *      - le repertoire de base des sources, non vide
     *      - le repertoire contenant les fichiers de langage
     *      - le fichier callsTo, potentiellement vide
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        int retour = 0;
        int nbArgs = args.length;

        if (nbArgs >= 5) {
            String[] sFileList = args[0].split(",");
            String language = args[2];
            File resultFile = new File(args[1], language+"Result.xml");
            File languageFile = new File(args[4], language+".xml");
            String basePath = args[3];
            File callsTo = null;
            if(nbArgs > 5) {
                callsTo = new File(args[5]);
            }
            AbstractConfigurationFactory configFactory = AbstractConfigurationFactory.getFactory();
            IParserConfiguration config = configFactory.createConfig(new ArrayList<File>(), resultFile, callsTo, languageFile);
            if(sFileList != null) {
                for(int i=0; i<sFileList.length; i++) {
                    config.addSource(new File(sFileList[i]));
                }
            }
            GenericParser parser = new GenericParser(config);
            /*List<IElementBean> result = */parser.analyze(basePath);
        } else {
            System.out.println("Generic Parser usage :");
    		System.out.println("[Source list] [Full result file's path] [langage configuration file] [Base path] [callsTo file : optionnal]");
    		retour = -1;
        }
        System.exit(retour);
    }
}
