package com.compuware.toolbox.util.resources;

import java.util.Locale;

public interface Internationalizable {

    public static final String KEY_SEPARATOR = ".";
    public static final String LIB_PROPERTY_KEY = "lib";
    public static final String DESC_PROPERTY_KEY = "desc";
    public static final String COMPL_PROPERTY_KEY = "compl";

    public abstract String getTextKey(String columnName);

    public String getId();

    /** Get the data label.
     * @param loc the locale 
     * @return the data label.
     */
    public String getLib(Locale loc);

    /** Get the data description.
     * @param loc the locale 
     * @return the data description.
     */
    public String getDesc(Locale loc);

    /** Get the data complement.
     * @param loc the locale 
     * @return the data complement .
     */
    public String getComplement(Locale loc);

    /**
     * returns the table_name used in i18n
     * @return the table_name used in i18n
     */
    public String getBundleName();
}
