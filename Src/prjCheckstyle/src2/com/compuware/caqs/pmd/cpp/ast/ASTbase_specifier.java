package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTbase_specifier.java */

public class ASTbase_specifier extends SimpleNode {
  public ASTbase_specifier(int id) {
    super(id);
  }

  public ASTbase_specifier(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}