package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTparameter_declaration.java */

public class ASTparameter_declaration extends SimpleNode {
  public ASTparameter_declaration(int id) {
    super(id);
  }

  public ASTparameter_declaration(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
