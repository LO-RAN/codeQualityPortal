<?php
/**
 * Checks the destructuration for functions.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: CyclomaticComplexitySniff.php,v 1.1 2007/07/30 04:55:35 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Checks the destructuration for functions.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Johann-Peter Hartmann <hartmann@mayflower.de>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2007 Mayflower GmbH
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.0.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Caqs_Sniffs_Metrics_DestructurationSniff implements PHP_CodeSniffer_Sniff
{


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
        $this->currentFile = $phpcsFile;

        $tokens = $phpcsFile->getTokens();

        // Ignore abstract methods.
        if (isset($tokens[$stackPtr]['scope_opener']) === false) {
            return;
        }

        // Detect start and end of this function definition.
        $start = $tokens[$stackPtr]['scope_opener'];
        $end   = $tokens[$stackPtr]['scope_closer'];

        // Predicate nodes for PHP.
        $find = array(
                 'T_BREAK',
                 'T_CONTINUE',
                 'T_RETURN',
                );

        $destructuration = 1;
        // Iterate from start to end and count predicate nodes.
        for ($i = ($start + 1); $i < $end; $i++) {
            if (in_array($tokens[$i]['type'], $find) === true) {
                $destructuration++;
            }
        }

            $info = '<elt type="MET" name="'.$phpcsFile->getDeclarationName($stackPtr).'"><metric id="EVG" value="'.$destructuration.'"/></elt>';
            $phpcsFile->addInfoWithValue($info, $stackPtr, $destructuration);

        return;

    }//end process()


}//end class

?>
