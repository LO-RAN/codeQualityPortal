package com.compuware.caqs.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.compuware.caqs.domain.dataschemas.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 29 d�c. 2005
 * Time: 11:41:58
 * To change this template use File | Settings | File Templates.
 */
public class LangageUtil {

    private static final String LANGAGE_FILE_EXT_PREFIX = "langage.file.ext.";

    public static List getFileNameFromDescription(String idLangage,
            String idTelt, String description, Properties dynProps) {
        List result = new ArrayList();
        String name = null;
        if (description != null && description.length() > 0) {
            if (ElementType.MET.equals(idTelt)) {
                name = description.replaceFirst("\\(.*\\)", "");
                name = name.substring(0, name.lastIndexOf("."));
            } else {
                name = description;
            }
            name = name.replaceAll("\\.", "/");
            String fileExtListStr = dynProps.getProperty(LANGAGE_FILE_EXT_PREFIX
                    + idLangage);
            String[] fileExtList = fileExtListStr.split(",");
            for (int i = 0; i < fileExtList.length; i++) {
                result.add(name + '.' + fileExtList[i]);
            }
        }
        return result;
    }
}
