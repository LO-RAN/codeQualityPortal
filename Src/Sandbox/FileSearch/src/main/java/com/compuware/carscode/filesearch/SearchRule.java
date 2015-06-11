package com.compuware.carscode.filesearch;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 9 févr. 2006
 * Time: 16:29:31
 * To change this template use File | Settings | File Templates.
 */
public class SearchRule {

    /** The name of the search rule. */
    private String name;
    /** The file filter to apply. */
    private String fileFilterRegexp;
    /** The list of regular expression to apply in files. */
    private List regexpList;

    /**
     * Getter for the rule name.
     * @return the name of the search rule.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the rule name.
     * @param name the rule name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the file filter to apply.
     * @return the file filter to apply.
     */
    public String getFileFilterRegexp() {
        return fileFilterRegexp;
    }

    /**
     * Setter for the file filter to apply.
     * @param fileFilterRegexp the file filter to apply.
     */
    public void setFileFilterRegexp(String fileFilterRegexp) {
        this.fileFilterRegexp = fileFilterRegexp;
    }

    /**
     * Getter for the list of regular expression to apply in files.
     * @return the list of regular expression to apply in files.
     */
    public List getRegexpList() {
        return regexpList;
    }

    /**
     * Setter for the list of regular expression to apply in files.
     * @param regexpList the list of regular expression to apply in files.
     */
    public void setRegexpList(List regexpList) {
        this.regexpList = regexpList;
    }

}
