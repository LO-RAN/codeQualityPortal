package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTPrefixIncDecExpression.java */

public class ASTPrefixIncDecExpression extends SimpleNode {
  public ASTPrefixIncDecExpression(int id) {
    super(id);
  }

  public ASTPrefixIncDecExpression(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}