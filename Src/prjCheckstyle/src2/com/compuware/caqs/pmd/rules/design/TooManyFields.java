/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules.design;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.PropertyDescriptor;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTFieldDeclaration;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.properties.IntegerProperty;
import com.compuware.caqs.pmd.util.NumericConstants;


public class TooManyFields extends AbstractRule {

    private static final int DEFAULT_MAXFIELDS = 15;

    private Map stats;
    private Map nodes;
    private int maxFields;

    private static final PropertyDescriptor maxFieldsDescriptor = new IntegerProperty(
    		"maxfields", 
    		"Maximum allowable fields per class",
    		DEFAULT_MAXFIELDS,
    		1.0f
    		);
    
    private static final Map propertyDescriptorsByName = asFixedMap(maxFieldsDescriptor);
    
    public Object visit(ASTCompilationUnit node, Object data) {
    	
        maxFields = getIntProperty(maxFieldsDescriptor);

        stats = new HashMap(5);
        nodes = new HashMap(5);

        List l = node.findChildrenOfType(ASTFieldDeclaration.class);

        for (Iterator it = l.iterator(); it.hasNext();) {
            ASTFieldDeclaration fd = (ASTFieldDeclaration) it.next();
            if (fd.isFinal() && fd.isStatic()) {
                continue;
            }
            ASTClassOrInterfaceDeclaration clazz = (ASTClassOrInterfaceDeclaration) fd.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
            if (clazz != null && !clazz.isInterface()) {
                bumpCounterFor(clazz);
            }
        }
        for (Iterator it = stats.keySet().iterator(); it.hasNext();) {
            String k = (String) it.next();
            int val = ((Integer) stats.get(k)).intValue();
            SimpleNode n = (SimpleNode) nodes.get(k);
            if (val > maxFields) {
                addViolation(data, n);
            }
        }
        return data;
    }

    private void bumpCounterFor(ASTClassOrInterfaceDeclaration clazz) {
        String key = clazz.getImage();
        if (!stats.containsKey(key)) {
            stats.put(key, NumericConstants.ZERO);
            nodes.put(key, clazz);
        }
        Integer i = new Integer(((Integer) stats.get(key)).intValue() + 1);
        stats.put(key, i);
    }

    /**
     * @return Map
     */
    protected Map propertiesByName() {
    	return propertyDescriptorsByName;
    }
}
