package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTInterfaceDeclaration.java */

public class ASTInterfaceDeclaration extends SimpleNode {
  public ASTInterfaceDeclaration(int id) {
    super(id);
  }

  public ASTInterfaceDeclaration(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}