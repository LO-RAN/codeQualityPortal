package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTdtor_definition.java */

public class ASTdtor_definition extends SimpleNode {
  public ASTdtor_definition(int id) {
    super(id);
  }

  public ASTdtor_definition(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
