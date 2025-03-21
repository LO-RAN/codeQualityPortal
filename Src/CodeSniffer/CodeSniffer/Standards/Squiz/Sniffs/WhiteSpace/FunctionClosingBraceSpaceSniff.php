<?php
/**
 * Squiz_Sniffs_WhiteSpace_FunctionClosingBraceSpaceSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: FunctionClosingBraceSpaceSniff.php,v 1.13 2008/10/27 02:59:21 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_WhiteSpace_FunctionClosingBraceSpaceSniff.
 *
 * Checks that there is one empty line before the closing brace of a function.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_WhiteSpace_FunctionClosingBraceSpaceSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of tokenizers this sniff supports.
     *
     * @var array
     */
    public $supportedTokenizers = array(
                                   'PHP',
                                   'JS',
                                  );


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_FUNCTION);

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token
     *                                        in the stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if (isset($tokens[$stackPtr]['scope_closer']) === false) {
            // Probably an interface method.
            return;
        }

        $closeBrace  = $tokens[$stackPtr]['scope_closer'];
        $prevContent = $phpcsFile->findPrevious(T_WHITESPACE, ($closeBrace - 1), null, true);

        // Special case for empty JS functions
        if ($phpcsFile->tokenizerType === 'JS' && $prevContent === $tokens[$stackPtr]['scope_opener']) {
            // In this case, the opening and closing brace must be
            // right next to each other.
            if ($tokens[$stackPtr]['scope_closer'] !== ($tokens[$stackPtr]['scope_opener'] + 1)) {
                $error = 'The opening and closing braces of empty functions must be directly next to each other; e.g., function () {}';
                $phpcsFile->addError($error, $closeBrace);
            }

            return;
        }

        $braceLine = $tokens[$closeBrace]['line'];
        $prevLine  = $tokens[$prevContent]['line'];

        $found = ($braceLine - $prevLine - 1);
        if ($phpcsFile->hasCondition($stackPtr, T_FUNCTION) === true || isset($tokens[$stackPtr]['nested_parenthesis']) === true) {
            // Nested function.
            if ($found < 0) {
                $error = "Closing brace of nested function must be on a new line";
                $phpcsFile->addError($error, $closeBrace);
            } else if ($found > 0) {
                $error = "Expected 0 blank lines before closing brace of nested function; $found found";
                $phpcsFile->addError($error, $closeBrace);
            }
        } else {
            if ($found !== 1) {
                $error = "Expected 1 blank line before closing function brace; $found found";
                $phpcsFile->addError($error, $closeBrace);
            }
        }

    }//end process()


}//end class

?>
