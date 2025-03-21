<?php
/**
 * Full report for PHP_CodeSniffer.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Gabriele Santini <gsantini@sqli.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2009 SQLI <www.sqli.com>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: IsCamelCapsTest.php 240585 2007-08-02 00:05:40Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Full report for PHP_CodeSniffer.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Gabriele Santini <gsantini@sqli.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2009 SQLI <www.sqli.com>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class PHP_CodeSniffer_Reports_Full implements PHP_CodeSniffer_Report
{


    /**
     * Prints all errors and warnings for each file processed.
     *
     * Errors and warnings are displayed together, grouped by file.
     *
     * @param array   $report       Prepared report.
     * @param boolean $showWarnings Show warnings?
     * @param boolean $showSources  Show sources?
     * @param int     $width        Maximum allowed lne width.
     * 
     * @return string 
     */
    public function generate(
        $report,
        $showWarnings=true,
        $showSources=false,
        $width=80
    ) {
        $errorsShown = 0;
        $width       = max($width, 70);

        foreach ($report['files'] as $filename => $file) {
            if (empty($file['messages']) === true) {
                continue;
            }

            echo PHP_EOL.'FILE: ';
            if (strlen($filename) <= ($width - 9)) {
                echo $filename;
            } else {
                echo '...'.substr($filename, (strlen($filename) - ($width - 9)));
            }

            echo PHP_EOL;
            echo str_repeat('-', $width).PHP_EOL;

            echo 'FOUND '.$file['errors'].' ERROR(S) ';

            if ($showWarnings === true) {
                echo 'AND '.$file['warnings'].' WARNING(S) ';
            }

            echo 'AFFECTING '.count($file['messages']).' LINE(S)'.PHP_EOL;
            echo str_repeat('-', $width).PHP_EOL;

            // Work out the max line number for formatting.
            $maxLine = 0;
            foreach ($file['messages'] as $line => $lineErrors) {
                if ($line > $maxLine) {
                    $maxLine = $line;
                }
            }

            $maxLineLength = strlen($maxLine);

            // The length of the word ERROR or WARNING; used for padding.
            if ($showWarnings === true && $file['warnings'] > 0) {
                $typeLength = 7;
            } else {
                $typeLength = 5;
            }

            // The padding that all lines will require that are
            // printing an error message overflow.
            $paddingLine2  = str_repeat(' ', ($maxLineLength + 1));
            $paddingLine2 .= ' | ';
            $paddingLine2 .= str_repeat(' ', $typeLength);
            $paddingLine2 .= ' | ';

            // The maxium amount of space an error message can use.
            $maxErrorSpace = ($width - strlen($paddingLine2) - 1);

            foreach ($file['messages'] as $line => $lineErrors) {
                foreach ($lineErrors as $column => $colErrors) {
                    foreach ($colErrors as $error) {
                        $message = $error['message'];
                        if ($showSources === true) {
                            $message .= ' ('.$error['source'].')';
                        }

                        // The padding that goes on the front of the line.
                        $padding  = ($maxLineLength - strlen($line));
                        $errorMsg = wordwrap(
                            $message,
                            $maxErrorSpace,
                            PHP_EOL.$paddingLine2
                        );

                        echo ' '.str_repeat(' ', $padding).$line.' | '.$error['type'];
                        if ($error['type'] === 'ERROR') {
                            if ($showWarnings === true && $file['warnings'] > 0) {
                                echo '  ';
                            }
                        }

                        echo ' | '.$errorMsg.PHP_EOL;
                        $errorsShown++;
                    }//end foreach
                }//end foreach
            }//end foreach

            echo str_repeat('-', $width).PHP_EOL.PHP_EOL;
        }//end foreach

        return $errorsShown;

    }//end generate()


}//end class

?>
