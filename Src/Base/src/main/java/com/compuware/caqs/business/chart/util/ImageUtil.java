/**
 * 
 */
package com.compuware.caqs.business.chart.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.filter.RegexpFilter;

/**
 * @author cwfr-fdubois
 *
 */
public class ImageUtil {

    private static ImageUtil singleton = new ImageUtil();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    private ImageUtil() {
    }

    public static ImageUtil getInstance() {
        return singleton;
    }

    public byte[] getImageBytes(File file) throws IOException {
        byte[] result = null;
        r.lock();
        try {
            if (file != null && file.exists()) {
                result = new byte[(int) file.length()];
                BufferedInputStream input = null;
                try {
                    input = new BufferedInputStream(new FileInputStream(file));
                    input.read(result);
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }
        } finally {
            r.unlock();
        }
        return result;
    }

    public File retrieveExistingImage(String idElt, String idBline, GraphImageConfig imgConfig) {
        File result = null;
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String caqsImageDirectory = dynProp.getProperty(Constants.CAQS_IMAGE_TMP_DIR_KEY);
        File dir = new File(caqsImageDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(caqsImageDirectory, "kiviat-" + idBline + '-'
                + imgConfig.getLocale().getLanguage() + '-' + idElt + '-'
                + imgConfig.getWidth() + '-' + imgConfig.getHeight() + ".png");
        return result;
    }

    public File retrieveExistingFavoriteKiviatImage(String idElt, String idBline, GraphImageConfig imgConfig) {
        File result = null;
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String caqsImageDirectory = dynProp.getProperty(Constants.CAQS_IMAGE_TMP_DIR_KEY);
        File dir = new File(caqsImageDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(caqsImageDirectory, "kiviat-favorite-" + idBline + '-'
                + imgConfig.getLocale().getLanguage() + '-' + idElt + '-'
                + imgConfig.getWidth() + '-' + imgConfig.getHeight() + ".png");
        return result;
    }

    public void clearKiviatImage(String idBline) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String caqsImageDirectory = dynProp.getProperty(Constants.CAQS_IMAGE_TMP_DIR_KEY);
        RegexpFilter fileFilter = new RegexpFilter();
        fileFilter.setAcceptDirectory(false);
        fileFilter.setRegexp("kiviat-(favorite-)?" + idBline + "-[a-z]+-"
                + "[0-9-]+\\.png");
        File dir = new File(caqsImageDirectory);
        if (dir.exists()) {
            w.lock();
            try {
                File[] fileList = dir.listFiles(fileFilter);
                if (fileList != null) {
                    File currentFile = null;
                    for (int i = 0; i < fileList.length; i++) {
                        currentFile = fileList[i];
                        currentFile.delete();
                    }
                }
            } finally {
                w.unlock();
            }
        }
    }

    public File retrieveExistingDashboardEvolutionImage(String idElt, String idBline, GraphImageConfig imgConfig) {
        File result = null;
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String caqsImageDirectory = dynProp.getProperty(Constants.CAQS_IMAGE_TMP_DIR_KEY);
        File dir = new File(caqsImageDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(caqsImageDirectory, "dashboard-evolution-" + idBline
                + '-' + imgConfig.getLocale().getLanguage() + '-' + idElt + '-'
                + imgConfig.getWidth() + '-' + imgConfig.getHeight() + ".png");
        return result;
    }
}
