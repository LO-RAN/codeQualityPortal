/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.carscode.plugin.deventreprise.dataschemas;

import java.io.File;

/**
 *
 * @author cwfr-dzysman
 */
public class CobolPackage {
    private String cobolPackage;
    private File   cobolPackageDirectory;

    public CobolPackage(String cobolPackage, String srcDir) {
        this.cobolPackage = cobolPackage;
    }

    public void setCobolPackage(String s) {
        this.cobolPackage = s;
    }

    public String getCobolPackage() {
        return this.cobolPackage;
    }

    public String getCobolPackagePath() {
        String s = this.getCobolPackage();
        s = s.replaceAll("\\.", "/");
        return s;
    }

    public File getCobolPackageDirectory() {
        return cobolPackageDirectory;
    }

    public void setCobolPackageDirectory(File cobolPackageDirectory) {
        this.cobolPackageDirectory = cobolPackageDirectory;
    }
}
