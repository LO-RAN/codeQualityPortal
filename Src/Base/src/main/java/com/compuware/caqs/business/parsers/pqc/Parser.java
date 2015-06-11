package com.compuware.caqs.business.parsers.pqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;

import au.com.bytecode.opencsv.CSVWriter;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.constants.Constants;
import org.apache.regexp.RESyntaxException;

/*
 * Created on 9 f�vr. 2005
 *
 */
/**
 * @author cwfr-lizac
 *  
 */
public class Parser {
    //logger

    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    // how to identify an indicator/criterium/factor line in the file
    private static final String ID_MET_REGEXP = "((I|C|F)\\d{5})";
    // for criteria and factors
    // column where mark starts
    private static final int CF_MARK_COLUMN_START = 128;
    // column where mark ends
    private static final int CF_MARK_COLUMN_END = 131;
    // for indicators
    // column where mark starts
    private static final int I_MARK_COLUMN_START = 153;
    // column where mark ends
    private static final int I_MARK_COLUMN_END = 156;
    private String m_inFilePath;
    private String m_outFilePath;

    private class Tuple {

        int m_x;
        int m_y;
        String m_value;

        public Tuple(int x, int y, String value) {
            m_x = x;
            m_y = y;
            m_value = value;
        }

        public int getX() {
            return m_x;
        }

        public int getY() {
            return m_y;
        }

        public String getValue() {
            return m_value;
        }
    }

    /**
     *
     */
    public Parser(String inFilePath, String outFilePath) {
        m_inFilePath = inFilePath;
        m_outFilePath = outFilePath;
    }

    public boolean generateCSV() throws AnalysisException {
        boolean status = false;
        ArrayList indics = new ArrayList();
        ArrayList progs = new ArrayList();
        ArrayList values = new ArrayList();

        File fi = new File(m_inFilePath);
        if (fi.exists()) {
            try {
                BufferedReader flux = new BufferedReader(new FileReader(fi));
                String line;
                String prgName = "";
                String indicName = "";
                String indicValue = "";
                int indicPos = 0;
                int prgPos = 0;
                try {
                    RE re = new RE(Parser.ID_MET_REGEXP);

                    while ((line = flux.readLine()) != null) {

                        if (re.match(line)) {

                            indicName = re.getParen(1);
                            indicPos = indics.indexOf(indicName);
                            // if indicator not found
                            if (indicPos == -1) {
                                // add it
                                indics.add(indicName);
                                indicPos = indics.size() - 1;
                            }

                            prgName = line.substring(0, 6);
                            prgPos = progs.indexOf(prgName);
                            // if program name not found
                            if (prgPos == -1) {
                                // add it
                                progs.add(prgName);
                                prgPos = progs.size() - 1;
                            }

                            // if the indicator is a raw metric (I*****)
                            if (indicName.charAt(0) == 'I') {
                                // the mark is taken from column 153
                                indicValue = line.substring(I_MARK_COLUMN_START, I_MARK_COLUMN_END);
                            } // otherwise (ie. criterium C***** or factor F*****)
                            else {
                                // the mark is taken from column 128
                                indicValue = line.substring(CF_MARK_COLUMN_START, CF_MARK_COLUMN_END);
                            }
                            values.add(new Tuple(indicPos, prgPos, indicValue));
                        }
                    }
                } catch (RESyntaxException exc) {
                    logger.error(exc.getMessage());
                }

                logger.debug("" + indics.size() + 1);
                logger.debug("" + progs.size() + 1);


                String[][] out = new String[progs.size() + 1][indics.size() + 1];
                out[0][0] = "prg_name";

                //init first line with indicators
                for (int i = 0; i < indics.size(); i++) {
                    out[0][i + 1] = indics.get(i).toString();
                }

                //init first column with program names
                for (int i = 0; i < progs.size(); i++) {
                    out[i + 1][0] = progs.get(i).toString();
                }

                // fill array with marks
                for (int i = 0; i < values.size(); i++) {
                    Tuple t = (Tuple) values.get(i);

                    logger.debug(t.getX() + " " + t.getY() + " " + t.getValue());

                    out[t.getY() + 1][t.getX() + 1] = t.getValue();
                }

                List<String[]> csvList = Arrays.asList(out);

                Writer w = new FileWriter(m_outFilePath);
                CSVWriter writer = new CSVWriter(w);
                writer.writeAll(csvList);
                w.flush();
                w.close();
                status = true;

            } catch (IOException e) {
                throw new AnalysisException("Error writing the PQC result CSV file", e);

            }
        } else {
            throw new AnalysisException("Error processing PQC data : File "
                    + fi.toString() + " doesn't exist");
        }
        return status;
    }

    public static void main(String[] args) throws AnalysisException {
        Parser p = new Parser("D:/Temp/PQC/R�sultatsPQC/BB3/PACQMK.txt", "D:/Temp/PQC/results.csv");
        p.generateCSV();
    }
}
