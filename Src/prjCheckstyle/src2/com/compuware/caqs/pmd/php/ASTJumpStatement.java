package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTJumpStatement.java */

public class ASTJumpStatement extends SimpleNode {
  public ASTJumpStatement(int id) {
    super(id);
  }

  public ASTJumpStatement(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}