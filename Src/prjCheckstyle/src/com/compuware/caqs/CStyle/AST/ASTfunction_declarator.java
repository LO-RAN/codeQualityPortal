package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTfunction_declarator.java */

public class ASTfunction_declarator extends SimpleNode {
  public ASTfunction_declarator(int id) {
    super(id);
  }

  public ASTfunction_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}