package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTEchoStatement.java */

public class ASTEchoStatement extends SimpleNode {
  public ASTEchoStatement(int id) {
    super(id);
  }

  public ASTEchoStatement(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
