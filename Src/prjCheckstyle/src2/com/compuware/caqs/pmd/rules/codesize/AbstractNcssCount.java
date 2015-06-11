package com.compuware.caqs.pmd.rules.codesize;

import com.compuware.caqs.pmd.ast.ASTBreakStatement;
import com.compuware.caqs.pmd.ast.ASTCatchStatement;
import com.compuware.caqs.pmd.ast.ASTContinueStatement;
import com.compuware.caqs.pmd.ast.ASTDoStatement;
import com.compuware.caqs.pmd.ast.ASTFinallyStatement;
import com.compuware.caqs.pmd.ast.ASTForInit;
import com.compuware.caqs.pmd.ast.ASTForStatement;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.ast.ASTLabeledStatement;
import com.compuware.caqs.pmd.ast.ASTLocalVariableDeclaration;
import com.compuware.caqs.pmd.ast.ASTReturnStatement;
import com.compuware.caqs.pmd.ast.ASTStatementExpression;
import com.compuware.caqs.pmd.ast.ASTStatementExpressionList;
import com.compuware.caqs.pmd.ast.ASTSwitchLabel;
import com.compuware.caqs.pmd.ast.ASTSwitchStatement;
import com.compuware.caqs.pmd.ast.ASTSynchronizedStatement;
import com.compuware.caqs.pmd.ast.ASTThrowStatement;
import com.compuware.caqs.pmd.ast.ASTWhileStatement;
import com.compuware.caqs.pmd.ast.SimpleJavaNode;
import com.compuware.caqs.pmd.stat.DataPoint;
import com.compuware.caqs.pmd.stat.StatisticalRule;
import com.compuware.caqs.pmd.util.NumericConstants;

/**
 * Abstract superclass for NCSS counting methods. Counts tokens according to <a
 * href="http://www.kclee.de/clemens/java/javancss/">JavaNCSS rules</a>.
 * 
 * @author Jason Bennett
 */
public abstract class AbstractNcssCount extends StatisticalRule {

  private Class nodeClass;

  /**
   * Count the nodes of the given type using NCSS rules.
   * 
   * @param nodeClass
   *          class of node to count
   */
  protected AbstractNcssCount(Class nodeClass) {
    this.nodeClass = nodeClass;
  }

  public Object visit(SimpleJavaNode node, Object data) {
    int numNodes = 0;

    for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
      SimpleJavaNode simpleNode = (SimpleJavaNode) node.jjtGetChild( i );
      Integer treeSize = (Integer) simpleNode.jjtAccept( this, data );
      numNodes += treeSize.intValue();
    }

    if ( this.nodeClass.isInstance( node ) ) {
      // Add 1 to account for base node
      numNodes++;
      DataPoint point = new DataPoint();
      point.setNode( node );
      point.setScore( 1.0 * numNodes );
      point.setMessage( getMessage() );
      addDataPoint( point );
    }

    return new Integer( numNodes );
  }

  /**
   * Count the number of children of the given Java node. Adds one to count the
   * node itself.
   * 
   * @param node
   *          java node having children counted
   * @param data
   *          node data
   * @return count of the number of children of the node, plus one
   */
  protected Integer countNodeChildren(SimpleJavaNode node, Object data) {
    Integer nodeCount = null;
    int lineCount = 0;
    for ( int i = 0; i < node.jjtGetNumChildren(); i++ ) {
      nodeCount = (Integer) ( (SimpleJavaNode) node.jjtGetChild( i ) ).jjtAccept(
          this, data );
      lineCount += nodeCount.intValue();
    }
    return new Integer( ++lineCount );
  }

  public Object visit(ASTForStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTDoStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTIfStatement node, Object data) {

    Integer lineCount = countNodeChildren( node, data );

    if ( node.hasElse() ) {
      int lines = lineCount.intValue();
      lines++;
      lineCount = new Integer( lines );
    }

    return lineCount;
  }

  public Object visit(ASTWhileStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTBreakStatement node, Object data) {
    return NumericConstants.ONE;
  }

  public Object visit(ASTCatchStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTContinueStatement node, Object data) {
    return NumericConstants.ONE;
  }

  public Object visit(ASTFinallyStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTReturnStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTSwitchStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTSynchronizedStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTThrowStatement node, Object data) {
    return NumericConstants.ONE;
  }

  public Object visit(ASTStatementExpression node, Object data) {

    // "For" update expressions do not count as separate lines of code
    if ( node.jjtGetParent() instanceof ASTStatementExpressionList ) {
      return NumericConstants.ZERO;
    }

    return NumericConstants.ONE;
  }

  public Object visit(ASTLabeledStatement node, Object data) {
    return countNodeChildren( node, data );
  }

  public Object visit(ASTLocalVariableDeclaration node, Object data) {

    // "For" init declarations do not count as separate lines of code
    if ( node.jjtGetParent() instanceof ASTForInit ) {
      return NumericConstants.ZERO;
    }

    /*
     * This will count variables declared on the same line as separate NCSS
     * counts. This violates JavaNCSS standards, but I'm not convinced that's a
     * bad thing here.
     */

    return countNodeChildren( node, data );
  }

  public Object visit(ASTSwitchLabel node, Object data) {
    return countNodeChildren( node, data );
  }

}
