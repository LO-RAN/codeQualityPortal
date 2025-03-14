<?php
/**
 * Reports errors if the same class or interface name is used in multiple files.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: DuplicateClassNameSniff.php,v 1.1 2008/07/25 04:24:10 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Reports errors if the same class or interface name is used in multiple files.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.0RC2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Generic_Sniffs_Classes_DuplicateClassNameSniff implements PHP_CodeSniffer_MultiFileSniff
{


    /**
     * Called once per script run to allow for processing of this sniff.
     *
     * @param array(PHP_CodeSniffer_File) $files The PHP_CodeSniffer files processed
     *                                           during the script run.
     *
     * @return void
     */
    public function process(array $files)
    {
        $foundClasses = array();

        foreach ($files as $phpcsFile) {
            $tokens = $phpcsFile->getTokens();

            $stackPtr = $phpcsFile->findNext(array(T_CLASS, T_INTERFACE), 0);
            while ($stackPtr !== false) {
                $nameToken   = $phpcsFile->findNext(T_STRING, $stackPtr);
                $name        = $tokens[$nameToken]['content'];
                $compareName = strtolower($name);
                if (isset($foundClasses[$compareName]) === true) {
                    $type  = strtolower($tokens[$stackPtr]['content']);
                    $file  = $foundClasses[$compareName]['file'];
                    $line  = $foundClasses[$compareName]['line'];
                    $error = "Duplicate $type name \"$name\" found; first defined in $file on line $line";
                    $phpcsFile->addWarning($error, $stackPtr);
                } else {
                    $foundClasses[$compareName] = array(
                                                        'file' => $phpcsFile->getFilename(),
                                                        'line' => $tokens[$stackPtr]['line'],
                                                       );
                }

                $stackPtr = $phpcsFile->findNext(array(T_CLASS, T_INTERFACE), ($stackPtr + 1));
            }//end while

        }//end foreach

    }//end process()


}//end class

?>