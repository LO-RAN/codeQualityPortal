package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTArgumentExpressionList.java */

public class ASTArgumentExpressionList extends SimpleNode {
  public ASTArgumentExpressionList(int id) {
    super(id);
  }

  public ASTArgumentExpressionList(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
