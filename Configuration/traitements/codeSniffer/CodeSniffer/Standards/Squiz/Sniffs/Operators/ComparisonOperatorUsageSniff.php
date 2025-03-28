<?php
/**
 * A Sniff to enforce the use of IDENTICAL type operators rather than EQUAL operators.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ComparisonOperatorUsageSniff.php,v 1.7 2007/11/29 02:57:55 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * A Sniff to enforce the use of IDENTICAL type operators rather than EQUAL operators.
 *
 * The use of === true is enforced over implicit true statements,
 * for example:
 *
 * <code>
 * if ($a)
 * {
 *     ...
 * }
 * </code>
 *
 * should be:
 *
 * <code>
 * if ($a === true)
 * {
 *     ...
 * }
 * </code>
 *
 * It also enforces the use of === false over ! operators.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.0.1
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_Operators_ComparisonOperatorUsageSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of valid comparison operators.
     *
     * @var array
     */
    private static $_validOps = array(
                                 T_IS_IDENTICAL,
                                 T_IS_NOT_IDENTICAL,
                                 T_LESS_THAN,
                                 T_GREATER_THAN,
                                 T_IS_GREATER_OR_EQUAL,
                                 T_IS_SMALLER_OR_EQUAL,
                                 T_INSTANCEOF,
                                );

    /**
     * A list of invalid operators with their alternatives.
     *
     * @var array(int => string)
     */
    private static $_invalidOps = array(
                                   T_IS_EQUAL     => '===',
                                   T_IS_NOT_EQUAL => '!==',
                                   T_BOOLEAN_NOT  => '=== FALSE',
                                  );


    /**
     * Registers the token types that this sniff wishes to listen to.
     *
     * @return array
     */
    public function register()
    {
        return array(
                T_IF,
                T_INLINE_THEN,
               );

    }//end register()


    /**
     * Process the tokens that this sniff is listening for.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where the token was found.
     * @param int                  $stackPtr  The position in the stack where the token
     *                                        was found.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if ($tokens[$stackPtr]['code'] === T_INLINE_THEN) {
            $end = $phpcsFile->findPrevious(PHP_CodeSniffer_Tokens::$emptyTokens, ($stackPtr - 1), null, true);
            if ($tokens[$end]['code'] !== T_CLOSE_PARENTHESIS) {
                // This inline IF statement does not have its condition
                // bracketed, so we need to guess where it starts.
                for ($i = ($end - 1); $i >= 0; $i--) {
                    if ($tokens[$i]['code'] === T_SEMICOLON) {
                        // Stop here as we assume it is the end
                        // of the previous statement.
                        break;
                    } else if ($tokens[$i]['code'] === T_OPEN_TAG) {
                        // Stop here as this is the start of the file.
                        break;
                    } else if ($tokens[$i]['code'] === T_CLOSE_CURLY_BRACKET) {
                        // Stop if this is the closing brace of
                        // a code block.
                        if (isset($tokens[$i]['scope_opener']) === true) {
                            break;
                        }
                    } else if ($tokens[$i]['code'] === T_OPEN_CURLY_BRACKET) {
                        // Stop if this is the opening brace of
                        // a code block.
                        if (isset($tokens[$i]['scope_closer']) === true) {
                            break;
                        }
                    }
                }//end for

                $start = $phpcsFile->findNext(PHP_CodeSniffer_Tokens::$emptyTokens, ($i + 1), null, true);
            } else {
                $start = $tokens[$end]['parenthesis_opener'];
            }
        } else {
            $start = $tokens[$stackPtr]['parenthesis_opener'];
            $end   = $tokens[$stackPtr]['parenthesis_closer'];
        }

        $requiredOps = 0;
        $foundOps    = 0;

        for ($i = $start; $i <= $end; $i++) {
            $type = $tokens[$i]['code'];
            if (in_array($type, array_keys(self::$_invalidOps)) === true) {
                $error  = 'Operator '.$tokens[$i]['content'].' prohibited;';
                $error .= ' use '.self::$_invalidOps[$type].' instead';
                $phpcsFile->addError($error, $i);
                $foundOps++;
            } else if (in_array($type, self::$_validOps) === true) {
                $foundOps++;
            }

            if ($tokens[$i]['code'] === T_BOOLEAN_AND || $tokens[$i]['code'] === T_BOOLEAN_OR) {
                $requiredOps++;

                // If we get to here and we have not found the right number of
                // comparison operators, then we must have had an implicit
                // true operation ie. if ($a) instead of the required
                // if ($a === true), so let's add an error.
                if ($requiredOps !== $foundOps) {
                    $error = 'Implicit true comparisons prohibited; use === TRUE instead';
                    $phpcsFile->addError($error, $stackPtr);
                    $foundOps++;
                }
            }
        }//end for

        $requiredOps++;

        if ($foundOps < $requiredOps) {
            $error = 'Implicit true comparisons prohibited; use === TRUE instead';
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>
