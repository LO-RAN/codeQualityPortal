package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTmember_declarator.java */

public class ASTmember_declarator extends SimpleNode {
  public ASTmember_declarator(int id) {
    super(id);
  }

  public ASTmember_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
