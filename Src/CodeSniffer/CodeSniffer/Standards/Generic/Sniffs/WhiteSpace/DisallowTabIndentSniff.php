<?php
/**
 * Generic_Sniffs_WhiteSpace_DisallowTabIndentSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: DisallowTabIndentSniff.php,v 1.3 2008/10/28 04:43:57 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Generic_Sniffs_WhiteSpace_DisallowTabIndentSniff.
 *
 * Throws errors if tabs are used for indentation.
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
class Generic_Sniffs_WhiteSpace_DisallowTabIndentSniff implements PHP_CodeSniffer_Sniff
{

    /**
     * A list of tokenizers this sniff supports.
     *
     * @var array
     */
    public $supportedTokenizers = array(
                                   'PHP',
                                   'JS',
                                   'CSS',
                                  );


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_WHITESPACE);

    }//end register()


    /**
     * Processes this test, when one of its tokens is encountered.
     *
     * @param PHP_CodeSniffer_File $phpcsFile All the tokens found in the document.
     * @param int                  $stackPtr  The position of the current token in
     *                                        the stack passed in $tokens.
     *
     * @return void
     */
    public function process(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        // Make sure this is whitespace used for indentation.
        $line = $tokens[$stackPtr]['line'];
        if ($stackPtr > 0 && $tokens[($stackPtr - 1)]['line'] === $line) {
            return;
        }

        if (strpos($tokens[$stackPtr]['content'], "\t") !== false) {
            $error = 'Spaces must be used to indent lines; tabs are not allowed';
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>
