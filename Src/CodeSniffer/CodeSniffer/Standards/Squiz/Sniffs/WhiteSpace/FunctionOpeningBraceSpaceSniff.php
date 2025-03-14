<?php
/**
 * Squiz_Sniffs_WhiteSpace_FunctionOpeningBraceSpaceSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: FunctionOpeningBraceSpaceSniff.php,v 1.7 2008/10/27 03:00:03 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_WhiteSpace_FunctionOpeningBraceSpaceSniff.
 *
 * Checks that there is no empty line after the opening brace of a function.
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
class Squiz_Sniffs_WhiteSpace_FunctionOpeningBraceSpaceSniff implements PHP_CodeSniffer_Sniff
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

        if (isset($tokens[$stackPtr]['scope_opener']) === false) {
            // Probably an interface method.
            return;
        }

        $openBrace   = $tokens[$stackPtr]['scope_opener'];
        $nextContent = $phpcsFile->findNext(T_WHITESPACE, ($openBrace + 1), null, true);

        if ($nextContent === $tokens[$stackPtr]['scope_closer']) {
             // The next bit of content is the closing brace, so this
             // is an empty function and should have a blank line
             // between the opening and closing braces.
            return;
        }

        $braceLine = $tokens[$openBrace]['line'];
        $nextLine  = $tokens[$nextContent]['line'];

        $found = ($nextLine - $braceLine - 1);
        if ($found > 0) {
            $error = "Expected 0 blank lines after opening function brace; $found found";
            $phpcsFile->addError($error, $openBrace);
        }

        if ($phpcsFile->tokenizerType === 'JS') {
            // Do some additional checking before the function brace.
            $nestedFunction = ($phpcsFile->hasCondition($stackPtr, T_FUNCTION) === true || isset($tokens[$stackPtr]['nested_parenthesis']) === true);

            $functionLine   = $tokens[$tokens[$stackPtr]['parenthesis_closer']]['line'];
            $lineDifference = ($braceLine - $functionLine);

            if ($nestedFunction === true) {
                if ($lineDifference > 0) {
                    $error = "Expected 0 blank lines before openning brace of nested function; $found found";
                    $phpcsFile->addError($error, $openBrace);
                }
            } else {
                if ($lineDifference === 0) {
                    $error = 'Opening brace should be on a new line';
                    $phpcsFile->addError($error, $openBrace);
                    return;
                }

                if ($lineDifference > 1) {
                    $ender = 'line';
                    if (($lineDifference - 1) !== 1) {
                        $ender .= 's';
                    }

                    $error = 'Opening brace should be on the line after the declaration; found '.($lineDifference - 1).' blank '.$ender;
                    $phpcsFile->addError($error, $openBrace);
                    return;
                }
            }//end if
        }//end if

    }//end process()


}//end class

?>
