package com.compuware.carscode.plugin.systemcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 23 févr. 2006
 * Time: 12:35:14
 * To change this template use File | Settings | File Templates.
 */
public class SystemCodeDao {

    private static SystemCodeDao singleton = new SystemCodeDao();

    private SystemCodeDao() {
    }

    public static SystemCodeDao getInstance() {
        return singleton;
    }

    public HashMap retrieveMetrics(File srcFile) {
        HashMap result = new HashMap();
        try {
            FileReader reader = new FileReader(srcFile);
            BufferedReader breader = new BufferedReader(reader);
            while (true) {
                String violationLine = breader.readLine();
                if (violationLine != null) {
                    String[] violationData = violationLine.split(";");
                    if (violationData != null && violationData.length > 0) {
                        String elt = violationData[0];
                        SystemCodeBean currentElement = (SystemCodeBean)result.get(elt);
                        SystemCodeRuleBean rule = new SystemCodeRuleBean();
                        rule.setId(violationData[2]);
                        rule.setValue(1);
                        if (currentElement == null) {
                            currentElement = new SystemCodeBean();
                            currentElement.setId(elt);
                            currentElement.setDesc(elt);
                            result.put(elt, currentElement);
                        }
                        currentElement.addRule(rule);
                    }
                }
                else {
                    break;
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
