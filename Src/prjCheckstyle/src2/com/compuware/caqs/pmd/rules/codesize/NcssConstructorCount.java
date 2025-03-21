package com.compuware.caqs.pmd.rules.codesize;

import java.util.Iterator;
import java.util.Set;

import com.compuware.caqs.pmd.RuleContext;
import com.compuware.caqs.pmd.ast.ASTConstructorDeclaration;
import com.compuware.caqs.pmd.ast.ASTExplicitConstructorInvocation;
import com.compuware.caqs.pmd.stat.DataPoint;
import com.compuware.caqs.pmd.util.NumericConstants;

/**
 * Non-commented source statement counter for constructors.
 * 
 * @author Jason Bennett
 */
public class NcssConstructorCount extends AbstractNcssCount {

  /**
   * Count constructor declarations. This includes any explicit super() calls.
   */
  public NcssConstructorCount() {
    super( ASTConstructorDeclaration.class );
  }

  public Object visit(ASTExplicitConstructorInvocation node, Object data) {
    return NumericConstants.ONE;
  }

  protected void makeViolations(RuleContext ctx, Set p) {
    Iterator points = p.iterator();
    while ( points.hasNext() ) {
      DataPoint point = (DataPoint) points.next();
      // TODO need to put class name or constructor ID in string
      addViolation(
          ctx,
          point.getNode(),
          new String[] {
              String.valueOf( ( (ASTConstructorDeclaration) point.getNode() ).getParameterCount() ),
              String.valueOf( (int) point.getScore() ) } );
    }
  }
}
