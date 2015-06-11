/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import java.util.Map;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.PropertyDescriptor;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.properties.IntegerProperty;

public class AvoidDeeplyNestedIfStmtsRule extends AbstractRule {

    private int depth;
    private int depthLimit;
    
    private static final PropertyDescriptor problemDepthDescriptor = new IntegerProperty(
    		"problemDepth", 
    		"Maximum allowable statement depth",
    		0,
    		1.0f
    		);
    
    private static final Map propertyDescriptorsByName = asFixedMap(problemDepthDescriptor);
        
    public Object visit(ASTCompilationUnit node, Object data) {
        depth = 0;
        depthLimit = getIntProperty(problemDepthDescriptor);
        return super.visit(node, data);
    }

    public Object visit(ASTIfStatement node, Object data) {
        if (!node.hasElse()) {
            depth++;
        }
        super.visit(node, data);
        if (depth == depthLimit) {
            addViolation(data, node);
        }
        depth--;
        return data;
    }

    /**
     * @return Map
     */
    protected Map propertiesByName() {
    	return propertyDescriptorsByName;
    }
}
