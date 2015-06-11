﻿/* Generated By:JJTree&JavaCC: Do not edit this line. JspParserConstants.java */
/** 
 * JSP Parser for PMD.
 * @author Pieter � Application Engineers NV/SA � http://www.ae.be
 */

package com.compuware.caqs.pmd.jsp.ast;

public interface JspParserConstants {

  int EOF = 0;
  int ALPHA_CHAR = 1;
  int NUM_CHAR = 2;
  int ALPHANUM_CHAR = 3;
  int IDENTIFIER_CHAR = 4;
  int IDENTIFIER = 5;
  int XMLNAME = 6;
  int QUOTED_STRING_NO_BREAKS = 7;
  int QUOTED_STRING = 8;
  int WHITESPACE = 9;
  int NEWLINE = 10;
  int QUOTE = 11;
  int NO_WHITESPACE_OR_LT_OR_DOLLAR = 12;
  int NO_LT_OR_DOLLAR = 13;
  int DOLLAR = 14;
  int NO_OPENBRACE_OR_LT = 15;
  int TEXT_IN_EL = 16;
  int EL_ESCAPE = 17;
  int NO_JSP_COMMENT_END = 18;
  int NO_JSP_TAG_END = 19;
  int TAG_START = 22;
  int ENDTAG_START = 23;
  int COMMENT_START = 24;
  int DECL_START = 25;
  int DOCTYPE_DECL_START = 26;
  int CDATA_START = 27;
  int JSP_COMMENT_START = 28;
  int JSP_DECLARATION_START = 29;
  int JSP_EXPRESSION_START = 30;
  int JSP_SCRIPTLET_START = 31;
  int JSP_DIRECTIVE_START = 32;
  int EL_EXPRESSION = 33;
  int UNPARSED_TEXT = 34;
  int JSP_DIRECTIVE_NAME = 35;
  int JSP_DIRECTIVE_ATTRIBUTE_NAME = 36;
  int JSP_DIRECTIVE_ATTRIBUTE_EQUALS = 37;
  int JSP_DIRECTIVE_ATTRIBUTE_VALUE = 38;
  int JSP_DIRECTIVE_END = 39;
  int JSP_SCRIPTLET_END = 40;
  int JSP_SCRIPTLET = 41;
  int JSP_EXPRESSION_END = 42;
  int JSP_EXPRESSION = 43;
  int JSP_DECLARATION_END = 44;
  int JSP_DECLARATION = 45;
  int JSP_COMMENT_END = 46;
  int JSP_COMMENT_CONTENT = 47;
  int WHITESPACES = 48;
  int NAME = 49;
  int PUBLIC = 50;
  int SYSTEM = 51;
  int DOCTYPE_DECL_END = 52;
  int QUOTED_LITERAL = 53;
  int UNPARSED = 54;
  int CDATA_END = 55;
  int TAG_NAME = 56;
  int LST_ERROR = 57;
  int ATTR_NAME = 58;
  int TAG_END = 59;
  int DECL_END = 60;
  int TAG_SLASHEND = 61;
  int ATTR_EQ = 62;
  int IN_TAG_ERROR = 63;
  int SINGLE_QUOTE = 64;
  int DOUBLE_QUOTE = 65;
  int EL_EXPRESSION_IN_ATTRIBUTE = 66;
  int VALUE_BINDING_IN_ATTRIBUTE = 67;
  int JSP_EXPRESSION_IN_ATTRIBUTE = 68;
  int ENDING_SINGLE_QUOTE = 69;
  int UNPARSED_TEXT_NO_SINGLE_QUOTES = 70;
  int DOLLAR_OR_HASH_SINGLE_QUOTE = 71;
  int ENDING_DOUBLE_QUOTE = 72;
  int UNPARSED_TEXT_NO_DOUBLE_QUOTES = 73;
  int DOLLAR_OR_HASH_DOUBLE_QUOTE = 74;
  int COMMENT_END = 75;
  int COMMENT_TEXT = 76;

  int CommentState = 0;
  int AttrValueBetweenDoubleQuotesState = 1;
  int AttrValueBetweenSingleQuotesState = 2;
  int StartTagState = 3;
  int CDataState = 4;
  int DocTypeExternalIdState = 5;
  int DocTypeState = 6;
  int JspCommentState = 7;
  int JspDeclarationState = 8;
  int JspExpressionState = 9;
  int JspScriptletState = 10;
  int InTagState = 11;
  int AfterTagState = 12;
  int AttrValueState = 13;
  int JspDirectiveAttributesState = 14;
  int JspDirectiveState = 15;
  int DEFAULT = 16;

  String[] tokenImage = {
    "<EOF>",
    "<ALPHA_CHAR>",
    "<NUM_CHAR>",
    "<ALPHANUM_CHAR>",
    "<IDENTIFIER_CHAR>",
    "<IDENTIFIER>",
    "<XMLNAME>",
    "<QUOTED_STRING_NO_BREAKS>",
    "<QUOTED_STRING>",
    "<WHITESPACE>",
    "<NEWLINE>",
    "<QUOTE>",
    "<NO_WHITESPACE_OR_LT_OR_DOLLAR>",
    "<NO_LT_OR_DOLLAR>",
    "\"$\"",
    "<NO_OPENBRACE_OR_LT>",
    "<TEXT_IN_EL>",
    "<EL_ESCAPE>",
    "<NO_JSP_COMMENT_END>",
    "<NO_JSP_TAG_END>",
    "<token of kind 20>",
    "<token of kind 21>",
    "\"<\"",
    "\"</\"",
    "\"<!--\"",
    "\"<?\"",
    "\"<!DOCTYPE\"",
    "\"<![CDATA[\"",
    "\"<%--\"",
    "\"<%!\"",
    "\"<%=\"",
    "\"<%\"",
    "\"<%@\"",
    "<EL_EXPRESSION>",
    "<UNPARSED_TEXT>",
    "<JSP_DIRECTIVE_NAME>",
    "<JSP_DIRECTIVE_ATTRIBUTE_NAME>",
    "\"=\"",
    "<JSP_DIRECTIVE_ATTRIBUTE_VALUE>",
    "\"%>\"",
    "\"%>\"",
    "<JSP_SCRIPTLET>",
    "\"%>\"",
    "<JSP_EXPRESSION>",
    "\"%>\"",
    "<JSP_DECLARATION>",
    "\"--%>\"",
    "<JSP_COMMENT_CONTENT>",
    "<WHITESPACES>",
    "<NAME>",
    "\"PUBLIC\"",
    "\"SYSTEM\"",
    "\">\"",
    "<QUOTED_LITERAL>",
    "<UNPARSED>",
    "\"]]>\"",
    "<TAG_NAME>",
    "<LST_ERROR>",
    "<ATTR_NAME>",
    "\">\"",
    "<DECL_END>",
    "\"/>\"",
    "\"=\"",
    "<IN_TAG_ERROR>",
    "\"\\\'\"",
    "\"\\\"\"",
    "<EL_EXPRESSION_IN_ATTRIBUTE>",
    "<VALUE_BINDING_IN_ATTRIBUTE>",
    "<JSP_EXPRESSION_IN_ATTRIBUTE>",
    "\"\\\'\"",
    "<UNPARSED_TEXT_NO_SINGLE_QUOTES>",
    "<DOLLAR_OR_HASH_SINGLE_QUOTE>",
    "\"\\\"\"",
    "<UNPARSED_TEXT_NO_DOUBLE_QUOTES>",
    "<DOLLAR_OR_HASH_DOUBLE_QUOTE>",
    "<COMMENT_END>",
    "<COMMENT_TEXT>",
  };

}
