package com.compuware.caqs.pmd.php;

/* Generated By:JJTree: Do not edit this line. ASTVisibility.java */

public class ASTVisibility extends SimpleNode {
  public ASTVisibility(int id) {
    super(id);
  }

  public ASTVisibility(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}