<?php
/**
 * The Tokens class contains weightings for tokens based on their
 * probability of occurance in a file.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: Tokens.php 270342 2008-12-03 04:42:07Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

define('T_NONE', 0);
define('T_OPEN_CURLY_BRACKET', 1000);
define('T_CLOSE_CURLY_BRACKET', 1001);
define('T_OPEN_SQUARE_BRACKET', 1002);
define('T_CLOSE_SQUARE_BRACKET', 1003);
define('T_OPEN_PARENTHESIS', 1004);
define('T_CLOSE_PARENTHESIS', 1005);
define('T_COLON', 1006);
define('T_STRING_CONCAT', 1007);
define('T_INLINE_THEN', 1008);
define('T_NULL', 1009);
define('T_FALSE', 1010);
define('T_TRUE', 1011);
define('T_SEMICOLON', 1012);
define('T_EQUAL', 1013);
define('T_MULTIPLY', 1015);
define('T_DIVIDE', 1016);
define('T_PLUS', 1017);
define('T_MINUS', 1018);
define('T_MODULUS', 1019);
define('T_POWER', 1020);
define('T_BITWISE_AND', 1021);
define('T_BITWISE_OR', 1022);
define('T_ARRAY_HINT', 1023);
define('T_GREATER_THAN', 1024);
define('T_LESS_THAN', 1025);
define('T_BOOLEAN_NOT', 1026);
define('T_SELF', 1027);
define('T_PARENT', 1028);
define('T_DOUBLE_QUOTED_STRING', 1029);
define('T_COMMA', 1030);
define('T_HEREDOC', 1031);
define('T_PROTOTYPE', 1032);
define('T_THIS', 1033);
define('T_REGULAR_EXPRESSION', 1034);
define('T_PROPERTY', 1035);
define('T_LABEL', 1036);
define('T_OBJECT', 1037);
define('T_COLOUR', 1038);
define('T_HASH', 1039);
define('T_URL', 1040);
define('T_STYLE', 1041);
define('T_ASPERAND', 1042);

/**
 * The Tokens class contains weightings for tokens based on their
 * probability of occurance in a file.
 *
 * The less the chance of a high occurance of an abitrary token, the higher
 * the weighting.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
final class PHP_CodeSniffer_Tokens
{

    /**
     * The token weightings.
     *
     * @var array(int => int)
     */
    public static $weightings = array(
                                 T_CLASS               => 1000,
                                 T_FUNCTION            => 100,

                                 /*
                                     Conditions.
                                 */

                                 T_WHILE               => 50,
                                 T_FOR                 => 50,
                                 T_FOREACH             => 50,
                                 T_IF                  => 50,
                                 T_ELSE                => 50,
                                 T_ELSEIF              => 50,
                                 T_WHILE               => 50,
                                 T_DO                  => 50,
                                 T_TRY                 => 50,
                                 T_CATCH               => 50,
                                 T_SWITCH              => 50,

                                 T_SELF                => 25,
                                 T_PARENT              => 25,

                                 /*
                                     Operators and arithmetic.
                                 */

                                 T_BITWISE_AND         => 8,
                                 T_BITWISE_OR          => 8,

                                 T_MULTIPLY            => 5,
                                 T_DIVIDE              => 5,
                                 T_PLUS                => 5,
                                 T_MINUS               => 5,
                                 T_MODULUS             => 5,
                                 T_POWER               => 5,

                                 T_EQUAL               => 5,
                                 T_AND_EQUAL           => 5,
                                 T_CONCAT_EQUAL        => 5,
                                 T_DIV_EQUAL           => 5,
                                 T_MINUS_EQUAL         => 5,
                                 T_MOD_EQUAL           => 5,
                                 T_MUL_EQUAL           => 5,
                                 T_OR_EQUAL            => 5,
                                 T_PLUS_EQUAL          => 5,
                                 T_XOR_EQUAL           => 5,

                                 T_BOOLEAN_AND         => 5,
                                 T_BOOLEAN_OR          => 5,

                                 /*
                                     Equality.
                                 */

                                 T_IS_EQUAL            => 5,
                                 T_IS_NOT_EQUAL        => 5,
                                 T_IS_IDENTICAL        => 5,
                                 T_IS_NOT_IDENTICAL    => 5,
                                 T_IS_SMALLER_OR_EQUAL => 5,
                                 T_IS_GREATER_OR_EQUAL => 5,
                                );

    /**
     * Tokens that represent assignments.
     *
     * @var array(int)
     */
    public static $assignmentTokens = array(
                                       T_EQUAL,
                                       T_AND_EQUAL,
                                       T_CONCAT_EQUAL,
                                       T_DIV_EQUAL,
                                       T_MINUS_EQUAL,
                                       T_MOD_EQUAL,
                                       T_MUL_EQUAL,
                                       T_PLUS_EQUAL,
                                       T_XOR_EQUAL,
                                      );

    /**
     * Tokens that represent equality comparisons.
     *
     * @var array(int)
     */
    public static $equalityTokens = array(
                                     T_IS_EQUAL,
                                     T_IS_NOT_EQUAL,
                                     T_IS_IDENTICAL,
                                     T_IS_NOT_IDENTICAL,
                                     T_IS_SMALLER_OR_EQUAL,
                                     T_IS_GREATER_OR_EQUAL,
                                    );

    /**
     * Tokens that represent comparison operator.
     *
     * @var array(int)
     */
    public static $comparisonTokens = array(
                                       T_IS_EQUAL,
                                       T_IS_IDENTICAL,
                                       T_IS_NOT_EQUAL,
                                       T_IS_NOT_IDENTICAL,
                                       T_LESS_THAN,
                                       T_GREATER_THAN,
                                       T_IS_SMALLER_OR_EQUAL,
                                       T_IS_GREATER_OR_EQUAL,
                                      );

    /**
     * Tokens that represent arithmetic operators.
     *
     * @var array(int)
     */
    public static $arithmeticTokens = array(
                                       T_PLUS,
                                       T_MINUS,
                                       T_MULTIPLY,
                                       T_DIVIDE,
                                       T_MODULUS,
                                      );

    /**
     * Tokens that represent casting.
     *
     * @var array(int)
     */
    public static $castTokens = array(
                                 T_INT_CAST,
                                 T_STRING_CAST,
                                 T_DOUBLE_CAST,
                                 T_ARRAY_CAST,
                                 T_BOOL_CAST,
                                 T_OBJECT_CAST,
                                 T_UNSET_CAST,
                                );

    /**
     * Token types that open parethesis.
     *
     * @var array(int)
     */
    public static $parenthesisOpeners = array(
                                         T_ARRAY,
                                         T_FUNCTION,
                                         T_WHILE,
                                         T_FOR,
                                         T_FOREACH,
                                         T_SWITCH,
                                         T_IF,
                                         T_ELSEIF,
                                         T_CATCH,
                                        );

    /**
     * Tokens that are allowed to open scopes.
     *
     * @var array(int)
     */
    public static $scopeOpeners = array(
                                   T_CLASS,
                                   T_INTERFACE,
                                   T_FUNCTION,
                                   T_IF,
                                   T_SWITCH,
                                   T_CASE,
                                   T_DEFAULT,
                                   T_WHILE,
                                   T_ELSE,
                                   T_ELSEIF,
                                   T_FOR,
                                   T_FOREACH,
                                   T_DO,
                                   T_TRY,
                                   T_CATCH,
                                  );

    /**
     * Tokens that represent scope modifiers.
     *
     * @var array(int)
     */
    public static $scopeModifiers = array(
                                     T_PRIVATE,
                                     T_PUBLIC,
                                     T_PROTECTED,
                                    );

    /**
     * Tokens that perform operations.
     *
     * @var array(int)
     */
    public static $operators = array(
                                T_MINUS,
                                T_PLUS,
                                T_MULTIPLY,
                                T_DIVIDE,
                                T_MODULUS,
                                T_POWER,
                                T_BITWISE_AND,
                                T_BITWISE_OR,
                               );

    /**
     * Tokens that perform boolean operations.
     *
     * @var array(int)
     */
    public static $booleanOperators = array(
                                       T_BOOLEAN_AND,
                                       T_BOOLEAN_OR,
                                       T_LOGICAL_AND,
                                       T_LOGICAL_OR,
                                      );

    /**
     * Tokens that perform operations.
     *
     * @var array(int)
     */
    public static $blockOpeners = array(
                                   T_OPEN_CURLY_BRACKET,
                                   T_OPEN_SQUARE_BRACKET,
                                   T_OPEN_PARENTHESIS,
                                  );

    /**
     * Tokens that don't represent code.
     *
     * @var array(int)
     */
    public static $emptyTokens = array(
                                  T_WHITESPACE,
                                  T_COMMENT,
                                  T_DOC_COMMENT,
                                 );

    /**
     * Tokens that are comments.
     *
     * @var array(int)
     */
    public static $commentTokens = array(
                                    T_COMMENT,
                                    T_DOC_COMMENT,
                                   );

    /**
     * Tokens that represent strings.
     *
     * Note that T_STRINGS are NOT represented in this list.
     *
     * @var array(int)
     */
    public static $stringTokens = array(
                                   T_CONSTANT_ENCAPSED_STRING,
                                   T_DOUBLE_QUOTED_STRING,
                                  );

    /**
     * Tokens that include files.
     *
     * @var array(int)
     */
    public static $includeTokens = array(
                                    T_REQUIRE_ONCE,
                                    T_REQUIRE,
                                    T_INCLUDE_ONCE,
                                    T_INCLUDE,
                                   );

    /**
     * Tokens that make up a heredoc string.
     *
     * @var array(int)
     */
    public static $heredocTokens = array(
                                    T_START_HEREDOC,
                                    T_END_HEREDOC,
                                    T_HEREDOC,
                                   );


    /**
     * A PHP_CodeSniffer_Tokens class cannot be constructed.
     *
     * Only static calls are allowed.
     */
    private function __construct()
    {

    }//end __construct()


    /**
     * Returns the highest weighted token type.
     *
     * Tokens are weighted by their approximate frequency of appearance in code
     * - the less frequently they appear in the code, the higher the weighting.
     * For example T_CLASS tokens apprear very infrequently in a file, and
     * therefore have a high weighting.
     *
     * Returns false if there are no weightings for any of the specified tokens.
     *
     * @param array(int) $tokens The token types to get the highest weighted
     *                           type for.
     *
     * @return int The highest weighted token.
     */
    public static function getHighestWeightedToken(array $tokens)
    {
        $highest     = -1;
        $highestType = false;

        $weights = self::$weightings;

        foreach ($tokens as $token) {
            if (isset($weights[$token]) === true) {
                $weight = $weights[$token];
            } else {
                $weight = 0;
            }

            if ($weight > $highest) {
                $highest     = $weight;
                $highestType = $token;
            }
        }

        return $highestType;

    }//end getHighestWeightedToken()


}//end class

?>
