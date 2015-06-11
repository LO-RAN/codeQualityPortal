/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 24 mai 2005
 * Time: 17:23:18
 * To change this template use File | Settings | File Templates.
 */
package com.compuware.caqs.dao.dbms;

// Usefull java libraries.
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.jcs.JCS;
import org.apache.jcs.engine.control.CompositeCacheManager;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.util.logging.LoggerManager;

public class DataAccessCache {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_CACHE_LOGGER_KEY);
    private static HashMap projectBaselineMap = new HashMap();
    private static DataAccessCache ourInstance = new DataAccessCache();

    public static DataAccessCache getInstance() {
        return ourInstance;
    }

    private DataAccessCache() {
        initCache();
    }
    private static JCS resultMap;

    private static void initCache() {
        try {
            String configDir = CaqsConfigUtil.getCaqsConfigDir();
            mLog.debug("Cache config file: "
                    + FileTools.concatPath(configDir, Constants.CAQS_CACHE_CONFIG_FILE_NAME));
            CompositeCacheManager cacheManager = CompositeCacheManager.getUnconfiguredInstance();
            cacheManager.configure(PropertiesReader.getProperties(FileTools.concatPath(configDir, Constants.CAQS_CACHE_CONFIG_FILE_NAME), null));
            resultMap = JCS.getInstance("caqsCache");
        } catch (org.apache.jcs.access.exception.CacheException e) {
            mLog.error("Erreur de lecture du cache", e);
        } catch (Exception e) {
            mLog.error("Erreur d'acc�s au cache", e);
        }
    }

    public void clearCache() {
        try {
            synchronized (resultMap) {
                resultMap.clear();
            }
            synchronized (projectBaselineMap) {
                projectBaselineMap.clear();
            }
        } catch (org.apache.jcs.access.exception.CacheException e) {
            mLog.error("Erreur de lecture du cache", e);
        }
    }

    public void clearCache(String partId) {
        try {
            Collection tmp = null;
            synchronized (projectBaselineMap) {
                tmp = (Collection) projectBaselineMap.get(partId);
            }
            if (tmp != null) {
                synchronized (resultMap) {
                    Iterator i = tmp.iterator();
                    while (i.hasNext()) {
                        String key = (String) i.next();
                        resultMap.remove(key);
                    }
                }
            }
        } catch (org.apache.jcs.access.exception.CacheException e) {
            mLog.error("Erreur de lecture du cache", e);
        }
    }

    public Object loadFromCache(String key) {
        synchronized (resultMap) {
            logStatistics();
            return resultMap.get(key);
        }
    }

    public void storeToCache(String partId, String key, Object obj) {
        try {
            synchronized (resultMap) {
                resultMap.put(key, obj);
            }
            storeToProjectBaselineMap(partId, key);
        } catch (org.apache.jcs.access.exception.CacheException e) {
            mLog.error("Erreur de lecture du cache", e);
        }
    }

    private void storeToProjectBaselineMap(String partId, String key) {
        synchronized (resultMap) {
            Collection tmp = (Collection) projectBaselineMap.get(partId);
            if (tmp == null) {
                tmp = new ArrayList();
                projectBaselineMap.put(partId, tmp);
            }
            tmp.add(key);
        }
    }

    private void logStatistics() {
        mLog.debug(resultMap.getStats());
    }
}
