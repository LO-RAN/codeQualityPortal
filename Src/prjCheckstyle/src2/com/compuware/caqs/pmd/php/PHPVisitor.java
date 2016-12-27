package com.compuware.caqs.pmd.php;

/* Generated By:JJTree: Do not edit this line. D:\Prof\Data\tools\javacc-4.0\javacc-4.0\examples\PHPGrammar\PHPVisitor.java */

public interface PHPVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTPhpPage node, Object data);
  public Object visit(ASTHtmlBlock node, Object data);
  public Object visit(ASTStatement node, Object data);
  public Object visit(ASTThrowStatement node, Object data);
  public Object visit(ASTTryBlock node, Object data);
  public Object visit(ASTEndOfStatement node, Object data);
  public Object visit(ASTEmbeddedHtml node, Object data);
  public Object visit(ASTDefineStatement node, Object data);
  public Object visit(ASTLabeledStatement node, Object data);
  public Object visit(ASTExpressionStatement node, Object data);
  public Object visit(ASTCompoundStatement node, Object data);
  public Object visit(ASTSelectionStatement node, Object data);
  public Object visit(ASTIterationStatement node, Object data);
  public Object visit(ASTJumpStatement node, Object data);
  public Object visit(ASTParameterList node, Object data);
  public Object visit(ASTParameter node, Object data);
  public Object visit(ASTClassDeclaration node, Object data);
  public Object visit(ASTClassMembers node, Object data);
  public Object visit(ASTMemberDeclaration node, Object data);
  public Object visit(ASTInterfaceDeclaration node, Object data);
  public Object visit(ASTInterfaceMembers node, Object data);
  public Object visit(ASTInterfaceMember node, Object data);
  public Object visit(ASTIncludeStatement node, Object data);
  public Object visit(ASTEchoStatement node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTLogicalTextOrExpression node, Object data);
  public Object visit(ASTLogicalTextXorExpression node, Object data);
  public Object visit(ASTLogicalTextAndExpression node, Object data);
  public Object visit(ASTAssignmentExpression node, Object data);
  public Object visit(ASTAssignmentOperator node, Object data);
  public Object visit(ASTConditionalExpression node, Object data);
  public Object visit(ASTLogical_Or_Expression node, Object data);
  public Object visit(ASTLogical_And_Expression node, Object data);
  public Object visit(ASTBitwiseOrExpression node, Object data);
  public Object visit(ASTBitwiseXorExpression node, Object data);
  public Object visit(ASTBitwiseAndExpression node, Object data);
  public Object visit(ASTEqualityExpression node, Object data);
  public Object visit(ASTRelationalExpression node, Object data);
  public Object visit(ASTShiftExpression node, Object data);
  public Object visit(ASTAdditiveExpression node, Object data);
  public Object visit(ASTMultiplicativeExpression node, Object data);
  public Object visit(ASTCastExpression node, Object data);
  public Object visit(ASTUnaryExpression node, Object data);
  public Object visit(ASTPrefixIncDecExpression node, Object data);
  public Object visit(ASTPostfixIncDecExpression node, Object data);
  public Object visit(ASTInstanceOfExpression node, Object data);
  public Object visit(ASTPostfixExpression node, Object data);
  public Object visit(ASTPrimaryExpression node, Object data);
  public Object visit(ASTArray node, Object data);
  public Object visit(ASTClassInstantiation node, Object data);
  public Object visit(ASTVariable node, Object data);
  public Object visit(ASTArgumentExpressionList node, Object data);
  public Object visit(ASTConstant node, Object data);
  public Object visit(ASTString node, Object data);
  public Object visit(ASTDoubleStringLiteral node, Object data);
  public Object visit(ASTVisibility node, Object data);
}