package com.compuware.carscode.plugin.deventreprise.dataschemas;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compuware.carscode.plugin.deventreprise.util.Constants;
import java.io.FileReader;
import java.util.HashMap;

public class Procedure {

    private int id;
    private String name;
    private int lineNumber;
    private int mcCabe;
    private int nbIO = 0;
    private int nbGOTOs = 0;
    private Program parent = null;
    private String programName = "";
    private int loc = 0;
    private int cloc = 0;
    private int nl = 0;
    private int nbParagrahs = 0;
    //private boolean performRangeViolation = false;

    public Procedure(Map<String, String> line, Program p) {
        this.parent = p;
        this.name = line.get(Constants.PROCEDURE_NAME);
        String s = line.get(Constants.PROCEDURE_NB_IO);
        // with DevEnterprise 5.2, column header changed
        if (null == s){
            s=line.get(Constants.PROCEDURE_NB_IO_NEW);
        }
        this.nbIO = (new Integer(s)).intValue();
        String sMcCabe = line.get(Constants.MCCABE);
        this.mcCabe = (new Integer(sMcCabe)).intValue();
        String sNbGOTOs = line.get(Constants.GOTOS);
        this.nbGOTOs = (new Integer(sNbGOTOs)).intValue();
        this.programName = line.get(Constants.PROGRAM);
    }

    public void analyze(BufferedReader br) throws IOException {
        Pattern pattern = Pattern.compile("[A-Z0-9\\s]{0,6}[\\s|D]([A-Z0-9-]+)(\\sSECTION)?(\\.)\\s*");
        Pattern endProgramPattern = Pattern.compile("[A-Z0-9\\s]{0,6}[\\s|D]END PROGRAM\\s[A-Z0-9-]+\\.\\s*");
        if (this.name.contains(" THRU ")) {
            this.analyseThru(this.name, br, pattern, endProgramPattern);
        } else {
            this.getLOC(this.name, br, pattern, endProgramPattern);
            this.nbParagrahs = 1;
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> line = new HashMap<String, String>();
        line.put(Constants.PROCEDURE_NAME, "PAGINATION-ECRAN THRU E--PAGINATION-ECRAN");
        line.put(Constants.PROCEDURE_NB_IO, "0");
        line.put(Constants.MCCABE, "7");
        line.put(Constants.GOTOS, "0");
        line.put(Constants.PROGRAM, "PREGR1");
        Program p = null;
        Procedure proc = new Procedure(line, p);
        BufferedReader br = new BufferedReader(new FileReader("D:\\DevEnterprise\\Download\\OR7SQE\\SFI\\INT\\DAV\\COBRP\\LDD\\PREGR1.CBL"), 2048);
        proc.analyze(br);
        br.close();
        System.out.println(proc.getLoc());
    }

    int getLoc() {
        return this.loc;
    }

    private String getProcedureNameInLine(String line, Pattern p) {
        String retour = "";
        Matcher m = p.matcher(line);
        if (m.find() && m.start() == 0) {
            line = line.substring(6);
            line = line.trim();
            line = line.substring(0, line.indexOf('.'));
            if (line.endsWith(" SECTION")) {
                line = line.substring(0, line.lastIndexOf(" SECTION"));
            }
            retour = line;
        }
        return retour;
    }

    private void analyseThru(String thruName, BufferedReader br, Pattern p, Pattern endProgramPattern) throws IOException {
        String[] split = thruName.split(" THRU ");
        if (split != null && split.length == 2) {
            String start = split[0];
            String end = split[1];
            getLOC(start, end, br, p, endProgramPattern);
        }
    }

    private void getProcNames(String start, String end, BufferedReader br, Pattern p, List<String> procNames) throws IOException {
        String line = null;
        boolean startFound = false;
        br.mark(2000);
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) {
                continue;
            }
            //line = line.substring(6);
            if (line.trim().startsWith("*")) {
                continue;
            }
            String s = this.getProcedureNameInLine(line, p);
            if ("".equals(s)) {
                continue;
            }
            if (!startFound) {
                if (start.equals(s)) {
                    startFound = true;
                    procNames.add(start);
                }
            } else {
                procNames.add(s);
                if (end.equals(s)) {
                    break;
                }
            }
        }
    }

    private void getLOC(String procName, BufferedReader br, Pattern p, Pattern endProgramPattern) throws IOException {
        String line = null;
        boolean startCount = false;
        while ((line = br.readLine()) != null) {
            br.mark(512);
            Matcher m = endProgramPattern.matcher(line);
            if (m.matches()) {
                //end program
                break;
            }

            if (line.trim().equals("")) {
                if (startCount) {
                    nl++;
                }
                continue;
            }
            //line = line.substring(6);
            if (line.length() > 6 && (line.charAt(6) == '*')) {
                if (startCount) {
                    nl++;
                    cloc++;
                }
                continue;
            }
            String s = this.getProcedureNameInLine(line, p);
            if (!"".equals(s)) {
                if (s.equals(procName)) {
                    startCount = true;
                } else {
                    startCount = false;
                    br.reset();
                    break;
                }
            }
            if (startCount) {
                nl++;
                loc++;
            }
        }
    }

    private void getLOC(String start, String end, BufferedReader br, Pattern p, Pattern endProgramPattern) throws IOException {
        String line = null;
        boolean startCount = false;
        boolean lastParagraph = false;
        while ((line = br.readLine()) != null) {
            Matcher m = endProgramPattern.matcher(line);
            if (m.matches()) {
                //end program, quit
                break;
            }

            /* Line is empty: just count number of lines. */
            if (line.trim().equals("")) {
                if (startCount) {
                    nl++;
                }
                br.mark(512);
                continue;
            }

            /* Line is a comment: count number of lines and comment lines. */
            if (line.length() > 6 && (line.charAt(6) == '*')) {
                if (startCount) {
                    nl++;
                    cloc++;
                }
                br.mark(512);
                continue;
            }

            String s = this.getProcedureNameInLine(line, p);
            if (!"".equals(s)) {
                if (s.equals(start)) {
                    startCount = true;
                } else if (s.equals(end)) {
                    lastParagraph = true;
                } else if (lastParagraph) {
                    br.reset();
                    break;
                }
                if (startCount) {
                    this.nbParagrahs++;
                }
            }
            br.mark(512);
            if (startCount) {
                nl++;
                loc++;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbIO() {
        return nbIO;
    }

    public void setNbIO(int nbIO) {
        this.nbIO = nbIO;
    }

    public String toString() {
        StringBuffer bf = new StringBuffer();
        String myname = this.name;
        myname = myname.replaceAll("\\s", "_");
        bf.append("  <elt type=\"PROCEDURE\" name=\"" + this.parent.getCobolPackage().getCobolPackage() + "." +
                this.parent.getName() + "." + myname + "\" filepath=\"" +
                this.parent.getCobolPackage().getCobolPackagePath() + "/" + this.parent.getElementName() + ".CBL\">\n");
        bf.append("    <parent name=\"").append(this.parent.getCobolPackage().getCobolPackage()).append('.').append(this.parent.getName()).append("\"/>\n");
        if (this.nbIO > 0) {
            bf.append("    <metric id=\"IO-STMT-NB\" value=\"" + this.nbIO + "\" />\n");
        }
        bf.append("    <metric id=\"CLOC\" value=\"" + this.cloc + "\" />\n");
        bf.append("    <metric id=\"LOC\" value=\"" + this.loc + "\" />\n");
        bf.append("    <metric id=\"NL\" value=\"" + this.nl + "\" />\n");
        bf.append("    <metric id=\"NB_PARAGRAPHS\" value=\"" + this.nbParagrahs + "\" />\n");
        bf.append("    <metric id=\"NB-GOTOS\" value=\"" + this.nbGOTOs + "\" />\n");
        bf.append("    <metric id=\"MCCABE-CYCLOMATIC\" value=\"" + this.mcCabe + "\" />\n");
        //bf.append("    <metric id=\"GOTO-OUT-OF-PERFORM\" value=\""+this.performRangeViolation+"\" />\n");
        bf.append("  </elt>\n");
        return bf.toString();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
