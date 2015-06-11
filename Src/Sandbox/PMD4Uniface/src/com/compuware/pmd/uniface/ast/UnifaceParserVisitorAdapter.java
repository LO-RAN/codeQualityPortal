
package com.compuware.pmd.uniface.ast;

/**
 *
 * @author cwfr-lizac
 */
class UnifaceParserVisitorAdapter  implements UnifaceParserVisitor{

    public UnifaceParserVisitorAdapter() {
    }

    public Object visit(SimpleNode node, Object data) {
        node.childrenAccept(this, data);
        return null;
    }

    public Object visit(ASTCompilationUnit node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTComment node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTCommentLine node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTInstruction node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTProcStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTIndirection node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTCompute node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTAssignment node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTFunction node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTIfStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTWhileStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTRepeatStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTSelectCaseStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTLabelStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTStatement node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDeleteinstance node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTNewinstance node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTSetformfocus node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTEntitycopy node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTAddmonths node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTCompare node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDisplaylength node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTLength node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTLowercase node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTReset node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTScan node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTSet node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTStripattributes node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTUppercase node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTClose node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTCommit node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTOpen node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTRollback node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTSql node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTGoto node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTArgument node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTCompileTimeConstant node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTConstant node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTExpressionList node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTLogicalExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTRelationalExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTAdditiveExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTMultiplicativeExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTUnaryExpression node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTUserDefinedFunction node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTString node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTIdentifier node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTRegister node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTGlobalVariable node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTComponentVariable node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTIdentifierList node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTInteger node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDatatype node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDirection node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTVariableDefinition node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTParameter node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTParameterBlock node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTVariableBlock node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTActivate node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDisplay node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTClrmess node, Object data) {
         return visit((SimpleNode) node, data);
    }

    public Object visit(ASTDebug node, Object data) {
         return visit((SimpleNode) node, data);
    }

}
