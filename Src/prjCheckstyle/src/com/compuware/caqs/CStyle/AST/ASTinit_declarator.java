package com.compuware.caqs.CStyle.AST;
/* Generated By:JJTree: Do not edit this line. ASTinit_declarator.java */

public class ASTinit_declarator extends SimpleNode {
  public ASTinit_declarator(int id) {
    super(id);
  }

  public ASTinit_declarator(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
