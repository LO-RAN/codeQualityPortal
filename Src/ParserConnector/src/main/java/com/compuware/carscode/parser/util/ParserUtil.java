/**
 * 
 */
package com.compuware.carscode.parser.util;


/**
 * @author cwfr-fdubois
 *
 */
public class ParserUtil {

	public static String[][] flags = 
	{	
	  /*
	  ** Null Dereferences (Manual Section 2)
	  */

	  {
	    "FK_NULL, FK_MEMORY, specialFlag",
	    "null",

	    "misuses of null pointer",
	    "A possibly null pointer is misused (sets nullderef, nullpass, "+
	    "nullref, nullassign, and nullstate).",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, modeFlag",
	    "nullderef",

	    "possible dereferencce of null pointer",
	    "A possibly null pointer is dereferenced.  Value is "+
	    "either the result of a function which may return null "+
	    "(in which case, code should check it is not null), or a "+
	    "global, parameter or structure field declared with the "+
	    "null qualifier.",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, modeFlag",
	    "nullpass",

	    "possibly null pointer passed as formal with no null annotation",
	    "A possibly null pointer is passed as a parameter corresponding to "+
	    "a formal parameter with no /*@null@*/ annotation.  If NULL "+
	    "may be used for this parameter, add a /*@null@*/ annotation "+
	    "to the function parameter declaration.",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, modeFlag",
	    "nullret",

	    "possibly null pointer returned as result with no null annotation",
	    "Function returns a possibly null pointer, but is not declared "+
	    "using /*@null@*/ annotation of result.  If function may "+
	    "return NULL, add /*@null@*/ annotation to the return "+
	    "value declaration.",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, modeFlag",
	    "nullstate",

	    "possibly null pointer reachable from a reference with no null annotation",
	    "A possibly null pointer is reachable from a parameter or global "+
	    "variable that is not declared using a /*@null@*/ annotation.",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, modeFlag",
	    "nullassign",

	    "inconsistent assignment or initialization involving null pointer",
	    "A reference with no null annotation is assigned or initialized "+
	    "to NULL.  Use /*@null@*/ to declare the reference as "+
	    "a possibly null pointer.",
	    
	  },
	  {
	    "FK_NULL, FK_MEMORY, plainFlag",
	    "nullinit",

	    "inconsistent initialization involving null pointer",
	    "A reference with no null annotation is initialized "+
	    "to NULL.  Use /*@null@*/ to declare the reference as "+
	    "a possibly null pointer.",
	    
	  },

	  /*
	  ** Undefined Values (Section 3)
	  */

	  {
	    "FK_DEF, FK_NONE, modeFlag",
	    "usedef",

	    "use before definition",
	    "An rvalue is used that may not be initialized to a value on some execution path.",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEF, modeFlag",
	    "mustdefine",

	    "out storage not defined before return or scope exit",
	    "An out parameter or global is not defined before control is transferred.",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEF, modeFlag",
	    "uniondef",

	    "at least one field of a union must be defined",
	    "No field of a union is defined. Generally, one field of a union is "+
	    "expected to be defined.",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEF, modeFlag",
	    "compdef",

	    "parameter, return value or global completely defined",
	    "Storage derivable from a parameter, return value or global is "+
	    "not defined. Use /*@out@*/ to denote passed or returned "+
	    "storage which need not be defined.",
	    
	  },
	  {
	    "FK_DEF, FK_NONE, plainFlag",
	    "fullinitblock",

	    "initializer sets all fields",
	    "Initializer does not set every field in the structure.",
	    
	  },
	  {
	    "FK_DEF, FK_NONE, plainFlag",
	    "initallelements",

	    "initializer defines all array elements",
	    "Initializer does not define all elements of a declared array.",
	    
	  },
	  {
	    "FK_DEF, FK_NONE, plainFlag",
	    "initsize",

	    "initializer defines extra array elements",
	    "Initializer block contains more elements than the size of a declared array.",
	    
	  },

	  {
	    "FK_DEF, FK_IMPLICIT, modeFlag",
	    "impouts",

	    "pointer parameters to unspecified functions may be implicit out parameters",
	    "", 
	  },

	  /*
	  ** Types (Section 4)
	  */

	  {
	    "FK_DECL, FK_TYPE, modeFlag",
	    "incondefs",

	    "function, variable or constant redefined with inconsistent type",
	    "A function, variable or constant is redefined with a different type.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, modeFlag",
	    "matchfields",

	    "struct or enum type redefined with inconsistent fields or members",
	    "A struct, union or enum type is redefined with inconsistent fields or members.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "fcnderef",

	    "dereferencce of a function type",
	    "A function type is dereferenced.  The ANSI standard allows this "+
	    "because of implicit conversion of function designators, however the "+
	    "dereference is unnecessary.", 
	    
	  },
	  {
	    "FK_OPS, FK_NONE, modeFlag",
	    "realcompare",

	    "dangerous equality comparison between reals (dangerous because of inexact "+
	    "floating point representations)",
	    "Two real (float, double, or long double) values are compared "+
	    "directly using == or != primitive. "+
	    "This may produce unexpected results since floating point "+
	    "representations are inexact. Instead, compare the difference to "+
	    "FLT_EPSILON or DBL_EPSILON.",
	  },
	  {
	    "FK_OPS, FK_NONE, modeFlag",
	    "realrelatecompare",

	    "possibly dangerous relational comparison between reals (dangerous because of inexact "+
	    "floating point representations)",
	    "Two real (float, double, or long double) values are compared "+
	    "directly using < or >. "+
	    "This may produce unexpected results since floating point "+
	    "representations are inexact. Instead, compare the difference to "+
	    "FLT_EPSILON or DBL_EPSILON.",
	  },
	  {
	    "FK_OPS, FK_NONE, modeFlag",
	    "unsignedcompare",
	    "comparison using <, <=, >= between an unsigned integral and zero constant",
	    "An unsigned value is used in a comparison with zero in a way that is either a bug or confusing.",
	  },
	  {
	    "FK_OPS, FK_POINTER, modeFlag",
	    "ptrarith",

	    "arithmetic involving pointer and integer",
	    "Pointer arithmetic using pointer and integer.", 
	  },
	  {
	    "FK_OPS, FK_POINTER, modeFlag",
	    "nullptrarith",

	    "arithmetic involving possibly null pointer and integer",
	    "Pointer arithmetic using a possibly null pointer and integer.", 
	  },
	  {
	    "FK_OPS, FK_POINTER, modeFlag",
	    "ptrcompare",

	    "comparison between pointer and number",
	    "A pointer is compared to a number.", 
	  },
	  {
	    "FK_OPS, FK_TYPE, modeFlag",
	    "strictops",

	    "primitive operation does not type check strictly",
	    "A primitive operation does not type check strictly.", 
	  },
	  {
	    "FK_OPS, FK_TYPE, modeFlag",
	    "bitwisesigned",

	    "a bitwise logical operator does not have unsigned operands",
	    "An operand to a bitwise operator is not an unsigned values.  This "+
	    "may have unexpected results depending on the signed "+
	    "representations.", 
	  },
	  {
	    "FK_OPS, FK_TYPE, modeFlag",
	    "shiftnegative",

	    "a shift right operand may be negative",
	    "The right operand to a shift operator may be negative (behavior undefined).",
	    
	  },
	  {
	    "FK_OPS, FK_TYPE, modeFlag",
	    "shiftimplementation",

	    "a shift left operand may be negative",
	    "The left operand to a shift operator may be negative (behavior is implementation-defined).",
	    
	  },
	  {
	    "FK_OPS, FK_TYPE, modeFlag",
	    "sizeoftype",

	    "sizeof operator has a type argument",
	    "Operand of sizeof operator is a type. (Safer to use expression, "+
	    "int *x = sizeof (*x); instead of sizeof (int).)", 
	    
	  },
	  {
	    "FK_OPS, FK_TYPE, plainFlag",
	    "sizeofformalarray",

	    "sizeof operator has an array formal parameter argument",
	    "Operand of a sizeof operator is a function parameter declared as "+
	    "an array.  The value of sizeof will be the size of a pointer to the "+
	    "element type, not the number of elements in the array.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, plainFlag",
	    "fixedformalarray",

	    "formal parameter of type array is declared with size",
	    "A formal parameter is declared as an array with size.  The size of the array "+
	    "is ignored in this context, since the array formal parameter is treated "+
	    "as a pointer.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, plainFlag",
	    "incompletetype",

	    "formal parameter has an incomplete type",
	    "A formal parameter is declared with an incomplete type.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, plainFlag",
	    "formalarray",

	    "formal parameter is an array",
	    "A formal parameter is declared as an array.  This can be confusing, since "+
	    "a formal array parameter is treated as a pointer.",
	    
	  },


	  /*
	  ** Booleans (4.2)
	  */

	  {
	    "FK_BOOL, FK_NONE, regStringFlag",
	    "booltype",

	    "set name of boolean type (default bool)",
	    "", 
	  },
	  {
	    "FK_BOOL, FK_NONE, regStringFlag",
	    "boolfalse",

	    "set name of boolean false (default false)",
	    "", 
	  },
	  {
	    "FK_BOOL, FK_NONE, regStringFlag",
	    "booltrue",
	 
	    "set name of boolean true (default true)",
	    "", 
	  },
	  {
	    "FK_BOOL, FK_HELP, plainFlag",
	    "likelybool",

	    "type name is probably a boolean type but does not match default "+
	    "boolean type name, \"bool\", and alternate name is not set",
	    "Use the -booltype, -boolfalse and -booltrue flags to change the "+
	    "name of the default boolean type.",
	    
	  },

	  {
	    "FK_BOOL, FK_OPS, modeFlag",
	    "boolcompare",

	    "comparison between bools (dangerous because of multiple true values)",
	    "Two bool values are compared directly using a C primitive. This "+
	    "may produce unexpected results since all non-zero values are "+
	    "considered true, so different true values may not be equal. "+
	    "The file bool.h (included in splint/lib) provides bool_equal "+
	    "for safe bool comparisons.", 
	  },
	  {
	    "FK_BOOL, FK_OPS, modeFlag",
	    "boolops",

	    "primitive operation (!, && or ||) does not has a boolean argument",
	    "The operand of a boolean operator is not a boolean. Use +ptrnegate "+
	    "to allow ! to be used on pointers.",
	    
	  },
	  {
	    "FK_BOOL, FK_POINTER, modeFlag",
	    "ptrnegate",

	    "allow ! to be used on pointer operand",
	    "The operand of ! operator is a pointer.", 
	  },
	  {
	    "FK_BOOL, FK_PRED,plainFlag",
	    "predassign",

	    "condition test (if, while or for) is an assignment",
	    "The condition test is an assignment expression. Probably, you mean "+
	    "to use == instead of =. If an assignment is intended, add an "+
	    "extra parentheses nesting (e.g., if ((a = b)) ...) to suppress "+
	    "this message.",
	    
	  },
	  {
	    "FK_BOOL, FK_PRED, specialFlag",
	    "predbool",

	    "type of condition test (if, while or for) not bool (sets predboolint, "+
	    "predboolptr and predboolothers)",
	    "Test expression type is not boolean.", 
	  },
	  {
	    "FK_PRED, FK_BOOL, modeFlag",
	    "predboolint",

	    "type of condition test (if, while or for) is an integral type",
	    "Test expression type is not boolean or int.", 
	  },
	  {
	    "FK_BOOL, FK_PRED, modeFlag",
	    "predboolptr",

	    "type of condition test (if, while or for) is a pointer",
	    "Test expression type is not boolean.", 
	  },
	  {
	    "FK_BOOL, FK_PRED, modeFlag",
	    "predboolothers",

	    "type of condition test (if, while or for) not bool, int or pointer",
	    "Test expression type is not boolean.", 
	  },

	  /*
	  ** 4.3 Abstract types
	  */

	  {
	    "FK_ABSTRACT, FK_NONE, plainFlag",
	    "abstract",

	    "data abstraction barriers",
	    "An abstraction barrier is broken. If necessary, use /*@access <type>@*/ to allow access to an abstract type.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NONE, modeFlag",
	    "abstractcompare",

	    "object equality comparison on abstract type operands",
	    "An object comparison (== or !=) is used on operands of abstract type.",
	    
	  },

	  {
	    "FK_ABSTRACT, FK_NONE, plainFlag",
	    "numabstract",

	    "data abstraction barriers",
	    "An abstraction barrier involving a numabstract type is broken. If necessary, use /*@access <type>@*/ to allow access to a numabstract type.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NONE, modeFlag",
	    "numabstractcast",

	    "numeric literal cast to numabstract type",
	    "A numeric literal is cast to a numabstract type.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NONE, modeFlag",
	    "numabstractlit",

	    "numeric literal can used as numabstract type",
	    "To allow a numeric literal to be used as a numabstract type, use +numabstractlit.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_TYPEEQ, modeFlag",
	    "numabstractindex",

	    "a numabstract type can be used to index an array",
	    "To allow numabstract types to index arrays, use +numabstractindex.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NONE, modeFlag",
	    "numabstractprint",

	    "a numabstract value is printed using %d format code",
	    "A numabstract value is printed usind %d format code in a printf.",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_IMPLICIT, plainFlag",
	    "impabstract",

	    "assume user type definitions are abstract (unless /*@concrete@*/ is used)",
	    "",
	    
	  },

	  /* 4.3.1 Access */

	  {
	    "FK_ABSTRACT, FK_NAMES, plainFlag",
	    "accessmodule",

	    "allow access to abstract types in definition module",
	    "The representation of an abstract type defined in <M>.<x> is "+
	    "accessible anywhere in a file named <M>.<y>.", 
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NAMES, plainFlag",
	    "accessfile",

	    "allow access to abstract types by file name convention",
	    "The representation of an abstract type named <t> is "+
	    "accessible anywhere in a file named <t>.<x>.", 
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NAMES, plainFlag",
	    "accessczech",

	    "allow access to abstract types by czech naming convention",
	    "The representation of an abstract type named <t> is accessible "+
	    "in the definition of a function or constant named <t>_<name>",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NAMES, plainFlag",
	    "accessslovak",

	    "allow access to abstract types by slovak naming convention",
	    "The representation of an abstract type named <t> is accessible "+
	    "in the definition of a function or constant named <t><Name>",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NAMES, plainFlag",
	    "accessczechoslovak",

	    "allow access to abstract types by czechoslovak naming convention",
	    "The representation of an abstract type named <t> is accessible "+
	    "in the definition of a function or constant named <t>_<name> or <t><Name>",
	    
	  },
	  {
	    "FK_ABSTRACT, FK_NAMES, specialFlag",
	    "accessall",

	    "set accessmodule, accessfile and accessczech",
	    "Sets accessmodule, accessfile and accessczech",
	    
	  },

	  /* 4.3.2 Mutability */
	  {
	    "FK_ABSTRACT, FK_NONE, modeFlag",
	    "mutrep",

	    "representation of mutable type has sharing semantics",
	    "LCL semantics requires that a mutable type exhibits sharing semantics. "+
	    "In order for objects to be shared a indirection is necessary in the representation. "+
	    "A mutable type may be represented by a pointer or an abstract mutable type. Handles "+
	    "into static data are fine, too, but will generate this error message unless it is suppressed.",
	    
	  },
	  
	  
	  /*
	  ** Memory Management (5)
	  */

	  /* Deallocation Errors */
	  {
	    "FK_MEMORY, FK_LEAK, modeFlag",
	    "mustfreefresh",

	    "freshly allocated storage not released before return or scope exit",
	    "A memory leak has been detected. Storage allocated locally "+
	    "is not released before the last reference to it is lost.",
	    
	  },
	  {
	    "FK_MEMORY, FK_LEAK, modeFlag",
	    "mustfreeonly",

	    "only storage not released before return or scope exit",
	    "A memory leak has been detected. Only-qualified storage is not released before the last "+
	    "reference to it is lost.",
	    
	  },
	  {
	    "FK_MEMORY, FK_LEAK, specialFlag",
	    "mustfree",

	    "fresh or only storage not released before return or scope exit (sets mustfreefresh and mustfreeonly)",
	    "A memory leak has been detected.",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEAD, modeFlag",
	    "usereleased",

	    "storage used after release",
	    "Memory is used after it has been released (either by passing "+
	    "as an only param or assigning to an only global).",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEAD, modeFlag",
	    "strictusereleased",

	    "element used after it may have been released",
	    "Memory (through fetch) is used after it may have been released "+
	    "(either by passing as an only param or assigning to an only global).",
	    
	  },
	  {
	    "FK_MEMORY, FK_LEAK, modeFlag",
	    "compdestroy",

	    "all only references derivable from void pointer out only parameter are released",
	    "A storage leak due to incomplete deallocation of a structure or deep "+
	    "pointer is suspected. Unshared storage that is reachable from "+
	    "a reference that is being deallocated has not yet been deallocated. "+
	    "Splint assumes when an object is passed "+
	    "as an out only void pointer that the outer object will be "+
	    "deallocated, but the inner objects will not.",
	    
	  },
	  {
	    "FK_MEMORY, FK_LEAK, modeFlag",
	    "strictdestroy",

	    "report complete destruction errors for array elements that "+
	    "may have been released",
	    "",
	    	
	  },	
	  {
	    "FK_MEMORY, FK_ARRAY, modeFlag",
	    "deparrays",

	    "array elements are dependent storage",
	    "When an element is fetched from an array, Splint analysis is "+
	    "not able to determine if the same element is reused. "+
	    "If +deparrays, Splint will mark local storage assigned from "+
	    "array fetches as dependent.", 
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "branchstate",

	    "storage has inconsistent states of alternate paths through a branch",
	    "The state of a variable is different depending on which branch "+
	    "is taken. This means no annotation can sensibly be applied "+
	    "to the storage.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "strictbranchstate",

	    "storage through array fetch has inconsistent states of alternate "+
	    "paths through a branch",
	    "The state of a variable through an array fetch is different depending "+
	    "on which branch is taken. This means no annotation can sensibly be applied "+
	    "to the storage.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, specialFlag",
	    "memchecks",

	    "sets all dynamic memory checking flags (memimplicit, mustfree, mustdefine, "+
	    "mustnotalias, null, memtrans)",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_DEF, modeFlag",
	    "compmempass",

	    "actual parameter matches alias kind of formal parameter completely ",
	    "Storage derivable from a parameter does not match the alias kind "+
	    "expected for the formal parameter.",
	    
	  },
	  {
	    "FK_MEMORY, FK_DEAD, modeFlag",
	    "stackref",

	    "external reference to stack-allocated storage is created",
	    "A stack reference is pointed to by an external reference when the "+
	    "function returns. The stack-allocated storage is destroyed "+
	    "after the call, leaving a dangling reference.",
	    
	  },

	  {
	    "FK_MEMORY, FK_NONE, specialFlag",
	    "memtrans",

	    "memory transfer errors (sets all *trans flags)",
	    "Memory is transferred in a way that violates annotations.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "dependenttrans",

	    "dependent transfer errors",
	    "Dependent storage is transferred to a non-dependent reference.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "newreftrans",

	    "new reference transfer to reference counted reference",
	    "A new reference is transferred to a reference counted reference.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "onlytrans",

	    "only storage transferred to non-only reference (memory leak)",
	    "The only reference to this storage is transferred to another "+
	    "reference (e.g., by returning it) that does not have the "+
	    "only annotation. This may lead to a memory leak, since the "+
	    "new reference is not necessarily released.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "onlyunqglobaltrans",

	    "only storage transferred to an unqualified global or "+
	    "static reference (memory leak)",
	    "The only reference to this storage is transferred to another "+
	    "reference that does not have an aliasing annotation. "+
	    "This may lead to a memory leak, since the "+
	    "new reference is not necessarily released.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "ownedtrans",

	    "owned storage transferred to non-owned reference (memory leak)",
	    "The owned reference to this storage is transferred to another "+
	    "reference (e.g., by returning it) that does not have the "+
	    "owned annotation. This may lead to a memory leak, since the "+
	    "new reference is not necessarily released.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "freshtrans",

	    "fresh storage transferred to non-only reference (memory leak)",
	    "Fresh storage (newly allocated in this function) is transferred "+
	    "in a way that the obligation to release storage is not "+
	    "propagated.  Use the /*@only@*/ annotation to indicate "+
	    "the a return value is the only reference to the returned "+
	    "storage.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "sharedtrans",

	    "shared storage transferred to non-shared reference",
	    "Shared storage is transferred to a non-shared reference. The other "+
	    "reference may release storage needed by this reference.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "temptrans",

	    "temp storage transferred to non-temporary reference",
	    "Temp storage (associated with a formal parameter) is transferred "+
	    "to a non-temporary reference. The storage may be released "+
	    "or new aliases created.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "kepttrans",

	    "kept storage transferred to non-temporary reference",
	    "storage is transferred "+
	    "to a non-temporary reference after being passed as keep parameter. The storage may be released "+
	    "or new aliases created.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "keeptrans",

	    "keep storage transferred inconsistently",
	    "Keep storage is transferred inconsistently --- either in a way "+
	    "that may add a new alias to it, or release it.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "immediatetrans",

	    "an immediate address (result of &) is transferred inconsistently",
	    "An immediate address (result of & operator) is transferred "+
	    "inconsistently.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "refcounttrans",

	    "reference counted storage is transferred in an inconsistent way",
	    "Reference counted storage is transferred in a way that may not "+
	    "be consistent with the reference count.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "statictrans",

	    "static storage is transferred in an inconsistent way",
	    "Static storage is transferred in an inconsistent way.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "unqualifiedtrans",

	    "unqualified storage is transferred in an inconsistent way",
	    "Unqualified storage is transferred in an inconsistent way.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "staticinittrans",

	    "static storage is used as an initial value in an inconsistent way",
	    "Static storage is used as an initial value in an inconsistent way.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "unqualifiedinittrans",

	    "unqualified storage is used as an initial value in an inconsistent way",
	    "Unqualified storage is used as an initial value in an inconsistent way.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "readonlytrans",

	    "report memory transfer errors for initializations to read-only string literals",
	    "A read-only string literal is assigned to a non-observer reference.",
	    
	  },
	  {
	    "FK_MEMORY, FK_PARAMS, modeFlag",
	    "passunknown",

	    "passing a value as an un-annotated parameter clears its annotation",
	    "", 
	  },

	  /* 5.3 Implicit Memory Annotations */

	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "readonlystrings",

	    "string literals are read-only (error if one is modified or released)",
	    "String literals are read-only. An error is reported "+
	    "if a string literal may be modified or released.",
	    
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, modeFlag",
	    "memimp",

	    "memory errors for unqualified storage",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "paramimptemp",

	    "assume unannotated parameter is temp",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, specialFlag",
	    "allimponly",

	    "sets globimponly, retimponly, structimponly, specglobimponly, "+
	    "specretimponly and specstructimponly",  
	    "", 
	  },	
	  {
	    "FK_MEMORY, FK_IMPLICIT, specialFlag",
	    "codeimponly",

	    "sets globimponly, retimponly and structimponly",
	    "", 
	  },	
	  {
	    "FK_MEMORY, FK_IMPLICIT, specialFlag",
	    "specimponly",

	    "sets specglobimponly, specretimponly and specstructimponly",
	    "", 
	  },	
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "globimponly",

	    "assume unannotated global storage is only",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "retimponly",

	    "assume unannotated returned storage is only",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "structimponly",

	    "assume unannotated structure field is only",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "specglobimponly",

	    "assume unannotated global storage is only",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "specretimponly",

	    "assume unannotated returned storage is only",
	    "", 
	  },
	  {
	    "FK_MEMORY, FK_IMPLICIT, plainFlag",
	    "specstructimponly",

	    "assume unannotated structure field is only",
	    "", 
	  },

	  /* Reference Counting */

	  

	  /*
	  ** 6. Sharing
	  */

	  /* 6.1 Aliasing warnings */

	  {
	    "FK_ALIAS, FK_MEMORY, modeFlag",
	    "aliasunique",

	    "unique parameter is aliased",
	    "A unique or only parameter is aliased by some other parameter or visible global.",
	    
	  },
	  {
	    "FK_ALIAS, FK_MEMORY, modeFlag",
	    "mayaliasunique",

	    "unique parameter may be aliased",
	    "A unique or only parameter may be aliased by some other parameter or visible global.",
	    
	  },
	  {
	    "FK_ALIAS, FK_MEMORY, modeFlag",
	    "mustnotalias",

	    "temp storage aliased at return point or scope exit",
	    "An alias has been added to a temp-qualifier parameter or global that is visible externally when the function returns. If the aliasing is needed, use the /*@shared@*/ annotation to indicate that new aliases to the parameter may be created.",
	    
	  },
	  {
	    "FK_ALIAS, FK_NONE, modeFlag",
	    "retalias",

	    "function returns alias to parameter or global",
	    "The returned value shares storage with a parameter or global. If a parameter is to be returned, use the returned qualifier. If the result is not modified, use the observer qualifier on the result type. Otherwise, exposed can be used, but limited checking is done.", 
	  },

	  /* Global aliasing */
	  {
	    "FK_ALIAS, FK_GLOBALS, specialFlag",
	    "globalias",

	    "function returns with global aliasing external state (sets "+
	    "checkstrictglobalias, checkedglobalias, checkmodglobalias and "+
	    "uncheckedglobalias)",
	    "A global variable aliases externally-visible state when the function returns.",
	    
	  },
	  {
	    "FK_ALIAS, FK_GLOBALS, modeFlag",
	    "checkstrictglobalias",

	    "function returns with a checkstrict global aliasing external state",
	    "A global variable aliases externally-visible state when the function returns.",
	    
	  },
	  {
	    "FK_ALIAS, FK_GLOBALS, modeFlag",
	    "checkedglobalias",

	    "function returns with a checked global aliasing external state",
	    "A global variable aliases externally-visible state when the function returns.",
	    
	  },
	  {
	    "FK_ALIAS, FK_GLOBALS, modeFlag",
	    "checkmodglobalias",

	    "function returns with a checkmod global aliasing external state",
	    "A global variable aliases externally-visible state when the function returns.",
	    
	  },
	  {
	    "FK_ALIAS, FK_GLOBALS, modeFlag",
	    "uncheckedglobalias",

	    "function returns with an unchecked global aliasing external state",
	    "A global variable aliases externally-visible state when the function returns.",
	    
	  },

	  /* 6.2 Exposure */

	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "exposetrans",

	    "exposure transfer errors",
	    "Exposed storage is transferred to a non-exposed, non-observer reference.",
	    
	  },
	  {
	    "FK_MEMORY, FK_NONE, modeFlag",
	    "observertrans",

	    "observer transfer errors",
	    "Observer storage is transferred to a non-observer reference.",
	    
	  },
	  {
	    "FK_EXPOSURE, FK_ABSTRACT, specialFlag",
	    "repexpose",

	    "abstract representation is exposed (sets assignexpose, retexpose, and castexpose)",
	    "The internal representation of an abstract type is visible to the "+
	    "caller. This means clients may have access to a pointer "+
	    "into the abstract representation.",
	    
	  },
	  {
	    "FK_EXPOSURE, FK_ABSTRACT, modeFlag",
	    "retexpose",

	    "abstract representation is exposed (return values only)",
	    "The return value shares storage with an instance of an abstract "+
	    "type. This means clients may have access to a pointer into "+
	    "the abstract representation. Use the observer qualifier to "+
	    "return exposed storage that may not be modified by the "+
	    "client. Use the exposed qualifier to return modifiable "+
	    "(but not deallocatable) exposed storage (dangerous).",
	    
	  },
	  {
	    "FK_EXPOSURE, FK_ABSTRACT, modeFlag",
	    "assignexpose",

	    "abstract representation is exposed (assignments only)",
	    "Storage internal to the representation of an abstract type is "+
	    "assigned to an external pointer. This means clients may "+
	    "have access to a pointer into the abstract representation. "+
	    "If the external pointer is a parameter, the exposed qualifier "+
	    "can be used to allow the assignment, however, this is "+
	    "considered dangerous programming practice.",
	    
	  },
	  {
	    "FK_EXPOSURE, FK_ABSTRACT, modeFlag",
	    "castexpose",

	    "abstract representation is exposed through a cast",
	    "Storage internal to the representation of an abstract type is exposed "+
	    "through a type cast. This means clients may have access to a "+
	    "pointer into the abstract representation.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, modeFlag",
	    "redundantsharequal",

	    "declaration uses observer qualifier that is always true",
	    "A declaration of an immutable object uses a redundant observer qualifier.",
	    
	  } ,
	  {
	    "FK_DECL, FK_TYPE, modeFlag",
	    "misplacedsharequal",

	    "declaration of unsharable storage uses sharing annotation",
	    "A declaration of an unsharable object uses a sharing annotation.",
	    
	  } ,

	  /*
	  ** 7. Function Interfaces
	  */

	  /* 7.1  Modifications */

	  {
	    "FK_MODIFIES, FK_SPEC, plainFlag",
	    "mods",

	    "unspecified modification of caller-visible state",
	    "An externally-visible object is modified by a function, but not "+
	    "listed in its modifies clause.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_SPEC, modeFlag",
	    "mustmod",

	    "specified modification is not detected",
	    "An object listed in the modifies clause is not modified by the "+
	    "implementation of the function. The modification may not "+
	    "be detected if it is done through a call to an unspecified "+
	    "function.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_MEMORY, plainFlag",
	    "modobserver",

	    "possible modification of observer storage",
	    "Storage declared with observer is possibly modified. Observer "+
	    "storage may not be modified.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_MEMORY, modeFlag",
	    "modobserveruncon",

	    "possible modification of observer storage through unconstrained call",
	    "Storage declared with observer may be modified through a call to an "+
	    "unconstrained function.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_MEMORY, modeFlag",
	    "modinternalstrict",

	    "possible modification of internal storage through function call",
	    "A function that modifies internalState is called from a function that "+
	    "does not list internalState in its modifies clause",
	    
	  },
	  {
	    "FK_MODIFIES, FK_UNSPEC, modeFlag",
	    "modfilesys",

	    "report undocumented file system modifications (applies to unspecified "+
	    "functions if modnomods is set)", 
	    "", 
	  },
	  {
	    "FK_MODIFIES, FK_UNSPEC, specialFlag",
	    "modunspec",

	    "modification in unspecified functions (sets modnomods, "+
	    "modglobunspec and modstrictglobsunspec)",
	    "", 
	  },
	  {
	    "FK_MODIFIES, FK_UNSPEC, modeFlag",
	    "modnomods",

	    "modification in a function with no modifies clause",
	    "An externally-visible object is modified by a function with no "+
	    "/*@modifies@*/ comment. The /*@modifies ... @*/ control "+
	    "comment can be used to give a modifies list for an "+
	    "unspecified function.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_UNSPEC, modeFlag",
	    "moduncon",

	    "possible modification through a call to an unconstrained function",
	    "An unconstrained function is called in a function body where "+
	    "modifications are checked. Since the unconstrained function "+
	    "may modify anything, there may be undetected modifications in "+
	    "the checked function.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_UNSPEC, modeFlag",
	    "modunconnomods",

	    "possible modification through a call to an unconstrained function in "+
	    "a function with no modifies clause",
	    "An unconstrained function is called in a function body where "+
	    "modifications are checked. Since the unconstrained function "+
	    "may modify anything, there may be undetected modifications in "+
	    "the checked function.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_GLOBALS, modeFlag",
	    "globsimpmodsnothing",

	    "functions declared with a globals list but no modifies clause are "+
	    "assumed to modify nothing",
	    "An implicit modifies nothing clause is assumed for a function "+
	    "declared with a globals list but not modifies clause.",
	    
	  },
	  {
	    "FK_MODIFIES, FK_GLOBALS, modeFlag",
	    "modsimpnoglobs",

	    "functions declared with a modifies clause but no globals list "+
	    "are assumed to use no globals",
	    "An implicit empty globals list is assumed for a function "+
	    "declared with a modifies clause but no globals list.",
	    
	  },

	  /*
	  ** Globals
	  */

	  {
	    "FK_GLOBALS, FK_NONE, modeFlag",
	    "globstate",

	    "returns with global in inconsistent state (null or undefined)",
	    "A global variable does not satisfy its annotations when control is transferred.",
	    
	  },

	  {
	    "FK_GLOBALS, FK_SPEC, plainFlag",
	    "globs",

	    "undocumented use of a checked global variable",
	    "A checked global variable is used in the function, but not listed in "+
	    "its globals clause. By default, only globals specified in .lcl "+
	    "files are checked. To check all globals, use +allglobals. To "+
	    "check globals selectively use /*@checked@*/ in the global "+
	    "declaration.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_SPEC, modeFlag",
	    "globuse",

	    "global listed for a function not used",
	    "A global variable listed in the function's globals list is not used "+
	    "in the body of the function.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_NONE, modeFlag",
	    "internalglobs",

	    "use of internalState",
	    "A called function uses internal state, but the globals list for the "+
	    "function being checked does not include internalState",
	    
	  },
	  {
	    "FK_GLOBALS, FK_NONE, modeFlag",
	    "internalglobsnoglobs",

	    "use of internalState (in function with no globals list)",
	    "A called function uses internal state, but the function being checked "+
	    "has no globals list",
	    
	  },
	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "warnmissingglobs",

	    "global variable used in modifies clause is not listed in globals list",
	    "A global variable is used in the modifies clause, but it is not listed in "+
	    "the globals list.  The variable will be added to the globals list.",
	    
	  },

	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "warnmissingglobsnoglobs",

	    "global variable used in modifies clause in a function with no globals list",
	    "A global variable is used in the modifies clause, but the function "+
	    "has no globals list.  The variable will be added to the globals list.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "globnoglobs",

	    "use of checked global in a function with no globals list or specification",
	    "A specified global variable is used in the function, but not listed "+
	    "in its globals list. Without +globnoglobs, only globals "+
	    "declared with /*@checkedstrict@*/ will produce use "+
	    "errors in functions without globals "+
	    "lists. The /*@globals ... @*/ control comment can be used to give "+
	    "a globals list for an unspecified function.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_IMPLICIT, modeFlag",
	    "allglobs",

	    "report use and modification errors for globals not annotated with unchecked",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "checkstrictglobs",

	    "report use and modification errors for checkedstrict globals",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedspecglobs",

	    "assume checked qualifier for unqualified global declarations in .lcl files",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckmodspecglobs",

	    "assume checkmod qualifier for unqualified global declarations in .lcl files",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedstrictspecglobs",

	    "assume checkmod qualifier for unqualified global declarations in .lcl files",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedglobs",

	    "assume checked qualifier for unqualified global declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckmodglobs",

	    "assume checkmod qualifier for unqualified global declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedstrictglobs",

	    "assume checkedstrict qualifier for unqualified global declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedstatics",

	    "assume checked qualifier for unqualified file static declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckmodstatics",

	    "assume checkmod qualifier for unqualified file static declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckmodinternals",

	    "assume checkmod qualifier for unqualified local "+
	    "static declarations (for internal state modifications)",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_UNSPEC, modeFlag",
	    "impcheckedstrictstatics",

	    "assume checkedstrict qualifier for unqualified file static declarations",
	    "", 
	  },
	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "modglobs",

	    "undocumented modification of a checked global variable",
	    "A checked global variable is modified by the function, but not listed in "+
	    "its modifies clause.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "modglobsnomods",

	    "undocumented modification of a checked global variable in a function "+
	    "declared with no modifies clause",
	    "A checked global variable is modified by the function, but not listed in "+
	    "its modifies clause.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "modstrictglobsnomods",

	    "undocumented modification of a strict checked global variable in a "+
	    "function declared with no modifies clause",
	    "A checked global variable is modified by the function, but not listed in "+
	    "its modifies clause.",
	    
	  },
	  {
	    "FK_GLOBALS, FK_MODIFIES, modeFlag",
	    "modglobsunchecked",

	    "undocumented modification of an unchecked checked global variable",
	    "An unchecked global variable is modified by the function, but not listed in "+
	    "its modifies clause.",
	    
	  },

	  /*
	  ** State Clauses
	  */

	  /*
	  ** 8. Control Flow 
	  */

	  /* 8.1 Execution */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "noret",

	    "path with no return detected in non-void function",
	    "There is a path through a function declared to return a value on "+
	    "which there is no return statement. This means the execution "+
	    "may fall through without returning a meaningful result to "+
	    "the caller.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, plainFlag",
	    "emptyret", 
	 
	    "empty return in function declared to return value",
	    "",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, plainFlag",
	    "alwaysexits",
	 
	    "loop predicate always exits", 
	    "",
	    
	  },

	  {
	    "FK_CONTROL, FK_MEMORY, specialFlag",
	    "loopexec",

	    "assume all loops execute at least once (sets forloopexec, whileloopexec and iterloopexec)",
	    "", 
	  },
	  {
	    "FK_CONTROL, FK_MEMORY, plainFlag",
	    "forloopexec",

	    "assume all for loops execute at least once",
	    "", 
	  },
	  {
	    "FK_CONTROL, FK_MEMORY, plainFlag",
	    "whileloopexec",

	    "assume all while loops execute at least once",
	    "", 
	  },
	  {
	    "FK_CONTROL, FK_MEMORY, plainFlag",
	    "iterloopexec",

	    "assume all iterator loops execute at least once",
	    "", 
	  },
	  {
	    "FK_CONTROL, FK_MEMORY, plainFlag",
	    "obviousloopexec",

	    "assume loop that can be determined to always execute always does",
	    "", 
	  },

	  /* 8.2 Undefined Behavior */

	  {
	    "FK_BEHAVIOR, FK_ANSI, modeFlag",
	    "evalorder",

	    "code has unspecified or implementation-dependent behavior "+
	    "because of order of evaluation",
	    "Code has unspecified behavior. "+
	    "Order of evaluation of function parameters or subexpressions "+
	    "is not defined, so if a value is used and modified in different "+
	    "places not separated by a sequence point constraining "+
	    "evaluation order, then the result of the expression is "+
	    "unspecified.", 
	    
	  },
	  {
	    "FK_BEHAVIOR, FK_ANSI, modeFlag",
	    "evalorderuncon",

	    "code involving call to unspecified function has undefined or implementation-dependent behavior",
	    "Code involving a call to function with no modifies or globals clause "+
	    "may have undefined or implementation-dependent behavior (Splint assumes the "+
	    "unconstrained call may modify any reachable state or use any global). Add a "+
	    "specification for the function.", 
	    
	  },

	  /* 8.3 Problematic Control Structures */

	  /* 8.3.1 Infinite Loops */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "infloops",

	    "likely infinite loop is detected",
	    "This appears to be an infinite loop. Nothing in the body of the "+
	    "loop or the loop test modifies the value of the loop test. "+
	    "Perhaps the specification of a function called in the loop "+
	    "body is missing a modification.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "infloopsuncon",

	    "likely infinite loop is detected (may result from unconstrained function)",
	    "This appears to be an infinite loop. Nothing in the body of the "+
	    "loop or the loop test modifies the value of the loop test. "+
	    "There may be a modification through a call to an unconstrained "+
	    "function, or an unconstrained function in the loop test may use "+
	    "a global variable modified by the loop body.",
	    
	  },

	  /* 8.3.2 Switches */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "casebreak",

	    "non-empty case in a switch without preceding break",
	    "Execution falls through from the previous case (use /*@fallthrough@*/ to mark fallthrough cases).", 
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "misscase",

	    "switch on enum type missing case for some value",
	    "Not all values in an enumeration are present as cases in the switch.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "firstcase",

	    "first statement in switch is not a case",
	    "The first statement after a switch is not a case.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, plainFlag",
	    "duplicatecases",
	 
	    "duplicate cases in switch",
	    "Duplicate cases in switch.",
	    
	  },

	  /* 8.3.3 Deep Breaks */

	  {
	    "FK_CONTROL, FK_NONE, specialFlag",
	    "deepbreak",

	    "break inside nested while or for or switch",
	    "A break statement appears inside the body of a nested while, for or "+
	    "switch statement. Sets looploopbreak, loopswitchbreak, "+
	    "switchloopbreak, switchswitchbreak, and looploopcontinue.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "looploopbreak",

	    "break inside nested while or for",
	    "A break statement appears inside the body of a nested while or for "+
	    "statement. This is perfectly reasonable code, but check that "+
	    "the break is intended to break only the inner loop. The "+
	    "break statement may be preceded by /*@innerbreak@*/ to suppress "+
	    "the message for this break only.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "switchloopbreak",

	    "break in loop inside switch",
	    "A break statement appears inside the body of a while or for "+
	    "statement within a switch. This is perfectly reasonable code, but check that "+
	    "the break is intended to break only the inner loop. The "+
	    "break statement may be preceded by /*@loopbreak@*/ to suppress "+
	    "the message for this break only.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "loopswitchbreak",

	    "break in switch inside loop",
	    "A break statement appears inside a switch statement within a while or "+
	    "for loop. This is perfectly reasonable code, but check that "+
	    "the break is intended to break only the inner loop. The "+
	    "break statement may be preceded by /*@switchbreak@*/ to suppress "+
	    "the message for this break only.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "switchswitchbreak",

	    "break in switch inside switch",
	    "A break statement appears inside a switch statement within another "+
	    "switch statement. This is perfectly reasonable code, but check that "+
	    "the break is intended to break only the inner switch. The "+
	    "break statement may be preceded by /*@innerbreak@*/ to suppress "+
	    "the message for this break only.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "looploopcontinue",

	    "continue inside nested loop",
	    "A continue statement appears inside a loop within a loop. "+
	    "This is perfectly reasonable code, but check that "+
	    "the continue is intended to continue only the inner loop. The "+
	    "continue statement may be preceded by /*@innercontinue@*/ to suppress "+
	    "the message for this continue only.",
	    
	  },

	  /* 8.3.4 Loop and If Bodies */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "whileempty",

	    "a while statement has no body",
	    "While statement has no body.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "whileblock",

	    "the body of a while statement is not a block",
	    "While body is a single statement, not a compound block.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "forempty",

	    "a for statement has no body",
	    "For statement has no body.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "forblock",

	    "the body of a for statement is not a block",
	    "Loop body is a single statement, not a compound block.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "ifempty",

	    "an if statement has no body",
	    "If statement has no body.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "ifblock",

	    "the body of an if statement is not a block",
	    "If body is a single statement, not a compound block.",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, specialFlag",
	    "allempty",

	    "an if, while or for statement has no body (sets ifempty, "+
	    "whileempty and forempty",
	    "",
	    
	  },
	  {
	    "FK_CONTROL, FK_NONE, specialFlag",
	    "allblock",

	    "the body of an if, while or for statement is not a block "+
	    "(sets ifblock, whileblock and forblock)",
	    "Body is a single statement, not a compound block.",
	    
	  },

	  /* 8.3.5 Complete Logic */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "elseifcomplete",

	    "if ... else if chains must have final else",
	    "There is no final else following an else if construct.",
	    
	  },

	  /* 8.4 Suspicious Statements */

	  {
	    "FK_CONTROL, FK_NONE, modeFlag",
	    "unreachable",

	    "unreachable code detected",
	    "This code will never be reached on any possible execution.",
	    
	  },

	  /* 8.4.1 Statements with No Effects */

	  {
	    "FK_EFFECT, FK_CONTROL, modeFlag",
	    "noeffect",

	    "statement with no effect",
	    "Statement has no visible effect --- no values are modified.",
	    
	  },
	  {
	    "FK_EFFECT, FK_CONTROL, modeFlag",
	    "noeffectuncon",

	    "statement with no effect (except possibly through call to "+
	    "unconstrained function)",
	    "Statement has no visible effect --- no values are modified. It may "+
	    "modify something through a call to an unconstrained function.",
	    
	  },

	  /* 8.4.2 Ignored Return Values */

	  {
	    "FK_IGNORERET, FK_NONE, specialFlag",
	    "retval",

	    "return value ignored (sets retvalint, retvalbool and retvalother)",
	    "Result returned by function call is not used. If this is intended, "+
	       "cast result to (void) to eliminate message.",
	    
	  },
	  {
	    "FK_IGNORERET, FK_BOOL, modeFlag",
	    "retvalother",

	    "return value of type other than bool or int ignored",
	    "Result returned by function call is not used. If this is intended, "+
	       "can cast result to (void) to eliminate message.",
	    
	  },
	  {
	    "FK_IGNORERET, FK_BOOL, modeFlag",
	    "retvalbool",

	    "return value of manifest type bool ignored",
	    "Result returned by function call is not used. If this is intended, "+
	       "can cast result to (void) to eliminate message.",
	    
	  },
	  {
	    "FK_IGNORERET, FK_NONE, modeFlag",
	    "retvalint",

	    "return value of type int ignored",
	    "Result returned by function call is not used. If this is intended, "+
	       "can cast result to (void) to eliminate message.",
	    
	  },

	  /*
	  ** 9. Buffer Sizes 
	  */

	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "nullterminated",

	    "misuse of nullterminated allocation",
	    "A possibly non-nullterminated string/memory is used/referenced as a nullterminated one.",
	    
	  },
	  {
	    "FK_BOUNDS, FK_MEMORY, specialFlag",
	    "bounds",

	    "memory bounds checking (sets boundsread and boundswrite)",
	    "Memory read or write may be out of bounds of allocated storage.", 
	  },
	  {
	    "FK_BOUNDS, FK_MEMORY, specialFlag",
	    "likelybounds",

	    "memory bounds checking (sets likelyboundsread and likelyboundswrite)",
	    "Memory read or write may be out of bounds of allocated storage.", 
	  },
	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "likelyboundsread",

	    "likely out of bounds read",
	    "A memory read references memory beyond the allocated storage.",
	    
	  },
	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "likelyboundswrite",

	    "likely buffer overflow from an out of bounds write",
	    "A memory write may write to an address beyond the allocated buffer.",
	    
	  },
	  
	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "boundsread",

	    "possible out of bounds read",
	    "A memory read references memory beyond the allocated storage.",
	    
	  },
	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "boundswrite",

	    "possible buffer overflow from an out of bounds write",
	    "A memory write may write to an address beyond the allocated buffer.",
	    
	  },
	  
	  {
	    "FK_BOUNDS, FK_DISPLAY, plainFlag",
	    "fcnpost",

	    "display function post conditions",
	    "Display function post conditions.",
	    
	  },
	  {
	    "FK_BOUNDS, FK_DISPLAY, plainFlag",
	    "redundantconstraints",

	    "display seemingly redundant constraints",
	    "Display seemingly redundant constraints",
	    
	  },
	  /*drl7x added 6/18/01 */    
	  {
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "checkpost",

	    "unable to verify predicate in ensures clause",
	    "The function implementation may not satisfy a post condition given in an ensures clause.",
	    
	  },

	  {
	    "FK_BOUNDS, FK_MEMORY, plainFlag",
	    "impboundsconstraints",

	    "generate implicit constraints for functions",
	    "",
	    
	  },
	  /*drl7x added 4/29/01 */    
	  {
	    "FK_BOUNDS, FK_MEMORY, plainFlag",
	    "orconstraint",

	    "use limited OR expressions to resolve constraints",
	    "",
	    
	  },
	  {
	    "FK_BOUNDS, FK_DISPLAY, plainFlag",
	    "showconstraintparens",

	    "display parentheses around constraint terms",
	    "",
	    
	  },  
	  /*drl added 2/4/2002*/
	  {
	    "FK_BOUNDS, FK_DISPLAY, plainFlag",
	    "boundscompacterrormessages",

	    "Display fewer new lines in bounds checking error messages",
	    "",
	    
	  },  
	  {
	    "FK_BOUNDS, FK_DISPLAY, plainFlag",
	    "showconstraintlocation",

	    "display location for every constraint generated",
	    "",
	    
	  }, /*drl added flag 4/26/01*/

	  { /* evans added 2003-06-08 */
	    "FK_BOUNDS, FK_MEMORY, modeFlag",
	    "allocmismatch",

	    "type conversion involves storage of non-divisble size",
	    "", 
	  },

	  /*
	  ** 10. Extensible Checking 
	  */

	  {
	    "FK_EXTENSIBLE, FK_FILES, globalStringFlag",
	    "mts",

	    "load meta state declaration and corresponding xh file", 
	    "", 
	  },
	  {
	    "FK_EXTENSIBLE, FK_MEMORY, modeFlag",
	    "statetransfer",

	    "storage has been transfered with invalid state",
	    "Transfer violates user-defined state rules.",
	    
	  },
	  {
	    "FK_EXTENSIBLE, FK_MEMORY, modeFlag",
	    "statemerge",

	    "control paths merge with storage in incompatible states",
	    "Control path merge violates user-defined state merge rules.",
	    
	  },

	  /* 
	  ** 11. Macros 
	  */

	  {
	    "FK_MACROS, FK_NONE, modeFlag",
	    "macroredef",

	    "macro redefined",
	    "A macro is defined in more than one place.", 
	  },
	  {
	    "FK_MACROS, FK_UNRECOG, modeFlag",
	    "macrounrecog",

	    "unrecognized identifier in macro",
	    "An unrecognized identifier appears in a macro. If the identifier "+
	    "is defined before the macro is used, then this is okay.",
	    
	  },

	  /* 11.1 Constant Macros */

	  {
	    "FK_MACROS, FK_PROTOS, modeFlag",
	    "macroconstdecl",

	    "non-parameterized macro without prototype or specification",
	    "Macro constant has no declaration. Use /*@constant ...@*/ to "+
	    "declare the macro.",
	    
	  },

	  {
	    "FK_MACROS, FK_PROTOS, plainFlag",
	    "macroconstdistance",

	    "macro constant name does not match nearby name",
	    "Macro constant name does matches name of a previous declaration, but they are not near each other.",
	    
	  },

	  /* 11.2 Function-like Macros */
	  
	  {
	    "FK_MACROS, FK_NONE, modeFlag",
	    "macrostmt",

	    "macro definition is syntactically not equivalent to function",
	    "A macro is defined in a way that may cause syntactic problems. "+
	    "If the macro returns a value, use commas to separate expressions; "+
	    "otherwise, use do { <macro body> } while (FALSE) construct.",
	    
	  },
	  {
	    "FK_MACROS, FK_NONE, modeFlag",
	    "macroempty",

	    "macro definition for is empty",
	    "A macro definition has no body.",
	    
	  },
	  {
	    "FK_MACROS, FK_PARAMS, modeFlag",
	    "macroparams",

	    "macro parameter not used exactly once",
	    "A macro parameter is not used exactly once in all possible "+
	    "invocations of the macro. To behave like a function, "+
	    "each macro parameter must be used exactly once on all "+
	    "invocations of the macro so that parameters with "+
	    "side-effects are evaluated exactly once. Use /*@sef@*/ to "+
	    "denote parameters that must be side-effect free.",
	    
	  },
	  {
	    "FK_MACROS, FK_CONTROL, modeFlag",
	    "macroret",

	    "return statement in macro body",
	    "The body of a macro declared as a function uses a return statement. "+
	    "This exhibits behavior that "+
	    "could not be implemented by a function.", 
	    
	  },
	  {
	    "FK_MACROS, FK_PARAMS, modeFlag",
	    "macroassign",

	    "assignment to a macro parameter",
	    "A macro parameter is used as the left side of an "+
	    "assignment expression. This exhibits behavior that "+
	    "could not be implemented by a function.", 
	    
	  },
	  {
	    "FK_MACROS, FK_NONE, modeFlag",
	    "macroparens",

	    "macro parameter used without parentheses (in potentially dangerous context)",
	    "A macro parameter is used without parentheses. This could be "+
	    "dangerous if the macro is invoked with a complex expression "+
	    "and precedence rules will change the evaluation inside the macro.",
	    
	  },
	  {
	    "FK_MACROS, FK_PROTOS, modeFlag",
	    "macrodecl",

	    "macro without prototype or specification (sets macrofcndecl and macroconstdecl)",
	    "Argument checking cannot be done well for macros without prototypes "+
	    "or specifications, since the types of the arguments are unknown.",
	    
	  },
	  {
	    "FK_MACROS, FK_PROTOS, modeFlag",
	    "macrofcndecl",

	    "parameterized macro without prototype or specification",
	    "Function macro has no declaration.",
	    
	  },

	  /* 11.2.1 Side Effect Free Parameters */

	  {
	    "FK_MACROS, FK_PARAMS, modeFlag",
	    "sefparams",

	    "a parameter with side-effects is passed as a sef parameter",
	    "An actual parameter corresponding to a sef parameter may have a side-effect.",
	    
	  },
	  {
	    "FK_MACROS, FK_PARAMS, modeFlag",
	    "sefuncon",

	    "a parameter with unconstrained side-effects is passed as a sef parameter",
	    "An actual parameter corresponding to a sef parameter involves a call "+
	    "to a procedure with no modifies clause that may have a side-effect.",
	    
	  },

	  /* 11.3 Controlling Macro Checking */

	  {
	    "FK_MACROS, FK_NONE, plainFlag",
	    "constmacros",

	    "check all macros without parameter lists as constants",
	    "Every non-parameterized macro (not preceded by /*@notfunction@*/) "+
	    "is checked as a constant.",
	    	
	  },
	  {
	    "FK_MACROS, FK_NONE, plainFlag",
	    "fcnmacros",

	    "check all macros with parameter lists as functions",
	    "Every parameterized macro (not preceded by /*@notfunction@*/) "+
	    "is checked as a function.",
	    	
	  },
	  {
	    "FK_MACROS, FK_NONE, plainSpecialFlag",
	    "allmacros",

	    "sets fcnmacros and constmacros",
	    "All macros (not preceded by /*@notfunction@*/) are checked as functions or "+
	    "constants depending on whether or not they have parameter lists.",
	    	
	  },
	  {
	    "FK_MACROS, FK_NONE, plainFlag",
	    "libmacros",

	    "check all macros with declarations in library as functions",
	    "Every macro declared in the load library is checked.",
	    	
	  },
	  {
	    "FK_MACROS, FK_NONE, plainFlag",
	    "specmacros",

	    "check all macros corresponding to specified functions or constants",
	    "Every macro declared a specification file is checked.",
	    	
	  },
	  {
	    "FK_MACROS, FK_NONE, modeFlag",
	    "macromatchname",

	    "macro definition does not match iter or constant declaration",
	    "A iter or constant macro is defined using a different name from the "+
	    "one used in the previous syntactic comment",
	    
	  },
	  {
	    "FK_MACROS, FK_NONE, plainFlag",
	    "nextlinemacros",

	    "the line after a constant or iter declaration must be a macro definition",
	    "A constant or iter declaration is not immediately followed by a macro definition.",
	    
	  },

	  /* 11.4 Iterators */

	  {
	    "FK_ITER, FK_NONE, plainFlag",
	    "iterbalance",

	    "iter is not balanced with end_<iter>",
	    "",
	    
	  },
	  {
	    "FK_ITER, FK_NONE, plainFlag",
	    "iteryield",

	    "iter yield parameter is inappropriate",
	    "",
	    
	  },
	  {
	    "FK_ITER, FK_NONE, plainFlag",
	    "hasyield",

	    "iter declaration has no yield parameters",
	    "An iterator has been declared with no parameters annotated with "+
	    "yield. This may be what you want, if the iterator is meant "+
	    "to do something a fixed number of times, but returns no "+
	    "information to the calling context. Probably, a parameter "+
	    "is missing the yield annotation to indicate that it is "+
	    "assigned a value in the calling context.",
	    
	  },

	  /*
	  ** 12. Naming Conventions 
	  */

	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "namechecks",

	    "controls name checking without changing other settings",
	    "", 
	  },

	  /* 12.1.1 Czech Names */

	  {
	    "FK_NAMES, FK_ABSTRACT, specialFlag",
	    "czech",

	    "czech naming convention (sets accessczech, czechfunctions, czechvars, "+
	    "czechconstants, czechenums, and czechmacros)",
	    "Name is not consistent with Czech naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechfcns",

	    "czech naming convention violated in a function or iterator declaration",
	    "Function or iterator name is not consistent with Czech naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechvars",

	    "czech naming convention violated in a variable declaration",
	    "Variable name is not consistent with Czech naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechmacros",

	    "czech naming convention violated in an expanded macro name",
	    "Expanded macro name is not consistent with Czech naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechconsts",

	    "czech naming convention violated in a constant declaration",
	    "Constant name is not consistent with Czech naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechtypes",

	    "czech naming convention violated in a user-defined type definition",
	    "Type name is not consistent with Czech naming convention. Czech type "+
	    "names must not use the underscore character.", 
	  },

	  /* 12.1.2 Slovak Names */

	  {
	    "FK_NAMES, FK_ABSTRACT, specialFlag",
	    "slovak",

	    "slovak naming convention violated",
	    "Name is not consistent with Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "slovakfcns",

	    "slovak naming convention violated in a function or iterator declaration",
	    "Function or iterator name is not consistent with Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "slovakmacros",

	    "slovak naming convention violated in an expanded macro name",
	    "Expanded macro name is not consistent with Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "slovakvars",

	    "slovak naming convention violated in a variable declaration",
	    "Variable name is not consistent with Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "slovakconsts",

	    "slovak naming convention violated in a constant declaration",
	    "Constant name is not consistent with Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "slovaktypes",

	    "slovak naming convention violated in a use-defined type definition",
	    "Type name is not consistent with Slovak naming convention. Slovak type "+
	    "names may not include uppercase letters.", 
	  },

	  /* 12.1.3 Czechoslovak Names */
	  {
	    "FK_NAMES, FK_ABSTRACT, specialFlag",
	    "czechoslovak",

	    "czech or slovak naming convention violated",
	    "Name is not consistent with either Czech or Slovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechoslovakfcns",

	    "czechoslovak naming convention violated in a function or iterator declaration",
	    "Function name is not consistent with Czechoslovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechoslovakmacros",

	    "czechoslovak naming convention violated in an expanded macro name",
	    "Expanded macro name is not consistent with Czechoslovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechoslovakvars",

	    "czechoslovak naming convention violated in a variable declaration",
	    "Variable name is not consistent with Czechoslovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechoslovakconsts",

	    "czechoslovak naming convention violated in a constant declaration",
	    "Constant name is not consistent with Czechoslovak naming convention.", 
	  },
	  {
	    "FK_NAMES, FK_ABSTRACT, plainFlag",
	    "czechoslovaktypes",

	    "czechoslovak naming convention violated in a user-defined type definition",
	    "Type name is not consistent with Czechoslovak naming convention. Czechoslovak "+
	    "type names may not include uppercase letters or the underscore character.", 
	  },

	  /* 12.2 Namespace Prefixes */

	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "macrovarprefix",

	    "set namespace prefix for variables declared in a macro body",
	    "A variable declared in a macro body does not start with the macrovarprefix.",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",	
	    "macrovarprefixexclude",

	    "the macrovarprefix may not be used for non-macro variables",
	    "A variable declared outside a macro body starts with the macrovarprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "tagprefix",

	    "set namespace prefix for struct, union and enum tags",
	    "A tag identifier does not start with the tagprefix.",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "tagprefixexclude",

	    "the tagprefix may not be used for non-tag identifiers",
	    "An identifier that is not a tag starts with the tagprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "enumprefix",

	    "set namespace prefix for enum members",
	    "An enum member does not start with the enumprefix.",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "enumprefixexclude",

	    "the enumprefix may not be used for non-enum member identifiers",
	    "An identifier that is not an enum member starts with the enumprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "filestaticprefix",

	    "set namespace prefix for file static declarations",
	    "A file-static identifier does not start with the filestaticprefix.",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "filestaticprefixexclude",

	    "the filestaticprefix may not be used for identifiers that are not file static",
	    "An identifier that is not file static starts with the filestaticprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "globalprefix",

	    "set namespace prefix for global variables",
	    "A global variable does not start with the globalprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "globalprefixexclude",

	    "the globalprefix may not be used for non-global identifiers",
	    "An identifier that is not a global variable starts with the globalprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "typeprefix",

	    "set namespace prefix for user-defined types",
	    "A user-defined type does not start with the typeprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "typeprefixexclude",

	    "the typeprefix may not be used for identifiers that are not type names",
	    "An identifier that is not a type name starts with the typeprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "externalprefix",

	    "set namespace prefix for external identifiers",
	    "An external identifier does not start with the externalprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "externalprefixexclude",

	    "the externalprefix may not be used for non-external identifiers",
	    "An identifier that is not external starts with the externalprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "localprefix",

	    "set namespace prefix for local variables",
	    "A local variable does not start with the localprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "localprefixexclude",

	    "the localprefix may not be used for non-local identifiers",
	    "An identifier that is not a local variable starts with the localprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "uncheckedmacroprefix",

	    "set namespace prefix for unchecked macros",
	    "An unchecked macro name does not start with the uncheckedmacroprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "uncheckedmacroprefixexclude",

	    "the uncheckmacroprefix may not be used for identifiers that are not "+
	    "unchecked macros",
	    "An identifier that is not the name of an unchecked macro "+
	    "starts with the uncheckedmacroprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "constprefix",

	    "set namespace prefix for constants",
	    "A constant does not start with the constantprefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "constprefixexclude",

	    "the constprefix may not be used for non-constant identifiers",
	    "An identifier that is not a constant starts with the constantprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "iterprefix",

	    "set namespace prefix for iterators",
	    "An iter does not start with the iterator prefix",
	    
	  } ,
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "iterprefixexclude",

	    "the iterprefix may not be used for non-iter identifiers",
	    "An identifier that is not a iter starts with the iterprefix.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, idemStringFlag",
	    "protoparamprefix",

	    "set namespace prefix for parameters in function prototype declarations",
	    "A parameter name in a function prototype declaration does not start with the "+
	    "declaration parameter prefix",
	    
	  } ,

	  /* 12.3 Naming Restrictions */
	  {
	    "FK_NAMES, FK_ANSI, modeFlag",
	    "isoreserved",

	    "external name conflicts with name reserved for system or standard library",
	    "External name is reserved for system use by ISO C99 standard.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, modeFlag",
	    "cppnames",

	    "external or internal name is a C++ keyword or reserved word",
	    "External name is a C++ keyword or reserved word. "+
	    "This could lead to problems if the "+
	    "code is compiled with a C++ compiler.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, modeFlag",
	    "isoreservedinternal",

	    "internal name conflicts with name reserved for system or standard library",
	    "Internal name is reserved for system in ISO C99 standard (this should not be necessary unless you are worried about C library implementations that violate the standard and use macros).",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, plainFlag",
	    "distinctexternalnames",

	    "external name is not distinguishable from another external name using "+
	    "the number of significant characters",
	    "An external name is not distinguishable from another external name "+
	    "using the number of significant characters. According to "+
	    "ANSI Standard (3.1), an implementation may only consider the first 6 "+
	    "characters significant, and ignore alphabetical case "+
	    "distinctions (ISO C99 requires 31). The "+
	    "+externalnamelen <n> flag may be used to change the number "+
	    "of significant characters, and -externalnamecaseinsensitive to make "+
	    "alphabetical case significant in external names.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, specialValueFlag",
	    "externalnamelen",

	    "set the number of significant characters in an external name",
	    "Sets the number of significant characters in an external name (default is 6 for old "+
	    "ANSI89 limit, C99 requires 31). "+
	    "Sets +distinctexternalnames.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, plainSpecialFlag",
	    "externalnamecaseinsensitive",

	    "alphabetic comparisons for external names are case-insensitive",
	    "Make alphabetic case insignificant in external names. By ANSI89 "+
	    "standard, case need not be significant in an external name. "+
	    "If +distinctexternalnames is not set, sets "+
	    "+distinctexternalnames with unlimited external name length.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, plainFlag",
	    "distinctinternalnames",

	    "internal name is not distinguishable from another internal name using "+
	    "the number of significant characters",
	    "An internal name is not distinguishable from another internal name "+
	    "using the number of significant characters. According to "+
	    "ANSI89 Standard (3.1), an implementation may only consider the first 31 "+
	    "characters significant (ISO C99 specified 63). The "+
	    "+internalnamelen <n> flag changes the number "+
	    "of significant characters, -internalnamecaseinsensitive to makes "+
	    "alphabetical case significant, and "+
	    "+internalnamelookalike to make similar-looking characters "+
	    "non-distinct.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, specialValueFlag",
	    "internalnamelen",

	    "set the number of significant characters in an internal name",
	    "Sets the number of significant characters in an internal name (ANSI89 "+
	    "default is 31.)  Sets +distinctinternalnames.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, plainSpecialFlag",
	    "internalnamecaseinsensitive",

	    "set whether case is significant an internal names "+
	    "(-internalnamecaseinsensitive means case is significant)" ,
	    "Set whether case is significant an internal names "+
	    "(-internalnamecaseinsensitive "+
	    "means case is significant). By ANSI89 default, case is not "+
	    "significant.  If +distinctinternalnames is not set, sets "+
	    "+distinctinternalnames with unlimited internal name length.",
	    
	  },
	  {
	    "FK_NAMES, FK_ANSI, plainSpecialFlag",
	    "internalnamelookalike",

	    "lookalike characters match in internal names",
	    "Set whether similar looking characters (e.g., \"1\" and \"l\") "+
	    "match in internal names.",
	    
	  },
	  {
	    "FK_NAMES, FK_PREFIX, modeFlag",
	    "protoparamname",

	    "a parameter in a function prototype has a name",
	    "A parameter in a function prototype has a name.  This is dangerous, "+
	    "since a macro definition could be visible here.",
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, modeFlag",
	    "protoparammatch",

	    "the name of a parameter in a function prototype and corresponding "+
	    "declaration must match (after removing the protoparamprefix", 
	    "A parameter in a function definition does not have the same name as "+
	    "the corresponding in the declaration of the function after "+
	    "removing the protoparamprefix", 
	    	
	  } ,	
	  {
	    "FK_NAMES, FK_PREFIX, plainFlag",
	    "protoparamprefixexclude",

	    "the protoparamprefix may not be used for non-declaraction parameter identifiers",
	    "An identifier that is not a parameter name in a function prototype "+
	    "starts with the protoparamprefix.",
	    	
	  } ,	

	  /*
	  ** 13. Completeness 
	  */

	  /* 13.1 Unused Declarations */

	  {
	    "FK_USE, FK_COMPLETE, modeFlag",
	    "topuse",

	    "declaration at top level not used",
	    "An external declaration not used in any source file.", 
	  },
	  {
	    "FK_USE, FK_EXPORT, modeFlag",
	    "exportlocal",

	    "a declaration is exported but not used outside this module",
	    "A declaration is exported, but not used outside this module. "+
	    "Declaration can use static qualifier.",
	    
	  },
	  {
	    "FK_USE, FK_EXPORT, modeFlag",
	    "exportheader",

	    "a declaration is exported but does not appear in a header file",
	    "A declaration is exported, but does not appear in a header file.",
	    
	  },
	  {
	    "FK_USE, FK_EXPORT, modeFlag",
	    "exportheadervar",

	    "a variable declaration is exported but does not appear in a header file",
	    "A variable declaration is exported, but does not appear in a header "+
	    "file. (Used with exportheader.)",
	    
	  },
	  {
	    "FK_USE, FK_NONE, modeFlag",
	    "fielduse",

	    "field of structure type not used",
	    "A field is present in a structure type but never used. Use /*@unused@*/ in front of field declaration to suppress message.",
	    
	  },
	  {
	    "FK_USE, FK_NONE, modeFlag",
	    "enummemuse",

	    "member of an enum type not used",
	    "A member of an enum type is never used.",
	    
	  },
	  {
	    "FK_USE, FK_NONE, modeFlag",
	    "constuse",

	    "constant declared but not used",
	    "A constant is declared but not used. Use unused in the constant declaration to suppress message.",
	    
	  },
	  {
	    "FK_USE, FK_NONE, modeFlag",
	    "fcnuse",

	    "function declared but not used",
	    "A function is declared but not used. Use /*@unused@*/ in front of function header to suppress message.",
	    
	  },
	  {
	    "FK_USE, FK_PARAMS, modeFlag",
	    "paramuse",

	    "function parameter not used ",
	    "A function parameter is not used in the body of the function. If the argument is needed for type compatibility or future plans, use /*@unused@*/ in the argument declaration.",
	    
	  },
	  {
	    "FK_USE, FK_TYPE, modeFlag",
	    "typeuse",

	    "type declared but not used",
	    "A type is declared but not used. Use /*@unused@*/ in front of typedef to suppress messages.",
	    
	  },
	  {
	    "FK_USE, FK_NONE, modeFlag",
	    "varuse",

	    "variable declared but not used",
	    "A variable is declared but never used. Use /*@unused@*/ in front "+
	    "of declaration to suppress message.",
	    
	  },
	  {
	    "FK_USE, FK_COMPLETE, modeFlag",
	    "unusedspecial",

	    "unused declaration in special file (corresponding to .l or .y file)",
	    "", 
	  } ,

	  /* 13.2 Complete Programs */

	  {
	    "FK_COMPLETE, FK_NONE, modeFlag",
	    "declundef",

	    "function or variable declared but never defined",
	    "A function or variable is declared, but not defined in any source code file.",
	    
	  },
	  {
	    "FK_COMPLETE, FK_SPEC, modeFlag",
	    "specundef",

	    "function or variable specified but never defined",
	    "A function or variable is declared in an .lcl file, but not defined in any source code file.",
	    
	  },
	  {
	    "FK_COMPLETE, FK_SPEC, plainFlag",
	    "specundecl",

	    "function or variable specified but never declared in a source file",
	    "A function or variable is declared in an .lcl file, but not declared "+
	    "in any source code file.",
	    
	  },
	  {
	    "FK_DECL, FK_LIBS, plainFlag",
	    "newdecl",

	    "report new global declarations in source files",
	    "There is a new declaration that is not declared in a loaded library "+
	    "or earlier file.  (Use this flag to check for consistency "+
	    "against a library.)",
	    
	  },
	  {
	    "FK_INIT, FK_SPEC, plainFlag",
	    "needspec",

	    "information in specifications is not also included in syntactic comments",
	    "There is information in the specification that is not duplicated "+
	    "in syntactic comments. Normally, this is not an "+
	    "error, but it may be useful to detect it to make "+
	    "sure checking incomplete systems without the specifications will "+
	    "still use this information.",
	    
	  },

	  /*
	  ** 14. Libraries and Header File Inclusion 
	  */

	  /* 14.1 Standard Libraries */

	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "nolib",

	    "do not load standard library",
	    "", 
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "isolib",

	    "use normal standard library",
	    "Library based on the ISO standard library specification is used.", 
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "strictlib",

	    "interpret standard library strictly",
	    "Stricter version of the standard library is used. (The default "+
	    "library is standard.lcd;  strict library is strict.lcd.)", 
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "unixlib",

	    "use UNIX (sort-of) standard library",
	    "UNIX version of the standard library is used.",
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "unixstrictlib",

	    "use strict version of UNIX (sort-of) library",
	    "strict version of the UNIX library is used.",
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "posixlib",

	    "use POSIX standard library",
	    "POSIX version of the standard library is used.",
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "posixstrictlib",

	    "use strict POSIX standard library",
	    "POSIX version of the strict standard library is used.",
	    
	  },
	  {
	    "FK_LIBS, FK_INIT, idemGlobalFlag",
	    "whichlib",

	    "show standard library filename",
	    "", 
	  },
	  {
	    "FK_LIBS, FK_ANSI, plainFlag",
	    "warnposixheaders",

	    "a POSIX header is included, but the POSIX library is not used",
	    "Header name matches a POSIX header, but the POSIX library is not selected.",
	    
	  },
	  {
	    "FK_LIBS, FK_ANSI, plainFlag",
	    "warnunixlib",

	    "warn when the unix library is used",
	    "Unix library may not be compatible with all platforms", 
	    
	  },
	  {
	    "FK_LIBS, FK_ANSI, plainFlag",
	    "usevarargs",

	    "non-standard <varargs.h> included",
	    "Header <varargs.h> is not part of ANSI Standard. "+
	    "Should use <stdarg.h> instead.",
	    
	  },
	  {
	    "FK_HEADERS, FK_FILES, plainFlag",
	    "caseinsensitivefilenames",

	    "file names are case insensitive (file.h and FILE.H are the same file)",
	    "", 
	  },

	  /* 14.2 Generating Libraries */

	  {
	    "FK_LIBS, FK_FILES, globalStringFlag",
	    "dump",

	    "save state for merging (default suffix .lcd)",
	    "", 
	  },
	  {
	    "FK_LIBS, FK_FILES, globalStringFlag",
	    "load",

	    "load state from dump file (default suffix .lcd)",
	    "", 
	  },

	  /* 14.3 Header File Inclusion */

	  {
	    "FK_HEADERS, FK_SPEED, globalFlag",
	    "singleinclude",

	    "optimize header inclusion to eliminate redundant includes",
	    "When checking multiple files, each header file is processed only "+
	    "once. This may change the meaning of the code, if the "+
	    "same header file is included in different contexts (e.g., the "+
	    "header file includes #if directives and the values are "+
	    "different when it is included in different places.)",
	    
	  },
	  {
	    "FK_HEADERS, FK_SPEED, globalFlag",
	    "neverinclude",

	    "optimize header inclusion to not include any header files",
	    "Ignore header includes. Only works if relevant information is "+
	    "loaded from a library.",
	    
	  },
	  {
	    "FK_HEADERS, FK_SPEED, globalFlag",
	    "skipsysheaders",

	    "do not include header files in system directories (set by -sysdirs)",
	    "Do not include header files in system directories (set by -sysdirs)",
	    
	  },

	  /* 
	  ** A. Operation?
	  */


	  /*
	  ** Syntax 
	  */

	  {
	    "FK_SYNTAX, FK_ANSI, plainFlag",
	    "gnuextensions",

	    "support some gnu (gcc) language extensions",
	    "ANSI C does not allow some language features supported by gcc and other compilers. "+
	    "Use +gnuextensions to allow some of these extensions.", 
	  },

	  /* Prototypes */

	  {
	    "FK_PROTOS, FK_ANSI, modeFlag",
	    "noparams",

	    "function declaration has no parameter list",
	    "A function declaration does not have a parameter list.",
	    
	  },
	  {
	    "FK_PROTOS, FK_ANSI, modeFlag",
	    "oldstyle",

	    "old style function definition",
	    "Function definition is in old style syntax. Standard prototype "+
	    "syntax is preferred.",
	    
	  },


	  /*
	  ** System functions
	  */

	  {
	    "FK_SYSTEMFUNCTIONS, FK_TYPE, plainFlag",
	    "maintype",

	    "type of main does not match expected type",
	    "The function main does not match the expected type.",
	    
	  },
	  {
	    "FK_SYSTEMFUNCTIONS, FK_BEHAVIOR, modeFlag",
	    "exitarg",

	    "argument to exit has implementation defined behavior",
	    "The argument to exit should be 0, EXIT_SUCCESS or EXIT_FAILURE",
	    
	  },

	  {
	    "FK_DECL, FK_NONE, modeFlag",
	    "shadow",

	    "declaration reuses name visible in outer scope",
	    "An outer declaration is shadowed by the local declaration.",
	    
	  },
	  {
	    "FK_DECL, FK_LIBS, modeFlag",
	    "incondefslib",

	    "function, variable or constant defined in a library is redefined with inconsistent type",
	    "A function, variable or constant previously defined in a library is "+
	    "redefined with a different type.",
	    
	  },
	  {
	    "FK_DECL, FK_LIBS, modeFlag",
	    "overload",

	    "library function overloaded",
	    "A function, variable or constant defined in the library is redefined "+
	    "with a different type.",
	    
	  },
	  {
	    "FK_DECL, FK_NONE, modeFlag",
	    "nestedextern",

	    "an extern declaration is inside a function scope",
	    "An extern declaration is used inside a function scope.",
	    
	  },	
	  {
	    "FK_DECL, FK_NONE, modeFlag",
	    "redecl",

	    "function or variable redeclared",
	    "A function or variable is declared in more than one place. This is "+
	    "not necessarily a problem, since the declarations are consistent.",
	    
	  },	
	  {
	    "FK_DECL, FK_NONE, plainFlag",
	    "redef",

	    "function or variable redefined",
	    "A function or variable is redefined. One of the declarations should use extern.",
	    
	  },
	  {
	    "FK_DECL, FK_TYPE, modeFlag",
	    "imptype",

	    "variable declaration has unknown (implicitly int) type",
	    "A variable declaration has no explicit type.  The type is implicitly int.",
	    
	  },

	  {
	    "FK_DIRECT, FK_FILES, globalStringFlag",
	    "tmpdir",

	    "set directory for writing temp files",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_FILES, globalStringFlag",
	    "larchpath",

	    "set path for searching for library files (overrides LARCH_PATH environment variable)",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_FILES, globalStringFlag",
	    "lclimportdir",

	    "set directory to search for LCL import files (overrides LCLIMPORTDIR)",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_FILES, globalStringFlag",
	    "sysdirs",

	    "set directories for system files (default /usr/include). Separate "+
	    "directories with path separator (colons in Unix, semi-colons in Windows). "+
	    "Flag settings propagate to files in a system directory. If "+
	    "-sysdirerrors is set, no errors are reported for files in "+
	    "system directories.",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_FILES, plainFlag",
	    "skipisoheaders",

	    "prevent inclusion of header files in a system directory with "+
	    "names that match standard ANSI headers. The symbolic information "+
	    "in the standard library is used instead.  Flag in effect only "+
	    "if a library including the ANSI library is loaded.  The ANSI "+
	    "headers are: assert, ctype, errno, float, limits, locale, math, "+
	    "setjmp, signal, stdarg, stddef, stdio, stdlib, strings, string, "+
	    "time, and wchar.",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_FILES, plainFlag",
	    "skipposixheaders",

	    "prevent inclusion of header files in a system directory with "+
	    "names that match standard POSIX headers. The symbolic information "+
	    "in the posix library is used instead.  The POSIX headers are: "+
	    "dirent, fcntl, grp, pwd, termios, sys/stat, sys/times, "+
	    "sys/types, sys/utsname, sys/wait, unistd, and utime.",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_SUPPRESS, modeFlag",
	    "sysdirerrors",

	    "report errors in files in system directories (set by -sysdirs)",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_MACROS, plainFlag",
	    "sysdirexpandmacros",

	    "expand macros in system directories regardless of other settings, "+
	    "except for macros corresponding to names defined in a load library",
	    "", 
	  },

	  {
	    "FK_DIRECT, FK_HEADERS, globalExtraArgFlag",
	    "I<directory>",

	    "add to C include path",
	    "", 
	  },
	  {
	    "FK_DIRECT, FK_SPEC, globalExtraArgFlag",
	    "S<directory>",

	    "add to spec path",
	    "", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, specialFlag",
	    "exportany",

	    "variable, function or type exported but not specified",
	    "A variable, function or type is exported, but not specified.",
	    
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exportfcn",

	    "function exported but not specified",
	    "A function is exported, but not specified.", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exportmacro",

	    "expanded macro exported but not specified",
	    "A macro is exported, but not specified.", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exporttype",

	    "type definition exported but not specified",
	    "A type is exported, but not specified.", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exportvar",

	    "variable exported but not specified",
	    "A variable is exported, but not specified.", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exportconst",

	    "constant exported but not specified",
	    "A constant is exported, but not specified.", 
	  },
	  {
	    "FK_EXPORT, FK_SPEC, modeFlag",
	    "exportiter",

	    "constant exported but not specified",
	    "A constant is exported, but not specified.", 
	  },

	  {
	    "FK_FORMAT, FK_DISPLAY, valueFlag",
	    "linelen",

	    "set length of messages (number of chars)",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, valueFlag",
	    "indentspaces",

	    "set number of spaces to indent sub-messages",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, valueFlag",
	    "locindentspaces",

	    "set number of spaces to indent sub-messages that start with file locations",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "showdeephistory",

	    "show all available information about storage mentioned in warnings",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "showcolumn",

	    "show column number where error is found",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "showloadloc",

	    "show location information for load files",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, globalFileFlag",
	    "csv",

	    "produce comma-separated values (CSV) warnings output file",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "csvoverwrite",

	    "overwrite exisiting CVS output file",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "parenfileformat",

	    "show column number where error is found",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_DISPLAY, plainFlag",
	    "htmlfileformat",

	    "show file locations as links",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_NONE, plainFlag",
	    "showfunc",

	    "show name of function containing error",
	    "", 
	  },
	  {
	    "FK_FORMAT, FK_NONE, plainFlag",
	    "showallconjs",

	    "show all possible types",
	    "When a library function is declared with multiple possible type, the "+
	    "alternate types are shown only if +showallconjs.", 
	    
	  },
	  {
	    "FK_LIBS, FK_NONE, plainFlag", 
	    "impconj",
	 
	    "make all alternate types implicit (useful for making system libraries",
	    "", 
	  } ,
	  {
	    "FK_GLOBAL, FK_ERRORS, globalValueFlag",
	    "expect",

	    "expect <int> code errors",
	    "", 
	  },
	  {
	    "FK_GLOBAL, FK_ERRORS, globalValueFlag",
	    "lclexpect",

	    "expect <int> spec errors",
	    "", 
	  },
	  {
	    "FK_GLOBAL, FK_USE, idemSpecialFlag",
	    "partial",

	    "check as partial system (-specundef, -declundef, -exportlocal, "+
	    "don't check macros in headers without corresponding .c files)",
	    "", 
	  },

	  /*
	  ** Appendix D. Specifications 
	  */

	  {
	    "FK_HEADERS, FK_SPEC, globalFlag",
	    "lh",

	    "generate .lh files", "",
	    
	  },
	  {
	    "FK_HEADERS, FK_SPEC, globalFlag",
	    "lcs",

	    "generate .lcs files", "",
	    
	  },

	  /*
	  ** 1. Operation
	  */

	  {
	    "FK_HELP, FK_NONE, plainFlag",
	    "warnflags",

	    "warn when command line sets flag in abnormal way",
	    "Command line sets flag in abnormal way",
	    
	  },
	  {
	    "FK_HELP, FK_NONE, plainFlag",
	    "warnrc",

	    "warn when there are problems with reading the initialization files",
	    "There was a problem reading an initialization file",
	    
	  },
	  {
	    "FK_HELP, FK_NONE, plainFlag",
	    "badflag",
	    "FLG_BADFlag",
	    "warn about bad command line flags", 
	    "A flag is not recognized or used in an incorrect way",
	    
	  },
	  {
	    "FK_HELP, FK_NONE, plainFlag",
	    "fileextensions",

	    "warn when command line file does not have a recognized extension",
	    "", 
	  },
	  {
	    "FK_HELP, FK_NONE, globalExtraArgFlag",
	    "help",

	    "-help <flags> will describe flags",
	    "Display help",
	    
	  },
	  {
	    "FK_INIT, FK_FILES, globalFileFlag",
	    "f",

	    "read an options file (default ~/.splintrc not loaded)",
	    "Read an options file (instead of loading default ~/.splintc)",
	    
	  },
	  {
	    "FK_INIT, FK_FILES, globalFileFlag",
	    "i",

	    "set LCL initilization file",
	    "", 
	  },
	  {
	    "FK_INIT, FK_FILES, globalFlag",
	    "nof",

	    "do not read options file",
	    "Do not read the default options file (~/.splintrc)",
	    
	  },
	  {
	    "FK_INIT, FK_COMMENTS, charFlag",
	    "commentchar",

	    "set marker character for syntactic comments (default is '@')",
	    "Set the marker character for syntactic comments. Comments beginning "+
	    "with /*<char> are interpreted by Splint, where <char> is the "+
	    "comment marker character.",
	    
	  },

	  /*
	  ** Limits 
	  */

	  {
	    "FK_LIMITS, FK_ANSI, modeValueFlag",
	    "controlnestdepth",

	    "set maximum nesting depth of compound statements, iteration control "+
	    "structures, and selection control structures (ANSI89 minimum is 15; ISO99 is 63)",
	    "Maximum number of control levels exceeded.",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, modeValueFlag",
	    "stringliterallen",

	    "set maximum length of string literals (ANSI89 minimum is 509; ISO99 is 4095)",
	    "Maximum length of string literal exceeded.",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, modeValueFlag",
	    "numstructfields",

	    "set maximum number of fields in a struct or union (ANSI89 minimum is 127; ISO99 is 1023)",
	    "Maximum number of fields in a struct or union exceeded.",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, modeValueFlag",
	    "numenummembers",

	    "set maximum number of members of an enum (ANSI89 minimum is 127; ISO99 is 1023)",
	    "Limit on maximum number of members of an enum is exceeded.",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, modeValueFlag",
	    "includenest",

	    "set maximum number of nested #include files (ANSI89 minimum is 8; ISO99 is 63)",
	    "Maximum number of nested #include files exceeded.",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, specialFlag",
	    "ansi89limits",
	    "check for violations of standard limits (controlnestdepth, "+
	    "stringliterallen, includenest, numstructfields, numenummembers) based on ANSI89 standard",
	    "",
	    
	  },
	  {
	    "FK_LIMITS, FK_ANSI, specialFlag",
	    "iso99limits",
	    "check for violations of standard limits (controlnestdepth, "+
	    "stringliterallen, includenest, numstructfields, numenummembers) based on ISO99 standard",
	    "",
	    
	  },

	  {
	    "FK_PREPROC, FK_NONE, globalExtraArgFlag",
	    "D<initializer>",

	    "passed to pre-processor",
	    "", 
	  },
	  {
	    "FK_PREPROC, FK_NONE, globalExtraArgFlag",
	    "U<initializer>",

	    "passed to pre-processor",
	    "", 
	  },
	  {
	    "FK_PREPROC, FK_SYNTAX, plainFlag",
	    "unrecogdirective",

	    "unrecognized pre-processor directive",
	    "Pre-processor directive is not recognized.", 
	    
	  },
	  {
	    "FK_SUPPRESS, FK_COMMENTS, globalFlag",
	    "supcounts",

	    "The number of errors detected does not match number in /*@i<n>@*/.",
	    "", 
	  },
	  {
	    "FK_SUPPRESS, FK_ERRORS, valueFlag",
	    "limit",

	    "limit <int> consecutive repeated errors",
	    "", 
	  },
	  {
	    "FK_SYNTAX, FK_NONE, plainFlag",
	    "syntax",

	    "syntax error in parsing",
	    "Code cannot be parsed.  For help on parse errors, see splint -help parseerrors.", 
	    
	  },
	  {
	    "FK_SYNTAX, FK_NONE, plainFlag",
	    "trytorecover",

	    "try to recover from parse error",
	    "Try to recover from parse error.  It really means try - this doesn't usually work.", 
	  },
	  {
	    "FK_SYNTAX, FK_PREPROC, plainFlag",
	    "preproc",

	    "preprocessing error",
	    "Preprocessing error.",
	    
	  },

	  {
	    "FK_TYPE, FK_NONE, plainFlag",
	    "type",

	    "type mismatch",
	    "Types are incompatible.",
	    
	  },

	  {
	    "FK_TYPE, FK_NONE, plainFlag",
	    "stringliteraltoolong",

	    "string literal too long for character array",
	    "A string literal is assigned to a char array too small to hold it.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "stringliteralnoroomfinalnull",

	    "string literal leaves no room for null terminator",
	    "A string literal is assigned to a char array that is not big enough to hold the final null terminator.  This may not be a problem because a null character has been explictedly included in the string literal using an escape sequence",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "stringliteralnoroom",

	    "string literal leaves no room for null terminator",
	    "A string literal is assigned to a char array that is not big enough to hold the null terminator.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "stringliteralsmaller",

	    "string literal is smaller than the char array it is assigned to",
	    "A string literal is assigned to a char array that smaller than the string literal needs.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "enummembers",

	    "enum members must be int values",
	    "Type of initial values for enum members must be int.",
	    
	  },

	  {
	    "FK_TYPE, FK_NONE, plainFlag",
	    "formattype",

	    "type-mismatch in parameter corresponding to format code in a printf or scanf-like function",
	    "Type of parameter is not consistent with corresponding code in format string.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, modeFlag",
	    "formatconst",

	    "format parameter is not a string constant (hence variable arguments cannot be typechecked)",
	    "Format parameter is not known at compile-time.  This can lead to security vulnerabilities because the arguments cannot be type checked.",
	    
	  },
	  {
	    "FK_TYPE, FK_NONE, plainFlag",
	    "formatcode",

	    "invalid format code in format string for printf or scanf-like function",
	    "Format code in a format string is not valid.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_ABSTRACT, modeFlag",
	    "forwarddecl",

	    "forward declarations of pointers to abstract representation match abstract type",
	    "", 
	  },
	  {
	    "FK_TYPEEQ, FK_ABSTRACT, modeFlag",
	    "voidabstract",

	    "void * matches pointers to abstract types, casting ok (dangerous)",
	    "A pointer to void is cast to a pointer to an abstract type (or vice versa).",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_POINTER, plainFlag",
	    "castfcnptr",

	    "a pointer to a function is cast to a pointer to void (or vice versa)",
	    "A pointer to a function is cast to (or used as) a pointer to void (or vice versa).",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_ARRAY, modeFlag",
	    "charindex",

	    "char can be used to index arrays",
	    "To allow char types to index arrays, use +charindex.", 
	  },
	  {
	    "FK_TYPEEQ, FK_ARRAY, modeFlag",
	    "enumindex",

	    "enum can be used to index arrays",
	    "To allow enum types to index arrays, use +enumindex.", 
	  },
	  {
	    "FK_TYPEEQ, FK_BOOL, modeFlag",
	    "boolint",

	    "bool and int are equivalent",
	    "To make bool and int types equivalent, use +boolint.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "charint",

	    "char and int are equivalent",
	    "To make char and int types equivalent, use +charint.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "enumint",

	    "enum and int are equivalent",
	    "To make enum and int types equivalent, use +enumint.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "longint",

	    "long int and int are equivalent",
	    "To make long int and int types equivalent, use +longint.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "shortint",

	    "short int and int are equivalent",
	    "To make short int and int types equivalent, use +shortint.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "floatdouble",

	    "float and double are equivalent",
	    "To make float and double types equivalent, use +floatdouble.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "ignorequals",

	    "ignore type qualifiers (long, short, unsigned)",
	    "To ignore type qualifiers in type comparisons use +ignorequals.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_SYNTAX, plainFlag",
	    "duplicatequals",

	    "report duplicate type qualifiers (e.g., unsigned unsigned)",
	    "Duplicate type qualifiers not supported by ISO standard.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "ignoresigns",

	    "ignore signs in type comparisons (unsigned matches signed)",
	    "To ignore signs in type comparisons use +ignoresigns",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "numliteral",

	    "int literals can be reals",
	    "An int literal is used as any numeric type (including float and long long). Use +numliteral to "+
	    "allow int literals to be used as any numeric type.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "charintliteral",

	    "character constants (e.g., 'a') can be used as ints",
	    "A character constant is used as an int. Use +charintliteral to "+
	    "allow character constants to be used as ints.  (This is safe "+
	    "since the actual type of a char constant is int.)",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "relaxquals",

	    "report qualifier mismatches only if dangerous",
	    "To allow qualifier mismatches that are not dangerous, use +relaxquals.", 
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "relaxtypes",

	    "allow all numeric types to match",
	    "To allow all numeric types to match, use +relaxtypes.", 
	  },
	  {
	    "FK_TYPEEQ, FK_NONE, modeFlag",
	    "charunsignedchar",

	    "allow char and unsigned char types to match",
	    "To allow char and unsigned char types to match use +charunsignedchar.", 
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "matchanyintegral",

	    "allow any intergral type to match an arbitrary integral type (e.g., dev_t)",
	    "To allow arbitrary integral types to match any integral type, use +matchanyintegral.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "longunsignedintegral",

	    "allow long unsigned type to match an arbitrary integral type (e.g., dev_t)",
	    "To allow arbitrary integral types to match long unsigned, use +longunsignedintegral.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "longintegral",

	    "allow long type to match an arbitrary integral type (e.g., dev_t)",
	    "To allow arbitrary integral types to match long unsigned, use +longintegral.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "longunsignedunsignedintegral",

	    "allow long unsigned type to match an arbitrary unsigned integral type (e.g., size_t)",
	    "To allow arbitrary unsigned integral types to match long unsigned, "+
	    "use +longunsignedunsignedintegral.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_NUMBERS, modeFlag",
	    "longsignedintegral",

	    "allow long type to match an arbitrary signed integral type (e.g., ssize_t)",
	    "To allow arbitrary signed integral types to match long unsigned, use +longsignedintegral.",
	    
	  },
	  {
	    "FK_TYPEEQ, FK_POINTER, plainFlag",
	    "zeroptr",

	    "treat 0 as a pointer",
	    "", 
	  },
	  {
	    "FK_TYPEEQ, FK_BOOL, modeFlag",
	    "zerobool",

	    "treat 0 as a boolean",
	    "", 
	  },
	  {
	    "FK_UNRECOG, FK_DISPLAY, plainFlag",
	    "repeatunrecog",

	    "do not suppress repeated unrecognized identifier messages (instead of only reporting the first error)",
	    "Identifier used in code has not been declared. (Message repeated for future uses in this file.)",
	    
	  },
	  {
	    "FK_UNRECOG, FK_DISPLAY, plainFlag",
	    "sysunrecog",

	    "report unrecognized identifiers with system (__) prefix",
	    "Identifier used in code has not been declared. (Message repeated for "+
	    "future uses in this file.)  Use +gnuextensions to make Splint "+
	    "recognize some keywords that are gnu extensions.",
	    
	  },
	  {
	    "FK_UNRECOG, FK_NONE, plainFlag",
	    "unrecog",

	    "unrecognized identifier",
	    "Identifier used in code has not been declared.", 
	  },


	  {
	    "FK_DECL, FK_TYPE, plainFlag",
	    "annotationerror",

	    "annotation is used in inconsistent location",
	    "A declaration uses an invalid annotation.",
	    
	  } ,
	  {
	    "FK_DECL, FK_TYPE, plainFlag",
	    "commenterror",

	    "inconsistent syntactic comment",
	    "A syntactic comment is used inconsistently.",
	    
	  } ,

	  /*
	  ** Use Warnings
	  */

	  {
	    "FK_WARNUSE, FK_NONE, plainFlag",
	    "warnuse",

	    "warn when declaration marked with warn is used",
	    "Declaration marked with warn clause is used (can be suppresed by more specific flags).",
	    
	  },
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "bufferoverflow",

	    "possible buffer overflow vulnerability",
	    "Use of function that may lead to buffer overflow.",
	    
	  }, 
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "bufferoverflowhigh",

	    "likely buffer overflow vulnerability",
	    "Use of function that may lead to buffer overflow.",
	    
	  }, 
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "implementationoptional",

	    "declarator is implementation optional (ISO99 does not require an implementation to provide it)",
	    "Use of a declarator that is implementation optional, not required by ISO99.",
	    
	  }, 
	  {
	    "FK_WARNUSE, FK_NONE, modeFlag",
	    "legacy",

	    "legacy declaration in Unix Standard",
	    "Use of a declarator that is marked as a legacy entry in the Unix Standard.",
	    
	  }, 
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "multithreaded",

	    "function is not reentrant",
	    "Non-reentrant function should not be used in multithreaded code.",
	    
	  },
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "portability",

	    "function may have undefined behavior",
	    "Use of function that may have implementation-dependent behavior.",
	    
	  },
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "superuser",

	    "function is restricted to superusers",
	    "Call to function restricted to superusers.",
	    
	  },
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "toctou",

	    "possible time of check, time of use vulnerability",
	    "Possible time of check, time of use vulnerability.",
	    
	  },
	  {
	    "FK_WARNUSE, FK_SECURITY, modeFlag",
	    "unixstandard",

	    "function is not required in Standard UNIX Specification",
	    "Use of function that need not be provided by UNIX implementations",
	    
	  },

	  /*
	  ** ITS4 Compability Flags
	  **
	  ** These flags flags must appear in order (most severe -> weakest).
	  */

	  {
	    "FK_ITS4, FK_SECURITY, specialFlag",
	    "its4mostrisky",
	    "most risky security vulnerabilities (from its4 database)",
	    "Security vulnerability classified as most risky in its4 database.",
	  },
	  {
	    "FK_ITS4, FK_SECURITY, specialFlag",
	    "its4veryrisky",
	    "very risky security vulnerabilities (from its4 database)",
	    "Security vulnerability classified as very risky in its4 database.",
	    
	  },
	  {
	    "FK_ITS4, FK_SECURITY, specialFlag",
	    "its4risky",
	    "risky security vulnerabilities (from its4 database)",
	    "Security vulnerability classified as risky in its4 database.",
	    
	  },
	  {
	    "FK_ITS4, FK_SECURITY, specialFlag",
	    "its4moderate",
	    "moderately risky security vulnerabilities (from its4 database)",
	    "Security vulnerability classified as moderate risk in its4 database.",
	    
	  },
	  {
	    "FK_ITS4, FK_SECURITY, specialFlag",
	    "its4low",
	    "risky security vulnerabilities (from its4 database)",
	    "Security vulnerability classified as risky in its4 database.",
	    
	  },

	  /*
	  ** Syntactic comments
	  */

	  {
	    "FK_SYNCOMMENTS, FK_SUPPRESS, plainFlag",
	    "nocomments",

	    "ignore all stylized comments",
	    "", 
	  },
	  {
	    "FK_SYNCOMMENTS, FK_ABSTRACT, plainFlag",
	    "noaccess",

	    "ignore access comments",
	    "", 
	  },

	  {
	    "FK_SYNCOMMENTS, FK_SYNTAX, plainFlag",
	    "unrecogcomments",

	    "stylized comment is unrecognized",
	    "Word after a stylized comment marker does not correspond to a "+
	    "stylized comment.",
	    
	  },
	  {
	    "FK_SYNCOMMENTS, FK_SYNTAX, plainFlag",
	    "unrecogflagcomments",

	    "stylized flag comment uses an unrecognized flag",
	    "Semantic comment attempts to set a flag that is not recognized.",
	    
	  },
	  {
	    "FK_SYNCOMMENTS, FK_SUPPRESS, modeFlag",
	    "tmpcomments",

	    "interpret t comments (ignore errors in lines marked with /*@t<n>@*/", 
	    "", 
	  },
	  {
	    "FK_SYNCOMMENTS, FK_SUPPRESS, plainFlag",
	    "lintcomments",

	    "interpret traditional lint comments (/*FALLTHROUGH*/, /*NOTREACHED*/)",
	    "", 
	  },
	  {
	    "FK_SYNCOMMENTS, FK_SUPPRESS, modeFlag",
	    "warnlintcomments",

	    "warn when a traditional lint comment is used",
	    "A traditional lint comment is used. Some traditional lint comments "+
	    "are interpreted by Splint to enable easier checking of legacy "+
	    "code. It is preferable to replace these comments with the "+
	    "suggested Splint alternative.",
	    
	  },

	  /*
	  ** Comments
	  */

	  {
	    "FK_COMMENTS, FK_SYNTAX, plainFlag",
	    "continuecomment",

	    "line continuation marker (\\) in comment before */ on same line",
	    "A line continuation marker (\\) appears inside a comment on the same "+
	    "line as the comment close. Preprocessors should handle this "+
	    "correctly, but it causes problems for some preprocessors.",
	    
	  },
	  {
	    "FK_COMMENTS, FK_SYNTAX, plainFlag",
	    "slashslashcomment",

	    "use of // comment", 
	    "A // comment is used.  ISO C99 allows // comments, but earlier standards did not.",
	    
	  },
	  {
	    "FK_COMMENTS, FK_SYNTAX, plainFlag",
	    "nestcomment",

	    "comment begins inside comment", 
	    "A comment open sequence (/*) appears within a comment.  This usually "+
	    "means an earlier comment was not closed.",
	    
	  },

	  /*
	  ** Flags for controlling warning message printing.
	  */

	  /* Display */

	  {
	    "FK_DISPLAY, FK_ERRORS, plainFlag",
	    "quiet",

	    "suppress herald and error count",
	    "", 
	  },

	  /*
	  ** Default is to send messages, warnings and errors to stderr
	  */

	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "messagestreamstdout",

	    "send status messages to standard output stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "messagestreamstderr",

	    "send status messages to standard error stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, globalStringFlag",
	    "messagestream",

	    "send status messages to <file>",
	    "", 
	  },

	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "warningstreamstdout",

	    "send warnings to standard output stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "warningstreamstderr",

	    "send warnings to standard error stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, globalStringFlag",
	    "warningstream",

	    "send warnings to <file>",
	    "", 
	  },

	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "errorstreamstdout",

	    "send fatal errors to standard output stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, idemGlobalFlag",
	    "errorstreamstderr",

	    "send fatal errors to standard error stream",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_ERRORS, globalStringFlag",
	    "errorstream",

	    "send fatal errors to <file>",
	    "", 
	  },
	  
	  {
	    "FK_DISPLAY, FK_ERRORS, globalFlag",
	    "streamoverwrite",

	    "warn and exit if a stream output file would overwrite an existing file",
	    "", 
	  },


	  {
	    "FK_DISPLAY, FK_ERRORS, plainFlag",
	    "showsummary",

	    "show summary of all errors reported and suppressed",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_FILES, plainFlag",
	    "showscan",

	    "show file names are they are processed",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_FILES, plainFlag",
	    "warnsysfiles",

	    "Splint has been run on a system file, by default no errors are reported for system files.  Use +systemdirerrors if you want splint to report errors in system files.  A file is considered a system file if it is in a system directory or a subdirectory of a system directory.  The sysdirs flag can be used to control the directories treated as system directories.",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_NONE, globalFlag",
	    "stats",

	    "display lines processed and time",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_NONE, globalFlag",
	    "timedist",

	    "display time distribution",
	    "", 
	  },
	  {
	    "FK_DISPLAY, FK_USE, globalFlag",
	    "showalluses",

	    "show sorted list of uses of all globals",
	    "", 
	  },

	  /* Hints */

	  {
	    "FK_HINTS, FK_FORMAT, plainFlag",
	    "hints",

	    "provide a hint the first time a particular warning appears",
	    "Provide a hint the first time a particular warning appears", 
	    
	  },
	  {
	    "FK_HINTS, FK_FORMAT, plainFlag",
	    "forcehints",

	    "provide a hint for every warnings",
	    "Provide a hint for every warning",
	    
	  },

	  /*
	  ** Flags for debugging
	  */

	  {
	    "FK_DEBUG, FK_NONE, valueFlag",
	    "bugslimit",

	    "set maximum number of bugs detected before giving up",
	    "", 
	  },
	  {
	    "FK_DEBUG, FK_BOUNDS, plainFlag",
	    "debugfcnconstraint",

	    "debug function constraints",
	    "Perform buffer overflow checking even if the errors would be surpressed.",
	    
	  },
	  {
	    "FK_DEBUG, FK_NONE, specialDebugFlag",
	    "grammar",
	 
	    "debug parsing", "",
	    
	  },
	  {
	    "FK_DEBUG, FK_NONE, debugFlag",
	    "keep",

	    "do not delete temporary files", "",
	    
	  },
	  {
	    "FK_DEBUG, FK_NONE, debugFlag",
	    "nopp",

	    "do not pre-process input files", "",
	    
	  },
	  {
	    "FK_DEBUG, FK_NONE, debugFlag",
	    "showsourceloc",

	    "display the source code location where a warning is produced", "",
	    
	  },
	} ;

	public static void main(String[] args) {
		for (int i = 0; i < flags.length; i++) {
			System.out.print(flags[i][1] + ";");
			System.out.print("\"" + flags[i][2] + "\";");
			System.out.println("\"" + flags[i][3] + "\";");
		}
	}
	
}
