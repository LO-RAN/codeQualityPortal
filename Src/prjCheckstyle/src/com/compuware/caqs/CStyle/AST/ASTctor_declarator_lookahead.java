package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTctor_declarator_lookahead.java */

public class ASTctor_declarator_lookahead extends SimpleNode {
  public ASTctor_declarator_lookahead(int id) {
    super(id);
  }

  public ASTctor_declarator_lookahead(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}