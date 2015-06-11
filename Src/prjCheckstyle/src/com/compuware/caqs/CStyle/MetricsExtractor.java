package com.compuware.caqs.CStyle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.compuware.caqs.CStyle.AST.*;

public class MetricsExtractor {
	private boolean inFunction;
	private String functionName;
	private int vg=0;
	private int loc;
	private static List l;
	private static int functionLevel;
	String[] contents;
	public int beginLine;
	public int endLine;
	public static String fileName="";
	public static boolean constTrouve=false;
	public static boolean funcTrouve=false;
	public static boolean specifierTrouve=false;
	public static boolean ctorInitializerTrouve=false;
	public static boolean parameterDeclaration=false;
	public static boolean declTrouve=false;
	public static CommentsCountExtractor comments;
	public static Set table;
	private static int lastLine=0;
	
	public MetricsExtractor(String input){
		comments=new CommentsCountExtractor(input);
		fileName=input;
		l=new ArrayList();
		functionLevel=0;
		inFunction=false;
		
		try{
		CPPParser parser;
		parser = new CPPParser(new java.io.FileInputStream(input));
		parse(parser);
		}catch(Exception e){
			System.out.println("erreur lors du parsing du fichier "+input);
			e.printStackTrace();
		}
		
	}
	
	public MetricsExtractor(InputStream input){
		l=new ArrayList();
		functionLevel=0;
		//vg=0;
		//loc=0;
		inFunction=false;
		//beginLine=0;
		CPPParser parser;
		parser = new CPPParser(input);
		parse(parser);
		
	}
	
	public static void main(String args[]) {
		InputStream input;
		int ai = 0;
		try{
			if(args.length > 0){
				comments=new CommentsCountExtractor(args[0]);
				fileName=args[0];
				input = new java.io.FileInputStream(args[0]);
				new MetricsExtractor(input);
				
				
			}
			
			
		}catch(Exception e){
			System.err.println("erreur lors de la lecture du fichier "+args[0]);
			e.printStackTrace();
		}
	}
	
	private void parse(CPPParser parser){
		try{
			ASTtranslation_unit c = parser.translation_unit();
			Thread.yield();
			
			CPPParserVisitor visitor; 
			
			List acus = new ArrayList();
			acus.add(c);
			if(!acus.isEmpty()){								
				SimpleNode rn=(SimpleNode)parser.getRootNode();
				exploreNode(rn,rn.jjtGetNumChildren());
			}
			
		} catch (ParseException e) {
			System.out.println("Encountered errors during parse.");
		}
	}
	
	private static int niveau=0;
	private SimpleNode exploreNode(Node n,int nbChild){
		SimpleNode sn=null;
		//System.out.println("exploration de "+n.getClass());
		for(int i = 0; i< nbChild;i++){
			Node fils=n.jjtGetChild(i);
			sn=(SimpleNode)fils;
			//for(int niv=0;niv<niveau;niv++) System.out.print("-");
			//System.out.println(sn.toString()+" "+sn.getBeginLine());
			lastLine=sn.getBeginLine();
			if(sn.toString()=="func_decl_def"){
				inFunction=true;
				
				vg=1;
				functionLevel=niveau;
				
			}
			if(sn.toString()=="type_qualifier" && sn.getImage()=="const"){
				constTrouve=true;
				
				//System.out.println("----constante "+sn.getImage()+" "+sn.jjtGetNumChildren()+" "+i+" "+nbChild);
			}
			if(sn.toString()=="builtin_type_specifier"){
				
				specifierTrouve=true;
				//System.out.println("builtin_type_specifier "+sn.getImage()+" "+sn.getBeginLine());
			}
			if(sn.toString()=="declaration"){
				
				declTrouve=true;
				//System.out.println("declarator "+sn.getImage()+" "+sn.jjtGetNumChildren()+" "+i+" "+nbChild);
			}
			if(sn.toString()=="ctor_initializer"){
				
				ctorInitializerTrouve=true;
				//System.out.println("ctor_initializer "+sn.getImage()+" "+sn.jjtGetNumChildren()+" "+i+" "+nbChild);
			}
			if(sn.toString()=="parameter_declaration"){
				
				parameterDeclaration=true;
				//System.out.println("parameter_declaration "+sn.getImage()+" "+sn.jjtGetNumChildren()+" "+i+" "+nbChild);
			}
			if(sn.toString()=="qualified_id"){
				
				
				if(specifierTrouve){
					if(parameterDeclaration){
						if(!Constants.functionName.matcher(sn.getImage()).matches()){
							System.out.println("<rule type=\"parameterName\" file=\""+fileName+"\" function=\""+functionName+"\" message=\""+sn.getImage()+"\" beginLine=\""+sn.getBeginLine()+"\"/>");
						}
						//System.out.println("parameterDeclaration : "+sn.getImage()+" "+sn.getBeginLine());
						parameterDeclaration=false;
						
					}else{
						if(constTrouve){
							if(!Constants.constantName.matcher(sn.getImage()).matches()){
								System.out.println("<rule type=\"constanteName\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\""+sn.getImage()+" ne devrait être composé que de majuscules et de '_'\" beginLine=\""+sn.getBeginLine()+"\"/>");
							}
							constTrouve=false;
							
						}else{
							if(funcTrouve && !declTrouve){
								if(!Constants.functionName.matcher(sn.getImage().substring(sn.getImage().indexOf("::")+2)).matches()){
									System.out.println("<rule type=\"functionName\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\""+sn.getImage()+"\" beginLine=\""+sn.getBeginLine()+"\"/>");
								}
								//System.out.println("func : "+sn.getImage().substring(sn.getImage().indexOf("::")+2)+" "+sn.getBeginLine());
								funcTrouve=false;
							}else{
								if(declTrouve){
									if(!Constants.functionName.matcher(sn.getImage()).matches()){
										System.out.println("<rule type=\"varName\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\""+sn.getImage()+"\" beginLine=\""+sn.getBeginLine()+"\"/>");
									}
									//System.out.println("var : "+sn.getImage()+" "+sn.getBeginLine());
									declTrouve=false;
								}
							}
						}
					}
					specifierTrouve=false;
				}
				if(ctorInitializerTrouve){
					//System.out.println("<rule type=\"ctorInit\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\""+sn.getImage()+"\" beginLine=\""+sn.getBeginLine()+"\"/>");
					//System.out.println("ctorInit : "+sn.getImage()+" "+sn.getBeginLine());
					ctorInitializerTrouve=false;
					//specifierTrouve=false;
				}		
				
				
			}
			if(sn.toString()=="function_direct_declarator"){
				funcTrouve=true;
				functionName=sn.getImage();
				//int temp=sn.getBeginLine();
				if(beginLine != 0){
					//System.out.println("comments : "+comments.count(beginLine,temp));
				}
				
				beginLine = sn.getBeginLine();
				endLine = sn.getEndLine();
				int parentBeginLine = ((SimpleNode)sn.jjtGetParent()).getBeginLine();
				loc=0;
			}
			
			if(sn.toString()=="class_specifier"){
				//System.out.println("class : "+sn.getImage()+" "+sn.getBeginLine());
			}
			
			if(sn.toString()=="jump_statement" && sn.getImage()=="goto"){
				System.out.println("<rule type=\"jump-goto\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\"L'instruction "+sn.getImage()+" ne devrait pas être utilisé.\" beginLine=\""+sn.getBeginLine()+"\"/>");
			}
			if(sn.toString()=="jump_statement" && sn.getImage()=="continue"){
				//System.out.println("jump \"continue\"trouve : "+sn.getImage()+" "+sn.getBeginLine());
				System.out.println("<rule type=\"jump-continue\" file=\""+fileName+"\" function=\""+functionName+"\"  message=\""+sn.getImage()+" ne devrait pas être utilisé.\" beginLine=\""+sn.getBeginLine()+"\"/>");
			}
			
			if(sn.toString()=="selection_statement"){
				vg++;
			}
			if(sn.toString()=="statement"){
				loc++;
			}
			if(sn.toString()=="iteration_statement"){
				vg++;
			}
			if(sn.toString()=="logical_and_expression" && sn.jjtGetNumChildren() >1){
				vg++;
			}
			if(sn.toString()=="logical_or_expression" && sn.jjtGetNumChildren() >1){
				vg++;
			}
			SimpleNode parent=(SimpleNode)sn.jjtGetParent();
			/*if(i>0){
			 System.out.println(sn.getBeginLine() +" "+ ((SimpleNode)n.jjtGetChild(i-1)).getBeginLine());
			 if(inFunction && sn.getBeginLine() != ((SimpleNode)n.jjtGetChild(i-1)).getBeginLine()){
			 System.out.println("ajout d'une ligne1 "+sn.getImage());
			 loc++;
			 }
			 }else{
			 System.out.println("ajout d'une ligne2 "+sn.getImage());
			 loc++;
			 }*/
			niveau++;
			
			SimpleNode sn2=exploreNode(fils,fils.jjtGetNumChildren());
			
			niveau--;
			if(inFunction && niveau<functionLevel){
				inFunction=false;
		//		System.out.println("sortie de fonction "+beginLine+" "+lastLine);
				int temp=sn.getBeginLine();
			//	System.out.println("comments : "+comments.count(beginLine,lastLine));
				System.out.println("<metric file=\""+fileName+"\" function=\""+functionName+"\" beginLine=\""+beginLine+"\" loc=\""+loc+"\" cloc=\""+comments.count(beginLine,lastLine)+"\" vg=\""+vg+"\"/>");
				String[] s=new String[2];
				/*s[0]=functionName;
				s[1]=new Integer(beginLine).toString();
				table.add(s);*/
			}
		}
		return sn;
	}
}
