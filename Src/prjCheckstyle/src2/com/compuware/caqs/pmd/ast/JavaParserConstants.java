/* Generated By:JJTree&JavaCC: Do not edit this line. JavaParserConstants.java */
package com.compuware.caqs.pmd.ast;

public interface JavaParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 6;
  int FORMAL_COMMENT = 9;
  int MULTI_LINE_COMMENT = 10;
  int ABSTRACT = 12;
  int BOOLEAN = 13;
  int BREAK = 14;
  int BYTE = 15;
  int CASE = 16;
  int CATCH = 17;
  int CHAR = 18;
  int CLASS = 19;
  int CONST = 20;
  int CONTINUE = 21;
  int _DEFAULT = 22;
  int DO = 23;
  int DOUBLE = 24;
  int ELSE = 25;
  int EXTENDS = 26;
  int FALSE = 27;
  int FINAL = 28;
  int FINALLY = 29;
  int FLOAT = 30;
  int FOR = 31;
  int GOTO = 32;
  int IF = 33;
  int IMPLEMENTS = 34;
  int IMPORT = 35;
  int INSTANCEOF = 36;
  int INT = 37;
  int INTERFACE = 38;
  int LONG = 39;
  int NATIVE = 40;
  int NEW = 41;
  int NULL = 42;
  int PACKAGE = 43;
  int PRIVATE = 44;
  int PROTECTED = 45;
  int PUBLIC = 46;
  int RETURN = 47;
  int SHORT = 48;
  int STATIC = 49;
  int SUPER = 50;
  int SWITCH = 51;
  int SYNCHRONIZED = 52;
  int THIS = 53;
  int THROW = 54;
  int THROWS = 55;
  int TRANSIENT = 56;
  int TRUE = 57;
  int TRY = 58;
  int VOID = 59;
  int VOLATILE = 60;
  int WHILE = 61;
  int STRICTFP = 62;
  int INTEGER_LITERAL = 63;
  int DECIMAL_LITERAL = 64;
  int HEX_LITERAL = 65;
  int OCTAL_LITERAL = 66;
  int FLOATING_POINT_LITERAL = 67;
  int HEX_FLOATING_POINT_LITERAL = 68;
  int EXPONENT = 69;
  int CHARACTER_LITERAL = 70;
  int STRING_LITERAL = 71;
  int IDENTIFIER = 72;
  int LETTER = 73;
  int PART_LETTER = 74;
  int LPAREN = 75;
  int RPAREN = 76;
  int LBRACE = 77;
  int RBRACE = 78;
  int LBRACKET = 79;
  int RBRACKET = 80;
  int SEMICOLON = 81;
  int COMMA = 82;
  int DOT = 83;
  int AT = 84;
  int ASSIGN = 85;
  int LT = 86;
  int BANG = 87;
  int TILDE = 88;
  int HOOK = 89;
  int COLON = 90;
  int EQ = 91;
  int LE = 92;
  int GE = 93;
  int NE = 94;
  int SC_OR = 95;
  int SC_AND = 96;
  int INCR = 97;
  int DECR = 98;
  int PLUS = 99;
  int MINUS = 100;
  int STAR = 101;
  int SLASH = 102;
  int BIT_AND = 103;
  int BIT_OR = 104;
  int XOR = 105;
  int REM = 106;
  int LSHIFT = 107;
  int PLUSASSIGN = 108;
  int MINUSASSIGN = 109;
  int STARASSIGN = 110;
  int SLASHASSIGN = 111;
  int ANDASSIGN = 112;
  int ORASSIGN = 113;
  int XORASSIGN = 114;
  int REMASSIGN = 115;
  int LSHIFTASSIGN = 116;
  int RSIGNEDSHIFTASSIGN = 117;
  int RUNSIGNEDSHIFTASSIGN = 118;
  int ELLIPSIS = 119;
  int RUNSIGNEDSHIFT = 120;
  int RSIGNEDSHIFT = 121;
  int GT = 122;

  int DEFAULT = 0;
  int IN_FORMAL_COMMENT = 1;
  int IN_MULTI_LINE_COMMENT = 2;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<token of kind 7>",
    "\"/*\"",
    "\"*/\"",
    "\"*/\"",
    "<token of kind 11>",
    "\"abstract\"",
    "\"boolean\"",
    "\"break\"",
    "\"byte\"",
    "\"case\"",
    "\"catch\"",
    "\"char\"",
    "\"class\"",
    "\"const\"",
    "\"continue\"",
    "\"default\"",
    "\"do\"",
    "\"double\"",
    "\"else\"",
    "\"extends\"",
    "\"false\"",
    "\"final\"",
    "\"finally\"",
    "\"float\"",
    "\"for\"",
    "\"goto\"",
    "\"if\"",
    "\"implements\"",
    "\"import\"",
    "\"instanceof\"",
    "\"int\"",
    "\"interface\"",
    "\"long\"",
    "\"native\"",
    "\"new\"",
    "\"null\"",
    "\"package\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"return\"",
    "\"short\"",
    "\"static\"",
    "\"super\"",
    "\"switch\"",
    "\"synchronized\"",
    "\"this\"",
    "\"throw\"",
    "\"throws\"",
    "\"transient\"",
    "\"true\"",
    "\"try\"",
    "\"void\"",
    "\"volatile\"",
    "\"while\"",
    "\"strictfp\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<HEX_FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<PART_LETTER>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"@\"",
    "\"=\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"%\"",
    "\"<<\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
    "\"...\"",
    "\">>>\"",
    "\">>\"",
    "\">\"",
    "\"\\u001a\"",
    "\"~[]\"",
  };

}
