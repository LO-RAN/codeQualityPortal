<?php
/**
 * Squiz_Sniffs_CSS_MissingColonSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: MissingColonSniff.php,v 1.1 2008/11/07 05:15:47 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_CSS_MissingColonSniff.
 *
 * Ensure that all style definitions have a colon.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Sniffs_CSS_MissingColonSniff implements PHP_CodeSniffer_Sniff
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
        return array(T_OPEN_CURLY_BRACKET);

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
        $tokens   = $phpcsFile->getTokens();
        $lastLine = $tokens[$stackPtr]['line'];
        $end      = $tokens[$stackPtr]['bracket_closer'];
        $endLine  = $tokens[$end]['line'];

        $foundColon  = false;
        $foundString = false;
        for ($i = ($stackPtr + 1); $i <= $end; $i++) {
            if ($tokens[$i]['line'] !== $lastLine) {
                // We changed lines.
                if ($foundColon === false && $foundString !== false) {
                    // We didn't find a colon on the previous line.
                    $error = 'No style definition found on line; check for missing colon';
                    $phpcsFile->addError($error, $foundString);
                }

                $foundColon  = false;
                $foundString = false;
                $lastLine    = $tokens[$i]['line'];
            }

            if ($tokens[$i]['code'] === T_STRING) {
                $foundString = $i;
            } else if ($tokens[$i]['code'] === T_COLON) {
                $foundColon = $i;
            }
        }//end for

    }//end process()


}//end class

?>