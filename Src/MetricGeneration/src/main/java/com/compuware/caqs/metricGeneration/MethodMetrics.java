package com.compuware.caqs.metricGeneration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import antlr.collections.ASTEnumeration;

import com.compuware.caqs.metricGeneration.graph.BasicNode;
import com.compuware.caqs.metricGeneration.graph.CyclomaticFlowNode;
import com.compuware.caqs.metricGeneration.graph.DestructuringNode;
import com.compuware.caqs.metricGeneration.graph.MethodCallNode;
import com.compuware.caqs.metricGeneration.graph.Node;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.checks.j2ee.Utils;



/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 11 mai 2006
 * Time: 17:35:48
 * To change this template use File | Settings | File Templates.
 */
public class MethodMetrics extends Check {
    private static int vg;
    private static boolean inIf;
    private int loc;
    private int cloc;
    private int startLine;
    private int fanout;
    private static int maxMcd;
    private static int tempMaxMcd;
    /**the current visited method */
    private DetailAST currentMEthod;
    private Set<String> operandSet = new HashSet<String>();
    private int nbTotalOperand = 0;
    private Set<String> operatorSet = new HashSet<String>();
    private int nbTotalOperator = 0;
    
    public MethodMetrics()
    {
    	initVal();
        
    }

    private static void initVal(){
        vg=0;
        maxMcd=0;
        inIf=false;
    }

    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ENUM_DEF,

            TokenTypes.CTOR_DEF,
            TokenTypes.METHOD_DEF,

            TokenTypes.INSTANCE_INIT,
            TokenTypes.STATIC_INIT,

            TokenTypes.LITERAL_WHILE,
            TokenTypes.LITERAL_DO,
            TokenTypes.LITERAL_FOR,
            TokenTypes.LITERAL_IF,
            TokenTypes.LITERAL_CASE,

            TokenTypes.METHOD_CALL,

            TokenTypes.QUESTION,
            TokenTypes.LAND,
            TokenTypes.LOR,
            
        TokenTypes.ASSIGN,
        TokenTypes.BAND,
        TokenTypes.BAND_ASSIGN,
        TokenTypes.BNOT,
        TokenTypes.BOR,
        TokenTypes.BOR_ASSIGN,
        TokenTypes.BSR,
        TokenTypes.BSR_ASSIGN,
        TokenTypes.BXOR,
        TokenTypes.BXOR_ASSIGN,
        TokenTypes.CASE_GROUP,
        TokenTypes.DEC,
        TokenTypes.DIV,
        TokenTypes.DIV_ASSIGN,
        TokenTypes.DO_WHILE,
        TokenTypes.EQUAL,
        TokenTypes.FOR_CONDITION,
        TokenTypes.FOR_EACH_CLAUSE,
        TokenTypes.FOR_ITERATOR,
        TokenTypes.GE,
        TokenTypes.GT,
        TokenTypes.INC,
        TokenTypes.LAND,
        TokenTypes.LE,
        TokenTypes.LITERAL_BREAK,
        TokenTypes.LITERAL_CASE,
        TokenTypes.LITERAL_CATCH,
        TokenTypes.LITERAL_CONTINUE,
        TokenTypes.LITERAL_DO,
        TokenTypes.LITERAL_FINALLY,
        TokenTypes.LITERAL_FOR,
        TokenTypes.LITERAL_IF,
        TokenTypes.LITERAL_INSTANCEOF,
        TokenTypes.LITERAL_RETURN,
        TokenTypes.LITERAL_SWITCH,
        TokenTypes.LITERAL_THROW,
        TokenTypes.LITERAL_TRY,
        TokenTypes.LITERAL_WHILE,
        TokenTypes.LNOT,
        TokenTypes.LOR,
        TokenTypes.LT,
        TokenTypes.MINUS,
        TokenTypes.MINUS_ASSIGN,
        TokenTypes.MOD,
        TokenTypes.MOD_ASSIGN,
        TokenTypes.NOT_EQUAL,
        TokenTypes.PLUS,
        TokenTypes.PLUS_ASSIGN,
        TokenTypes.POST_DEC,
        TokenTypes.POST_INC,
        TokenTypes.QUESTION,
        TokenTypes.SL,
        TokenTypes.SL_ASSIGN,
        TokenTypes.SR,
        TokenTypes.SR_ASSIGN,
        TokenTypes.STAR,
        TokenTypes.STAR_ASSIGN,
        TokenTypes.UNARY_MINUS,
        TokenTypes.UNARY_PLUS,
        
        TokenTypes.IDENT
            
        };
    }

    public void visitToken(DetailAST aAST)
    {

        switch (aAST.getType()) {
        	case TokenTypes.IDENT:
            	if (operandSet != null
            			&& aAST.getParent() != null
            			&& aAST.getParent().getType() != TokenTypes.TYPE
            			&& aAST.getParent().getType() != TokenTypes.METHOD_DEF
            			&& aAST.getParent().getType() != TokenTypes.METHOD_CALL
            			&& aAST.getPreviousSibling() != null
            			&& aAST.getPreviousSibling().getType() != TokenTypes.METHOD_CALL) {
            		operandSet.add(aAST.getText());
            		nbTotalOperand++;
            	}
            	break;
            case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
            	if (TokenTypes.LITERAL_NEW != aAST.getParent().getParent().getType()) {
            		visitMethodDef(aAST);
            	}
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.ENUM_DEF:
            case TokenTypes.INTERFACE_DEF:
                visitClassDef(aAST);
                break;
            case TokenTypes.LITERAL_WHILE:
            case TokenTypes.LITERAL_DO:
            case TokenTypes.LITERAL_FOR:
            case TokenTypes.LITERAL_IF:
            case TokenTypes.LITERAL_CASE:
            case TokenTypes.QUESTION:
            case TokenTypes.LAND:
            case TokenTypes.LOR:
            case TokenTypes.METHOD_CALL:
            	if(aAST.getType() == TokenTypes.LITERAL_IF ){
            		tempMaxMcd=0;
            		inIf=true;
            	}
                visitKeywordDef(aAST);
            default:
            	checkForOperators(aAST);
                visitTokenHook(aAST);
        }
    }
    
    private void checkForOperators(DetailAST aAST) {
        switch (aAST.getType()) {
        case TokenTypes.ASSIGN:
        case TokenTypes.BAND:
        case TokenTypes.BAND_ASSIGN:
        case TokenTypes.BNOT:
        case TokenTypes.BOR:
        case TokenTypes.BOR_ASSIGN:
        case TokenTypes.BSR:
        case TokenTypes.BSR_ASSIGN:
        case TokenTypes.BXOR:
        case TokenTypes.BXOR_ASSIGN:
        case TokenTypes.CASE_GROUP:
        case TokenTypes.DEC:
        case TokenTypes.DIV:
        case TokenTypes.DIV_ASSIGN:
        case TokenTypes.DO_WHILE:
        case TokenTypes.EQUAL:
        case TokenTypes.FOR_CONDITION:
        case TokenTypes.FOR_EACH_CLAUSE:
        case TokenTypes.FOR_ITERATOR:
        case TokenTypes.GE:
        case TokenTypes.GT:
        case TokenTypes.INC:
        case TokenTypes.LAND:
        case TokenTypes.LE:
        case TokenTypes.LITERAL_BREAK:
        case TokenTypes.LITERAL_CASE:
        case TokenTypes.LITERAL_CATCH:
        case TokenTypes.LITERAL_CONTINUE:
        case TokenTypes.LITERAL_DO:
        case TokenTypes.LITERAL_FINALLY:
        case TokenTypes.LITERAL_FOR:
        case TokenTypes.LITERAL_IF:
        case TokenTypes.LITERAL_INSTANCEOF:
        case TokenTypes.LITERAL_RETURN:
        case TokenTypes.LITERAL_SWITCH:
        case TokenTypes.LITERAL_THROW:
        case TokenTypes.LITERAL_TRY:
        case TokenTypes.LITERAL_WHILE:
        case TokenTypes.LNOT:
        case TokenTypes.LOR:
        case TokenTypes.LT:
        case TokenTypes.MINUS:
        case TokenTypes.MINUS_ASSIGN:
        case TokenTypes.MOD:
        case TokenTypes.MOD_ASSIGN:
        case TokenTypes.NOT_EQUAL:
        case TokenTypes.PLUS:
        case TokenTypes.PLUS_ASSIGN:
        case TokenTypes.POST_DEC:
        case TokenTypes.POST_INC:
        case TokenTypes.QUESTION:
        case TokenTypes.SL:
        case TokenTypes.SL_ASSIGN:
        case TokenTypes.SR:
        case TokenTypes.SR_ASSIGN:
        case TokenTypes.STAR:
        case TokenTypes.STAR_ASSIGN:
        case TokenTypes.UNARY_MINUS:
        case TokenTypes.UNARY_PLUS:
        	this.nbTotalOperator++;
        	this.operatorSet.add(aAST.getText());
        	break;
    	default:
        }
    }

    public void leaveToken(DetailAST aAST)
    {
    	try{
        switch (aAST.getType()) {
	        case TokenTypes.CTOR_DEF:
            case TokenTypes.METHOD_DEF:
            	if (TokenTypes.LITERAL_NEW != aAST.getParent().getParent().getType()) {
            		leaveMethodDef(aAST);
            	}
                break;
            case TokenTypes.CLASS_DEF:
            case TokenTypes.ENUM_DEF:
            case TokenTypes.INTERFACE_DEF:
                leaveClassDef(aAST);
                break;
            case TokenTypes.LITERAL_WHILE:
            case TokenTypes.LITERAL_DO:
            case TokenTypes.LITERAL_FOR:
            case TokenTypes.LITERAL_IF:
            case TokenTypes.LITERAL_CASE:
            case TokenTypes.QUESTION:
            case TokenTypes.LAND:
            case TokenTypes.LOR:
            case TokenTypes.METHOD_CALL:
            	 
            	if(aAST.getType()==TokenTypes.LITERAL_IF){
            		tempMaxMcd=0;
            		inIf=false;
            	}
            	if(inIf){
            		if(aAST.getType()==TokenTypes.LAND || aAST.getType()==TokenTypes.LOR){
            			tempMaxMcd++;
            			if(tempMaxMcd>maxMcd){
                        	maxMcd=tempMaxMcd;
                        }
            		}
            	}
                leaveKeywordDef();
            default:
                leaveTokenHook(aAST);
        }
    	}catch(Exception e){
        	//System.err.println("erreur dans leaveMethod def");
        }
    }

    public void visitMethodDef(DetailAST aAST){
        vg=1;
        this.fanout=0;
    	this.operandSet = new HashSet<String>();
    	this.nbTotalOperand = 0;
    	this.nbTotalOperator = 0;
    	this.operatorSet = new HashSet<String>();
    }

    public void visitClassDef(DetailAST aAST){
    }

    public void visitKeywordDef(DetailAST aAST){
        if(aAST.getType() == TokenTypes.METHOD_CALL){
            fanout++;
        }else{
            vg=vg+1;
        }
    }

    protected void visitTokenHook(DetailAST aAST)
    {
    }

    public void leaveMethodDef(DetailAST aAST){
        String type="";
        String visu="";
        int nl=0;
        int vg=1;
        int evg=1;
        int ivg=1;
        try{
        //nl. Attention, il faut tester la présence d'un corps dans la méthode, au cas où.
        if(aAST.findFirstToken(TokenTypes.SLIST)!=null){
            nl=aAST.findFirstToken(TokenTypes.SLIST).findFirstToken(TokenTypes.RCURLY).getLineNo()-aAST.getLineNo()+1;
        }else{
            nl=0;
        }
        this.currentMEthod=aAST;
        //visibility
        if(Utils.isPublic(aAST)){
            visu="Public";
        }else{
            if(aAST.findFirstToken(TokenTypes.MODIFIERS).findFirstToken(TokenTypes.LITERAL_PRIVATE) != null){
                visu="Private";
            }else{
                visu="Protected";
            }
        }
        if(Utils.isAbstract(aAST)) {
            type="Abstract";
        }else{
            type="Normal";
        }
        loc=getLoc(aAST);
        cloc=getCloc(aAST);
        startLine=aAST.getLineNo();
        
        BasicNode baseNode= new BasicNode("", aAST.getLineNo());
        List<Node> astGraph = expl(aAST.findFirstToken(TokenTypes.SLIST), null, true);
        baseNode.setContent(astGraph);
        
        vg += baseNode.calculateCyclomatic();
        
        Node reducedGraph = baseNode.getReducedGraph(DestructuringNode.class); 
        if (reducedGraph != null) {
        	evg += reducedGraph.calculateCyclomatic();
        }
        reducedGraph = baseNode.getReducedGraph(MethodCallNode.class); 
        if (reducedGraph != null) {
        	ivg += reducedGraph.calculateCyclomatic();
        }
        
        double halOperand = this.operandSet.size();
        double halOperator = this.operatorSet.size();
        double halTotalOperand = nbTotalOperand;
        double halTotalOperator = this.nbTotalOperator;
        
        double halLength = halTotalOperand + halTotalOperator;
        double halVocabulary = halOperand + halOperator;
        double halVolume = 0.0;
        if (halVocabulary != 0.0) {
        	halVolume = halLength * log2(halVocabulary);
        }
        double halDifficulty = 0.0;
        if (halOperand != 0.0) {
        	halDifficulty = (halOperator/2) * (halTotalOperand/halOperand);
        }
        double halEffort = 0.0;
        if (halVolume != 0.0) {
        	halEffort = halDifficulty * halVolume;
        }
        
		String pkgName = this.getPackageName(this.currentMEthod);
		String className = this.getClassName(this.currentMEthod);
		if (pkgName != null && pkgName.length() > 0) {
			className = pkgName + "." + className;
		}
		String methodName = this.getCurrentMethodName(className);
		
	    NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
	    nf.setMaximumFractionDigits(2);
	    nf.setMinimumFractionDigits(2);
	    nf.setGroupingUsed(false);
        
        log(aAST,"<elt type=\"MET\" name=\""+methodName+"\" startLine=\""+this.startLine+"\" subtype=\""+type+"\" visibility=\""+visu+"\">\n"+
                "<parent name=\""+className+"\"/>\n"+
                "<metric id=\"loc\" value=\""+this.loc+"\"/>\n"+
                "<metric id=\"vg\" value=\""+vg+"\"/>\n"+
                "<metric id=\"cloc\" value=\""+this.cloc+"\"/>\n"+
                "<metric id=\"nl\" value=\""+nl+"\"/>\n"+
                "<metric id=\"evg\" value=\""+evg+"\"/>\n"+
                "<metric id=\"ivg\" value=\""+ivg+"\"/>\n"+
                "<metric id=\"fanout\" value=\""+this.getFanout()+"\"/>\n"+
                "<metric id=\"maxMcd\" value=\""+maxMcd+"\"/>\n"+
                "<metric id=\"UNIQUE_OPERANDS\" value=\""+nf.format(halOperand)+"\"/>\n"+
                "<metric id=\"UNIQUE_OPERATORS\" value=\""+nf.format(halOperator)+"\"/>\n"+
                "<metric id=\"OPERAND_COUNT\" value=\""+nf.format(halTotalOperand)+"\"/>\n"+
                "<metric id=\"OPERATOR_COUNT\" value=\""+nf.format(halTotalOperator)+"\"/>\n"+
                "<metric id=\"HAL_LENGTH\" value=\""+ nf.format(halLength) +"\"/>\n"+
                "<metric id=\"HAL_VOCABULARY\" value=\""+ nf.format(halVocabulary) +"\"/>\n"+
                "<metric id=\"HAL_VOLUME\" value=\""+ nf.format(halVolume) +"\"/>\n"+
                "<metric id=\"HAL_DIFFICULTY\" value=\""+ nf.format(halDifficulty) +"\"/>\n"+
                "<metric id=\"HAL_EFFORT\" value=\""+ nf.format(halEffort) +"\"/>\n"+
                "</elt>\n");
        maxMcd=0;
        }
        catch (RuntimeException e) {
        	System.err.println(this.getCurrentMethodName("")+" : erreur dans leaveMethod def");
        	e.printStackTrace(System.err);
        }
        catch (Exception e) {
        	System.err.println(this.getCurrentMethodName("")+" : erreur dans leaveMethod def");
        	e.printStackTrace(System.err);
        }
        catch (Throwable e) {
        	System.err.println(this.getCurrentMethodName("")+" : erreur dans leaveMethod def");
        	e.printStackTrace(System.err);
        }
    }

    public static double log2(double d) {
        return Math.log(d)/Math.log(2.0);
    }
    
    private int getFanout(){
        return fanout;
    }
    
    private List<Node> expl(DetailAST ast, Node parentNode, boolean doGetFirstChild){
    	List<Node> result = new ArrayList<Node>();
    	if (ast != null) {
	        DetailAST tempAst=ast;
	        if (doGetFirstChild) {
	        	tempAst = (DetailAST)tempAst.getFirstChild();
	        }
	        Node currentNode = null;
	        for(; tempAst != null; tempAst = (DetailAST)tempAst.getNextSibling()) {
	            if(isKeyWord(tempAst)){
	                int nbLogicalOp = countLogicalOp(tempAst);
	                currentNode = new CyclomaticFlowNode(tempAst.getText(), tempAst.getLineNo(), nbLogicalOp);
	                DetailAST contentAst = skipExpression(tempAst);
	                if (!tempAst.equals(contentAst)) {
	                	currentNode.setContent(expl(contentAst, currentNode, false));
	                }
	                result.add(currentNode);
	            }
	            else if(isElseKeyWord(tempAst)){
	            	((CyclomaticFlowNode)parentNode).setElseContent(expl(tempAst, null, true));
	            }
	            else if(isOutKeyWordEvg(tempAst)){
                	currentNode = new DestructuringNode(tempAst.getText(), tempAst.getLineNo());
	                currentNode.setContent(expl(tempAst, currentNode, true));
                	result.add(currentNode);
                }
                else {
                	if(isOutKeyWordIvg(tempAst)){
	                	currentNode = new MethodCallNode(tempAst.getText(), tempAst.getLineNo());
	                	result.add(currentNode);
                	}
                	result.addAll(expl(tempAst, null, true));
                }
	        }
        }
    	return result;
    }

    private DetailAST skipExpression(DetailAST ast) {
    	DetailAST result = ast;
    	if (ast.getFirstChild() != null
    			&& ast.getFirstChild().getNextSibling() != null
    			&& ast.getFirstChild().getNextSibling().getNextSibling() != null) {
    		result = (DetailAST)ast.getFirstChild().getNextSibling().getNextSibling().getNextSibling();
    	}
    	return result;
    }
    
    private boolean isKeyWord(DetailAST mt){
     if(mt.getType() == TokenTypes.LITERAL_IF  ||
                mt.getType() == TokenTypes.LITERAL_FOR  ||
                mt.getType() == TokenTypes.LITERAL_WHILE ||
                mt.getType() == TokenTypes.LITERAL_CASE ||
                mt.getType() == TokenTypes.QUESTION){
            return true;
        }else{
            return false;
        }
    }

    private boolean isElseKeyWord(DetailAST mt){
        if(mt.getType() == TokenTypes.LITERAL_ELSE){
            return true;
        }else{
            return false;
        }
    }
    
    private int countLogicalOp(DetailAST mt) {
    	int result = 0;
    	DetailAST tempAst = (DetailAST)mt.getFirstChild();
    	if (tempAst != null && (tempAst.getType() == TokenTypes.LPAREN)) {
    		tempAst = (DetailAST)tempAst.getNextSibling();
    		if (tempAst.getType() == TokenTypes.EXPR) {
    			tempAst = (DetailAST)tempAst.getFirstChild();
    			DetailAST template = new DetailAST();
    			template.setType(TokenTypes.LOR);
    			template.setText("||");
    			ASTEnumeration astEnum = tempAst.findAllPartial(template);
    			while (astEnum.hasMoreNodes()) {
    				astEnum.nextNode();
    				result++;
    			}
    		}
    	}
    	return result;
    }
    
    private boolean isOutKeyWordIvg(DetailAST mt){
        if(mt.getType() == TokenTypes.METHOD_CALL
		|| mt.getType() == TokenTypes.LITERAL_NEW){
            return true;
        }else{
            return false;
        }
    }
    
    private boolean isOutKeyWordEvg(DetailAST mt){
        if(mt.getType() == TokenTypes.LITERAL_RETURN ||
            mt.getType() == TokenTypes.LITERAL_THROW ||
            mt.getType() == TokenTypes.LITERAL_BREAK ||
            mt.getType() == TokenTypes.LITERAL_CONTINUE) {
            return true;
        }else{
            return false;
        }
    }


    public void leaveClassDef(DetailAST aAST){
    }

    public void leaveKeywordDef(){
    }

    protected void leaveTokenHook(DetailAST aAST)
    {
    }

    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    public int getLoc(DetailAST aAST){
        //pattern d'un comment sur une ligne
        Pattern fullSlashComment=Pattern.compile("^\\s*/\\*.*\\*/.*$") ;
        //pattern d'un comment commencant au début d'une ligne
        Pattern BeginSlashComment=Pattern.compile("^\\s*/\\*.*$") ;
        //pattern d'un comment commencant au milieu d'une ligne
        Pattern MiddleSlashComment=Pattern.compile("^\\s[a-zA-Z]+.*/\\*.*$") ;
        //pattern repérant la fin d'un comment sans rien après
        Pattern FullEndSlashComment=Pattern.compile("^.*\\*/\\s*$") ;
        //pattern repérant la fin d'un comment avec du code après
        Pattern partialEndSlashComment=Pattern.compile("^.*\\*/.*[a-zA-Z]+.*$") ;
        final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST);

        //indice indiquant si on est dans une zone commentée ou non
        boolean isCommented=false;
        int length=0;

        if (openingBrace != null) {
            final DetailAST closingBrace =
                    openingBrace.findFirstToken(TokenTypes.RCURLY);
            length =
                    closingBrace.getLineNo() - openingBrace.getLineNo() + 1;

            if (length>1) {
                final FileContents contents = getFileContents();
                final int lastLine = closingBrace.getLineNo();
                for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {
                    if (contents.lineIsBlank(i) || contents.lineIsComment(i)) {

                        length--;
                    }else{
                        if(fullSlashComment.matcher(contents.getLines()[i]).matches()) {
                            length--;
                        }
                        else{
                            if(!isCommented){

                                if(BeginSlashComment.matcher(contents.getLines()[i]).matches()){
                                    isCommented=true;
                                    length--;
                                }else{
                                    if(MiddleSlashComment.matcher(contents.getLines()[i]).matches()){
                                        isCommented=true;
                                    }
                                }
                            }else{
                                if(FullEndSlashComment.matcher(contents.getLines()[i]).matches()){
                                    isCommented=false;
                                    length--;
                                }else{
                                    if(partialEndSlashComment.matcher(contents.getLines()[i]).matches()){
                                        isCommented=false;
                                    }else{
                                        length--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return length;
    }

    public int getCloc(DetailAST aAST)
    {

        int count=0;
        int lastLine;
        final FileContents contents = getFileContents();

        try{
            if(aAST.findFirstToken(TokenTypes.SLIST ) != null){
                final DetailAST openingBrace = aAST.findFirstToken(TokenTypes.SLIST );
                if(openingBrace != null){
                    final DetailAST closingBrace =
                            aAST.findFirstToken(TokenTypes.SLIST ).findFirstToken(TokenTypes.RCURLY );
                    lastLine=closingBrace.getLineNo();


                    if(lastLine-openingBrace.getLineNo()>1){
                        for (int i = openingBrace.getLineNo() - 1; i < lastLine; i++) {

                            if(contents.hasIntersectionWithComment(i,0,i,1000) ){
                                count++;
                            };

                        }
                    }else{
                        count=0;
                    }
                }else{
                    count=0;
                }
            }else{
                count=0;
            }
        }catch(Exception e){
            //System.err.println("erreur dans method metrics");
        }
        return count;
    }

    protected final String getParameters(){

        DetailAST temp0;
        DetailAST parameterDefAST;
        String p="";
        String type="";
        if(this.currentMEthod.findFirstToken(TokenTypes.PARAMETERS) != null){
            temp0= this.currentMEthod.findFirstToken(TokenTypes.PARAMETERS );

            parameterDefAST= temp0.findFirstToken(TokenTypes.PARAMETER_DEF );

            for (; parameterDefAST != null;
                 parameterDefAST = (DetailAST) parameterDefAST.getNextSibling())

            {
                if (parameterDefAST.getType() == TokenTypes.PARAMETER_DEF) {

                    final DetailAST paramType=
                            parameterDefAST.findFirstToken(TokenTypes.TYPE).getLastChild();


                    final FullIdent full;
                    if(paramType.getType() == TokenTypes.ARRAY_DECLARATOR ){
                        DetailAST temp=paramType;
                        type="[]";

                        while(temp.findFirstToken(TokenTypes.ARRAY_DECLARATOR) != null){
                            type=type+"[]";
                            temp=temp.findFirstToken(TokenTypes.ARRAY_DECLARATOR);
                        }

                        full = FullIdent.createFullIdent((DetailAST)temp.getFirstChild());
                        type=full.getText()+type;
                    }else{
                        full = FullIdent.createFullIdent(paramType);
                        type=full.getText();
                    }
                    p=p+","+type;
                }
            }
        }
        return p;
    }


    public static FullIdent createFullType(DetailAST aTypeAST)
    {
        DetailAST arrayDeclAST =
                aTypeAST.findFirstToken(TokenTypes.ARRAY_DECLARATOR);

        return createFullTypeNoArrays(arrayDeclAST == null ? aTypeAST
                : arrayDeclAST);
    }


    /*
    * @param aTypeAST a type node (no array)
    * @return <code>FullIdent</code> for given type.
    */
    private static FullIdent createFullTypeNoArrays(DetailAST aTypeAST)
    {
        return FullIdent.createFullIdent((DetailAST) aTypeAST.getFirstChild());
    }

    protected final String getCurrentMethodName(String className){

        String tabParam;
        String parameters;
        tabParam= getParameters();
        parameters="(";
        try{
            if(tabParam.length() >2){
                parameters= parameters+tabParam.substring(1) ;
            }   else{
                parameters= parameters+tabParam;
            }
            parameters=parameters+")";
        }catch(Exception e){
            //System.err.println(this.currentMEthod+ " : erreur dans getCurrentMethodName() ");
        }

        return className+"."+this.currentMEthod.findFirstToken(TokenTypes.IDENT).getText()+parameters ;
    }

    public DetailAST setClassLevel(DetailAST c){
        while(c != null && c.getType()!=TokenTypes.ENUM_DEF && c.getType() != TokenTypes.CLASS_DEF && c.getType() != TokenTypes.INTERFACE_DEF && c.getParent() != null){
            c=c.getParent();
        }
        return c;
    }
    
	public String getClassName(DetailAST d){
		DetailAST mycurrentClass= setClassLevel(d);
		String className= mycurrentClass.findFirstToken(TokenTypes.IDENT).getText();
		
		if((mycurrentClass.getParent()) != null && mycurrentClass.getParent().getType() != 0){
			while((mycurrentClass.getPreviousSibling()) == null ){
				mycurrentClass=mycurrentClass.getParent();
			}
		}
		while((mycurrentClass=mycurrentClass.getPreviousSibling()) != null ){          
			while(mycurrentClass.getParent() != null || mycurrentClass.getPreviousSibling() != null) {
				if(mycurrentClass.getParent() != null) {
					mycurrentClass=mycurrentClass.getParent();
					
				}else{
					mycurrentClass=mycurrentClass.getPreviousSibling();
					
				}
				if(mycurrentClass.getType() == TokenTypes.CLASS_DEF){
					
					className = mycurrentClass.findFirstToken(TokenTypes.IDENT).getText() + "#" + className;
				}
				if(mycurrentClass.getType() == TokenTypes.PACKAGE_DEF ){
					//className=FullIdent.createFullIdent((DetailAST)mycurrentClass.getFirstChild().getNextSibling()).getText()+"."+className;
				}
			}
		}
		
		return className;
		
	}    
    /*public String getClassName(DetailAST d){

        this.currentClass= setClassLevel(d);
        String className= setClassLevel(d).findFirstToken(TokenTypes.IDENT).getText();

        while((this.currentClass=this.currentClass.getPreviousSibling()) != null){
            if(this.currentClass.getParent() != null) this.currentClass=this.currentClass.getParent();
            if(this.currentClass.getType() == TokenTypes.CLASS_DEF){
            	
            	className = this.currentClass.findFirstToken(TokenTypes.IDENT).getText() + "#" + className;
            }
            if(this.currentClass.getType() == TokenTypes.PACKAGE_DEF ){
                className=FullIdent.createFullIdent((DetailAST)this.currentClass.getFirstChild().getNextSibling()).getText()+"."+className;
            }
        }
        return className;
    }*/
    
	public String getPackageName(DetailAST d){
		
		DetailAST tmpCurrentClass= setClassLevel(d);
		String className= setClassLevel(d).findFirstToken(TokenTypes.IDENT).getText();
		
		while(tmpCurrentClass.getPreviousSibling()!= null || tmpCurrentClass.getParent()!= null ){          
			if(tmpCurrentClass.getParent() != null) tmpCurrentClass=tmpCurrentClass.getParent();
			if(tmpCurrentClass.getPreviousSibling() != null) tmpCurrentClass=tmpCurrentClass.getPreviousSibling();
			if(tmpCurrentClass.getType() == TokenTypes.PACKAGE_DEF ){
				className=FullIdent.createFullIdent((DetailAST)tmpCurrentClass.getFirstChild().getNextSibling()).getText();
			}
		}
		return className;
	}   
    
}

