/* Generated By:JavaCC: Do not edit this line. CPPParserConstants.java */
package com.compuware.caqs.CPD.cppast;

public interface CPPParserConstants {

  int EOF = 0;
  int LCURLYBRACE = 16;
  int RCURLYBRACE = 17;
  int LSQUAREBRACKET = 18;
  int RSQUAREBRACKET = 19;
  int LPARENTHESIS = 20;
  int RPARENTHESIS = 21;
  int SCOPE = 22;
  int COLON = 23;
  int SEMICOLON = 24;
  int COMMA = 25;
  int QUESTIONMARK = 26;
  int ELLIPSIS = 27;
  int ASSIGNEQUAL = 28;
  int TIMESEQUAL = 29;
  int DIVIDEEQUAL = 30;
  int MODEQUAL = 31;
  int PLUSEQUAL = 32;
  int MINUSEQUAL = 33;
  int SHIFTLEFTEQUAL = 34;
  int SHIFTRIGHTEQUAL = 35;
  int BITWISEANDEQUAL = 36;
  int BITWISEXOREQUAL = 37;
  int BITWISEOREQUAL = 38;
  int OR = 39;
  int AND = 40;
  int BITWISEOR = 41;
  int BITWISEXOR = 42;
  int AMPERSAND = 43;
  int EQUAL = 44;
  int NOTEQUAL = 45;
  int LESSTHAN = 46;
  int GREATERTHAN = 47;
  int LESSTHANOREQUALTO = 48;
  int GREATERTHANOREQUALTO = 49;
  int SHIFTLEFT = 50;
  int SHIFTRIGHT = 51;
  int PLUS = 52;
  int MINUS = 53;
  int STAR = 54;
  int DIVIDE = 55;
  int MOD = 56;
  int PLUSPLUS = 57;
  int MINUSMINUS = 58;
  int TILDE = 59;
  int NOT = 60;
  int DOT = 61;
  int POINTERTO = 62;
  int DOTSTAR = 63;
  int ARROWSTAR = 64;
  int AUTO = 65;
  int BREAK = 66;
  int CASE = 67;
  int CATCH = 68;
  int CHAR = 69;
  int CONST = 70;
  int CONTINUE = 71;
  int _DEFAULT = 72;
  int DELETE = 73;
  int DO = 74;
  int DOUBLE = 75;
  int ELSE = 76;
  int ENUM = 77;
  int EXTERN = 78;
  int FLOAT = 79;
  int FOR = 80;
  int FRIEND = 81;
  int GOTO = 82;
  int IF = 83;
  int INLINE = 84;
  int INT = 85;
  int LONG = 86;
  int NEW = 87;
  int PRIVATE = 88;
  int PROTECTED = 89;
  int PUBLIC = 90;
  int REDECLARED = 91;
  int REGISTER = 92;
  int RETURN = 93;
  int SHORT = 94;
  int SIGNED = 95;
  int SIZEOF = 96;
  int STATIC = 97;
  int STRUCT = 98;
  int CLASS = 99;
  int SWITCH = 100;
  int TEMPLATE = 101;
  int THIS = 102;
  int TRY = 103;
  int TYPEDEF = 104;
  int UNION = 105;
  int UNSIGNED = 106;
  int VIRTUAL = 107;
  int VOID = 108;
  int VOLATILE = 109;
  int WHILE = 110;
  int OPERATOR = 111;
  int TRUETOK = 112;
  int FALSETOK = 113;
  int THROW = 114;
  int OCTALINT = 115;
  int OCTALLONG = 116;
  int UNSIGNED_OCTALINT = 117;
  int UNSIGNED_OCTALLONG = 118;
  int DECIMALINT = 119;
  int DECIMALLONG = 120;
  int UNSIGNED_DECIMALINT = 121;
  int UNSIGNED_DECIMALLONG = 122;
  int HEXADECIMALINT = 123;
  int HEXADECIMALLONG = 124;
  int UNSIGNED_HEXADECIMALINT = 125;
  int UNSIGNED_HEXADECIMALLONG = 126;
  int FLOATONE = 127;
  int FLOATTWO = 128;
  int CHARACTER = 129;
  int STRING = 130;
  int ID = 132;

  int DEFAULT = 0;
  int IN_LINE_COMMENT = 1;
  int IN_COMMENT = 2;
  int PREPROCESSOR_OUTPUT = 3;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\\n\"",
    "\"\\n\"",
    "\"//\"",
    "\"/*\"",
    "\"#\"",
    "\"\\n\"",
    "<token of kind 9>",
    "\"*/\"",
    "<token of kind 11>",
    "\"\\n\"",
    "\"\\\\\\n\"",
    "\"\\\\\\r\\n\"",
    "<token of kind 15>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\"(\"",
    "\")\"",
    "\"::\"",
    "\":\"",
    "\";\"",
    "\",\"",
    "\"?\"",
    "\"...\"",
    "\"=\"",
    "\"*=\"",
    "\"/=\"",
    "\"%=\"",
    "\"+=\"",
    "\"-=\"",
    "\"<<=\"",
    "\">>=\"",
    "\"&=\"",
    "\"^=\"",
    "\"|=\"",
    "\"||\"",
    "\"&&\"",
    "\"|\"",
    "\"^\"",
    "\"&\"",
    "\"==\"",
    "\"!=\"",
    "\"<\"",
    "\">\"",
    "\"<=\"",
    "\">=\"",
    "\"<<\"",
    "\">>\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "\"++\"",
    "\"--\"",
    "\"~\"",
    "\"!\"",
    "\".\"",
    "\"->\"",
    "\".*\"",
    "\"->*\"",
    "\"auto\"",
    "\"break\"",
    "\"case\"",
    "\"catch\"",
    "\"char\"",
    "\"const\"",
    "\"continue\"",
    "\"default\"",
    "\"delete\"",
    "\"do\"",
    "\"double\"",
    "\"else\"",
    "\"enum\"",
    "\"extern\"",
    "\"float\"",
    "\"for\"",
    "\"friend\"",
    "\"goto\"",
    "\"if\"",
    "\"inline\"",
    "\"int\"",
    "\"long\"",
    "\"new\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"redeclared\"",
    "\"register\"",
    "\"return\"",
    "\"short\"",
    "\"signed\"",
    "\"sizeof\"",
    "\"static\"",
    "\"struct\"",
    "\"class\"",
    "\"switch\"",
    "\"template\"",
    "\"this\"",
    "\"try\"",
    "\"typedef\"",
    "\"union\"",
    "\"unsigned\"",
    "\"virtual\"",
    "\"void\"",
    "\"volatile\"",
    "\"while\"",
    "\"operator\"",
    "\"true\"",
    "\"false\"",
    "\"throw\"",
    "<OCTALINT>",
    "<OCTALLONG>",
    "<UNSIGNED_OCTALINT>",
    "<UNSIGNED_OCTALLONG>",
    "<DECIMALINT>",
    "<DECIMALLONG>",
    "<UNSIGNED_DECIMALINT>",
    "<UNSIGNED_DECIMALLONG>",
    "<HEXADECIMALINT>",
    "<HEXADECIMALLONG>",
    "<UNSIGNED_HEXADECIMALINT>",
    "<UNSIGNED_HEXADECIMALLONG>",
    "<FLOATONE>",
    "<FLOATTWO>",
    "<CHARACTER>",
    "<STRING>",
    "\"finally\"",
    "<ID>",
  };

}
