package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTctor_declarator.java */

public class ASTctor_declarator extends SimpleNode {
  public ASTctor_declarator(int id) {
    super(id);
  }

  public ASTctor_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
