<?php
/**
 * Ensures that getRequestData() is used to access super globals.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: GetRequestDataSniff.php,v 1.2 2009/03/02 05:25:04 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Ensures that getRequestData() is used to access super globals.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class MySource_Sniffs_PHP_GetRequestDataSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_VARIABLE);

    }//end register()


    /**
     * Processes this sniff, when one of its tokens is encountered.
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

        $varName = $tokens[$stackPtr]['content'];
        if ($varName !== '$_REQUEST'
            && $varName !== '$_GET'
            && $varName !== '$_POST'
        ) {
            return;
        }

        // The only place these super globals can be accessed directly is
        // in the getRequestData() method of the Security class.
        $inClass = false;
        foreach ($tokens[$stackPtr]['conditions'] as $i => $type) {
            if ($tokens[$i]['code'] === T_CLASS) {
                $className = $phpcsFile->findNext(T_STRING, $i);
                $className = $tokens[$className]['content'];
                if (strtolower($className) === 'security') {
                    $inClass = true;
                } else {
                    // We don't have nested classes.
                    break;
                }
            } else if ($inClass == true && $tokens[$i]['code'] === T_FUNCTION) {
                $funcName = $phpcsFile->findNext(T_STRING, $i);
                $funcName = $tokens[$funcName]['content'];
                if (strtolower($funcName) === 'getrequestdata') {
                    // This is valid.
                    return;
                } else {
                    // We don't have nested functions.
                    break;
                }
            }//end if
        }//end foreach

        // If we get to here, the super global was used incorrectly.
        // First find out how it is being used.
        $globalName   = strtolower(substr($varName, 2));
        $usedVar      = '';

        $openBracket = $phpcsFile->findNext(T_WHITESPACE, ($stackPtr + 1), null, true);
        if ($tokens[$openBracket]['code'] === T_OPEN_SQUARE_BRACKET) {
            $closeBracket = $tokens[$openBracket]['bracket_closer'];
            $usedVar      = $phpcsFile->getTokensAsString(($openBracket + 1), ($closeBracket - $openBracket - 1));
        }

        $error = "The $varName super global must not be accessed directly; use Security::getRequestData(";
        if ($usedVar !== '') {
            $error .= "$usedVar, '$globalName'";
        }

        $error .= ') instead';
        $phpcsFile->addError($error, $stackPtr);

    }//end process()


}//end class

?>
