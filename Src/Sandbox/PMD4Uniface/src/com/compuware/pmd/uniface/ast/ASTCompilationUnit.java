/* Generated By:JJTree: Do not edit this line. ASTCompilationUnit.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package com.compuware.pmd.uniface.ast;

import net.sourceforge.pmd.ast.CompilationUnit;

public class ASTCompilationUnit extends SimpleNode  implements CompilationUnit{
  public ASTCompilationUnit(int id) {
    super(id);
  }

  public ASTCompilationUnit(UnifaceParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(UnifaceParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=eac262b02d7467e03677880a781f0a12 (do not edit this line) */
