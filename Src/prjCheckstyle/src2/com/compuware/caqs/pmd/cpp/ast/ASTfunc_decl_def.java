package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTfunc_decl_def.java */

public class ASTfunc_decl_def extends SimpleNode {
  public ASTfunc_decl_def(int id) {
    super(id);
  }

  public ASTfunc_decl_def(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
