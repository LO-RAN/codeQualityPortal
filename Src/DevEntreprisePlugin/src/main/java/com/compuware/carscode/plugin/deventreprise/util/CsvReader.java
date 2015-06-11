package com.compuware.carscode.plugin.deventreprise.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.compuware.toolbox.util.logging.LoggerManager;

public class CsvReader {

    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");
    private List<Map<String, String>> values = null;
    private String delimiter = ";";

    public CsvReader(String filename, String delimiter) {
        if (delimiter != null) {
            this.delimiter = delimiter;
        }
        this.constructValues(filename);
    }

    private void constructValues(String filename) {
        File f = new File(filename);
        if (f != null && f.isFile()) {
            this.values = new ArrayList<Map<String, String>>();
            //on commence par lire les noms des colonnes
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String sColumns = br.readLine();
                if (sColumns != null) {
                    String[] split = sColumns.split(this.delimiter);
                    if (split != null) {
                        //on continue avec chaque ligne
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            String[] sLine = line.split(this.delimiter);
                            if (sLine != null) {
                                Map<String, String> map = new HashMap<String, String>();
                                for (int i = 0; i < sLine.length && i
                                        < split.length; i++) {
                                    String key = split[i];
                                    map.put(key, sLine[i]);
                                }
                                this.values.add(map);
                            }
                        }
                    }
                }
                br.close();
                fr.close();
            } catch (FileNotFoundException e) {
                logger.error("DevEntreprize : CsvReader : " + e.getMessage());
            } catch (IOException e) {
                logger.error("DevEntreprize : CsvReader : " + e.getMessage());
            }
        }
    }

    public String getColumnValue(String columnIdName, String id, String columnName) {
        String retour = null;
        if (id != null) {
            for (Iterator<Map<String, String>> it = this.values.iterator(); it.hasNext();) {
                Map<String, String> line = it.next();
                String thisId = line.get(columnIdName);
                if (thisId != null && id.equals(thisId)) {
                    retour = line.get(columnName);
                    break;
                }
            }
        }
        return retour;
    }

    public Map<String, String> getUniqueLinesFor(String columnIdName, String id) {
        Map<String, String> retour = null;
        if (id != null) {
            for (Iterator<Map<String, String>> it = this.values.iterator(); it.hasNext();) {
                Map<String, String> line = it.next();
                String thisId = line.get(columnIdName);
                if (thisId != null && id.equals(thisId)) {
                    retour = line;
                    break;
                }
            }
        }
        return retour;
    }

    public List<Map<String, String>> getAllLinesFor(String columnIdName, String id) {
        List<Map<String, String>> retour = new ArrayList<Map<String, String>>();
        boolean foundOne = false;
        if (id != null) {
            for (Iterator<Map<String, String>> it = this.values.iterator(); it.hasNext();) {
                Map<String, String> line = it.next();
                String thisId = line.get(columnIdName);
                if (thisId != null && id.equals(thisId)) {
                    retour.add(line);
                    foundOne = true;
                } else if (foundOne) {
                    /* End of the list for the given program. */
                    break;
                }
            }
        }
        return retour;
    }
}
