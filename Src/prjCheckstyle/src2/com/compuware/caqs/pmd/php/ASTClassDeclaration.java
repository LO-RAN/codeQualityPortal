package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTClassDeclaration.java */

public class ASTClassDeclaration extends SimpleNode {
  public ASTClassDeclaration(int id) {
    super(id);
  }

  public ASTClassDeclaration(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
