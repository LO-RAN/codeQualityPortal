<?php
/**
 * Squiz_Sniffs_Objects_ObjectInstantiationSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: ObjectInstantiationSniff.php,v 1.3 2007/10/23 06:05:14 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Squiz_Sniffs_Objects_ObjectInstantiationSniff.
 *
 * Ensures objects are assigned to a variable when instantiated.
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
class Squiz_Sniffs_Objects_ObjectInstantiationSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Registers the token types that this sniff wishes to listen to.
     *
     * @return array
     */
    public function register()
    {
        return array(T_NEW);

    }//end register()


    /**
     * Process the tokens that this sniff is listening for.
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

        $allowedTokens   = PHP_CodeSniffer_Tokens::$emptyTokens;
        $allowedTokens[] = T_BITWISE_AND;

        $prev = $phpcsFile->findPrevious($allowedTokens, ($stackPtr - 1), null, true);

        $allowedTokens = array(
                          T_EQUAL,
                          T_DOUBLE_ARROW,
                          T_THROW,
                         );

        if (in_array($tokens[$prev]['code'], $allowedTokens) === false) {
            $error = 'New objects must be assigned to a variable';
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>
