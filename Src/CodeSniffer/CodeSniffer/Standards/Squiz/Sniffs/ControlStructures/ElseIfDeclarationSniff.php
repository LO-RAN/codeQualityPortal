<?php
/**
 * Squiz_Sniffs_ControlStructures_ElseIfDeclarationSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ElseIfDeclarationSniff.php,v 1.2 2007/07/23 01:47:53 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_ControlStructures_ElseIfDeclarationSniff.
 *
 * Verifies that there are not elseif statements. The else and the if should
 * be separated by a space.
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
class Squiz_Sniffs_ControlStructures_ElseIfDeclarationSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_ELSEIF);

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
        $error = 'Usage of ELSEIF not allowed. Use ELSE IF instead.';
        $phpcsFile->addError($error, $stackPtr);

    }//end process()


}//end class


?>
