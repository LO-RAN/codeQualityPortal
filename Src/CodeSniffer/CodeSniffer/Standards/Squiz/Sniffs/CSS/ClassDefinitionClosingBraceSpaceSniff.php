<?php
/**
 * Squiz_Sniffs_CSS_ColonSpacingSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ClassDefinitionClosingBraceSpaceSniff.php,v 1.2 2008/10/31 05:36:54 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_CSS_ClassDefinitionClosingBraceSpaceSniff.
 *
 * Ensure there is a single blank line after the closing brace of a class definition.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_CSS_ClassDefinitionClosingBraceSpaceSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of tokenizers this sniff supports.
     *
     * @var array
     */
    public $supportedTokenizers = array('CSS');


    /**
     * Returns the token types that this sniff is interested in.
     *
     * @return array(int)
     */
    public function register()
    {
        return array(T_CLOSE_CURLY_BRACKET);

    }//end register()


    /**
     * Processes the tokens that this sniff is interested in.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where the token was found.
     * @param int                  $stackPtr  The position in the stack where
     *                                        the token was found.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        $next = $phpcsFile->findNext(T_WHITESPACE, ($stackPtr + 1), null, true);
        if ($next === false) {
            return;
        }

        if ($tokens[$next]['code'] !== T_CLOSE_TAG) {
            $found = (($tokens[$next]['line'] - $tokens[$stackPtr]['line']) - 1);
            if ($found !== 1) {
                $error = "Expected one blank line after closing brace of class definition; $found found";
                $phpcsFile->addError($error, $stackPtr);
            }
        }

        $prev = $phpcsFile->findPrevious(PHP_CodeSniffer_Tokens::$emptyTokens, ($stackPtr - 1), null, true);
        if ($prev !== false && $tokens[$prev]['line'] !== ($tokens[$stackPtr]['line'] - 1)) {
            $num   = ($tokens[$stackPtr]['line'] - $tokens[$prev]['line'] - 1);
            $error = "Expected 0 blank lines before closing brace of class definition; $num found";
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>