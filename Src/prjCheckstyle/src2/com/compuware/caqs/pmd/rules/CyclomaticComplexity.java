/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.rules;

import java.util.Stack;

import com.compuware.caqs.pmd.AbstractRule;
import com.compuware.caqs.pmd.ast.ASTBlockStatement;
import com.compuware.caqs.pmd.ast.ASTCatchStatement;
import com.compuware.caqs.pmd.ast.ASTClassOrInterfaceDeclaration;
import com.compuware.caqs.pmd.ast.ASTCompilationUnit;
import com.compuware.caqs.pmd.ast.ASTConditionalExpression;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTDoStatement;
import com.compuware.caqs.pmd.ast.ASTEnumDeclaration;
import com.compuware.caqs.pmd.ast.ASTExpression;
import com.compuware.caqs.pmd.ast.ASTForStatement;
import com.compuware.caqs.pmd.ast.ASTIfStatement;
import com.compuware.caqs.pmd.ast.ASTMethodDeclaration;
import com.compuware.caqs.pmd.ast.ASTMethodDeclarator;
import com.compuware.caqs.pmd.ast.ASTSwitchLabel;
import com.compuware.caqs.pmd.ast.ASTSwitchStatement;
import com.compuware.caqs.pmd.ast.ASTWhileStatement;
import com.compuware.caqs.pmd.ast.Node;
import com.compuware.caqs.pmd.ast.SimpleNode;
import com.compuware.caqs.pmd.rules.design.NpathComplexity;

/**
 * @author Donald A. Leckie
 * @version $Revision: 1.18 $, $Date: 2006/10/16 13:25:23 $
 * @since January 14, 2003
 */
public class CyclomaticComplexity extends AbstractRule {

  private int reportLevel;

  private static class Entry {
    private SimpleNode node;
    private int decisionPoints = 1;
    public int highestDecisionPoints;
    public int methodCount;

    private Entry(SimpleNode node) {
      this.node = node;
    }

    public void bumpDecisionPoints() {
      decisionPoints++;
    }

    public void bumpDecisionPoints(int size) {
      decisionPoints += size;
    }

    public int getComplexityAverage() {
      return ( (double) methodCount == 0 ) ? 1
          : (int) ( Math.rint( (double) decisionPoints / (double) methodCount ) );
    }
  }

  private Stack entryStack = new Stack();

  public Object visit(ASTCompilationUnit node, Object data) {
    reportLevel = getIntProperty( "reportLevel" );
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTIfStatement node, Object data) {
    int boolCompIf = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
    // If statement always has a complexity of at least 1
    boolCompIf++;

    ( (Entry) entryStack.peek() ).bumpDecisionPoints( boolCompIf );
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTCatchStatement node, Object data) {
    ( (Entry) entryStack.peek() ).bumpDecisionPoints();
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTForStatement node, Object data) {
    int boolCompFor = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
    // For statement always has a complexity of at least 1
    boolCompFor++;

    ( (Entry) entryStack.peek() ).bumpDecisionPoints( boolCompFor );
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTDoStatement node, Object data) {
    int boolCompDo = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
    // Do statement always has a complexity of at least 1
    boolCompDo++;

    ( (Entry) entryStack.peek() ).bumpDecisionPoints( boolCompDo );
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTSwitchStatement node, Object data) {
    Entry entry = (Entry) entryStack.peek();

    int boolCompSwitch = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
    entry.bumpDecisionPoints( boolCompSwitch );

    int childCount = node.jjtGetNumChildren();
    int lastIndex = childCount - 1;
    for ( int n = 0; n < lastIndex; n++ ) {
      Node childNode = node.jjtGetChild( n );
      if ( childNode instanceof ASTSwitchLabel ) {
        // default is generally not considered a decision (same as "else")
        ASTSwitchLabel sl = (ASTSwitchLabel) childNode;
        if ( !sl.isDefault() ) {
          childNode = node.jjtGetChild( n + 1 );
          if ( childNode instanceof ASTBlockStatement ) {
            entry.bumpDecisionPoints();
          }
        }
      }
    }
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTWhileStatement node, Object data) {
    int boolCompWhile = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
    // While statement always has a complexity of at least 1
    boolCompWhile++;

    ( (Entry) entryStack.peek() ).bumpDecisionPoints( boolCompWhile );
    super.visit( node, data );
    return data;
  }

  public Object visit(ASTConditionalExpression node, Object data) {
    if ( node.isTernary() ) {
      int boolCompTern = NpathComplexity.sumExpressionComplexity( (ASTExpression) node.getFirstChildOfType( ASTExpression.class ) );
      // Ternary statement always has a complexity of at least 1
      boolCompTern++;

      ( (Entry) entryStack.peek() ).bumpDecisionPoints( boolCompTern );
      super.visit( node, data );
    }
    return data;
  }

  public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
    if ( node.isInterface() ) {
      return data;
    }

    entryStack.push( new Entry( node ) );
    super.visit( node, data );
    Entry classEntry = (Entry) entryStack.pop();
    if ( ( classEntry.getComplexityAverage() >= reportLevel )
        || ( classEntry.highestDecisionPoints >= reportLevel ) ) {
      addViolation( data, node, new String[] {
          "class",
          node.getImage(),
          classEntry.getComplexityAverage() + " (Highest = "
              + classEntry.highestDecisionPoints + ')' } );
    }
    return data;
  }

  public Object visit(ASTMethodDeclaration node, Object data) {
    entryStack.push( new Entry( node ) );
    super.visit( node, data );
    Entry methodEntry = (Entry) entryStack.pop();
    int methodDecisionPoints = methodEntry.decisionPoints;
    Entry classEntry = (Entry) entryStack.peek();
    classEntry.methodCount++;
    classEntry.bumpDecisionPoints( methodDecisionPoints );

    if ( methodDecisionPoints > classEntry.highestDecisionPoints ) {
      classEntry.highestDecisionPoints = methodDecisionPoints;
    }

    ASTMethodDeclarator methodDeclarator = null;
    for ( int n = 0; n < node.jjtGetNumChildren(); n++ ) {
      Node childNode = node.jjtGetChild( n );
      if ( childNode instanceof ASTMethodDeclarator ) {
        methodDeclarator = (ASTMethodDeclarator) childNode;
        break;
      }
    }

    if ( methodEntry.decisionPoints >= reportLevel ) {
      addViolation( data, node, new String[] { "method",
          ( methodDeclarator == null ) ? "" : methodDeclarator.getImage(),
          String.valueOf( methodEntry.decisionPoints ) } );
    }

    return data;
  }

  public Object visit(ASTEnumDeclaration node, Object data) {
    entryStack.push( new Entry( node ) );
    super.visit( node, data );
    Entry classEntry = (Entry) entryStack.pop();
    if ( ( classEntry.getComplexityAverage() >= reportLevel )
        || ( classEntry.highestDecisionPoints >= reportLevel ) ) {
      addViolation( data, node, new String[] {
          "class",
          node.getImage(),
          classEntry.getComplexityAverage() + "(Highest = "
              + classEntry.highestDecisionPoints + ')' } );
    }
    return data;
  }

  public Object visit(ASTConstructorDeclaration node, Object data) {
    entryStack.push( new Entry( node ) );
    super.visit( node, data );
    Entry constructorEntry = (Entry) entryStack.pop();
    int constructorDecisionPointCount = constructorEntry.decisionPoints;
    Entry classEntry = (Entry) entryStack.peek();
    classEntry.methodCount++;
    classEntry.decisionPoints += constructorDecisionPointCount;
    if ( constructorDecisionPointCount > classEntry.highestDecisionPoints ) {
      classEntry.highestDecisionPoints = constructorDecisionPointCount;
    }
    if ( constructorEntry.decisionPoints >= reportLevel ) {
      addViolation( data, node, new String[] { "constructor",
          classEntry.node.getImage(),
          String.valueOf( constructorDecisionPointCount ) } );
    }
    return data;
  }

}
