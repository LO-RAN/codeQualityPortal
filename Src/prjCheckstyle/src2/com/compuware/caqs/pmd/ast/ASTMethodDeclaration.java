/* Generated By:JJTree: Do not edit this line. ASTMethodDeclaration.java */

package com.compuware.caqs.pmd.ast;

public class ASTMethodDeclaration extends AccessNode {
    public ASTMethodDeclaration(int id) {
        super(id);
    }

    public ASTMethodDeclaration(JavaParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public void dump(String prefix) {
        System.out.println(collectDumpedModifiers(prefix));
        dumpChildren(prefix);
    }

    /**
     * Gets the name of the method.
     *
     * @return a String representing the name of the method
     */
    public String getMethodName() {
        ASTMethodDeclarator md = (ASTMethodDeclarator) getFirstChildOfType(ASTMethodDeclarator.class);
        if (md != null)
            return md.getImage();
        return null;
    }

    public boolean isSyntacticallyPublic() {
        return super.isPublic();
    }

    public boolean isSyntacticallyAbstract() {
        return super.isAbstract();
    }

    public boolean isPublic() {
        if (isInterfaceMember()) {
            return true;
        }
        return super.isPublic();
    }

    public boolean isAbstract() {
        if (isInterfaceMember()) {
            return true;
        }
        return super.isAbstract();
    }

    public boolean isInterfaceMember() {
        ASTClassOrInterfaceDeclaration clz = (ASTClassOrInterfaceDeclaration) getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        return clz != null && clz.isInterface();
    }

    public boolean isVoid() {
        return ((ASTResultType) getFirstChildOfType(ASTResultType.class)).isVoid();
    }

    public ASTResultType getResultType() {
        return (ASTResultType) getFirstChildOfType(ASTResultType.class);
    }

    public ASTBlock getBlock() {
        // FIXME doesn't work for all methods that use generics and declare exceptions
        if (this.jjtGetChild(2) instanceof ASTBlock) {
            return (ASTBlock) this.jjtGetChild(2);
        }
        if (jjtGetNumChildren() > 3) {
            if (this.jjtGetChild(3) instanceof ASTBlock) {
                return (ASTBlock) this.jjtGetChild(3);
            }
        }
        return null;
    }
}
