package com.compuware.carscode.ant.taskdef;

public class Constants {

	public static final String OS_NAME = System.getProperty("os.name");
	
	public static final String OS_SPECIFIC_BATCH_EXTENSION = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? ".bat" : ".sh";
	
	public static final String OS_SPECIFIC_DOUBLE_QUOTE = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? "\"" : "";
	
	public static final String OS_SPECIFIC_SPACE = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? " " : "\\\\ ";

	public static final String OS_SPECIFIC_SHELL = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? "cmd" : "sh";
	
	public static final String OS_SPECIFIC_SHELL_OPTION = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? "/c" : "-c";
	
}
