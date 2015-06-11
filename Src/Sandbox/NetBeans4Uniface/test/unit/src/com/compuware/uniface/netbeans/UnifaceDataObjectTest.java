/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.uniface.netbeans;

import junit.framework.TestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;

public class UnifaceDataObjectTest extends TestCase {

    public UnifaceDataObjectTest(String testName) {
        super(testName);
    }

    public void testDataObject() throws Exception {
        FileObject root = Repository.getDefault().getDefaultFileSystem().getRoot();
        FileObject template = root.getFileObject("Templates/Other/UnifaceTemplate.uniface");
        assertNotNull("Template file shall be found", template);

        DataObject obj = DataObject.find(template);
        assertEquals("It is our data object", UnifaceDataObject.class, obj.getClass());
    }
}
