package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTunary_operator.java */

public class ASTunary_operator extends SimpleNode {
  public ASTunary_operator(int id) {
    super(id);
  }

  public ASTunary_operator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}