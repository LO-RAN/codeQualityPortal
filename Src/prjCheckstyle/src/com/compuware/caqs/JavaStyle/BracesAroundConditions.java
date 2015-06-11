
package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class BracesAroundConditions extends Check{
	
	public BracesAroundConditions()
	{
		super();		
	}
	
	
	public int[] getDefaultTokens()
	{
		return new int[] {
				//TokenTypes.LITERAL_IF, 
				TokenTypes.LAND, 
				TokenTypes.LOR};
	}
	
	
	public int[] getRequiredTokens()
	{
		return getDefaultTokens();
	}
	
	public void visitToken(DetailAST aAST)
	{
		switch (aAST.getType()) {
		//case TokenTypes.LITERAL_IF:
		//inIf=true;
		//break;
		case TokenTypes.LAND:
		case TokenTypes.LOR:
		
			controlBraces(aAST);
			break;
		default:
			visitTokenHook(aAST);
		}
		
	}
	
	private boolean isSearchedToken(DetailAST aAST){
		if(aAST.getType() == TokenTypes.LAND
			||aAST.getType() == TokenTypes.LOR) 
			return true;
		else 
			return false;
	}
	
	private boolean controlBraces(DetailAST aAST){
		boolean foundBraces=false;
		
		if((aAST.getPreviousSibling() !=null && aAST.getNextSibling() !=null) 
				|| (isSearchedToken(aAST.getParent()) && aAST.getParent().getType() != aAST.getType())){
			log(aAST.getLineNo()," in control "+aAST.getText()+","+aAST.getLineNo()+" , "+aAST.getParent().getText());
			if(aAST.getNextSibling().getType()==TokenTypes.RPAREN && aAST.getPreviousSibling().getType()==TokenTypes.LPAREN ){
				foundBraces=true;
			}else{
				log(aAST.getLineNo()," no parens");
				
			} 
		}
		
		return foundBraces;
	}
	
	private void visitTokenHook(DetailAST aAST){}
}

