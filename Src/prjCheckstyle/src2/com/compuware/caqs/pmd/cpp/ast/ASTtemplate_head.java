package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTtemplate_head.java */

public class ASTtemplate_head extends SimpleNode {
  public ASTtemplate_head(int id) {
    super(id);
  }

  public ASTtemplate_head(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
