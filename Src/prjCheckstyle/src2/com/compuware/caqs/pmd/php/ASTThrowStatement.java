package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTThrowStatement.java */

public class ASTThrowStatement extends SimpleNode {
  public ASTThrowStatement(int id) {
    super(id);
  }

  public ASTThrowStatement(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}