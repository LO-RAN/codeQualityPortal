package com.compuware.caqs.pmd.cpp.ast;
/* Generated By:JJTree: Do not edit this line. ASTexception_list.java */

public class ASTexception_list extends SimpleNode {
  public ASTexception_list(int id) {
    super(id);
  }

  public ASTexception_list(CPPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(CPPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
