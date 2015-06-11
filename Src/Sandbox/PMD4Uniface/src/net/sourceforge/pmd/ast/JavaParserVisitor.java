/* Generated By:JavaCC: Do not edit this line. JavaParserVisitor.java Version 4.1 */
package net.sourceforge.pmd.ast;

public interface JavaParserVisitor
{
  public Object visit(SimpleJavaNode node, Object data);
  public Object visit(ASTCompilationUnit node, Object data);
  public Object visit(ASTPackageDeclaration node, Object data);
  public Object visit(ASTImportDeclaration node, Object data);
  public Object visit(ASTTypeDeclaration node, Object data);
  public Object visit(ASTClassOrInterfaceDeclaration node, Object data);
  public Object visit(ASTExtendsList node, Object data);
  public Object visit(ASTImplementsList node, Object data);
  public Object visit(ASTEnumDeclaration node, Object data);
  public Object visit(ASTEnumBody node, Object data);
  public Object visit(ASTEnumConstant node, Object data);
  public Object visit(ASTTypeParameters node, Object data);
  public Object visit(ASTTypeParameter node, Object data);
  public Object visit(ASTTypeBound node, Object data);
  public Object visit(ASTClassOrInterfaceBody node, Object data);
  public Object visit(ASTClassOrInterfaceBodyDeclaration node, Object data);
  public Object visit(ASTFieldDeclaration node, Object data);
  public Object visit(ASTVariableDeclarator node, Object data);
  public Object visit(ASTVariableDeclaratorId node, Object data);
  public Object visit(ASTVariableInitializer node, Object data);
  public Object visit(ASTArrayInitializer node, Object data);
  public Object visit(ASTMethodDeclaration node, Object data);
  public Object visit(ASTMethodDeclarator node, Object data);
  public Object visit(ASTFormalParameters node, Object data);
  public Object visit(ASTFormalParameter node, Object data);
  public Object visit(ASTConstructorDeclaration node, Object data);
  public Object visit(ASTExplicitConstructorInvocation node, Object data);
  public Object visit(ASTInitializer node, Object data);
  public Object visit(ASTType node, Object data);
  public Object visit(ASTReferenceType node, Object data);
  public Object visit(ASTClassOrInterfaceType node, Object data);
  public Object visit(ASTTypeArguments node, Object data);
  public Object visit(ASTTypeArgument node, Object data);
  public Object visit(ASTWildcardBounds node, Object data);
  public Object visit(ASTPrimitiveType node, Object data);
  public Object visit(ASTResultType node, Object data);
  public Object visit(ASTName node, Object data);
  public Object visit(ASTNameList node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTAssignmentOperator node, Object data);
  public Object visit(ASTConditionalExpression node, Object data);
  public Object visit(ASTConditionalOrExpression node, Object data);
  public Object visit(ASTConditionalAndExpression node, Object data);
  public Object visit(ASTInclusiveOrExpression node, Object data);
  public Object visit(ASTExclusiveOrExpression node, Object data);
  public Object visit(ASTAndExpression node, Object data);
  public Object visit(ASTEqualityExpression node, Object data);
  public Object visit(ASTInstanceOfExpression node, Object data);
  public Object visit(ASTRelationalExpression node, Object data);
  public Object visit(ASTShiftExpression node, Object data);
  public Object visit(ASTAdditiveExpression node, Object data);
  public Object visit(ASTMultiplicativeExpression node, Object data);
  public Object visit(ASTUnaryExpression node, Object data);
  public Object visit(ASTPreIncrementExpression node, Object data);
  public Object visit(ASTPreDecrementExpression node, Object data);
  public Object visit(ASTUnaryExpressionNotPlusMinus node, Object data);
  public Object visit(ASTPostfixExpression node, Object data);
  public Object visit(ASTCastExpression node, Object data);
  public Object visit(ASTPrimaryExpression node, Object data);
  public Object visit(ASTMemberSelector node, Object data);
  public Object visit(ASTPrimaryPrefix node, Object data);
  public Object visit(ASTPrimarySuffix node, Object data);
  public Object visit(ASTLiteral node, Object data);
  public Object visit(ASTBooleanLiteral node, Object data);
  public Object visit(ASTNullLiteral node, Object data);
  public Object visit(ASTArguments node, Object data);
  public Object visit(ASTArgumentList node, Object data);
  public Object visit(ASTAllocationExpression node, Object data);
  public Object visit(ASTArrayDimsAndInits node, Object data);
  public Object visit(ASTStatement node, Object data);
  public Object visit(ASTLabeledStatement node, Object data);
  public Object visit(ASTBlock node, Object data);
  public Object visit(ASTBlockStatement node, Object data);
  public Object visit(ASTLocalVariableDeclaration node, Object data);
  public Object visit(ASTEmptyStatement node, Object data);
  public Object visit(ASTStatementExpression node, Object data);
  public Object visit(ASTSwitchStatement node, Object data);
  public Object visit(ASTSwitchLabel node, Object data);
  public Object visit(ASTIfStatement node, Object data);
  public Object visit(ASTWhileStatement node, Object data);
  public Object visit(ASTDoStatement node, Object data);
  public Object visit(ASTForStatement node, Object data);
  public Object visit(ASTForInit node, Object data);
  public Object visit(ASTStatementExpressionList node, Object data);
  public Object visit(ASTForUpdate node, Object data);
  public Object visit(ASTBreakStatement node, Object data);
  public Object visit(ASTContinueStatement node, Object data);
  public Object visit(ASTReturnStatement node, Object data);
  public Object visit(ASTThrowStatement node, Object data);
  public Object visit(ASTSynchronizedStatement node, Object data);
  public Object visit(ASTTryStatement node, Object data);
  public Object visit(ASTCatchStatement node, Object data);
  public Object visit(ASTFinallyStatement node, Object data);
  public Object visit(ASTAssertStatement node, Object data);
  public Object visit(ASTRUNSIGNEDSHIFT node, Object data);
  public Object visit(ASTRSIGNEDSHIFT node, Object data);
  public Object visit(ASTAnnotation node, Object data);
  public Object visit(ASTNormalAnnotation node, Object data);
  public Object visit(ASTMarkerAnnotation node, Object data);
  public Object visit(ASTSingleMemberAnnotation node, Object data);
  public Object visit(ASTMemberValuePairs node, Object data);
  public Object visit(ASTMemberValuePair node, Object data);
  public Object visit(ASTMemberValue node, Object data);
  public Object visit(ASTMemberValueArrayInitializer node, Object data);
  public Object visit(ASTAnnotationTypeDeclaration node, Object data);
  public Object visit(ASTAnnotationTypeBody node, Object data);
  public Object visit(ASTAnnotationTypeMemberDeclaration node, Object data);
  public Object visit(ASTAnnotationMethodDeclaration node, Object data);
  public Object visit(ASTDefaultValue node, Object data);
}
/* JavaCC - OriginalChecksum=6f9222a800daef876471227b5e8c50ac (do not edit this line) */
