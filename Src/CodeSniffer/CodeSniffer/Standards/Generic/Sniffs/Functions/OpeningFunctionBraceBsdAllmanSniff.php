<?php
/**
 * Generic_Sniffs_Methods_OpeningMethodBraceBsdAllmanSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: OpeningFunctionBraceBsdAllmanSniff.php,v 1.8 2008/05/05 03:59:12 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Generic_Sniffs_Functions_OpeningFunctionBraceBsdAllmanSniff.
 *
 * Checks that the opening brace of a function is on the line after the
 * function declaration.
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
class Generic_Sniffs_Functions_OpeningFunctionBraceBsdAllmanSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Registers the tokens that this sniff wants to listen for.
     *
     * @return void
     */
    public function register()
    {
        return array(T_FUNCTION);

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token in the
     *                                        stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if (isset($tokens[$stackPtr]['scope_opener']) === false) {
            return;
        }

        $openingBrace = $tokens[$stackPtr]['scope_opener'];

        // The end of the function occurs at the end of the argument list. Its
        // like this because some people like to break long function declarations
        // over multiple lines.
        $functionLine = $tokens[$tokens[$stackPtr]['parenthesis_closer']]['line'];
        $braceLine    = $tokens[$openingBrace]['line'];

        $lineDifference = ($braceLine - $functionLine);

        if ($lineDifference === 0) {
            $error = 'Opening brace should be on a new line';
            $phpcsFile->addError($error, $openingBrace);
            return;
        }

        if ($lineDifference > 1) {
            $ender = 'line';
            if (($lineDifference - 1) !== 1) {
                $ender .= 's';
            }

            $error = 'Opening brace should be on the line after the declaration; found '.($lineDifference - 1).' blank '.$ender;
            $phpcsFile->addError($error, $openingBrace);
            return;
        }

        // We need to actually find the first piece of content on this line,
        // as if this is a method with tokens before it (public, static etc)
        // or an if with an else before it, then we need to start the scope
        // checking from there, rather than the current token.
        $lineStart = $stackPtr;
        while (($lineStart = $phpcsFile->findPrevious(array(T_WHITESPACE), ($lineStart - 1), null, false)) !== false) {
            if (strpos($tokens[$lineStart]['content'], $phpcsFile->eolChar) !== false) {
                break;
            }
        }

        // We found a new line, now go forward and find the first non-whitespace
        // token.
        $lineStart = $phpcsFile->findNext(array(T_WHITESPACE), $lineStart, null, true);

        // The opening brace is on the correct line, now it needs to be
        // checked to be correctly indented.
        $startColumn = $tokens[$lineStart]['column'];
        $braceIndent = $tokens[$openingBrace]['column'];

        if ($braceIndent !== $startColumn) {
            $error = 'Opening brace indented incorrectly; expected '.($startColumn - 1).' spaces, found '.($braceIndent - 1);
            $phpcsFile->addError($error, $openingBrace);
        }

    }//end process()


}//end class

?>