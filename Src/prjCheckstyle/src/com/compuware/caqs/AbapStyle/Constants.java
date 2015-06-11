package com.compuware.caqs.AbapStyle;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-kbigelow
 * Date: 30 mars 2006
 * Time: 15:10:44
 * To change this template use File | Settings | File Templates.
 */
public class Constants {
	public static final String beginForm="[F|f][O|o][R|r][M|m]";
	public static final String endForm="[E|e][N|n][D|d][F|f][O|o][R|r][M|m]";
	public static final String beginModule="MODULE";
	public static final String endModule="ENDMODULE";
	
	public static final Pattern functionBegin=Pattern.compile("^\\s*("+
																"FORM|Form|form|MODULE|FUNCTION|function|METHOD"+
																
															")\\s+.*$");
	
	public static final Pattern functionEnd=Pattern.compile("^\\s*(endform|ENDFORM|ENDMODULE|ENDFUNCTION|endfunction|ENDMETHOD)\\s*\\..*$");
	
	public static final Pattern blankLine=Pattern.compile("^[\\s|\\t]*$");
	
	public static final Pattern CommentLine=Pattern.compile("^.*[\\*|\\\"].*$");
	
	public static final Pattern sourceCodeLine=Pattern.compile("^[\\s|\\t]*[a-zA-Z]+.*$");
	
	//calcul du vg
	public static final String ifKw							="IF";
	public static final String elseifKw						="ELSEIF";
	public static final String doKw							="DO";
	public static final String untilKw						="UNTIL";
	public static final String accordingKw					="ACCORDING";
	public static final String checkKw						="CHECK";
	public static final String selectKw						="SELECT";
	public static final String loopKw						="LOOP";
	public static final String whileKw						="WHILE";
	public static final String whenKw						="WHEN";
	public static final String onChangeKw					="ON CHANGE OF";
	public static final String atFirstKw					="AT FIRST";
	public static final String atNewKw						="AT NEW";
	public static final String atEndOfKw					="AT END OF";
	public static final String atLastKw						="AT LAST";
	public static final String atSelectionScreenKw			="AT SELECTION-SCREEN";
	public static final String atLineSelectionKw			="AT LINE-SELECTION";
	public static final String atUserCommandKw				="AT USER-COMMAND";
	public static final String topOfPageKw					="TOP-OF-PAGE";
	public static final String endOfPageKw					="END-OF-PAGE";
	public static final String catchSystemExceptionKw		="CATCH SYSTEM-EXCEPTIONS";
	public static final String raiseExceptionKw				="RAISE EXCEPTION";
	public static final String handlersForExceptionsKw		="HANDLERS FOR EXCEPTIONS";
	public static final String systemExceptionKw			="SYSTEM-EXCEPTIONS";  
	public static final String notKw						="NOT";
	public static final String andKw						="AND";
	public static final String orKw							="OR";
	public static final String pipeKw						="\\|"; 
	
	public static final Pattern kw=Pattern.compile("^\\s+(" +
																ifKw+"|"+
																elseifKw+"|"+
																doKw+"|"+
																untilKw+"|"+
																accordingKw+"|"+
																checkKw+"|"+
																selectKw+"|"+
																loopKw+"|"+
																whileKw+"|"+
																whenKw+"|"+
																onChangeKw+"|"+
																atFirstKw+"|"+
																atNewKw+"|"+
																atEndOfKw+"|"+
																atLastKw+"|"+
																atSelectionScreenKw+"|"+
																atLineSelectionKw+"|"+
																atUserCommandKw+"|"+
																topOfPageKw+"|"+
																endOfPageKw+"|"+
																catchSystemExceptionKw+"|"+
																raiseExceptionKw+"|"+
																handlersForExceptionsKw+"|"+
																systemExceptionKw+"|"+
																notKw+"|"+
																andKw+"|"+
																orKw+"|"+
																pipeKw+
														")[\\.]?\\s*.*$");
	
}
