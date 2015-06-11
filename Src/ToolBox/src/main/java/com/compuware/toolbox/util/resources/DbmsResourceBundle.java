/**
 * 
 */
package com.compuware.toolbox.util.resources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author cwfr-fdubois
 *
 */
public class DbmsResourceBundle extends ResourceBundle {

    private Properties resources;

    public DbmsResourceBundle(Properties prop) {
        this.resources = prop;
    }

    protected Object handleGetObject(String key) {
        return resources.get(key);
    }

    public String getStringNoDefault(String key) {
        return (String) resources.get(key);
    }

    protected ResourceBundle getParent() {
        return this.parent;
    }

    protected void setParent(ResourceBundle p) {
        this.parent = p;
    }

    public void setString(String key, String value) {
        if(value==null || value.length()<1) {
            this.resources.remove(key);
        } else {
            this.resources.put(key, value);
        }
    }

    public Enumeration getKeys() {
        Enumeration result = null;

        if (parent == null) {
            result = resources.propertyNames();
        } else {
            // We make a new Set that holds all the keys, then return an enumeration
            // for that. This prevents modifications from ruining the enumeration,
            // as well as ignoring duplicates.
            Set s = new HashSet();
            Enumeration e = resources.propertyNames();
            while (e.hasMoreElements()) {
                s.add(e.nextElement());
            }
            DbmsResourceBundle bundle = (DbmsResourceBundle) parent;
            // Eliminate tail recursion.
            do {
                e = bundle.getKeys();
                while (e.hasMoreElements()) {
                    s.add(e.nextElement());
                }
                bundle = (DbmsResourceBundle) bundle.getParent();
            } while (bundle != null);

            result = Collections.enumeration(s);
        }
        return result;
    }
    private static Map bundleCache = new HashMap();

    public static void init(String bundleName, Locale loc, ResultSet rs, List keyColumnList, String valueColumnName) throws SQLException {
        Properties prop = new Properties();
        BundleKey key = new BundleKey(bundleName, loc);
        while (rs.next()) {
            String propKey = getPropertyKey(rs, keyColumnList);
            String value = rs.getString(valueColumnName);
            prop.put(propKey, value);
        }
        initBundle(key, prop);
    }

    private static String getPropertyKey(ResultSet rs, List keyColumnList) throws SQLException {
        StringBuffer result = new StringBuffer();
        if (keyColumnList != null) {
            Iterator i = keyColumnList.iterator();
            while (i.hasNext()) {
                String columnName = (String) i.next();
                result.append(rs.getString(columnName)).append(".");
            }
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    private static synchronized void initBundle(BundleKey key, Properties newResources) {
        bundleCache.remove(key);
        DbmsResourceBundle bundle = new DbmsResourceBundle(newResources);
        bundleCache.put(key, bundle);
        if (!key.locale.equals(Locale.getDefault())) {
            BundleKey defkey = new BundleKey(key.bundleName, Locale.getDefault());
            bundle.setParent((ResourceBundle) bundleCache.get(defkey));
        }
    }

    public static synchronized ResourceBundle getResourceBundle(String bundleName, Locale loc) {
        BundleKey key = new BundleKey(bundleName, loc);
        ResourceBundle result = (ResourceBundle) bundleCache.get(key);
        if (result == null) {
            key = new BundleKey(bundleName, Locale.getDefault());
            result = (ResourceBundle) bundleCache.get(key);
        }
        return result;
    }

    public static String getString(Internationalizable obj, String bundleName, String columnName, Locale loc, String defaultValue) {
        String result = "";
        try {
            DbmsResourceBundle messages = (DbmsResourceBundle) DbmsResourceBundle.getResourceBundle(bundleName, loc);
            String key = obj.getTextKey(columnName);
            result = messages.getString(key);
        } catch (MissingResourceException e) {
            // Aucune donnée n'est présente, on retourne la valeur par défaut.
            result = defaultValue;
        }
        return result;
    }

    public static void setString(Internationalizable obj, String columnName, Locale loc, String text) {
        DbmsResourceBundle messages = (DbmsResourceBundle) DbmsResourceBundle.getResourceBundle(obj.getBundleName(), loc);
        String key = obj.getTextKey(columnName);
        messages.setString(key, text);
    }

    public static String getString(Internationalizable obj, String bundleName, String columnName, Locale loc) {
        return getString(obj, bundleName, columnName, loc, obj.getId());
    }

    public static String getString(Internationalizable obj, String bundleName, String columnName, List args, Locale loc) {
        String s = getString(obj, bundleName, columnName, loc, obj.getId());
        s = MessageFormat.format(s, args.toArray());
        return s;
    }

    private static class BundleKey {

        String bundleName;
        Locale locale;
        int hashcode;

        BundleKey() {
        }

        BundleKey(String s, Locale l) {
            set(s, l);
        }

        void set(String s, Locale l) {
            bundleName = s;
            locale = l;
            hashcode = bundleName.hashCode();
            if (locale != null) {
                hashcode = hashcode ^ locale.getLanguage().hashCode();
            }
        }

        public int hashCode() {
            return hashcode;
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (o instanceof BundleKey) {
                BundleKey key = (BundleKey) o;
                result = hashcode == key.hashcode &&
                        bundleName.equals(key.bundleName);
                if (result) {
                    if (locale != null &&
                            (key.locale == null ||
                            !locale.getLanguage().equals(key.locale.getLanguage()))) {
                        result = false;
                    } else if (locale == null && key.locale != null) {
                        result = false;
                    }
                }
            }
            return result;
        }
    }
}
