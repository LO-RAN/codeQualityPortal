package com.compuware.caqs.pmd.php;
/* Generated By:JJTree: Do not edit this line. ASTTryBlock.java */

public class ASTTryBlock extends SimpleNode {
  public ASTTryBlock(int id) {
    super(id);
  }

  public ASTTryBlock(PHPparser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(PHPVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
