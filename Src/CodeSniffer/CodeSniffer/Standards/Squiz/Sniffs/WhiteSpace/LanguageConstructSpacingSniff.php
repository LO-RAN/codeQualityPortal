<?php
/**
 * Squiz_Sniffs_WhiteSpace_LanguageConstructSpacingSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: LanguageConstructSpacingSniff.php,v 1.1 2007/11/06 01:09:33 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_WhiteSpace_LanguageConstructSpacingSniff.
 *
 * Ensures all language constructs (without brackets) contain a
 * single space between themselves and their content.
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
class Squiz_Sniffs_WhiteSpace_LanguageConstructSpacingSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(
                T_ECHO,
                T_PRINT,
                T_RETURN,
                T_INCLUDE,
                T_INCLUDE_ONCE,
                T_REQUIRE,
                T_REQUIRE_ONCE,
                T_NEW,
               );

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file being scanned.
     * @param int                  $stackPtr  The position of the current token in
     *                                        the stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if ($tokens[($stackPtr + 1)]['code'] === T_SEMICOLON) {
            // No content for this language construct.
            return;
        }

        if ($tokens[($stackPtr + 1)]['code'] === T_WHITESPACE) {
            $content       = $tokens[($stackPtr + 1)]['content'];
            $contentLength = strlen($content);
            if ($contentLength !== 1) {
                $error = "Language constructs must be followed by a single space; expected 1 space but found $contentLength";
                $phpcsFile->addError($error, $stackPtr);
            }
        } else {
            $expected = $tokens[$stackPtr]['content'].' '.$tokens[($stackPtr + 1)]['content'];
            $found    = $tokens[$stackPtr]['content'].$tokens[($stackPtr + 1)]['content'];
            $error    = "Language constructs must be followed by a single space; expected \"$expected\" but found \"$found\"";
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>
