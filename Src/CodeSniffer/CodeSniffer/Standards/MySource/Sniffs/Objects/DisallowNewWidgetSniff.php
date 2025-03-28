<?php
/**
 * Ensures that widgets are not manually created.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: DisallowNewWidgetSniff.php,v 1.1 2008/02/25 03:48:52 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Ensures that widgets are not manually created.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class MySource_Sniffs_Objects_DisallowNewWidgetSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns an array of tokens this test wants to listen for.
     *
     * @return array
     */
    public function register()
    {
        return array(T_NEW);

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

        $className = $phpcsFile->findNext(T_WHITESPACE, ($stackPtr + 1), null, true);
        if ($tokens[$className]['code'] !== T_STRING) {
            return;
        }

        if (substr(strtolower($tokens[$className]['content']), -10) === 'widgettype') {
            $widgetType = substr($tokens[$className]['content'], 0, -10);
            $error      = "Manual creation of widget objects is banned; use Widget::getWidget('$widgetType'); instead";
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end process()


}//end class

?>
