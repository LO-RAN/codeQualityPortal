package com.compuware.caqs.JavaStyle;

import java.util.HashSet;
import java.util.Iterator; 
import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public final class SearchForNotFreeRessources extends Check
{
	private static String methodName;
	private static Set liste=new HashSet();;
	
	public int[] getDefaultTokens()
	{
		return new int[] {
				TokenTypes.METHOD_DEF,
				TokenTypes.VARIABLE_DEF,
				TokenTypes.METHOD_CALL
		};
	}
	
	public int[] getRequiredTokens()
	{
		return getDefaultTokens();
	}
	
	public void visitToken(DetailAST aAST)
	{   	
		switch(aAST.getType()){
		case  TokenTypes.METHOD_DEF:
			methodName=aAST.findFirstToken( TokenTypes.IDENT).getText() ;
			break;
		case  TokenTypes.VARIABLE_DEF:
			try{
				if(aAST.findFirstToken(TokenTypes.TYPE).findFirstToken(TokenTypes.IDENT) != null){
					
					String type=aAST.findFirstToken(TokenTypes.TYPE).findFirstToken(TokenTypes.IDENT).getText();
					if(type.equals("ResultSet")
							||type.equals("PreparedStatement")
							||type.equals("Statement")
							||type.equals("Connection")
					){
						liste.add(aAST.findFirstToken(TokenTypes.IDENT).getText() );
					}
				}
			}catch(Exception e){
				log(aAST.getLineNo(),"exception dans case  TokenTypes.VARIABLE_DEF :"+aAST.getText() );
			}
			break;
		case  TokenTypes.METHOD_CALL:
			boolean founded=false;
			if(aAST.findFirstToken(TokenTypes.DOT)!=null){
				if(aAST.findFirstToken(TokenTypes.DOT).getLastChild()!=null){
					if(aAST.findFirstToken(TokenTypes.DOT).getLastChild().getText().equals("close")){
						
						if(liste.contains(aAST.findFirstToken(TokenTypes.DOT).getFirstChild().getText())){
							founded=true;
							if(!removeFromListe(aAST.findFirstToken(TokenTypes.DOT).getFirstChild().getText()))
								log(aAST.getLineNo(),"pb de remove de l'objet "+aAST.findFirstToken(TokenTypes.DOT).getFirstChild().getText());
						}
					}
				}
			}
			if(!founded){
				if(aAST.findFirstToken(TokenTypes.ELIST).findFirstToken(TokenTypes.EXPR)!=null){
					DetailAST tmp=aAST.findFirstToken(TokenTypes.ELIST).getLastChild();
					
					if(tmp.getChildCount() > 1){
						
						if(tmp.findFirstToken(TokenTypes.IDENT)!=null){
							log(aAST.getLineNo(),"simple EXPR : "+tmp.findFirstToken(TokenTypes.IDENT).getText() );
						if(liste.contains(tmp.findFirstToken(TokenTypes.IDENT).getText())){
					
						//log(aAST.getLineNo(),"objet trouve en arg de fonc");
						}
						}
					}else{
						int i=0;
					while(i<aAST.findFirstToken(TokenTypes.ELIST).getChildCount() ){
						if(tmp.getType() == TokenTypes.EXPR){
							//log(aAST.getLineNo(),"passage "+i+" sur "+aAST.findFirstToken(TokenTypes.ELIST).getChildCount());
							if(tmp.findFirstToken(TokenTypes.IDENT)!=null){
						//		log(aAST.getLineNo(),"parcours expr "+tmp.findFirstToken( TokenTypes.IDENT).getText() );
								if(liste.contains(tmp.findFirstToken(TokenTypes.IDENT).getText())){
									log(aAST.getLineNo(),"Dans la methode "+this.methodName +" l objet "+tmp.findFirstToken(TokenTypes.IDENT).getText()+" trouve en arg de fonc "
											+FullIdent.createFullIdent((DetailAST)aAST.getFirstChild() ));
								}
							}
						}
						i=i+1;
						
						tmp=tmp.getPreviousSibling();
					}
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	private boolean removeFromListe(String nomIt){
		boolean removed=false;
		final Iterator it = liste.iterator();
		while (it.hasNext() && !removed) {
			String name=(String) it.next();
			if(nomIt.equals(name)){
				it.remove();
				removed=true;
			}
		}
		return removed;
	}
	
	public void leaveToken(DetailAST aAST)
	{   	
		String[] table=null;
		int i = 0;
		
		switch(aAST.getType()){
		case  TokenTypes.METHOD_DEF:
			
			final Iterator it = liste.iterator();
			if(!liste.isEmpty()){
				String errorLog="Dans la methode "+aAST.findFirstToken( TokenTypes.IDENT ).getText() +
				" les objets suivants n ont pas ete liberes : ";
				
				while (it.hasNext()) {
					String name=(String) it.next();
					errorLog=errorLog+name+", ";
				}
				log(aAST.getLineNo(),errorLog);
			}
			liste.clear();
			break;
		default:
			break;
		}
	}
	
}
