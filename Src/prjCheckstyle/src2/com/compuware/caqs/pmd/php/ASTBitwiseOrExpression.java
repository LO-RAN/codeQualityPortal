package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTBitwiseOrExpression.java */

public class ASTBitwiseOrExpression extends SimpleNode {
  public ASTBitwiseOrExpression(int id) {
    super(id);
  }

  public ASTBitwiseOrExpression(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
