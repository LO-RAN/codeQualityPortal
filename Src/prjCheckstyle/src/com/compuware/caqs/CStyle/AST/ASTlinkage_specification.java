package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTlinkage_specification.java */

public class ASTlinkage_specification extends SimpleNode {
  public ASTlinkage_specification(int id) {
    super(id);
  }

  public ASTlinkage_specification(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
