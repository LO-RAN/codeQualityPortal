package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTfunction_direct_declarator.java */

public class ASTfunction_direct_declarator extends SimpleNode {
  public ASTfunction_direct_declarator(int id) {
    super(id);
  }

  public ASTfunction_direct_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}