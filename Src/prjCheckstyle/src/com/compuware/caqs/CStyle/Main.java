package com.compuware.caqs.CStyle;

import com.compuware.caqs.CStyle.AST.*;

import java.io.*;
import java.util.Hashtable;

import com.compuware.caqs.FileManager.*;
import com.compuware.caqs.CStyle.AST.CPPParserTreeConstants;
public class Main implements CPPParserTreeConstants{
	private static CPPParser parser;
	private static int niveau=0;
	static SimpleNode[] nodes=new SimpleNode[10000];
	static SimpleNode[] functionDefinitionNodes;
	static SimpleNode[] functionCallNodes;
	static SimpleNode[] extDeclNodes;
	static SimpleNode[] localDataNodes;
	static int functionDefinitionNumber=0;
	static int functionCallNumber=0;
	static int extDeclNumber=0;
	static int localDataNumber=0;
	static int numTemp=0;
	private Hashtable temp;
	private SimpleNode[] functionCallPerFuncNodes;
	private int functionCallPerFuncNumber=0;
	
	public Main(){
		//((SimpleNode)parser.getRootNode()).dump("-");
		
		SimpleNode root=(SimpleNode)parser.getRootNode();
		//functionDefinitionNodes=new SimpleNode[0];
		
		//liste de toutes les definitions et declarations de fonction
		this.findNode(JJTFUNCTION_DEFINITION);
		functionDefinitionNumber=numTemp;
		numTemp=0;
		functionDefinitionNodes=new SimpleNode[functionDefinitionNumber];
		for(int i=0;i<functionDefinitionNumber;i++) functionDefinitionNodes[i]=nodes[i];
		nodes=new SimpleNode[10000];
		
		for(int i=0;i<functionDefinitionNumber;i++){
			isFunctionDefinition(i);
			isFunctionDeclaration(i);
			//isExternalFunction(i);
		}
		
		//liste de tous les appels
		/*this.findNode(JJTPOSTFIX_EXPRESSION);
		functionCallNumber=numTemp;
		numTemp=0;
		functionCallNodes=new SimpleNode[functionCallNumber];
		for(int i=0;i<functionCallNumber;i++) functionCallNodes[i]=nodes[i];
		nodes=new SimpleNode[1000];
		
		for(int i=0;i<functionCallNumber;i++){
			isFunctionCall(i);
		}*/
		
		//liste de tous les appels par fonction
		for(int i=0;i<functionDefinitionNumber;i++){
			this.explore(functionDefinitionNodes[i],JJTPOSTFIX_EXPRESSION);
			functionCallPerFuncNumber=numTemp;
			numTemp=0;
			functionCallPerFuncNodes=new SimpleNode[functionCallPerFuncNumber];
			for(int j=0;j<functionCallPerFuncNumber;j++) functionCallPerFuncNodes[j]=nodes[j];
			nodes=new SimpleNode[10000];
			//System.err.println("fonctions appelees dans la fonction "+((SimpleNode)functionDefinitionNodes[i].jjtGetChild(1).jjtGetChild(0)).getImage());
			for(int j=0;j<functionCallPerFuncNumber;j++){
				isFunctionCallPerFunc(((SimpleNode)functionDefinitionNodes[i].jjtGetChild(1).jjtGetChild(0)).getImage(),j);
			}
			this.explore(functionDefinitionNodes[i],JJTDECLARATION);
			localDataNumber=numTemp;
			numTemp=0;
			localDataNodes=new SimpleNode[localDataNumber];
			for(int j=0;j<localDataNumber;j++) {
				localDataNodes[j]=nodes[j];
				System.out.println(" local data dans func ligne "+localDataNodes[j].getBeginLine()+" "+localDataNodes[j].getImage());
			}
			nodes=new SimpleNode[1000];
			System.out.println(localDataNumber+" local data dans func "+((SimpleNode)functionDefinitionNodes[i].jjtGetChild(1).jjtGetChild(0)).getImage());
		}
		
		//liste de toutes les variables globales
		this.findNode(JJTEXTERNAL_DECLARATION);
		extDeclNumber=numTemp;
		numTemp=0;
		extDeclNodes=new SimpleNode[extDeclNumber];
		for(int i=0;i<extDeclNumber;i++) extDeclNodes[i]=nodes[i];
		nodes=new SimpleNode[1000];
		
		for(int i=0;i<extDeclNumber;i++){
			isGlobalData(i);
		}
	}
	
	private void isGlobalData(int num){
		//System.err.println("ext decl : "+extDeclNodes[num].getId());
		//((SimpleNode)extDeclNodes[num]).dump("ed");
		if(extDeclNodes[num].getId() == JJTEXTERNAL_DECLARATION 
				&& extDeclNodes[num].jjtGetNumChildren() > 0){
			if(((SimpleNode)extDeclNodes[num].jjtGetChild(0)).getId() == JJTDECLARATION 
					&& extDeclNodes[num].jjtGetChild(0).jjtGetNumChildren() == 2){
				
				if(((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1)).getId() == JJTINIT_DECLARATOR_LIST
						&& extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetNumChildren() > 0){
					if(((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0)).getId() == JJTINIT_DECLARATOR
							&& extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetNumChildren() > 0){
						if(((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0)).getId() == JJTDECLARATOR
								&& extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren() > 0){
							if(((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getId() == JJTDIRECT_DECLARATOR
									&& extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren() > 0){
								if(((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getId() == JJTQUALIFIED_ID){
									String ext="file";
									if(isExterne((SimpleNode)extDeclNodes[num].jjtGetChild(0))){
										ext="extern";
									}
									System.out.println( ((SimpleNode)extDeclNodes[num].jjtGetChild(0).jjtGetChild(1).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getImage()+" is a global data ("+ext+") in the .c program");
								}
							}
							
						}
					}
					/*if(functionCallNodes[num].jjtGetChild(0).jjtGetNumChildren() > 0){
						if(((SimpleNode)functionCallNodes[num].jjtGetChild(0).jjtGetChild(0)).getId() == JJTID_EXPRESSION){
							System.err.println( ((SimpleNode)functionCallNodes[num].jjtGetChild(0).jjtGetChild(0)).getImage()+" is a called function in the .c program");
						}
					}*/
				}
			}
		}
	}
	
	private void isFunctionCall(int num){
		if(functionCallNodes[num].getId() == JJTPOSTFIX_EXPRESSION 
				&& functionCallNodes[num].jjtGetNumChildren() == 2){
			if(((SimpleNode)functionCallNodes[num].jjtGetChild(0)).getId() == JJTPRIMARY_EXPRESSION
					&& ((SimpleNode)functionCallNodes[num].jjtGetChild(1)).getId() == JJTEXPRESSION_LIST){
				if(functionCallNodes[num].jjtGetChild(0).jjtGetNumChildren() > 0){
					if(((SimpleNode)functionCallNodes[num].jjtGetChild(0).jjtGetChild(0)).getId() == JJTID_EXPRESSION){
						System.out.println( ((SimpleNode)functionCallNodes[num].jjtGetChild(0).jjtGetChild(0)).getImage()+" is a called function in the .c program");
					}
				}
			}
		}
	}
	
	private void isFunctionCallPerFunc(String func,int num){
		if(functionCallPerFuncNodes[num].getId() == JJTPOSTFIX_EXPRESSION 
				&& functionCallPerFuncNodes[num].jjtGetNumChildren() == 2){
			if(((SimpleNode)functionCallPerFuncNodes[num].jjtGetChild(0)).getId() == JJTPRIMARY_EXPRESSION
					&& ((SimpleNode)functionCallPerFuncNodes[num].jjtGetChild(1)).getId() == JJTEXPRESSION_LIST){
				if(functionCallPerFuncNodes[num].jjtGetChild(0).jjtGetNumChildren() > 0){
					if(((SimpleNode)functionCallPerFuncNodes[num].jjtGetChild(0).jjtGetChild(0)).getId() == JJTID_EXPRESSION){
						System.out.println("calls : "+func+" calls "+ ((SimpleNode)functionCallPerFuncNodes[num].jjtGetChild(0).jjtGetChild(0)).getImage());
					}
				}
			}
		}
	}
	
	private boolean isExterne(SimpleNode sn){
		
		if(sn.jjtGetNumChildren()>0){
			if(((SimpleNode)sn.jjtGetChild(0)).getId() == JJTDECLARATION_SPECIFIERS){
				
				if(sn.jjtGetChild(0).jjtGetNumChildren()>0){
					if(((SimpleNode)sn.jjtGetChild(0).jjtGetChild(0)).getId() == JJTTYPE_MODIFIERS){
						
					if(sn.jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren()>0){
						if(((SimpleNode)sn.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getId() == JJTSTORAGE_CLASS_SPECIFIER){
							
							if(((SimpleNode)sn.jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getImage() == "EXTERN") {
								
								return true;
							}else{
								return false;
							}
						}
					}
					}
				}	
			}
		}
		return false;
	}
	
	/*private void isExternalFunction(int num){
		if(functionDefinitionNodes[num].getId() == JJTFUNCTION_DEFINITION 
				&& functionDefinitionNodes[num].jjtGetNumChildren() == 3){
			if(((SimpleNode)functionDefinitionNodes[num].jjtGetChild(0)).getId() == JJTDECLARATION_SPECIFIERS
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1)).getId() == JJTFUNCTION_DECLARATOR
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(2)).getId() == JJTFUNC_DECL_DEF){
				if(functionDefinitionNodes[num].jjtGetChild(0).jjtGetNumChildren()>0){
					if(functionDefinitionNodes[num].jjtGetChild(0).jjtGetChild(0).jjtGetNumChildren()>0){
						if(((SimpleNode)functionDefinitionNodes[num].jjtGetChild(0).jjtGetChild(0).jjtGetChild(0)).getId() == JJTSTORAGE_CLASS_SPECIFIER){
							
							
							System.err.println( ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1).jjtGetChild(0)).getImage()+" is External");
						}
					}
				}	
			}
		}
	}*/
	
	
	private void isFunctionDefinition(int num){
		
		if(functionDefinitionNodes[num].getId() == JJTFUNCTION_DEFINITION 
				&& functionDefinitionNodes[num].jjtGetNumChildren() == 3){
			if(((SimpleNode)functionDefinitionNodes[num].jjtGetChild(0)).getId() == JJTDECLARATION_SPECIFIERS
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1)).getId() == JJTFUNCTION_DECLARATOR
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(2)).getId() == JJTFUNC_DECL_DEF){
				if(functionDefinitionNodes[num].jjtGetChild(2).jjtGetNumChildren()>0){
					if(((SimpleNode)functionDefinitionNodes[num].jjtGetChild(2).jjtGetChild(0)).getId() == JJTCOMPOUND_STATEMENT){
						String ext="file";
						if(isExterne((SimpleNode)functionDefinitionNodes[num])){
							ext="extern";
						}
						System.out.println("Function Definition ("+ext+"):"+((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1).jjtGetChild(0)).getImage());
					}
				}
			}
		}
	}
	
	private void isFunctionDeclaration(int num){
		if(functionDefinitionNodes[num].getId() == JJTFUNCTION_DEFINITION 
				&& functionDefinitionNodes[num].jjtGetNumChildren() == 3){
			if(((SimpleNode)functionDefinitionNodes[num].jjtGetChild(0)).getId() == JJTDECLARATION_SPECIFIERS
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1)).getId() == JJTFUNCTION_DECLARATOR
					&& ((SimpleNode)functionDefinitionNodes[num].jjtGetChild(2)).getId() == JJTFUNC_DECL_DEF){
				if(functionDefinitionNodes[num].jjtGetChild(2).jjtGetNumChildren() == 0){
					String ext="file";
					if(isExterne((SimpleNode)functionDefinitionNodes[num])){
						ext="extern";
					}
					System.out.println("Function Declaration ("+ext+"):"+((SimpleNode)functionDefinitionNodes[num].jjtGetChild(1).jjtGetChild(0)).getImage()+" is Function Declaration");
					
				}
			}
		}
	}
	
	public static void main(String args[]){
		String outDir="";
		String[] files;
		try{
			
			if(args.length > 0){
				
				if(args.length >1){
					outDir=args[1];
				}else{
					outDir=args[0];
				}
				//new CParser(args[0]);
				if((new File(outDir)).isDirectory()){
					DirectoryManage dm=new DirectoryManage(outDir);
					files=dm.getFileNames(outDir,"cpp");
				}else{
					files=new String[1];
					files[0]=outDir;
				}
				//MetricsExtractor me= new MetricsExtractor(new java.io.FileInputStream(files[0]));
				for(int i=0;i<files.length;i++){
					//System.out.println(files[i]);
					
					java.io.InputStream input;
					
					int ai = 0;
					
					if (ai == (args.length-1)) {
						System.err.println("Reading from file " + files[i] + " . . .");
						try {
							input = new java.io.FileInputStream(files[i]);
						} catch (java.io.FileNotFoundException e) {
							System.err.println("File " + args[0] + " not found.");
							return;
						}
					} else if (ai >= args.length) {
						input = System.in;
					} else {
						System.err.println("Usage: java ... [-d] [inputfile]");
						return;
					}
					System.err.println("parsing "+files[i]); 
					try {
						System.out.println("<file name=\""+files[i]+"\">");
						parser = new CPPParser(input);
						parser.ReInit(input);
						parser.translation_unit();
						System.err.println(files[i]+" parsed successfully.");
						new Main();
						System.out.println("</file>");
						
					} catch (ParseException e) {
						System.err.println(files[i]+" encountered errors during parse.");
					}
				}
			}else{
				System.err.println("erreur, pas de fichier ou de répertoire en entrée...");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("aïeu");}
	}
	
	private void explore(SimpleNode sn, int id){
		
		if(sn.getId() == id) {
			//sn.dump("-");//System.out.println(CPPParserTreeConstants.jjtNodeName[id]+" "+sn.getImage());
			//System.out.println("explore "+numTemp);
			nodes[numTemp]=sn;
			numTemp++;
		}
		for(int i=0;i<sn.jjtGetNumChildren();i++){
			explore((SimpleNode)sn.jjtGetChild(i),id);
		}
		
	}
	
	
	
	private void findNode(int nodeType){
		
		explore((SimpleNode)parser.getRootNode(),nodeType);
		
		
	}
	
}
