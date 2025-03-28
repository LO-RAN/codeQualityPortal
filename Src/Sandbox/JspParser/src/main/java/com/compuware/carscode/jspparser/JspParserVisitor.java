/* Generated By:JJTree: Do not edit this line. /home/tom/pmd/pmd/src/net/sourceforge/pmd/jsp/ast/JspParserVisitor.java */

package com.compuware.carscode.jspparser;

public interface JspParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTCompilationUnit node, Object data);
  public Object visit(ASTContent node, Object data);
  public Object visit(ASTJspDirective node, Object data);
  public Object visit(ASTJspDirectiveAttribute node, Object data);
  public Object visit(ASTJspScriptlet node, Object data);
  public Object visit(ASTJspExpression node, Object data);
  public Object visit(ASTJspDeclaration node, Object data);
  public Object visit(ASTJspComment node, Object data);
  public Object visit(ASTText node, Object data);
  public Object visit(ASTUnparsedText node, Object data);
  public Object visit(ASTElExpression node, Object data);
  public Object visit(ASTValueBinding node, Object data);
  public Object visit(ASTCData node, Object data);
  public Object visit(ASTElement node, Object data);
  public Object visit(ASTAttribute node, Object data);
  public Object visit(ASTAttributeValue node, Object data);
  public Object visit(ASTJspExpressionInAttribute node, Object data);
  public Object visit(ASTCommentTag node, Object data);
  public Object visit(ASTDeclaration node, Object data);
  public Object visit(ASTDoctypeDeclaration node, Object data);
  public Object visit(ASTDoctypeExternalId node, Object data);
}
