package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTdeclaration.java */

public class ASTdeclaration extends SimpleNode {
  public ASTdeclaration(int id) {
    super(id);
  }

  public ASTdeclaration(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
