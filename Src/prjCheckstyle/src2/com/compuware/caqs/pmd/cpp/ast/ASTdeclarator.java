package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTdeclarator.java */

public class ASTdeclarator extends SimpleNode {
  public ASTdeclarator(int id) {
    super(id);
  }

  public ASTdeclarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
