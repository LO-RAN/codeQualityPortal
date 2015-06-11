package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.ScopeUtils;
import com.puppycrawl.tools.checkstyle.checks.CheckUtils;
import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 22 mars 2006
 * Time: 18:22:20
 * To change this template use File | Settings | File Templates.
 */
public class MagicNumberandLiteralCheck  extends Check
{
	/**
	 * The token types that are allowed in the AST path from the
	 * number literal to the enclosing constant definition.
	 */
	private static final int[] ALLOWED_PATH_TOKENTYPES = {
		TokenTypes.ASSIGN,
		TokenTypes.ARRAY_INIT,
		TokenTypes.EXPR,
		TokenTypes.UNARY_PLUS,
		TokenTypes.INDEX_OP,
		TokenTypes.UNARY_MINUS,
		TokenTypes.TYPECAST,
		TokenTypes.ELIST,
		TokenTypes.LITERAL_NEW,
		TokenTypes.METHOD_CALL,
		TokenTypes.STAR,
	};
	
	static {
		Arrays.sort(ALLOWED_PATH_TOKENTYPES);
	}
	
	/** the numbers to ignore in the check, sorted */
	private double[] mIgnoreNumbers = {-1, 0, 1, 2};
	
	public int[] getDefaultTokens()
	{
		return new int[] {
				TokenTypes.NUM_DOUBLE,
				TokenTypes.NUM_FLOAT,
				TokenTypes.NUM_INT,
				TokenTypes.NUM_LONG,
				TokenTypes.STRING_LITERAL
		};
	}
	
	public void visitLiteralDef(DetailAST lit){

		final DetailAST constantDefAST = findContainingConstantDef(lit);
		
		if (constantDefAST == null) {
			reportMagicNumber(lit);
		}
	}
	
	
	public void visitToken(DetailAST aAST)
	{   try{
		switch(aAST.getType()){
		case (TokenTypes.STRING_LITERAL):
			
		visitLiteralDef(aAST);
		break;
		default:
			if (inIgnoreList(aAST)) {
				return;
			}
		
		final DetailAST constantDefAST = findContainingConstantDef(aAST);
		
		if (constantDefAST == null) {
			reportMagicNumber(aAST);
		}
		else {
			DetailAST ast = aAST.getParent();
			while (ast != constantDefAST) {
				final int type = ast.getType();
				if (Arrays.binarySearch(ALLOWED_PATH_TOKENTYPES, type) < 0) {
					reportMagicNumber(aAST);
					break;
				}
				
				ast = ast.getParent();
			}
		}
		break;
		}
	}catch(Exception e){
		log(aAST.getLineNo(), "erreur dans visitToken :"+getFileContents().getLines()[aAST.getLineNo()]);
		
	}
	}
	
	/**
	 * Finds the constant definition that contains aAST.
	 * @param aAST the AST
	 * @return the constant def or null if aAST is not
	 * contained in a constant definition
	 */
	private DetailAST findContainingConstantDef(DetailAST aAST)
	{
		try{
		DetailAST varDefAST = aAST;
		while (varDefAST != null
				&& varDefAST.getType() != TokenTypes.VARIABLE_DEF
				&& varDefAST.getType() != TokenTypes.ENUM_CONSTANT_DEF)
		{
			varDefAST = varDefAST.getParent();
		}
		
		// no containing variable definition?
		if (varDefAST == null) {
			return null;
		}
		
		// implicit constant?
		if (ScopeUtils.inInterfaceOrAnnotationBlock(varDefAST)
				|| varDefAST.getType() == TokenTypes.ENUM_CONSTANT_DEF)
		{
			return varDefAST;
		}
		
		// explicit constant
		final DetailAST modifiersAST =
			varDefAST.findFirstToken(TokenTypes.MODIFIERS);
		if (modifiersAST.branchContains(TokenTypes.FINAL)) {
			return varDefAST;
		}
		}catch(Exception e){
			log(aAST.getLineNo(),"erreur dans findContainingConstantDef");
		}
		return null;
	}
	
	/**
	 * Reports aAST as a magic number, includes unary operators as needed.
	 * @param aAST the AST node that contains the number to report
	 */
	private void reportMagicNumber(DetailAST aAST)
	{
		String text = aAST.getText();
		final DetailAST parent = aAST.getParent();
		DetailAST reportAST = aAST;
		if (parent.getType() == TokenTypes.UNARY_MINUS) {
			reportAST = parent;
			text = "-" + text;
		}
		else if (parent.getType() == TokenTypes.UNARY_PLUS) {
			reportAST = parent;
			text = "+" + text;
		}
		System.out.println("magic.number "+aAST.getText());
		log(reportAST.getLineNo(),
				reportAST.getColumnNo(),
				"magic.number "+aAST.getText() ,
				text);
	}
	
	/**
	 * Decides whether the number of an AST is in the ignore list of this
	 * check.
	 * @param aAST the AST to check
	 * @return true if the number of aAST is in the ignore list of this
	 * check.
	 */
	private boolean inIgnoreList(DetailAST aAST)
	{
		double value = CheckUtils.parseDouble(aAST.getText(), aAST.getType());
		final DetailAST parent = aAST.getParent();
		if (parent.getType() == TokenTypes.UNARY_MINUS) {
			value = -1 * value;
		}
		return (Arrays.binarySearch(mIgnoreNumbers, value) >= 0);
	}
	
	/**
	 * Sets the numbers to ignore in the check.
	 * BeanUtils converts numeric token list to double array automatically.
	 * @param aList list of numbers to ignore.
	 */
	public void setIgnoreNumbers(double[] aList)
	{
		if ((aList == null) || (aList.length == 0)) {
			mIgnoreNumbers = new double[0];
		}
		else {
			mIgnoreNumbers = new double[aList.length];
			System.arraycopy(aList, 0, mIgnoreNumbers, 0, aList.length);
			Arrays.sort(mIgnoreNumbers);
		}
	}
}
