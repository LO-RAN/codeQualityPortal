/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.toolbox.io;

import java.io.File;
import java.util.List;
import java.util.zip.ZipOutputStream;
import junit.framework.TestCase;

/**
 *
 * @author dt-lizac
 */
public class FileToolsTest extends TestCase {
    
    public FileToolsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of endsWithFileSeparator method, of class FileTools.
     */
    public void testEndsWithFileSeparator() {
        System.out.println("endsWithFileSeparator");
        String path = "/opt/someApp/";
        boolean expResult = true;
        boolean result = FileTools.endsWithFileSeparator(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of copy method, of class FileTools.
    public void testCopy_3args() throws Exception {
        System.out.println("copy");
        File src = null;
        File dest = null;
        boolean append = false;
        FileTools.copy(src, dest, append);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
 */
    
    /**
     * Test of copy method, of class FileTools.
    public void testCopy_File_File() throws Exception {
        System.out.println("copy");
        File src = null;
        File dest = null;
        FileTools.copy(src, dest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
 */
    
    /**
     * Test of rcopy method, of class FileTools.
    public void testRcopy_File_File() throws Exception {
        System.out.println("rcopy");
        File src = null;
        File dest = null;
        FileTools.rcopy(src, dest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of copy method, of class FileTools.
    public void testCopy_FileArr_File() throws Exception {
        System.out.println("copy");
        File[] list = null;
        File dest = null;
        FileTools.copy(list, dest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of rcopy method, of class FileTools.
    public void testRcopy_FileArr_File() throws Exception {
        System.out.println("rcopy");
        File[] list = null;
        File dest = null;
        FileTools.rcopy(list, dest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of concatPath method, of class FileTools.
     */
    public void testConcatPath() {
        System.out.println("concatPath");
        String pathBase = "/opt/someApp/";
        String pathRelative = "/SomeFolder/";
        String expResult = "/opt/someApp/SomeFolder/";
        String result = FileTools.concatPath(pathBase, pathRelative);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of zipDir method, of class FileTools.
    public void testZipDir_3args_1() {
        System.out.println("zipDir");
        File zipDir = null;
        ZipOutputStream zos = null;
        String rootPath = "";
        FileTools.zipDir(zipDir, zos, rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of zipDir method, of class FileTools.
    public void testZipDir_3args_2() throws Exception {
        System.out.println("zipDir");
        File zipDir = null;
        File zipFile = null;
        String rootPath = "";
        FileTools.zipDir(zipDir, zipFile, rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of zipFile method, of class FileTools.
    public void testZipFile_3args_1() throws Exception {
        System.out.println("zipFile");
        File srcFile = null;
        File zipFile = null;
        String rootPath = "";
        FileTools.zipFile(srcFile, zipFile, rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of zipFile method, of class FileTools.
    public void testZipFile_3args_2() throws Exception {
        System.out.println("zipFile");
        List srcFileList = null;
        File zipFile = null;
        String rootPath = "";
        FileTools.zipFile(srcFileList, zipFile, rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of zipFile method, of class FileTools.
    public void testZipFile_3args_3() throws Exception {
        System.out.println("zipFile");
        List srcFileList = null;
        File zipFile = null;
        boolean keepPath = false;
        FileTools.zipFile(srcFileList, zipFile, keepPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of zipDir method, of class FileTools.
    public void testZipDir_3args_3() throws Exception {
        System.out.println("zipDir");
        String zipDir = "";
        String zipFile = "";
        String rootPath = "";
        FileTools.zipDir(zipDir, zipFile, rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of unzipToDir method, of class FileTools.
    public void testUnzipToDir() throws Exception {
        System.out.println("unzipToDir");
        String destinationDir = "";
        File zipFile = null;
        FileTools.unzipToDir(destinationDir, zipFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of rdelete method, of class FileTools.
    public void testRdelete() throws Exception {
        System.out.println("rdelete");
        File src = null;
        FileTools.rdelete(src);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of insertFiles method, of class FileTools.
    public void testInsertFiles() throws Exception {
        System.out.println("insertFiles");
        File mainFile = null;
        List fileInsertList = null;
        FileTools.insertFiles(mainFile, fileInsertList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of getBytesFromFile method, of class FileTools.
    public void testGetBytesFromFile() throws Exception {
        System.out.println("getBytesFromFile");
        File file = null;
        byte[] expResult = null;
        byte[] result = FileTools.getBytesFromFile(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */
    
}
