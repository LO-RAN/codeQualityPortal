<?php
/**
 * Zend_Sniffs_Debug_CodeAnalyzerSniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Holger Kral <holger.kral@zend.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: CodeAnalyzerSniff.php,v 1.2 2007/10/21 22:28:13 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Zend_Sniffs_Debug_CodeAnalyzerSniff.
 *
 * Runs the Zend Code Analyzer (from Zend Studio) on the file.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Holger Kral <holger.kral@zend.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.0.0
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Zend_Sniffs_Debug_CodeAnalyzerSniff implements PHP_CodeSniffer_Sniff
{


    /**
     * Returns the token types that this sniff is interested in.
     *
     * @return array(int)
     */
    public function register()
    {
        return array(T_OPEN_TAG);

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
        // Because we are analyzing the whole file in one step, execute this method
        // only on first occurence of a T_OPEN_TAG.
        $prevOpenTag = $phpcsFile->findPrevious(T_OPEN_TAG, ($stackPtr - 1));
        if ($prevOpenTag !== false) {
            return;
        }

        $fileName = $phpcsFile->getFilename();

        $analyzerPath = PHP_CodeSniffer::getConfigData('zend_ca_path');
        if (is_null($analyzerPath) === true) {
            echo "\n--- WARNING ---\n";
            echo "The Zend Code Analyzer path is not set.\n";
            echo "Run \"phpcs --config-set zend_ca_path /path/to/ZendCodeAnalyzer\".\n\n";
            return;
        }

        // In the command, 2>&1 is important because the code analyzer sends its
        // findings to stderr. $output normally contains only stdout, so using 2>&1
        // will pipe even stderr to stdout.
        $cmd = $analyzerPath.' '.$fileName.' 2>&1';

        // There is the possibility to pass "--ide" as an option to the analyzer. This
        // would result in an output format which would be easier to parse. The problem
        // here is that no cleartext error messages are returnd; only error-code-labels.
        // So for a start we go for cleartext output.

        $exitCode = exec($cmd, $output, $retval);

        // $exitCode is the last line of $output if no error occures, on error it is numeric.
        // try to handle various errorcondition an provide useful error reporting
        //  || (is_numeric($retval) && $retval > 0)
        if (is_numeric($exitCode) === true && $exitCode > 0) {
            if (is_array($output) === true) {
                $msg = join('\n', $output);
            }

            throw new PHP_CodeSniffer_Exception("Failed invoking ZendCodeAnalyzer, exitcode was [$exitCode], retval was [$retval], output was [$msg]");
        }

        if (is_array($output) === true) {
            $tokens = $phpcsFile->getTokens();

            foreach ($output as $finding) {
                // The first two lines of analyzer output contain something like this:
                // > Zend Code Analyzer 1.2.2
                // > Analyzing <filename>...
                // So skip these...
                $res = eregi("^.+\(line ([0-9]+)\):(.+)$", $finding, $regs);
                if ($regs === null || $res === false) {
                    continue;
                }

                // Find the token at the start of the line.
                $lineToken = null;
                foreach ($tokens as $ptr => $info) {
                    if ($info['line'] == $regs[1]) {
                        $lineToken = $ptr;
                        break;
                    }
                }

                if ($lineToken !== null) {
                    $phpcsFile->addWarning(trim($regs[2]), $ptr);
                }
            }//end foreach
        }//end if

    }//end process()

}//end class
?>